/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Holds teradata tessellation parameters.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class TessellationInfo {

    /** 
     * user data key
     */
    public static String KEY = TessellationInfo.class.getName();
    
    /** bounds of universe */
    Envelope uBounds;
    
    /** index dimensions */
    int nx, ny;
    
    /** levels */
    int levels;
    
    /** scale */
    double scale;
    
    /** shift */
    int shift;
    
    /** table info */
    String schemaName;
    String tableName;
    String columName;
    
    /** spatial index */
    String indexTableName;
    
    public Envelope getUBounds() {
        return uBounds;
    }
    
    public void setUBounds(Envelope uBounds) {
        this.uBounds = uBounds;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }
    
    public void setNy(int ny) {
        this.ny = ny;
    }
    
    public int getNy() {
        return ny;
    }
    
    public int getLevels() {
        return levels;
    }
    
    public void setLevels(int levels) {
        this.levels = levels;
    }
    
    public double getScale() {
        return scale;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public int getShift() {
        return shift;
    }
    
    public void setShift(int shift) {
        this.shift = shift;
    }
    
    public String getSchemaName() {
        return schemaName;
    }
    
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getColumName() {
        return columName;
    }
    
    public void setColumName(String columName) {
        this.columName = columName;
    }
    
    public String getIndexTableName() {
        return indexTableName;
    }
    
    public void setIndexTableName(String indexTableName) {
        this.indexTableName = indexTableName;
    }
    
    
}
