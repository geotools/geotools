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
package org.geotools.validation;

import java.util.logging.Logger;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;


/**
 * Tests to see if a Feature ...
 * 
 * <p>
 * The geometry is first tested to see if it is null, and if it is null,  then
 * it is tested to see if it is allowed to be null by calling isNillable().
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class DefaultFeatureValidation implements FeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.validation");

    /** User's Name of this validation test. */
    private String name; // name of the validation

    /** User's description of this validation test. */
    private String description;

    /**
     * Identification of required FeatureType as dataStoreId:typeName.
     * 
     * <p>
     * The provided ValidationProcessor assumes that FeatureTypes will be
     * references will be of the form dataStoreId:typeName.
     * </p>
     * 
     * <p>
     * If "" or null is used All FetureTypes will be checked.
     * </p>
     */
    private String typeRef;

    /**
     * No argument constructor, required by the Java Bean Specification.
     */
    public DefaultFeatureValidation() {
    }

    /**
     * Sets the name of this validation.
     *
     * @param name The name of this validation.
     *
     * @see org.geotools.validation.Validation#setName(java.lang.String)
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * Access the user's name for this test.
     *
     * @return The name of this validation.
     *
     * @see org.geotools.validation.Validation#getName()
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the description of this validation.
     *
     * @param description The description of the validation.
     *
     * @see org.geotools.validation.Validation#setDescription(java.lang.String)
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     * Override getDescription.
     * 
     * <p>
     * Returns the description of this validation as a string.
     * </p>
     *
     * @return The description of this validation.
     *
     * @see org.geotools.validation.Validation#getDescription()
     */
    public final String getDescription() {
        return description;
    }

    /**
     * The priority level used to schedule this Validation.
     *
     * @return PRORITY_SIMPLE
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return PRIORITY_SIMPLE;
    }

    /**
     * Implementation of getTypeNames.
     *
     * @return Array of typeNames, or empty array for all, null for disabled
     *
     * @see org.geotools.validation.Validation#getTypeRefs()
     */
    public String[] getTypeRefs() {
        if (typeRef == null) {
            return null;
        }

        if (typeRef.equals("*")) {
            return ALL;
        }

        return new String[] { typeRef, };
    }

    /**
     * Access typeRef property.
     *
     * @return Returns the typeRef in the format dataStoreId:typeName.
     */
    public String getTypeRef() {
        return typeRef;
    }

    /**
     * Set typeRef to typeRef.
     *
     * @param typeRef The typeRef in the format dataStoreId:typeName
     */
    public void setTypeRef(String typeRef) {
        this.typeRef = typeRef;
    }

    /**
     * Validation test for feature.
     * 
     * <p>
     * Description of test ...
     * </p>
     *
     * @param feature The Feature to be validated
     * @param type The FeatureType of the feature
     * @param results The storage for error messages.
     *
     * @return <code>true</code> if the feature is a valid geometry.
     *
     * @see org.geotools.validation.FeatureValidation#validate
     */
    public boolean validate(SimpleFeature feature, SimpleFeatureType type,
        ValidationResults results) {
        LOGGER.warning(getName() + " not implemented");
        results.warning(feature, " test not implemented");

        return false;
    }
    
    //
    // Convience Methods
    //
    /**
     * Retrives a single LineString from feature.getDefaultGeometry.
     * <p>
     * If feature contains MultiLineString (or GeometryCollection ) of length
     * 1 it will be deemed sufficient. Shapefiles are determined to work with
     * MultiLineStrings of length 1 forcing the creation of this method.
     * </p>
     * 
     * <p>
     * If feature.getDefaultGeometry returns <code>null</code> this method
     * will return null. For most cases the validation should just be abandoned
     * with a warning; the user can separately specify a NullZero check. This
     * will prevent the same error (a null value) being reproted by
     * each and every SpatialValidation test.
     * </p>
     * 
     * @param feature Feature
     * @return feature.getDefaultGeomertry as a LineString, or <code>null</code>
     * @throws ClassCastException If feature.getDefaultGeometry is the wrong type
     */
    protected LineString getDefaultLineString( SimpleFeature feature ) throws ClassCastException {
        Geometry geom = (Geometry)feature.getDefaultGeometry();
        if (geom == null) {
            // Ignore null value, user can use NullZero check
            return null;
        }        
        if( geom instanceof LineString ){
            return (LineString) geom;            
        }
        else if( geom instanceof MultiLineString ){
            // Shapefiles are determined to give us their contents as
            // a MultiLineString - forcing our hand in this case
            //
            MultiLineString lines = (MultiLineString) geom;
            if( lines.getNumGeometries() == 1){
                return (LineString) lines.getGeometryN(0);
            }
            throw new ClassCastException("MultiLineString does not contain a single LineString");
        }
        else if( geom instanceof GeometryCollection ){
            GeometryCollection geoms = (GeometryCollection) geom;
            if( geoms.getNumGeometries() == 1 &&
                geoms.getGeometryN( 0 ) instanceof LineString ){
                return (LineString) geoms.getGeometryN(0);
            }
            throw new ClassCastException("GeometryCollection does not contain a single LineString");            
        }
        else {
            throw new ClassCastException("Cannot convert to LineString");
        }                
    }    
}
