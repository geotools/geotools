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
package org.geotools.filter.visitor;

import org.geotools.api.filter.And;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.BinaryLogicOperator;
import org.geotools.api.filter.ExcludeFilter;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.api.filter.NativeFilter;
import org.geotools.api.filter.Not;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.api.filter.PropertyIsNil;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.Beyond;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Disjoint;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.api.filter.temporal.After;
import org.geotools.api.filter.temporal.AnyInteracts;
import org.geotools.api.filter.temporal.Before;
import org.geotools.api.filter.temporal.Begins;
import org.geotools.api.filter.temporal.BegunBy;
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.api.filter.temporal.During;
import org.geotools.api.filter.temporal.EndedBy;
import org.geotools.api.filter.temporal.Ends;
import org.geotools.api.filter.temporal.Meets;
import org.geotools.api.filter.temporal.MetBy;
import org.geotools.api.filter.temporal.OverlappedBy;
import org.geotools.api.filter.temporal.TContains;
import org.geotools.api.filter.temporal.TEquals;
import org.geotools.api.filter.temporal.TOverlaps;

/**
 * Base implementation of the FilterVisitor used for inorder traversal of expressions.
 *
 * <p>This class implements the full FilterVisitor interface and will visit every member of a Filter
 * object. This class performs no actions and is not intended to be used directly, instead extend it
 * and overide the methods for the expression types you are interested in. Remember to call the
 * super method if you want to ensure that the entire filter tree is still visited.
 *
 * <p>You may still need to implement FilterVisitor directly if the visit order set out in this
 * class does not meet your needs. This class visits in sequence i.e. Left - Middle - Right for all
 * expressions which have sub-expressions.
 *
 * @author James Macgill, Penn State
 * @author Justin Deoliveira, The Open Planning Project
 */
public class AbstractFilterVisitor implements FilterVisitor {

    /** expression visitor */
    private ExpressionVisitor expressionVisitor;

    /** Empty constructor */
    public AbstractFilterVisitor() {
        this(new NullExpressionVisitor());
    }

