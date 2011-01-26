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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author ijt1
 * 
 */
public class ExcelFeatureSource extends ContentFeatureSource implements SimpleFeatureSource {

    private Sheet sheet;

    private ExcelDataStore dataStore;

    private int latCol;

    private int lonCol;

    private ArrayList<SimpleFeature> features, filteredFeatures;

    private Query lastQuery = null;

    private FormulaEvaluator evaluator;

    public ExcelFeatureSource(ContentEntry entry) {
        super(entry, Query.ALL);

    }

    /**
     * create a FeatureSource with the specified Query
     * 
     * @param entry
     * @param query
     *            - a query containing a filter that will be applied to the data
     */
    public ExcelFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
        Date beginingOfExcelTime = HSSFDateUtil.getJavaDate(0);

        dataStore = (ExcelDataStore) entry.getDataStore();

        sheet = dataStore.getSheet();
        latCol = dataStore.getLatColumnIndex();
        lonCol = dataStore.getLonColumnIndex();
        int rows = sheet.getPhysicalNumberOfRows();
        int start = dataStore.getHeaderRowIndex() + 1;
        latCol = dataStore.getLatColumnIndex();
        lonCol = dataStore.getLonColumnIndex();
        features = new ArrayList<SimpleFeature>();
        filteredFeatures = new ArrayList<SimpleFeature>();
        evaluator = dataStore.workbook.getCreationHelper().createFormulaEvaluator();
        if(schema==null) {
            schema=getSchema();
        }
        GeometryFactory geometryFactory = dataStore.getGeometryFactory();
        
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
        Row header = sheet.getRow(dataStore.getHeaderRowIndex());
        for (int i = start; i < rows; i++) {
            Row data = sheet.getRow(i);
            double x = 0.0;
            double y = 0.0;
            for (int col = data.getFirstCellNum(); col < data.getLastCellNum(); col++) {
                final Cell cell = data.getCell(col);
                CellValue value = evaluator.evaluate(cell);
                if (col == latCol) {

                    if (value.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        y = value.getNumberValue();
                    }
                } else if (col == lonCol) {
                    if (value.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        x = value.getNumberValue();
                    }
                } else {
                    // cast and handle
                    final String name = header.getCell(col).getStringCellValue().trim();
                    switch (value.getCellType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        AttributeType type = schema.getType(name);
                        Class<?> clazz = type.getBinding();
                        if(clazz==Double.class) {
                            builder.set(name, value.getNumberValue());
                        }else if (clazz == java.sql.Date.class) {
                            final java.util.Date javaDate = HSSFDateUtil.getJavaDate(value
                                    .getNumberValue());
                            final Calendar cal = Calendar.getInstance();
                            cal.clear();
                            cal.setTime(javaDate);
                            java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
                            builder.set(name, date);
                        }else if (clazz == java.util.Date.class) {
                            final java.util.Date javaDate = HSSFDateUtil.getJavaDate(value
                                    .getNumberValue());
                            builder.set(name, javaDate);
                        }else if (clazz == Time.class) {
                            final java.util.Date javaDate = HSSFDateUtil.getJavaDate(value
                                    .getNumberValue());
                            final Calendar cal = Calendar.getInstance();
                            cal.clear();
                            cal.setTime(javaDate);
                            cal.set(0, 0, 0);
                            Time time = new Time(cal.getTimeInMillis());
                            builder.set(name, time);
                        }
                                                break;
                    case Cell.CELL_TYPE_STRING:
                        builder.set(name, value.getStringValue().trim());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        builder.set(name, value.getBooleanValue());
                        break;

                    default:
                        System.out.println("We don't handle " + cell.getCellType() + " type cells "
                                + cell.getStringCellValue());
                    }
                }
            }
            Point p = geometryFactory.createPoint(new Coordinate(x, y));
            builder.set("the_geom", p);

            SimpleFeature feature = builder.buildFeature(null);
            features.add(feature);

        }
        filterFeatures(query);
    }

    @Override
    /**
     * Calculates the bounds of a specified query.
     * 
     * @param query - the query to be applied.
     */
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {

        ReferencedEnvelope env = new ReferencedEnvelope(getSchema().getCoordinateReferenceSystem());
        if (lastQuery != query)
            filterFeatures(query);
        for (SimpleFeature feature : filteredFeatures) {

            Point p = (Point) feature.getDefaultGeometry();
            env.expandToInclude(p.getCoordinate());
        }

        return env;
    }

    /**
     * regenerate the filteredFeatures list if the query has changed since the last time we did
     * this.
     * 
     * @param query
     */
    private void filterFeatures(Query query) {
        filteredFeatures = new ArrayList<SimpleFeature>();

        for (SimpleFeature feature : features) {

            if (query.getFilter().evaluate(feature)) {
                filteredFeatures.add(feature);
            }
        }

        lastQuery = query;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (lastQuery != query)
            filterFeatures(query);
        return filteredFeatures.size();
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        if (lastQuery != query)
            filterFeatures(query);
        return new ExcelFeatureReader(filteredFeatures, this);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(entry.getName());
        tb.setCRS(dataStore.getProjection());
        Row header = sheet.getRow(dataStore.getHeaderRowIndex());
        Row data = sheet.getRow(dataStore.getHeaderRowIndex() + 1);
        Row nextData = sheet.getRow(dataStore.getHeaderRowIndex() + 2);
        boolean latColGood = false;
        boolean lonColGood = false;
        for (int i = header.getFirstCellNum(); i < header.getLastCellNum(); i++) {
            // go through and guess data type from cell types!
            Cell cell = data.getCell(i);
            String name = header.getCell(i).getStringCellValue().trim();
            CellValue value = evaluator.evaluate(cell);
            int type = value.getCellType();

            Class<?> clazz = null;
            if (latCol == i) {
                // check it's a number
                if (type == Cell.CELL_TYPE_NUMERIC) {
                    latColGood = true;
                }
            } else if (lonCol == i) {
                // check it's a number
                if (type == Cell.CELL_TYPE_NUMERIC) {
                    lonColGood = true;
                }
            } else {
                switch (type) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        if (value.getNumberValue() < 1.0) {
                            clazz = Time.class;
                        } else if (Math.floor(cell.getNumericCellValue()) == Math.ceil(cell
                                .getNumericCellValue())) {
                            // midnight or just a date
                            // check the next row
                            Cell cell2 = nextData.getCell(i);
                            if (Math.floor(cell2.getNumericCellValue()) == Math.ceil(cell2
                                    .getNumericCellValue())) {
                                //probably a simple date
                                clazz = java.sql.Date.class;
                            } else {
                                // actual date/time element
                                clazz = java.util.Date.class;
                            }
                        } else {
                            // actual date/time element
                            clazz = java.util.Date.class;
                        }
                    } else {
                        clazz = Double.class;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    clazz = String.class;
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    clazz = Boolean.class;
                    break;
                }
                System.out.println(name + ":" + clazz);
                tb.add(name, clazz);
            }

        }
        if (latColGood && lonColGood) {
            tb.add("the_geom", Point.class);
        } else {
            throw new IOException("failed to find a Lat and Lon column");
        }
        // build the type (it is immutable and cannot be modified)
        final SimpleFeatureType SCHEMA = tb.buildFeatureType();
        return SCHEMA;
    }

}
