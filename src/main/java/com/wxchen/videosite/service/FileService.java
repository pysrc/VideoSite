package com.wxchen.videosite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {
    private final String original;
    private final String m3u8;

    public FileService(
            @Value("${work.video.original}") String original,
            @Value("${work.video.m3u8}") String m3u8
    ) {
        this.original = original;
        this.m3u8 = m3u8;
    }

    public File save(InputStream inputStream, String filename, String group) throws IOException {
        String path = String.format("%s/%s/%s", original, group, filename);
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        File dir = file.getParentFile();
        if(!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        inputStream.transferTo(outputStream);
        outputStream.flush();
        outputStream.close();
        return file;
    }

    public String getM3u8Id(String group, String filename) {
        return m3u8 + "/" + group + "/" + filename;
    }
    public String getOriginalId(String group, String filename) {
        return original + "/" + group + "/" + filename;
    }
    public static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolder(file);
                }
            }
        }
        System.out.println("DELETE: " + folder.getAbsolutePath());
        folder.delete();
    }
    public void delete(String group, String filename) {
        String m3u8Id = getM3u8Id(group, filename);
        String originalId = getOriginalId(group, filename);
        deleteFolder(new File(m3u8Id));
        deleteFolder(new File(originalId));
    }
}
