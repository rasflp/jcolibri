package jcolibri.util;

import java.io.File;

/**
 * Utilities to help when working with files.
 * 
 * @author Antonio A. Sánchez Ruiz-Granados
 */
public class FileUtil {

	/**
	 * If fileName exists then the file is renamed to fileName+".old".
	 * 
	 * @param fileName
	 */
	public static void createFileBackup(String fileName) {
		File file = null, fileBack = null;

		file = new File(fileName);
		if (file.exists()) {
			fileBack = new File(fileName + ".old");
			if (fileBack.exists()) {
				fileBack.delete();
				// Thread.sleep(500);
			}
			file.renameTo(fileBack);
			// Thread.sleep(500);
		}
	}
}
