package com.wxchen.videosite.tools;

import com.wxchen.videosite.data.DocBase;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Tools {
    /**
     * 获取sha1
     * @param text
     * @return
     */
    public static String getSha1(String text) {
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void scan(File file, DocBase docBase) {
        System.out.println("Scan: " + file.getAbsolutePath());
        if(file.isFile()) {
            return;
        } else {
            File[] files = file.listFiles();
            // 子文件夹是否存在 desc.m3u8 存在则为视频文件
            boolean isvideo = false;
            for (File f : files) {
                if(f.isFile() && f.getName().toLowerCase().equals("desc.m3u8")) {
                    isvideo = true;
                    break;
                }
            }
            if(docBase.getDocs() == null) {
                docBase.setDocs(new ArrayList<>());
            }
            if(isvideo) {
                // 是video
                docBase.setGroup(false);
                docBase.setName(file.getName());
            } else {
                // 是分组
                docBase.setGroup(true);
                docBase.setName(file.getName());
                docBase.setDocs(new ArrayList<>());
                for (File f : files) {
                    DocBase base = new DocBase();
                    scan(f, base);
                    docBase.getDocs().add(base);
                }
            }
        }
    }

    public static DocBase scanAll(String startPath) {
        File file = new File(startPath);
        if(!file.exists()) {
            file.mkdirs();
        }
        DocBase root = new DocBase();
        Tools.scan(file, root);
        root.setName("");
        return root;
    }
}
