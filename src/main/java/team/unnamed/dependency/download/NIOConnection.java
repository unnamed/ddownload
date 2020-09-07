package team.unnamed.dependency.download;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface NIOConnection {

    File download(File file, String repoURL);

    int sizeOf(URL url) throws IOException;
}
