package codingdojo;

import static java.util.Arrays.stream;

class MessageEnricher {

    ErrorResult enrichError(SpreadsheetWorkbook spreadsheetWorkbook, Exception e) {
        String formulaName = spreadsheetWorkbook.getFormulaName();
        String error;
        if (e instanceof ExpressionParseException)
            error = "Invalid expression found in tax formula [" + formulaName + "]. Check that separators and delimiters use the English locale.";
        else if (e.getMessage().startsWith("Circular Reference") && e instanceof SpreadsheetException)
            error = "Circular Reference in spreadsheet related to formula '" + formulaName + "'. Cells: " + ((SpreadsheetException) e).getCells();
        else if ("Object reference not set to an instance of an object".equals(e.getMessage()) && stackTraceContainsVLookup(e))
            error = "Missing Lookup Table";
        else if ("No matches found".equals(e.getMessage()) && e instanceof SpreadsheetException)
            error = "No match found for token [" + ((SpreadsheetException) e).getToken() + "] related to formula '" + formulaName + "'.";
        else
            error = e.getMessage();
        return new ErrorResult(formulaName, error, spreadsheetWorkbook.getPresentation());
    }

    private boolean stackTraceContainsVLookup(Exception e) {
        return stream(e.getStackTrace()).anyMatch(s -> s.getMethodName().contains("vLookup"));
    }

}
