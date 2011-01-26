/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.gml;

import java.util.Map;

import org.geotools.xml.FlowHandler;
import org.geotools.xml.XMLHandlerHints;

/**
 * Allows the XMLSAXHandler to abort parsing of GML.
 * 
 * @author Richard Gould
 *
 * @source $URL$
 */
public class FCFlowHandler implements FlowHandler {
    public boolean shouldStop(Map hints) {
        if ((hints != null) && (hints.get(XMLHandlerHints.STREAM_HINT) != null)) {
            FCBuffer buffer = (FCBuffer) hints.get(XMLHandlerHints.STREAM_HINT);

            if (buffer.getInternalState() == FCBuffer.STOP) {
                return true;
            }

            buffer.resetTimer();
        }

        return false;
    }
}
