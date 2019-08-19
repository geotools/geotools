/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package com.esri.sde.sdk.client;

public class SeRasterColumn {

    public SeRasterColumn(SeConnection conn, SeObjectId id) throws SeException {}

    public SeRasterColumn(SeConnection conn) throws SeException {}

    public SeCoordinateReference getCoordRef() {
        return null;
    }

    public String getName() {
        return null;
    }

    public String getQualifiedTableName() {
        return null;
    }

    public void setTableName(String name) {}

    public void setDescription(String desc) {}

    public void setRasterColumnName(String rColName) {}

    public void setCoordRef(SeCoordinateReference coordref) {}

    public void setConfigurationKeyword(String s) {}

    public void create() {}

    public String getTableName() {
        return null;
    }

    public SeObjectId getID() throws SeException {
        // TODO Auto-generated method stub
        return null;
    }
}
