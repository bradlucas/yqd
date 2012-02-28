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
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Takes the commands as a ProcessorCommand object and processes them using the
 * Downloader.
 * 
 * @author Brad Lucas <brad@beaconhill.com>
 * 
 */
public class Processor {

	Downloader downloader = new Downloader();

	public void run(ProcessorCommand cmd) {
		for (String s : cmd.symbols) {
			if (cmd.outStdio) {
				download(s);
			} else {
				download(cmd.dataDir.toString(), s);
			}
		}
	}

	public void download(String symbol) {
		try {
			String data = downloader.getSymbolData(symbol);
			if (data.startsWith("<!doctype html")) {
				System.err.println("ERROR: not found " + symbol);
			} else {
				System.out.println(data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void download(String dataDir, String s) {
		try {
			String fileName = dataDir + "/" + s + ".csv";
			String data = downloader.getSymbolData(s);
			if (data.startsWith("<!doctype html")) {
				System.err.println("ERROR: the symbol '" + s
						+ "' was not found ");
			} else {
				FileUtils.writeStringToFile(new File(fileName), data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void download(String dataDir, List<String> symbols) {
		try {
			Downloader d = new Downloader();
			for (String s : symbols) {
				String fileName = dataDir + "/" + s + ".csv";
				String data = d.getSymbolData(s);
				if (data.startsWith("<!doctype html")) {
					System.err.println("ERROR: not found " + s);
				} else {
					FileUtils.writeStringToFile(new File(fileName), data);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
