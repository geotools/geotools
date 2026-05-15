/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;

/**
 * Checks if the given zoneId is a parent of the given referenceZoneId. This function is meant to be used against a
 * {@link org.geotools.dggs.gstore.DGGSStore} that will fill in the {@link DGGSInstance} to use, usage in any other
 * context will throw an exception. large
 */
public class ParentsFunction extends DGGSSetFunctionBase {

    Set<String> zoneIds;

    public static FunctionName NAME = functionName(
            "parents",
            "result:Boolean",
            "testedZoneId:String",
            "referenceZoneId:String",
            "dggs:org.geotools.dggs.DGGSInstance");

    public ParentsFunction() {
        super(NAME);
    }

    @Override
    public Object evaluate(Object object) {
        // get the zone being tested
        String testedZoneId = (String) getParameterValue(object, 0);
        if (testedZoneId == null) return Boolean.FALSE;

        return matches(testedZoneId, () -> {
            // check params
            String referenceZoneId = (String) getParameterValue(object, 1);
            DGGSInstance<?> dggs = (DGGSInstance<?>) getParameterValue(object, 2);
            if (referenceZoneId == null || dggs == null) {
                return Collections.emptyIterator();
            }

            // go through the typed ID path
            return dggs.parentsFromString(referenceZoneId);
        });
    }

    @Override
    public void setDGGSInstance(DGGSInstance<?> dggs) {
        Literal dggsLiteral = FF.literal(dggs);
        List<Expression> parameters = getParameters();
        if (parameters.size() == 2) {
            parameters.add(dggsLiteral);
        } else {
            parameters.set(2, dggsLiteral);
        }
        setParameters(parameters);
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // DGGSInstance lifecycle managed elsewhere
    public Iterator<Zone> getMatchedZones() {
        if (!isStable()) throw new IllegalStateException("Source parameters are not stable");
        String referenceZoneId = (String) getParameterValue(null, 1);
        DGGSInstance<?> dggs = (DGGSInstance<?>) getParameterValue(null, 2);
        if (referenceZoneId == null || dggs == null) {
            return Collections.emptyIterator();
        }
        return dggs.parentsFromString(referenceZoneId);
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // DGGSInstance lifecycle managed elsewhere
    public long countMatched() {
        if (!isStable()) throw new IllegalStateException("Source parameters are not stable");
        String referenceZoneId = (String) getParameterValue(null, 1);
        DGGSInstance<?> dggs = (DGGSInstance<?>) getParameterValue(null, 2);
        if (referenceZoneId == null || dggs == null) {
            return 0L;
        }
        return dggs.countParentsFromString(referenceZoneId);
    }
}
