/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.util.Collection;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.opengis.feature.Association;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AssociationDescriptor;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Factory for creating instances of the Attribute family of classes.
 * 
 * @author Ian Schneider
 * @author Gabriel Roldan
 * @author Justin Deoliveira
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class AbstractFeatureFactoryImpl implements FeatureFactory {
 
	/**
	 * Factory used to create CRS objects
	 */
    CRSFactory crsFactory;
    /**
     * Factory used to create geomtries
     */
    GeometryFactory  geometryFactory;
    
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    
    /**
     * Whether the features to be built should be self validating on construction and value setting, or not.
     * But default, not, subclasses do override this value
     */
    boolean validating = false;
    
    public CRSFactory getCRSFactory() {
        return crsFactory;
    }

    public void setCRSFactory(CRSFactory crsFactory) {
        this.crsFactory = crsFactory;
    }

    public GeometryFactory getGeometryFactory() {
        return geometryFactory;
    }

    public void setGeometryFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }
    
    public Association createAssociation(Attribute related, AssociationDescriptor descriptor) {
        return new AssociationImpl(related,descriptor);
    }
	
	public Attribute createAttribute( Object value, AttributeDescriptor descriptor, String id ) {
		return new AttributeImpl(value,descriptor, id == null? null : ff.gmlObjectId(id));
	}
	
	public GeometryAttribute createGeometryAttribute(
		Object value, GeometryDescriptor descriptor, String id, CoordinateReferenceSystem crs
	) {
	
		return new GeometryAttributeImpl(value,descriptor, id == null? null : ff.gmlObjectId(id));
	}
	
	public ComplexAttribute createComplexAttribute( 
		Collection value, AttributeDescriptor descriptor, String id
	) {
		return new ComplexAttributeImpl(value, descriptor, id == null? null : ff.gmlObjectId(id) );
	}

	public ComplexAttribute createComplexAttribute( Collection value, ComplexType type, String id ) 
	{
		return new ComplexAttributeImpl(value, type, id == null? null : ff.gmlObjectId(id) );
	}
	
	public Feature createFeature(Collection value, AttributeDescriptor descriptor, String id) {
		return new FeatureImpl(value,descriptor,ff.featureId(id));
	}

	public Feature createFeature(Collection value, FeatureType type, String id) {
		return new FeatureImpl(value,type,ff.featureId(id));
	}
	
    public SimpleFeature createSimpleFeature(Object[] array,
            SimpleFeatureType type, String id) {
        if( type.isAbstract() ){
            throw new IllegalArgumentException("Cannot create an feature of an abstract FeatureType "+type.getTypeName());
        }
        return new SimpleFeatureImpl(array, type, ff.featureId(id), validating);
    }

    public SimpleFeature createSimpleFeautre(Object[] array,
            AttributeDescriptor descriptor, String id) {
        return createSimpleFeature( array, (SimpleFeatureType) descriptor, id );
    }
   
}
