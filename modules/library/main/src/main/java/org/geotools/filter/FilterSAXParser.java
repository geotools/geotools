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
package org.geotools.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.xml.sax.Attributes;


/**
 * Creates filters from FilterFilter, which reads in a SAX stream and passes
 * the appropriate messages here.
 *
 * @author Rob Hranac, Vision for New York<br>
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 */
public class FilterSAXParser {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");
    
    /** The number of attributes to be found in a like filter */
    private static final int NUM_LIKE_ATTS = 3;

    /** The filter being currently constructed */
    private Filter curFilter = null;

    /** The current completion state of the filter */
    private String curState = "uninitialized";

    /** The short representation of this type of filter */
    private short filterType;

    /** factory for creating filters. */
    private FilterFactory ff;

    /**
     * the Attributes of the filter (only applicable to LIKE filters, I think)
     */
    private Map attributes = new HashMap();

    /**
     * Constructor which flags the operator as between.
     */
    public FilterSAXParser() {
    	this( FilterFactoryFinder.createFilterFactory() );
    }
    /** Constructor injdection */
    public FilterSAXParser( FilterFactory factory ){
    	ff = factory;
    }

    /** Setter injection */
    public void setFilterFactory( FilterFactory factory ){
    	ff = factory;
    }
    /**
     * Handles all incoming generic string 'messages,' including a message to
     * create the filter, based on the XML tag that represents the start of
     * the filter.
     *
     * @param filterType The string from the SAX filter.
     *
     * @throws IllegalFilterException Filter is illegal.
     */
    public void start(short filterType) throws IllegalFilterException {
        LOGGER.finest("starting filter type " + filterType);

        if ((filterType == AbstractFilter.FID) && !curState.equals("fid")) {
            LOGGER.finer("creating the FID filter");
            curFilter = ff.createFidFilter();
        } else if (AbstractFilter.isGeometryDistanceFilter(filterType)) {
            curFilter = ff.createGeometryDistanceFilter(filterType);
        } else if (AbstractFilter.isGeometryFilter(filterType)) {
            curFilter = ff.createGeometryFilter(filterType);
        } else if (filterType == AbstractFilter.BETWEEN) {
            curFilter = ff.createBetweenFilter();
        } else if (filterType == AbstractFilter.NULL) {
            curFilter = ff.createNullFilter();
        } else if (filterType == AbstractFilter.LIKE) {
            curFilter = ff.createLikeFilter();
        } else if (AbstractFilter.isCompareFilter(filterType)) {
            curFilter = ff.createCompareFilter(filterType);
        } else {
            throw new IllegalFilterException(
                "Attempted to start a new filter with invalid type: "
                + filterType);
        }

        curState = setInitialState(filterType);
        this.filterType = filterType;

        attributes = new HashMap();
    }

    /**
     * Handles all incoming generic string 'messages,' including a message to
     * create the filter, based on the XML tag that represents the start of
     * the filter.
     *
     * @param message The string from the SAX filter.
     *
     * @throws IllegalFilterException Filter is illegal.
     */
    public void value(String message) throws IllegalFilterException {
    }

    /**
     * Adds the passed in expression to the current filter.  Generally  created
     * by the ExpressionSAXParser.
     *
     * @param expression The value of the attribute for comparison.
     *
     * @throws IllegalFilterException if the expression does not match what the
     *         current filter is expecting.
     *
     * @task REVISIT: split this method up.
     */
    public void expression(Expression expression) throws IllegalFilterException {
        // Handle all filter compare states and expressions
        if (filterType == AbstractFilter.BETWEEN) {
            if (curState.equals("attribute")) {
                ((BetweenFilter) curFilter).addMiddleValue(expression);
                curState = "LowerBoundary";
            } else if (curState.equals("LowerBoundary")) {
                ((BetweenFilter) curFilter).addLeftValue(expression);
                curState = "UpperBoundary";
            } else if (curState.equals("UpperBoundary")) {
                ((BetweenFilter) curFilter).addRightValue(expression);
                curState = "complete";
            } else {
                throw new IllegalFilterException(
                    "Got expression for Between Filter in illegal state: "
                    + curState);
            }
        } else if (AbstractFilter.isCompareFilter(filterType)) {
            if (curState.equals("leftValue")) {
                ((CompareFilter) curFilter).addLeftValue(expression);
                curState = "rightValue";
            } else if (curState.equals("rightValue")) {
                ((CompareFilter) curFilter).addRightValue(expression);
                curState = "complete";
            } else {
                throw new IllegalFilterException(
                    "Got expression for Compare Filter in illegal state: "
                    + curState);
            }
        } else if (filterType == AbstractFilter.NULL) {
            if (curState.equals("attribute")) {
                ((NullFilter) curFilter).nullCheckValue(expression);
                curState = "complete";
            } else {
                throw new IllegalFilterException(
                    "Got expression for Null Filter in illegal state: "
                    + curState);
            }
        } else if (AbstractFilter.isGeometryFilter(filterType)) {
            if (curState.equals("leftValue")) {
                ((GeometryFilter) curFilter).addLeftGeometry(expression);
                curState = "rightValue";
            } else if (curState.equals("rightValue")) {
                ((GeometryFilter) curFilter).addRightGeometry(expression);

                if (AbstractFilter.isGeometryDistanceFilter(filterType)) {
                    curState = "distance";
                } else {
                    curState = "complete";
                }

                LOGGER.finer("expression called on geometry, curState = "
                    + curState);
            } else {
                throw new IllegalFilterException(
                    "Got expression for Geometry Filter in illegal state: "
                    + curState);
            }
        } else if (filterType == AbstractFilter.LIKE) {
            if (curState.equals("attribute")) {
                ((LikeFilter) curFilter).setValue(expression);
                curState = "pattern";
            } else if (curState.equals("pattern")) {
                if (attributes.size() != NUM_LIKE_ATTS) {
                    throw new IllegalFilterException(
                        "Got wrong number of attributes (expecting 3): "
                        + attributes.size() + "\n" + attributes);
                }

                String wildcard = (String) attributes.get("wildCard");
                
                if ( (wildcard == null) || (wildcard.length() != 1) )
                {
                	throw new IllegalFilterException("like filter -- required attribute 'wildCard' missing or not exactly 1 char long.  Capitalization?");                	
                }
                String singleChar = (String) attributes.get("singleChar");
                
                if ( (singleChar == null)|| (singleChar.length() != 1) )
                {
                   	throw new IllegalFilterException("like filter -- required attribute 'singleChar' missing  or not exactly 1 char long.  Capitalization?");                	
                }
                
                String escapeChar = (String) attributes.get("escape");
                if (escapeChar == null) //totally against spec, but...
                	 escapeChar = (String) attributes.get("escapeChar");
                
                if ( (escapeChar == null)|| (escapeChar.length() != 1) )
                {
                   	throw new IllegalFilterException("like filter -- required attribute 'escape' missing  or not exactly 1 char long.  Capitalization?");                	
                }
                
                LOGGER.fine("escape char is " + escapeChar);

              
                ((LikeFilter) curFilter).setPattern(expression, wildcard,
                    singleChar, escapeChar);
                curState = "complete";
            } else {
                throw new IllegalFilterException(
                    "Got expression for Like Filter in illegal state: "
                    + curState);
            }
        }

        LOGGER.finer("current state (end): " + curState);
    }

