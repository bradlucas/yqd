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
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/**
 * Used to package the arguments for yqd into and object for the Processor to work with.
 * 
 * @author Brad Lucas <brad@beaconhill.com>
 *
 */
public class ProcessorCommand {

	DataDir dataDir; // tofile must equal true	

	Set<String> symbols;

	boolean outStdio = true; // if false output to stdout (ignoreing dataDir)

	String symbolFile;

	public ProcessorCommand() {
		symbols = new LinkedHashSet<String>();
		dataDir = new DataDir();
		symbolFile = "";
		outStdio = true;
	}

	// TODO would be better with a SymbolFile object (no Strings)
	public ProcessorCommand(String d, String s, boolean o) {
		symbols = new LinkedHashSet<String>();
		dataDir = new DataDir(d);
		symbolFile = s;
		outStdio = o;
	}

	public int getCnd() {
		return symbols.size();
	}

	void initialize() {

		// check directory
		verifyCreateDirectory(dataDir.toString());

		// symbols from directory
		List<String> dirSymbols = (new DataDir())
				.getSymbols(dataDir.toString());
		addSymbols(dirSymbols);

		// symbols from file
		List<String> fileSymbols = readSymbolFile(symbolFile);
		addSymbols(fileSymbols);
	}

	void addSymbols(List<String> fileSymbols) {
		if (fileSymbols != null) {
			if (fileSymbols != null) {
				for (String s : fileSymbols) {
					symbols.add(s.toUpperCase());
				}
			}
		}
	}

	private void verifyCreateDirectory(String dir) {
		File f = new File(dir);
		if (!f.isDirectory()) {
			f.mkdir();
		}
		if (!f.isDirectory()) {
			throw new RuntimeException("Can't create directory : " + dir);
		}
	}

	List<String> readSymbolFile(String fileName) {
		List<String> lines = null;
		if (fileName != null && fileName.length() > 0) {
			try {
				lines = FileUtils.readLines(new File(fileName));
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			return lines;
		}
		return lines;
	}

}
