package team.unnamed.dependency;

import team.unnamed.dependency.load.JarLoader;
import team.unnamed.dependency.load.JarLoaderJdk8;
import team.unnamed.dependency.logging.LogStrategy;
import team.unnamed.dependency.util.Validate;

import java.io.File;
import java.net.URLClassLoader;
import java.util.concurrent.Executor;

/**
 * Fluent builder for creating a {@link DependencyHandler}
 */
public final class DependencyHandlerBuilder {

    private Executor executor = Runnable::run;
    private LogStrategy logStrategy = LogStrategy.getDefault();
    private File folder; // required
    private URLClassLoader classLoader;
    private JarLoader loader;

    private boolean deleteOnNonEqual = true;

    DependencyHandlerBuilder() {
    }

    public DependencyHandlerBuilder setExecutor(Executor executor) {
        this.executor = Validate.notNull(executor, "executor");
        return this;
    }

    public DependencyHandlerBuilder setLogStrategy(LogStrategy logStrategy) {
        this.logStrategy = Validate.notNull(logStrategy, "logStrategy");
        return this;
    }

    public DependencyHandlerBuilder setDependenciesFolder(File folder) {
        this.folder = Validate.notNull(folder, "folder");
        return this;
    }

    public DependencyHandlerBuilder setClassLoader(ClassLoader classLoader) {
        if (!(classLoader instanceof URLClassLoader)) {
            throw new IllegalArgumentException("The provided class loader isn't an URLClassLoader!");
        }
        this.classLoader = (URLClassLoader) classLoader;
        return this;
    }


    public DependencyHandlerBuilder setLoader(JarLoader loader) {
        this.loader = Validate.notNull(loader, "loader");
        return this;
    }

    public DependencyHandlerBuilder setDeleteOnNonEqual(boolean deleteOnNonEqual) {
        this.deleteOnNonEqual = deleteOnNonEqual;
        return this;
    }

    public DependencyHandler build() {
        if (loader == null) {
            if (classLoader == null) {
                classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            }

            this.loader = new JarLoaderJdk8(classLoader);
        }

        Validate.notNull(folder, "Folder is required!");
        return new DependencyHandlerImpl(folder, logStrategy, loader, executor, deleteOnNonEqual);
    }

}
