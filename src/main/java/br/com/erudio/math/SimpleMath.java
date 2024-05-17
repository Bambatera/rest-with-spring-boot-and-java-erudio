package br.com.erudio.math;

import br.com.erudio.exceptions.UnsupportedMathOperationException;

public class SimpleMath {

    public static Double sum(Double numberOne, Double numberTwo) {
        return numberOne + numberTwo;
    }

    public static Double subtraction(Double numberOne, Double numberTwo) {
        return numberOne - numberTwo;
    }

    public static Double multiplication(Double numberOne, Double numberTwo) {
        return numberOne * numberTwo;
    }

    public static Double division(Double numberOne, Double numberTwo) throws Exception {
        if (numberTwo <= 0d) {
            throw new UnsupportedMathOperationException("Please set a value gratter than zero!");
        }

        return numberOne / numberTwo;
    }

    public static Double average(Double numberOne, Double numberTwo) {
        return (numberOne + numberTwo) / 2;
    }

    public static Double squareRoot(Double number) {
        return Math.sqrt(number);
    }

}
