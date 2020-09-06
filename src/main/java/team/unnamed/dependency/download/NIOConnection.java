package team.unnamed.dependency.download;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface NIOConnection {

  File download(File file);

  int sizeOf(URL url) throws IOException;
}
