package team.unnamed.dependency;

import team.unnamed.dependency.download.NIOConnection;
import team.unnamed.dependency.download.NIOConnectionImpl;
import team.unnamed.dependency.exception.DependencyNotFoundException;
import team.unnamed.dependency.load.JarLoader;
import team.unnamed.dependency.logging.LogStrategy;
import team.unnamed.dependency.util.Validate;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link DependencyHandler}
 */
public class DependencyHandlerImpl implements DependencyHandler {

    private static final File[] EMPTY_FILE_ARRAY = new File[0];

    private final File folder;
    private final NIOConnection downloader;
    private final JarLoader loader;
    private final LogStrategy logger;

    public DependencyHandlerImpl(File folder, LogStrategy logger, URLClassLoader classLoader) {
        this.folder = Validate.notNull(folder, "folder");
        this.downloader = new NIOConnectionImpl(logger);
        this.loader = new JarLoader(classLoader);
        this.logger = logger;
        this.createFolderIfAbsent();
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

            File file = new File(folder, dependency.getArtifactName());

            if (file.exists()) { // the file already exists, don't download
                continue;
            }

            List<Throwable> errors = new ArrayList<>();
            boolean success = false;

            for (String url : dependency.getPossibleUrls()) {

                try {
                    downloader.download(file, url);
                    success = true;
                    break;
                } catch (DependencyNotFoundException e) {
                    errors.add(e);
                }
            }

            if (success) {
                downloaded.add(file);
            } else {
                Throwable[] throwables = errors.toArray(new Throwable[0]);
                logger.error("Dependency not found: " + dependency.toString(), throwables);
            }
        }

        return downloaded.toArray(EMPTY_FILE_ARRAY);
    }

    @Override
    public void toClasspath(File file) {
        loader.load(file);
    }

    private void createFolderIfAbsent() {
        if (!this.folder.exists()) {
            if (!this.folder.mkdirs()) {
                logger.error("Cannot create the folder " + folder.toString());
            }
        }
    }

}
