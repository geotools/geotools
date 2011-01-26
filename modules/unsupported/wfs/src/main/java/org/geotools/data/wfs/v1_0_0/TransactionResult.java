/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.util.List;

import org.xml.sax.SAXException;


/**
 * DOCUMENT ME!
 *
 * @author dzwiers
 * @source $URL$
 */
public class TransactionResult {
    /**
     * no status
     */
    public static final int NO_STATUS = 0;
    /**
     * success
     */
    public static final int SUCCESS = 1;
    /**
     * failed
     */
    public static final int FAILED = 2;
    /**
     * partial
     */
    public static final int PARTIAL = 4;
    /**
     * A list of the fids returned in the TransactionResult in the order they were received.  
     * The first element is the FID of the first InsertResults response.
     */
    private List insertResult;
    private int status;

    private SAXException error;

    private TransactionResult() {
        // should not be used
    }

    /**
     * 
     * @param status
     * @param insertResult
     * @param error
     */
    public TransactionResult(int status, List insertResult,
        SAXException error) {
        this.status = status;
        this.insertResult = insertResult;
        this.error = error;
    }

    /**
     * 
     * @param status
     * @param insertResult
     * @param locator nullable
     * @param message
     */
    public TransactionResult(int status, List insertResult,
        String locator, String message) {
        this.status = status;
        this.insertResult = insertResult;
        error = new SAXException(message + ":"
                + ((locator == null) ? "" : locator));
    }

    /**
     * 
     * @param s
     * @return one of the constant status'
     */
    public static int parseStatus(String s) {
        if ("SUCCESS".equalsIgnoreCase(s)) {
            return SUCCESS;
        }

        if ("FAILED".equalsIgnoreCase(s)) {
            return FAILED;
        }

        if ("PARTIAL".equalsIgnoreCase(s)) {
            return PARTIAL;
        }

        return NO_STATUS;
    }

    /**
     * 
     * @param i
     * @return String representation of the constant value in i
     */
    public static String printStatus(int i) {
        switch (i) {
        case SUCCESS:
            return "SUCCESS";

        case FAILED:
            return "FAILED";

        case PARTIAL:
            return "PARTIAL";

        default:
            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the error.
     */
    public SAXException getError() {
        return error;
    }

    /**
     * A list of the fids returned in the TransactionResult in the order they were received.  
     * The first element is the FID of the first InsertResults response.
     *
     * @return list of the fids returned in the TransactionResult in the order they were received.  
     */
    public List getInsertResult() {
        return insertResult;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the status.
     */
    public int getStatus() {
        return status;
    }
}
