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
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;

import org.geotools.data.DataUtilities;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.data.wfs.internal.WFSResponse;
import org.geotools.data.wfs.internal.WFSResponseParser;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A WFS response parser that parses a GetFeature response that did not return an ExceptionReport
 * and is on GML 3.1 format into a {@link GetFeatureParser} in order to stream the features produced
 * by the server.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.6
 * 
 * 
 * 
 * @source $URL$
 * @see GmlGetFeatureResponseParserFactory
 */
public class FeatureCollectionParser implements WFSResponseParser {

    /**
     * @return a {@link GetFeatureParser} to stream the contents of the GML 3.1 response
     */
    public Object parse(WFSResponse response) throws IOException {

        final GetFeatureType request = (GetFeatureType) response.getOriginatingRequest();
        final QueryType queryType = (QueryType) request.getQuery().get(0);
        final QName remoteFeatureName = response.getRemoteTypeName();

        SimpleFeatureType schema = response.getQueryType();
        @SuppressWarnings("unchecked")
        List<String> propertyNames = queryType.getPropertyName();
        if (propertyNames.size() > 0) {
            // the expected schema may contain less properties than the full schema. Let's say it to
            // the parser so it does not parse unnecessary attributes in case the WFS returns more
            // than requested
            String[] properties = propertyNames.toArray(new String[propertyNames.size()]);
            try {
                schema = DataUtilities.createSubType(schema, properties);
            } catch (SchemaException e) {
                throw (RuntimeException) new RuntimeException().initCause(e);
            }
        }

        InputStream in = response.getInputStream();

        GetFeatureParser featureReader = new XmlSimpleFeatureParser(in, schema, remoteFeatureName);
        return featureReader;
    }
}
