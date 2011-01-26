/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sfs.mock;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * This class will handle requests like:
 * http://localhost:8082/simplefeatureservice/capabilities
 * It generates a list of available layers
 * @author 
 */
public class CapabilitiesResource extends Resource {

    public CapabilitiesResource(Context context, Request request, Response response) {
        super(context, request, response);

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }

    /**
     * This method is used to server a list of available layers
     * http://localhost:8084/simplefeatureservice-mockup-service-1.0-SNAPSHOT/capabilities
     * @param variant
     * @return
     * @throws ResourceException
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {

        String _jsonText = "[{"
                + "   \"name\": \"layerAsia\","
                + "   \"bbox\": [-10,-40,30,80],"
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:4326\","
                + "   \"axisorder\": \"yx\"},"
                + "{"
                + "   \"name\": \"layerAmerica\","
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:32632\" ,"
                + "   \"axisorder\": \"xy\" },"
                + "{"
                + "   \"name\": \"layerEurope\","
                + "   \"bbox\": [15000000,49000000,18000000,52000000],"
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:32632\" "
                + " }]";

        Representation representation = new StringRepresentation(_jsonText, MediaType.APPLICATION_JSON);
        return representation;
    }
}
