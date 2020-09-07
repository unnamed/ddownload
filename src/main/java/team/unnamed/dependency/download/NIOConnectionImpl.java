package team.unnamed.dependency.download;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NIOConnectionImpl implements NIOConnection {
    private final Logger logger;

    public NIOConnectionImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public File download(File file, String repoURL) {
        try {
            URL url = Paths.get(repoURL, file.getName())
                .toUri().toURL();
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
                info("Downloading " + file.getName() + "... [Success]");
            }
        } catch (FileNotFoundException e) {
            // Delete him to avoid corrupted files
            file.delete();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private boolean alreadyExist(File file, int expectedSize) throws IOException {
        if (file.exists()) {
            String fileName = file.getName();
            long fileSize = Files.size(file.toPath());
            if (fileSize == expectedSize) {
                info(fileName + " [Already exist]");
                return true;
            }
            warning("Dependency file '" + fileName
                + "' size not match with file size in Maven repository."
                + "(File size in folder: " + fileSize
                + ", File size in repository: " + expectedSize);
            warning("deleting file to download it again...");
            file.delete();
        }
        return false;
    }

    private void info(String message) {
        if (logger != null) {
            logger.info(message);
        }
    }

    private void warning(String message) {
        if (logger != null) {
            logger.warning(message);
        }
    }

    @Override
    public int sizeOf(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        return connection.getContentLength();
    }
}
