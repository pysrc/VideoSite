import os
import subprocess

def mkdirs(path):
    if not os.path.exists(path):
        os.makedirs(path)

def gen_index_image(src, todir):
    # 生成缩略图
    p = todir + "/" + src
    mkdirs(p)
    command = ["ffmpeg", "-i", src, "-ss", "00:00:10.000", "-vframes", "1", "-s", "200x100", "-f", "image2", p + "/desc.jpg", "-y"]
    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    # 打印输出和错误信息
    print(result.stdout.decode())
    print(result.stderr.decode())
def gen_m3u8(src, todir):
    # 生成m3u8
    p = todir + "/" + src
    mkdirs(p)
    command = ["ffmpeg", "-i", src, "-c:v", "copy", "-ac", "2", "-start_number", "0", "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", p + "/desc.m3u8", "-y"]
    result = subprocess.run(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    # 打印输出和错误信息
    print(result.stdout.decode())
    print(result.stderr.decode())

dirs = os.listdir()
for i in dirs:
    if i.endswith(".mp4") or i.endswith(".mkv"):
        print("start", i)
        gen_index_image(i, "dist")
        gen_m3u8(i, "dist")
