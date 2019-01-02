package com.bwin.docx4jdemo.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 进程工具类
 * 
 * @author 陈孟飞
 * @date 2017年9月14日
 * 
 */
public class ProgressUtil {

	/**
	 * 防止进程还没有执行完就返回
	 * @param process
	 * @return
	 */
	public static int doWaitFor(Process process) {
		InputStream in = null;
		InputStream err = null;
		int exitValue = -1; // returned to caller when p is finished
		try {
			in = process.getInputStream();
			err = process.getErrorStream();
			boolean finished = false; // Set to true when p is finished
			while (!finished) {
				try {
					while (in.available() > 0) {
						// Print the output of our system call
						Character c = new Character((char) in.read());
//						System.out.print(c);
					}
					while (err.available() > 0) {
						// Print the output of our system call
						Character c = new Character((char) err.read());
//						System.out.print(c);
					}
					// Ask the process for its exitValue. If the process
					// is not finished, an IllegalThreadStateException
					// is thrown. If it is finished, we fall through and
					// the variable finished is set to true.
					exitValue = process.exitValue();
					finished = true;
				} catch (IllegalThreadStateException e) {
					// Process is not finished yet;
					// Sleep a little to save on CPU cycles
					Thread.currentThread().sleep(500);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (err != null) {
				try {
					err.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return exitValue;
	}

	public static void main(String[] args) {
		
	}

}
