package org.java.lessons.springilmiofotoalbum.exception;

public class PhotoNotFoundException extends RuntimeException {

    public PhotoNotFoundException(String message) {
        super(message);
    }
}
