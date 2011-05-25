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
package org.geotools.data.wfs.v1_1_0.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;

import org.geotools.data.DataUtilities;
import org.geotools.data.wfs.protocol.wfs.GetFeatureParser;
import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.protocol.wfs.WFSResponseParser;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A WFS response parser that parses a GetFeature response that did not return an ExceptionReport
 * and is on GML 3.1 format into a {@link GetFeatureParser} in order to stream the features produced
 * by the server.
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: FeatureCollectionParser.java 31823 2008-11-11 16:11:49Z groldan $
 * @since 2.6
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/wfs-ng/src/main/java/org/geotools/data/wfs/v1_1_0/parsers/FeatureCollectionParser.java $
 * @see Gml31GetFeatureResponseParserFactory
 */
public class FeatureCollectionParser implements WFSResponseParser {

    /**
     * @return a {@link GetFeatureParser} to stream the contents of the GML 3.1 response
     */
    public Object parse(WFS_1_1_0_DataStore wfs, WFSResponse response) throws IOException {

        GetFeatureType request = (GetFeatureType) response.getOriginatingRequest();
        QueryType queryType = (QueryType) request.getQuery().get(0);
        String prefixedTypeName = (String) queryType.getTypeName().get(0);
        SimpleFeatureType schema = wfs.getSchema(prefixedTypeName);
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
        QName featureName = wfs.getFeatureTypeName(prefixedTypeName);
        InputStream in = response.getInputStream();

        GetFeatureParser featureReader = new XmlSimpleFeatureParser(in, schema, featureName);
        return featureReader;
    }
}
