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
package org.geotools.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsNull;

/**
 * This class contains utility methods focused on the schema represented by
 * the FeatureType data model.
 * <p>
 * These methods are often used for implementation the convience methods
 * such as FeatureType.getAttributeCount(), although they may be used directly
 * with any FeatureType.
 * </p>
 * <p>
 * These schema methods are based on the *complete* picture indicated by a FeatureType
 * and its ancestors. Many of these methods are focused on the derivation of AttribtueTypes
 * during an override.
 * </p>
 * @see FeatureTypes
 * @see FeatureType
 * @author Jody Garnett
 * @since 2.1.0
 * @deprecated This helper class was for the old feature model; please use FeatureTypes
 * @source $URL$
 */
public class Schema {
    private static Schema DEFAULT = new Schema();
    private FilterFactory ff;
    
    public Schema(){
        this( (Hints) null );
    }
    public Schema( Hints hints ){
        this( CommonFactoryFinder.getFilterFactory( hints ));
    }
    public Schema( FilterFactory filterFactory ){
        ff = filterFactory;
    }

    /**
     * Walk the provided FeatureType and produce a count of distinct attribtues.
     * <p>
     * used to detect duplicate attributes names (ie override)
     * </p>
     *  
     * @param featureType
     */
    public int getAttributeCount( SimpleFeatureType featureType ) {
        return getNames( featureType ).size();
    }
    
    /**
     * Does a quick walk to detect only a list of attribute names.
     * <p>
     * This method does not produce the complete schema (ie derrived restrictions based
     * on attribute facets). It is only used to get a list of the unique attribtues in
     * the resulting schema.
     * </p>
     * @param featureType
     * 
     * @return Set of unique attribute names
     */
    public List getNames( SimpleFeatureType featureType ) {
        return getNames( featureType, new ArrayList() );        
    }
    
        
    /**
     * This order is to be respected, based on Ancestors and so on.
     * <p>
     * This method is "faster" then actually constructing the merged
     * AttribtueTypes.
     * </p>
     */
    public List getNames( SimpleFeatureType featureType, List names ){
        if( featureType == null || featureType.getAttributeDescriptors() == null ){
            return names;
        }
        List ancestors = FeatureTypes.getAncestors(featureType);
        if( ancestors != null && !ancestors.isEmpty() ){
            for( int i=0, length = ancestors.size(); i<length; i++ ){
                SimpleFeatureType superType = (SimpleFeatureType) ancestors.get(i);
                getNames( superType, names );	           
	        }
        }
        List attributes = featureType.getAttributeDescriptors();
        if( attributes != null && !attributes.isEmpty() ){
            for( int i=0, length = attributes.size(); i<length; i++ ){
                AttributeDescriptor type = (AttributeDescriptor) attributes.get(i);
	            String name = type.getLocalName();
	            if( !names.contains( name )){
	                names.add( name );
	            }
	        }
        }
        return names;
    }

    public List getAttributes( SimpleFeatureType featureType ){
        return getAttributes( featureType, new ArrayList() );                       
    }
    
    
    /**
     * This order is to be respected, based on Ancestors and so on.
     * <p>
     * This method is "faster" then actually constructing the merged
     * AttribtueTypes.
     * </p>
     */
    public List getAttributes( SimpleFeatureType featureType, List list ){
        if( featureType == null || featureType.getAttributeDescriptors() == null ) {
            return list;
        }

        List ancestors = FeatureTypes.getAncestors(featureType);
        if( ancestors != null && !ancestors.isEmpty()){
            for( int i=0, length = ancestors.size(); i<length; i++ ){
                //eatureType type = ancestors[i];
                getAttributes( (SimpleFeatureType) ancestors.get(i), list );	           
	        }
        }
        List attributes = featureType.getAttributeDescriptors();
        if( attributes != null && !attributes.isEmpty() ){
            for( int i=0, length = attributes.size(); i<length; i++ ){
                AttributeDescriptor type = (AttributeDescriptor) attributes.get(i);
	            String name = type.getLocalName();
	            int index = getIndexOf( list, name );
	            if( index != -1 ){
	            	AttributeDescriptor origional = (AttributeDescriptor) list.get( index );
	            	list.remove( index );
	            	list.add( index, override( origional, type ));
	            }
	            else {
	            	list.add( type );
	            }
	        }
        }
        return list;
    }
    /**
     * Query featureType information the complete restrictions for the indicated name.
     * 
     * @param featureType
     * @param name
     */
    public Filter getRestrictions( SimpleFeatureType featureType, String name ){
        if( featureType == null || featureType.getAttributeDescriptors() == null ) return Filter.EXCLUDE;
        
        List restrictions = restriction( featureType, name, Collections.singletonList(Filter.INCLUDE) );
        return (Filter) restrictions.get(0);
    }
    
    /**
     * Lookup can only really be by name.
     * 
     * @param type
     */
    public int getIndexOf( SimpleFeatureType type, String name ) {
        List names = getNames( type );
        return names.indexOf( name );
    }
    
    /**
     * Look up based on name in the provided position.
     * 
     * @param type  the FeatureType
     * @param index the position
     * 
     */
    public AttributeDescriptor getAttribute( SimpleFeatureType type, int index ) {
        String name = (String) getNames( type ).get( index );
        return getXPath( type, name );
    }
    
    public AttributeDescriptor getAttribute( SimpleFeatureType type, String name ){
    	List list = getAttributes( type );
    	int index = getIndexOf( list, name );
    	if( index == -1 ) return null;
    	return (AttributeDescriptor) list.get( index );
    }

