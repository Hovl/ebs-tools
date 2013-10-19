package ebs.ssh;

import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.LocalFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksey Dubov.
 * Date: 7/3/11
 * Time: 4:05 PM
 * Copyright (c) 2011
 */
public class FileSystemDirectory extends FileSystemFile {
	private String filerValue;

	public FileSystemDirectory(String path, String filterValue) {
		super(path);
		this.filerValue = filterValue;
	}

	@Override
	public Iterable<FileSystemFile> getChildren(LocalFileFilter filter) throws IOException {
		File file = getFile();
		File[] childFiles = file.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().contains(filerValue);
			}
		});

		if (childFiles == null) {
			throw new IOException("Error listing files in directory: " + this);
		}

		final List<FileSystemFile> children = new ArrayList<FileSystemFile>(childFiles.length);
		for (File f : childFiles) {
			children.add(new FileSystemFile(f));
		}
		return children;
	}
}
