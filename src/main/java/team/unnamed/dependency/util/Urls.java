package team.unnamed.dependency.util;

/**
 * Collection of util methods for
 * easy URL handling.
 */
public final class Urls {

    private Urls() {
        throw new UnsupportedOperationException();
    }

    /**
     * Just adds a slash to the provided
     * text if the text doesn't end with
     * that slash.
     * @param url The url
     * @return The url with "/" at the end
     */
    public static String endWithSlash(String url) {
        return url.endsWith("/") ? url : (url + "/");
    }

    /**
     * Converts the dots '.' to slashes '/'
     * @param text The text
     * @return The converted text
     */
    public static String dotsToSlashes(String text) {

        char[] characters = text.toCharArray();

        for (int i = 0; i < characters.length; i++) {
            char current = characters[i];
            if (current == '.') {
                characters[i] = '/';
            }
        }

        return new String(characters);
    }

}
