/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.io.File;
import java.net.URL;

/**
 * Enumerates the known types of files associated with a shapefile.
 * 
 * @author jesse
 */
public enum ShpFileType {

    /**
     * The .shp file. It contains the geometries of the shapefile
     */
    SHP("shp"),
    /**
     * the .dbf file, it contains the attribute information of the shapefile
     */
    DBF("dbf"),
    /**
     * the .shx file, it contains index information of the existing features
     */
    SHX("shx"),
    /**
     * the .prj file, it contains the projection information of the shapefile
     */
    PRJ("prj"),
    /**
     * the .qix file, A quad tree spatial index of the shapefile. It is the same
     * format the mapservers shptree tool generates
     */
    QIX("qix"),
    /**
     * the .fix file, it contains all the Feature IDs for constant time lookup
     * by fid also so that the fids stay consistent across deletes and adds
     */
    FIX("fix"),
    /**
     * the .shp.xml file, it contains the metadata about the shapefile
     */
    SHP_XML("shp.xml"),
    /**
     * the .grx file, an RTree spatial index of the shapefile.  This is not longer supported
     * @deprecated
     */
    GRX("grx");

    public final String extension;
    public final String extensionWithPeriod;

    private ShpFileType(String extension) {
        this.extension = extension.toLowerCase();
        this.extensionWithPeriod = "." + this.extension;
    }

    /**
     * Returns the base of the file or null if the file passed in is not of the
     * correct type (has the correct extension.)
     * <p>
     * For example if the file is c:\shapefiles\file1.dbf. The DBF type will
     * return c:\shapefiles\file1 but all other will return null.
     */
    public String toBase(File file) {
        String path = file.getPath();
        return toBase(path);
    }

    /**
     * Returns the base of the file or null if the file passed in is not of the
     * correct type (has the correct extension.)
     * <p>
     * For example if the file is c:\shapefiles\file1.dbf. The DBF type will
     * return c:\shapefiles\file1 but all other will return null.
     */
    public String toBase(String path) {
        if (!path.toLowerCase().endsWith(extensionWithPeriod)
                || path.equalsIgnoreCase(extensionWithPeriod)) {
            return null;
        }

        int indexOfExtension = path.toLowerCase().lastIndexOf(
                extensionWithPeriod);
        return path.substring(0, indexOfExtension);
    }

    /**
     * Returns the base of the file or null if the file passed in is not of the
     * correct type (has the correct extension.)
     * <p>
     * For example if the file is c:\shapefiles\file1.dbf. The DBF type will
     * return c:\shapefiles\file1 but all other will return null.
     */
    public String toBase(URL url) {
        return toBase( url.toExternalForm() );        
    }
}
