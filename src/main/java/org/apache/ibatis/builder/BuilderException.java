package org.apache.ibatis.builder;

/**
 * @author furious 2024/4/7
 */
public class BuilderException extends RuntimeException {

    public BuilderException() {
        super();
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }

    protected BuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
