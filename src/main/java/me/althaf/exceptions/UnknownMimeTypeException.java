package me.althaf.exceptions;

public class UnknownMimeTypeException extends RuntimeException {

    public UnknownMimeTypeException(){
        super("Unknown MimeType");
    }
}
