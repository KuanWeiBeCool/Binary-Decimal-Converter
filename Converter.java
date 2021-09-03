package com.company;

/**
 * This class performs the conversion between 32-bit binary and decimal numbers.
 * For integers, it'll use 2C's complement. For float, it'll use IEEE-754.
 *
 * @author kuanw
 */
public class Converter {

    private final int fractionLegnth = 23;
    private final int exponentLength = 8;
    private final int integerLength = 32;

    /**
     * Convert a decimal float to a 32-bit binary value using IEEE-754 standards.
     *
     * @param value the float value to convert in String format
     * @return the binary value in String format
     */
    public String decimalToBinaryFloat(String value) {
        if (!value.contains(".")) {
            throw new IllegalArgumentException("The value " + value + " is not a float number!");
        }
        String result;
        if (value.contains("-")) {
            result = "1";
            // remove the minus sign
            value = value.substring(1);
            // Special case for -0.0
            if (Double.parseDouble(value) == 0.0) return "10000000000000000000000000000000";
        } else if (value.contains("+")) {
            result = "0";
            // remove the plus sign
            value = value.substring(1);
            // Special case for 0.0
            if (Double.parseDouble(value) == 0.0) return "00000000000000000000000000000000";
        } else {
            // by default, no plus or minus sign represents positive
            result = "0";
            // Special case for 0.0
            if (Double.parseDouble(value) == 0.0) return "00000000000000000000000000000000";
        }


        String[] values = value.split("\\.");
        int integralPart = Integer.parseInt(values[0]);
        double fractionalPart = Double.parseDouble("0." + values[1]);
        int exponent = 127;
        //////////////////////////////// fraction part //////////////////////////////////////////////
        // Convert integral part into binary
        String integralBinary = "";
        // Keep performing division by 2 until the value is 0. Record the residuals
        while (integralPart != 0.0) {
            int bit = integralPart % 2;
            // keep adding bit to the left
            integralBinary = String.valueOf(bit) + integralBinary;
            integralPart /= 2;
        }
        // Convert fractional part into binary
        String fractionalBinary = "";
        // +1 at the end because the first bit of the integral part won't count towards the final
        // fraction part in IEEE-754 standard
        int fractionalBinaryLength = fractionLegnth - integralBinary.length() + 1;
        // Keep performing multiplication by 2 until filled up all the available slots for fraction part
        while (fractionalBinary.length() < fractionalBinaryLength) {
            fractionalPart *= 2;
            if (fractionalPart >= 1.0) {
                fractionalBinary += "1";
                fractionalPart -= 1.0;
            } else {
                // if there is no integral part and we haven't got any 1 yet in the fractional part,
                // we need to decrement the exponent (since the binary point should move to the right)
                // and ignore the "0". Otherwise add "0"
                if (integralBinary.isEmpty() && !fractionalBinary.contains("1")) {
                    exponent -= 1;
                } else {
                    fractionalBinary += "0";
                }
            }
        }

        // Do not need the first bit
        String fractionIEEE = (integralBinary + fractionalBinary).substring(1);
        //////////////////////////////// exponent part //////////////////////////////////////////////
        // find the final exponent part
        exponent += (integralBinary.length() - 1);

        String exponentIEEE = "";
        // Keep performing division by 2 until the exponent part matches the length. Record the residuals
        while (exponentIEEE.length() < exponentLength) {
            int bit = exponent % 2;
            // keep adding bit to the left
            exponentIEEE = String.valueOf(bit) + exponentIEEE;
            exponent /= 2;
        }
        result = result + exponentIEEE + fractionIEEE;
        return result;
    }


