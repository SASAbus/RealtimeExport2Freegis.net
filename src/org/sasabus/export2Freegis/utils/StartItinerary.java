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
package org.sasabus.export2Freegis.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author windegger
 *
 */
public class StartItinerary extends TeqObjects
{	
	
	private long tripId = 0;
		
	private String tripCode = "";

	
	
	private StartItinerary()
	{
		super();
	}
	
	public static StartItinerary getFromCSV(String csv, long notification, String ack_timestamp, 
			String notification_timestamp, String notification_valid_timestamp) throws Exception
	{
		String[] list = csv.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		StartItinerary myVehicleTracking = null;
		if(list.length == 8)
		{
			myVehicleTracking = new StartItinerary();
			myVehicleTracking.setVehicleCode(list[0].replaceAll("\"", ""));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");//"yyyy-MM-dd'T'HH:mm:ssZ");
			Date datum = sdf.parse(list[1].replaceAll("\"", ""));//.substring(0,22) +  list[1].substring(23));
			myVehicleTracking.setTimestamp(datum);
			myVehicleTracking.setLat(Double.parseDouble(list[2].replaceAll("\"", "").replaceAll(",", ".")));
			myVehicleTracking.setLon(Double.parseDouble(list[3].replaceAll("\"", "").replaceAll(",", ".")));
			if(list[4].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setTripId(Long.parseLong(list[4].replaceAll("\"", "")));
			if(list[6].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setOdometro(Double.parseDouble(list[6].replaceAll("\"", "").replaceAll(",", ".")));
			if(list[7].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setTripCode(list[7].replaceAll("\"", ""));
			myVehicleTracking.setNotificationId(notification);
			myVehicleTracking.setAck_timestamp(ack_timestamp);
			myVehicleTracking.setNotification_timestamp(notification_timestamp);
			myVehicleTracking.setNotification_valid_timestamp(notification_valid_timestamp);
		}
		return myVehicleTracking;
	}

	
	/**
	 * @return the tripId
	 */
	public long getTripId() {
		return tripId;
	}

	/**
	 * @param tripId the tripId to set
	 */
	public void setTripId(long tripId) {
		this.tripId = tripId;
	}

	
	/**
	 * @return the tripCode
	 */
	public String getTripCode() {
		return tripCode;
	}

	/**
	 * @param tripCode the tripCode to set
	 */
	public void setTripCode(String tripCode) {
		this.tripCode = tripCode;
	}
	
	
	@Override
	public String toString()
	{
		return "StartItinerary: " + this.getVehicleCode() + ";" + this.getTripCode();
	}
	
	

	
	
}
