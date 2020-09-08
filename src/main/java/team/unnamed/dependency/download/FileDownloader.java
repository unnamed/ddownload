package team.unnamed.dependency.download;

import team.unnamed.dependency.exception.ErrorDetails;

import java.io.File;

public interface FileDownloader {

    /**
     * Downloads the file present in the provided URL (parameter "from"),
     * adds any error to the provided error details.
     *
     * If the destiny file already exists, checks if the
     * remote file is the same size as the local file, if
     * the size of the local file is smaller than the remote
     * file, the local file is deleted and downloaded.
     *
     * If the destiny file doesn't exist, the file is downloaded.
     *
     * @param destiny The destiny file
     * @param from The url where the file will be downloaded from
     * @param errorDetails The error details, errors will be
     *                     added here.
     * @return True if the file has been downloaded successfully
     */
    boolean download(File destiny, String from, ErrorDetails errorDetails);

}
