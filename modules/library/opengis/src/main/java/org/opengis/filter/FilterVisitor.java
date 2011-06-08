/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

import org.opengis.annotation.Extension;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;


/**
 * Visitor with {@code visit} methods to be called by {@link Filter#accept Filter.accept(...)}.
 * <p>
 * Consider: It is unclear if this visitor should be applied directly to Filter, or should be walked accross
 * the data structure by hand.  The standard complient structure is well defined, and this should negate
 * the need for a formal visitor (we don't have internal structure we are hiding).
 * </p>
 * <p>
 * There is still a very valid use for FilterVisitor, a instance may implement both FilterVisitor and ExpressionVisitor
 * and ExpressionVisitory in one direction, and a FilterVisitor and a StyleVisitor in the other. The ability
 * to directly focus on transforming data within a larger structure is something a normal data walk
 * can not accomplish in a scalable manner.
 * </p>
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/opengis/src/main/java/org/opengis/filter/FilterVisitor.java $
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@Extension
public interface FilterVisitor {
    /**
     * Used to account for a <code>null</code> filter value.
     * <p>
     * This is particularly used during data structure transofrmations, however
     * the use of <code>null</code> is not recommended. Please make use of Filter.NONE
     * and Filter.ALL as placeholder objects that communicate intent.
     * </p>
     * @param extraData Value object provided to visitor
     * @return subclass defined
     */
    Object visitNullFilter(Object extraData);

    /**
     * Visit {@link Filter#EXCLUDE} (often used during data structure transformations).
     *
     * @param filter {@link Filter#EXCLUDE}.
     * @param extraData Value object provided to visitor
     * @return subclass supplied
     */
    Object visit(ExcludeFilter filter, Object extraData);

    /**
     * Visit {@link Filter#INCLUDE} (often used during data structure transformations).
     *
     * @param filter {@link Filter#INCLUDE}.
     * @param extraData Value object provided to visitor
     * @return subclass supplied
     */
    Object visit(IncludeFilter filter, Object extraData);

    Object visit(And filter,                            Object extraData);
    Object visit(Id filter,                             Object extraData);
    Object visit(Not filter,                            Object extraData);
    Object visit(Or filter,                             Object extraData);
    Object visit(PropertyIsBetween filter,              Object extraData);
    Object visit(PropertyIsEqualTo filter,              Object extraData);
    Object visit(PropertyIsNotEqualTo filter,           Object extraData);
    Object visit(PropertyIsGreaterThan filter,          Object extraData);
    Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData);
    Object visit(PropertyIsLessThan filter,             Object extraData);
    Object visit(PropertyIsLessThanOrEqualTo filter,    Object extraData);
    Object visit(PropertyIsLike filter,                 Object extraData);
    Object visit(PropertyIsNull filter,                 Object extraData);

    Object visit(BBOX filter,       Object extraData);
    Object visit(Beyond filter,     Object extraData);
    Object visit(Contains filter,   Object extraData);
    Object visit(Crosses filter,    Object extraData);
    Object visit(Disjoint filter,   Object extraData);
    Object visit(DWithin filter,    Object extraData);
    Object visit(Equals filter,     Object extraData);
    Object visit(Intersects filter, Object extraData);
    Object visit(Overlaps filter,   Object extraData);
    Object visit(Touches filter,    Object extraData);
    Object visit(Within filter,     Object extraData);
    
    Object visit(After after, Object extraData);
    Object visit(AnyInteracts anyInteracts, Object extraData);
    Object visit(Before before, Object extraData);
    Object visit(Begins begins, Object extraData);
    Object visit(BegunBy begunBy, Object extraData);
    Object visit(During during, Object extraData);
    Object visit(EndedBy endedBy, Object extraData);
    Object visit(Ends ends, Object extraData);
    Object visit(Meets meets, Object extraData);
    Object visit(MetBy metBy, Object extraData);
    Object visit(OverlappedBy overlappedBy, Object extraData);
    Object visit(TContains contains, Object extraData);
    Object visit(TEquals equals, Object extraData);
    Object visit(TOverlaps contains, Object extraData);
}
