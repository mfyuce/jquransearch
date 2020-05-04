package org.jquransearch.core.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ResourceUtil {
    public List<String> loadResourceFile(String f, Charset charset) throws IOException, URISyntaxException {
        return loadResourceFile(f, getClass(), charset);
    }

    public static List<String> loadResourceFile(String f, Class c, Charset charset) throws IOException, URISyntaxException {
        return Files.readAllLines(Paths.get(getResourceFileUri(f, c)), charset);
    }

    private URI getResourceFileUri(String f) throws URISyntaxException, IOException {
        return getResourceFileUri(f, getClass());
    }

    public static URI getResourceFileUri(String f, Class c) throws URISyntaxException, IOException {
        URI uri = c.getResource("/" + f).toURI();
        try {
            FileSystems.newFileSystem(uri, Collections.emptyMap());
        } catch (Exception e) {
        }
        return uri;
    }
}