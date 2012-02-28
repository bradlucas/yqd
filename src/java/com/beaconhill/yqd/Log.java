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

/**
 * A basic logging mechanism. With the -o flag we don't want any messages going to
 * stdio so only the data is output. For this all messages are sent through this class
 * which has a flag to tell if it should actually output the msg.
 * 
 * <p>
 * CmdLine sill set the Log.on to false it the -o flag is set
 * </p>
 * 
 * @author Brad Lucas <brad@beaconhill.com>
 *
 */
public class Log {
	
	public static boolean on = true;
	
	public static void print(String msg) {
		if (on) {
			System.out.print(msg);
		}
	}
	
	public static void println(String msg) {
		if (on) {
			System.out.println(msg);
		}
	}
	
	public static void error(String msg) {
		System.err.println(msg);
	}
	
}
