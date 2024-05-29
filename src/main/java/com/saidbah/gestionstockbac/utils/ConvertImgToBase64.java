package com.saidbah.gestionstockbac.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
public class ConvertImgToBase64 {
    public static String encodeImageToBase64(String imagePath) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
