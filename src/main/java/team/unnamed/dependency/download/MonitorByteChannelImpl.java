package team.unnamed.dependency.download;

import team.unnamed.dependency.logging.LogStrategy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of {@link MonitorByteChannel}.
 */
public class MonitorByteChannelImpl implements MonitorByteChannel {

    private final String fileName;
    // Hardcoded message info
    private final String formatInfo = "Downloading %s... [%s]";

    private final ReadableByteChannel delegate;
    private final int expectedSize;
    private int downloadSize;
    private final LogStrategy logger;

    private final int ticks = 50;
    private int cooldownTicks = ticks;

    public MonitorByteChannelImpl(ReadableByteChannel delegate,
                                  String fileName,
                                  int expectedSize,
                                  LogStrategy logger) {
        this.fileName = fileName;
        this.delegate = delegate;
        this.expectedSize = expectedSize;
        this.logger = logger;
    }

    @Override
    public int read(ByteBuffer byteBuffer) throws IOException {
        int bytesDownloaded = delegate.read(byteBuffer);
        if (bytesDownloaded > 0) {
            downloadSize += bytesDownloaded;
            if (cooldownTicks > 0) {
                cooldownTicks--;
            } else {
                logger.info(String.format(formatInfo, fileName, getPercentage()));
                cooldownTicks = ticks;
            }
        }
        return bytesDownloaded;
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public String getPercentage() {
        long result = Math.round(
            ((double) downloadSize / (double) expectedSize) * 100.0);
        return result + "%";
    }

    @Override
    public int getTotalDownloaded() {
        return downloadSize;
    }

    @Override
    public int getAbsoluteSize() {
        return expectedSize;
    }

    @Override
    public int getBytesNeeded() {
        return expectedSize - downloadSize;
    }

}
