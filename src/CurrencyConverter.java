import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
/*
   to implement org.json, following steps were taken
   (File) -> (Project Structure) -> (select source) -> (dependencies) -> (+) ->
   -> (search for org.json or org.json:json) -> (select org.json:json:20250517) -> (ok [or] apply)
 */

public class CurrencyConverter {

    public static void main(String[] args) {
        System.out.println("=== Currency Converter ===");
        System.out.println("Real-time exchange rates using ExchangeRate-API");
        System.out.println();

        try { //exception handling which is important to identify, log, and diagnose issues that may arise while using external API i.e. (ExchangeRate-API)
            while (true) {
                conversion();
                System.out.println("\nDo you want to perform another conversion? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y") && !choice.equals("yes")) {
                    break;
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

        System.out.println("Thank you for using Currency Converter!");
    }

    static final String API_URL = "https://api.exchangerate-api.com/v4/latest/"; // latest version of a reliable external API (ExchangeRate-API)
    static final Scanner scanner = new Scanner(System.in);

    // using hashmaps to store different global currencies and their respective official symbols in key-value pairs
    // taking top 15 global currencies as valid inputs for currency conversion
    static final Map<String, String> CURRENCY_SYMBOLS = new HashMap<String, String>() {{
        put("INR", "Indian Rupee (₹)");
        put("USD", "US Dollar ($)");
        put("EUR", "Euro (€)");
        put("JPY", "Japanese Yen (¥)");
        put("SGD", "Singapore Dollar (S$)");
        put("GBP", "British Pound (£)");
        put("CAD", "Canadian Dollar (C$)");
        put("AUD", "Australian Dollar (A$)");
        put("CHF", "Swiss Franc (CHF)");
        put("CNY", "Chinese Yuan (¥)");
        put("KRW", "South Korean Won (₩)");
        put("MXN", "Mexican Peso ($)");
        put("BRL", "Brazilian Real (R$)");
        put("RUB", "Russian Ruble (₽)");
        put("ZAR", "South African Rand (R)");

    }};

    static void conversion() {
        try {
            availableCurrencies(); // Display available currencies
            String baseCurrency = getCurrencyInput("Enter base currency code: "); // Get base currency
            String targetCurrency = getCurrencyInput("Enter target currency code: "); // Get target currency
            double amount = getAmountInput();// Get amount to convert

            System.out.println("Fetching real-time exchange rates...");

            double exchangeRate = exchangeRate(baseCurrency, targetCurrency);

            if (exchangeRate == -1) {
                System.out.println("Failed to fetch exchange rate. Please try again.");
                return;
            }

            // Perform conversion
            double convertedAmount = amount * exchangeRate;

            // Display results
            displayResults(amount, baseCurrency, convertedAmount, targetCurrency, exchangeRate);

        } catch (Exception e) {
            System.err.println("Error during conversion: " + e.getMessage());
        }
    }

    static void availableCurrencies() {
        System.out.println("Available currencies:");
        System.out.println("====================");
        for (Map.Entry<String, String> entry : CURRENCY_SYMBOLS.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
        System.out.println("(You can also use other currency codes not listed above)");
        System.out.println();
    }

    static String getCurrencyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String currency = scanner.nextLine().trim().toUpperCase();

            if (currency.length() == 3 && currency.matches("[A-Z]{3}")) {
                return currency;
            } else {
                System.out.println("Please enter a valid 3-letter currency code (e.g., USD, EUR).");
            }
        }
    }

    static double getAmountInput() {
        while (true) {
            System.out.print("Enter amount to convert: ");
            try {
                double amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount <= 0) {
                    System.out.println("Please enter a positive amount.");
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static double exchangeRate(String baseCurrency, String targetCurrency) {
        try {
            // If same currency, return 1
            if (baseCurrency.equals(targetCurrency)) {
                return 1.0;
            }

            // Build API URL
            String urlString = API_URL + baseCurrency;
            URL url = new URL(urlString);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "CurrencyConverter/1.0");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                System.err.println("API request failed with response code: " + responseCode);
                return -1;
            }

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject rates = jsonResponse.getJSONObject("rates");

            if (!rates.has(targetCurrency)) {
                System.err.println("Target currency '" + targetCurrency + "' not found in exchange rates.");
                return -1;
            }

            return rates.getDouble(targetCurrency);

        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.err.println("Error parsing exchange rate data: " + e.getMessage());
            return -1;
        }
    }

    static void displayResults(double amount, String baseCurrency, double convertedAmount,
                               String targetCurrency, double exchangeRate) {
        System.out.println("=== Conversion Result ===");
        System.out.printf("Amount: %.2f %s%n", amount, currencyName(baseCurrency));
        System.out.printf("Exchange Rate: 1 %s = %.6f %s%n", baseCurrency, exchangeRate, targetCurrency);
        System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, currencyName(targetCurrency));

        // Display currency symbols
        String baseSymbol = currencySymbols(baseCurrency);
        String targetSymbol = currencySymbols(targetCurrency);

        if (!baseSymbol.isEmpty() && !targetSymbol.isEmpty()) {
            System.out.printf("Display: %s%.2f → %s%.2f%n", baseSymbol, amount, targetSymbol, convertedAmount);
        }

        System.out.println("========================");
    }

    static String currencyName(String currencyCode) {
        return CURRENCY_SYMBOLS.getOrDefault(currencyCode, currencyCode);
    }

    static String currencySymbols(String currencyCode) {
        return switch (currencyCode) { //enhanced switch case
            case "USD", "CAD", "AUD", "MXN" -> "$";
            case "EUR" -> "€";
            case "GBP" -> "£";
            case "JPY", "CNY" -> "¥";
            case "CHF" -> "CHF ";
            case "INR" -> "₹";
            case "KRW" -> "₩";
            case "BRL" -> "R$";
            case "RUB" -> "₽";
            case "ZAR" -> "R";
            case "SGD" -> "S$";
            default -> "";
        };
    }
}
