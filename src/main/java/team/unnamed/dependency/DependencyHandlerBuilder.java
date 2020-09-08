package team.unnamed.dependency;

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

    public DependencyHandler build() {
        if (classLoader == null) {
            classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        }
        Validate.notNull(folder, "Folder is required!");
        return new DependencyHandlerImpl(folder, logStrategy, classLoader, executor);
    }

}
