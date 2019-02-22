package com.bwin.docx4jdemo.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {

    public static void main( String args[] ){

        // 按指定模式在字符串查找
        String text = "#4203医疗、社会福利服务........................................................................................................................... 394 ";
        String regex = "(#?)(\\d+)(.*)";

        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile(regex);

        // 现在创建 matcher 对象
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            System.out.println("Found value: " + matcher.group(0) );
            System.out.println("Found value: " + matcher.group(1) );
            System.out.println("Found value: " + matcher.group(2) );
            System.out.println("Found value: " + matcher.group(3) );
        } else {
            System.out.println("NO MATCH");
        }
    }

}
