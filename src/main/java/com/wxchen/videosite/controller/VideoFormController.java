package com.wxchen.videosite.controller;

import com.wxchen.videosite.data.RestReturn;
import com.wxchen.videosite.service.FileService;
import com.wxchen.videosite.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VideoFormController {
    private final FileService fileService;
    private final VideoService videoService;

    public VideoFormController(FileService fileService, VideoService videoService) {
        this.fileService = fileService;
        this.videoService = videoService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("files") MultipartFile[] files, @RequestParam("group") String group) throws IOException, InterruptedException, NoSuchAlgorithmException {
        // 写文件到group
        List<File> localFiles = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            InputStream inputStream = file.getInputStream();
            File localFile = fileService.save(inputStream, file.getOriginalFilename(), group);
            localFiles.add(localFile);
            inputStream.close();
        }
        // 异步调用ffmpeg转码
        videoService.videoHandle(localFiles, group);
        // 响应前端
        return "redirect:/index.html";
    }
}
