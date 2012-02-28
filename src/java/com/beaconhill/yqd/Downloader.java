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

import java.net.Socket;
import java.net.URL;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

/**
 * Used to download the symbol quote data. Takes a symbol, queries Yahoo and
 * returns the resulting data.
 * 
 * @author Brad Lucas <brad@beaconhill.com>
 * 
 */
public class Downloader {

	public String getSymbolData(String symbol) {
		Log.print("Downloading " + symbol + " ... ");
		String data = "";
		URL url = buildUrl(symbol);
		if (url == null)
			return "";

		try {
			data = download(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.print("\n");
		return data;
	}

	URL buildUrl(String symbol) {
		String rtn = "";

		rtn = "http://ichart.finance.yahoo.com/table.csv?s=" + symbol
				+ "&ignore=.csv";
		URL url = null;
		try {
			url = new URL(rtn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return url;
	}

	String getUri(URL url) {
		String uri = "";

		String path = url.getPath();
		if (path != null) {
			uri += path;
		}
		String query = url.getQuery();
		if (query != null) {
			uri += "?" + query;
		}
		return uri;
	}

	int getPort(URL url) {
		int port = url.getPort();
		if (port == -1) {
			port = url.getDefaultPort();
		}
		return port;
	}

	String download(URL url) throws Exception {
		String data = "";
		try {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "UTF-8");
			HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
			HttpProtocolParams.setUseExpectContinue(params, true);

			BasicHttpProcessor httpproc = new BasicHttpProcessor();
			// Required protocol interceptors
			httpproc.addInterceptor(new RequestContent());
			httpproc.addInterceptor(new RequestTargetHost());
			// Recommended protocol interceptors
			httpproc.addInterceptor(new RequestConnControl());
			httpproc.addInterceptor(new RequestUserAgent());
			httpproc.addInterceptor(new RequestExpectContinue());

			HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

			HttpContext context = new BasicHttpContext(null);
			// HttpHost host = new HttpHost("localhost", 8080);
			HttpHost host = new HttpHost(url.getHost(), getPort(url));

			DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
			ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

			context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
			context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

			if (!conn.isOpen()) {
				Socket socket = new Socket(host.getHostName(), host.getPort());
				conn.bind(socket, params);
			}
			BasicHttpRequest request = new BasicHttpRequest("GET", getUri(url));
			// System.out.println(">> Request URI: " +
			// request.getRequestLine().getUri());

			request.setParams(params);
			httpexecutor.preProcess(request, httpproc, context);
			HttpResponse response = httpexecutor
					.execute(request, conn, context);
			response.setParams(params);
			httpexecutor.postProcess(response, httpproc, context);

			// System.out.println("<< Response: " + response.getStatusLine());
			// System.out.println(EntityUtils.toString(response.getEntity()));
			data = EntityUtils.toString(response.getEntity());
			// System.out.println("==============");
			if (!connStrategy.keepAlive(response, context)) {
				conn.close();
			} else {
				Log.println("Connection kept alive...");
			}
		} finally {
			// conn.close();
		}
		return data;
	}

}