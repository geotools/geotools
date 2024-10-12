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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;

/**
 * A VPF feature type. Note that feature classes may contain one or more feature types. However, all
 * of the feature types of a feature class share the same schema. A feature type will therefore
 * delegate its schema related operations to its feature class.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFFeatureType implements SimpleFeatureType {
    /** The feature class that this feature type belongs to */
    private final VPFFeatureClass featureClass;
    /** The type name for this specific feature type */
    private final String typeName;
    /** The FACC code, a two-letter, 3-number code identifying the feature type */
    private final String faccCode;

    public static void debugFeature(SimpleFeature feature) {
        String featureName = feature.getName() != null ? feature.getName().getLocalPart() : "";
        String id = feature.getID();
        SimpleFeatureType featureType = feature.getFeatureType();
        String featureTypeName = featureType.getTypeName();

        int attrCount = feature.getAttributeCount();
        int attrCount2 = featureType.getAttributeCount();

        VPFLogger.log("****** dbug feature: " + id);
        VPFLogger.log("          name: " + featureName);
        VPFLogger.log("      typeName: " + featureTypeName);
        VPFLogger.log("     attrCount: " + attrCount);
        VPFLogger.log("    attrCount2: " + attrCount2);

        for (int iat = 0; iat < attrCount; iat++) {
            Object attr = feature.getAttribute(iat);
            AttributeDescriptor desc = featureType.getDescriptor(iat);
            String aname = desc != null ? desc.getLocalName() : "null";
            String avalue = attr != null ? attr.toString() : "null";

            VPFLogger.log(aname + ": " + avalue);
        }
    }

    public static void debugFeatureType(SimpleFeatureType featureType) {
        String featureTypeName = featureType.getTypeName();

        int attrCount = featureType.getAttributeCount();
        VPFLogger.log("****** dbug featureType: " + featureTypeName);
        VPFLogger.log("              attrCount: " + attrCount);

        for (int iat = 0; iat < attrCount; iat++) {
            AttributeDescriptor desc = featureType.getDescriptor(iat);
            VPFLogger.log(desc.getLocalName());
        }
    }

    /**
     * Constructor
     *
     * @param cFeatureClass The owning feature class
     * @param cFeature A <code>Feature</code> from the CHAR.VDT file with more detailed information
     *     for this feature type
     */
    public VPFFeatureType(VPFFeatureClass cFeatureClass, SimpleFeature cFeature) {
        featureClass = cFeatureClass;
        faccCode = cFeature.getAttribute("value").toString().trim();
        String mainTableFileName = cFeature.getAttribute("table").toString().trim();

        String tempTypeName = cFeature.getAttribute("description").toString().trim();

        // This block helps us give tables a distinguishing suffix
        try {
            int index = mainTableFileName.lastIndexOf(".") + 1;
            String dimensionality =
                    mainTableFileName.substring(index, index + 1).toLowerCase();
            if (dimensionality.equals("a")) {
                tempTypeName = tempTypeName.concat(" Area");
            } else if (dimensionality.equals("l")) {
                tempTypeName = tempTypeName.concat(" Line");
            } else if (dimensionality.equals("p")) {
                tempTypeName = tempTypeName.concat(" Point");
            } else if (dimensionality.equals("t")) {
                tempTypeName = tempTypeName.concat(" Text");
            }
        } catch (RuntimeException e) {
            // If this does not work, no big deal
        }
        tempTypeName = tempTypeName.toUpperCase();
        tempTypeName = tempTypeName.replace(' ', '_');
        tempTypeName = tempTypeName.replace('/', '_');
        tempTypeName = tempTypeName.replace('(', '_');
        tempTypeName = tempTypeName.replace(')', '_');
        typeName = tempTypeName;
    }
    /**
     * A constructor for feature types with no information in a char.vdt file.
     *
     * @param cFeatureClass The owning feature class
     */
    public VPFFeatureType(VPFFeatureClass cFeatureClass) {
        featureClass = cFeatureClass;
        faccCode = null;
        typeName = cFeatureClass.getTypeName().toUpperCase();
    }

    /* (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getAttributeCount()
     */
    @Override
    public int getAttributeCount() {
        return featureClass.getAttributeCount();
    }

    /** @return The <code>VPFCoverage</code> that this <code>FeatureType</code> belongs to. */
    public VPFCoverage getCoverage() {
        return featureClass.getCoverage();
    }

    /**
     * @return The <code>String</code> path for the directory containing the <code>VPFFeatureClass
     *     </code> that this <code>FeatureType</code> belongs to.
     */
    public String getDirectoryName() {
        return featureClass.getDirectoryName();
    }
    /** @return Returns the featureClass. */
    public VPFFeatureClass getFeatureClass() {
        return featureClass;
    }

    public synchronized List<SimpleFeature> readAllRows() throws IOException {
        return this.featureClass.readAllRows(this);
    }

    /**
     * Returns a list of file objects
     *
     * @return A <code>List</code> containing <code>VPFFile</code> objects.
     */
    public List getFileList() {
        return featureClass.getFileList();
    }
    /**
     * @return A <code>List</code> containing the <code>ColumnPair</code> objects which identify the
     *     file joins for the <code>VPFFeatureClass</code> that this <code>FeatureType</code>
     *     belongs to.
     */
    /*
    public List getJoinList() {
        return featureClass.getJoinList();
    }
    */

    /* (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getTypeName()
     */
    @Override
    public String getTypeName() {
        return typeName;
    }

    /* (non-Javadoc)
     * @see org.geotools.feature.FeatureType#isAbstract()
     */
    @Override
    public boolean isAbstract() {
        return featureClass.isAbstract();
    }

    /**
     * The FACC code, a two-letter, 3-number code identifying the feature type
     *
     * @return Returns the FACC Code.
     */
    public String getFaccCode() {
        return faccCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VPFFeatureType) {
            return Objects.equals(featureClass, ((VPFFeatureType) obj).featureClass);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return featureClass.hashCode();
    }

    @Override
    public AttributeDescriptor getDescriptor(int index) {
        return featureClass.getDescriptor(index);
    }

    @Override
    public List<AttributeDescriptor> getAttributeDescriptors() {
        return featureClass.getAttributeDescriptors();
    }

    @Override
    public AttributeDescriptor getDescriptor(Name name) {
        return featureClass.getDescriptor(name);
    }

    @Override
    public AttributeDescriptor getDescriptor(String name) {
        return featureClass.getDescriptor(name);
    }

    @Override
    public org.geotools.api.feature.type.AttributeType getType(Name name) {
        return featureClass.getType(name);
    }

    @Override
    public org.geotools.api.feature.type.AttributeType getType(String name) {
        return featureClass.getType(name);
    }

    @Override
    public org.geotools.api.feature.type.AttributeType getType(int index) {
        return featureClass.getType(index);
    }

    @Override
    public List<AttributeType> getTypes() {
        return featureClass.getTypes();
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return featureClass.getCoordinateReferenceSystem();
    }

    @Override
    public GeometryDescriptor getGeometryDescriptor() {
        return featureClass.getGeometryDescriptor();
    }

    @Override
    public Class<Collection<Property>> getBinding() {
        return featureClass.getBinding();
    }

    @Override
    public Collection<PropertyDescriptor> getDescriptors() {
        return featureClass.getDescriptors();
    }

    @Override
    public boolean isInline() {
        return featureClass.isInline();
    }

    @Override
    public List<Filter> getRestrictions() {
        return featureClass.getRestrictions();
    }

    @Override
    public org.geotools.api.feature.type.AttributeType getSuper() {
        return featureClass.getSuper();
    }

    @Override
    public boolean isIdentified() {
        return featureClass.isIdentified();
    }

    @Override
    public InternationalString getDescription() {
        return featureClass.getDescription();
    }

    @Override
    public Name getName() {
        return featureClass.getName();
    }

    @Override
    public Map<Object, Object> getUserData() {
        return featureClass.getUserData();
    }

    @Override
    public int indexOf(String name) {
        return featureClass.indexOf(name);
    }

    @Override
    public int indexOf(Name name) {
        return featureClass.indexOf(name);
    }
}
