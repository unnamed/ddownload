package team.unnamed.dependency;

import team.unnamed.dependency.download.FileDownloader;
import team.unnamed.dependency.download.MonitoreableFileDownloader;
import team.unnamed.dependency.exception.ErrorDetails;
import team.unnamed.dependency.load.JarLoader;
import team.unnamed.dependency.logging.LogStrategy;
import team.unnamed.dependency.resolve.DependencyResolver;
import team.unnamed.dependency.resolve.MavenDependencyResolver;
import team.unnamed.dependency.resolve.SubDependenciesResolver;
import team.unnamed.dependency.util.Validate;

import java.io.File;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 * Default implementation of {@link DependencyHandler}
 */
public class DependencyHandlerImpl implements DependencyHandler {

    private static final File[] EMPTY_FILE_ARRAY = new File[0];
    private static final Map<Class<?>, DependencyResolver<?>> RESOLVERS = new HashMap<>();

    private final DependencyResolver<Class<?>> mavenDependencyResolver;
    private final File folder;
    private final FileDownloader downloader;
    private final JarLoader loader;
    private final LogStrategy logger;

    // for async operations, you can
    // pass a Direct executor (sync)
    // like: Executor executor = Runnable::run;
    private final Executor executor;

    static {
        RESOLVERS.put(Dependency.class, new SubDependenciesResolver());
    }

    // use DependencyHandler.builder() to create a builder
    DependencyHandlerImpl(File folder, LogStrategy logger, JarLoader loader, Executor executor, boolean deleteOnNonEqual) {
        this.folder = Validate.notNull(folder, "folder");
        this.downloader = new MonitoreableFileDownloader(logger, deleteOnNonEqual);
        this.loader = loader;
        this.logger = logger;
        this.executor = executor;
        this.mavenDependencyResolver = new MavenDependencyResolver(logger);
        this.createFolderIfAbsent();
    }

    @Override
    public File getDestinyFolder() {
        return folder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setup(T object) {

        // it's safe.
        DependencyResolver<?> resolver = RESOLVERS.get(object.getClass());
        List<Dependency> dependencies;

        if (resolver == null) {
            if (object instanceof Class) {
                dependencies = mavenDependencyResolver.resolve((Class<?>) object);
            } else {
                dependencies = mavenDependencyResolver.resolve(object.getClass());
            }
        } else {
            dependencies = ((DependencyResolver<T>) resolver).resolve(object);
        }

        setup(dependencies);
    }

    @Override
    public void setup(Collection<? extends Dependency> dependencies) {

        ErrorDetails errorDetails = new ErrorDetails("Cannot setup dependencies");
        File[] downloaded = download(dependencies, errorDetails);

        for (File file : downloaded) {
            loader.load(file, errorDetails);
        }

        if (errorDetails.errorCount() > 0) {
            throw errorDetails.toLoadException();
        }
    }

    @Override
    public File[] download(Collection<? extends Dependency> dependencies) {

        ErrorDetails errorDetails = new ErrorDetails("Cannot download dependencies");
        File[] files = download(dependencies, errorDetails);

        if (errorDetails.errorCount() > 0) {
            throw errorDetails.toDownloadException();
        }

        return files;
    }

    private File[] download(Collection<? extends Dependency> dependencies, ErrorDetails errorDetails) {

        List<File> downloaded = new ArrayList<>();
        CountDownLatch countdown = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {

            executor.execute(() -> {

                File file = new File(folder, dependency.getArtifactName());
                ErrorDetails dependencyErrorDetails = new ErrorDetails("Cannot download dependency: " + dependency);
                boolean success = false;

                for (String url : dependency.getPossibleUrls()) {
                    success = downloader.download(file, url, dependencyErrorDetails);
                    if (success) {
                        // clear the errors, we already succeeded
                        dependencyErrorDetails = new ErrorDetails("Cannot download dependency: " + dependency);
                        // don't try in another URL
                        break;
                    }
                }

                if (success) {
                    if (dependencyErrorDetails.errorCount() == 0) {
                        downloaded.add(file);
                    } else if (!dependency.isOptional()) {
                        errorDetails.merge(dependencyErrorDetails);
                    }
                }

                countdown.countDown();
            });
        }

        try {
            countdown.await();
        } catch (InterruptedException e) {
            errorDetails.add(e);
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
