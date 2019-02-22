package com.bwin.docx4jdemo.service;

import com.bwin.docx4jdemo.entity.Trademark;
import com.bwin.docx4jdemo.mapper.TrademarkMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.bwin.docx4jdemo.util.PdfUtil.parsePdf;

@Slf4j
@AllArgsConstructor
@Service
public class TrademarkServiceImpl {

    private final TrademarkMapper trademarkMapper;

//    @PostConstruct
    public void parseAndSave() {
        String regex = "^(#?)(\\d+)(.*)$";
        Pattern pattern = Pattern.compile(regex);
        List<String> texts = parsePdf("D:/Test/pdf/白兔标注版《类似商品和服务区分表》（2019版修订）.pdf", "D:/Test/image/");
        // 只能解析目录
        for (int i = 3; i < texts.size(); i++) {
            String[] lines = texts.get(i).split("\n");
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    log.info(line);
                    String code = matcher.group(2);
                    if (StringUtils.isEmpty(code)) {
                        break;
                    }
                    String name = matcher.group(3);
                    if (StringUtils.isEmpty(name)) {
                        break;
                    }
                    Trademark trademark = new Trademark();
                    trademark.setCode(code);
                    String parentId = code.startsWith("0") ? code.substring(1, 2) : code.substring(0, 2);
                    trademark.setParentId(parentId);
                    name = name.contains("..") ? name.substring(0, name.indexOf("..")) : name;
                    trademark.setName(name);
                    trademark.setIsParent("0");
                    trademarkMapper.insert(trademark);
                }
            }
        }
    }

}
