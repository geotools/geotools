/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.vpf.io;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * VPFDate.java Created: Tue Jan 28 20:50:51 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 *
 * @source $URL$
 * 
 */
public class VPFDate {
    /**
     * Describe variable <code>sdf</code> here.
     *
     */
    private SimpleDateFormat sdf = null;

    /**
     * Describe variable <code>dateBin</code> here.
     *
     */
    private byte[] dateBin = null;

    /**
     * Creates a new <code>VPFDate</code> instance.
     *
     * @param date a <code>byte[]</code> value
     */
    public VPFDate(byte[] date) {
        dateBin = (byte[]) date.clone();
        initialize();
    }

    /**
     * Creates a new <code>VPFDate</code> instance.
     *
     * @param date a <code>String</code> value
     */
    public VPFDate(String date) {
        dateBin = new byte[date.length()];

        for (int i = 0; i < date.length(); i++) {
            dateBin[i] = (byte) date.charAt(i);
        }

        initialize();
    }

    /**
     * Describe <code>initialize</code> method here.
     *
     */
    private void initialize() {
        for (int i = 0; i < dateBin.length; i++) {
            if ((char) dateBin[i] == ' ') {
                dateBin[i] = (byte) '0';
            }
        }

        sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        StringBuffer sb = new StringBuffer();

        for (int i = 15; i < dateBin.length; i++) {
            char cr = (char) dateBin[i];

            if (i == 18) {
                sb.append(':');
            }

            sb.append(cr);
        }

        sdf.setTimeZone(TimeZone.getTimeZone(sb.toString()));
    }

    /**
     * Describe <code>toString</code> method here.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(dateBin.length);

        for (int i = 0; i < dateBin.length; i++) {
            sb.append((char) dateBin[i]);
        }

        return sb.toString();
    }

    /**
     * Describe <code>getDate</code> method here.
     *
     * @return a <code>Date</code> value
     */
    public Date getDate() {
        try {
            return sdf.parse(toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Describe <code>getCalendar</code> method here.
     *
     * @return a <code>Calendar</code> value
     */
    public Calendar getCalendar() {
        try {
            sdf.parse(toString());

            return sdf.getCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
