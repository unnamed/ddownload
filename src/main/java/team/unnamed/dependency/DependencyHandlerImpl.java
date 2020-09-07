package team.unnamed.dependency;

import team.unnamed.dependency.download.NIOConnection;
import team.unnamed.dependency.download.NIOConnectionImpl;
import team.unnamed.dependency.load.JarLoader;
import team.unnamed.dependency.util.Validate;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Default implementation of {@link DependencyHandler}
 */
public class DependencyHandlerImpl implements DependencyHandler {

    private static final File[] EMPTY_FILE_ARRAY = new File[0];

    private final File folder;
    private final NIOConnection downloader;
    private final JarLoader loader;

    public DependencyHandlerImpl(File folder, Logger logger, URLClassLoader classLoader) {
        this.folder = Validate.notNull(folder, "folder");
        this.downloader = new NIOConnectionImpl(logger);
        this.loader = new JarLoader(classLoader);
    }

    @Override
    public File getDestinyFolder() {
        return folder;
    }

    @Override
    public void setup(Iterable<? extends Dependency> dependencies) {
        File[] downloaded = download(dependencies);
        for (File file : downloaded) {
            toClasspath(file);
        }
    }

    @Override
    public File[] download(Iterable<? extends Dependency> dependencies) {
        List<File> downloaded = new ArrayList<>();
        for (Dependency dependency : dependencies) {
            // TODO: Download the dependencies :D
        }
        return downloaded.toArray(EMPTY_FILE_ARRAY);
    }

    @Override
    public void toClasspath(File file) {
        loader.load(file);
    }

}
