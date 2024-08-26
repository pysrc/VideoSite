package com.wxchen.videosite.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class VideoService {
    private final String ffmpeg;
    private final String m3u8;
    private final CacheService cacheService;

    public VideoService(
            @Value("${work.ffmpeg.bin}") String ffmpeg,
            @Value("${work.video.m3u8}") String m3u8,
            CacheService cacheService
    ) {
        this.ffmpeg = ffmpeg;
        this.m3u8 = m3u8;
        this.cacheService = cacheService;
    }

    public String getIdString(String group, String filename) {
        return m3u8 + "/" + group + "/" + filename;
    }
    @Async("customExecutor")
    public void videoHandle(List<File> files, String group) throws IOException, InterruptedException, NoSuchAlgorithmException {
        for (File file : files) {
            String id = getIdString(group, file.getName());
            File dirc = new File(id);
            if(!dirc.exists()) {
                dirc.mkdirs();
            }
            // 生成缩略图
            {
                // ffmpeg -i xx.mp4 -ss 00:00:10.000 -vframes 1 -s 200x100 -f image2 desc.jpg
                List<String> command = new ArrayList<>();
                command.add(ffmpeg);
                command.add("-i");
                command.add(file.getAbsolutePath());
                command.add("-ss");
                command.add("00:00:10.000");
                command.add("-vframes");
                command.add("1");
                command.add("-s");
                command.add("200x100");
                command.add("-f");
                command.add("image2");
                command.add(id + "/desc.jpg");
                command.add("-y");

                ProcessBuilder processBuilder = new ProcessBuilder(command);
                Process process = processBuilder.start();
                //注意：使用ProcessBuilder执行任务，必须要手动接收正常输出信息和错误输出信息，不然ProcessBuilder任务会阻塞卡住
                Thread t1 = Thread.ofVirtual().start(() -> {
                    try {
                        process.getInputStream().transferTo(System.out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                //多线程读取错误输出信息 process.getErrorStream()
                Thread t2 = Thread.ofVirtual().start(()->{
                    try {
                        process.getErrorStream().transferTo(System.out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                t1.join();
                t2.join();
                System.out.println("image done.");
            }
            // 生成m3u8文件
            {
                // ffmpeg -i xx.mp4 -codec: copy -start_number 0 -hls_time 10 -hls_list_size 0 -f hls desc.m3u8
                List<String> command = new ArrayList<>();
                command.add(ffmpeg);
                command.add("-i");
                command.add(file.getAbsolutePath());
                command.add("-c:v");
                command.add("copy");
                command.add("-ac");
                command.add("2");
                command.add("-start_number");
                command.add("0");
                command.add("-hls_time");
                command.add("10");
                command.add("-hls_list_size");
                command.add("0");
                command.add("-f");
                command.add("hls");
                command.add(id + "/desc.m3u8");
                command.add("-y");
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                Process process = processBuilder.start();
                //注意：使用ProcessBuilder执行任务，必须要手动接收正常输出信息和错误输出信息，不然ProcessBuilder任务会阻塞卡住
                Thread t1 = Thread.ofVirtual().start(() -> {
                    try {
                        process.getInputStream().transferTo(System.out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                //多线程读取错误输出信息 process.getErrorStream()
                Thread t2 = Thread.ofVirtual().start(()->{
                    try {
                        process.getErrorStream().transferTo(System.out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                t1.join();
                t2.join();
                System.out.println("m3u8 done.");
            }
            // 生成搜索引擎索引
            String[] split = group.split("/");
            List<String> gps = Arrays.asList(split);
            cacheService.add(gps, file.getName());
        }
    }
}
