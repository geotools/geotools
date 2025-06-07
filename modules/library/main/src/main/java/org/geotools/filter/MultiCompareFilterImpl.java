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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.util.Converters;

/**
 * Support for Multi-valued properties when comparing
 *
 * @author Niels Charlier, Curtin University of Technology
 */
public abstract class MultiCompareFilterImpl extends CompareFilterImpl {

    protected MatchAction matchAction;

    protected MultiCompareFilterImpl(Expression e1, Expression e2) {
        super(e1, e2);
        this.matchAction = MatchAction.ANY;
    }

    protected MultiCompareFilterImpl(Expression e1, Expression e2, boolean matchCase) {
        super(e1, e2, matchCase);
        this.matchAction = MatchAction.ANY;
    }

    protected MultiCompareFilterImpl(Expression e1, Expression e2, MatchAction matchAction) {
        super(e1, e2);
        this.matchAction = matchAction;
    }

    protected MultiCompareFilterImpl(Expression e1, Expression e2, boolean matchCase, MatchAction matchAction) {
        super(e1, e2, matchCase);
        this.matchAction = matchAction;
    }

    @Override
    public MatchAction getMatchAction() {
        return matchAction;
    }

    private Collection<Object> toCollection(Object obj) {
        Object collection = null;
        if (obj instanceof Collection) {
            collection = obj;
        }
        if (obj != null && obj.getClass().isArray()) {
            collection = Converters.convert(obj, List.class);
        }
        @SuppressWarnings("unchecked")
        Collection<Object> cast = (Collection<Object>) collection;
        return cast;
    }

    @Override
    public final boolean evaluate(Object feature) {
        final Object object1 = eval(expression1, feature);
        final Object object2 = eval(expression2, feature);

        Collection<Object> collection1 = toCollection(object1);
        Collection<Object> collection2 = toCollection(object2);

        if (collection1 == null && collection2 == null) {
            return evaluateInternal(object1, object2);
        }

        Collection<Object> leftValues = collection1 != null ? collection1 : Collections.singletonList(object1);
        Collection<Object> rightValues = collection2 != null ? collection2 : Collections.singletonList(object2);

        int count = 0;

        for (Object value1 : leftValues) {
            for (Object value2 : rightValues) {
                boolean temp = evaluateInternal(value1, value2);
                if (temp) {
                    count++;
                }

                switch (matchAction) {
                    case ONE:
                        if (count > 1) return false;
                        break;
                    case ALL:
                        if (!temp) return false;
                        break;
                    case ANY:
                        if (temp) return true;
                        break;
                }
            }
        }

        switch (matchAction) {
            case ONE:
                return count == 1;
            case ALL:
                return true;
            case ANY:
                return false;
            default:
                return false;
        }
    }

    public abstract boolean evaluateInternal(Object value1, Object value2);
}
