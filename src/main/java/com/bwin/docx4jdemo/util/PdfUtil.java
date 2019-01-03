package com.bwin.docx4jdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.util.StringUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PdfUtil {

    public static void main(String[] args) {
        List<String> texts = parsePdf("D:/Test/pdf/初步审定公告通知书.pdf", "D:/Test/image/");
    }

    /**
     * 读取pdf文本和图片
     * @param pdf pdf文件
     * @param directory 图片保存路径。若为empty，则不读取图片
     * @return 文本集合
     * @see <a href="https://blog.csdn.net/GlutinousRice/article/details/79597076">java-pdfbox2.0.8读取pdf文本和图片</a>
     */
    public static List<String> parsePdf(String pdf, String directory) {
        List<String> texts = new ArrayList<>();
        PDDocument document = null;
        try {
            if (pdf.endsWith(".pdf")) {
                document = PDDocument.load(new File(pdf));
                int pageNumbers = document.getNumberOfPages();
                // 一页一页读取
                for (int i = 0; i < pageNumbers; i++) {
                    // 文本内容
                    PDFTextStripper stripper = new PDFTextStripper();
                    // 设置按顺序输出
                    stripper.setSortByPosition(true);
                    stripper.setStartPage(i + 1);
                    stripper.setEndPage(i + 1);
                    String text = stripper.getText(document);
//                    log.info(text.trim());
                    texts.add(text.trim());
                    // 图片内容
                    if (!StringUtils.isEmpty(directory)) {
                        PDPage page = document.getPage(i);
                        PDResources resources = page.getResources();
                        Iterable<COSName> cosNames = resources.getXObjectNames();
                        if (cosNames != null) {
                            for (COSName cosName : cosNames) {
                                if (resources.isImageXObject(cosName)) {
                                    PDImageXObject imageXObject = (PDImageXObject) resources.getXObject(cosName);
                                    BufferedImage image = imageXObject.getImage();
                                    String suffix = imageXObject.getSuffix();
                                    try (FileOutputStream outputStream = new FileOutputStream(directory + UUID.randomUUID() + "." + suffix)) {
                                        ImageIO.write(image, suffix, outputStream);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return texts;
    }

}
