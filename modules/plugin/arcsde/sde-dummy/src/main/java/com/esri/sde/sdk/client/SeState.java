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

public class SeState {

    public static int SE_STATE_DIFF_NOCHECK = 0;
    public static int SE_NULL_STATE_ID = 0;

    public SeState(SeConnection conn, SeObjectId stateId) throws SeException {}

    public SeState(SeConnection conn) throws SeException {}

    public boolean isOpen() {
        return false;
    }

    public void close() throws SeException {}

    public SeObjectId getId() {
        return null;
    }

    public void create(SeObjectId parentId) throws SeException {}

    public void trimTree(SeObjectId from, SeObjectId to) throws SeException {}

    public void delete() throws SeException {}

    public SeObjectId getParentId() {
        return null;
    }

    public void open() throws SeException {}

    public void freeLock() throws SeException {}

    public void lock() throws SeException {}

    public void merge(SeObjectId id, SeObjectId id2) throws SeException {}

    public String getOwner() {
        return null;
    }
}
