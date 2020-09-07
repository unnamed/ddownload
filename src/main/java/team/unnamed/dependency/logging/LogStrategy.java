package team.unnamed.dependency.logging;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

/**
 * Abstract Logger for log messages.
 */
public interface LogStrategy {

    /**
     * Logs information
     * @param message The information message
     */
    void info(String message);

    /**
     * Sends a warning to the log receiver
     * @param message The warning message
     */
    void warning(String message);

    /**
     * Sends an error to the log receiver
     * @param message The error message
     */
    void error(String message);

    /**
     * Returns a silent log strategy.
     * It doesn't log the messages
     * @return The log strategy
     */
    static LogStrategy getSilent() {
        return SilentLogStrategy.INSTANCE;
    }

    /**
     * Returns a strategy that uses
     * the specified logger.
     * @param logger The logger
     * @return The log strategy
     */
    static LogStrategy getUsing(Logger logger) {
        return new LoggerStrategy(logger);
    }

    /**
     * Returns a strategy that uses
     * {@link System#out} and {@link System#err} to
     * log messages.
     * @return The log strategy
     */
    static LogStrategy getDefault() {
        return getDefault("[%s] %s: %s");
    }

    /**
     * Returns a strategy that uses
     * {@link System#out} and {@link System#err} to
     * log messages. Also uses the specified
     * log format.
     * @param format The log format
     * @return The log strategy
     */
    static LogStrategy getDefault(String format) {
        return getDefault("HH:mm:ss", format);
    }

    /**
     * Returns a strategy that uses
     * {@link System#out} and {@link System#err} to
     * log messages. Also uses the specified
     * date format and log format.
     * @param dateFormat The date format
     * @param format The log format
     * @return The strategy
     */
    static LogStrategy getDefault(String dateFormat, String format) {
        return new DefaultLogStrategy(new SimpleDateFormat(dateFormat), format);
    }

}
