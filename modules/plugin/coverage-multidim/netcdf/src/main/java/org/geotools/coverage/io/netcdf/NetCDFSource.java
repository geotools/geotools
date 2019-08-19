/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.io.CoverageReadRequest;
import org.geotools.coverage.io.CoverageResponse;
import org.geotools.coverage.io.impl.DefaultCoverageSource;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.util.ProgressListener;

/**
 * Implementation of a coverage source for netcdf data
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
@SuppressWarnings("rawtypes")
public class NetCDFSource extends DefaultCoverageSource {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(NetCDFSource.class);

    NetCDFImageReader reader;

    Set<ParameterDescriptor<List>> dynamicParameters = null;

    public NetCDFSource(final NetCDFImageReader reader, final Name name) {
        super(name, reader.getCoverageDescriptor(name));
        this.reader = reader;
    }

    @Override
    public CoverageResponse read(CoverageReadRequest request, ProgressListener listener)
            throws IOException {
        ensureNotDisposed();
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Reading NetCDFSource with request: " + request);
        }
        NetCDFRequest coverageRequest = new NetCDFRequest(this, request);
        NetCDFResponse netCDFresponse = new NetCDFResponse(coverageRequest);
        return netCDFresponse.createResponse();
    }

    public boolean isParameterSupported(ReferenceIdentifier name) throws IOException {
        getDynamicParameters();
        if (dynamicParameters != null && !dynamicParameters.isEmpty()) {
            for (ParameterDescriptor<List> desc : dynamicParameters) {
                if (desc.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<ParameterDescriptor<List>> getDynamicParameters() throws IOException {
        if (dynamicParameters == null) {
            dynamicParameters = new HashSet<ParameterDescriptor<List>>();
            List<AdditionalDomain> domains = getAdditionalDomains();
            if (domains != null && !domains.isEmpty()) {
                for (AdditionalDomain domain : domains) {
                    dynamicParameters.add(
                            DefaultParameterDescriptor.create(
                                    domain.getName().toUpperCase(),
                                    "Additional " + domain.getName() + " domain",
                                    List.class,
                                    null,
                                    false));
                }
            }
        }
        return dynamicParameters;
    }
}
