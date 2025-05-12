package couplesmatching;

public class InvalidInputException extends Exception{
    private String malformedData;

    public InvalidInputException(String message, String malformedData) {
        super(message);
        this.malformedData = malformedData;
    }

    public String getMalformedData() {
        return malformedData;
    }
}
