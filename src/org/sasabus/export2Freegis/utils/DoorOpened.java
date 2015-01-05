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
public class DoorOpened extends TeqObjects
{	
	
	private DoorOpened()
	{
		super();
	}
	
	public static DoorOpened getFromCSV(String csv, long notification, String ack_timestamp, 
			String notification_timestamp, String notification_valid_timestamp) throws Exception
	{
		String[] list = csv.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		DoorOpened myVehicleTracking = null;
		if(list.length == 12)
		{
			myVehicleTracking = new DoorOpened();
			myVehicleTracking.setVehicleCode(list[0].replaceAll("\"", ""));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");//"yyyy-MM-dd'T'HH:mm:ssZ");
			Date datum = sdf.parse(list[1].replaceAll("\"", ""));//.substring(0,22) +  list[1].substring(23));
			myVehicleTracking.setTimestamp(datum);
			myVehicleTracking.setLat(Double.parseDouble(list[2].replaceAll("\"", "").replaceAll(",", ".")));
			myVehicleTracking.setLon(Double.parseDouble(list[3].replaceAll("\"", "").replaceAll(",", ".")));
			if(list[4].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setOdometro(Double.parseDouble(list[4].replaceAll("\"", "").replaceAll(",", ".")));
			myVehicleTracking.setNotificationId(notification);
			myVehicleTracking.setAck_timestamp(ack_timestamp);
			myVehicleTracking.setNotification_timestamp(notification_timestamp);
			myVehicleTracking.setNotification_valid_timestamp(notification_valid_timestamp);
		}
		return myVehicleTracking;
	}


	
	@Override
	public String toString()
	{
		return "DoorOpened: " + this.getVehicleCode();
	}
	
	private String formatToISO8601(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String timestamp = sdf.format(this.getTimestamp());
		return timestamp.substring(0, 22) + ":" + timestamp.substring(22);
	}
	

	/* (non-Javadoc)
	 * @see org.sasabus.export2Freegis.utils.TeqObjects#getInsertQuery()
	 */
	@Override
	public String getInsertQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
