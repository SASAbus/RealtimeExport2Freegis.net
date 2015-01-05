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
package org.sasabus.export2Freegis.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.sasabus.export2Freegis.utils.DoorOpened;
import org.sasabus.export2Freegis.utils.EndItinerary;
import org.sasabus.export2Freegis.utils.StartItinerary;
import org.sasabus.export2Freegis.utils.TeqObjects;
import org.sasabus.export2Freegis.utils.VehicleTracking;

import com.sun.org.apache.xml.internal.utils.StopParseException;


/**
 * @author windegger
 *
 */
public class SasaRTDataDbManager
{

	
	private static Connection conn = null;
	
	private static PreparedStatement vehicleTracking = null;
	
	private static PreparedStatement doorOpened = null;
	
	private static PreparedStatement startItinerary = null;
	
	private static PreparedStatement stopItinerary = null;
	
	private static final String query_vehicle = "INSERT INTO VehicleTracking (CodiceVeicolo, CodiceTurno, Timestamp, Latitudine," +
			  " Longitudine, Rit_min, Rit_sec, IDTurnoMacchina, IDCorsa, MatricolaAutista, Odometro, CodiceCorsa) " +
			  " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String query_startitinerary = "INSERT INTO StartItinerary (CodiceVeicolo, Timestamp, Latitudine, " + 
			  " Longitudine, IDCorsa, Odometro, CodiceCorsa) " +
			  " VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String query_stopitinerary = "INSERT INTO EndItinerary (CodiceVeicolo, Timestamp, Latitudine, " +
			  " Longitudine, IDCorsa, Odometro, CodiceCorsa) " +
			  " VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String query_dooropened = "INSERT INTO DoorOpened (CodiceVeicolo, Timestamp, Latitudine, Longitudine, " +
			  " Odometro)" +
			  " VALUES (?, ?, ?, ?, ?)";

	
	public static synchronized void initConnection() throws Exception
	{
		if(conn != null && !conn.isClosed())
		{
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("DBconnection.properties"));
			String url = properties.getProperty("jdbc.url");
			String driver = properties.getProperty("jdbc.driver");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, username, password);
			Statement statement = conn.createStatement();
			statement.executeQuery("SET NAMES utf8");
			vehicleTracking = conn.prepareStatement(query_vehicle);
			doorOpened = conn.prepareStatement(query_dooropened);
			startItinerary = conn.prepareStatement(query_startitinerary);
			stopItinerary = conn.prepareStatement(query_stopitinerary);
		}
	}
	
	private static synchronized void insertVehicleTracking(VehicleTracking vt) throws Exception
	{
		System.out.println("Before INSERTING!!!!!");
		if(vehicleTracking != null && !vehicleTracking.isClosed())
		{
			System.out.println("INSERTING VEHICLES!!!!!!!!");
			vehicleTracking.setString(1, vt.getVehicleCode());
			vehicleTracking.setLong(2, vt.getVehicleTripNumber());
			vehicleTracking.setString(3, vt.getNotification_timestamp());
			vehicleTracking.setDouble(4, vt.getLat());
			vehicleTracking.setDouble(5, vt.getLon());
			vehicleTracking.setInt(6, vt.getRit_min());
			vehicleTracking.setInt(7, vt.getRit_sec());
			vehicleTracking.setLong(8, vt.getRotaID());
			vehicleTracking.setLong(9, vt.getTripId());
			vehicleTracking.setInt(10, vt.getDriver());
			vehicleTracking.setDouble(11, vt.getOdometro());
			vehicleTracking.setString(12, vt.getTripCode());
			vehicleTracking.execute();
			
		}
	}
	
	private static synchronized void insertDoorOpened(DoorOpened doorOpen) throws Exception
	{
		if(doorOpened != null && !doorOpened.isClosed())
		{
			doorOpened.setString(1, doorOpen.getVehicleCode());
			doorOpened.setString(2, doorOpen.getNotification_timestamp());
			doorOpened.setDouble(3, doorOpen.getLat());
			doorOpened.setDouble(4, doorOpen.getLon());
			doorOpened.setDouble(5, doorOpen.getOdometro());
			doorOpened.execute();
			
		}
	}
	
	private static synchronized void insertStartItinerary(StartItinerary si) throws Exception
	{
		if(startItinerary != null && !startItinerary.isClosed())
		{
			startItinerary.setString(1, si.getVehicleCode());
			startItinerary.setString(2, si.getNotification_timestamp());
			startItinerary.setDouble(3, si.getLat());
			startItinerary.setDouble(4, si.getLon());
			startItinerary.setLong(5, si.getTripId());
			startItinerary.setDouble(6, si.getOdometro());
			startItinerary.setString(7, si.getTripCode());
			startItinerary.execute();
			
		}
	}
	
	private static synchronized void insertStopItinerary(EndItinerary ei) throws Exception
	{
		if(stopItinerary != null && !stopItinerary.isClosed())
		{
			stopItinerary.setString(1, ei.getVehicleCode());
			stopItinerary.setString(2, ei.getNotification_timestamp());
			stopItinerary.setDouble(3, ei.getLat());
			stopItinerary.setDouble(4, ei.getLon());
			stopItinerary.setLong(5, ei.getTripId());
			stopItinerary.setDouble(6, ei.getOdometro());
			stopItinerary.setString(7, ei.getTripCode());
			stopItinerary.execute();
			
		}
	}
	
	
	
	public static synchronized void insertIntoDatabase(ArrayList<TeqObjects> elements) throws Exception
	{
		Iterator<TeqObjects> iterator = elements.iterator();
		while(iterator.hasNext())
		{
			TeqObjects item = iterator.next();
			if(item instanceof VehicleTracking)
			{
				insertVehicleTracking((VehicleTracking)item);
				System.out.println("Insert VehicleTracking " + item.getVehicleCode());
			}
			else if(item instanceof StartItinerary)
			{
				insertStartItinerary((StartItinerary)item);
				System.out.println("Insert StartItinerary " + item.getVehicleCode());
			}
			else if(item instanceof EndItinerary)
			{
				insertStopItinerary((EndItinerary)item);
				System.out.println("Insert EndItinerary " + item.getVehicleCode());
			}
			else if(item instanceof DoorOpened)
			{
				insertDoorOpened((DoorOpened)item);
				System.out.println("Insert DoorOpened " + item.getVehicleCode());
			}
		}
	}
	
	public static synchronized void closeConnection() throws Exception
	{
		System.out.println("CLOSE CLOSE CLOSE CLOSE CLOSE CLOSE!!!");
		conn.close();
	}
}
