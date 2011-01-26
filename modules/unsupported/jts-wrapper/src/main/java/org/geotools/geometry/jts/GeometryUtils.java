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
package org.geotools.geometry.jts;

/*import com.polexis.go.FactoryManager;
import com.polexis.go.typical.coord.LatLonAlt;
import com.polexis.referencing.crs.CRSUtils;
import com.polexis.referencing.cs.CSUtils;
import com.polexis.units.UnitUtils;*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

//apache dependencies
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//openGIS dependencies
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.SurfaceBoundary;

//geotools dpendencies
import org.geotools.factory.BasicFactories;
import org.geotools.referencing.CRS;

/**
 * @author crossley
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 * @source $URL$
 */
public final class GeometryUtils {
    

    //*************************************************************************
    //  Static Fields
    //*************************************************************************
    
    private static Envelope WHOLE_WORLD;
    
    public static Envelope getWholeWorld() {
        if (WHOLE_WORLD == null) {
            CoordinateReferenceSystem crs = null;
            try {
                crs = org.geotools.referencing.CRS.decode("EPSG:4326");
            } catch (Exception nsace){
                getLog().warn("could not get crs for EPSG:4326");
            }
            
            final BasicFactories commonFactory = BasicFactories.getDefault();
            final GeometryFactory geometryFactory = commonFactory.getGeometryFactory(crs);
            
            final DirectPosition lowerCorner = geometryFactory.createDirectPosition(new double[] { -90, -180 });
            final DirectPosition upperCorner = geometryFactory.createDirectPosition(new double[] { 90, 180 });
            
            WHOLE_WORLD = geometryFactory.createEnvelope(lowerCorner, upperCorner);
        }
        return WHOLE_WORLD;
    }


    public static CoordinateReferenceSystem getCRS(final Envelope envelope) {
        return envelope.getLowerCorner().getCoordinateReferenceSystem();
    }
    
	// PENDING(jdc): need to respect a given Unit for the return array.
    /**
     * Converts an {@code Envelope} to a "minx, miny, maxx, maxy" array.
     * @param envelope
     * @param unit
     * @return
     */
    public static double[] getBBox(final Envelope envelope, final Unit unit) {
        final double[] returnable = new double[4];
        
        final DirectPosition lowerCorner = envelope.getLowerCorner();
        final DirectPosition upperCorner = envelope.getUpperCorner();
        
        final CoordinateSystem cs = getCRS(envelope).getCoordinateSystem();
        final int xIndex = getDirectedAxisIndex(cs, AxisDirection.EAST);
        final Unit xUnit = getDirectedAxisUnit(cs, AxisDirection.EAST);
        final int yIndex = getDirectedAxisIndex(cs, AxisDirection.NORTH);
        final Unit yUnit = getDirectedAxisUnit(cs, AxisDirection.NORTH);
        
        //edited to use javax.measure.unit.Convertor
        UnitConverter xConverter = xUnit.getConverterTo(unit);
        UnitConverter yConverter = yUnit.getConverterTo(unit);
        
        returnable[0] = xConverter.convert(lowerCorner.getOrdinate(xIndex));
        returnable[1] = yConverter.convert(lowerCorner.getOrdinate(yIndex));
        returnable[2] = xConverter.convert(upperCorner.getOrdinate(xIndex));
        returnable[3] = yConverter.convert(upperCorner.getOrdinate(yIndex));
        
        return returnable;
    }
    
    public static Envelope createCRSEnvelope(
            final CoordinateReferenceSystem crs,
            final double minx,
            final double miny,
            final double maxx,
            final double maxy) {
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final GeometryFactory geometryFactory = commonFactory.getGeometryFactory(crs);
        
        final DirectPosition lowerCorner = geometryFactory.createDirectPosition();
        lowerCorner.setOrdinate(0, minx);
        lowerCorner.setOrdinate(1, miny);
        
        final DirectPosition upperCorner = geometryFactory.createDirectPosition();
        upperCorner.setOrdinate(0, maxx);
        upperCorner.setOrdinate(1, maxy);
        
        return geometryFactory.createEnvelope(lowerCorner, upperCorner);
    }
    
