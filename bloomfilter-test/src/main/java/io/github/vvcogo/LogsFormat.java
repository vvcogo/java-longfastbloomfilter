package io.github.vvcogo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

final class LogsFormat {

    private static final String ASCII_RED = "\u001B[31m";
    private static final String ASCII_YELLOW = "\u001B[33m";
    private static final String ASCII_WHITE = "\u001B[37m";
    private static final String ASCII_RESET = "\u001B[0m";

    public static Formatter CONSOlE = new ConsoleFormatter();
    public static Formatter FILE = new FileFormatter();

    private static String getAsciiColor(Level level) {
        if (level.equals(Level.SEVERE))
            return ASCII_RED;
        if (level.equals(Level.WARNING))
            return ASCII_YELLOW;
        return ASCII_WHITE;
    }

    private static class ConsoleFormatter extends Formatter {

        private static final String CONSOLE_FORMAT = "%s[%s] %s - %s%s" + System.lineSeparator();

        @Override
        public String format(LogRecord record) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
            String formattedDate = dateFormatter.format(new Date(record.getMillis()));
            Level level = record.getLevel();
            String message = record.getMessage();
            return String.format(CONSOLE_FORMAT, getAsciiColor(level), formattedDate, level, message, ASCII_RESET);
        }
    }

    private static class FileFormatter extends Formatter {

        private static final String FILE_FORMAT = "[%s] %s - %s" + System.lineSeparator();

        @Override
        public String format(LogRecord record) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
            String formattedDate = dateFormatter.format(new Date(record.getMillis()));
            Level level = record.getLevel();
            String message = record.getMessage();
            return String.format(FILE_FORMAT, formattedDate, level, message);
        }
    }
}
