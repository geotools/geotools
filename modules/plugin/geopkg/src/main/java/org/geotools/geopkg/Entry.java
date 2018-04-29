/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.util.Date;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Entry in a geopackage.
 *
 * <p>This class corresponds to the "geopackage_contents" table.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class Entry {

    public static enum DataType {
        Feature("features"),
        Tile("tiles");

        String value;

        DataType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    protected String tableName;
    protected DataType dataType;
    protected String identifier;
    protected String description;
    protected Date lastChange;
    protected ReferencedEnvelope bounds;
    protected Integer srid;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public ReferencedEnvelope getBounds() {
        return bounds;
    }

    public void setBounds(ReferencedEnvelope bounds) {
        this.bounds = bounds;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    void init(Entry e) {
        setDescription(e.getDescription());
        setIdentifier(e.getIdentifier());
        setDataType(e.getDataType());
        setBounds(e.getBounds());
        setSrid(e.getSrid());
        setTableName(e.getTableName());
    }

    Entry copy() {
        Entry e = new Entry();
        e.init(this);
        return e;
    }
}
