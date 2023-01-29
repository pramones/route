package org.pk.route.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ApplicationUtil {

    public static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    public static String getRootPath() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
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

