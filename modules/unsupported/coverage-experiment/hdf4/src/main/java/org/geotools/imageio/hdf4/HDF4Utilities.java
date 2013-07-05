/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.hdf4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HDF4Utilities {
	
	public final static String TERASCAN_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
		
	public final static String APS_DATETIME_FORMAT = "EEE MMM dd HH:mm:ss yyyy"; 
	
//	private static Map<String, SimpleDateFormat> DATE_FORMATTER = new HashMap<String,SimpleDateFormat>(2);

	public static Date getDateTime(final String toBeParsed, final String inputFormat){
		final SimpleDateFormat sdf = new SimpleDateFormat(inputFormat,Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return sdf.parse(toBeParsed);
		} catch (ParseException e) {
			return null;
		}
	}
}
