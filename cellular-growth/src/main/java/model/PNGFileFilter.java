package model;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import controller.FileUtil;

public class PNGFileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		if (file == null) {
			return false;
		}
		
		if (file.isDirectory()) {
			return true;
		}
		
		FileExtension fileExtension = FileUtil.getFileExtension(file);
		if (fileExtension != null) {
			switch (fileExtension) {
				case PNG:
					return true;
			}
		}
		return false;
	}
	
	@Override
	public String getDescription() {
		return "PNG files (.png)";
	}
}
