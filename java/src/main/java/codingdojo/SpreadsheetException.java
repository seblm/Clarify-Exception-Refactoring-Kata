package codingdojo;


import java.util.List;

/**
 * This kind of exception is thrown when something bad
 * happens when evaluating the calculation formulas in a
 * SpreadsheetWorkbook
 */
class SpreadsheetException extends Exception {
    private List<String> cells;
    private String token;

    SpreadsheetException(String message, List<String> cells, String token) {
        super(message);
        this.cells = cells;
        this.token = token;
    }

    List<String> getCells() {
        return cells;
    }

    String getToken() {
        return token;
    }
}
