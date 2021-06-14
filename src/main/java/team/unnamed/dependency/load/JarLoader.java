package team.unnamed.dependency.load;

import team.unnamed.dependency.exception.ErrorDetails;

import java.io.File;

public interface JarLoader {
    void load(File jarFile, ErrorDetails errorDetails);
}