    /**
     * DOCUMENT ME.
     * @param crs
     * @param minx
     * @param miny
     * @param maxx
     * @param maxy
     * @param unit
     * @return
     */
    public static Envelope createEnvelope(
            final CoordinateReferenceSystem crs,
            final double minx,
            final double miny,
            final double maxx,
            final double maxy,
            final Unit unit) {
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final GeometryFactory geometryFactory = commonFactory.getGeometryFactory(crs);
        
        final CoordinateSystem cs = crs.getCoordinateSystem();
        
        final int xIndex = getDirectedAxisIndex(cs, AxisDirection.EAST);
        final Unit xUnit = getDirectedAxisUnit(cs, AxisDirection.EAST);
        final int yIndex = getDirectedAxisIndex(cs, AxisDirection.NORTH);
        final Unit yUnit = getDirectedAxisUnit(cs, AxisDirection.NORTH);
        
        // HACK(jdc): need to determine the order of the axes...
        /*int[] indices = CSUtils.getDirectedAxisIndices(
                crs.getCoordinateSystem(), 
                new AxisDirection[] { AxisDirection.EAST, AxisDirection.NORTH });*/
        
        //edited to use javax.measure.unit.Convertor
        UnitConverter xConverter = xUnit.getConverterTo(unit);
        UnitConverter yConverter = yUnit.getConverterTo(unit);
        
        double[] lowerOrdinates = new double[crs.getCoordinateSystem().getDimension()];
        lowerOrdinates[xIndex] = xConverter.convert(minx);
        lowerOrdinates[yIndex] = yConverter.convert(miny);
        
        /*for (int i = 0; i < lowerOrdinates.length; i++) {
            // the east or x ordinate
            if (i == indices[0]) {
                lowerOrdinates[i] = minx;
            // the north or y ordinate    
            } else if (i == indices[1]) {
                lowerOrdinates[i] = miny;
            } else {
                lowerOrdinates[i] = 0;
            }
        }*/
        double[] upperOrdinates = new double[crs.getCoordinateSystem().getDimension()];
        upperOrdinates[xIndex] = xConverter.convert(maxx);
        upperOrdinates[yIndex] = yConverter.convert(maxy);
        
        /*for (int i = 0; i < upperOrdinates.length; i++) {
            // the east or x ordinate
            if (i == indices[0]) {
                upperOrdinates[i] = maxx;
            // the north or y ordinate    
            } else if (i == indices[1]) {
                upperOrdinates[i] = maxy;
            } else {
                upperOrdinates[i] = 0;
            }
        }*/
        final DirectPosition lowerCorner = geometryFactory.createDirectPosition(lowerOrdinates);
        final DirectPosition upperCorner = geometryFactory.createDirectPosition(upperOrdinates);
        
        return geometryFactory.createEnvelope(lowerCorner, upperCorner);
    }
    
    
    
    /**
     * DOCUMENT ME.
     * @param envelope
     * @param crs
     * @param minx
     * @param miny
     * @param maxx
     * @param maxy
     * @return
     */
    public static boolean within(
            final Envelope envelope, 
            final CoordinateReferenceSystem crs, 
            final double minx, 
            final double miny, 
            final double maxx, 
            final double maxy) {
        
        
        final CoordinateSystem cs = crs.getCoordinateSystem();
        final int xIndex = getDirectedAxisIndex(cs, AxisDirection.EAST);
        final int yIndex = getDirectedAxisIndex(cs, AxisDirection.NORTH);
        return ( (minx <= envelope.getMinimum(xIndex)) && (maxx >= envelope.getMaximum(xIndex)) &&
                 (miny <= envelope.getMinimum(yIndex)) && (maxy >= envelope.getMaximum(yIndex)) );
        
    }
    
    /*public static boolean overlaps(
            final Envelope envelope, 
            final CoordinateReferenceSystem crs, 
            final double minx, 
            final double miny, 
            final double maxx, 
            final double maxy) {
        
        
        final CoordinateSystem cs = crs.getCoordinateSystem();
        final int xIndex = CSUtils.getDirectedAxisIndex(cs, AxisDirection.EAST);
        final int yIndex = CSUtils.getDirectedAxisIndex(cs, AxisDirection.NORTH);
        return ( (minx <= envelope.getMinimum(xIndex)) || (maxx >= envelope.getMaximum(xIndex)) ||
                 (miny <= envelope.getMinimum(yIndex)) || (maxy >= envelope.getMaximum(yIndex)) );
        
    }*/
    
    /**
     * DOCUMENT ME.
     * @param envelope1
     * @param envelope2
     * @return
     */
    public static boolean equals(final Envelope envelope1, final Envelope envelope2) {
        //getLog().debug("PENDING(jdc): implement the method instead of returning false...");
        if (envelope1 == null || envelope2 == null) {
            return false;
        }
        final double[] bbox1 = getBBox(envelope1, NonSI.DEGREE_ANGLE);
        final double[] bbox2 = getBBox(envelope2, NonSI.DEGREE_ANGLE);
        return 
            bbox1[0] == bbox2[0] &&
            bbox1[1] == bbox2[1] &&
            bbox1[2] == bbox2[2] &&
            bbox1[3] == bbox2[3];
    }

