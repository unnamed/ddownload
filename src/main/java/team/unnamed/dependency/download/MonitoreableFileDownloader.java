package team.unnamed.dependency.download;

import team.unnamed.dependency.exception.ErrorDetails;
import team.unnamed.dependency.logging.LogStrategy;
import team.unnamed.dependency.util.Validate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MonitoreableFileDownloader implements FileDownloader {

    private final LogStrategy logger;

    public MonitoreableFileDownloader(LogStrategy logger) {
        // it can be a dummy log strategy, but never null
        this.logger = Validate.notNull(logger, "logger");
    }

    @Override
    public boolean download(File file, String repoURL, ErrorDetails errorDetails) {
        try {
            URL url = new URL(repoURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "ddownloader");
            int expectedSize = connection.getContentLength();

            if (alreadyExist(file, expectedSize)) {
                // it's already downloaded, don't
                // try to download it from another
                // source.
                return true;
            }

            try (final InputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
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
                return true;
            }
        } catch (FileNotFoundException e) {
            // Delete it to avoid corrupted files
            file.delete();
            errorDetails.add(e);
        } catch (IOException e) {
            errorDetails.add(e);
        }
        return false;
    }

    private boolean alreadyExist(File file, int expectedSize) throws IOException {
        if (!file.exists()) {
            return false;
        }
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
        return false;
    }

}
