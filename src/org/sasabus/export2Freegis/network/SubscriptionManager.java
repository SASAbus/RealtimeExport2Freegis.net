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
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
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
public class SubscriptionManager
{
	public static final String UNSUBSCRIPTIONFILE = "./xmlCommands/delrequest.xml";
	
	public static final String[] SUBFILEARRAY = {"./xmlCommands/subrequest_vt.xml", 
		"./xmlCommands/subrequest_od.xml", "./xmlCommands/subrequest_istart.xml", 
		"./xmlCommands/subrequest_istop.xml"};

	private String address = null;
	private int portnumber_sender = 0;

	public SubscriptionManager(String address, int portnumber_sender) {
		this.address = address;
		this.portnumber_sender = portnumber_sender;
	}

	public boolean subscribe() throws IOException {
		for(int i = 0; i < SUBFILEARRAY.length; ++i)
		{
			Scanner sc = new Scanner(new File(SUBFILEARRAY[i]));
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
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DATE, 1);
			d = c.getTime();
			String valid_until = date_date.format(d) + "T" + date_time.format(d);
			valid_until = valid_until.substring(0,  valid_until.length()-2) + ":" + valid_until.substring(valid_until.length() - 2);
			subscriptionstring = subscriptionstring.replaceAll(":timestamp_valid", valid_until);
			subscriptionstring = subscriptionstring.replaceAll(":timestamp", timestamp);
	
			System.out.println("Subscriptionstring: " + subscriptionstring);
			
			String requestString = "http://" + this.address + ":" + this.portnumber_sender + "/TmEvNotificationService/gms/subscription.xml";
			
			HttpPost subrequest = new HttpPost(requestString);
	
			StringEntity requestEntity = new StringEntity(subscriptionstring, ContentType.create("text/xml", "ISO-8859-1")); 
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			subrequest.setEntity(requestEntity);
			
			CloseableHttpResponse response = httpClient.execute(subrequest);
			
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{
				try 
				{
					System.out.println("Stauts Response: " + response.getStatusLine().getStatusCode());
					System.out.println("Status Phrase: " + response.getStatusLine().getReasonPhrase());
					HttpEntity responseEntity = response.getEntity();
				    if (responseEntity != null) {
				    	String responsebody = EntityUtils.toString(responseEntity);
				    	System.out.println(responsebody);
				    }
				} 
				finally 
				{
				    response.close();
				    httpClient.close();
				}
				return false;
			}
			System.out.println("Subscription of " + SUBFILEARRAY[i] + " with Statuscode " + response.getStatusLine().getStatusCode());
		}
		return true;

	}

	
	public void unsubscribe() throws IOException {
		Scanner sc = new Scanner(new File(UNSUBSCRIPTIONFILE));
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

		String requestString = "http://" + this.address + ":" + this.portnumber_sender + "/TmEvNotificationService/gms/subscription.xml";
		
		HttpPost subrequest = new HttpPost(requestString);

		StringEntity requestEntity = new StringEntity(subscriptionstring, ContentType.create("text/xml", "ISO-8859-1")); 
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		subrequest.setEntity(requestEntity);
		
		CloseableHttpResponse response = httpClient.execute(subrequest);
		
		if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
		{
			try 
			{
				System.out.println("Stauts Response: " + response.getStatusLine().getStatusCode());
				System.out.println("Status Phrase: " + response.getStatusLine().getReasonPhrase());
				HttpEntity responseEntity = response.getEntity();
			    if (responseEntity != null) {
			    	String responsebody = EntityUtils.toString(responseEntity);
			    	System.out.println(responsebody);
			    }
			} 
			finally 
			{
			    response.close();
			    httpClient.close();
			}
		}
	}

}
