package com.animals.app.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * Created by oleg on 21.08.2015.
 */
public class CreateAnimalImage {
    private static final int BUFFER_SIZE = 1024;
    private static final int START_OFFSET = 0;
    private static final int END_OF_FILE = -1;
    private static final String NEW_IMAGE_FORMAT = ".png";

    /**
     * Call save image method and return image URL for insert into database
     * @param imageBytes Image bytes
     * @param pathToImageStorage Path to image storage folder
     * @return Image name for insert into database
     */
    public static String createAnimalImage(String imageBytes, String pathToImageStorage) throws IOException{
        String fileName = imageName();

        try(InputStream is = new ByteArrayInputStream(decodedBytes(imageBytes))) {
            saveImage(is, pathToImageStorage(pathToImageStorage, fileName));
            return fileName;
        } catch (IOException e) {
            throw new IOException("Fail to load image", e);
        }
    }

    /**
     * Decode bytes from BASE64
     * @param image Decode bytes
     * @return bytes
     * @throws IOException File isn't image
     */
    private static byte[] decodedBytes(String image) throws IOException {
        return new BASE64Decoder().decodeBuffer(image);
    }

    /**
     * Save image in folder
     * @param inputStream Stream of image bytes
     * @param uploadedFileLocation Image storage
     */
    private static void saveImage(InputStream inputStream, String uploadedFileLocation) throws IOException{
        try(OutputStream out = new FileOutputStream(new File(uploadedFileLocation))) {
            int read;
            byte[] bytes = new byte[BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != END_OF_FILE) {
                out.write(bytes, START_OFFSET, read);
            }
            out.flush();
        } catch (IOException e) {
            throw new IOException("Fail to save image", e);
        }
    }

    /**
     * Generate unique name of image
     * @return Unique name of image
     */
    private static String imageName(){
        return System.currentTimeMillis() + NEW_IMAGE_FORMAT;
    }

    private static String pathToImageStorage(String pathToImageStorage, String fileName){
        return pathToImageStorage + fileName;
    }
}