    /**
     * Convert a  a 32-bit binary value using IEEE-754 standards to a decimal float
     *
     * @param value the binary value to convert in String format
     * @return the decimal float value in String format
     */
    public String binaryToDecimalFloat(String value) {
        if (value == "10000000000000000000000000000000") return "-0.0";
        if (value == "00000000000000000000000000000000") return "0.0";

        String signIEEE = value.substring(0, 1);
        String exponentIEEE = value.substring(1, 9);
        String fractionIEEE = value.substring(9);
        String result = "";
        if (signIEEE.equals("1")) {
            result += "-";
        }
        //////////////////////////////// exponent part //////////////////////////////////////////////
        // convert exponent into decimal integer
        int exponent = 0;
        int base = 1;
        for (int i = exponentIEEE.length() - 1; i >= 0; i--) {
            exponent += base * Character.getNumericValue(exponentIEEE.charAt(i));
            base *= 2;
        }
        // bias term
        exponent -= 127;

        //////////////////////////////// fraction part //////////////////////////////////////////////
        String integralBinary = "";
        String fractionalBinary = "";
        if (exponent >= 0) {
            integralBinary = "1" + fractionIEEE.substring(0, exponent);
            fractionalBinary = fractionIEEE.substring(exponent);
        } else {
            for (int i = 0; i < -exponent - 1; i++) {
                fractionalBinary += "0";
            }
            fractionalBinary = fractionalBinary + "1" + fractionIEEE;
        }

        // calculate the integral part
        int integralPart = 0;
        base = 1;
        for (int i = integralBinary.length() - 1; i >= 0; i--) {
            integralPart += base * Character.getNumericValue(integralBinary.charAt(i));
            base *= 2;
        }

        // calculate the fractional part
        double fractionalPart = 0.0;
        double baseFrac = 0.5;
        for (int i = 0; i < fractionalBinary.length(); i++) {
            fractionalPart += baseFrac * Character.getNumericValue(fractionalBinary.charAt(i));
            baseFrac /= 2;
        }
        result += String.valueOf(integralPart) + String.valueOf(fractionalPart).substring(1);
        return result;
    }

    /**
     * Convert a decimal integer to a binary value of given bit.
     *
     * @param value  the integer value to convert in String format
     * @param bitNum the bit length. e.g. 4-bit, 8-bit, 12-bit...
     * @return the binary value in String format
     */
    public String decimalToBinaryInt(String value, int bitNum) {
        boolean isNegative = false;
        int integer = Integer.parseInt(value);
        if (integer >= Math.pow(2, bitNum - 1) || integer < -Math.pow(2, bitNum - 1)) {
            throw new IllegalArgumentException("Value " + value + " cannot fit in " + bitNum + "-bits.");
        }
        if (integer < 0) {
            isNegative = true;
            // convert to positive first
            integer *= -1;
        }

        String integerBinary = "";
        // Keep performing division by 2 until the value is 0. Record the residuals
        while (integer != 0.0) {
            int bit = integer % 2;
            // keep adding bit to the left
            integerBinary = String.valueOf(bit) + integerBinary;
            integer /= 2;
        }

        // Place the remaining bits by 0
        while (integerBinary.length() < bitNum) {
            integerBinary = "0" + integerBinary;
        }

        // if negative, flip
        if (isNegative) {
            String newIntegerBinary = "";
            char currBit = '0';
            int index = integerBinary.length() - 1;
            // Keep the part from right to left until the first "1" (including this "1")
            while (currBit != '1') {
                currBit = integerBinary.charAt(index);
                newIntegerBinary = currBit + newIntegerBinary;
                index -= 1;
            }
            // Flip the rest
            for (int i = index; i >= 0; i--) {
                if (integerBinary.charAt(i) == '0') {
                    newIntegerBinary = "1" + newIntegerBinary;
                } else {
                    newIntegerBinary = "0" + newIntegerBinary;
                }
            }
            integerBinary = newIntegerBinary;
        }
        return integerBinary;
    }

    /**
     * Convert a binary value to a decimal integer.
     *
     * @param value  the binary value in String format
     * @return the integer value in String format
     */
    public String binaryToDecimalInt(String value) {
        boolean isNegative = false;
        if (value.startsWith("1")) isNegative = true;

        // if negative, flip
        if (isNegative) {
            String newValue = "";
            char currBit = '0';
            int index = value.length() - 1;
            // Keep the part from right to left until the first "1" (including this "1")
            while (currBit != '1') {
                currBit = value.charAt(index);
                newValue = currBit + newValue;
                index -= 1;
            }
            // Flip the rest
            for (int i = index; i >= 0; i--) {
                if (value.charAt(i) == '0') {
                    newValue = "1" + newValue;
                } else {
                    newValue = "0" + newValue;
                }
            }
            value = newValue;
        }

        int integer = 0;
        int base = 1;
        for (int i = value.length() - 1; i >= 0; i--) {
            integer += base * Character.getNumericValue(value.charAt(i));
            base *= 2;
        }
        if (isNegative) integer *= -1;
        return String.valueOf(integer);
    }
}
