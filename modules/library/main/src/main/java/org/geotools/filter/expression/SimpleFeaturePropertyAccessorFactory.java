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
package org.geotools.filter.expression;

import java.util.regex.Pattern;

import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Creates a property accessor for simple features.
 * <p>
 * The created accessor handles a small subset of xpath expressions, a
 * non-nested "name" which corresponds to a feature attribute, and "@id",
 * corresponding to the feature id.
 * </p>
 * <p>
 * THe property accessor may be run against {@link SimpleFeature}, or 
 * against {@link SimpleFeature}. In the former case the feature property 
 * value is returned, in the latter the feature property type is returned. 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 *
 * @source $URL$
 */
public class SimpleFeaturePropertyAccessorFactory implements
        PropertyAccessorFactory {

    /** Single instnace is fine - we are not stateful */
    static PropertyAccessor ATTRIBUTE_ACCESS = new SimpleFeaturePropertyAccessor();
    static PropertyAccessor DEFAULT_GEOMETRY_ACCESS = new DefaultGeometrySimpleFeaturePropertyAccessor();
    static PropertyAccessor FID_ACCESS = new FidSimpleFeaturePropertyAccessor();
    static Pattern idPattern = Pattern.compile("@(\\w+:)?id");
    static Pattern propertyPattern = Pattern.compile("(\\w+:)?(\\w+)");

    public PropertyAccessor createPropertyAccessor(Class type, String xpath,
            Class target, Hints hints) {

    	if ( xpath == null ) 
    		return null;
    	
        if (!SimpleFeature.class.isAssignableFrom(type) && !SimpleFeatureType.class.isAssignableFrom(type))
            return null; // we only work with simple feature

        //if ("".equals(xpath) && target == Geometry.class)
        if ("".equals(xpath))
            return DEFAULT_GEOMETRY_ACCESS;

        //check for fid access
        if (idPattern.matcher(xpath).matches())
            return FID_ACCESS;

        //check for simple property acess
        if (propertyPattern.matcher(xpath).matches()) {
        	return ATTRIBUTE_ACCESS;	
        }
        
        return null;
    }

    /**
     * We strip off namespace prefix, we need new feature model to do this
     * property
     * <ul>
     * <li>BEFORE: foo:bar
     * <li>AFTER: bar
     * </ul>
     * 
     * @param xpath
     * @return xpath with any XML prefixes removed
     */
    static String stripPrefix(String xpath) {
        int split = xpath.indexOf(":");
        if (split != -1) {
            return xpath.substring(split + 1);
        }
        return xpath;
    }

    /**
     * Access to SimpleFeature Identifier.
     * 
     * @author Jody Garnett, Refractions Research Inc.
     */
    static class FidSimpleFeaturePropertyAccessor implements PropertyAccessor {        
        public boolean canHandle(Object object, String xpath, Class target) {
        	//we only work against feature, not feature type
            return object instanceof SimpleFeature && xpath.matches("@(\\w+:)?id");
        }
        public Object get(Object object, String xpath, Class target) {
            SimpleFeature feature = (SimpleFeature) object;
            return feature.getID();
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {
            throw new IllegalAttributeException("feature id is immutable");            
        }
    }
    static class DefaultGeometrySimpleFeaturePropertyAccessor implements PropertyAccessor {
        
        public boolean canHandle(Object object, String xpath, Class target) {
        	if ( !"".equals( xpath ) )
        		return false;
        	
//        	if ( target != Geometry.class ) 
//        		return false;
        	
        	if ( !( object instanceof SimpleFeature || object instanceof SimpleFeatureType ) ) {
        		return false;
        	}
        	
        	return true;
            
        }
        public Object get(Object object, String xpath, Class target) {
        	if ( object instanceof SimpleFeature ) {
        	    SimpleFeature f = (SimpleFeature) object;
        	    Object defaultGeometry = f.getDefaultGeometry();

                    // not found? Ok, let's do a lookup then...
                    if ( defaultGeometry == null ) {
                        for ( Object o : f.getAttributes() ) {
                            if ( o instanceof Geometry ) {
                                defaultGeometry = o;
                                break;
                            }
                        }
                    }
                    
                    return defaultGeometry;
        	}
        	
                if ( object instanceof SimpleFeatureType ) {
                    SimpleFeatureType ft = (SimpleFeatureType) object;
                    GeometryDescriptor gd = ft.getGeometryDescriptor();
                
                    if ( gd == null ) {
                        //look for any geometry descriptor
                        for ( AttributeDescriptor ad : ft.getAttributeDescriptors() ) {
                            if ( Geometry.class.isAssignableFrom( ad.getType().getBinding() ) ) {
                                return ad;
                            }
                        }
                    }
                    
                    return gd;
                }
            
        	return null;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {
            
        	if ( object instanceof SimpleFeature ) {
        		((SimpleFeature) object).setDefaultGeometry( (Geometry) value );
        	}
        	if ( object instanceof SimpleFeatureType ) {
        		throw new IllegalAttributeException("feature type is immutable");
        	}
        	
        }
    }

    static class SimpleFeaturePropertyAccessor implements PropertyAccessor {
        public boolean canHandle(Object object, String xpath, Class target) {
        	xpath = stripPrefix(xpath);
        	
        	if ( object instanceof SimpleFeature ) {
        		return ((SimpleFeature) object).getType().getDescriptor(xpath) != null;
        	}
        	
        	if ( object instanceof SimpleFeatureType ) {
        		return ((SimpleFeatureType) object).getDescriptor( xpath ) != null;
        	}
        	
        	return false;
        }
        
        public Object get(Object object, String xpath, Class target) {
        	xpath = stripPrefix(xpath);
        	
        	if ( object instanceof SimpleFeature ) {
        		return ((SimpleFeature) object).getAttribute( xpath );
        	}
        	
        	if ( object instanceof SimpleFeatureType ) {
        		return ((SimpleFeatureType) object).getDescriptor( xpath );
        	}
        	
        	return null;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {
        	xpath = stripPrefix(xpath);
        	
        	if ( object instanceof SimpleFeature ) {
        		((SimpleFeature) object).setAttribute( xpath, value );
        	}
        	
        	if ( object instanceof SimpleFeatureType ) {
        		throw new IllegalAttributeException("feature type is immutable");    
        	}
        	
        }
    }

}
