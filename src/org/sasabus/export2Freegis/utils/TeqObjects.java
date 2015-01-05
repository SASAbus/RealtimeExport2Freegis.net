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

import java.util.Date;


/**
 * @author windegger
 *
 */
public abstract class TeqObjects
{
	
	private String vehicleCode = null;
	
	private Date timestamp = null;
	
	private double lat = 0;
	
	private double lon = 0;
	
	private double odometro = 0;
	
	private long notificationId = 0;
	
	private String ack_timestamp = null;
	
	private String notification_timestamp = null;
	
	private String notification_valid_timestamp = null;

	/**
	 * @return the ack_timestamp
	 */
	public String getAck_timestamp() {
		return ack_timestamp;
	}

	/**
	 * @param ack_timestamp the ack_timestamp to set
	 */
	public void setAck_timestamp(String ack_timestamp) {
		this.ack_timestamp = ack_timestamp;
	}

	/**
	 * @return the notification_timestamp
	 */
	public String getNotification_timestamp() {
		return notification_timestamp;
	}

	/**
	 * @param notification_timestamp the notification_timestamp to set
	 */
	public void setNotification_timestamp(String notification_timestamp) {
		this.notification_timestamp = notification_timestamp;
	}

	/**
	 * @return the notification_valid_timestamp
	 */
	public String getNotification_valid_timestamp() {
		return notification_valid_timestamp;
	}

	/**
	 * @param notification_valid_timestamp the notification_valid_timestamp to set
	 */
	public void setNotification_valid_timestamp(String notification_valid_timestamp) {
		this.notification_valid_timestamp = notification_valid_timestamp;
	}

	/**
	 * @return the notificationId
	 */
	public long getNotificationId() {
		return notificationId;
	}

	/**
	 * @param notificationId the notificationId to set
	 */
	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the vehicleCode
	 */
	public String getVehicleCode() {
		return vehicleCode;
	}

	/**
	 * @param vehicleCode the vehicleCode to set
	 */
	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * @return the odometro
	 */
	public double getOdometro() {
		return odometro;
	}

	/**
	 * @param odometro the odometro to set
	 */
	public void setOdometro(double odometro) {
		this.odometro = odometro;
	}
	 
	 public abstract String getInsertQuery();
}
