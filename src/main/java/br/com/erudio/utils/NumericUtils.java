package br.com.erudio.utils;

public class NumericUtils {

    public static boolean isNumeric(String strNumber) {
        if (strNumber == null || strNumber.trim().isEmpty()) {
            return false;
        }
        String number = strNumber.replaceAll(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double convertToDouble(String strNumber) {
        if (strNumber == null) {
            return 0d;
        }
        String number = strNumber.replaceAll(",", ".");
        if (isNumeric(number)) {
            return Double.valueOf(number);
        }
        return 0d;
    }
}
