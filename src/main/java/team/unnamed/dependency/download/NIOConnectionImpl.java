package team.unnamed.dependency.download;

import team.unnamed.dependency.exception.DependencyNotFoundException;
import team.unnamed.dependency.logging.LogStrategy;
import team.unnamed.dependency.util.Validate;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NIOConnectionImpl implements NIOConnection {

    private final LogStrategy logger;

    public NIOConnectionImpl(LogStrategy logger) {
        // it can be a dummy log strategy, but never null
        this.logger = Validate.notNull(logger, "logger");
    }

    @Override
    public File download(File file, String repoURL) {
        try {
            URL url = new URL(repoURL);
            int expectedSize = sizeOf(url);
            if (alreadyExist(file, expectedSize)) {
                return file;
            }
            try (final InputStream inputStream = new BufferedInputStream(url.openStream())) {
                try (MonitorByteChannel channel = MonitorByteChannel
                    .newChannel(
                        inputStream,
                        file.getName(),
                        expectedSize,
                        logger)) {
                    try (FileOutputStream stream = new FileOutputStream(file)) {
                        stream
                            .getChannel()
                            .transferFrom(channel, 0, expectedSize);
                    }
                }
                logger.info("Downloading " + file.getName() + "... [Success]");
            }
        } catch (FileNotFoundException e) {
            // Delete it to avoid corrupted files
            file.delete();
            throw new DependencyNotFoundException(e);
        } catch (IOException e) {
            throw new DependencyNotFoundException(e);
        }

        return file;
    }

    private boolean alreadyExist(File file, int expectedSize) throws IOException {
        if (file.exists()) {
            String fileName = file.getName();
            long fileSize = Files.size(file.toPath());
            if (fileSize == expectedSize) {
                logger.info(fileName + " [Already exist]");
                return true;
            }
            logger.warning("Dependency file '" + fileName
                + "' size not match with file size in Maven repository."
                + "(File size in folder: " + fileSize
                + ", File size in repository: " + expectedSize);
            logger.warning("deleting file to download it again...");
            file.delete();
        }
        return false;
    }

    @Override
    public int sizeOf(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        return connection.getContentLength();
    }

}