    /**
     * Determines whether or not the two specified Envelopes intersect.
     * Currently this method requires that the defining corners of the two Envelopes
     * must all have the same CRS, otherwise an Exception is thrown.
     * @param envelope1
     * @param envelope2
     * @return True if the Envelopes overlap
     */
    public static boolean intersects(final Envelope envelope1, final Envelope envelope2) {
        DirectPosition top1 = envelope1.getUpperCorner();
        DirectPosition bot1 = envelope1.getLowerCorner();
        DirectPosition top2 = envelope2.getUpperCorner();
        DirectPosition bot2 = envelope2.getLowerCorner();
        CoordinateReferenceSystem crs = top1.getCoordinateReferenceSystem();
        if (!crs.equals(bot1.getCoordinateReferenceSystem())
        		|| !crs.equals(top2.getCoordinateReferenceSystem())
        		|| !crs.equals(bot2.getCoordinateReferenceSystem())) {
        	throw new IllegalArgumentException(
        		"Current implementation of GeoemtryUtils.intersect requires that the corners of both Envelopes have the same CRS");
        }
        double minx1 = bot1.getOrdinate(0);
        double maxx1 = top1.getOrdinate(0);
        double miny1 = bot1.getOrdinate(1);
        double maxy1 = top1.getOrdinate(1);
        double minx2 = bot2.getOrdinate(0);
        double maxx2 = top2.getOrdinate(0);
        double miny2 = bot2.getOrdinate(1);
        double maxy2 = top2.getOrdinate(1);
        boolean xoverlap = minx2 < maxx1 && maxx2 > minx1;
        return xoverlap && (miny2 < maxy1 && maxy2 > miny1);
    }

    /**
     * Converts a double array to an array of {@code DirectPosition}s.
     * @param points the source data
     * @param sourceDirections the source data's axes' directions
     * @param sourceUnits the source data's axes' units
     * @param crs the target {@code CoordinateReferenceSystem}.  the {@code crs}'s
     *        dimension must match the 'dimension' in the source double array.
     * @return an array of DirectPositions
     */
    public static DirectPosition[] getDirectPositions(
            final double[] points, 
            final AxisDirection[] sourceDirections,
            final Unit[] sourceUnits,
            final CoordinateReferenceSystem crs) {
        int dimension = crs.getCoordinateSystem().getDimension();
        int length = points.length / dimension;
        DirectPosition[] returnable = new DirectPosition[length];
        for (int i = 0; i < length; i++) {
            
            getLog().debug("need to make a DirectPosition");
            // umm, how am i gonna make a DirectPosition here?
            //FactoryManager.getCommonFactory().getGeometryFactory(crs).createDirectPosition
        }
        return returnable;
    }

    /**
     * Converts an array of {@code DirectPosition}s to a double array.
     * @param positions the source data
     * @param targetDirections the target data's axes' directions
     * @param targetUnits the target data's axes' units
     * @return an array of doubles
     */
    public static double[] getPoints(
            final DirectPosition[] positions, 
            final AxisDirection[] targetDirections,
            final Unit[] targetUnits) {
        
        // make our returnable array of doubles
        int length = positions.length * targetDirections.length;
        double[] returnable = new double[length];
        
        // just get the first CRS from the first position
        // these should be homogenous DirectPositions
        CoordinateReferenceSystem crs = positions[0].getCoordinateReferenceSystem();
        CoordinateSystem cs = crs.getCoordinateSystem();
        int dimension = cs.getDimension();

        // find the indices for the axes we want
        int[] axisIndices = new int[targetDirections.length];
        // also need the unit converters
        UnitConverter[] converters = new UnitConverter[targetUnits.length];

        // loop through the directions that were passed in
        for (int i = 0; i < targetDirections.length; i++) {
            
            // loop through the cs' axes, checking their direction.
            // store the index once we've found it
            boolean notfound = true;
            for (int j = 0; notfound && j < dimension; j++) {
                
                // if we match, store the axis' index and the converter
                if (cs.getAxis(j).getDirection().equals(targetDirections[i])) {
                    axisIndices[i] = j;
                    converters[i] = cs.getAxis(j).getUnit().getConverterTo(targetUnits[i]);
                    notfound = false;
                }
            }
        }
        
        // now loop through the given directpositions
        for (int i = 0; i < positions.length; i++) {

            // loop through the position by dimension and store the converted ordinate
            for (int j = 0; j < axisIndices.length; j++) {
                returnable[(i * dimension) + j] = converters[j].convert(positions[i].getOrdinate(axisIndices[j]));
            }
        }
        
        // return our fancy, new array of doubles
        return returnable;
    }
    
