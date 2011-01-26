/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ml;

import java.util.Calendar;


public class Envelope {
    String from;
    String to;
    Calendar date;
    String subject;
    Header[] headers;

    public Envelope(String from, String to, Calendar date, String subject, Header[] headers) {
        super();

        this.date = date;
        this.from = from;
        this.headers = headers;
        this.subject = subject;
        this.to = to;
    }

    public Calendar getDate() {
        return date;
    }

    public String getFrom() {
        return from;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public String getSubject() {
        return subject;
    }

    public String getTo() {
        return to;
    }
}
