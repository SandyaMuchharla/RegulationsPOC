Feature: Validate CSV File Columns

  Scenario: Ensure no column is reported twice
    Given I have CSV files
    When I check for duplicate columns
    Then The file should not have duplicate columns

  Scenario: Check if CSV data matches expected column data types
    Given the data definitions file "Data Fields_New.xlsx"
    When I identify the columns and their expected datatypes
    Then I verify that CSV data matches the expected datatype