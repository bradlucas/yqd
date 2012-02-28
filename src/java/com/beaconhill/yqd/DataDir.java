/*
 *    This file is part of yqd.
 *
 *    yqd is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    yqd is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with yqd.  If not, see <http:www.gnu.org/licenses/>.
 */
package com.beaconhill.yqd;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FilenameUtils;

/**
 * Used to manage the destination directory for symbol files. Has some special support for 
 * retreiving the names of previously downloaded files.
 * 
 * @author Brad Lucas <brad@beaconhill.com>
 *
 */
public class DataDir {

	String dataDir;

	public DataDir() {
		dataDir = getCurrentDirectory();
	}

	public DataDir(String d) {
		if (d == null || d.length() == 0) {
			dataDir = getCurrentDirectory();
		} else {
			dataDir = d;
		}
	}

	public void setDataDir(String d) {
		dataDir = d;
	}

	public String toString() {
		return dataDir;
	}

	private String getCurrentDirectory() {
		String currentDir = System.getProperty("user.dir");
		return currentDir;
	}

	/**
	 * Return a list of existing Symbols from the files in the DataDir Each
	 * symbol is represented by a file named SYMBOL.csv
	 */

	List<String> getSymbolsSubs(String directoryPath) {
		// get the list of files *.cvs in DATA_DIR
		List<String> symbols = null;

		MyDirectoryWalkerString walker = new MyDirectoryWalkerString();
		try {
			symbols = walker.getFiles(new File(directoryPath));
		} catch (IOException ioEx) {
			//
		}
		return symbols;
	}

	List<String> getSymbols(String directoryPath) {

		File dir = new File(directoryPath);

		String[] children = dir.list();

		// It is also possible to filter the list of returned files.
		// This example does not return any files that start with `.'.
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lname = name.toLowerCase();
				return lname.endsWith(".csv");
			}
		};
		children = dir.list(filter);
		List<String> rtn = new ArrayList<String>();
		for (String s : children) {
			try {
				rtn.add(FilenameUtils.getBaseName((new File(s)
						.getCanonicalPath()).toUpperCase()));
			} catch (IOException ioEx) {
				System.err.println(ioEx.getMessage());
			}
		}

		return rtn;
	}

	private static class MyDirectoryWalkerString extends DirectoryWalker {

		private MyDirectoryWalkerString() {
			super();
		}

		private List<String> getFiles(File dir) throws IOException {
			List<String> results = new ArrayList<String>();
			walk(dir, results);
			return results;
		}

		protected boolean handleDirectory(File directory, int depth,
				Collection results) {
			return true;

		}

		protected void handleFile(File file, int depth, Collection results)
				throws IOException {
			if (file.getCanonicalPath().endsWith(".csv")) {
				results.add(FilenameUtils.getBaseName(file.getCanonicalPath())
						.toUpperCase());
			}
		}
	}

}
