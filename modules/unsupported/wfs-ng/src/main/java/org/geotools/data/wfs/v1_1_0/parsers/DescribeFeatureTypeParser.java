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

import java.io.InputStream;

import org.geotools.data.wfs.protocol.wfs.WFSResponse;
import org.geotools.data.wfs.protocol.wfs.WFSResponseParser;
import org.geotools.data.wfs.protocol.wfs.WFSResponseParserFactory;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.geotools.gml3.ApplicationSchemaConfiguration;
import org.geotools.xml.Parser;

/**
 * This class is meant to parse a WFS DescribeFeatureType response but is not yet engaged.
 * <p>
 * NOTE there are currently technical limitations to engage the DescribeFeatureType request/response
 * to the {@link WFSResponseParserFactory}/{@link WFSResponseParser} architecture. This technical
 * limitations have to be with the fact that the {@link Parser} and
 * {@link ApplicationSchemaConfiguration configuration} used to parse a response need a direct
 * handle to the DescribeFeatureType request URL and have no way to directly parse from the plain
 * response {@link InputStream}.
 * So, for the time being, the request/response handling for GetFeatureType is being made in an ad-hoc
 * manner inside {@link WFS_1_1_0_DataStore#getSchema(String)}
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id: DescribeFeatureTypeParser.java 31823 2008-11-11 16:11:49Z groldan $
 * @since 2.6
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/main/java/org/geotools/data/wfs/v1_1_0/parsers/DescribeFeatureTypeParser.java $
 */
public class DescribeFeatureTypeParser implements WFSResponseParser {

    public Object parse(WFS_1_1_0_DataStore wfs, WFSResponse response) {
        throw new UnsupportedOperationException(
                "DescribeFeatureType responses not yet engaged in the framework, see class' javadocs");
    }

}
