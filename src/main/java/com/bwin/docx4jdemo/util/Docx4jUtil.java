package com.bwin.docx4jdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.docx4j.openpackaging.packages.PresentationMLPackage;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class Docx4jUtil {

    private static final String DOCX_IMAGE_PART_NAME_PATTERN = "/word/media/";
    private static final String PPTX_IMAGE_PART_NAME_PATTERN = "/ppt/media/";
    private static final String XLSX_IMAGE_PART_NAME_PATTERN = "/xl/media/";

    public static void main(String[] args) throws Exception {
        saveDocxImage("D:/Test/docx/battcn-plus手册.docx", "D:/Test/image");
        savePptxImage("D:/Test/pptx/battcn-plus手册.pptx", "D:/Test/image");
        saveXlsxImage("D:/Test/xlsx/battcn-plus手册.xlsx", "D:/Test/image");
    }

    /**
     * 提取docx文档图片
     * @param file 源文件
     * @param destination 保存目录
     */
    public static void saveDocxImage(String file, String destination) throws Exception {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(file));
        saveImage(wordMLPackage, destination, DOCX_IMAGE_PART_NAME_PATTERN);
    }

    /**
     * 提取pptx文档图片
     * @param file 源文件
     * @param destination 保存目录
     */
    public static void savePptxImage(String file, String destination) throws Exception {
        PresentationMLPackage presentationMLPackage = PresentationMLPackage.load(new File(file));
        saveImage(presentationMLPackage, destination, PPTX_IMAGE_PART_NAME_PATTERN);
    }

    /**
     * 提取xlsx文档图片
     * @param file 源文件
     * @param destination 保存目录
     */
    public static void saveXlsxImage(String file, String destination) throws Exception {
        SpreadsheetMLPackage presentationMLPackage = SpreadsheetMLPackage.load(new File(file));
        saveImage(presentationMLPackage, destination, XLSX_IMAGE_PART_NAME_PATTERN);
    }

    /**
     * 提取图片
     * @see <a href="http://www.it610.com/article/5309.htm">docx4j提取word 2007图片</a>
     */
    private static void saveImage(OpcPackage opcPackage, String destination, String partNamePattern) throws IOException {
        for (Map.Entry<PartName, Part> partEntry : opcPackage.getParts().getParts().entrySet()) {
            Part part = partEntry.getValue();
            if (part instanceof BinaryPartAbstractImage) {
                BinaryPartAbstractImage image = (BinaryPartAbstractImage) part;
                String contentType = image.getContentType();
                String partName = image.getPartName().getName();
                log.info(String.format("contentType=%s, partName=%s", contentType, partName));
                String fileName = null;
                if (partName.contains(partNamePattern)) {
                    fileName = partName.substring(partName.indexOf(partNamePattern) + partNamePattern.length());
                }
                if (fileName == null) {
                    continue;
                }
                FileOutputStream outputStream = new FileOutputStream(destination + File.separator + fileName);
                image.writeDataToOutputStream(outputStream);
                outputStream.close();
            }
        }
    }

}
