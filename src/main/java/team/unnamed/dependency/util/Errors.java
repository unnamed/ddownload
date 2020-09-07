package team.unnamed.dependency.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Collection of util methods for
 * easy {@link Throwable} handling.
 */
public final class Errors {

    private Errors() {
        throw new UnsupportedOperationException();
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
