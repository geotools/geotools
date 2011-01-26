/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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


package org.geotools.gce.imagemosaic.jdbc;

import java.util.concurrent.LinkedBlockingQueue;

import org.geotools.geometry.GeneralEnvelope;


/**
 * Holds the state of the {@link ImageMosaicJDBCReader}
 * making the reader thread safe
 *  
 * @author mcr
 * @since 2.6
 * 
*/
public class ImageMosaicJDBCReaderState {

    private boolean xAxisSwitch = false;
    private final LinkedBlockingQueue<TileQueueElement> tileQueue= 
        new LinkedBlockingQueue<TileQueueElement>();
    
    
    public GeneralEnvelope getRequestedEnvelope() {
        return requestedEnvelope;
    }

    public void setRequestedEnvelope(GeneralEnvelope requestedEnvelope) {
        this.requestedEnvelope = requestedEnvelope;
    }

    public GeneralEnvelope getRequestEnvelopeTransformed() {
        return requestEnvelopeTransformed;
    }

    public void setRequestEnvelopeTransformed(GeneralEnvelope requestEnvelopeTransformed) {
        this.requestEnvelopeTransformed = requestEnvelopeTransformed;
    }

    public LinkedBlockingQueue<TileQueueElement> getTileQueue() {
        return tileQueue;
    }

    private GeneralEnvelope requestedEnvelope = null;
    private GeneralEnvelope requestEnvelopeTransformed = null;


    public boolean isXAxisSwitch() {
        return xAxisSwitch;
    }

    public void setXAxisSwitch(boolean axisSwitch) {
        xAxisSwitch = axisSwitch;
    }

}
