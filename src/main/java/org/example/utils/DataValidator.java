package org.example.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class DataValidator {
    private static final Set<String> VALID_CURRENCY_CODES = new HashSet<>(Arrays.asList(
            "USD", "GBP", "EUR", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD"
    ));

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-_.,]*$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");  // YYYY-MM-DD
    private static final Pattern COUNTRY_PATTERN = Pattern.compile("^[A-Za-z\\s]+$");
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("^[A-Z]{3}$");  // ISO 4217 codes
    private static final Pattern MONETARY_PATTERN = Pattern.compile("^[$£€]?[0-9]+(\\.[0-9]{1,2})?$");

    public boolean isValidAlphanumeric(String value) {
        return ALPHANUMERIC_PATTERN.matcher(value).matches();
    }

    public boolean isValidDate(String value) {
        return DATE_PATTERN.matcher(value).matches();
    }

    public boolean isValidCountry(String value) {
        return COUNTRY_PATTERN.matcher(value).matches();
    }

    public boolean isValidCurrency(String value) {
        return CURRENCY_PATTERN.matcher(value).matches() && VALID_CURRENCY_CODES.contains(value);
    }

    public boolean isValidMonetaryValue(String value) {
        return MONETARY_PATTERN.matcher(value).matches();
    }

}
