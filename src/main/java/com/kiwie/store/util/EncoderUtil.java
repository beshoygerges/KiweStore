package com.kiwie.store.util;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class EncoderUtil {
    private EncoderUtil() {

    }

    public static String encode(String filePath) {
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }

    }
}