    /**
     * Verifies the CRS of the specified {@code DirectPosition} is
     * WGS84, and returns it unmodified if it is.
     * If not, transforms the input into a new DirectPosition
     * with a WGS84 CRS.  Returns it as a LatLonAlt if input was LatLonAlt.
     * @param dp The DirectPosition to examine and transform if necessary
     * @return The original DirectPosition if it was already WGS84,
     * or the transformed DirectPosition.
     */
    public static DirectPosition ensureWGS84(DirectPosition dp) {
    	CoordinateReferenceSystem crs = dp.getCoordinateReferenceSystem();
    	int dim = crs.getCoordinateSystem().getDimension();
    	boolean isProjectedCRS = crs instanceof ProjectedCRS;
    	CoordinateReferenceSystem bcrs = crs instanceof ProjectedCRS 
			? ((ProjectedCRS) crs).getBaseCRS() : crs;
    	
	GeographicCRS wgs84crs = null;
        try {
                wgs84crs = (GeographicCRS) CRS.decode("EPSG:4327");
        } catch (Exception nsace){
                getLog().warn("could not get crs for EPSG:4327");
        }
    	
        //have doubts about following line, was the commented out 2nd clause to condition doing anything - colin       
        if (bcrs.equals(wgs84crs)) {    // || bcrs.equals(CRSUtils.WGS84_PROJ)) {
    		return dp;
    	}
        //again, what does the follllowing achieve? - colin
    	if (bcrs.toWKT().indexOf("WGS84") > -1) {
    		return dp;
    	}
    	if (bcrs instanceof GeographicCRS) {
    		if (((GeographicCRS) bcrs).getDatum().equals(wgs84crs.getDatum())) {
    			return dp;
    		}
    	}
        //not going to need CommonFactory.getCoordinateOperationFactory(),
        //can use transform util in org.geotools.referencing.CRS instaed
        //CoordinateReferenceSystem crs2 = dim == 2 ? wgs84crs : CRSUtils.WGS84_PROJ;
        //same equality issues as above
        DirectPosition dp2 = BasicFactories.getDefault().getGeometryFactory(wgs84crs).createDirectPosition();
        try{
            MathTransform transform = CRS.findMathTransform(crs, wgs84crs);
            transform.transform(dp, dp2);
        } catch (FactoryException fe) {
        	getLog().warn("Could not create CoordinateOperation to convert DirectPosition CRS "
        		+ crs.getName() + " to WGS84, using original ordinates", fe);
        	//throw new IllegalArgumentException("Unconvertible coordinate CRS");
        } catch (TransformException e) {
        	getLog().warn("Could not transform DirectPosition CRS "
        		+ crs.getName() + " to WGS84, using original ordinates", e);
        	//throw new IllegalArgumentException("Unconvertible coordinate CRS");
        } catch (MismatchedDimensionException e) {
        	// PENDING(NL): There's probably something better we can do here
        	// than just throw an exception.  Normally we only care about lat and lon,
        	// and if one has altitude and the other doesn't that shouldn't
        	// be a showstopper.
        	getLog().warn("Dimension mismatch prevented conversion of DirectPosition CRS "
        		+ crs.getName() + " to WGS84, using original ordinates", e);
        	//throw new IllegalArgumentException("Unconvertible coordinate CRS");
        }
        return dp2;
        
        //hmm, not sure about following line, 
        //think the LatLongAlt class was specific to how the polexis code works 
        //and is not needed here
        //boolean wasLatLonAlt = dp instanceof LatLongAlt;
        /*
        if (wasLatLonAlt) {
        	dp = commonFactory.getGeometryFactory(crs).createDirectPosition();
        }
        */
        /*
        CommonFactory commonFactory = FactoryManager.getCommonFactory();
        CoordinateOperationFactory coopFactory = commonFactory.getCoordinateOperationFactory();
        try {
        	CoordinateReferenceSystem crs2 = dim == 2 ? wgs84crs : CRSUtils.WGS84_PROJ;
        	CoordinateOperation coOp = coopFactory.createOperation(crs, crs2);
        	DirectPosition dp2 = commonFactory.getGeometryFactory(crs2).createDirectPosition();
            dp2 = coOp.getMathTransform().transform(dp, dp2);
        	if (dp2.getCoordinateReferenceSystem() != null) {
        		if (wasLatLonAlt) {
        			dp2 = new LatLonAlt(dp2);
        		}
        		return dp2;
        	} else {
        		getLog().warn(
        			"Attempted to convert coordinate CRS, transform method returned DirectPosition with null CRS, using original ordinates",
					new IllegalArgumentException("Unconvertible coordinate CRS"));
        	}
        } catch (FactoryException fe) {
        	getLog().warn("Could not create CoordinateOperation to convert DirectPosition CRS "
        		+ crs.getName() + " to WGS84, using original ordinates", fe);
        	//throw new IllegalArgumentException("Unconvertible coordinate CRS");
        } catch (TransformException e) {
        	getLog().warn("Could not transform DirectPosition CRS "
        		+ crs.getName() + " to WGS84, using original ordinates", e);
        	//throw new IllegalArgumentException("Unconvertible coordinate CRS");
        } catch (MismatchedDimensionException e) {
        	// PENDING(NL): There's probably something better we can do here
        	// than just throw an exception.  Normally we only care about lat and lon,
        	// and if one has altitude and the other doesn't that shouldn't
        	// be a showstopper.
        	getLog().warn("Dimension mismatch prevented conversion of DirectPosition CRS "
        		+ crs.getName() + " to WGS84, using original ordinates", e);
        	//throw new IllegalArgumentException("Unconvertible coordinate CRS");
        } catch (RuntimeException e) {
        	getLog().warn("Could not convert DirectPosition CRS "
        		+ crs.getName() + " to WGS84, using original ordinates", e);
        	//throw e;
        }
        return dp;*/
    }
    
