package org.geotools.data.wfs.internal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.geotools.ows.ServiceException;
import org.geotools.xml.Configuration;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class DescribeFeatureTypeResponse extends WFSResponse {

    private FeatureType parsed;

    public DescribeFeatureTypeResponse(final DescribeFeatureTypeRequest request,
            final HTTPResponse httpResponse) throws ServiceException, IOException {

        super(request, httpResponse);

        final WFSStrategy strategy = request.getStrategy();
        final Configuration wfsConfiguration = strategy.getWfsConfiguration();
        final QName remoteTypeName = request.getTypeName();
        final FeatureTypeInfo featureTypeInfo = strategy.getFeatureTypeInfo(remoteTypeName);
        final CoordinateReferenceSystem defaultCrs = featureTypeInfo.getCRS();

        InputStream responseStream = httpResponse.getResponseStream();
        try {
            File tmp = File.createTempFile(remoteTypeName.getLocalPart(), ".xsd");
            OutputStream output = new BufferedOutputStream(new FileOutputStream(tmp));
            output = new TeeOutputStream(output, System.out);
            try {
                IOUtils.copy(responseStream, output);
            } finally {
                output.flush();
                IOUtils.closeQuietly(output);
            }
            try {
                URL schemaLocation = tmp.toURI().toURL();
                this.parsed = EmfAppSchemaParser.parse(wfsConfiguration, remoteTypeName,
                        schemaLocation, defaultCrs);
            } finally {
                tmp.delete();
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
