package ebs.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by Dubov Aleksey
 * Date: Dec 28, 2009
 * Time: 8:14:56 PM
 * Company: EBS (c) 2009
 */

public class RepositoryTools {
	public static int getCurrentVersion(String destFolder, boolean release) throws IOException {
		return Integer.parseInt(getProperties(getVersionFolder(destFolder, release)).getProperty("version", "0"));
	}

	public static String getCurrentInfo(String destFolder, boolean release) throws IOException {
		return getProperties(getVersionFolder(destFolder, release)).getProperty("info", "");
	}

	private static File getVersionFolder(String destFolder, boolean release) throws IOException {
		File df = new File(destFolder);
		createFolders(df);

		File betaFolder = new File(df, "beta");
		createFolders(betaFolder);

		File releaseFolder = new File(df, "release");
		createFolders(releaseFolder);

		return release ? releaseFolder : betaFolder;
	}

	public static int saveUpdate(String destFolder, Set<String> fileNames, String info, boolean release) throws IOException {
		File versionFolder = getVersionFolder(destFolder, release);

		for(String fileName : fileNames) {
			File file = new File(fileName);
			if(file.exists()) {
				FileUtils.copyFile(file, new File(versionFolder, FilenameUtils.getName(fileName)));
			} else {
				LoggerFactory.getLogger(RepositoryTools.class)
						.warn(new StringBuilder("File not found: ").append(file.getAbsolutePath()).toString());
			}
		}

		return updateVersion(versionFolder, release ? "release" : "beta", info);
	}

	private static int updateVersion(File srcFolder, String state, String info) throws IOException {
		Properties properties = getProperties(srcFolder);

		int version = Integer.parseInt(properties.getProperty("version", "0"));
		version += 1;

		properties.setProperty("version", Integer.toString(version));
		properties.setProperty("state", state);
		properties.setProperty("info", info);

		saveProperties(srcFolder, properties);

		return version;
	}

	private static Properties getProperties(File srcFolder) throws IOException {
		Properties properties = new Properties();

		File versionFile = new File(srcFolder, "version.properties");
		if(versionFile.exists()) {
			FileReader fr = new FileReader(versionFile);
			try {
				properties.load(fr);
			} finally {
				fr.close();
			}
		}

		return properties;
	}

	private static void saveProperties(File srcFolder, Properties properties) throws IOException {
		FileWriter fw = new FileWriter(new File(srcFolder, "version.properties"));
		try {
			properties.store(fw, "");
		} finally {
			fw.close();
		}
	}

	private static void createFolders(File folder) throws IOException {
		if(!folder.exists()) {
			if(!folder.mkdirs()) {
				throw new IOException("Cann't create dest folder for repository");
			}
		}
	}
}
