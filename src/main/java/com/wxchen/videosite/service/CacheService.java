package com.wxchen.videosite.service;

import com.alibaba.fastjson2.JSONObject;
import com.wxchen.videosite.data.DocBase;
import com.wxchen.videosite.data.GlobalCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CacheService {
    private static GlobalCache globalCache;
    private final String cacheFile;

    public CacheService(
            @Value("${work.cache.file}") String cacheFile
    ) {
        this.cacheFile = cacheFile;
        // 读缓存文件，加载到配置文件
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(cacheFile);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            String s = new String(bytes, StandardCharsets.UTF_8);
            CacheService.globalCache = JSONObject.parseObject(s, GlobalCache.class);
        } catch (FileNotFoundException e) {
            CacheService.globalCache = new GlobalCache();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DocBase query(List<String> group) {
        DocBase rt = CacheService.globalCache.getData();
        for (String g : group) {
            rt = rt.sub(g);
            if(rt == null) {
                return null;
            }
        }
        return rt;
    }

    /**
     * 缓存存盘
     * @param data
     * @throws IOException
     */
    public void flushDatasCache(DocBase data) throws IOException {
        if(data == null) {
            return;
        }
        CacheService.globalCache.setData(data);
        flushDatasCache();
    }

    public void flushDatasCache() throws IOException {
        String jsonString = JSONObject.toJSONString(CacheService.globalCache);
        // 落盘
        File file = new File(cacheFile);
        if(!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    public void add(List<String> group, String filename) throws IOException {
        DocBase base = CacheService.globalCache.getData();
        for (String s : group) {
            DocBase sub = base.sub(s);
            if(sub == null) {
                // 子一级为空，新建
                sub = new DocBase();
                sub.setName(s);
                sub.setGroup(true);
                sub.setDocs(new ArrayList<>());
                base.addSub(sub);
                base = sub;
            } else {
                base = sub;
            }
        }
        DocBase d = new DocBase();
        d.setGroup(false);
        d.setName(filename);
        d.setDocs(new ArrayList<>());
        base.addSub(d);
        // 刷新缓存
        flushDatasCache();
    }


}
