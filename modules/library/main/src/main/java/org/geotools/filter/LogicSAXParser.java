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


// J2SE dependencies
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.opengis.filter.Filter;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;


/**
 * Processes messages from clients to create Logic Filters.  Handles nested
 * logic filters.  Filters should call start and end when they reach logic
 * filters, and create when the filter is complete.
 * 
 *   This documenation provided by Dave Blasby April 1/2005 after fixing GEOS-328:
 *  DJB: okay, there's no where near enough comments in here to understand whats going on.  Hopefully I'm correct.
 *       I've looked at this for a bit, and this is what I can figure out.
 * 
 *       This is called by the FilterFilter class (nice name...NOT) which is also a sax parser-like class.
 *       Basically, the FilterFilter does most of the Filter parsing - but it hands most of the work off to the
 *       appropriate classes.  For NOT, AND, OR clauses, this class is used.
 * 
 *       As a simple example, <filter> <OR> [STATE_NAME = 'NY'] [STATE_NAME = 'WA'] </OR></FILTER>
 *        Or, in long form:
 *     <Filter>
 *     <Or>
 *         <PropertyIsEqualTo>
 *            <PropertyName>STATE_NAME</PropertyName>
 *              <Literal>NY</Literal>
 *        	</PropertyIsEqualTo>
 *          <PropertyIsEqualTo>
 *            <PropertyName>STATE_NAME</PropertyName>
 *              <Literal>WA</Literal>
 *        	</PropertyIsEqualTo>
 *     </Or>
 *     </Filter>
 * 
 *   The "PropertyIsEqualTo" is handled by another parser, so we dont have to worry about it here for the moment.
 * 
 *   So, the order of events are like this:
 * 
 *    
 *    start( "OR" )
 *    add([STATE_NAME = 'NY'])   // these are handled by another class 
 *    add([STATE_NAME = 'WA'])   // these are handled by another class 
 *    end ("OR")
 *    create()                   // this creates an actual Filter [[ STATE_NAME = NY ] OR [ STATE_NAME = WA ]]
 *  
 *    This is pretty simple, but it gets more complex when you have nested structures.
 *
 * 
 *       <Filter>
 *       <And>
 *       <Or>
 *           <PropertyIsEqualTo>
 *              <PropertyName>STATE_NAME</PropertyName>
 *                <Literal>NY</Literal>
 *          	</PropertyIsEqualTo>
 *            <PropertyIsEqualTo>
 *              <PropertyName>STATE_NAME</PropertyName>
 *                <Literal>WA</Literal>
 *          	</PropertyIsEqualTo>
 *       </Or>
 *                  <PropertyIsEqualTo>
 *                    <PropertyName>STATE_NAME</PropertyName>
 *                      <Literal>BC</Literal>
 *          	</PropertyIsEqualTo>
 *       </And>
 *       </Filter>
 *
 *     Again, we're going to ignore the "PropertyIsEqualTo" stuff since its handled elsewhere.
 *     
 *      The main idea is that there will be a LogicSAXParser for the top-level "AND" and another one
 *      for the nested "OR".  It gets a bit harder to describe because the classes start passing events
 *      to each other.
 *
 *        start("AND")  -- the parent LogicSAXParser starts to construct an "AND" filter
 *        start("OR")   -- the "AND" parser sees that its sub-element is another logic operator.
 *                         It makes another LogicSAXParser that will handle the "OR" SAX events.
 *        add([STATE_NAME = 'NY']) -- this is sent to the "AND" parser.  It then sends it to the "OR" parser.
 *                                    + "OR" parser remembers this component
 *        add([STATE_NAME = 'WA']) -- this is sent to the "AND" parser.  It then sends it to the "OR" parser.
 *                                    + "OR" parser remembers this component
 *        end("OR") -- this is sent to the "AND" parser.  It then sends it to the "OR" parser.
 *                                    + The "OR" parser marks itself as complete
 *                              + The "AND" parser notices that its child is completed parsing
 *                              + The "AND" parser calls create() on the "OR" parser to make a filter (see next step)
 *                              + Since "OR" is finished, "AND" stop passing events to it.
 *        "OR".create() -- makes a "[[ STATE_NAME = NY ] OR [ STATE_NAME = WA ]]" and "AND" remembers it as a component
 *        add ([ STATE_NAME = BC ]) --This is added as a component to the "AND" filter.
 *        end ("AND")   --  the "AND" parser marks itself as complete
 *        create()      --  the "AND" parser creates a FILTER [[[ STATE_NAME = NY ] OR [ STATE_NAME = WA ]] AND [ STATE_NAME = BC ]]
 *
 *
 *      Higher levels of nesting work the same way - each level will send the event down to the next level.
 *
 *      If logicFilter == null then this object is the one doing the processing.  If its non-null, then
 *      the sub-object is doing the processing - event are sent to it.
 *
 * @author Rob Hranac, Vision for New York
 * @author Chris Holmes, TOPP
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class LogicSAXParser {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    /** factory for creating filters. */
    private FilterFactory2 ff;

    /** AbstractFilter filte type. */
    private short logicType = -1;

    /** The array of filters to be logically joined. */
    private List<Filter> subFilters = new ArrayList<Filter>();

    /** An instance of this class for nested logic filter structures. */
    private LogicSAXParser logicFactory = null;

    /** if this logic filter is ready to be created. */
    private boolean isComplete = false;

    /**
     * Constructor which flags the operator as between.
     */
    public LogicSAXParser() {
    	this( CommonFactoryFinder.getFilterFactory2() );    	
    }
    /** Constructor injection */
    public LogicSAXParser( FilterFactory2 factory ){
    	ff = factory;
    	LOGGER.finer("made new logic factory");
    }
    /** Setter injection */
    public void setFilterFactory( FilterFactory2 factory ){
    	ff = factory;
    }

    /**
     * To be called by a parser to start the creation of a logic filter. Can
     * start a nested or a base logic filter.
     *
     * @param logicType OR, or AND abstract filter type.
     *
     * @throws IllegalFilterException if filter type does not match  declared
     *         type.
     */
     // logic types are AND=2, OR=1, NOT=3
    public void start(short logicType) throws IllegalFilterException {
        LOGGER.finest("got a start element: " + logicType);

        if (this.logicType != -1) {
        	//DJB: for GEOS-328 we need to keep the old parser around to handle multiple nestings of logic operators.
        	if (logicFactory == null) {
        		  logicFactory = new LogicSAXParser();
        		}
            logicFactory.start(logicType);
        } else if (!AbstractFilter.isLogicFilter(logicType)) {
            throw new IllegalFilterException(
                "Add logic filter type does not match declared type.");
        } else {
            this.logicType = logicType;
        }
    }

    /**
     * To be called when the sax parser reaches the end of a logic filter.
     * Tells this class to complete.
     *
     * @param logicType the Filter type.
     *
     * @throws IllegalFilterException If the end message can't be processed in
     *         this state.
     */
     // logic types are AND=2, OR=1, NOT=3
    public void end(short logicType) throws IllegalFilterException {
        LOGGER.finer("got an end element: " + logicType);

        if (logicFactory != null) {
            LOGGER.finer("sending end element to nested logic filter: "
                + logicType);
            logicFactory.end(logicType);

            if (logicFactory.isComplete()) {
                subFilters.add(logicFactory.create());
                logicFactory = null;
            }
        } else if (this.logicType == logicType) {
            LOGGER.finer("end element matched internal type: " + this.logicType);
            isComplete = true;
        } else {
            throw new IllegalFilterException(
                "Logic Factory got an end message that it can't process.");
        }
    }

    /**
     * Adds a filter to the current logic list.
     *
     * @param filter The filter to be added.
     */
    public void add(Filter filter) {
        LOGGER.finer("added a filter: " + filter.toString());

        if (logicFactory != null) {
            LOGGER.finer("adding to nested logic filter: " + filter.toString());
            logicFactory.add(filter);
        } else {
            LOGGER.finer("added to sub filters: " + filter.toString());
            subFilters.add(filter);
        }
    }

    /**
     * Creates the the logic filter if in a complete state.
     *
     * @return The created logic filter.
     *
     * @throws IllegalFilterException if the filter is not complete.
     */
    @SuppressWarnings("deprecation")
    public Filter create() throws IllegalFilterException {
        Filter filter = null;

        LOGGER.finer("creating a logic filter");

        if (isComplete()) {
            LOGGER.finer("filter is complete, with type: " + this.logicType);

            if (logicType == AbstractFilter.LOGIC_NOT) {
                filter = ff.not( subFilters.get(0) );
            } else if (logicType == AbstractFilter.LOGIC_AND ){
                filter = ff.and( subFilters );
            }  else if (logicType == AbstractFilter.LOGIC_OR ){
                filter = ff.or( subFilters );
            }

            //reset the variables so it works right if called again.
            subFilters = new ArrayList<Filter>();
            this.logicType = -1;
            isComplete = false;

            return filter;
        } else {
            throw new IllegalFilterException(
                "Attempted to generate incomplete logic filter.");
        }
    }

    /**
     * indicates if the logic filter is complete.
     *
     * @return <tt>true</tt> if this holds a complete logic filter to be
     *         created, <tt>false</tt> otherwise.
     */
    public boolean isComplete() {
        return isComplete;
    }
}
