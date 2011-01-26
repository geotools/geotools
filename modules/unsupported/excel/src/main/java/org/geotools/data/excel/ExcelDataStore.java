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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.GeometryFactory;

public class ExcelDataStore extends ContentDataStore {
    String name = "";

    public String getName() {
        return name;
    }

    /** the workbook object */
    org.apache.poi.ss.usermodel.Workbook workbook;
    
    /** The sheet that holds the data */
    org.apache.poi.ss.usermodel.Sheet sheet;

    private int headerRowIndex = 0;

    private int latColumnIndex = -1;

    private int lonColumnIndex = -1;

    ArrayList<Name> names = new ArrayList<Name>();

    private CoordinateReferenceSystem projection;

    private static final Logger logger = Logging.getLogger("org.geotools.excel");

    public ExcelDataStore(URL url, String sheet2, int headerRow, String latCol,
            String longCol, String projectionString) throws IOException {
        super();
        
        name = url.getFile();
        InputStream is = url.openStream();
        try {
            workbook = WorkbookFactory.create(is);
            
        } catch (InvalidFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        sheet = workbook.getSheet(sheet2);
        headerRowIndex = headerRow;
        latColumnIndex = lookupColumn(latCol);
        lonColumnIndex = lookupColumn(longCol);
        
        try {
            setProjection(CRS.decode(projectionString));
        } catch (NoSuchAuthorityCodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FactoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Look up a column name and return it's index
     * @param columnName 
     * @return the index of the column
     */
    private int lookupColumn(String columnName) {
        Row header = sheet.getRow(getHeaderRowIndex());

        for (int i = header.getFirstCellNum(); i < header.getLastCellNum(); i++) {
            if (header.getCell(i).getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    

    public int getLatColumnIndex() {
        return latColumnIndex;
    }

    public void setLatColumnIndex(int latColumnIndex) {
        this.latColumnIndex = latColumnIndex;
    }

    public int getLonColumnIndex() {
        return lonColumnIndex;
    }

    public void setLonColumnIndex(int lonColumnIndex) {
        this.lonColumnIndex = lonColumnIndex;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        return new ExcelFeatureSource(entry, Query.ALL);

    }

    protected List<Name> createTypeNames() throws IOException {
        if (names.isEmpty()) {
            int n = workbook.getNumberOfSheets();

            for (int i = 0; i < n; i++) {
                String name = workbook.getSheetName(i);
                Name typeName = new NameImpl(name);
                names.add(typeName);
            }
        }
        return names;
    }

    public int getHeaderRowIndex() {
        // TODO Auto-generated method stub
        return headerRowIndex;
    }

    public void setHeaderRowIndex(int headerRowIndex) {
        this.headerRowIndex = headerRowIndex;
    }

    public void setProjection(CoordinateReferenceSystem projection) {
        this.projection = projection;
    }

    public CoordinateReferenceSystem getProjection() {
        return projection;
    }

    @Override
    /**
     * Provide a geometery factory 
     * if none has been set then a JTS GeometeryFactory is returned.
     */
    public GeometryFactory getGeometryFactory() {

        GeometryFactory fac = super.getGeometryFactory();
        if (fac == null) {
            fac = JTSFactoryFinder.getGeometryFactory(null);
            setGeometryFactory(fac);
        }
        return fac;
    }

}