    /**
     * Populates the specified PointArray with the specified points.  Any and all preexisting
     * points in the PointArray will be overwritten.
     * @param pointArray The PointArray to be populated.  This must not be null.
     * @param newPts The list of new points
     * /
    public static void populatePointArray(PointArray pointArray, List newPts) {
    	List pts = pointArray.positions();
    	pts.clear();
    	// PENDING(NL): Verify points are really DirectPositions --
    	// convert from Positions if not
    	// Probably should save this method for when we can use 1.5
    	pts.addAll(newPts);
    }
*/    
    /**
     * Populates the specified PointArray with the specified points.  Any and all preexisting
     * points in the PointArray will be overwritten.
     * @param pointArray The PointArray to be populated.  This must not be null.
     * @param dps The new array of points
     */
    public static void populatePointArray(PointArray pointArray, DirectPosition[] dps) {
    	List pts = pointArray.positions();
    	pts.clear();
    	int count = dps.length;
    	for (int i = 0; i < count; i++) {
    		pts.add(dps[i]);
    	}
    }
    
    /**
     * Populates the specified PointArray with the specified points, starting at
     * the specified index.  Overwrites points with the specified index or higher.
     * @param pointArray The PointArray to be populated.  This must not be null.
     * @param dps The array of points to be added
     * @param startIndex The first position in the PointArray to overwrite
     * @throws ArrayIndexOutOfBoundsException if the start index is negative or
     * exceeds the number of points initially in the PointArray
     * /
    public static void populatePointArray(PointArray pointArray, DirectPosition[] dps,
    		int startIndex) {
    	if (startIndex < 0 || startIndex > pointArray.length()) {
    		throw new ArrayIndexOutOfBoundsException("Specified start index was "
    				+ startIndex + ", PointArray size was " + pointArray.length());
    	}
    	List pts = pointArray.positions();
    	pts.clear();
    	int count = dps.length;
    	for (int i = 0; i < count; i++) {
    		pointArray.set(i, dps[i]);
    	}
    }
*/    
    
    /**
     * Returns an array of LineStrings corresponding
     * to the primitive elements of the specified CompositeCurve.
     * This will be empty if the CompositeCurve is empty.
     * Throws an exception if any element of the CompositeCurve cannot be converted
     * to a LineString.
     * @param cc The CompositeCurve of interest
     * @return an array of LineStrings
     * @throws IllegalArgumentException if any element cannot be converted.
     * For the present version, only Curves that wrap only LineStrings are convertible.
     */
    public static LineString[] getLineStrings(CompositeCurve cc) {
    	ArrayList lsList = getLineStrings(cc, new ArrayList());
    	if (lsList == null) {
    		throw new IllegalArgumentException(
    				"Unable to convert all elements of CompositeCurve to LineString");
    	}
    	return (LineString[]) lsList.toArray(new LineString[lsList.size()]);
    }
    
    /**
     * Recursively populates the specified List with LineStrings corresponding
     * to the primitive elements of the specified CompositeCurve.
     * Returns null if any element of the CompositeCurve cannot be converted
     * to a LineString.
     * @param cc The CompositeCurve of interest
     * @param lsList The ArrayList to be populated
     * @return The populated List, or null if not valid
     */
    private static ArrayList getLineStrings(CompositeCurve cc, ArrayList lsList) {
        // Cast below can be removed when GeoAPI will be allowed to abandon Java 1.4 support.
    	List elements = (List) cc.getGenerators();
    	boolean valid = true;
    	if (!elements.isEmpty()) {
    		Iterator it = elements.iterator();
    		LineString ls = null;
    		while (it.hasNext() && valid) {
    			Object element = it.next();
    			if (element instanceof CompositeCurve) {
    				valid = getLineStrings((CompositeCurve) element, lsList) != null;
    			} else if (element instanceof Curve) {
    				// PENDING(NL):  When we have arc geometries implemented,
    				// make provision to pass in real parameters for spacing and offset.
    				// What we have below essentially just returns start and end points
    				// if it's not a LineString
    				ls = ((Curve) element).asLineString(Double.MAX_VALUE, Double.MAX_VALUE);
    				if (ls != null) {
    					lsList.add(ls);
    				} else {
    					valid = false;
    				}
    			} else {
    				valid = false;
    			}    			
    		}
    	}
    	if (valid) {
    		return null;
    	}
    	return lsList;
    }
    
