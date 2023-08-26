package io.github.vvcogo.longfastbloomfilter.framework.extensions;

public class ExtensionLoadException extends Exception {

    public ExtensionLoadException() {

    }

    public ExtensionLoadException(String message) {
        super(message);
    }

    public ExtensionLoadException(Throwable throwable) {
        super(throwable);
    }

    public ExtensionLoadException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
