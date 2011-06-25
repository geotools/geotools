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
package org.geotools.feature.simple;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A builder for simple feature types.
 * <p>
 * Simple Usage:
 * <pre>
 *  <code>
 *  //create the builder
 *  SimpleTypeBuilder builder = new SimpleTypeBuilder();
 *  
 *  //set global state
 *  builder.setName( "testType" );
 *  builder.setNamespaceURI( "http://www.geotools.org/" );
 *  builder.setSRS( "EPSG:4326" );
 *  
 *  //add attributes
 *  builder.add( "intProperty", Integer.class );
 *  builder.add( "stringProperty", String.class );
 *  builder.add( "pointProperty", Point.class );
 *  
 *  //add attribute setting per attribute state
 *  builder.minOccurs(0).maxOccurs(2).nillable(false).add("doubleProperty",Double.class);
 *  
 *  //build the type
 *  SimpleFeatureType featureType = builder.buildFeatureType();
 *  </code>
 * </pre>
 * </p>
 * This builder builds type by maintaining state. Two types of state are maintained:
 * <i>Global Type State</i> and <i>Per Attribute State</i>. Methods which set
 * global state are named <code>set&lt;property>()</code>. Methods which set per attribute 
 * state are named <code>&lt;property>()</code>. Furthermore calls to per attribute 
 * </p>
 * <p>
 * Global state is reset after a call to {@link #buildFeatureType()}. Per 
 * attribute state is reset after a call to {@link #add}.
 * </p>
 * <p>
 * A default geometry for the feature type can be specified explictly via 
 * {@link #setDefaultGeometry(String)}. However if one is not set the first
 * geometric attribute ({@link GeometryType}) added will be resulting default.
 * So if only specifying a single geometry for the type there is no need to 
 * call the method. However if specifying multiple geometries then it is good
 * practice to specify the name of the default geometry type. For instance:
 * <code>
 * 	<pre>
 *  builder.add( "pointProperty", Point.class );
 *  builder.add( "lineProperty", LineString.class );
 *  builder.add( "polygonProperty", Polygon.class );
 *  
 *  builder.setDefaultGeometry( "lineProperty" );
 * 	</pre>
 * </code>
 * </p>
 * 
 * @author Justin Deolivera
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class SimpleFeatureTypeBuilder {
	/**
	 * factories
	 */
	protected FeatureTypeFactory factory;

	/**
	 * Map of java class bound to properties types.
	 */
	protected Map/* <Class,AttributeType> */bindings;
	
	// Global state for the feature type
	//
	/**
	 * Naming: local name
	 */
	protected String local;

	/**
	 * Naming: uri indicating scope
	 */
	protected String uri;

	/**
	 * Description of type.
	 */
	protected InternationalString description;

	/**
	 * List of attributes.
	 */
	protected List<AttributeDescriptor> attributes;

	/**
	 * Additional restrictions on the type.
	 */
	protected List<Filter> restrictions;

	/** 
	 * Name of the default geometry to use 
	 */
	protected String defaultGeometry;

	/** 
	 * coordinate reference system of the type 
	 */
	protected CoordinateReferenceSystem crs;

	/**
	 * flag controlling if the type is abstract.
	 */
	protected boolean isAbstract = false;
	
	/**
	 * the parent type.
	 */
	protected SimpleFeatureType superType;
	
	/**
	 * attribute builder
	 */
	protected AttributeTypeBuilder attributeBuilder;

	/** Length for next filter */
    private int length = -1;
	
	/**
	 * Constructs the builder.
	 */
	public SimpleFeatureTypeBuilder() {
		this( new FeatureTypeFactoryImpl() );
	}
	
	/**
	 * Constructs the builder specifying the factory for creating feature and 
	 * feature collection types.
	 */
	public SimpleFeatureTypeBuilder(FeatureTypeFactory factory) {
		this.factory = factory;
		
		attributeBuilder = new AttributeTypeBuilder();
		setBindings( new SimpleSchema() );
		reset();
	}
	
	// Dependency Injection
	//
	/**
	 * Sets the factory used to create feature and feature collection types.
	 */
	public void setFeatureTypeFactory(FeatureTypeFactory factory) {
		this.factory = factory;
	}
	/**
	 * The factory used to create feature and feature collection types.
	 */
	public FeatureTypeFactory getFeatureTypeFactory() {
		return factory;
	}
	
	// Builder methods
	//
	/**
	 * Initializes the builder with state from a pre-existing feature type.
	 */
	public void init(SimpleFeatureType type) {
		init();
		if (type == null)
			return;

		uri = type.getName().getNamespaceURI();
		local = type.getName().getLocalPart();
		description = type.getDescription();
		restrictions = null;
		restrictions().addAll(type.getRestrictions());

		attributes = null;
		attributes().addAll(type.getAttributeDescriptors());
		
		isAbstract = type.isAbstract();
		superType = (SimpleFeatureType) type.getSuper();
	}

	/**
	 * Clears the running list of attributes. 
	 */
	protected void init() {
		attributes = null;
	}
	
	/**
	 * Completely resets all builder state.
	 *
	 */
	protected void reset() {
		uri = BasicFeatureTypes.DEFAULT_NAMESPACE;
		local = null;
		description = null;
		restrictions = null;
		attributes = null;
		crs = null;
		isAbstract = false;
		superType = BasicFeatureTypes.FEATURE;
	}
	
	/**
	 * Set the namespace uri of the built type.
	 */
	public void setNamespaceURI(String namespaceURI) {
		this.uri = namespaceURI;
	}
	public void setNamespaceURI(URI namespaceURI) {
	    if ( namespaceURI != null ) {
	        setNamespaceURI( namespaceURI.toString() );
	    }
	    else {
	        setNamespaceURI( (String) null );
	    }
	}
	/**
	 * The namespace uri of the built type.
	 */
	public String getNamespaceURI() {
		return uri;
	}
	/**
	 * Sets the name of the built type.
	 */
	public void setName(String name) {
		this.local = name;
	}
	/**
	 * The name of the built type.
	 */
	public String getName() {
		return local;
	}
	
	/**
	 * Sets the local name and namespace uri of the built type.
	 */
	public void setName(Name name) {
	    setName( name.getLocalPart() );
	    setNamespaceURI( name.getNamespaceURI() );
	}
	
	/**
	 * Sets the description of the built type.
	 */
	public void setDescription(InternationalString description) {
		this.description = description;
	}
	/**
	 * The description of the built type.
	 */
	public InternationalString getDescription() {
		return description;
	}
	
	/**
	 * Sets the name of the default geometry attribute of the built type.
	 */
	public void setDefaultGeometry(String defaultGeometryName) {
		this.defaultGeometry = defaultGeometryName;
	}
	/**
	 * The name of the default geometry attribute of the built type.
	 */
	public String getDefaultGeometry() {
		return defaultGeometry;
	}
	
	/**
	 * Sets the coordinate reference system of the built type.
         * The supplied coordinate reference system is only used if 
         * geometric attributes are later added to the type without
         * specifying their coordinate reference system.
	 */
	public void setCRS(CoordinateReferenceSystem crs) {
		this.crs = crs;
	}
	/**
	 * The fallback coordinate reference system that will be applied to
         * any geometric attributes added to the type without their own
         * coordinate reference system specified.
	 */
	public CoordinateReferenceSystem getCRS() {
		return crs;
	}
	
	/**
	 * Sets the coordinate reference system of the built type by specifying its
	 * srs.
	 * 
	 * @throws IllegalArgumentException When the srs specified can be decored 
	 * into a crs.
	 * 
	 */
	public void setSRS(String srs) {
		setCRS(decode(srs));	
	}
	
	/**
	 * Sets the flag controlling if the resulting type is abstract.
	 */
	public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }
	
	/**
	 * The flag controlling if the resulting type is abstract.
	 */
	public boolean isAbstract() {
        return isAbstract;
    }
	
	/**
	 * Sets the super type of the built type.
	 */
	public void setSuperType(SimpleFeatureType superType) {
        this.superType = superType;
    }
	/**
	 * The super type of the built type.
	 */
	public SimpleFeatureType getSuperType() {
        return superType;
    }
	
	
	/**
	 * Specifies an attribute type binding.
	 * <p>
	 * This method is used to associate an attribute type with a java class. 
	 * The class is retreived from <code>type.getBinding()</code>. When the
	 * {@link #add(String, Class)} method is used to add an attribute to the 
	 * type being built, this binding is used to locate the attribute type.
	 * </p>
	 * 
	 * @param type The attribute type.
	 */
	public void addBinding(AttributeType type) {
		bindings().put(type.getBinding(), type);
	}
	
	/**
	 * Specifies a number of attribute type bindings.
	 * 
	 * @param schema The schema containing the attribute types.
	 * 
	 * @see {@link #addBinding(AttributeType)}.
	 */
	public void addBindings( Schema schema ) {
		for (Iterator itr = schema.values().iterator(); itr.hasNext();) {
			AttributeType type = (AttributeType) itr.next();
			addBinding(type);
		}
	}
	
	/**
	 * Specifies a number of attribute type bindings clearing out all existing
	 * bindings.
	 * 
	 * @param schema The schema contianing attribute types.
	 * 
	 * @see {@link #addBinding(AttributeType)}.
	 */
	public void setBindings( Schema schema ) {
		bindings().clear();
		addBindings( schema );
	}
	
	/**
	 * Looks up an attribute type which has been bound to a class.
	 * 
	 * @param binding The class.
	 * 
	 * @return AttributeType The bound attribute type.
	 */
	public AttributeType getBinding(Class<?> binding) {
		return (AttributeType) bindings().get(binding);
	}
	
	// per attribute methods
	//
	/**
	 * Sets the minOccurs of the next attribute added to the feature type.
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 */
	public SimpleFeatureTypeBuilder minOccurs( int minOccurs ) {
		attributeBuilder.setMinOccurs(minOccurs);
		return this;
	}
	/**
	 * Sets the maxOccurs of the next attribute added to the feature type.
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 */
	public SimpleFeatureTypeBuilder maxOccurs( int maxOccurs ) {
		attributeBuilder.setMaxOccurs(maxOccurs);
		return this;
	}
	/**
	 * Sets the nullability of the next attribute added to the feature type.
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 */
	public SimpleFeatureTypeBuilder nillable( boolean isNillable ) {
		attributeBuilder.setNillable(isNillable);
		return this;
	}

	/**
     * Sets a restriction on the field length of the next attribute added to the feature type.
     * <p>
     * This method is the same as adding a restriction based on length( value ) < length
     * This value is reset after a call to {@link #add(String, Class)}
     * </p>
     * @return length Used to limit the length of the next attribute created
     */
    public SimpleFeatureTypeBuilder length( int length) {
        attributeBuilder.setLength(length);
        return this;
    }
    
    /**
	 * Adds a restriction to the next attribute added to the feature type.
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 */
	public SimpleFeatureTypeBuilder restriction( Filter filter ) {
		attributeBuilder.addRestriction( filter );
		return this;
	}
	/**
	 * Adds a collection of restrictions to the next attribute added to the 
	 * <p>
     * This value is reset after a call to {@link #add(String, Class)}
     * </p>
	 */
	public SimpleFeatureTypeBuilder restrictions( List<Filter> filters ) {
	    for ( Filter f : filters ) {
	        attributeBuilder.addRestriction(f);
	    }
	    return this;
	}
	
	/**
	 * Sets the description of the next attribute added to the feature type.
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 */
	public SimpleFeatureTypeBuilder description( String description ) {
		attributeBuilder.setDescription( description );
		return this;
	}
	/**
	 * Sets the default value of the next attribute added to the feature type.
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 */
	public SimpleFeatureTypeBuilder defaultValue( Object defaultValue ) {
		attributeBuilder.setDefaultValue( defaultValue );
		return this;
	}
	/**
	 * Sets the crs of the next attribute added to the feature type.
	 * <p>
	 * This only applies if the attribute added is geometric.
	 * </p>
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 */
	public SimpleFeatureTypeBuilder crs( CoordinateReferenceSystem crs ) {
		attributeBuilder.setCRS(crs);
		return this;
	}
	
	/**
	 * Sets the srs of the next attribute added to the feature type.
	 * <p>
	 * The <tt>srs</tt> parameter is the id of a spatial reference system, for
	 * example: "epsg:4326".
	 * </p>
	 * <p>
	 * This only applies if the attribute added is geometric.
	 * </p>
	 * <p>
     * This value is reset after a call to {@link #add(String, Class)}
     * </p>
     * 
     * @param srs The spatial reference system.
	 */
	public SimpleFeatureTypeBuilder srs( String srs ) {
	    if ( srs == null ) {
	        return crs( null );
	    }
	    
	    return crs(decode(srs));
	}
	
	/**
	 * Sets the srid of the next attribute added to the feature type.
	 * <p>
     * The <tt>srid</tt> parameter is the epsg code of a spatial reference 
     * system, for example: "4326".
     * </p>
	 * <p>
     * This only applies if the attribute added is geometric.
     * </p>
     * <p>
     * This value is reset after a call to {@link #add(String, Class)}
     * </p>
	 * 
	 * @param srid The id of a spatial reference system.
	 */
	public SimpleFeatureTypeBuilder srid( Integer srid ) {
	    if ( srid == null ) {
	        return crs( null );
	    }
	    
	    return crs( decode( "EPSG:" + srid ) );
	}
	
	/**
	 * Sets user data for the next attribute added to the feature type.
	 * <p>
	 * This value is reset after a call to {@link #add(String, Class)}
	 * </p>
	 * @param key  The key of the user data.
	 * @param value The value of the user data.
	 */
	public SimpleFeatureTypeBuilder userData( Object key, Object value ) {
	    attributeBuilder.addUserData( key, value );
	    return this;
	}
	
	/**
	 * Sets all the attribute specific state from a single descriptor.
	 * <p>
	 * This method is convenience for:
	 * <code>
	 * builder.minOccurs( descriptor.getMinOccurs() ).maxOccurs( descriptor.getMaxOccurs() )
	 *     .nillable( descriptor.isNillable() )...
	 * </code>
	 * </p>
	 */
	public SimpleFeatureTypeBuilder descriptor( AttributeDescriptor descriptor ) {
	    minOccurs( descriptor.getMinOccurs() );
	    maxOccurs( descriptor.getMaxOccurs() );
	    nillable( descriptor.isNillable() );
	    //namespaceURI( descriptor.getName().getNamespaceURI() );
	    defaultValue( descriptor.getDefaultValue() );
	    
	    if ( descriptor instanceof GeometryDescriptor ) {
	        crs( ( (GeometryDescriptor) descriptor).getCoordinateReferenceSystem() );
	    }
	    
	    return this;
	}
	
	/**
	 * Adds a new attribute w/ provided name and class.
	 * 
	 * <p>
	 * The provided class is used to locate an attribute type binding previously 
	 * specified by {@link #addBinding(AttributeType)},{@link #addBindings(Schema)}, 
	 * or {@link #setBindings(Schema)}. 
	 * </p>
	 * <p>
	 * If not such binding exists then an attribute type is created on the fly.
	 * </p>
	 * @param name The name of the attribute.
	 * @param bind The class the attribute is bound to.
	 * 
	 */
	public void add(String name, Class binding) {

	    AttributeDescriptor descriptor = null;
	    
	    attributeBuilder.setBinding(binding);
        attributeBuilder.setName(name);
        
		//check if this is the name of the default geomtry, in that case we 
		// better make it a geometry type
		//also check for jts geometry, if we ever actually get to a point where a
        // feature can be backed by another geometry model (like iso), we need 
        // to remove this check
        //
        if ( ( defaultGeometry != null && defaultGeometry.equals( name ) ) 
            || Geometry.class.isAssignableFrom(binding) ) {
		
            //if no crs was set, set to the global
            if ( !attributeBuilder.isCRSSet() ) {
                attributeBuilder.setCRS(crs);
            }
            
            GeometryType type = attributeBuilder.buildGeometryType();
            descriptor = attributeBuilder.buildDescriptor(name, type);
		}
        else {
            AttributeType type = attributeBuilder.buildType();
            descriptor = attributeBuilder.buildDescriptor(name, type );
        }
		
        
		attributes().add(descriptor);
	}
	
	/**
	 * Adds a descriptor directly to the builder.
	 * <p>
	 * Use of this method is discouraged. Consider using {@link #add(String, Class)}. 
	 * </p>
	 */
	public void add( AttributeDescriptor descriptor ) {
	    attributes().add(descriptor);
	}
	
	/**
     * Removes an attribute from the builder
     * 
     * @param attributeName the name of the AttributeDescriptor to remove
     * 
     * @return the AttributeDescriptor with the name attributeName
     * @throws IllegalArgumentException if there is no AttributeDescriptor with the name attributeName
     */
    public AttributeDescriptor remove(String attributeName){
        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
            AttributeDescriptor descriptor = (AttributeDescriptor) iterator.next();
            if( descriptor.getLocalName().equals(attributeName) ){
                iterator.remove();
                return descriptor;
            }
        }
        throw new IllegalArgumentException(attributeName+" is not an existing attribute descriptor in this builder");
    }

	/**
	 * Adds a descriptor to the builder by index.
	 * <p>
	 * Use of this method is discouraged. Consider using {@link #add(String, Class)}. 
	 * </p>
	 */
	public void add( int index, AttributeDescriptor descriptor ) {
	    attributes().add(index, descriptor);
	}

	
	
    /**
     * Adds a list of descriptors directly to the builder.
     * <p>
     * Use of this method is discouraged. Consider using {@link #add(String, Class)}.
     * </p>
     */
    public void addAll( List<AttributeDescriptor>  descriptors ) {
        if(descriptors != null)
            for ( AttributeDescriptor ad : descriptors ) {
                add( ad );
            }
    }
	/**
     * Adds an array of descriptors directly to the builder.
     * <p>
     * Use of this method is discouraged. Consider using {@link #add(String, Class)}.
     * </p>
     */
	public void addAll( AttributeDescriptor[] descriptors ) {
            if (descriptors != null) {
	        for ( AttributeDescriptor ad : descriptors ) {
	            add( ad );
                }
	    }
	}
	
	/**
	 * Adds a new geometric attribute w/ provided name, class, and coordinate 
	 * reference system.
	 * <p>
	 * The <tt>crs</tt> parameter may be <code>null</code>.
	 * </p>
	 * @param name The name of the attribute.
	 * @param binding The class that the attribute is bound to.
	 * @param crs The crs of of the geometry, may be <code>null</code>.
	 */
	public void add(String name, Class binding, CoordinateReferenceSystem crs ) {
		attributeBuilder.setBinding(binding);
		attributeBuilder.setName(name);
		attributeBuilder.setCRS(crs);
		
		GeometryType type = attributeBuilder.buildGeometryType();
		GeometryDescriptor descriptor = attributeBuilder.buildDescriptor(name,type);
		attributes().add(descriptor);
	}
	
	/**
     * Adds a new geometric attribute w/ provided name, class, and spatial 
     * reference system identifier
     * <p>
     * The <tt>srs</tt> parameter may be <code>null</code>.
     * </p>
     * @param name The name of the attribute.
     * @param binding The class that the attribute is bound to.
     * @param srs The srs of of the geometry, may be <code>null</code>.
     */
	public void add(String name, Class binding, String srs) {
	    if ( srs == null ) {
	        add(name,binding,(CoordinateReferenceSystem)null);
	        return;
	    }
	
	    add(name,binding,decode(srs));
	}
	
	/**
     * Adds a new geometric attribute w/ provided name, class, and spatial 
     * reference system identifier
     * <p>
     * The <tt>srid</tt> parameter may be <code>null</code>.
     * </p>
     * @param name The name of the attribute.
     * @param binding The class that the attribute is bound to.
     * @param srid The srid of of the geometry, may be <code>null</code>.
     */
	public void add(String name, Class binding, Integer srid) {
	    if ( srid == null ) {
	        add(name,binding,(CoordinateReferenceSystem)null);
	        return;
	    }
	    
	    add( name, binding, decode( "EPSG:" + srid ) );
	}
	
	/**
	 * Directly sets the list of attributes. 
	 * @param attributes the new list of attributes, or null to reset the list
	 */
	public void setAttributes(List<AttributeDescriptor> attributes) {
		List<AttributeDescriptor> atts = attributes();
		atts.clear();
		if(attributes != null)
			atts.addAll(attributes);
	}
	
	/**
	 * Directly sets the list of attributes. 
	 * @param attributes the new list of attributes, or null to reset the list
	 */
	public void setAttributes(AttributeDescriptor[] attributes) {
		List<AttributeDescriptor> atts = attributes();
		atts.clear();
		if(attributes != null)
			atts.addAll(Arrays.asList(attributes));
	}
	
	
	/**
	 * Builds a feature type from compiled state.
	 * <p>
	 * After the type is built the running list of attributes is cleared.
	 * </p>
	 * @return The built feature type.
	 */
	public SimpleFeatureType buildFeatureType() {
	    GeometryDescriptor defGeom = null;
		
		//was a default geometry set?
		if ( this.defaultGeometry != null ) {
			List<AttributeDescriptor> atts = attributes();
			for ( int i = 0; i < atts.size(); i++) {
				AttributeDescriptor att = atts.get(i);
				if ( this.defaultGeometry.equals( att.getName().getLocalPart() ) ) {
					//ensure the attribute is a geometry attribute
					if ( !(att instanceof GeometryDescriptor ) ) {
						attributeBuilder.init( att );
						attributeBuilder.setCRS(crs);
						GeometryType type = attributeBuilder.buildGeometryType();						
						att = attributeBuilder.buildDescriptor(att.getName(),type);
						atts.set( i, att );
					}
					defGeom = (GeometryDescriptor)att;
					break;
				}
			}
			
			if (defGeom == null) {
			    String msg = "'" + this.defaultGeometry + " specified as default" +
		    		" but could find no such attribute.";
			    throw new IllegalArgumentException( msg );
			}
		}
		
		if ( defGeom == null ) {
			//none was set by name, look for first geometric type
			for ( AttributeDescriptor att : attributes() ) {
				if ( att instanceof GeometryDescriptor ) {
					defGeom = (GeometryDescriptor) att;
					break;
				}
			}
		}
		
		SimpleFeatureType built = factory.createSimpleFeatureType(
			name(), attributes(), defGeom, isAbstract,
			restrictions(), superType, description);
		
		init();
		return built;
	}
	
	// Internal api available for subclasses to override
	/**
	 * Creates a new set instance, this default implementation returns {@link HashSet}.
	 */
	protected Set newSet(){
		return new HashSet();
	}
	/**
	 * Creates a new list instance, this default impelementation returns {@link ArrayList}.
	 */
	protected List newList() {
		return new ArrayList();
	}
	
	/**
	 * Creates a new map instance, this default implementation returns {@link HashMap}
	 */
	protected Map newMap() {
		return new HashMap();
	}
	
	/**
	 * Creates a new list which is the same type as the provided list.
	 * <p>
	 * If the new copy can not be created reflectively.. {@link #newList()} is 
	 * returned.
	 * </p>
	 */
	protected List newList(List origional) {
		if (origional == null) {
			return newList();
		}
		if (origional == Collections.EMPTY_LIST) {
			return newList();
		}
		try {
			return (List) origional.getClass().newInstance();
		} catch (InstantiationException e) {
			return newList();
		} catch (IllegalAccessException e) {
			return newList();
		}
	}
	
	// Helper methods, 
	//
	/**
	 * Naming: Accessor which returns type name as follows:
	 * <ol>
	 * <li>If <code>typeName</code> has been set, its value is returned.
	 * <li>If <code>name</code> has been set, it + <code>namespaceURI</code>
	 * are returned.
	 * </ol>
	 * 
	 */
	protected Name name() {
		if (local == null)
			return null;
		
		return new NameImpl(uri, local);
	}

	/**
	 * Accessor for attributes.
	 */
	protected List<AttributeDescriptor> attributes() {
		if (attributes == null) {
			attributes = newList();
		}
		return attributes;
	}
	/**
	 * Accessor for restrictions.
	 */
	protected List<Filter> restrictions(){
		if (restrictions == null) {
			restrictions = newList();
		}
		return restrictions;		
	}
	/**
	 * Accessor for bindings.
	 */
	protected Map bindings() {
		if (bindings == null) {
			bindings = newMap();
		}
		return bindings;
	}
	
	/**
	 * Decodes a srs, supplying a useful error message if there is a problem.
	 */
	protected CoordinateReferenceSystem decode( String srs ) {
	    try {
            return CRS.decode(srs);
        } catch (Exception  e) {
            String msg = "SRS '" + srs + "' unknown:" + e.getLocalizedMessage(); 
            throw (IllegalArgumentException) new IllegalArgumentException( msg ).initCause( e );
        }
	}
	
	/**
	 * Create a SimpleFeatureType containing just the descriptors indicated.
	 * @param original SimpleFeatureType
	 * @param types name of types to include in result
	 * @return SimpleFeatureType containing just the types indicated by name
	 */
	public static SimpleFeatureType retype( SimpleFeatureType original, String[] types ) {
	    SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
	    
	    //initialize the builder
	    b.init( original );
	    
	    //clear the attributes
	    b.attributes().clear();
	    
	    //add attributes in order
	    for ( int i = 0; i < types.length; i++ ) {
	        b.add( original.getDescriptor( types[i] ) );
	    }
	    
	    return b.buildFeatureType();
	}
	
	/**
         * Create a SimpleFeatureType with the same content; just updating the geometry
         * attribute to match the provided coordinate reference system.
         * @param original SimpleFeatureType
         * @param crs CoordianteReferenceSystem of result
         * @return SimpleFeatureType updated with the provided CoordinateReferenceSystem
         */
        public static SimpleFeatureType retype( SimpleFeatureType original,CoordinateReferenceSystem crs ) {
            SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
            
            //initialize the builder
            b.init( original );
            
            //clear the attributes
            b.attributes().clear();
            
            //add attributes in order
            for( AttributeDescriptor descriptor : original.getAttributeDescriptors() ){
                if( descriptor instanceof GeometryDescriptor ){
                    GeometryDescriptor geometryDescriptor = (GeometryDescriptor) descriptor;
                    AttributeTypeBuilder adjust = new AttributeTypeBuilder( b.factory );
                    adjust.init( geometryDescriptor );
                    adjust.setCRS( crs );
                    b.add( adjust.buildDescriptor( geometryDescriptor.getLocalName() ));
                    continue;
                }
                b.add( descriptor);
            }            
            return b.buildFeatureType();
        }
}
