package org.pk.route.util;


import org.pk.route.exception.ApplicationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ApplicationUtil {

    public static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    public static String getRootPath() {
        try {
            return new URI(Thread.currentThread().getContextClassLoader().getResource("").getPath()).getPath();
        } catch (URISyntaxException e) {
            throw new ApplicationException("Application Root Path cannot be found");
        }
    }

    public static InputStream getResourceAsStream(String resource) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    }

    public static Stream<String> readLines(String resource) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = ApplicationUtil.getResourceAsStream(resource);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines.stream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static String routeCacheKey(String origin, String destination) {
        return String.format("%s->%s", origin, destination);
    }
}