    public static DirectPosition[] getDirectPositions(final LineString lineString) {
        final PointArray controlPoints = lineString.getControlPoints();
        final DirectPosition[] returnable = new DirectPosition[controlPoints.length()];
        for (int i = 0; i < controlPoints.length(); i++) {
            returnable[i] = controlPoints.getDirectPosition(i, null);
        }
        return returnable;
    }
    
    public static DirectPosition[] getDirectPositions(final Ring ring) {
        final List directPositionList = new ArrayList();
        // Cast below can be removed when GeoAPI will be allowed to abandon Java 1.4 support.
        final List/*<Curve>*/ generators = (List) ring.getGenerators();
        for (int i = 0; i < generators.size(); i++) {
            final Curve curve = (Curve) generators.get(i);
            final List/*<CurveSegments>*/ segments = curve.getSegments();
            for (int j = 0; j < segments.size(); j++) {
                final CurveSegment curveSegment = (CurveSegment) segments.get(j);
                if (curveSegment instanceof LineString) {
                    final LineString lineString = (LineString) curveSegment;
                    final DirectPosition[] positions = getDirectPositions(lineString);
                    directPositionList.addAll(Arrays.asList(positions));
                    /*final List<Position> positions = lineString.getControlPoints().positions();
                    for (int k = 0; k < positions.size(); k++) {
                        Position position = (Position) positions.get(k);
                        directPositionList.add(position.getPosition());
                    }*/
                    
                }
            }            
        }        
        if (directPositionList.size() > 0) {
            return (DirectPosition[]) directPositionList.toArray(new DirectPosition[directPositionList.size()]);
        }
        return new DirectPosition[0];
    }
    
    public static DirectPosition[] getExteriorDirectPositions(final Polygon polygon) {
        final SurfaceBoundary surfaceBoundary = polygon.getBoundary();
        final Ring exteriorRing = surfaceBoundary.getExterior();
        return GeometryUtils.getDirectPositions(exteriorRing);
    }
    
    public static DirectPosition[][] getInteriorDirectPositions(final Polygon polygon) {
        final SurfaceBoundary surfaceBoundary = polygon.getBoundary();
        final List interiorRings = surfaceBoundary.getInteriors();
        final DirectPosition[][] returnable = new DirectPosition[interiorRings.size()][];
        for (int i = 0; i < interiorRings.size(); i++) {
            returnable[i] = getDirectPositions((Ring)interiorRings.get(i));
        }
        return returnable;
    }
    
    public static PolyhedralSurface createPolyhedralSurface(final DirectPosition[][] patchPoints) {
        // get the crs and factories
        final CoordinateReferenceSystem crs = patchPoints[0][0].getCoordinateReferenceSystem();
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final GeometryFactory geometryFactory = commonFactory.getGeometryFactory(crs);
        
        // create polygons from each of the arrays of directPositions
        final List polygons = new ArrayList(patchPoints.length);
        for (int i = 0; i < patchPoints.length; i++) {
            final Polygon polygon = createPolygon(patchPoints[i]);
            polygons.add(polygon);            
        }        
        return geometryFactory.createPolyhedralSurface(polygons);
    }
    
    public static Polygon createPolygon(
            final DirectPosition[] exteriorRing) {
        return createPolygon(exteriorRing, new DirectPosition[0][0]);
    }
    
    public static Polygon createPolygon(
            final DirectPosition[] exteriorRingPoints,
            final DirectPosition[][] interiorRingsPoints) {
        
        final CoordinateReferenceSystem crs = exteriorRingPoints[0].getCoordinateReferenceSystem();
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final GeometryFactory geometryFactory = commonFactory.getGeometryFactory(crs);
        final PrimitiveFactory primitiveFactory = commonFactory.getPrimitiveFactory(crs);
        
        final Ring exteriorRing = createRing(primitiveFactory, exteriorRingPoints);
        
        List interiorRingList = interiorRingsPoints.length == 0 ?
                Collections.EMPTY_LIST : 
                    new ArrayList(interiorRingsPoints.length);
        for (int i = 0; i < interiorRingsPoints.length; i++) {
            final DirectPosition[] interiorRingPoints = interiorRingsPoints[i];
            interiorRingList.add(createRing(primitiveFactory,interiorRingPoints));
        }
        
        final SurfaceBoundary surfaceBoundary =
            primitiveFactory.createSurfaceBoundary(exteriorRing, interiorRingList);
        
        return geometryFactory.createPolygon(surfaceBoundary);
    }
    
