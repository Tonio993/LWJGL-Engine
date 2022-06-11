package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Files {

    private Files() {}

    public static String read(String path) {
        return toString(inputStream(path));
    }

    public static List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        forEachLine(inputStream(path), line -> {
            lines.add(line);
        });
        return lines;
    }


    public static InputStream inputStream(String fileName) {
        ClassLoader classLoader = Files.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    public static byte[] bytes(String fileName) throws IOException {
        InputStream fileInputStream = inputStream(fileName);
        int bytes = fileInputStream.available();
        byte[] bytesArray = new byte[bytes];
        fileInputStream.read(bytesArray);
        return bytesArray;
    }

    public static String toString(InputStream is) {
        StringBuffer sb = new StringBuffer();
        forEachLine(is, line -> {
            sb.append(line);
            sb.append('\n');
        });
        return sb.toString();
    }

    private static void forEachLine(InputStream is, Consumer<String> operation) {
        try (
                InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                operation.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
