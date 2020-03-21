/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.geotools.ows.ServiceException;
import org.geotools.xsd.Configuration;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class DescribeFeatureTypeResponse extends WFSResponse {

    private FeatureType parsed;

    public DescribeFeatureTypeResponse(
            final DescribeFeatureTypeRequest request, final HTTPResponse httpResponse)
            throws ServiceException, IOException {

        super(request, httpResponse);

        final WFSStrategy strategy = request.getStrategy();
        final Configuration wfsConfiguration = strategy.getWfsConfiguration();
        final QName remoteTypeName = request.getTypeName();
        final FeatureTypeInfo featureTypeInfo = strategy.getFeatureTypeInfo(remoteTypeName);
        final CoordinateReferenceSystem defaultCrs = featureTypeInfo.getCRS();

        InputStream responseStream = httpResponse.getResponseStream();
        try {
            String prefix = remoteTypeName.getLocalPart();
            if (prefix.length() < 3) {
                /*
                 * CreateTempFile will throw an exception if the prefix is less that 3 chars long
                 */
                prefix += "zzz";
            }
            File tmpSchemaFile = File.createTempFile(prefix, ".xsd");
            try (OutputStream output = new FileOutputStream(tmpSchemaFile)) {
                IOUtils.copy(responseStream, output);
                output.flush();
            }
            try {
                URL schemaLocation = tmpSchemaFile.toURI().toURL();
                this.parsed =
                        EmfAppSchemaParser.parse(
                                wfsConfiguration,
                                remoteTypeName,
                                schemaLocation,
                                defaultCrs,
                                strategy.getFieldTypeMappings());
            } finally {
                tmpSchemaFile.delete();
            }
        } finally {
            responseStream.close();
            httpResponse.dispose();
        }
    }

    public FeatureType getFeatureType() {
        return parsed;
    }
}
