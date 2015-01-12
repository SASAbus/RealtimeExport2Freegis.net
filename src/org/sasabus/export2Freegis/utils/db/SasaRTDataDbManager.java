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

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.sasabus.export2Freegis.utils.DoorOpened;
import org.sasabus.export2Freegis.utils.EndItinerary;
import org.sasabus.export2Freegis.utils.StartItinerary;
import org.sasabus.export2Freegis.utils.TeqObjects;
import org.sasabus.export2Freegis.utils.VehicleTracking;


/**
 * @author windegger
 *
 */
public class SasaRTDataDbManager
{

	public static final String dbconn_properties = "./DBconnection.properties";
	
	
	private Connection conn = null;
	
	private PreparedStatement vehicleTracking = null;
	
	private PreparedStatement doorOpened = null;
	
	private PreparedStatement startItinerary = null;
	
	private PreparedStatement stopItinerary = null;
	
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

	
	public SasaRTDataDbManager() throws Exception
	{
		initConnection();
	}
	
	private void initConnection() throws Exception
	{
		if(conn == null || conn.isClosed())
		{
			Properties properties = new Properties();
			properties.load(new FileInputStream(dbconn_properties));
			String url = properties.getProperty("jdbc.url");
			String driver = properties.getProperty("jdbc.driver");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(true);
			Statement statement = conn.createStatement();
			statement.executeQuery("SET NAMES utf8");
			vehicleTracking = conn.prepareStatement(query_vehicle);
			doorOpened = conn.prepareStatement(query_dooropened);
			startItinerary = conn.prepareStatement(query_startitinerary);
			stopItinerary = conn.prepareStatement(query_stopitinerary);
		}
	}
	
	private synchronized void insertVehicleTracking(VehicleTracking vt) throws Exception
	{
		initConnection();
		if(vehicleTracking != null && !vehicleTracking.isClosed())
		{
			int vehicleNumber = 0;
			if(vt.getVehicleCode().indexOf(' ') != -1)
			{
				vehicleNumber = Integer.parseInt(vt.getVehicleCode().substring(0, vt.getVehicleCode().indexOf(' ')));
			}
			else
			{
				vehicleNumber = Integer.parseInt(vt.getVehicleCode());
			}
			vehicleTracking.setInt(1, vehicleNumber);
			vehicleTracking.setLong(2, vt.getVehicleTripNumber());
			SimpleDateFormat newdate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			vehicleTracking.setString(3, newdate.format(vt.getTimestamp()));
			vehicleTracking.setDouble(4, vt.getLat());
			vehicleTracking.setDouble(5, vt.getLon());
			vehicleTracking.setInt(6, vt.getRit_min());
			vehicleTracking.setInt(7, vt.getRit_sec());
			vehicleTracking.setLong(8, vt.getRotaID());
			vehicleTracking.setLong(9, vt.getTripId());
			vehicleTracking.setInt(10, vt.getDriver());
			vehicleTracking.setDouble(11, vt.getOdometro());
			if(vt.getTripCode().equals(""))
				vehicleTracking.setLong(12, 0);
			else
				vehicleTracking.setLong(12, Long.parseLong(vt.getTripCode()));
			vehicleTracking.execute();
			
		}
	}
	
	private synchronized void insertDoorOpened(DoorOpened doorOpen) throws Exception
	{
		initConnection();
		if(doorOpened != null && !doorOpened.isClosed())
		{
			int vehicleNumber = 0;
			if(doorOpen.getVehicleCode().indexOf(' ') != -1)
			{
				vehicleNumber = Integer.parseInt(doorOpen.getVehicleCode().substring(0, doorOpen.getVehicleCode().indexOf(' ')));
			}
			else
			{
				vehicleNumber = Integer.parseInt(doorOpen.getVehicleCode());
			}
			doorOpened.setInt(1, vehicleNumber);
			SimpleDateFormat newdate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			doorOpened.setString(2, newdate.format(doorOpen.getTimestamp()));
			doorOpened.setDouble(3, doorOpen.getLat());
			doorOpened.setDouble(4, doorOpen.getLon());
			doorOpened.setDouble(5, doorOpen.getOdometro());
			doorOpened.execute();
			
		}
	}
	
	private synchronized void insertStartItinerary(StartItinerary si) throws Exception
	{
		initConnection();
		if(startItinerary != null && !startItinerary.isClosed())
		{
			int vehicleNumber = 0;
			if(si.getVehicleCode().indexOf(' ') != -1)
			{
				vehicleNumber = Integer.parseInt(si.getVehicleCode().substring(0, si.getVehicleCode().indexOf(' ')));
			}
			else
			{
				vehicleNumber = Integer.parseInt(si.getVehicleCode());
			}
			startItinerary.setInt(1, vehicleNumber);
			SimpleDateFormat newdate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			startItinerary.setString(2, newdate.format(si.getTimestamp()));
			startItinerary.setDouble(3, si.getLat());
			startItinerary.setDouble(4, si.getLon());
			startItinerary.setLong(5, si.getTripId());
			startItinerary.setDouble(6, si.getOdometro());
			if(si.getTripCode().equals(""))
				startItinerary.setLong(7, 0);
			else
				startItinerary.setLong(7, Long.parseLong(si.getTripCode()));
			startItinerary.execute();
			
		}
	}
	
	private synchronized void insertStopItinerary(EndItinerary ei) throws Exception
	{
		initConnection();
		if(stopItinerary != null && !stopItinerary.isClosed())
		{
			int vehicleNumber = 0;
			if(ei.getVehicleCode().indexOf(' ') != -1)
			{
				vehicleNumber = Integer.parseInt(ei.getVehicleCode().substring(0, ei.getVehicleCode().indexOf(' ')));
			}
			else
			{
				vehicleNumber = Integer.parseInt(ei.getVehicleCode());
			}
			SimpleDateFormat newdate = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			stopItinerary.setInt(1, vehicleNumber);
			stopItinerary.setString(2, newdate.format(ei.getTimestamp()));
			stopItinerary.setDouble(3, ei.getLat());
			stopItinerary.setDouble(4, ei.getLon());
			stopItinerary.setLong(5, ei.getTripId());
			stopItinerary.setDouble(6, ei.getOdometro());
			if(ei.getTripCode().equals(""))
				stopItinerary.setLong(7, 0);
			else
				stopItinerary.setLong(7, Long.parseLong(ei.getTripCode()));
			stopItinerary.execute();
			
		}
	}
	
	
	
	public synchronized void insertIntoDatabase(ArrayList<TeqObjects> elements) throws Exception
	{
		Iterator<TeqObjects> iterator = elements.iterator();
		while(iterator.hasNext())
		{
			TeqObjects item = iterator.next();
			if(item instanceof VehicleTracking)
			{
				insertVehicleTracking((VehicleTracking)item);
			}
			else if(item instanceof StartItinerary)
			{
				insertStartItinerary((StartItinerary)item);
			}
			else if(item instanceof EndItinerary)
			{
				insertStopItinerary((EndItinerary)item);
			}
			else if(item instanceof DoorOpened)
			{
				insertDoorOpened((DoorOpened)item);
			}
		}
	}
	
	public synchronized void closeConnection() throws Exception
	{
		System.out.println("CLOSE CLOSE CLOSE CLOSE CLOSE CLOSE!!!");
		conn.close();
	}
}
