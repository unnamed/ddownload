package team.unnamed.dependency.logging;

import team.unnamed.dependency.util.Errors;
import team.unnamed.dependency.util.Validate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A {@link LogStrategy} implementation that uses
 * {@link System#out} and {@link System#err} to
 * print logs.
 */
class DefaultLogStrategy implements LogStrategy {

    private final SimpleDateFormat dateFormat;
    private final Date date = new Date();
    private final String format;

    DefaultLogStrategy(SimpleDateFormat dateFormat, String format) {
        this.dateFormat = dateFormat;
        this.format = Validate.notEmpty(format);
    }

    @Override
    public void info(String message) {
        System.out.println(formatMessage("INFO", message));
    }

    @Override
    public void warning(String message, Throwable... errors) {
        if (errors.length > 0) {
            System.err.println(
                    formatMessage("WARNING", Errors.format(message, errors))
            );
        } else {
            System.out.println(formatMessage("WARNING", message));
        }
    }

    @Override
    public void error(String message, Throwable... errors) {
        if (errors.length > 0) {
            System.err.println(
                    formatMessage("ERROR", Errors.format(message, errors))
            );
        } else {
            System.err.println(formatMessage("ERROR", message));
        }
    }

    private String formatMessage(String level, String message) {

        date.setTime(System.currentTimeMillis());
        return String.format(
                format,
                dateFormat.format(date),
                level,
                message
        );
    }

}
