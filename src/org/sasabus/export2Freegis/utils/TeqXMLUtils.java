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

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * @author windegger
 *
 */
public class TeqXMLUtils
{
	public static ArrayList<TeqObjects> extractFromXML(String xml) throws Exception
	{
		System.out.println("XML Parsing:\nLength of xml: " + xml.length());
		if(xml.trim().equals("") || xml.length() == 0)
			return new ArrayList<>();
		return parseXmlFile(xml.trim());
	}
	
	
	private static ArrayList<TeqObjects> parseXmlFile(String textContent) throws Exception {
		// get the factory
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(textContent));

		// parse using builder to get DOM representation of the XML file
		Document dom = db.parse(is);

		Element gmsmessageElement = dom.getDocumentElement();
		NodeList nl = gmsmessageElement.getElementsByTagName("GMSNotification");
		ArrayList<TeqObjects> elementslist = new ArrayList<>();
		Element acknowledge = (Element)gmsmessageElement.getElementsByTagName("Acknowledge").item(0);
		String ack_timestamp = "";
		if(acknowledge != null)
		{
			ack_timestamp = acknowledge.getAttribute("TimeStamp");
		}
		if (nl != null && nl.getLength() > 0)
		{
			for (int i = 0; i < nl.getLength(); i++)
			{
				// get the GSMNotification element
				Element gmsnotificationelement = (Element) nl.item(i);
				
				if(gmsnotificationelement.getChildNodes().getLength() == 7)
				{
					String type = gmsnotificationelement.getChildNodes().item(1).getTextContent();
					long notificationId = Long.parseLong(gmsnotificationelement.getChildNodes().item(3).getTextContent());
					String csvcontent = gmsnotificationelement.getChildNodes().item(6).getTextContent();
					if(type.equalsIgnoreCase("VehicleTracking"))
					{
						VehicleTracking vtnew = VehicleTracking.getFromCSV(csvcontent, notificationId, ack_timestamp, gmsnotificationelement.getAttribute("TimeStamp"), gmsnotificationelement.getAttribute("ValidUntilTimeStamp"));
						elementslist.add(vtnew);
					}
				}
				

			}
		}
		return elementslist;

	}
}
