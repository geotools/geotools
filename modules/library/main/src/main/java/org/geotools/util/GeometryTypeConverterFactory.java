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
package org.geotools.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.Hints;
import org.geotools.util.Converter;
import org.geotools.util.ConverterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
/**
 * Converter factory performing conversions among JTS geometries
 * of different types.
 * 
 *  - single type geometries (Point, LineString, Polygon) are converted to multi types
 *    (MultiPoint, LineString, MultiPolygon) containing 1 geometry element.
 *  - GeometryCollection(s) are converted to multi types, converting each element to the
 *    permitted type (Point, LineString or Polygon).   
 *  
 * @author m.bartolomeoli
 *
 *
 * @source $URL$
 */
public class GeometryTypeConverterFactory implements ConverterFactory {

	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeometryTypeConverterFactory.class);
	
	static GeometryFactory gFac=new GeometryFactory();
	
	public Converter createConverter(Class<?> source, Class<?> target,
			Hints hints) {
		// we can convert geometric types
		if(Geometry.class.isAssignableFrom(source) && Geometry.class.isAssignableFrom(target)) {
			return new Converter() {
				/**
				 * Converts all the geometries of the given GeometryCollection to a specified
				 * simple type.
				 * @param <T>
				 * @param gc
				 * @param target
				 * @return
				 * @throws Exception
				 */
				public <T> List<T> convertAll(GeometryCollection gc,Class<T> target) throws Exception {
					List<T> result=new ArrayList<T>();
					for(int count=0;count<gc.getNumGeometries();count++) {
						T geo=(T)convert(gc.getGeometryN(count),target);
						if(geo!=null)
							result.add(geo);
					}
					return result;
				}
				
				public Object convert(Object source, Class target)
						throws Exception {
					// hierarchy compatible geometries -> nothing to do
					if(target.isAssignableFrom(source.getClass()))
						return source;
					if(source instanceof Geometry) { 
						Geometry sourceGeometry=(Geometry)source;
						
						Geometry destGeometry = null;
						// multi<geometry> types: for each one we
						// try the followings:
						//  - if source is <geometry> we create a multi<geometry> with just 1 element
						//  - if source is a GeometryCollection, we try to convert each element to <geometry>
						//  - else we firtsly convert the geometry to a <geometry> and then we create a multi<geometry> with the obtained element
						if(MultiPoint.class.isAssignableFrom(target)) {
							Point[] points;
							// NC - Empty Geometry Support
							if (sourceGeometry.isEmpty())
				                                points = new Point[0];
				                        else if(source instanceof Point)
								points=new Point[] {(Point)source};
							else if(source instanceof GeometryCollection)
								points=this.convertAll((GeometryCollection)source,Point.class).toArray(new Point[] {});												
							else
								points=new Point[] {(Point)this.convert(source,Point.class)};
							destGeometry = gFac.createMultiPoint(points);
						}
						else if(MultiLineString.class.isAssignableFrom(target)) {
							LineString[] lineStrings;
							// NC - Empty Geometry Support
							if (sourceGeometry.isEmpty())
				                                lineStrings = new LineString[0];
				                        else if(source instanceof LineString)
								lineStrings=new LineString[] {(LineString)source};
							else if(source instanceof GeometryCollection)
								lineStrings=this.convertAll((GeometryCollection)source,LineString.class).toArray(new LineString[] {});							
							else
								lineStrings=new LineString[] {(LineString)this.convert(source,LineString.class)};
							destGeometry = gFac.createMultiLineString(lineStrings);
						}
						else if(MultiPolygon.class.isAssignableFrom(target)) {
							Polygon[] polygons;
							// NC - Empty Geometry Support
				                        if (sourceGeometry.isEmpty())
				                                polygons = new Polygon[0];
				                        else if(source instanceof Polygon)
								polygons=new Polygon[] {(Polygon)source};
							else if(source instanceof GeometryCollection)
								polygons=this.convertAll((GeometryCollection)source,Polygon.class).toArray(new Polygon[] {});
							else
								polygons=new Polygon[] {(Polygon)this.convert(source,Polygon.class)};
							destGeometry = gFac.createMultiPolygon(polygons);
						}
						
						// target is a geometrycollection: we add the source to
						// a new geometrycollection
						else if(GeometryCollection.class.isAssignableFrom(target)) {
						    // NC - Empty Geometry Support
			                            if (sourceGeometry.isEmpty())
			                                destGeometry = gFac
			                                    .createGeometryCollection(new Geometry[0]);
			                            else
			                                destGeometry = gFac.createGeometryCollection(new Geometry[] {(Geometry)source});						
						}
						
						// target is a point: we return the centroid of any complex geometry
						else if(Point.class.isAssignableFrom(target)) {
    						        // NC - Empty Geometry Support
    			                                if (sourceGeometry.isEmpty())
    			                                    destGeometry = gFac.createPoint((Coordinate) null);
    			                                else if(source instanceof MultiPoint && sourceGeometry.getNumGeometries()==1)
    			                                    destGeometry = (Geometry) ((MultiPoint)source).getGeometryN(0).clone();
							else {
								if(LOGGER.isLoggable(Level.FINE))
									LOGGER.fine("Converting Geometry "+source.toString()+" to Point. This could be unsafe");
								destGeometry = ((Geometry)source).getCentroid();
							}
						}
						
						// target is a linestring: we return the linestring connecting all the geometry coordinates
						else if(LineString.class.isAssignableFrom(target)) {
						        // NC - Empty Geometry Support
    			                                if (sourceGeometry.isEmpty())
    			                                    destGeometry = gFac.createLineString(new Coordinate[0]);
    			                                else if(source instanceof MultiLineString && sourceGeometry.getNumGeometries()==1)
								destGeometry = (Geometry) ((MultiLineString)source).getGeometryN(0).clone();
							else {
								if(LOGGER.isLoggable(Level.FINE))
									LOGGER.fine("Converting Geometry "+source.toString()+" to LineString. This could be unsafe");
								destGeometry = gFac.createLineString(getLineStringCoordinates(((Geometry)source).getCoordinates()));
							}
						}
						// target is a polygon: we return a polygon connecting all the coordinates of the given geometry
						else if(Polygon.class.isAssignableFrom(target)) {
						        // NC - Empty Geometry Support
			                                if (sourceGeometry.isEmpty())
			                                    destGeometry = gFac.createLineString(new Coordinate[0]);
			                                else if(source instanceof MultiPolygon && sourceGeometry.getNumGeometries()==1)
								destGeometry = (Geometry) ((MultiPolygon)source).getGeometryN(0).clone();
							else {
								if(LOGGER.isLoggable(Level.FINE))
									LOGGER.fine("Converting Geometry "+source.toString()+" to Polygon. This could be unsafe");
								Coordinate[] coords=getPolygonCoordinates(((Geometry)source).getCoordinates());
								destGeometry = gFac.createPolygon(gFac.createLinearRing(coords), new LinearRing[] {});
							}
						}
											
        					// NC - added cloning above for cases where an existing geometry is used
        		                        // for purpose for changing user data - we don't want any side effects
        
        		                        // NC-added, copy userdata
        		                        if (destGeometry != null) {
        		                            Map<Object, Object> newUserData = new HashMap<Object, Object>();
        
        		                            // copy if anything is already in destination data
        		                            if (destGeometry.getUserData() instanceof Map) {
        		                                newUserData.putAll((Map) destGeometry.getUserData());
        		                            } else if (destGeometry.getUserData() instanceof CoordinateReferenceSystem) {
        		                                newUserData.put(CoordinateReferenceSystem.class,
        		                                        destGeometry.getUserData());
        		                            }
        		                            // overwrite with source
        		                            if (sourceGeometry.getUserData() instanceof Map) {
        		                                newUserData.putAll((Map) sourceGeometry.getUserData());
        		                            } else if (sourceGeometry.getUserData() instanceof CoordinateReferenceSystem) {
        		                                newUserData.put(CoordinateReferenceSystem.class,
        		                                        sourceGeometry.getUserData());
        		                            }
        
        		                            destGeometry.setUserData(newUserData);
        
        		                        }
        		                        return destGeometry;
					}
					
					return null;
				}
				
				@SuppressWarnings("unchecked")
				private <T> T[] arrayCopy(T[] original,int length) {
					Class<?> arrayType = original.getClass().getComponentType();
					T[] copy = (T[])java.lang.reflect.Array.newInstance(arrayType, length);
					System.arraycopy(original, 0, copy, 0, original.length<length ? original.length : length);					
					return copy;
				}


				/**
				 * Add dummy coordinates to the given array to reach
				 * numpoints points.
				 * If the array is already made of numpoints or
				 * more coordinates, it will be returned untouched.
				 * @param input
				 * @param numpoints
				 * @return
				 */
				private Coordinate[] growCoordinatesNum(Coordinate[] input,int numpoints) {
					if(input.length<numpoints) {
						Coordinate[] newCoordinates=arrayCopy(input,numpoints);
						Arrays.fill(newCoordinates, input.length, numpoints,input[0]);
						
						input=newCoordinates;
					}
					return input;
				}
				/**
				 * Gets a set of coordinates valid to create a linestring:
				 *  - at least 2 coordinates
				 *  
				 * @param coordinates
				 * @return
				 */
				private Coordinate[] getLineStringCoordinates(
						Coordinate[] coordinates) {
					// at least 2 points
					coordinates=growCoordinatesNum(coordinates, 2);
					return coordinates;
				}

				/**
				 * Gets a set of coordinates valid to create a polygon:
				 *  - at least 4 coordinates
				 *  - closed path
				 * @param coordinates
				 * @return
				 */
				private Coordinate[] getPolygonCoordinates(
						Coordinate[] coordinates) {
					// at least 4 points
					coordinates=growCoordinatesNum(coordinates, 4);
					
					if(!coordinates[coordinates.length-1].equals(coordinates[0])) {
						Coordinate[] newCoordinates=arrayCopy(coordinates,coordinates.length+1);
						newCoordinates[newCoordinates.length-1]=newCoordinates[0];
						
						coordinates=newCoordinates;
					}
					return coordinates;
				}
				
			};
			
			
		}
		
		return null;
	}

}
