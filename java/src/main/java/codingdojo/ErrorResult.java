package codingdojo;

class ErrorResult {
    private final String formulaName;
    private final String message;
    private final String presentation;

    ErrorResult(String formulaName, String message, String presentation) {

        this.formulaName = formulaName;
        this.message = message;
        this.presentation = presentation;
    }

    String getFormulaName() {
        return formulaName;
    }

    String getMessage() {
        return message;
    }

    String getPresentation() {
        return presentation;
    }
}
