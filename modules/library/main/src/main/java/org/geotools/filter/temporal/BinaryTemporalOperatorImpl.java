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

import org.opengis.filter.expression.Expression;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.RelativePosition;
import org.opengis.temporal.TemporalPrimitive;

/**
 * 
 *
 * @source $URL$
 */
public abstract class BinaryTemporalOperatorImpl implements BinaryTemporalOperator {

    protected Expression e1,e2;
    protected MatchAction matchAction;
    
    protected BinaryTemporalOperatorImpl(Expression e1, Expression e2) {
        this(e1, e2, MatchAction.ANY);
    }
    
    protected BinaryTemporalOperatorImpl(Expression e1, Expression e2, MatchAction matchAction) {
        this.e1 = e1;
        this.e2 = e2;
        this.matchAction = matchAction;
    }
    
    public Expression getExpression1() {
        return e1;
    }

    public Expression getExpression2() {
        return e2;
    }
    
    public MatchAction getMatchAction() {
        return matchAction;
    }

    public boolean evaluate(Object object) {
        TemporalPrimitive left = toTemporal(object, e1);
        TemporalPrimitive right = toTemporal(object, e2);
        
        if (left == null || right == null) {
            return false;
        }
        
        RelativePosition pos = left.relativePosition(right);
        return pos != null && doEvaluate(pos);
    }
    
    protected Instant toInstant(Object object, Expression e) {
        return e.evaluate(object, Instant.class);
    }
    
    protected Period toPeriod(Object object, Expression e) {
        return e.evaluate(object, Period.class);
    }
    
    protected TemporalPrimitive toTemporal(Object object, Expression e) {
        TemporalPrimitive p = toPeriod(object, e);
        if (p != null) {
            return p;
        }
        
        p = toInstant(object, e);
        if (p != null) {
            return p;
        }
        
        return null;
    }
    
    protected abstract boolean doEvaluate(RelativePosition pos);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
        result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
        result = prime * result + ((matchAction == null) ? 0 : matchAction.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BinaryTemporalOperatorImpl other = (BinaryTemporalOperatorImpl) obj;
        if (e1 == null) {
            if (other.e1 != null)
                return false;
        } else if (!e1.equals(other.e1))
            return false;
        if (e2 == null) {
            if (other.e2 != null)
                return false;
        } else if (!e2.equals(other.e2))
            return false;
        if (matchAction != other.matchAction)
            return false;
        return true;
    }

    
}
