package com.simpleworkshopsoftware.pdfGenerator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
/**
 * PdfUtils is a utility class for formatting numbers in various ways, specifically for
 * generating formatted strings for kilometers, general numbers, and remove tax from prices.
 * It includes methods to format integers and strings representing numbers, utilizing
 * custom decimal formatting with specified grouping separators and fraction digits.
 *
 * Author: Attila Eckert
 * Date: 12/27/2024
 * Version: 1.0
 */
public class PdfUtils {

    private PdfUtils() {}

    public static String kmNumberFormat(int number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        df.setMaximumFractionDigits(2);
        return df.format(number);
    }

    public static String numberFormat(String number) {
        if (number.isEmpty()) {
            return "";
        }
        double result = Double.parseDouble(number);
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(result);
    }


    public static String priceWithoutTax(String number) {
        if (number.isEmpty()) {
            return "";
        }
        double result = Double.parseDouble(number) / 1.27;
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(result);
    }
}
