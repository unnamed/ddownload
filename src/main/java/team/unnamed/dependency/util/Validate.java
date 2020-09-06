package team.unnamed.dependency.util;

/**
 * Collection of util methods for
 * validate arguments, states,
 * and more objects.
 */
public final class Validate {

    private Validate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if the provided string is
     * null or empty. If the string
     * is null or empty, the method
     * results in a {@link IllegalArgumentException}
     * being thrown.
     * @param string The checked string
     * @return The string, if not null
     * and not empty.
     */
    public static String notEmpty(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalArgumentException("Provided argument is null or empty!");
        }
        return string;
    }

    /**
     * Checks if all elements of the provided
     * array aren't empty and not null. Also
     * checks if the array isn't empty.
     * If the array is empty, or an element
     * is null or empty, the method results
     * in a {@link IllegalArgumentException}
     * being thrown.
     * @param array The checked array
     * @return The array, if not empty, and all
     * elements not null and not empty.
     */
    public static String[] eachNotEmpty(String[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Provided array is empty");
        }
        for (int i = 0; i < array.length; i++) {
            String string = array[i];
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException("Element at index " + i + " in the"
                        + " provided array is null or an empty string");
            }
        }
        return array;
    }

}
