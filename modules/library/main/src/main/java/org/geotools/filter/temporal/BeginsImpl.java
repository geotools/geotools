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
import org.opengis.filter.temporal.Begins;
import org.opengis.temporal.RelativePosition;

public class BeginsImpl extends BinaryTemporalOperatorImpl implements Begins {
    
    public BeginsImpl(Expression e1, Expression e2) {
        super(e1, e2);
    }
    
    public BeginsImpl(Expression e1, Expression e2, MatchAction matchAction) {
        super(e1, e2, matchAction);
    }

    @Override
    protected boolean doEvaluate(RelativePosition pos) {
        return pos == RelativePosition.BEGINS;
    }

    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

}
