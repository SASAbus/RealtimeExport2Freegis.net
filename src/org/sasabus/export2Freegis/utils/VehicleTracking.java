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
public class VehicleTracking extends TeqObjects
{
	private int rit_min = 0;
	
	private int rit_sec = 0;
	
	private long vehicleTripNumber = 0;
	
	private long tripId = 0;
	
	private long rotaID = 0;
	
	private int driver = 0;
	
	private String tripCode = "";

	
	
	private VehicleTracking()
	{
		super();
	}
	
	public static VehicleTracking getFromCSV(String csv, long notification, String ack_timestamp, 
			String notification_timestamp, String notification_valid_timestamp) throws Exception
	{
		String[] list = csv.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		VehicleTracking myVehicleTracking = null;
		if(list.length == 12)
		{
			myVehicleTracking = new VehicleTracking();
			myVehicleTracking.setVehicleCode(list[0].replaceAll("\"", ""));
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");//"yyyy-MM-dd'T'HH:mm:ssZ");
			Date datum = sdf.parse(list[1].replaceAll("\"", ""));//.substring(0,22) +  list[1].substring(23));
			myVehicleTracking.setTimestamp(datum);
			myVehicleTracking.setLat(Double.parseDouble(list[2].replaceAll("\"", "").replaceAll(",", ".")));
			myVehicleTracking.setLon(Double.parseDouble(list[3].replaceAll("\"", "").replaceAll(",", ".")));
			myVehicleTracking.setRit_min(Integer.parseInt(list[4].replaceAll("\"", "")));
			myVehicleTracking.setRit_sec(Integer.parseInt(list[5].replaceAll("\"", "")));
			if(list[6].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setVehicleTripNumber(Long.parseLong(list[6].replaceAll("\"", "")));
			if(list[7].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setTripId(Long.parseLong(list[7].replaceAll("\"", "")));
			if(list[8].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setRotaID(Long.parseLong(list[8].replaceAll("\"", "")));
			if(list[9].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setDriver(Integer.parseInt(list[9].replaceAll("\"", "")));
			if(list[10].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setOdometro(Double.parseDouble(list[10].replaceAll("\"", "").replaceAll(",", ".")));
			if(list[11].replaceAll("\"", "").length() != 0)
				myVehicleTracking.setTripCode(list[11].replaceAll("\"", ""));
			myVehicleTracking.setNotificationId(notification);
			myVehicleTracking.setAck_timestamp(ack_timestamp);
			myVehicleTracking.setNotification_timestamp(notification_timestamp);
			myVehicleTracking.setNotification_valid_timestamp(notification_valid_timestamp);
		}
		return myVehicleTracking;
	}

	/**
	 * @return the rit_min
	 */
	public int getRit_min() {
		return rit_min;
	}

	/**
	 * @param rit_min the rit_min to set
	 */
	public void setRit_min(int rit_min) {
		this.rit_min = rit_min;
	}

	/**
	 * @return the rit_sec
	 */
	public int getRit_sec() {
		return rit_sec;
	}

	/**
	 * @param rit_sec the rit_sec to set
	 */
	public void setRit_sec(int rit_sec) {
		this.rit_sec = rit_sec;
	}

	/**
	 * @return the vehicleTripNumber
	 */
	public long getVehicleTripNumber() {
		return vehicleTripNumber;
	}

	/**
	 * @param vehicleTripNumber the vehicleTripNumber to set
	 */
	public void setVehicleTripNumber(long vehicleTripNumber) {
		this.vehicleTripNumber = vehicleTripNumber;
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
	 * @return the rotaID
	 */
	public long getRotaID() {
		return rotaID;
	}

	/**
	 * @param rotaID the rotaID to set
	 */
	public void setRotaID(long rotaID) {
		this.rotaID = rotaID;
	}

	/**
	 * @return the driver
	 */
	public int getDriver() {
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(int driver) {
		this.driver = driver;
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
		return "VehicleTracking: " + this.getVehicleCode() + ";" + this.getTripCode();
	}
	
	private String formatToISO8601(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String timestamp = sdf.format(this.getTimestamp());
		return timestamp.substring(0, 22) + ":" + timestamp.substring(22);
	}
	

	
	public String toGeoJson()
	{
		/*
		 * {"type":"Feature","geometry":{"type":"Point","coordinates":[11.346833,46.483133]},"properties":{"gps_date":"2014-12-22T10:53:28+01:00",
		 * "delay_sec":"2","frt_fid":"001107200030","notification_id":"635548426854018792",
		 * "notification_date":"2014-12-22T09:58:06+00:00","notification_validity_date":"2014-12-22T09:58:11+00:00",
		 * "acknowledge_date":"2014-12-22T09:58:11+00:00"}}
		 */
		
		return "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + this.getLon() + "," + this.getLat() +
				"]},\"properties\":{\"gps_date\":\"" + formatToISO8601(this.getTimestamp()) + "\",\"delay_sec\":\"" + this.getRit_sec() + "\","
						+ "\"frt_fid\":\"" + this.getTripCode() + "\",\"notification_id\":\"" + this.getNotificationId() + "\","
						+ "\"notification_date\":\"" + this.getNotification_timestamp() + "\","
						+ "\"notification_validity_date\":\"" + this.getNotification_valid_timestamp() + "\","
						+ "\"vehicleCode\":\"" + this.getVehicleCode() + "\","
						+ "\"acknowledge_date\":\"" + this.getAck_timestamp() + "\"}}";
	}

	
	
}
