package controller;

import java.io.File;

import model.FileExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(FileUtil.class);
	
	public static FileExtension getFileExtension(File file) {
		String filename = file.getName();
		int extensionIndex = filename.lastIndexOf('.');
		
		if (extensionIndex > 0 && extensionIndex < filename.length() - 1) {
			String extension = filename.substring(extensionIndex + 1).toUpperCase();
			
			try {
				return FileExtension.valueOf(extension);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}
	
	public static String getFileRootName(File file) {
		String filename = file.getName();
		int extensionIndex = filename.lastIndexOf('.');
		
		if (extensionIndex > 0 && extensionIndex < filename.length() - 1) {
			return filename.substring(0, extensionIndex);
		}
		return "";
	}
	
	public static String getFileRootPath(File file) {
		String filename = file.getPath();
		int extensionIndex = filename.lastIndexOf('.');
		
		if (extensionIndex > 0 && extensionIndex < filename.length() - 1) {
			return filename.substring(0, extensionIndex);
		}
		return "";
	}
}
