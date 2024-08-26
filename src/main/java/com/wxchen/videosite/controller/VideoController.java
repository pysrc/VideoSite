package com.wxchen.videosite.controller;

import com.wxchen.videosite.data.DocBase;
import com.wxchen.videosite.data.RestReturn;
import com.wxchen.videosite.service.CacheService;
import com.wxchen.videosite.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class VideoController {
    private final CacheService cacheService;
    private final FileService fileService;

    public VideoController(
            CacheService cacheService, FileService fileService
    ) {
        this.cacheService = cacheService;
        this.fileService = fileService;
    }

    @GetMapping("/videos")
    public RestReturn<DocBase> videos(
            @RequestParam(name = "group", defaultValue = "/") String group
    ) {
        String[] split = group.split("/");
        List<String> gp = Arrays.asList(split);
        return new RestReturn(true, null, CacheService.query(gp));
    }

    @GetMapping("/delete-video")
    public RestReturn deleteVideo(
            @RequestParam(name = "group") String group,
            @RequestParam(name = "filename") String filename
    ) throws IOException {
        fileService.delete(group, filename);

        return new RestReturn(true);
    }

}
