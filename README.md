# yqd
======

Yqd is a command line tool for downloading historical price information from Yahoo. It accepts a group of symbols on the command line or from a file and downloads each symbols historical data to a CSV file. Yqd is useful for end of day scripts to download price information regularly. Also, you can quickly verify data by running yqd manually and either editing the resulting file or passing an argument to yqd to display the results to stdio.

## Installation

### To build yqd

To build yqd run Ant.

    $ ant

The dist folder will contain the compiled application name yqd-cmdline-VERSION.jar. This jar is built with 'one-jar' so you can run it directly.

## Usage

Enter the following at the command line to see the options:

      $ java -jar yqd-cmdline.jar -h

To download the quote history for google enter the following:

   $ java -jar yqd-cmdline.jar goog

To download the quote history for google to the screen:

   $ java -jar yqd-cmdline.jar -o goog


## Feedback

If you try yqd and have a suggestion please let me know.

   brad@beaconhill.com
