package com.wxchen.videosite.controller;

import com.wxchen.videosite.data.DocBase;
import com.wxchen.videosite.data.RestReturn;
import com.wxchen.videosite.service.CacheService;
import com.wxchen.videosite.tools.Tools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("/cache")
public class CacheController {
    private final String m3u8;
    private final CacheService cacheService;

    public CacheController(@Value("${work.video.m3u8}") String m3u8, CacheService cacheService) {
        this.m3u8 = m3u8;
        this.cacheService = cacheService;
    }

    @GetMapping("/refresh")
    public RestReturn refresh() throws IOException {
        // 遍历刷新磁盘上的vedio结构
        DocBase res = Tools.scanAll(m3u8);
        cacheService.flushDatasCache(res);
        return new RestReturn(true, "", res);
    }
}
