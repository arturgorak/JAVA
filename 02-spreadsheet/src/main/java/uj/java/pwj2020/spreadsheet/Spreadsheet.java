package uj.java.pwj2020.spreadsheet;

public class Spreadsheet {
    public CellAddress stringToCellAddress(String input){
        return new CellAddress(Integer.parseInt(input.substring(2)) - 1, input.charAt(1) - 'A');
    }

    public int valueFromCellAddress(String word, String[][] input) {
        if (word.charAt(0) == '$') {
            return Integer.parseInt(input[stringToCellAddress(word).x()][stringToCellAddress(word).y()]);
        } else {
            return Integer.parseInt(word);
        }
    }

    public void referenceProcessing(String[][] input, int x, int y){
        while (input[x][y].charAt(0) == '$'){
            CellAddress cellAddress = stringToCellAddress(input[x][y]);
            input[x][y] = input[cellAddress.x()][cellAddress.y()];
        }
    }

    public void expressionProcessing(String[][] input, int x, int y){
        if (input[x][y].charAt(0) == '=') {

            String firstStringValue = input[x][y].substring(input[x][y].indexOf('(') + 1, input[x][y].indexOf(','));
            String secondStringValue = input[x][y].substring(input[x][y].indexOf(',') + 1, input[x][y].length() - 1);
            int firstValue = valueFromCellAddress(firstStringValue, input);
            int secondValue = valueFromCellAddress(secondStringValue, input);

            switch (input[x][y].substring(1, input[x][y].indexOf('('))) {
                case "ADD" -> input[x][y] = String.valueOf(firstValue + secondValue);
                case "SUB" -> input[x][y] = String.valueOf(firstValue - secondValue);
                case "MUL" -> input[x][y] = String.valueOf(firstValue * secondValue);
                case "DIV" -> input[x][y] = String.valueOf(firstValue / secondValue);
                case "MOD" -> input[x][y] = String.valueOf(firstValue % secondValue);
            }
        }
    }

    public String[][] calculate(String[][] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                referenceProcessing(input, i, j);
                expressionProcessing(input, i, j);
            }
        }
        return input;
    }
}