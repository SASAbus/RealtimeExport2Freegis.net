/**
 * This programm is used to export the realtimedata from SASA 
 * to the World
 * Copyright (c) 2014 windegger
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *
 *
 */
package org.sasabus.export2Freegis.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.sasabus.export2Freegis.utils.TeqObjects;
import org.sasabus.export2Freegis.utils.TeqXMLUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


/**
 * @author windegger
 *
 */
public class DataReadyManager implements HttpHandler
{

	public static final String DATAACKNOWLEDGE = "./xmlCommands/dataready.xml";
	
	public static final String DATAREQUESTFILE = "./xmlCommands/datarequest.xml";
	
	public static final String DATASEND = "http://realtimebus.tis.bz.it/receiver";

	private String hostname = null;
	
	private int portnumber = 0;
	
	public DataReadyManager(String hostname, int portnumber)
	{
		super();
		this.portnumber = portnumber;
		this.hostname = hostname;
	}
	
	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), "UTF-8"));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpExchange.getResponseBody()));
		try 
		{
			StringBuilder requestStringBuff = new StringBuilder();
			int b;
			while((b = in.read()) != -1)
			{
				requestStringBuff.append((char) b);
			}
			System.out.println(requestStringBuff.toString());
			Scanner sc = new Scanner(new File(DATAACKNOWLEDGE));
			String rdyackstring = "";
			while (sc.hasNextLine())
			{
				rdyackstring += sc.nextLine();
			}
			sc.close();
			SimpleDateFormat date_date = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat date_time = new SimpleDateFormat("HH:mm:ssZ");
	
			Date d = new Date();
			String timestamp = date_date.format(d) + "T" + date_time.format(d);
			timestamp = timestamp.substring(0,  timestamp.length()-2) + ":" + timestamp.substring(timestamp.length() - 2);
	
			rdyackstring = rdyackstring.replaceAll(":timestamp", timestamp);
			
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, rdyackstring.length());
			
			out.write(rdyackstring);
			out.flush();
			DataRequestManager teqrequest = new DataRequestManager(this.hostname, this.portnumber);
			String datarequest = teqrequest.datarequest();
			ArrayList<TeqObjects> requestelements = TeqXMLUtils.extractFromXML(datarequest);
			if(!requestelements.isEmpty())
			{
				Iterator<TeqObjects> iterator = requestelements.iterator();
				System.out.println("List of Elements requested!");
				String geoJson = "{\"type\":\"FeatureCollection\",\"features\":[";
				while(iterator.hasNext())
				{
					TeqObjects object = iterator.next();
					geoJson += object.toGeoJson();
					if(iterator.hasNext())
					{
						geoJson += ",";
					}
				}
				geoJson += "]}";
				System.out.println("GeoJson: " + geoJson);
				HttpPost subrequest = new HttpPost(DATASEND);
	
				StringEntity requestEntity = new StringEntity(geoJson, ContentType.create("application/json", "UTF-8")); 
				
				CloseableHttpClient httpClient = HttpClients.createDefault();
				
				subrequest.setEntity(requestEntity);
				
				CloseableHttpResponse response = httpClient.execute(subrequest);
				System.out.println("Stauts JsonSend Response: " + response.getStatusLine().getStatusCode());
				System.out.println("Status JsonSend Phrase: " + response.getStatusLine().getReasonPhrase());
				httpClient.close();
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		finally
		{
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(httpExchange != null)
				httpExchange.close();
		}
	}

}