    public static SurfaceBoundary createSurfaceBoundary(
            final DirectPosition[] exteriorRingPoints,
            final DirectPosition[][] interiorRingsPoints) {
        final CoordinateReferenceSystem crs = exteriorRingPoints[0].getCoordinateReferenceSystem();
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final PrimitiveFactory primitiveFactory = commonFactory.getPrimitiveFactory(crs);
        return createSurfaceBoundary(primitiveFactory, exteriorRingPoints, interiorRingsPoints);
    }
    
    private static SurfaceBoundary createSurfaceBoundary(
            final PrimitiveFactory primitiveFactory,
            final DirectPosition[] exteriorRingPoints,
            final DirectPosition[][] interiorRingsPoints) {
        
        final Ring exteriorRing = createRing(primitiveFactory, exteriorRingPoints);
        
        final List interiorRingList = interiorRingsPoints.length == 0 ?
                Collections.EMPTY_LIST :
                    new ArrayList();
        for (int i = 0; i < interiorRingsPoints.length; i++) {
            interiorRingList.add(createRing(primitiveFactory, interiorRingsPoints[i]));
        }
        
        final SurfaceBoundary surfaceBoundary =
            primitiveFactory.createSurfaceBoundary(exteriorRing, interiorRingList);
        return surfaceBoundary;
    }
    
    public static Ring createRing(final DirectPosition[] points) {
        final CoordinateReferenceSystem crs = points[0].getCoordinateReferenceSystem();
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final PrimitiveFactory primitiveFactory = commonFactory.getPrimitiveFactory(crs);
        return createRing(primitiveFactory, points);
    }
    
    private static Ring createRing(
            final PrimitiveFactory primitiveFactory, 
            final DirectPosition[] points) {
        
        final List curveList = Collections.singletonList(createCurve(primitiveFactory, points));
        
        final Ring ring = primitiveFactory.createRing(curveList);
        return ring;
    }
    
    public static Curve createCurve(final DirectPosition[] points) {
        final CoordinateReferenceSystem crs = points[0].getCoordinateReferenceSystem();
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final PrimitiveFactory primitiveFactory = commonFactory.getPrimitiveFactory(crs);
        return createCurve(primitiveFactory, points);
    }
    
    private static Curve createCurve(
            final PrimitiveFactory primitiveFactory, 
            final DirectPosition[] points) {
        
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final GeometryFactory geometryFactory = commonFactory.getGeometryFactory(primitiveFactory.getCoordinateReferenceSystem());
        
        final List curveSegmentList = Collections.singletonList(createLineString(geometryFactory, points));
        
        final Curve curve = primitiveFactory.createCurve(curveSegmentList);
        return curve;
    }

    public static LineString createLineString(final DirectPosition[] points) {
        final CoordinateReferenceSystem crs = points[0].getCoordinateReferenceSystem();
        final BasicFactories commonFactory = BasicFactories.getDefault();
        final GeometryFactory geometryFactory = commonFactory.getGeometryFactory(crs);
        return createLineString(geometryFactory, points);
    }
    
    private static LineString createLineString(
            final GeometryFactory geometryFactory, 
            final DirectPosition[] points) {
        
        final LineString lineString = geometryFactory.createLineString(new ArrayList(Arrays.asList(points)));
        return lineString;
    }
    
    
    //*************************************************************************
    //  private static methods
    //*************************************************************************
    
    /**
     * Gets the log.
     * @return the log
     */
    protected static Log getLog() {
        if (log == null) {
            log = LogFactory.getLog(GeometryUtils.class);
        }
        return log;
    }
    
    //*************************************************************************
    //  Static Fields
    //*************************************************************************
    
    /**
     * commons log.
     */
    private static transient Log log;
    
    //*************************************************************************
    //  Constructors
    //*************************************************************************
    
    /**
     * Prevents creating a new {@code GeometryUtils}.
     */
    private GeometryUtils() { }

