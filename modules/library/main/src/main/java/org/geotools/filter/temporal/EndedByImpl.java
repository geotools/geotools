/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.filter.temporal;

import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.temporal.RelativePosition;

/** @source $URL$ */
public class EndedByImpl extends BinaryTemporalOperatorImpl implements EndedBy {

    public EndedByImpl(Expression e1, Expression e2) {
        super(e1, e2);
    }

    public EndedByImpl(Expression e1, Expression e2, MatchAction matchAction) {
        super(e1, e2, matchAction);
    }

    @Override
    protected boolean doEvaluate(RelativePosition pos) {
        return pos == RelativePosition.ENDED_BY;
    }

    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
