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
 * This class will handle requests like
 * http://localhost:8084/simplefeatureservice/describe/layerAsia
 * Returns a description of the feature type
 * @author narad
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/sfs/src/test/java/org/geotools/data/sfs/mock/DescribeLayerResource.java $
 */
public class DescribeLayerResource extends Resource {

    public DescribeLayerResource(Context context, Request request, Response response) {
        super(context, request, response);

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }

    /**
     * This method is used to server a list of available layers
     * http://localhost:8084/simplefeatureservice-mockup-service-1.0-SNAPSHOT/describe/layerAsia
     * @param variant
     * @return
     * @throws ResourceException
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        String _strJson = "[ {\"CFCC\":\"string\",\"NAME\":\"string\",\"the_geom\":\"MultiLineString\" } ]";
        Representation representation = new StringRepresentation(_strJson, MediaType.APPLICATION_JSON);
        return representation;
    }
}
