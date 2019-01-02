package com.bwin.docx4jdemo.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ZipUtil {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 解压 zip 文件
     *
     * @param zipFile zip 压缩文件的路径
     * @param destDir zip 压缩文件解压后保存的目录
     * @param encoding zip 文件的编码
     * @return 返回 zip 压缩文件里的文件名列表
     * @see <a href="https://blog.csdn.net/liuxiangke0210/article/details/81941353">Java 解压 zip 文件</a>
     */
    private static List<String> unZip(File zipFile, String destDir, String encoding){
        // 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
        if (StringUtils.isEmpty(destDir)) {
            destDir = zipFile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        List<String> fileNames = new ArrayList<>();
        ZipArchiveInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE), encoding);
            ZipArchiveEntry entry;
            while ((entry = inputStream.getNextZipEntry()) != null) {
                String fileName = entry.getName();
                fileNames.add(fileName);
                File file = new File(destDir, fileName);
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(file); // 创建文件夹，如果中间有路径会自动创建
                }else {
                    FileUtils.touch(file);
                    outputStream = new FileOutputStream(file);
                    IOUtils.copy(inputStream, outputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
        return fileNames;
    }

    /**
     * 解压 zip 文件
     *
     * @param zipFile zip 压缩文件的路径
     * @param destDir zip 压缩文件解压后保存的目录
     * @param encoding zip 文件的编码
     * @return 返回 zip 压缩文件里的文件名列表
     */
    public static List<String> unZip(String zipFile, String destDir, String encoding) {
        File zipfile = new File(zipFile);
        return unZip(zipfile, destDir, encoding);
    }

    /**
     * 解压 zip 文件
     *
     * @param zipFile zip 压缩文件的路径
     * @param destDir zip 压缩文件解压后保存的目录
     * @return 返回 zip 压缩文件里的文件名列表
     */
    public static List<String> unZip(String zipFile, String destDir) {
        return unZip(zipFile, destDir, "UTF-8");
    }

    public static void main(String[] args) {
        List<String> names = unZip("D:/Test/zip/20181227_1427296431.zip", "D:/Test/image/");
        System.out.println(names);
    }

}
