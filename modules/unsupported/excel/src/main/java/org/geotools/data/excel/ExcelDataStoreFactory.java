package org.geotools.data.excel;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.util.KVP;
import org.geotools.util.logging.Logging;

public class ExcelDataStoreFactory extends AbstractDataStoreFactory implements DataStoreFactorySpi {
    private static final Logger logger = Logging
            .getLogger("org.geotools.excel.datastore.ExcelDataStoreFactory");

    static HashSet<Param> params = new HashSet<DataAccessFactory.Param>();

    public static final Param TYPE = new Param("type", String.class, "Type", true, "excel");

    public static final Param URLP = new Param("url", java.net.URL.class,
            "A URL pointing to the file containing the data", true,null,new KVP(Param.EXT,"xls",Param.EXT,"xlsx"));

    public static final Param SHEETNAME = new Param("sheet", String.class, "name of the sheet",
            true);

    public static final Param LATCOL = new Param("latcol", String.class,
            "Column name of Latitude or X value", true,"LAT");

    public static final Param LONGCOL = new Param("longcol", String.class,
            "Column name of Longitude or Y value", true,"LON");

    public static final Param PROJECTION = new Param("projection", String.class,
            "EPSG code of projection", true,"EPSG:4326");

    public static final Param HEADERROW = new Param("headerrow", Integer.class,
            "Row index for header row (default 0)", false,"0");

    public String getDisplayName() {
        // TODO Auto-generated method stub
        return "Excel DataStore";
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return "A Datastore backed by an Excel Workbook";
    }

    public boolean canProcess(Map params) {
        if (!super.canProcess(params)) {

            return false; // was not in agreement with getParametersInfo
        }

        try {
            URL url = (URL) URLP.lookUp(params);
            File f = DataUtilities.urlToFile(url);
            boolean accept = url.getFile().toUpperCase().endsWith("XLS")||url.getFile().toUpperCase().endsWith("XLSX");
            if(accept) {
                return true;
            }
        } catch (IOException e) {
            logger.log(Level.FINER, e.getMessage(), e);
        }
        return false;
    }

    public final Param[] getParametersInfo() {
        LinkedHashMap map = new LinkedHashMap();
        setupParameters(map);

        return (Param[]) map.values().toArray(new Param[map.size()]);
    }

    void setupParameters(LinkedHashMap map) {
        map.put(URLP.key, URLP);
        map.put(HEADERROW.key, HEADERROW);
        map.put(LATCOL.key, LATCOL);
        map.put(LONGCOL.key, LONGCOL);
        map.put(SHEETNAME.key, SHEETNAME);
        map.put(PROJECTION.key, PROJECTION);
    }

    public boolean isAvailable() {

        return true;
    }

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        URL url = (URL) URLP.lookUp(params);
        String sheet = (String) SHEETNAME.lookUp(params);
        int headerRow = 0;
        if (params.containsKey(HEADERROW.key)) {
            headerRow = ((Integer) HEADERROW.lookUp(params)).intValue();
        }
        String latCol = ((String) LATCOL.lookUp(params));
        String longCol = ((String) LONGCOL.lookUp(params));
        String projectionString = (String) PROJECTION.lookUp(params);
        ExcelDataStore excel = new ExcelDataStore(url, sheet, headerRow, latCol, longCol,
                projectionString);
        return excel;
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException("Read only datastore");

    }

}
