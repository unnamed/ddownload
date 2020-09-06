package team.unnamed.dependency.load;

import team.unnamed.dependency.exception.DependencyLoadException;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Loads JAR File classes using a
 * {@link URLClassLoader}
 */
public final class JarLoader {

    private static final Method ADD_URL_METHOD;
    private final URLClassLoader classLoader;

    static {
        try {
            ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new DependencyLoadException(e);
        }
    }

    /**
     * Constructs a new JAR Loader
     * @param classLoader The class loader that
     *                    will be used to load
     *                    all classes
     */
    public JarLoader(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Loads the file classes using the
     * provided classLoader
     * @param jarFile The jar file
     */
    public void load(File jarFile) {

        try {
            ADD_URL_METHOD.invoke(classLoader, jarFile.toURI().toURL());
        } catch (Exception e) {
            throw new DependencyLoadException(jarFile, e);
        }
    }

}
