package com.bwin.docx4jdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.ss.usermodel.Workbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PoiUtil {

    public static void main(String[] args) throws Exception {
        saveDocImage("D:/Test/doc/battcn-plus手册.doc","D:/Test/image");
        savePptImage("D:/Test/ppt/battcn-plus手册.ppt","D:/Test/image");
        saveXlsImage("D:/Test/xls/battcn-plus手册.xls","D:/Test/image");
    }

    /**
     * 提取doc文档图片
     * @param file 源文件
     * @param destination 保存目录
     * @see <a href="https://blog.csdn.net/GlutinousRice/article/details/79590445">java-poi3.17读取word文本及图片</a>
     */
    public static void saveDocImage(String file, String destination) {
        FileInputStream inputStream = null;
        HWPFDocument document = null;
        try {
            inputStream = new FileInputStream(new File(file));
            document = new HWPFDocument(inputStream);
            PicturesTable picturesTable = document.getPicturesTable();
            List<Picture> pictures = picturesTable.getAllPictures();
            for (Picture picture : pictures) {
                String fileName = picture.suggestFullFileName();
                FileOutputStream out = new FileOutputStream(new File(destination + File.separator + fileName));
                picture.writeImageContent(out);
                out.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            close(inputStream, document, null, null);
        }
    }

    /**
     * 提取ppt文档图片
     * @param file 源文件
     * @param destination 保存目录
     * @see <a href="https://blog.csdn.net/GlutinousRice/article/details/79597055">java-poi3.17读取ppt文本和图片</a>
     */
    public static void savePptImage(String file, String destination) {
        FileInputStream inputStream = null;
        SlideShow slideShow = null;
        try {
            inputStream = new FileInputStream(new File(file));
            slideShow = new HSLFSlideShow(inputStream);
            List pictureDatas = slideShow.getPictureData();
            for (Object object : pictureDatas) {
                PictureData pictureData = (PictureData) object;
                String type = pictureData.getType().toString();
                byte[] data = pictureData.getData();
                saveData(destination, type, data);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            close(inputStream, null, slideShow, null);
        }
    }

    /**
     * 提取xls文档图片
     * @param file 源文件
     * @param destination 保存目录
     * @see <a href="https://blog.csdn.net/GlutinousRice/article/details/79596998">java-poi3.17读取excel文本和图片</a>
     */
    public static void saveXlsImage(String file, String destination) {
        FileInputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(new File(file));
            workbook = new HSSFWorkbook(inputStream);
            int sheetCount = workbook.getNumberOfSheets();
            if (sheetCount > 0) {
                List pictures = workbook.getAllPictures();
                for (Object object : pictures) {
                    HSSFPictureData pictureData = (HSSFPictureData) object;
                    pictureData.getFormat();
                    String type = pictureData.suggestFileExtension();
                    byte[] data = pictureData.getData();
                    saveData(destination, type, data);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            close(inputStream, null, null, workbook);
        }
    }

    private static void saveData(String destination, String type, byte[] data) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(destination + File.separator + UUID.randomUUID() + "." + type);
        outputStream.write(data);
        outputStream.close();
    }

    private static void close(FileInputStream inputStream, HWPFDocument doc, SlideShow slideShow, Workbook workbook) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (doc != null) {
                doc.close();
            }
            if (slideShow != null) {
                slideShow.close();
            }
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
