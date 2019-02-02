/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf;

import static org.geotools.data.vpf.ifc.FCode.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.geotools.data.store.ContentState;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;

/**
 * @author <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @author Chris Holmes, Fulbright.
 * @source $URL$
 */
public class VPFFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    // private boolean hasNext = true;
    // private boolean nextCalled = true;
    private boolean resetCalled = false;
    private SimpleFeature currentFeature = null;
    private final VPFFeatureType featureType;

    /** State used when reading file */
    protected ContentState state;

    /** Creates a new instance of VPFFeatureReader */
    public VPFFeatureReader(VPFFeatureType type) {
        this.featureType = type;
    }

    public VPFFeatureReader(ContentState contentState, VPFFeatureType featureType)
            throws IOException {
        this.state = contentState;
        this.featureType = featureType;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#close()
     */
    public synchronized void close() throws IOException {
        reset();
    }

    /**
     * Put together a map of VPF files and their corresponding TableRows
     *
     * @param file
     * @param row
     */
    /*
    private Map<VPFFile, List<Object>> generateFileRowMap(VPFFile file, SimpleFeature row)
            throws IOException {
        String tileFileName = null;
        Map<VPFFile, List<Object>> rows = new HashMap<>();
        rows.put(file, Arrays.asList(null, row));
        Iterator joinIter = featureType.getFeatureClass().getJoinList().iterator();
        while (joinIter.hasNext()) {
            ColumnPair columnPair = (ColumnPair) joinIter.next();
            VPFFile primaryFile = getVPFFile(columnPair.column1);
            VPFFile joinFile = null;
            if (columnPair.column2 == null) {
                continue;
            }
            joinFile = getVPFFile(columnPair.column2);
            if (joinFile == null) {
                continue;
            }

            if (!rows.containsKey(joinFile) && rows.containsKey(primaryFile)) {
                List<Object> joinData = (List<Object>) rows.get(primaryFile);
                SimpleFeature joinRow = (SimpleFeature) joinData.get(1);

                try {
                    int joinID =
                            Integer.parseInt(
                                    joinRow.getAttribute(columnPair.column1.getName()).toString());
                    rows.put(
                            joinFile,
                            Arrays.asList(
                                    columnPair,
                                    getVPFFile(columnPair.column2)
                                            .getRowFromId(columnPair.column2.getName(), joinID)));
                } catch (NullPointerException exc) {
                    // Non-matching joins - just put in a NULL
                    rows.put(joinFile, null);
                } catch (IllegalAttributeException exc) {
                    // I really don't expect to see this one
                    exc.printStackTrace();
                    rows.put(joinFile, null);
                }
            }
        }
        return rows;
    }
    */

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#hasNext()
     */
    public synchronized boolean hasNext() throws IOException {
        VPFFeatureClass featureClass = featureType.getFeatureClass();
        if (!resetCalled) {
            this.reset();
        }
        return featureClass.hasNext();
    }

    /*
    public boolean hasNext0() throws IOException {
        if (nextCalled) {
            while (readNext()) ;
            nextCalled = false;
        }
        return hasNext;
    }
    */

    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#next()
     */
    public synchronized SimpleFeature next()
            throws IOException, IllegalAttributeException, NoSuchElementException {
        readNext();
        return currentFeature;
    }

    /*
    public SimpleFeature next0()
            throws IOException, IllegalAttributeException, NoSuchElementException {
        nextCalled = true;
        return currentFeature;
    }
    */

    /**
     * Read a row and determine if it matches the feature type Three possibilities here: row is null
     * -- hasNext = false, do not try again row matches -- hasNext = true, do not try again row does
     * not match -- hasNext is undefined because we must try again
     *
     * @return Whether we need to read again
     */
    private synchronized boolean readNext() throws IOException {
        VPFFeatureClass featureClass = featureType.getFeatureClass();
        if (!resetCalled) {
            this.reset();
        }
        currentFeature = featureClass.readNext(featureType);
        return currentFeature != null;
    }

    /*
    private boolean readNext0() throws IOException {
        boolean result = true;
        VPFFile file = (VPFFile) featureType.getFeatureClass().getFileList().get(0);
        hasNext = false;
        SimpleFeature row = null;
        try {
            if (file.hasNext()) {
                row = file.readFeature();
            }
        } catch (IOException exc1) {
            // TODO Auto-generated catch block
            exc1.printStackTrace();
        } catch (IllegalAttributeException exc1) {
            // TODO Auto-generated catch block
            exc1.printStackTrace();
        }
        if ((row == null)) {
            hasNext = false;
            result = false;
        }
        // Exclude objects with a different FACC Code
        else if (featureType.getFaccCode() != null) {
            try {
                //Object temp = null;
                //for (int i = 0; temp == null && i < ALLOWED_FCODE_ATTRIBUTES.length; i++) {
                    //temp = row.getAttribute(ALLOWED_FCODE_ATTRIBUTES[i]);
                //}
                //String faccCode = temp.toString().trim();
                //if (featureType.getFaccCode().equals(faccCode))
                if (true) {
                    retrieveObject(file, row);
                    hasNext = true;
                    result = false;
                }
            } catch (RuntimeException exc) {
                // Ignore this case because it typically means the f_code is invalid
            }
        }
        return result;
    }
    */
    /**
     * Get the values from all of the columns based on their presence (or absense) in the rows
     *
     * <p>Potential cases: simple column join column non-matching join null value geometry
     *
     * @param file the file
     * @param row the row
     */
    /*
    private synchronized void retrieveObject(VPFFile file, SimpleFeature row) throws IOException {
        VPFFile secondFile = null;
        VPFColumn column = null;

        VPFFeatureClass featureClass = featureType.getFeatureClass();
        ;
        SimpleFeatureType featureClassType = featureClass.getFeatureType();

        // VPFFeatureType.debugFeatureType(featureType);
        // VPFFeatureType.debugFeatureType(featureClassType);
        // VPFFeatureType.debugFeature(row);

        Map<VPFFile, List<Object>> rows = generateFileRowMap(file, row);

        // VPFFeatureType.debugRowMap(rows);

        // List<AttributeDescriptor> attributes =
        // featureType.getFeatureClass().getAttributeDescriptors();
        // Object[] values = new Object[featureType.getAttributeCount()];

        List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();
        Object[] values = new Object[attributes.size()];

        Object value = null;
        String featureId = null;
        // Pass 1 - identify the feature identifier
        for (int inx = 0; inx < attributes.size(); inx++) {
            // I am thinking it is probably safer to look this up
            // by column name than by position, but if it breaks,
            // it is easy enough to change
            // values[inx] = row.getAttribute(inx);
            String attrName = attributes.get(inx).getLocalName();
            values[inx] = row.getAttribute(attrName);
            if (attrName.equals("id")) {
                // value = row.getAttribute(inx);
                value = values[inx];
                if (value != null) {
                    featureId = value.toString();
                }
                // break;
            }
        }
        try {
            currentFeature =
                    featureId != null
                            ? SimpleFeatureBuilder.build(featureType, values, featureId)
                            : null;
        } catch (IllegalAttributeException exc) {
            // This shouldn't happen since everything should be nillable
            exc.printStackTrace();
            currentFeature = null;
        }

        if (currentFeature == null) return;

        // VPFFeatureType.debugFeature(currentFeature);

        // Pass 2 - get the attributes, including the geometry
        for (int inx = 0; inx < attributes.size(); inx++) {
            try {
                if (attributes
                        .get(inx)
                        .getLocalName()
                        .equals(AnnotationFeatureType.ANNOTATION_ATTRIBUTE_NAME)) {
                    try {
                        // TODO: are we sure this is the intended action? Hard-coding an attribute
                        // to "nam"?
                        currentFeature.setAttribute(inx, "nam");
                    } catch (IllegalAttributeException exc) {
                        exc.printStackTrace();
                    }
                    continue;
                }
                AttributeDescriptor colDesc = attributes.get(inx);
                String colName = colDesc.getLocalName();
                Object colValue = currentFeature.getAttribute(colName);
                if (colValue != null) {
                    continue;
                }

                SimpleFeature tempRow = null;
                ColumnPair joinPair = null;
                String col1Name, col2Name;
                secondFile = null;
                value = null;
                column = null;

                for (Map.Entry<VPFFile, List<Object>> entry : rows.entrySet()) {
                    VPFFile joinFile = (VPFFile) entry.getKey();
                    if (joinFile == null) {
                        continue;
                    }

                    if (joinFile.getPathName().equals(file.getPathName())) {
                        continue;
                    }

                    secondFile = joinFile;

                    List<Object> joinData = (List<Object>) entry.getValue();
                    joinPair = (ColumnPair) joinData.get(0);

                    col1Name = joinPair.column1.getName();
                    col2Name = joinPair.column2.getName();

                    if (col1Name.equals(colName)) {
                        tempRow = (SimpleFeature) joinData.get(1);
                        column = joinPair.column2;
                        break;
                    }
                }

                // column = (VPFColumn) attributes.get(inx);
                // secondFile = getVPFFile(column);
                // SimpleFeature tempRow = (SimpleFeature) rows.get(secondFile);

                if (tempRow != null) {
                    // value = tempRow.getAttribute(column.getName());
                    value = tempRow.getAttribute(column.getName());
                    if (column.isAttemptLookup()) {
                        try {
                            // Attempt to perform a lookup and conversion
                            String featureClassName = getVPFFile(column).getFileName();
                            String intVdtFileName =
                                    featureType
                                            .getFeatureClass()
                                            .getDirectoryName()
                                            .concat(File.separator)
                                            .concat("INT.VDT");
                            VPFFile intVdtFile =
                                    VPFFileFactory.getInstance().getFile(intVdtFileName);
                            Iterator intVdtIter = intVdtFile.readAllRows().iterator();
                            while (intVdtIter.hasNext()) {
                                SimpleFeature intVdtRow = (SimpleFeature) intVdtIter.next();
                                if (intVdtRow
                                                .getAttribute("table")
                                                .toString()
                                                .trim()
                                                .equals(featureClassName)
                                        && (Short.parseShort(
                                                                intVdtRow
                                                                        .getAttribute("value")
                                                                        .toString())
                                                        == Short.parseShort(value.toString())
                                                && (intVdtRow
                                                        .getAttribute("attribute")
                                                        .toString()
                                                        .trim()
                                                        .equals(column.getName())))) {
                                    value = intVdtRow.getAttribute("description").toString().trim();
                                    break;
                                }
                            }
                            // If there is a problem, forget about mapping and continue
                        } catch (IOException exc) {
                        } catch (RuntimeException exc) {
                        }
                    }
                }
                try {
                    currentFeature.setAttribute(inx, value);
                } catch (ArrayIndexOutOfBoundsException exc) {
                    // TODO Auto-generated catch block
                    exc.printStackTrace();
                } catch (IllegalAttributeException exc) {
                    // TODO Auto-generated catch block
                    exc.printStackTrace();
                }
            } catch (ClassCastException exc2) {
                try {
                    // This is the area geometry case
                    featureType
                            .getFeatureClass()
                            .getGeometryFactory()
                            .createGeometry(featureType, currentFeature);
                } catch (IllegalAttributeException exc) {
                    // TODO Auto-generated catch block
                    exc.printStackTrace();
                } catch (SQLException exc) {
                    // TODO Auto-generated catch block
                    exc.printStackTrace();
                }
            }
        }
    }
    */

    /**
     * Returns the VPFFile for a particular column. It will only find the first match, but that
     * should be okay because duplicate columns will cause even bigger problems elsewhere.
     *
     * @param column the column to search for
     * @return the VPFFile that owns this column
     */
    private synchronized VPFFile getVPFFile(VPFColumn column) {
        String columnName = column.getName();
        VPFFile result = null;
        VPFFile temp;
        Iterator<VPFFile> iter = featureType.getFeatureClass().getFileList().iterator();
        while (iter.hasNext()) {
            temp = (VPFFile) iter.next();
            if ((temp != null) && (temp.getColumn(columnName) != null)) {
                result = temp;
                break;
            }
        }
        return result;
    }
    /**
     * Returns the VPFFile for a particular column. It will only find the first match, but that
     * should be okay because duplicate columns will cause even bigger problems elsewhere.
     *
     * @param column the column to search for
     * @return the VPFFile that owns this column
     */
    private synchronized VPFFile getVPFFile(AttributeDescriptor column) {
        Name columnName = column.getName();
        VPFFile result = null;
        VPFFile temp;
        Iterator<VPFFile> iter = featureType.getFeatureClass().getFileList().iterator();
        while (iter.hasNext()) {
            temp = iter.next();
            if ((temp != null) && (temp.getColumn(columnName.getLocalPart()) != null)) {
                result = temp;
                break;
            }
        }
        return result;
    }
    /** Need to reset the stream for the next time Resets the iterator by resetting the stream. */
    public synchronized void reset() {
        VPFFeatureClass featureClass = featureType.getFeatureClass();
        featureClass.reset();
        this.resetCalled = true;
        /*
        VPFFile file = (VPFFile) featureType.getFeatureClass().getFileList().get(0);
        file.reset();
        VPFFileFactory.getInstance().reset();
        */
    }
}