    /**
     * Look up based on name in the provided position.
     * <p>
     * AttributeType needs a xpath based access
     * </p>
     * @param type
     * @param xpath
     * 
     */
    public AttributeDescriptor getXPath( SimpleFeatureType type, String xpath) {
        return getAttribute( type, xpath ); // for now, use JXPath later
    }
    
    // Utility Methods
    //
    private int getIndexOf( List attributes, String name ){
        int index = 0;
        for( Iterator i=attributes.iterator(); i.hasNext(); index++){
            AttributeDescriptor type = (AttributeDescriptor) i.next();
            if( name.equals( type.getLocalName() )) return index;
        }
        return -1;
    }
    
    private AttributeDescriptor override(AttributeDescriptor type, AttributeDescriptor override ){
        int max = override.getMaxOccurs();
        if( max < 0 ) max = type.getMinOccurs();
        
        int min = override.getMinOccurs();
        if( min < 0 ) min = type.getMinOccurs();
        
        String name = override.getLocalName();
        if( name == null ) name = type.getLocalName();
        
        List restrictions = override( type.getType().getRestrictions(), override.getType().getRestrictions() );
        
        Class javaType = override.getType().getBinding();
        if( javaType == null ) javaType = type.getType().getBinding();
        
        boolean isNilable = override.isNillable();
        
        Object defaultValue = override.getDefaultValue();
        if( defaultValue == null ) defaultValue = type.getDefaultValue();
        
        // WARNING cannot copy metadata!        
        return new AttributeDescriptorImpl(
            new AttributeTypeImpl( new NameImpl( name ), javaType, false, false, restrictions, null, null ), 
            new NameImpl( name ), min, max, isNilable, defaultValue
        );
    }
    
    private List restriction( SimpleFeatureType featureType, String name, List filters ){
        List ancestors = FeatureTypes.getAncestors(featureType);
        if( ancestors != null && !ancestors.isEmpty()){
            for( int i=0, length = ancestors.size(); i<length; i++ ){
                SimpleFeatureType superType = (SimpleFeatureType) ancestors.get(i);
                filters = restriction( superType, name, filters );                              
            }
        }
        List attributes = featureType.getAttributeDescriptors();
        if( attributes != null && !attributes.isEmpty()){
            for( int i=0, length = attributes.size(); i<length; i++ ){
                AttributeDescriptor type = (AttributeDescriptor) attributes.get(i);
                if( name.equals( type.getLocalName() )){
                    filters = override( filters, type.getType().getRestrictions() );                 
                }
            }
        }
        return filters;
    }
    
    private List override ( List filters, List overrides ){
        if ( filters.size() != overrides.size() ) {
            throw new IllegalArgumentException( "filters not same size");
        }
        
        List result = new ArrayList();
        for ( int i = 0; i < filters.size(); i++ ) {
            Filter f = override( (Filter) filters.get(i), (Filter) overrides.get(i) );
        }
        
        return result;
    }
    
    private Filter override( Filter filter, Filter override ){
        if( isNOP( override )){
            // no override is needed
            return filter;
        }
        else if( isNOP( filter )){
            return override;
        }
        else {            
            return ff.and( filter, override );
        }
    }
    
    private boolean isNOP( Filter filter ){
        return filter == null || filter instanceof PropertyIsNull || filter == Filter.INCLUDE;
    }
    
    // Utiltie Methods
    // (make use of DEFAULT Schema)
    //
    
    /**
     * Walk the provided FeatureType and produce a count of distinct attribtues.
     * <p>
     * used to detect duplicate attributes names (ie override)
     * </p>
     *  
     * @param featureType
     */
    public static int attributeCount( SimpleFeatureType featureType ){
        return DEFAULT.getAttributeCount(featureType);
    }    
    
    /**
     * @deprecated use getAttribute( type, index )
     */
    public static AttributeDescriptor attribute( SimpleFeatureType type, int index ) {
        return DEFAULT.getAttribute(type, index);
    }
    /** @deprecated use getAttribute( type, name ) */
    public static AttributeDescriptor attribute( SimpleFeatureType type, String name ){
        return DEFAULT.getAttribute(type, name );
    }
    /** @deprecated use getAttributes( featureType ) */
    public static List attributes( SimpleFeatureType featureType ){
        return DEFAULT.getAttributes(featureType);
    }    
    /** @deprecated use getAttributes( featureType, list ) */
    public static List attributes( SimpleFeatureType featureType, List list ){
        return DEFAULT.getAttributes(featureType, list);
    }
     
    /**
     * @deprecated please use getIndexOf( type, name )
     */
    public static int find( SimpleFeatureType type, String name ) {
        return DEFAULT.getIndexOf(type, name);
    }
    
    
    /**
     * @deprecated use getNames( featureType )
     */
    public static List names( SimpleFeatureType featureType ) {
        return DEFAULT.getNames(featureType);
    }
    
    /**
     * @deprecated use getNames( featureType, List )
     */
    public static List names( SimpleFeatureType featureType, List names ){
        return DEFAULT.getNames( featureType, names );
    }
    
    /**
     * @deprecated please use getRestriction( featureType, name )
     */
    public static Filter restriction( SimpleFeatureType featureType, String name ){
        return DEFAULT.getRestrictions(featureType, name);
    }
    
    /**
     * @deprecated use getXPath( type, xpath );
     */
    public static AttributeDescriptor xpath( SimpleFeatureType type, String xpath) {
        return DEFAULT.getAttribute( type, xpath ); // for now, use JXPath later
    }
}
