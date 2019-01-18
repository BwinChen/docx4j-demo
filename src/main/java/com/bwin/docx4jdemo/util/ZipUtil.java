package com.bwin.docx4jdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ZipUtil {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 解压 zip 文件
     *
     * @param zip zip 压缩文件
     * @param destDir zip 压缩文件解压后保存的目录
     * @param encoding zip 文件的编码
     * @return 返回 zip 压缩文件里的文件名列表
     * @see <a href="https://blog.csdn.net/liuxiangke0210/article/details/81941353">Java 解压 zip 文件</a>
     */
    public static List<String> unZip(File zip, String destDir, String encoding) {
        List<String> fileNames = new ArrayList<>();
        ZipArchiveInputStream inputStream = null;
        try {
            // 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
            if (destDir == null || destDir.length() == 0) {
                destDir = zip.getParent();
            }
            destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
            inputStream = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zip), BUFFER_SIZE), encoding);
            ZipArchiveEntry entry;
            while ((entry = inputStream.getNextZipEntry()) != null) {
                fileNames.add(entry.getName());
                File file = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(file); // 创建文件夹，如果中间有路径会自动创建
                } else {
                    OutputStream outputStream = null;
                    try {
                        FileUtils.touch(file);
                        outputStream = new FileOutputStream(new File(destDir, entry.getName()));
                        IOUtils.copy(inputStream, outputStream);
                    } finally {
                        IOUtils.closeQuietly(outputStream);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return fileNames;
    }

    /**
     * 解压 zip 文件
     *
     * @param zip zip 压缩文件的路径
     * @param destDir zip 压缩文件解压后保存的目录
     * @param encoding zip 文件的编码
     * @return 返回 zip 压缩文件里的文件名列表
     */
    public static List<String> unZip(String zip, String destDir, String encoding) {
        return unZip(new File(zip), destDir, encoding);
    }

    /**
     * 解压 zip 文件
     *
     * @param zip zip 压缩文件的路径
     * @param destDir zip 压缩文件解压后保存的目录
     * @return 返回 zip 压缩文件里的文件名列表
     */
    public static List<String> unZip(String zip, String destDir) {
        return unZip(zip, destDir, "GBK");
    }

    public static void main(String[] args) {
        List<String> names = unZip("D:/Test/zip/初步审定公告通知书.zip", "D:/Test/image/");
        log.info(names.toString());
    }

}
