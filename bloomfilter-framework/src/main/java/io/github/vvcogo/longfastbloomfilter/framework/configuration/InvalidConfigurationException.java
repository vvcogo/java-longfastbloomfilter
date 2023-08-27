package io.github.vvcogo.longfastbloomfilter.framework.configuration;

public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException() {
    }

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
