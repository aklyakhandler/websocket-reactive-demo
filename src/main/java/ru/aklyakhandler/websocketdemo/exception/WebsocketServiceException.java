package ru.aklyakhandler.websocketdemo.exception;

public class WebsocketServiceException extends RuntimeException {
    public WebsocketServiceException() {
        super();
    }

    public WebsocketServiceException(String message) {
        super(message);
    }

    public WebsocketServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebsocketServiceException(Throwable cause) {
        super(cause);
    }

    protected WebsocketServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
