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
package org.geotools.data.postgis;

import java.io.IOException;

import org.geotools.data.DataSourceException;

/**
 * Simple struct used to specify with revision must be extracted, and eventually
 * from which branch (even if at the moment we don't have branches around).
 * 
 * @author aaime
 * @since 2.4
 * 
 */
class RevisionInfo {
    long revision;

    String version;

    /**
     * RevisionInfo for the last revision in "trunk"
     */
    public RevisionInfo() {
        revision = Long.MAX_VALUE;
    }

    public RevisionInfo(long revision) {
        this.revision = revision;
    }

    /**
     * Parses a version specification into a RevisionInfo object
     * 
     * @param version
     * @throws IOException
     */
    public RevisionInfo(String version) throws IOException {
        this.version = version;
        if (version == null || version.trim().equals("") || 
                version.trim().equals("CURRENT") || version.trim().equals("LAST")) {
            revision = Long.MAX_VALUE;
            this.version = "LAST";
        } else if(version.trim().equals("FIRST")) {
            revision = 1;
            this.version = "FIRST";
        } else {
            try {
                revision = Long.parseLong(version);
            } catch (NumberFormatException e) {
                throw new DataSourceException("Unsupported revision format '" + version + "'", e);
            }
        }
    }

    public boolean isLast() {
        return revision == Long.MAX_VALUE;
    }
    
    public boolean isFirst() {
        return revision == 1;
    }

    /**
     * Returns the version in canonical form, that is, revision or "" if it's
     * the last revision
     * 
     * @return
     */
    public String getCanonicalVersion() {
        if (isLast())
            return "LAST";
        else if(isFirst())
            return "FIRST";
        else
            return String.valueOf(revision);
    }

    /**
     * Returns the original version used to create this revision info
     * 
     * @return
     */
    public String getVersion() {
        return version;
    }

    public String toString() {
        return getCanonicalVersion();
    }
}
