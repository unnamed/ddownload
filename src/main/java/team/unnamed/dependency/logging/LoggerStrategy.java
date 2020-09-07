package team.unnamed.dependency.logging;

import team.unnamed.dependency.util.Validate;

import java.util.logging.Logger;

/**
 * Uses a {@link Logger} to log messages
 */
class LoggerStrategy implements LogStrategy {

    private final Logger logger;

    LoggerStrategy(Logger logger) {
        this.logger = Validate.notNull(logger, "logger");
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warning(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message) {
        logger.warning(message);
    }

}
