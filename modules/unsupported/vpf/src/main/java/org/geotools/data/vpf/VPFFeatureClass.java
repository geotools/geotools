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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.DataTypesDefinition;
import org.geotools.data.vpf.ifc.FileConstants;
import org.geotools.data.vpf.readers.AreaGeometryFactory;
import org.geotools.data.vpf.readers.ConnectedNodeGeometryFactory;
import org.geotools.data.vpf.readers.EntityNodeGeometryFactory;
import org.geotools.data.vpf.readers.LineGeometryFactory;
import org.geotools.data.vpf.readers.TextGeometryFactory;
import org.geotools.data.vpf.readers.VPFGeometryFactory;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AnnotationFeatureType;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Geometry;


/**
 * A VPF feature class. Note that feature classes may contain one
 * or more feature types. However, all of the feature types of a 
 * feature class share the same schema. A feature type will therefore
 * delegate its schema related operations to its feature class.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 *
 * @source $URL$
 */
public class VPFFeatureClass implements DataTypesDefinition, FileConstants,
    SimpleFeatureType {
    /**
     * The contained feature type
     */
    private SimpleFeatureType featureType;

    /**
     * The columns that are part of this feature class
     */
    private final List columns = new Vector();

    /** The coverage this feature class is part of */
    private final VPFCoverage coverage;

    /** The path of the directory containing this feature class */
    private final String directoryName;

    /** A list of files which are read to retrieve data for this feature class */
    private final AbstractList fileList = new Vector();

    /** A list of ColumnPair objects which identify the file joins */
    private final AbstractList joinList = new Vector();

    /** The name of the feature class */
    private final String typeName;

    /** The uri of the namespace in which features should be created */
    private final URI namespace;

    /**
     * The geometry factory for this feature class
     */
    private VPFGeometryFactory geometryFactory;

    /**
     * Indicator that the feature type is a text feature.
     */
    private boolean textTypeFeature = false;

    /**
     * Constructor
     *
     * @param cCoverage the owning coverage
     * @param cName the name of the class
     * @param cDirectoryName the directory containing the class
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFFeatureClass(VPFCoverage cCoverage, String cName,
        String cDirectoryName) throws SchemaException{
        this(cCoverage, cName, cDirectoryName, null);
    }

    /**
     * Constructor
     *
     * @param cCoverage the owning coverage
     * @param cName the name of the class
     * @param cDirectoryName the directory containing the class
     * @param cNamespace the namespace to create features with.  If null then
     *        a default from VPFLibrary.DEFAULTNAMESPACE is assigned.
     * @throws SchemaException For problems making one of the feature classes as a FeatureType.
     */
    public VPFFeatureClass(VPFCoverage cCoverage, String cName,
        String cDirectoryName, URI cNamespace) throws SchemaException{
        coverage = cCoverage;
        directoryName = cDirectoryName;
        typeName = cName;
        if (cNamespace == null) {
            namespace = VPFLibrary.DEFAULT_NAMESPACE;
        } else {
            namespace = cNamespace;
        }
	    

        String fcsFileName = directoryName + File.separator + TABLE_FCS;

        try {
            VPFFile fcsFile = (VPFFile) VPFFileFactory.getInstance().getFile(fcsFileName);
            Iterator iter = fcsFile.readAllRows().iterator();

            while (iter.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iter.next();
                String featureClassName = feature.getAttribute("feature_class")
                                                 .toString().trim();

                if (typeName.equals(featureClassName)) {
                    addFCS(feature);
                }
            }

            // Deal with the geometry column
            iter = columns.iterator();

            GeometryDescriptor gat = null;
            AttributeDescriptor geometryColumn = null;

            while (iter.hasNext()) {
                geometryColumn = (AttributeDescriptor) iter.next();

                if(Geometry.class.isAssignableFrom(geometryColumn.getType().getBinding())){
                    if(geometryColumn instanceof GeometryDescriptor){
                        gat = (GeometryDescriptor)geometryColumn;
                    }else if (geometryColumn instanceof VPFColumn){
                        gat = ((VPFColumn)geometryColumn).getGeometryAttributeType();
                    }
                    break;
                }
            }

            SimpleFeatureType superType = null;
            // if it's a text geometry feature type add annotation as a super type
            if (textTypeFeature) {
                superType = AnnotationFeatureType.ANNOTATION;
            }
            
            SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
            b.setName(cName);
            b.setNamespaceURI(namespace);
            b.setSuperType(superType);
            b.addAll(columns);
            b.setDefaultGeometry(gat.getLocalName());
            
            featureType = b.buildFeatureType();
        } catch (IOException exp) {
            //We've already searched the FCS file once successfully
            //So this should never happen
            exp.printStackTrace();
        }
    }

    /**
     * Add the information from a new FCS row.
     *
     * @param row The FCS table row
     */
    private void addFCS(SimpleFeature row) //throws IOException
     {
        String table1 = row.getAttribute("table1").toString();
        String table1Key = row.getAttribute("table1_key").toString();
        String table2 = row.getAttribute("table2").toString();
        String table2Key = row.getAttribute("table2_key").toString();

        try {
            VPFFile vpfFile1 = VPFFileFactory.getInstance().getFile(directoryName.concat(
                        File.separator).concat(table1));
            addFileToTable(vpfFile1);

            VPFFile vpfFile2 = null;
            AttributeDescriptor joinColumn1 = (VPFColumn) vpfFile1.getDescriptor(table1Key);
            AttributeDescriptor joinColumn2;

            try {
                vpfFile2 = VPFFileFactory.getInstance().getFile(directoryName.concat(
                            File.separator).concat(table2));
                addFileToTable(vpfFile2);
                joinColumn2 = (VPFColumn) vpfFile2.getDescriptor(table2Key);
            } catch (IOException exc) {
                fileList.add(null);

                // We need to add a geometry column 
                joinColumn2 = buildGeometryColumn(table2);
            }

            // FCS's that are the inverse of existing ones are not needed
            // But we should never get this far
            if (!joinList.contains(new ColumnPair(joinColumn2, joinColumn1))) {
                joinList.add(new ColumnPair(joinColumn1, joinColumn2));
            }
        } catch (IOException exc) {
            // File was not present 
            // which means it is for a geometry table
            // we can safely ignore it for now 
            //          exc.printStackTrace();
        }
    }
    /**
     * Create a geometry column (usually for feature classes that
     * make use of tiles so simple joins can not be used)
     * @param table The name of the table containing the geometric primitives
     * @return An <code>AttributeType</code> for the geometry column which is actually a <code>GeometryAttributeType</code>
     */
    private AttributeDescriptor buildGeometryColumn(String table) {
        AttributeDescriptor result = null;

        table = table.trim().toLowerCase();

        // Why would the fileList already contain a null?
        //      if(!fileList.contains(null)){
        CoordinateReferenceSystem crs = getCoverage().getLibrary().getCoordinateReferenceSystem();
        if(crs != null){
            result = new AttributeTypeBuilder().binding( Geometry.class )
                .nillable(true).length(-1).crs(crs).buildDescriptor("GEOMETRY");
        }else{
            result = new AttributeTypeBuilder().binding( Geometry.class )
                .nillable(true).buildDescriptor("GEOMETRY");
        }
        columns.add(result);

        setGeometryFactory(table);

        //      }
        return result;
    }
    /**
     * Identifies the type of geometry factory to use based on the
     * name of the table containing the geometry, then constructs the
     * appropriate geometry factory object.
     * @param table The name of the geometry table
     */
    private void setGeometryFactory(String table) {
        if (table.equals(EDGE_PRIMITIVE)) {
            geometryFactory = new LineGeometryFactory();
        } else if (table.equals(FACE_PRIMITIVE)) {
            geometryFactory = new AreaGeometryFactory();
        } else if (table.equals(CONNECTED_NODE_PRIMITIVE)) {
            geometryFactory = new ConnectedNodeGeometryFactory();
        } else if (table.equals(ENTITY_NODE_PRIMITIVE)) {
            geometryFactory = new EntityNodeGeometryFactory();
        } else if (table.equals(TEXT_PRIMITIVE)) {
            geometryFactory = new TextGeometryFactory();
            textTypeFeature = true;
        }

        // if an invalid string is returned, there will be no geometry
    }
    /**
     * Adds all of the columns from a VPF file into the table. Note:
     * This does not handle columns with the same name particularly well. 
     * Perhaps the xpath mechanism can be used to help here.
     * @param vpfFile the <code>VPFFile</code> object to use
     */
    private void addFileToTable(VPFFile vpfFile) {
        //      Class columnClass;
        boolean addPrimaryKey = fileList.isEmpty();

        // Check to see if we have already grabbed this file
        if (!fileList.contains(vpfFile)) {
            fileList.add(vpfFile);

            // Pull the columns off of the file and add them to our schema
            // Except for the first file, ignore the first column since it is a join column
            for (int inx = addPrimaryKey ? 0 : 1;
                    inx < vpfFile.getAttributeCount(); inx++) {
                columns.add(vpfFile.getDescriptor(inx));
            }
        }
    }
    
    /**
     * The coverage that owns this feature class
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
    public List getFileList() {
        return fileList;
    }

    /**
     * DOCUMENT ME!
     *
     * @return a<code>List</code> containing <code>ColumnPair</code> objects 
     *         which identify the file joins.
     */
    public List getJoinList() {
        return joinList;
    }

    /* (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getTypeName()
     */
    public String getTypeName() {
        return featureType.getTypeName();
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

    /**
     * @return Returns the geometryFactory.
     */
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
		return featureType.getType( name );
	}

	public org.opengis.feature.type.AttributeType getType(String name) {
		return featureType.getType( name );
	}

	public org.opengis.feature.type.AttributeType getType(int index) {
		return featureType.getType( index );
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
