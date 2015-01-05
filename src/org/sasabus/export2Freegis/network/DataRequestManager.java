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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * @author windegger
 *
 */
public class DataRequestManager
{
	public static final String DATAREQUEST = "./xmlCommands/datarequest.xml";

	private String address = null;
	private int portnumber_sender = 0;

	public DataRequestManager(String address, int portnumber_sender) {
		this.address = address;
		this.portnumber_sender = portnumber_sender;
	}

	
	public String datarequest() throws IOException {
		Scanner sc = new Scanner(new File(DATAREQUEST));
		String subscriptionstring = "";
		while (sc.hasNextLine())
		{
			subscriptionstring += sc.nextLine();
		}
		sc.close();
		SimpleDateFormat date_date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat date_time = new SimpleDateFormat("HH:mm:ssZ");

		Date d = new Date();
		String timestamp = date_date.format(d) + "T" + date_time.format(d);
		timestamp = timestamp.substring(0,  timestamp.length()-2) + ":" + timestamp.substring(timestamp.length() - 2);

		subscriptionstring = subscriptionstring.replaceAll(":timestamp", timestamp);

		String requestString = "http://" + this.address + ":" + this.portnumber_sender + "/TmEvNotificationService/gms/polldata.xml";
		
		HttpPost subrequest = new HttpPost(requestString);

		StringEntity requestEntity = new StringEntity(subscriptionstring, ContentType.create("text/xml", "ISO-8859-1")); 
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		subrequest.setEntity(requestEntity);
		
		CloseableHttpResponse response = httpClient.execute(subrequest);
		//System.out.println("Stauts Response: " + response.getStatusLine().getStatusCode());
		//System.out.println("Status Phrase: " + response.getStatusLine().getReasonPhrase());
		HttpEntity responseEntity = response.getEntity();
		String responsebody = "";
	    if (responseEntity != null) {
	    	responsebody = EntityUtils.toString(responseEntity);
	    }
		return responsebody;
	}
}