    /**
     * Constructs the filter visitor with an expression visitor.
     *
     * <p>Using this constructor allows expressions of a filter to be visited as well.
     */
    public AbstractFilterVisitor(ExpressionVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    /** Does nothing; will return provided data unmodified. */
    @Override
    public Object visit(IncludeFilter filter, Object data) {
        return data;
    }

    /** Does nothing; will return provided data unmodified. */
    @Override
    public Object visit(ExcludeFilter filter, Object data) {
        return data;
    }

    /** Does nothing. */
    @Override
    public Object visitNullFilter(Object data) {
        return null;
    }

    /**
     * Visits filter.getLowerBoundary(),filter.getExpression(),filter.getUpperBoundary() if an
     * expression visitor was set.
     */
    @Override
    public Object visit(PropertyIsBetween filter, Object data) {
        if (filter.getLowerBoundary() != null) {
            filter.getLowerBoundary().accept(expressionVisitor, data);
        }
        if (filter.getExpression() != null) {
            filter.getExpression().accept(expressionVisitor, data);
        }
        if (filter.getUpperBoundary() != null) {
            filter.getUpperBoundary().accept(expressionVisitor, data);
        }
        return filter;
    }

    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor was set.
     */
    protected Object visit(BinaryComparisonOperator filter, Object data) {
        if (expressionVisitor != null) {
            if (filter.getExpression1() != null) {
                filter.getExpression1().accept(expressionVisitor, data);
            }
            if (filter.getExpression2() != null) {
                filter.getExpression2().accept(expressionVisitor, data);
            }
        }
        return filter;
    }

    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor was set.
     */
    @Override
    public Object visit(PropertyIsEqualTo filter, Object data) {
        return visit((BinaryComparisonOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor was set.
     */
    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object data) {
        return visit((BinaryComparisonOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor was set.
     */
    @Override
    public Object visit(PropertyIsLessThan filter, Object data) {
        return visit((BinaryComparisonOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor was set.
     */
    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
        return visit((BinaryComparisonOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor was set.
     */
    @Override
    public Object visit(PropertyIsGreaterThan filter, Object data) {
        return visit((BinaryComparisonOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(), and filter.getExpression2() if an expression visitor was set.
     */
    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
        return visit((BinaryComparisonOperator) filter, data);
    }

    /** does nothing */
    @Override
    public Object visit(BBOX filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }

    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    protected Object visit(BinarySpatialOperator filter, Object data) {
        if (expressionVisitor != null) {
            if (filter.getExpression1() != null) {
                filter.getExpression1().accept(expressionVisitor, data);
            }
            if (filter.getExpression2() != null) {
                filter.getExpression2().accept(expressionVisitor, data);
            }
        }
        return filter;
    }

    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Beyond filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Contains filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Crosses filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Disjoint filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(DWithin filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Equals filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Intersects filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Overlaps filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Touches filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }
    /**
     * Visits filter.getExpression1(),filter.getExpression2() if an expression visitor has been set.
     */
    @Override
    public Object visit(Within filter, Object data) {
        return visit((BinarySpatialOperator) filter, data);
    }

    /** Visits filter.getExpression() if an expression visitor was set. */
    @Override
    public Object visit(PropertyIsLike filter, Object data) {
        if (expressionVisitor != null) {
            if (filter.getExpression() != null) {
                filter.getExpression().accept(expressionVisitor, null);
            }
        }
        return filter;
    }

    /** Visits elements of filter.getChildren(). */
    protected Object visit(BinaryLogicOperator filter, Object data) {
        if (filter.getChildren() != null) {
            for (Filter child : filter.getChildren()) {
                child.accept(this, data);
            }
        }
        return filter;
    }

    /** Visits elements of filter.getChildren(). */
    @Override
    public Object visit(And filter, Object data) {
        return visit((BinaryLogicOperator) filter, data);
    }
    /** Visits elements of filter.getChildren(). */
    @Override
    public Object visit(Or filter, Object data) {
        return visit((BinaryLogicOperator) filter, data);
    }

    /** Visits filter.getFilter(). */
    @Override
    public Object visit(Not filter, Object data) {
        if (filter.getFilter() != null) {
            filter.getFilter().accept(this, data);
        }

        return filter;
    }

    /** Visits filter.getExpression() if an expression visitor was set. */
    @Override
    public Object visit(PropertyIsNull filter, Object data) {
        if (expressionVisitor != null) {
            if (filter.getExpression() != null) {
                filter.getExpression().accept(expressionVisitor, data);
            }
        }
        return filter;
    }

    /** Visits filter.getExpression() if an expression visitor was set. */
    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        if (expressionVisitor != null) {
            if (filter.getExpression() != null) {
                filter.getExpression().accept(expressionVisitor, extraData);
            }
        }
        return filter;
    }

    /** Does nothing. */
    @Override
    public Object visit(Id filter, Object data) {
        // do nothing
        return filter;
    }

    @Override
    public Object visit(After after, Object extraData) {
        return visit((BinaryTemporalOperator) after, extraData);
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visit((BinaryTemporalOperator) anyInteracts, extraData);
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return visit((BinaryTemporalOperator) before, extraData);
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return visit((BinaryTemporalOperator) begins, extraData);
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return visit((BinaryTemporalOperator) begunBy, extraData);
    }

    @Override
    public Object visit(During during, Object extraData) {
        return visit((BinaryTemporalOperator) during, extraData);
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return visit((BinaryTemporalOperator) endedBy, extraData);
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return visit((BinaryTemporalOperator) ends, extraData);
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return visit((BinaryTemporalOperator) meets, extraData);
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return visit((BinaryTemporalOperator) metBy, extraData);
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visit((BinaryTemporalOperator) overlappedBy, extraData);
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return visit((BinaryTemporalOperator) contains, extraData);
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return visit((BinaryTemporalOperator) equals, extraData);
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return visit((BinaryTemporalOperator) contains, extraData);
    }

    protected Object visit(BinaryTemporalOperator filter, Object data) {
        if (expressionVisitor != null) {
            if (filter.getExpression1() != null) {
                filter.getExpression1().accept(expressionVisitor, data);
            }
            if (filter.getExpression2() != null) {
                filter.getExpression2().accept(expressionVisitor, data);
            }
        }

        return filter;
    }

    @Override
    public String toString() {
        String name = getClass().getSimpleName();
        return "AbstractFilterVisitor " + name + " [expressionVisitor=" + expressionVisitor + "]";
    }

    @Override
    public Object visit(NativeFilter filter, Object extraData) {
        return filter;
    }
}
