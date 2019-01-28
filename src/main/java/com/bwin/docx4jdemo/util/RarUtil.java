package com.bwin.docx4jdemo.util;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class RarUtil {

   /*
    * cmd 压缩与解压缩命令
    */
	private static String rarCmd = "D:/Program Files/WinRAR/Rar.exe a ";
    private static String unRarCmd = "D:/Program Files/WinRAR/UnRAR x -y ";

   /**
    * 将1个文件压缩成RAR格式
    * rarName 压缩后的压缩文件名(不包含后缀)
    * fileName 需要压缩的文件名(必须包含路径)
    * destDir 压缩后的压缩文件存放路径
    */
    public static boolean rar(String rarName, String fileName, String destDir) {
        rarCmd += destDir + rarName + ".rar " + fileName;
        return exe(rarCmd);
    }

   /**
    * 将1个RAR文件解压
    * rarFileName 需要解压的RAR文件(必须包含路径信息以及后缀)
    * destDir 解压后的文件放置目录
    */
    public static boolean unRar(String rar, String destDir) {
        unRarCmd += rar + " " + destDir;
        return exe(unRarCmd);
    }

    /**
     * JAVA执行cmd命令
     * @param cmd 命令行
     * @see <a href="https://blog.csdn.net/flushest/article/details/52117068">JAVA利用cmd命令行调用WINRAR解压及压缩</a>
     */
    private static boolean exe(String cmd) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
            String line;
            while((line = reader.readLine()) != null) {
                log.info(line);
            }
            reader.close();
            if(p.waitFor()!=0) {
                return false;
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
        return true;
    }
    
    public static void main(String[] args){
        rar("商标注册证", "D:/Test/image/商标注册证.pdf", "D:/Test/image/");
        unRar("D:/Test/rar/商标注册证.rar", "D:/Test/image/");
    }

}
