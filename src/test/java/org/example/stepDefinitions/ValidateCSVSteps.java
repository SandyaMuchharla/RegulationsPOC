package org.example.stepDefinitions;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.utils.DataValidator;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.util.*;


public class ValidateCSVSteps {
    private boolean hasDuplicateColumns;
    private File folder;
    private String fileName;
    private final Map<String, String> columnDataTypes = new HashMap<>();
    private final SoftAssert softAssert = new SoftAssert();
    private final String resource = "src/test/resources/";

    @Given("I have CSV files")
    public void iHaveCSVFiles() {
        folder = new File(resource + "files");
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Directory does not exist or is not a directory.");
            return;
        }
    }

    @When("I check for duplicate columns")
    public void iCheckForDuplicateColumns() {
        File[] files = folder.listFiles((dir, name) -> name.startsWith("y") && name.endsWith(".csv"));
        if (files != null && files.length > 0) {
            for (File file : files) {
                System.out.println("Reading file: " + file.getName());
                fileName = file.getName();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String headerLine = br.readLine();
                    if (headerLine != null) {
                        String[] columns = headerLine.split(",");
                        System.out.println("Columns: " + Arrays.toString(columns));
                        Set<String> columnSet = new HashSet<>();
                        for (String column : columns) {
                            if (!columnSet.add(column)) {
                                hasDuplicateColumns = true;
                                return;
                            }
                        }
                        hasDuplicateColumns = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    hasDuplicateColumns = true;
                }
            }
        } else {
            System.out.println("No CSV files starting with 'y' found in the directory.");
        }

    }

    @Then("The file should not have duplicate columns")
    public void theFileShouldNotHaveDuplicateColumns() {
        Assert.assertFalse(hasDuplicateColumns, String.format("The file %s has duplicate columns!", fileName));
    }

    @Given("the data definitions file {string}")
    public void loadDataDefinitionsFile(String filePath) throws IOException {
        //Map<String, String> columnDataTypes = new HashMap<>();
        FileInputStream file = new FileInputStream(resource + "requirements/" + filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheet("Field Definitions");

        for (Row row : sheet) {
            Cell columnNameCell = row.getCell(0);
            Cell columnTypeCell = row.getCell(2);

            if (columnNameCell != null && columnTypeCell != null) {
                columnDataTypes.put(columnNameCell.getStringCellValue().trim(),
                        columnTypeCell.getStringCellValue().trim());
            }
        }
        workbook.close();
    }

    @When("I identify the columns and their expected datatypes")
    public void identifyExpectedDataTypes() {
        Assert.assertFalse(columnDataTypes.isEmpty(), "No columns found in data definitions!");
    }

    @Then("I verify that CSV data matches the expected datatype")
    public void validateDataTypes() throws FileNotFoundException {
        folder = new File("src/test/resources/files");
        File[] files = folder.listFiles((dir, name) -> name.startsWith("y") && name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            for (File file : files) {
                System.out.println("Reading file: " + file.getName());
                fileName = file.getName();
                List<String[]> reportData = new ArrayList<>();
                try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
                    reportData = csvReader.readAll();
                } catch (IOException | CsvException e) {
                    throw new RuntimeException(e);
                }

                String[] headers = reportData.get(0);  // Column names
                DataValidator dataValidator = new DataValidator();
                for (int i = 1; i < reportData.size(); i++) {
                    String[] row = reportData.get(i);
                    for (int j = 0; j < headers.length; j++) {
                        String columnName = headers[j].trim();
                        String expectedType = columnDataTypes.get(fileName.substring(0, fileName.lastIndexOf(".")) + "." + columnName.substring(1));
                        String value = row[j].trim();

                        if (expectedType == null) {
                            softAssert.assertTrue(false, "Filename " + fileName + " - Row " + i + " - Column: " + columnName + " - Value: " + value + " filename entries are not found in excel: " + expectedType);
                            continue; // Skip if column not defined in Excel
                        }
                        boolean isValid = false;
                        switch (expectedType) {
                            case "Alphanumerical":
                            case "Closed set of options":
                                isValid = dataValidator.isValidAlphanumeric(value);
                                break;
                            case "Date":
                                isValid = dataValidator.isValidDate(value);
                                break;
                            case "Country":
                                isValid = dataValidator.isValidCountry(value);
                                break;
                            case "Currency":
                                isValid = dataValidator.isValidCurrency(value);
                                break;
                            case "Monetary":
                                isValid = dataValidator.isValidMonetaryValue(value);
                                break;
                            default:
                                isValid = true; // Assume valid if datatype is not explicitly checked
                                break;
                        }
                        softAssert.assertTrue(isValid, "Filename " + fileName + " - Row " + i + " - Column: " + columnName + " - Value: " + value + " does not match expected type: " + expectedType);
                        //Assert.assertTrue(isValid, "Invalid " + expectedType + " data: " + value);
                    }
                }
            }
        }

        softAssert.assertAll();
    }

}