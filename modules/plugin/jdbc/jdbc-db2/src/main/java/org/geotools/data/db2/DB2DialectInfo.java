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

package org.geotools.data.db2;

/** @source $URL$ */
public class DB2DialectInfo {
    private int MajorVersion, MinorVersion;
    private String ProductName, ProductVersion;
    private boolean hasOGCWkbZTyps = false;
    private boolean supportingPrecalculatedExtents = false;

    public boolean isSupportingPrecalculatedExtents() {
        return supportingPrecalculatedExtents;
    }

    public void setSupportingPrecalculatedExtents(boolean supportingPrecalculatedExtents) {
        this.supportingPrecalculatedExtents = supportingPrecalculatedExtents;
    }

    public int getMajorVersion() {
        return MajorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        MajorVersion = majorVersion;
    }

    public int getMinorVersion() {
        return MinorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        MinorVersion = minorVersion;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductVersion() {
        return ProductVersion;
    }

    public void setProductVersion(String productVersion) {
        ProductVersion = productVersion;
    }

    public boolean isHasOGCWkbZTyps() {
        return hasOGCWkbZTyps;
    }

    public void setHasOGCWkbZTyps(boolean hasOGCWkbZTyps) {
        this.hasOGCWkbZTyps = hasOGCWkbZTyps;
    }
}
