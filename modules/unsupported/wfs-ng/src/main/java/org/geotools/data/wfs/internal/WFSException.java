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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Java Exception that mirrors a WFS {@code ExceptionReport} and is meant to be produced by {@link
 * ExceptionReportParser}.
 */
public class WFSException extends IOException {
    private static final long serialVersionUID = -2828901359361793862L;

    private StringBuilder msg;

    /** Structured representations of the WFS ExceptionReport */
    private List<ExceptionDetails> exceptionDetails = new ArrayList<>();

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

    /**
     * Use {@link #addExceptionMessage(String)} instead, or {@link #addExceptionDetails(String,
     * String, List)} for exceptions consisting of code and messages
     *
     * @param report
     */
    @Deprecated
    public void addExceptionReport(String report) {
        addExceptionMessage(report);
    }

    public void addExceptionMessage(String message) {
        msg.append("\n\t[").append(message).append("]");
    }

    public void addExceptionDetails(String code, String locator, List<String> texts) {
        addExceptionMessage(code + ": " + String.valueOf(texts));
        exceptionDetails.add(new ExceptionDetails(code, locator, texts));
    }

    @Override
    public String getMessage() {
        return msg.toString();
    }

    @Override
    public String getLocalizedMessage() {
        return msg.toString();
    }

    /** @return the structured contents of the WFS exception, if any */
    public List<ExceptionDetails> getExceptionDetails() {
        return Collections.unmodifiableList(exceptionDetails);
    }
}
