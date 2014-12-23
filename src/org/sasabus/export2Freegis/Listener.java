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
package org.sasabus.export2Freegis;



import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import org.sasabus.export2Freegis.network.DataReadyManager;
import org.sasabus.export2Freegis.network.SubscriptionManager;

import com.sun.net.httpserver.HttpServer;

/**
 * @author windegger
 *
 */
public class Listener
{

	
	public static final int PORTNUMBER_LISTENER = 8083;
	
	public static final int PORTNUMBER_DATARECIEVER = 9093;
		
	public static final String hostname_dataserver = "192.168.2.4";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket portlistener = null;
		try
		{
			SubscriptionManager teqserver = new SubscriptionManager(hostname_dataserver, 
					PORTNUMBER_DATARECIEVER);
			teqserver.unsubscribe();
			System.out.println("Unsubscribing for cleaning the System");
			if(!teqserver.subscribe())
			{
				System.exit(-1);
			}
			System.out.println("Successfully subscribed");
			HttpServer server = HttpServer.create(new InetSocketAddress(PORTNUMBER_LISTENER), 0);
	        server.createContext("/TmEvNotificationConsumer/gms/dataready.xml", new DataReadyManager(hostname_dataserver, PORTNUMBER_DATARECIEVER));
	        server.setExecutor(Executors.newCachedThreadPool());
	        server.start();
			
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		finally
		{
			try
			{
				if(portlistener != null)
					portlistener.close();
			}
			catch(Exception e)
			{
				
			}
		}
	}

}
