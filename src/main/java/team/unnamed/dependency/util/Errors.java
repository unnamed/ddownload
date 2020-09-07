package team.unnamed.dependency.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringJoiner;

/**
 * Collection of util methods for
 * easy {@link Throwable} handling.
 */
public final class Errors {

    private Errors() {
        throw new UnsupportedOperationException();
    }

    /**
     * Formats many throwables with an enumeration and
     * all the stack traces.
     * @param message The main message
     * @param throwables The throwables
     * @return The formatted throwables
     */
    public static String format(String message, Throwable... throwables) {

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(message);

        for (int i = 0; i < throwables.length; i++) {
            int index = i + 1;
            Throwable throwable = throwables[i];
            joiner.add(
                    index + ") " + getStackTrace(throwable)
            );
        }

        return joiner.toString();
    }

    /**
     * Gets the stack trace of a specified throwable.
     * @param throwable The throwable
     * @return The throwable's stack trace
     */
    public static String getStackTrace(Throwable throwable) {

        Validate.notNull(throwable, "throwable");

        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        throwable.printStackTrace(printWriter);
        String stackTrace = writer.toString();

        printWriter.flush();
        printWriter.close();

        return stackTrace;
    }

}
