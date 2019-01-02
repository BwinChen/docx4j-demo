package com.bwin.docx4jdemo.util;

public class RarUtil {

   /*
    * cmd 压缩与解压缩命令
    */
	private static String rarCmd = "D:\\Program Files\\WinRAR\\Rar.exe a ";
    private static String unRarCmd = "D:\\Program Files\\WinRAR\\UnRAR x ";
	  
   /** 
    * 将1个文件压缩成RAR格式 
    * rarName 压缩后的压缩文件名(不包含后缀) 
    * fileName 需要压缩的文件名(必须包含路径) 
    * destDir 压缩后的压缩文件存放路径 
    */  
    public static void rar(String rarName, String fileName, String destDir) {
       rarCmd += destDir + rarName + ".rar " + fileName;  
       try {  
           Runtime rt = Runtime.getRuntime();  
           Process p = rt.exec(rarCmd);  
       }catch(Exception e) {  
           System.out.println(e.getMessage());        
       }  
    }  
  
   /** 
    * 将1个RAR文件解压 
    * rarFileName 需要解压的RAR文件(必须包含路径信息以及后缀) 
    * destDir 解压后的文件放置目录 
    */  
    public static void unRar(String rarFileName, String destDir) {
       unRarCmd += rarFileName + " " + destDir;
       try {  
           Runtime rt = Runtime.getRuntime();  
           Process p = rt.exec(unRarCmd);
           ProgressUtil.doWaitFor(p);//防止进程还没有执行完就返回
       } catch (Exception e) {  
           System.out.println(e.getMessage());     
       }  
    }  
    
    public static void main(String[] args){
    	RarUtil.unRar("D:/Test/rar/商标注册证.rar", "D:/Test/image/");
    }

}  
