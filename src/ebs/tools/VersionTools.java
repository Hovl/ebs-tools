package ebs.tools;

import at.spardat.xma.xdelta.JarDelta;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by Dubov Aleksei
 * Date: Nov 18, 2008
 * Time: 4:17:57 PM
 * Company: EBS (c) 2008
 */

public class VersionTools {
	private static final Pattern verPattern = Pattern.compile("(\\d+)");
	private static final String repository = "repository";

    public static int saveSoftwareUpdate(String destFolder, final String targetSoftware,
											Map<String, String> fileNames, String fileToPatch, boolean patch)
	{
		int version = 0;
		String newVerPath = "";
		String newVerPath2Copy = "";

        try {
			File dest = new File(destFolder);
			String softwareDirName = targetSoftware + '1';
			File softwareDir = new File(dest, softwareDirName);

            if (!(dest.exists() & softwareDir.exists())) {
                softwareDir.mkdirs();
                newVerPath2Copy = softwareDir.getPath();

                if(patch) {
					File file = new File(dest, repository + File.separatorChar + softwareDirName);
                    file.mkdirs();
                    newVerPath = file.getPath();
                }

                for(String path : fileNames.keySet()){
					String fileName = new File(path).getName();
					FileUtils.copyFile(new File(path), new File(newVerPath2Copy, fileName));
                    if(path.contains(fileToPatch) & patch) {
                        FileUtils.copyFile(new File(path), new File(newVerPath, fileName));
                    }
                }

				return 1;
            }

			FilenameFilter targetFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.contains(targetSoftware);
				}
			};

			String [] dirs = dest.list(targetFilter);

			version = getLastVersion(dirs);
			softwareDirName = targetSoftware + (version + 1);
			if (version != 0) {
				File fff = new File(dest, softwareDirName);
				fff.mkdirs();
				newVerPath2Copy = fff.getPath();
			}


			for(String path : fileNames.keySet()) {
				String fileName = new File(path).getName();

				if(patch) {
					if(path.contains(fileToPatch)){
						String [] repDirs = new File(dest, repository).list(targetFilter);
						int hVersion = getLastVersion(repDirs);

						File f = new File(dest, new StringBuilder(repository).append(File.separatorChar)
								.append(softwareDirName).toString());
						f.mkdirs();
						newVerPath = f.getPath();

						FileUtils.copyFile(new File(path), new File(newVerPath, fileName));
						createPatch(new StringBuilder(destFolder).append(File.separatorChar)
										.append(repository).append(File.separatorChar)
										.append(targetSoftware).append(hVersion).toString(),
								newVerPath, newVerPath2Copy, fileName);
					} else {
						FileUtils.copyFile(new File(path), new File(newVerPath2Copy, fileName));
					}
				} else {
					FileUtils.copyFile(new File(path), new File(newVerPath2Copy, fileName));
				}
			}
        } catch (IOException e) {
			LoggerFactory.getLogger(VersionTools.class).warn("Error saving software update", e);
		}

        LoggerFactory.getLogger(VersionTools.class).info(targetSoftware + " Version is: " + (version +1));
        return version + 1;
    }

    private static boolean createPatch(String oldVerDir, String newVerDir, String newVerPath, String fileToPatch)
			throws IOException
	{
		String patchName = fileToPatch.substring(0, fileToPatch.indexOf('.') + 1) + "patch";
		ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(new File(newVerPath,  patchName)));

        try {
			new JarDelta().computeDelta(new ZipFile(oldVerDir + File.separatorChar + fileToPatch),
					new ZipFile(newVerDir + File.separatorChar + fileToPatch), stream);
        } catch (IOException e) {
			LoggerFactory.getLogger(VersionTools.class).warn("Error creating patch", e);
		} finally {
			stream.close();
		}
        return new File (newVerPath + File.separatorChar + patchName).exists();
    }

    private static int getLastVersion(String[] list){
        Integer lastVer = 0;
        for (String s : list){
            Matcher m = verPattern.matcher(s);
            if (m.find()) {
                if (Integer.parseInt(m.group()) > lastVer) lastVer = Integer.parseInt(m.group());
            }
        }
        return lastVer;
    }
}
