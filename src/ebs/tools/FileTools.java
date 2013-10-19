package ebs.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Dubov Aleksei
 * Date: Jan 28, 2008
 * Time: 8:04:57 PM
 * Company: EBS (c) 2008
 */
public class FileTools {
	private static final long BUF_SIZE_10MB = (10 * 1024 * 1024);
	private static final int BUF_SIZE_1MB = (1024 * 1024);

	public static long getWriteFileSpeed(String destFolder, String tmpFileName) throws IOException {
		File tempFile = new File(tmpFileName);
		if(tempFile.exists() && tempFile.length() != BUF_SIZE_10MB) {
			FileUtils.forceDelete(tempFile);
		}

		if(!tempFile.exists()) {
			RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
			try {
				raf.setLength(BUF_SIZE_10MB);
			} finally {
				raf.close();
			}
		}

		File destFile = new File(destFolder, 
				new StringBuilder(tmpFileName).append(new Date().getTime()).toString());

		long time = new Date().getTime();
		FileUtils.copyFile(tempFile, destFile);
		if(!destFile.exists()) {
			return -1;
		}
		FileUtils.forceDelete(destFile);

		time = new Date().getTime() - time;

		return Math.round(BUF_SIZE_10MB / (time / 1000f));
	}

	public static void zipFolder(File zipFile, ZipOutputStream zos) throws IOException {
		if (zipFile.isDirectory()) {
			String[] dirList = zipFile.list();
			for (String name : dirList) {
				File f = new File(zipFile, name);
				if (f.isDirectory()) {
					zipFolder(f, zos);
				} else {
					zipFile(f, zos);
				}
			}
		} else {
			zipFile(zipFile, zos);
		}
	}

	public static void zipFile(File zipFile, ZipOutputStream zos) throws IOException {
		FileInputStream fis = new FileInputStream(zipFile);
		try {
			ZipEntry entry = new ZipEntry(zipFile.getPath());
			zos.putNextEntry(entry);

			byte[] readBuffer = new byte[1024];
			int bytesIn;
			while ((bytesIn = fis.read(readBuffer)) != -1) zos.write(readBuffer, 0, bytesIn);
		} finally {
			fis.close();
		}
	}

	public static String getMimeType(String fileName) throws IOException {
		Properties props = new Properties();
		props.load(FileTools.class.getResourceAsStream(File.separator + "mimeTypes.properties"));

		return props.getProperty(FilenameUtils.getExtension(fileName), "application/octet-stream");
	}

	public static String streamToString(InputStream stream) throws IOException {
		try {
			byte[] buff = new byte[stream.available()];
			int len = stream.read(buff);
			return new String(buff, 0, len);
		} finally {
			stream.close();
		}
	}

	public static void copyFileToStream(String fileName, OutputStream outputStream) throws IOException {
		FileInputStream inputStream = new FileInputStream(fileName);
		try {
			byte[] buff;
			if(inputStream.available() > BUF_SIZE_1MB) {
				buff = new byte[BUF_SIZE_1MB];
			} else {
				buff = new byte[inputStream.available()];
			}
			int bytes;
			while((bytes = inputStream.read(buff)) != -1) outputStream.write(buff, 0, bytes);
		} finally {
			outputStream.flush();
			outputStream.close();
			inputStream.close();
		}
	}

	public static File getFile(String baseDir, String fileName) {
		File dir = new File(baseDir);
		if(!dir.exists()) dir.mkdirs();
		return new File(baseDir, fileName);
	}
}
