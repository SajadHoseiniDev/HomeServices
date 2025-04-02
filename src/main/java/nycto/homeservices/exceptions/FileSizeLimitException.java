package nycto.homeservices.exceptions;

public class FileSizeLimitException extends RuntimeException{
    public FileSizeLimitException(String message){
        super(message);
    }
}
