package org.jquransearch.core.error;


public class JQuranSearchException extends RuntimeException {
    public JQuranSearchException(String message) {
        super(message);
    }

    public JQuranSearchException(String message, Exception exception) {
        super(message, exception);
    }
}
