package team.unnamed.dependency.download;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

/**
 * A {@link ReadableByteChannel}.
 */
public interface MonitorByteChannel extends ReadableByteChannel {

    static MonitorByteChannel newChannel(InputStream channel,
                                         String fileName,
                                         int length,
                                         Logger logger) {
        return newChannel(Channels.newChannel(channel), fileName, length, logger);
    }

    static MonitorByteChannel newChannel(ReadableByteChannel channel,
                                         String fileName,
                                         int length,
                                         Logger logger) {
        return new MonitorByteChannelImpl(channel, fileName, length, logger);
    }

    void setLogger(Logger logger);

    /**
     * Get the percentage of download to finish.
     * @return Percentage of download
     */
    String getPercentage();

    /**
     * Get the total bytes downloaded.
     * @return Total bytes downloaded.
     */
    int getTotalDownloaded();

    /**
     * Get absolute size of download.
     * @return Absolute file
     */
    int getAbsoluteSize();

    /**
     * Get the needed bytes to download to finish.
     * @return Needed bytes to finish.
     */
    int getBytesNeeded();
}
