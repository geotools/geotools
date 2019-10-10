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

import static org.geotools.data.vpf.ifc.FileConstants.CONNECTED_NODE_PRIMITIVE;
import static org.geotools.data.vpf.ifc.FileConstants.EDGE_PRIMITIVE;
import static org.geotools.data.vpf.ifc.FileConstants.ENTITY_NODE_PRIMITIVE;
import static org.geotools.data.vpf.ifc.FileConstants.FACE_PRIMITIVE;
import static org.geotools.data.vpf.ifc.FileConstants.TABLE_FCS;
import static org.geotools.data.vpf.ifc.FileConstants.TEXT_PRIMITIVE;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.readers.AreaGeometryFactory;
import org.geotools.data.vpf.readers.ConnectedNodeGeometryFactory;
import org.geotools.data.vpf.readers.EntityNodeGeometryFactory;
import org.geotools.data.vpf.readers.LineGeometryFactory;
import org.geotools.data.vpf.readers.TextGeometryFactory;
import org.geotools.data.vpf.readers.VPFGeometryFactory;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AnnotationFeatureType;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

/**
 * A VPF feature class. Note that feature classes may contain one or more feature types. However,
 * all of the feature types of a feature class share the same schema. A feature type will therefore
 * delegate its schema related operations to its feature class.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFFeatureClass implements SimpleFeatureType {
    /** The contained feature type */
    private SimpleFeatureType featureType;

    /** The columns that are part of this feature class */
    private final List<VPFColumn> columns = new Vector<VPFColumn>();

    private final Map<String, ColumnSet> columnSet = new LinkedHashMap<>();
    private final Map<String, TableRelation> relations = new LinkedHashMap<>();

    /** The coverage this feature class is part of */
    private final VPFCoverage coverage;

    /** The path of the directory containing this feature class */
    private final String directoryName;

    /** A list of files which are read to retrieve data for this feature class */
    // private final AbstractList<VPFFile> fileList = new Vector<VPFFile>();

    /** A list of ColumnPair objects which identify the file joins */
    // private final AbstractList joinList = new Vector();

    /** The name of the feature class */
    private final String typeName;

    /** The uri of the namespace in which features should be created */
    private final URI namespace;

    /** The geometry factory for this feature class */
    private VPFGeometryFactory geometryFactory;

    /** Indicator that the feature type is a text feature. */
    private boolean textTypeFeature = false;

    private boolean enableFeatureCache = true;

    private final List<SimpleFeature> featureCache = new ArrayList<SimpleFeature>();

    private int cacheRow = 0;

    private boolean debug = false;

    /**
     * Constructor
     *
     * @param cCoverage the owning coverage
     * @param cName the name of the class
     * @param cDirectoryName the directory containing the class
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFFeatureClass(VPFCoverage cCoverage, String cName, String cDirectoryName)
            throws SchemaException {
        this(cCoverage, cName, cDirectoryName, null);
    }

    /**
     * Constructor
     *
     * @param cCoverage the owning coverage
     * @param cName the name of the class
     * @param cDirectoryName the directory containing the class
     * @param cNamespace the namespace to create features with. If null then a default from
     *     VPFLibrary.DEFAULTNAMESPACE is assigned.
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFFeatureClass(
            VPFCoverage cCoverage, String cName, String cDirectoryName, URI cNamespace)
            throws SchemaException {
        coverage = cCoverage;
        directoryName = cDirectoryName;
        typeName = cName;
        if (cNamespace == null) {
            namespace = VPFLibrary.DEFAULT_NAMESPACE;
        } else {
            namespace = cNamespace;
        }

        if (VPFLogger.isLoggable(Level.FINEST)) {
            if (typeName.equalsIgnoreCase("VPFFEATURETYPE")) {
                this.debug = true;
            }
        }

        String fcsFileName = directoryName + File.separator + TABLE_FCS;

        try {
            VPFFile fcsFile = (VPFFile) VPFFileFactory.getInstance().getFile(fcsFileName);

            Iterator<SimpleFeature> iter = fcsFile.readAllRows().iterator();

            if (this.debug) {
                SimpleFeatureType fcsFeatureType = fcsFile.getFeatureType();
                VPFFeatureType.debugFeatureType(fcsFeatureType);
            }

            while (iter.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iter.next();
                String featureClassName = feature.getAttribute("feature_class").toString().trim();

                if (typeName.equals(featureClassName)) {
                    if (this.debug) {
                        VPFFeatureType.debugFeature(feature);
                    }
                    addFCS(feature);
                }
            }

            this.assembleColumns();

            // Deal with the geometry column
            Iterator<VPFColumn> iter2 = columns.iterator();
            VPFColumn column;
            String geometryName = null;

            while (iter2.hasNext()) {
                column = (VPFColumn) iter2.next();
                if (column == null) continue;
                if (column.isGeometry()) {
                    geometryName = column.getName();
                    break;
                }
            }

            SimpleFeatureType superType = null;
            // if it's a text geometry feature type add annotation as a super type
            if (textTypeFeature) {
                superType = AnnotationFeatureType.ANNOTATION;
            }

            if (this.debug) {
                VPFLogger.log("class col count: " + columns.size());
            }

            SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
            b.setName(cName);
            b.setNamespaceURI(namespace);
            b.setSuperType(superType);
            for (VPFColumn col : columns) {
                if (col == null) continue;
                b.add(col.getDescriptor());
            }
            b.setDefaultGeometry(geometryName);

            featureType = b.buildFeatureType();

        } catch (IOException exp) {
            // We've already searched the FCS file once successfully
            // So this should never happen
            // exp.printStackTrace();
        }
    }

    /**
     * Add the information from a new FCS row.
     *
     * @param row The FCS table row
     */
    private synchronized void addFCS(SimpleFeature row) // throws IOException
            {
        String table1 = row.getAttribute("table1").toString().toUpperCase();
        String table1Key = row.getAttribute("table1_key").toString();
        String table2 = row.getAttribute("table2").toString().toUpperCase();
        String table2Key = row.getAttribute("table2_key").toString();

        if (this.debug) {
            VPFLogger.log("++++++++ addFCS");
            VPFLogger.log("table1: " + table1 + " key: " + table1Key);
            VPFLogger.log("table2: " + table2 + " key: " + table2Key);
        }

        try {
            VPFFile vpfFile1 =
                    VPFFileFactory.getInstance()
                            .getFile(directoryName.concat(File.separator).concat(table1));
            // addFileToTable(vpfFile1);

            if (!columnSet.containsKey(table1)) {
                ColumnSet cs = new ColumnSet();
                cs.setTable(table1, vpfFile1);
                columnSet.put(table1, cs);
            }

            ColumnSet cs1 = columnSet.get(table1);

            VPFFile vpfFile2 = null;
            VPFColumn joinColumn1 = vpfFile1.getColumn(table1Key);
            VPFColumn joinColumn2 = null;
            VPFColumn geometryColumn = null;
            boolean isGeometryTable = false;

            try {

                if (table2.equalsIgnoreCase(FACE_PRIMITIVE)) {
                    geometryColumn = buildGeometryColumn(table2);
                    joinColumn2 = null;
                    isGeometryTable = true;
                } else {
                    vpfFile2 =
                            VPFFileFactory.getInstance()
                                    .getFile(directoryName.concat(File.separator).concat(table2));
                    // addFileToTable(vpfFile2);
                    joinColumn2 = vpfFile2.getColumn(table2Key);
                }
            } catch (IOException exc) {
                vpfFile2 = null;
                joinColumn2 = null;
                isGeometryTable = true;
                // fileList.add(null);
                // We need to add a geometry column
                geometryColumn = buildGeometryColumn(table2);
            }

            if (!columnSet.containsKey(table2)) {
                ColumnSet cs = new ColumnSet();

                VPFGeometryFactory geometryFactory = isGeometryTable ? this.geometryFactory : null;

                cs.setTable(table2, vpfFile2);
                cs.setGeometry(isGeometryTable, geometryFactory, geometryColumn);

                columnSet.put(table2, cs);
            }

            ColumnSet cs2 = columnSet.get(table2);

            String relName = table1 + "_" + table1Key + "_" + table2 + "_" + table2Key;
            String relName2 = table2 + "_" + table2Key + "_" + table1 + "_" + table1Key;

            if (!relations.containsKey(relName) && !relations.containsKey(relName2)) {
                TableRelation tr = new TableRelation();

                tr.setTable(table2, cs2, table2Key, joinColumn2);
                tr.setJoinTable(table1, cs1, table1Key, joinColumn1);

                relations.put(relName, tr);
            }

        } catch (IOException exc) {
            // exc.printStackTrace();
        }
    }

    public synchronized void reset() {

        if (!this.enableFeatureCache || this.featureCache.size() == 0) {
            Iterator<Map.Entry<String, ColumnSet>> itr = columnSet.entrySet().iterator();
            Map.Entry<String, ColumnSet> first = itr.next();

            if (first == null) return;

            ColumnSet cs = first.getValue();

            VPFFile rootTable = cs != null ? cs.table : null;
            if (rootTable == null) return;

            cs.currRow = null;
            cs.geometry = null;

            while (itr.hasNext()) {
                Map.Entry<String, ColumnSet> next = itr.next();
                ColumnSet jcs = next.getValue();
                jcs.currRow = null;
                jcs.geometry = null;
            }

            rootTable.reset();
        }

        this.cacheRow = 0;
    }

    private synchronized void closeFiles() {
        Iterator<Map.Entry<String, ColumnSet>> itr = columnSet.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ColumnSet> next = itr.next();
            ColumnSet cs = next.getValue();
            try {
                if (cs.table != null) cs.table.close();
            } catch (IOException e) {
                VPFLogger.log("***** Exception when closing file");
                VPFLogger.log(cs.tableName);
                VPFLogger.log(cs.table.getPathName());
            }
        }
    }

    public synchronized boolean hasNext() {

        if (this.enableFeatureCache && this.featureCache.size() > 0) {
            return this.cacheRow < this.featureCache.size();
        } else {
            return this.internalHasNext();
        }
    }

    private synchronized boolean internalHasNext() {
        Iterator<Map.Entry<String, ColumnSet>> itr = columnSet.entrySet().iterator();
        Map.Entry<String, ColumnSet> first = itr.next();

        if (first == null) return false;

        ColumnSet cs = first.getValue();
        VPFFile rootTable = cs != null ? cs.table : null;
        if (rootTable == null) return false;

        return rootTable.hasNext();
    }

    public synchronized List<SimpleFeature> readAllRows(SimpleFeatureType featureType)
            throws IOException {

        this.enableFeatureCache = true;
        this.featureCache.clear();
        this.reset();

        readNext(featureType);

        return this.featureCache;
    }

    public synchronized SimpleFeature readNext(SimpleFeatureType featureType) {

        SimpleFeature nextFeature = null;
        if (this.enableFeatureCache && this.featureCache.size() == 0) {
            this.reset();
            while (this.internalHasNext()) {
                SimpleFeature feature = joinRows(featureType);

                this.featureCache.add(feature);
            }
            if (this.featureCache.size() > 0) {
                this.closeFiles();
            }
        }
        if (this.enableFeatureCache) {
            if (this.cacheRow < this.featureCache.size()) {
                nextFeature = this.featureCache.get(this.cacheRow);
                this.cacheRow++;
            } else {
                nextFeature = null;
            }
        } else nextFeature = joinRows(featureType);

        return nextFeature;
    }

    private synchronized void assembleColumns() {
        Iterator<Map.Entry<String, ColumnSet>> itr = columnSet.entrySet().iterator();

        columns.clear();

        while (itr.hasNext()) {
            Map.Entry<String, ColumnSet> next = itr.next();
            ColumnSet cs = next.getValue();
            if (cs.isGeometryTable) {
                columns.add(cs.geometryColumn);
            } else {
                VPFFile vpfFile = cs.table;
                for (int inx = 0; inx < vpfFile.getAttributeCount(); inx++) {
                    VPFColumn col = vpfFile.getColumn(inx);
                    String colName = col.getName();
                    if (columns.size() > 0 && colName.equalsIgnoreCase("id")) continue;
                    columns.add(col);
                }
            }
        }
    }

    private synchronized SimpleFeature joinRows(SimpleFeatureType featureType) {

        Iterator<Map.Entry<String, ColumnSet>> itr = columnSet.entrySet().iterator();
        Map.Entry<String, ColumnSet> first = itr.next();

        if (first == null) return null;

        ColumnSet cs = first.getValue();
        VPFFile rootTable = cs != null ? cs.table : null;
        if (rootTable == null) return null;

        SimpleFeature row = null;
        try {
            row = rootTable.readFeature();
        } catch (Exception e) {
            row = null;
        }
        if (row == null) return null;

        cs.currRow = row;

        Object fid = row.getAttribute("id");

        String featureId = fid != null ? fid.toString() : UUID.randomUUID().toString();

        Iterator<Map.Entry<String, TableRelation>> ritr = relations.entrySet().iterator();

        while (ritr.hasNext()) {

            Map.Entry<String, TableRelation> next = ritr.next();

            TableRelation tr = next.getValue();

            // String joinTableName = tr.tableName;
            String joinTableKeyName = tr.tableKeyName;
            ColumnSet jcs = tr.colSet;
            if (jcs == null) continue;
            VPFFile joinTable = jcs.table;

            jcs.currRow = null;

            // String foreignTableName = tr.joinTableName;
            String foreignTableKeyName = tr.joinTableKeyName;
            ColumnSet fcs = tr.joinColSet;
            if (fcs == null || fcs.currRow == null) continue;

            VPFFile foreignTable = fcs.table;
            if (foreignTable == null) continue;

            Object foreignKeyValue = fcs.currRow.getAttribute(foreignTableKeyName);

            if (foreignKeyValue == null) continue;

            SimpleFeature jrow = null;

            if (jcs.isGeometryTable && jcs.geometryFactory != null) {

                Geometry geometry = null;
                try {
                    geometry = jcs.geometryFactory.buildGeometry(this, fcs.currRow);
                } catch (Exception e) {
                    geometry = null;
                    e.printStackTrace();
                }
                jcs.geometry = geometry;

            } else if (joinTable != null) {

                joinTable.reset();

                try {
                    while (joinTable.hasNext()) {
                        SimpleFeature nrow = joinTable.readFeature();
                        if (nrow == null) break;
                        Object joinKeyValue = nrow.getAttribute(joinTableKeyName);
                        if (joinKeyValue == null) break;

                        if (Objects.equals(foreignKeyValue, joinKeyValue)) {
                            jrow = nrow;
                            break;
                        }
                    }
                } catch (Exception e) {
                    jrow = null;
                }
            }
            jcs.currRow = jrow;
        }

        return combineColumnSets(featureId, featureType);
    }

    private synchronized SimpleFeature combineColumnSets(
            String featureId, SimpleFeatureType featureType) {

        Iterator<Map.Entry<String, ColumnSet>> itr = columnSet.entrySet().iterator();

        Geometry geometry = null;

        List<Object> vlist = new ArrayList<Object>();

        // List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();

        while (itr.hasNext()) {
            Map.Entry<String, ColumnSet> next = itr.next();
            ColumnSet cs = next.getValue();
            if (cs.isGeometryTable) {
                geometry = cs.geometry;
                vlist.add(null);
            } else {
                SimpleFeature row = cs.currRow;
                // Integer attrCount = row != null ? row.getAttributeCount() : null;
                int colCount = cs.colNames.size();
                for (int inx = 0; inx < colCount; inx++) {
                    String colName = cs.colNames.get(inx);
                    if (vlist.size() > 0 && colName.equalsIgnoreCase("id")) continue;
                    Object value = row != null ? row.getAttribute(colName) : null;
                    vlist.add(value);
                }
            }
        }

        if (this.debug) {
            VPFFeatureType.debugFeatureType(this.featureType);
            VPFFeatureType.debugFeatureType(featureType);
        }

        SimpleFeature feature = SimpleFeatureBuilder.build(featureType, vlist, featureId);
        if (geometry != null) {
            feature.setDefaultGeometry(geometry);
        }
        return feature;
    }

    /**
     * Create a geometry column (usually for feature classes that make use of tiles so simple joins
     * can not be used)
     *
     * @param table The name of the table containing the geometric primitives
     * @return An <code>AttributeType</code> for the geometry column which is actually a <code>
     *     GeometryAttributeType</code>
     */
    private synchronized VPFColumn buildGeometryColumn(String table) {
        AttributeDescriptor descriptor = null;
        table = table.trim().toUpperCase();

        if (this.debug) {
            VPFLogger.log("buildGeometryColumn: " + table);
        }

        CoordinateReferenceSystem crs = getCoverage().getLibrary().getCoordinateReferenceSystem();
        if (crs != null) {
            descriptor =
                    new AttributeTypeBuilder()
                            .binding(Geometry.class)
                            .nillable(true)
                            .length(-1)
                            .crs(crs)
                            .buildDescriptor("GEOMETRY");
        } else {
            descriptor =
                    new AttributeTypeBuilder()
                            .binding(Geometry.class)
                            .nillable(true)
                            .buildDescriptor("GEOMETRY");
        }

        VPFColumn result = new VPFColumn("GEOMETRY", descriptor); // how to construct
        setGeometryFactory(table);

        return result;
    }
    /**
     * Identifies the type of geometry factory to use based on the name of the table containing the
     * geometry, then constructs the appropriate geometry factory object.
     *
     * @param table The name of the geometry table
     */
    private synchronized void setGeometryFactory(String table) {
        if (table.equalsIgnoreCase(EDGE_PRIMITIVE)) {
            geometryFactory = new LineGeometryFactory();
        } else if (table.equalsIgnoreCase(FACE_PRIMITIVE)) {
            geometryFactory = new AreaGeometryFactory();
        } else if (table.equalsIgnoreCase(CONNECTED_NODE_PRIMITIVE)) {
            geometryFactory = new ConnectedNodeGeometryFactory();
        } else if (table.equalsIgnoreCase(ENTITY_NODE_PRIMITIVE)) {
            geometryFactory = new EntityNodeGeometryFactory();
        } else if (table.equalsIgnoreCase(TEXT_PRIMITIVE)) {
            geometryFactory = new TextGeometryFactory();
            textTypeFeature = true;
        }

        // if an invalid string is returned, there will be no geometry
    }

    /**
     * The coverage that owns this feature class
     *
     * @return a <code>VPFCoverage</code> object
     */
    public VPFCoverage getCoverage() {
        return coverage;
    }

    /**
     * The path to the directory that contains this feature class
     *
     * @return a <code>String</code> value representing the path to the directory.
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * Returns a list of file objects
     *
     * @return a <code>List</code> containing <code>VPFFile</code> objects.
     */
    public synchronized List<VPFFile> getFileList() {
        // return fileList;

        List<ColumnSet> csets = new ArrayList<ColumnSet>(columnSet.values());
        List<VPFFile> fileList = new ArrayList<VPFFile>();
        for (int i = 0; i < csets.size(); i++) {
            ColumnSet cs = csets.get(i);
            fileList.add(cs.table);
        }
        return fileList;
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /* (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getTypeName()
     */
    public String getTypeName() {
        return featureType.getTypeName();
    }

    public String getFCTypeName() {
        return typeName;
    }

    /* (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getAttributeCount()
     */
    public int getAttributeCount() {
        return featureType.getAttributeCount();
    }

    /* (non-Javadoc)
     * @see org.geotools.feature.FeatureType#isAbstract()
     */
    public boolean isAbstract() {
        return featureType.isAbstract();
    }

    /** @return Returns the geometryFactory. */
    public VPFGeometryFactory getGeometryFactory() {
        return geometryFactory;
    }

    public boolean equals(Object obj) {
        return featureType.equals(obj);
    }

    public int hashCode() {
        return featureType.hashCode();
    }

    public AttributeDescriptor getDescriptor(Name name) {
        return featureType.getDescriptor(name);
    }

    public AttributeDescriptor getDescriptor(String name) {
        return featureType.getDescriptor(name);
    }

    public AttributeDescriptor getDescriptor(int index) {
        return featureType.getDescriptor(index);
    }

    public List getAttributeDescriptors() {
        return featureType.getAttributeDescriptors();
    }

    public org.opengis.feature.type.AttributeType getType(Name name) {
        return featureType.getType(name);
    }

    public org.opengis.feature.type.AttributeType getType(String name) {
        return featureType.getType(name);
    }

    public org.opengis.feature.type.AttributeType getType(int index) {
        return featureType.getType(index);
    }

    public List getTypes() {
        return featureType.getTypes();
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return featureType.getCoordinateReferenceSystem();
    }

    public GeometryDescriptor getGeometryDescriptor() {
        return featureType.getGeometryDescriptor();
    }

    public Class getBinding() {
        return featureType.getBinding();
    }

    public Collection getDescriptors() {
        return featureType.getDescriptors();
    }

    public boolean isInline() {
        return featureType.isInline();
    }

    public org.opengis.feature.type.AttributeType getSuper() {
        return featureType.getSuper();
    }

    public boolean isIdentified() {
        return featureType.isIdentified();
    }

    public InternationalString getDescription() {
        return featureType.getDescription();
    }

    public Name getName() {
        return featureType.getName();
    }

    public int indexOf(String name) {
        return featureType.indexOf(name);
    }

    public int indexOf(Name name) {
        return featureType.indexOf(name);
    }

    public List<Filter> getRestrictions() {
        return featureType.getRestrictions();
    }

    public Map<Object, Object> getUserData() {
        return featureType.getUserData();
    }
}
