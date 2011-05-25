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
package org.geotools.data.postgis.fidmapper;

import java.io.IOException;

import org.geotools.data.jdbc.fidmapper.FIDMapper;

/**
 * Fid mappers used in versioned data store. <br>
 * They do present the usual face to the internal, wrapped data store, but know how to remove
 * revisions from the fid and how to handle cases where the key does not need to be generated
 * because the feature is not really new, just a new version of the old one.
 * 
 * @author aaime
 * @since 2.4
 *
 *
 * @source $URL$
 */
public interface VersionedFIDMapper extends FIDMapper {
    /**
     * Given the FID exposed by the internal data store, build a representation that does not have
     * the
     * 
     * @param versionedFID
     * @return
     */
    public String getUnversionedFid(String versionedFID);

    /**
     * Given the external FID, returns the primary key column values (besides revision, of course)
     * 
     * @param FID
     * @return
     * @throws IOException
     */
    public Object[] getUnversionedPKAttributes(String FID) throws IOException;
    
    public String createVersionedFid(String extenalFID, long revision);
}
