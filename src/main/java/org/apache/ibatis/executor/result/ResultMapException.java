package org.apache.ibatis.executor.result;

/**
 * @author furious 2024/4/11
 */
public class ResultMapException extends RuntimeException{

    public ResultMapException() {
        super();
    }

    public ResultMapException(String message) {
        super(message);
    }

    public ResultMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultMapException(Throwable cause) {
        super(cause);
    }

    protected ResultMapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
