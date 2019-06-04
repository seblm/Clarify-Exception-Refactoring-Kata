package codingdojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageEnricherTest {

    private MessageEnricher enricher;

    private SpreadsheetWorkbook worksheet;

    @BeforeEach
    void init() {
        enricher = new MessageEnricher();
        worksheet = new SpreadsheetWorkbook();
    }

    @Test
    void should_returns_ErrorResult_with_message_as_is_for_a_RuntimeException() {
        String message = "Terrible problem";
        Exception e = new RuntimeException(message);

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals(worksheet.getFormulaName(), actual.getFormulaName());
        assertEquals(message, actual.getMessage());
        assertEquals(worksheet.getPresentation(), actual.getPresentation());
    }

    @Test
    void should_returns_ErrorResult_with_custom_message_for_an_ExpressionParseException() {
        Exception e = new ExpressionParseException();

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals("Invalid expression found in tax formula [" + worksheet.getFormulaName() + "]. Check that separators and delimiters use the English locale.", actual.getMessage());
    }

    @Test
    void should_returns_ErrorResult_with_message_as_is_for_an_Exception_with_message_starting_with_Circular_Reference() {
        String message = "Circular Reference into a RuntimeException";
        Exception e = new RuntimeException(message);

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals(message, actual.getMessage());
    }

    @Test
    void should_returns_ErrorResult_with_custom_message_for_a_SpreadsheetException_with_message_starting_with_Circular_Reference() {
        Exception e = new SpreadsheetException("Circular Reference into a SpreadsheetException", asList("A3", "B5"), "useless token");

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals("Circular Reference in spreadsheet related to formula '" + worksheet.getFormulaName() + "'. Cells: [A3, B5]", actual.getMessage());
    }

    @Test
    void should_returns_ErrorResult_with_custom_message_for_an_Exception_with_specific_message_and_with_vLookup_into_stacktrace() {
        String message = "Object reference not set to an instance of an object";
        Exception e = new RuntimeException(message);
        e.setStackTrace(new StackTraceElement[]{new StackTraceElement("useless class", "vLookup", "useless fileName", 0)});

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals("Missing Lookup Table", actual.getMessage());
    }

    @Test
    void should_returns_ErrorResult_with_message_as_is_for_an_Exception_with_specific_message_and_without_vLookup_into_stacktrace() {
        String message = "Object reference not set to an instance of an object";
        Exception e = new RuntimeException(message);
        e.setStackTrace(new StackTraceElement[0]);

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals(message, actual.getMessage());
    }

    @Test
    void should_returns_ErrorResult_with_message_as_is_for_an_Exception_with_No_matches_found_message() {
        String message = "No matches found";
        Exception e = new RuntimeException(message);

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals(message, actual.getMessage());
    }

    @Test
    void should_returns_ErrorResult_with_custom_message_for_a_SpreadsheetException_with_No_matches_found_message() {
        Exception e = new SpreadsheetException("No matches found", emptyList(), "some token");

        ErrorResult actual = enricher.enrichError(worksheet, e);

        assertEquals("No match found for token [some token] related to formula '" + worksheet.getFormulaName() + "'.", actual.getMessage());
    }

}