    /*public static void main(String[] args) {
        CoordinateReferenceSystem crs = CRSUtils.createCoordinateReferenceSystem("4327");
        LatLonAlt[] positions = new LatLonAlt[] {
                new LatLonAlt(Math.PI/4, Math.PI/4, 1, SI.RADIAN, NonSI.MILE, crs),
                new LatLonAlt(-Math.PI/4, -Math.PI/4, .5, SI.RADIAN, NonSI.MILE, crs),
                new LatLonAlt(Math.PI/3, Math.PI/3, 0, SI.RADIAN, NonSI.MILE, crs)
        };
        System.out.println("converting "+positions[0]+", "+positions[1]+", "+positions[2]);
        AxisDirection[] targetDirections = new AxisDirection[] {
                AxisDirection.EAST,
                AxisDirection.NORTH,
                AxisDirection.UP
        };
        System.out.println("directions "+targetDirections[0]+", "+targetDirections[1]+", "+targetDirections[2]);
        Unit[] targetUnits = new Unit[] {
                //SI.RADIAN,
                //SI.RADIAN,
                //NonSI.FOOT
                NonSI.DEGREE_ANGLE,
                NonSI.DEGREE_ANGLE,
                SI.METER
        };
        System.out.println("units      "+targetUnits[0]+", "+targetUnits[1]+", "+targetUnits[2]);
        
        double[] points = getPoints(positions, targetDirections, targetUnits);
        System.out.println("response a "+points[0]+", "+points[1]+", "+points[2]);
        System.out.println("response b "+points[3]+", "+points[4]+", "+points[5]);
        System.out.println("response c "+points[6]+", "+points[7]+", "+points[8]);
        
    }*/
    
         /**
     * Check if a reference coordinate system has the expected number of dimensions.
     * - code lifted from com.polexis.referencing.CRSUtils - thanks Jesse!
     * - not sure i see the need for both this method and CRS.ensureDimensionMatch()
     *
     * @param name     The argument name.
     * @param crs      The coordinate reference system to check.
     * @param expected The expected number of dimensions.
     */
    public static void checkDimension(
            final String name,
            final CoordinateReferenceSystem crs,
            final int expected) {
        if (crs != null) {
            final int actual = crs.getCoordinateSystem().getDimension();
            if (actual != expected) {
                throw new IllegalArgumentException(/*Resources.format(
                 ResourceKeys.ERROR_MISMATCHED_DIMENSION_$3,
                 name, new Integer(actual), new Integer(expected))*/"");
            }
        }
    }
    
    /**
     * Convenience method for checking object dimension validity.
     * This method is usually invoked for argument checking.
     *- code lifted from com.polexis.referencing.CRSUtils - thanks Jesse!
     *
     * @param  name The name of the argument to check.
     * @param  dimension The object dimension.
     * @param  expectedDimension The Expected dimension for the object.
     * @throws MismatchedDimensionException if the object doesn't have the expected dimension.
     */
    public static void ensureDimensionMatch(
            final String name,
            final int dimension,
            final int expectedDimension) throws MismatchedDimensionException {
        if (dimension != expectedDimension) {
            throw new MismatchedDimensionException(name + " does not have " + dimension + "dimension(s)"
                                                    /*
                                                    * Resources.format(
                                                    * ResourceKeys.ERROR_MISMATCHED_DIMENSION_$3,
                                                    * name, new
                                                    * Integer(dimension), new
                                                    * Integer(expectedDimension))
                                                    */);
        }
    }

        /**
     * Returns the {@code CoordinateSystemAxis} with the given {@code AxisDirection}.
     * @param cs the {@code CoordinateSystem} to check
     * @param direction the {@code AxisDirection} to check for
     * @return
     */
    public static CoordinateSystemAxis getDirectedAxis(
            final CoordinateSystem cs, 
            final AxisDirection direction) {
        
        int dimension = cs.getDimension();
        for (int i = 0; i < dimension; i++) {
            if (cs.getAxis(i).getDirection().equals(direction)) {
                return cs.getAxis(i);
            }
        }
        return null;
    }

    /*
     * reurns the index of an axis in a given coordinate system,
     * axes are specified using org.opengis.referencing.cs.AxisDirection
     * used by JTS geometry wrappers
     * - code from com.polexis.referencing.cs.CSUtils
     * - is AbstractCS a more appropriate place for this?
     */
    public static int getDirectedAxisIndex(
         final CoordinateSystem cs, 
         final AxisDirection direction) {
       int dimension = cs.getDimension();
       for (int i = 0; i < dimension; i++) {
       if (cs.getAxis(i).getDirection().equals(direction)) {
            return i;
            }
        }
        return -1;
    }

    /*
     * reurns the index of an axis in a given coordinate system,
     * axes are specified using org.opengis.referencing.cs.AxisDirection
     * used by JTS geometry wrappers
     * - code from com.polexis.referencing.cs.CSUtils
     * - is AbstractCS a more appropriate place for this?
     */    
    public static Unit getDirectedAxisUnit(
            final CoordinateSystem cs,
            final AxisDirection direction) {
        CoordinateSystemAxis axis = getDirectedAxis(cs, direction);
        if (axis != null) {
            return axis.getUnit();
        }
        return null;
    }

}

