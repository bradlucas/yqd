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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The main command line interface to yqd. The following syntax is supported
 * 
 * <p>
 * [-d DATA_DIR] [-s SYMBOL_FILE] [-o] [SYMBOLS] -h HELP
 * 
 * Description: -h will show some help and do no other processing
 * 
 * -d is for setting the destination directory for downloaded files. This is
 * ignored if -o is set. If not specified then the current directory is used.
 * 
 * -s allows for a file of symbols to process. The file should have a symbol on
 * each line.
 * 
 * -o tells yqd to output the symbol data to stdio. Normall stdio messages are
 * suppressed
 * 
 * SYMBOLS is a list of symbols on the command line
 * 
 * Notes:
 * 
 * All of the commands and options are optional. If you don't specify a DATA_DIR
 * then the current directory is used. You don't need to pass symbols using the
 * SYMBOL_FILE you can just put them as a list on the command line (SYMBOLS). If
 * yqd finds previous symbol files in the DATA_DIR it will reload them as well.
 * 
 * Overview:
 * 
 * Process command line arguments and build a ProcesserCommand object. Then pass the
 * ProcessorCommand object to the Processor for processing.
 * 
 * @author Brad Lucas <brad@beaconhill.com>
 * 
 */
public class CmdLine {

	public static void main(String[] args) throws Exception {
		CmdLine c = new CmdLine();
		c.run(args);
	}

	Options buildOptions() {
		Options options = new Options();

		Option dataDirectoryOption = OptionBuilder.hasArg().withArgName(
				"DATA DIR").withDescription("Data Directory").withLongOpt(
				"data-dir").isRequired(false).create("d");

		Option symbolFileOption = OptionBuilder.hasArg().withArgName(
				"SYMBOL FILE").withDescription("Symbol File").withLongOpt(
				"symbol-file").isRequired(false).create("s");

		// output
		Option outputOption = OptionBuilder.withDescription("Output to STDIO")
				.withLongOpt("output-stdio").isRequired(false).create("o");

		Option helpOption = OptionBuilder.withDescription("Help").withLongOpt(
				"help").isRequired(false).create("h");

		options.addOption(dataDirectoryOption);
		options.addOption(symbolFileOption);
		options.addOption(outputOption);
		options.addOption(helpOption);

		return options;
	}

	void run(String[] args) {
		Options options = buildOptions();

		// Settings settings = Settings.loadSettings(); // gets the settings
		// // available from Env
		// // settings
		// Set<String> symbols = new HashSet<String>();

		String dataDir = "";
		String symbolFile = "";
		boolean outStdio = false;

		CommandLine cmd = null;
		boolean process = true;
		if (args != null && args.length > 0) {
			cmd = parseCmdLine(options, args);
			if (cmd != null) {
				if (cmd.hasOption("d")) {
					dataDir = cmd.getOptionValue("d");
				}

				if (cmd.hasOption("s")) {
					symbolFile = cmd.getOptionValue("s");
				}

				if (cmd.hasOption("o")) {
					outStdio = true; // will output to stdio
					Log.on = false; // don't want other messages
				}

				if (cmd.hasOption("h")) {
					usage(options);
					process = false;
				}
			} 
		} 

		if (process) {
			ProcessorCommand pc = new ProcessorCommand(dataDir, symbolFile,
					outStdio);
			pc.initialize();

			if (cmd != null) {
				List<String> cmdLinesymbols = Arrays.asList(cmd.getArgs());
				pc.addSymbols(cmdLinesymbols);
			}

			Processor p = new Processor();
			if (pc.getCnd() == 0) {
				Log.error("No Symbols Entered, Nothing to Do");
				usage(options);
			} else {
				p.run(pc);
			}
		}
	}

	void usage(Options options) {
		// automatically generate the help statemente
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("yqd  [options]", options);

	}

	CommandLine parseCmdLine(Options options, String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (MissingOptionException missingEx) {
			Log.error(missingEx.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cmd;
	}

}
