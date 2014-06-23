/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal;

import java.io.IOException;

import org.geotools.data.wfs.internal.parsers.ExceptionReportParser;

/**
 * A Java Exception that mirrors a WFS {@code ExceptionReport} and is meant to be produced by
 * {@link ExceptionReportParser}.
 * 
 */
public class WFSException extends IOException {
    private static final long serialVersionUID = -2828901359361793862L;

    private StringBuilder msg;

    public WFSException(String msg) {
        this(msg, null);
    }

    public WFSException(String msg, Throwable cause) {
        super(msg);
        super.initCause(cause);
        this.msg = new StringBuilder();
        if (msg != null) {
            this.msg.append(msg);
        }
    }

    public void addExceptionReport(String report) {
        msg.append("\n\t[").append(report).append("]");
    }

    @Override
    public String getMessage() {
        return msg.toString();
    }

    @Override
    public String getLocalizedMessage() {
        return msg.toString();
    }
}
