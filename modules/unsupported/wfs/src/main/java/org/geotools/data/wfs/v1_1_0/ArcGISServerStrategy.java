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
package org.geotools.data.wfs.v1_1_0;

import java.util.Iterator;
import java.util.Set;
import org.geotools.data.wfs.protocol.wfs.WFSOperationType;
import org.geotools.data.wfs.protocol.wfs.WFSProtocol;

/**
 * B3partners B.V. http://www.b3partners.nl
 * @author Roy Braam
 * Created on 25-mei-2010, 12:12:31
 */
public class ArcGISServerStrategy extends DefaultWFSStrategy{
    /**
     * ArcGIS server configuration without @DefaultWFSStrategy#DEFAULT_OUTPUT_FORMAT but with some
     * subtype profile
     */
    @Override
    public String getDefaultOutputFormat(WFSProtocol wfs, WFSOperationType op) {
        try {
            return super.getDefaultOutputFormat(wfs, op);
        } catch (IllegalArgumentException e) {
            Set<String> supportedOutputFormats = wfs.getSupportedGetFeatureOutputFormats();
            Iterator<String> it= supportedOutputFormats.iterator();
            while (it.hasNext()){
                String outputFormat=it.next();
                if (outputFormat!=null && outputFormat.toLowerCase().startsWith(DEFAULT_OUTPUT_FORMAT.toLowerCase())){
                    return outputFormat;
                }
            }
            throw new IllegalArgumentException("Server does not support '" + DEFAULT_OUTPUT_FORMAT
                    + "' output format: " + supportedOutputFormats);
        }
    }
}