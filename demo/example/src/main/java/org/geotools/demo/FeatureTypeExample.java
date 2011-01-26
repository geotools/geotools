/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo;

import org.opengis.feature.simple.SimpleFeature;

/**
 * You may of noticed that the use of DataUtilities.createType in the Csv2Shape
 * was a bit of a hack.
 * <p>
 * Here is why we only use the createType method in examples and test cases:
 * <ul>
 * <li>It did not let us specify the projection; you saw we needed to use
 * ShapefileDataStore explicity in order to call an extra method to "force" the
 * projection to to WSG84.
 * <li>It did not let us specify any constrains (say maximum length of the
 * name field).
 * <li>We did not make use of a "Factory", so our code has no opportunity to be
 * context sensitive (choosing the appropriate implementation for the type we
 * are representing)
 * </ul>
 * The following example code can be used as a reference when fixing up
 * Csv2Shape on your own.
 * 
 * @author Jody Garnett (Refractions Research)
 *
 * @source $URL$
 */
public class FeatureTypeExample {

	/**
	 * This class shows how to create a FeatureType from scratch using an
	 * AttributeTypeFactory and a FeatureTypeFactory.
	 * <p>
	 * A couple of notes; AttributeTypeFactory.newAttributeType is used to
	 * create both AttributeTypes and GeometryAttributeTypes. The factory will
	 * create a GoemetryAttributeType when a JTS Geometry class is used, and you
	 * can use the "metdata" paramters to specify the
	 * CoordinateRefernenceSystem.
	 * <p>
	 * The AttributeTypeFactory implementation is a bit messed up, you can
	 * <b>only</b> use the static final methods to get anything done. This is
	 * something we are fixing for GeoTools 2.5.
	 *
	public FeatureType createUsingFactory() throws Exception {
		final AttributeType LOCATION = AttributeTypeFactory.newAttributeType(
				"Location",
				Point.class,
				false,
				0,
				null,
				DefaultGeographicCRS.WGS84); 		
		final AttributeType NAME = AttributeTypeFactory.newAttributeType(
				"Name",        // name of attirbute type
				String.class,  // attribute type binding
				true,          // nillable
				15             // 15 characters allowed
		);
		
		FeatureTypeBuilder builder = FeatureTypeBuilder.newInstance("Landmarks");
		builder.addType( LOCATION );
		builder.addType( NAME );
		
		FeatureType schema = builder.getFeatureType();
		
		return schema;
	}*/

	/**
	 * In GeoTools 2.5 we will switch to using the GeoAPI
	 * SimpleFeatureTypeFactory interface for the "raw" create methods (a task
	 * made easier with a "builder" as documented here.
	 * 
	 * @return <code>null</code>
	 */
	public SimpleFeature createUsingBuilder() {
		return null;
	}
}
