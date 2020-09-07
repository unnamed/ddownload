package team.unnamed.dependency;

import team.unnamed.dependency.download.FileDownloader;
import team.unnamed.dependency.download.MonitoreableFileDownloader;
import team.unnamed.dependency.exception.ErrorDetails;
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
    private final FileDownloader downloader;
    private final JarLoader loader;
    private final LogStrategy logger;

    public DependencyHandlerImpl(File folder, LogStrategy logger, URLClassLoader classLoader) {
        this.folder = Validate.notNull(folder, "folder");
        this.downloader = new MonitoreableFileDownloader(logger);
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
        ErrorDetails errorDetails = new ErrorDetails("Cannot load dependencies");

        for (File file : downloaded) {
            loader.load(file, errorDetails);
        }

        if (errorDetails.errorCount() > 0) {
            throw errorDetails.toLoadException();
        }
    }

    @Override
    public File[] download(Iterable<? extends Dependency> dependencies) {

        List<File> downloaded = new ArrayList<>();
        ErrorDetails errorDetails = new ErrorDetails("Cannot download dependencies");

        for (Dependency dependency : dependencies) {

            File file = new File(folder, dependency.getArtifactName());
            ErrorDetails dependencyErrorDetails = new ErrorDetails("Cannot download dependency: " + dependency);

            for (String url : dependency.getPossibleUrls()) {
                boolean success = downloader.download(file, url, dependencyErrorDetails);
                if (success) {
                    // don't try in another URL
                    break;
                }
            }

            if (dependencyErrorDetails.errorCount() == 0) {
                downloaded.add(file);
            } else if (!dependency.isOptional()) {
                errorDetails.merge(dependencyErrorDetails);
            }
        }

        if (errorDetails.errorCount() > 0) {
            throw errorDetails.toDownloadException();
        }

        return downloaded.toArray(EMPTY_FILE_ARRAY);
    }

    private void createFolderIfAbsent() {
        if (!this.folder.exists()) {
            if (!this.folder.mkdirs()) {
                logger.error("Cannot create the folder " + folder.toString());
            }
        }
    }

}