    /**
     * Creates the filter held in the parser.
     *
     * @return The current filter to be created by this parser.
     *
     * @throws IllegalFilterException If called before the filter is in a
     *         complete state.
     */
    public Filter create() throws IllegalFilterException {
        if (isComplete()) {
            LOGGER.finer("complete called, state = " + curState);
            curState = "complete"; //added by cholmes fid bug.

            return curFilter;
        } else {
            throw new IllegalFilterException(
                "Got to the end state of an incomplete filter, current"
                + " state is " + curState);
        }
    }

    /**
     * Sets the state that shall be expected next based on the fitlerType. So
     * if a between, null or like is the currentFilter then attribute should
     * be next, if an fid filter then fid should be next.  If it's a
     * comparison, geometry or not, then leftValue should be next.
     *
     * @param filterType An AbstractFilter short of the filter type.
     *
     * @return the string of what state should come next.
     *
     * @throws IllegalFilterException if the filter type is not recognized.
     */
    private static String setInitialState(short filterType)
        throws IllegalFilterException {
        if ((filterType == AbstractFilter.BETWEEN)
                || (filterType == AbstractFilter.NULL)
                || (filterType == AbstractFilter.LIKE)) {
            return "attribute";
        } else if ((filterType == AbstractFilter.FID)) {
            return "fid";
        } else if ((AbstractFilter.isCompareFilter(filterType))
                || (AbstractFilter.isGeometryFilter(filterType))) {
            return "leftValue";
        } else {
            throw new IllegalFilterException("Filter type: " + filterType
                + " is not recognized");
        }
    }

    /**
     * This sets the distance for a GeometryDistanceFilter.  It currently
     * ignores the units, and attempts to convert the distance to a double.
     *
     * @param distance the distance - should be a string of a double.
     * @param units a reference to a units dictionary.
     *
     * @throws IllegalFilterException if the distance string can not be
     *         converted to a double.
     *
     * @task TODO: Implement units, probably with org.geotools.units package
     *       and a special distance class in the filter package.  It would be
     *       nice if the distance class could get any type of units, like it
     *       would handle the conversion.
     */
    public void setDistance(String distance, String units)
        throws IllegalFilterException {
        LOGGER.finer("set distance called, current state is " + curState);

        if (curState.equals("distance")) {
            try {
                double distDouble = Double.parseDouble(distance);
                ((GeometryDistanceFilter) curFilter).setDistance(distDouble);
                curState = "complete";
            } catch (NumberFormatException nfe) {
                throw new IllegalFilterException("could not parse distance: "
                    + distance + " to a double");
            }
        } else {
            throw new IllegalFilterException(
                "Got distance for Geometry Distance Filter in illegal state: "
                + curState + ", geometry and property should be set first");
        }
    }

    /**
     * Sets the filter attributes.  Called when attributes are encountered by
     * the filter filter.  Puts them in a hash map by thier name and  value.
     *
     * @param atts the attributes to set.
     */
    public void setAttributes(Attributes atts) {
        LOGGER.finer("got attribute: " + atts.getLocalName(0) + ", "
            + atts.getValue(0));
        LOGGER.finer("current state: " + curState);

        if (curState.equals("fid")) {
            LOGGER.finer("is a fid");
            ((FidFilter) curFilter).addFid(atts.getValue(0));
            LOGGER.finer("added fid");
        } else {
            for (int i = 0; i < atts.getLength(); i++) {
                this.attributes.put(atts.getLocalName(i), atts.getValue(i));
            }
        }
    }

    /**
     * Indicates that the filter is in a complete state (ready to be created.)
     *
     * @return <tt>true</tt> if the current state is either complete or fid
     */
    private boolean isComplete() {
        return (curState.equals("complete") || curState.equals("fid"));
    }
}
