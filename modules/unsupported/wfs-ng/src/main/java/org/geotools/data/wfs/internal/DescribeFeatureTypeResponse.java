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
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.xsd.Configuration;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

public class DescribeFeatureTypeResponse extends WFSResponse {

    private FeatureType parsed;

    public DescribeFeatureTypeResponse(final DescribeFeatureTypeRequest request, final HTTPResponse httpResponse)
            throws ServiceException, IOException {

        super(request, httpResponse);

        final WFSStrategy strategy = request.getStrategy();
        final Configuration wfsConfiguration = strategy.getWfsConfiguration();
        final QName remoteTypeName = request.getTypeName();
        final FeatureTypeInfo featureTypeInfo = strategy.getFeatureTypeInfo(remoteTypeName);
        final CoordinateReferenceSystem defaultCrs = featureTypeInfo.getCRS();
        final EntityResolver resolver = request.getStrategy().getConfig().getEntityResolver();

        try (InputStream responseStream = httpResponse.getResponseStream()) {
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
                this.parsed = EmfAppSchemaParser.parse(
                        wfsConfiguration,
                        remoteTypeName,
                        schemaLocation,
                        defaultCrs,
                        strategy.getFieldTypeMappings(),
                        getTempFileEntityResolver(resolver, tmpSchemaFile));
            } finally {
                tmpSchemaFile.delete();
            }
        } finally {
            httpResponse.dispose();
        }
    }

    public FeatureType getFeatureType() {
        return parsed;
    }

    private EntityResolver getTempFileEntityResolver(EntityResolver resolver, File tempSchema) {
        if (resolver == null) return null;
        if (resolver instanceof EntityResolver2) return new TempEntityResolver2((EntityResolver2) resolver, tempSchema);
        return new TempEntityResolver(resolver, tempSchema);
    }

    private static class TempEntityResolver implements EntityResolver {
        EntityResolver delegate;
        File tempSchema;

        public TempEntityResolver(EntityResolver delegate, File tempSchema) {
            this.delegate = delegate;
            this.tempSchema = tempSchema;
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            if (isTempSchema(systemId)) return null;

            return delegate.resolveEntity(publicId, systemId);
        }

        protected boolean isTempSchema(String systemId) {
            // let it go
            if (systemId.equalsIgnoreCase("file:" + tempSchema.getAbsolutePath())) return true;
            return false;
        }
    }

    private static class TempEntityResolver2 extends TempEntityResolver implements EntityResolver2 {
        EntityResolver2 delegate;

        public TempEntityResolver2(EntityResolver2 delegate, File tempSchema) {
            super(delegate, tempSchema);
            this.delegate = delegate;
        }

        @Override
        public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
            // what to do here?
            return delegate.getExternalSubset(name, baseURI);
        }

        @Override
        public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
                throws SAXException, IOException {
            if (isTempSchema(systemId)) return null;
            return delegate.resolveEntity(name, publicId, baseURI, systemId);
        }
    }
}
