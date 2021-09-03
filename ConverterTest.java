package com.company;

import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("decimalToBinaryFloat")
    void decimalToBinaryFloat() {
        Converter converter = new Converter();
        // test float
        assertEquals("01000100001010001110100000000000", converter.decimalToBinaryFloat("675.625"));
        assertEquals("01000011101101001001111010111000", converter.decimalToBinaryFloat("361.24"));
        assertEquals("11000010100001100011010010111100", converter.decimalToBinaryFloat("-67.103"));
        assertEquals("00111101001000111101011100001010", converter.decimalToBinaryFloat("0.04"));
        assertEquals("10111100101011100111110101010110", converter.decimalToBinaryFloat("-0.0213"));
        assertEquals("00000000000000000000000000000000", converter.decimalToBinaryFloat("0.00"));
        assertEquals("10000000000000000000000000000000", converter.decimalToBinaryFloat("-0.00"));

    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("binaryToDecimalFloat")
    void binaryToDecimalFloat() {
        Converter converter = new Converter();
        // test float
        assertEquals(675.625, Double.parseDouble(converter.binaryToDecimalFloat("01000100001010001110100000000000")), 0.0001);
        assertEquals(361.24, Double.parseDouble(converter.binaryToDecimalFloat("01000011101101001001111010111000")), 0.001);
        assertEquals(-67.103, Double.parseDouble(converter.binaryToDecimalFloat("11000010100001100011010010111100")), 0.0001);
        assertEquals(0.04, Double.parseDouble(converter.binaryToDecimalFloat("00111101001000111101011100001010")), 0.0001);
        assertEquals(-0.0213, Double.parseDouble(converter.binaryToDecimalFloat("10111100101011100111110101010110")), 0.00001);
        assertEquals(0.0, Double.parseDouble(converter.binaryToDecimalFloat("00000000000000000000000000000000")), 0.001);
        assertEquals(-0.0, Double.parseDouble(converter.binaryToDecimalFloat("10000000000000000000000000000000")), 0.001);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("decimalToBinaryInt")
    void decimalToBinaryInt() {
        Converter converter = new Converter();
        // test float
        assertEquals("00000111", converter.decimalToBinaryInt("7", 8));
        assertEquals("11111001", converter.decimalToBinaryInt("-7", 8));
        assertEquals("00111111", converter.decimalToBinaryInt("63", 8));
        assertEquals("11000001", converter.decimalToBinaryInt("-63", 8));
        assertEquals("0000001000111011", converter.decimalToBinaryInt("571", 16));
        assertEquals("1111101111011010", converter.decimalToBinaryInt("-1062", 16));
        assertEquals("0000", converter.decimalToBinaryInt("0", 4));
        assertEquals("0000", converter.decimalToBinaryInt("-0", 4));
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("binaryToDecimalInt")
    void binaryToDecimalInt() {
        Converter converter = new Converter();
        // test float
        assertEquals("7", converter.binaryToDecimalInt("00000111"));
        assertEquals("-7", converter.binaryToDecimalInt("11111001"));
        assertEquals("63", converter.binaryToDecimalInt("00111111"));
        assertEquals("-63", converter.binaryToDecimalInt("11000001"));
        assertEquals("571", converter.binaryToDecimalInt("0000001000111011"));
        assertEquals("-1062", converter.binaryToDecimalInt("1111101111011010"));
        assertEquals("0", converter.binaryToDecimalInt("0000"));
    }
}