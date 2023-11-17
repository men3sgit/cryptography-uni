package com.menes.cryptography.utils;

import java.io.File;
import java.util.Arrays;

public class FileUtils {
    public static String getFileName;

    public static boolean isValidFile(String... filePaths) {
        return Arrays.stream(filePaths).allMatch(file -> {
            boolean isFile = new File(file).isFile();
            if (!isFile) {
                System.out.printf("%s is not a file!%n", file);
            }
            return isFile;
        });
    }

    public static String appendFileName(String source, String append) {
        return String.format("%s%s.%s", source.substring(0, source.lastIndexOf('.')), append, getExtension(source));
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
