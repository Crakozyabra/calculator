package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Pattern;

public class Main {
    private static final Set<String> operators = Set.of("+", "-", "*", "/");
    private static final String ROMAN_REG_EXP = "[IVX]+";

    public static void main(String[] args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Input:");
                System.out.printf("Output:\n%s\n", calc(reader.readLine()));
            }
        }
    }

    public static String calc(String input) throws Exception {
        String[] split = input.trim().split(" ");
        if (split.length == 1 || split.length == 2) {
            throw new Exception("Строка не является математической операцией");
        } else if (split.length != 3) {
            throw new Exception("Формат математической операции не удовлетворяет заданию - два операнда и один " +
                    "оператор (+, -, /, *), разделенных между друг другом пробелами");
        } else if (!operators.contains(split[1].trim())) {
            throw new Exception("Такая арифметическая операция не предусмотрена");
        }
        boolean firstOperandIsRomans = Pattern.matches(ROMAN_REG_EXP, split[0]);
        int firstOperand = parseOperand(split[0], firstOperandIsRomans);
        boolean secondOperandIsRomans = Pattern.matches(ROMAN_REG_EXP, split[2]);
        int secondOperand = parseOperand(split[2], secondOperandIsRomans);
        if ((firstOperandIsRomans && !secondOperandIsRomans) || (!firstOperandIsRomans && secondOperandIsRomans)) {
            throw new Exception("Используются одновременно разные системы счисления");
        }
        int result = computeOperationResult(firstOperand, secondOperand, split[1]);
        if (firstOperandIsRomans) {
            if (result < 0) {
                throw new Exception("В римской системе нет отрицательных чисел");
            } else if (result == 0) {
                return "";
            }
            return IntegerConverter.intToRoman(result);
        }
        return String.valueOf(result);
    }

    private static int parseOperand(String operand, boolean isRomans) throws Exception {
        int parsed;
        if (isRomans) {
            parsed = Roman.valueOf(operand).ordinal() + 1;
        } else {
            parsed = Integer.parseInt(operand);
            if (parsed < 1 || parsed > 10) {
                throw new Exception("Калькулятор должен принимать на вход числа от 1 до 10 включительно, не более");
            }
        }
        return parsed;
    }

    private static int computeOperationResult(int firstOperand, int secondOperand, String operator) throws Exception {
        return switch (operator) {
            case "+" -> firstOperand + secondOperand;
            case "-" -> firstOperand - secondOperand;
            case "*" -> firstOperand * secondOperand;
            case "/" -> firstOperand / secondOperand;
            default -> throw new Exception("Оператор не поддерживается");
        };
    }
}