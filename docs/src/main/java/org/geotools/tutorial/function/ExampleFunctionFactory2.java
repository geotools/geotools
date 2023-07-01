/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.capability.FunctionName;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.filter.capability.FunctionNameImpl;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

public class ExampleFunctionFactory2 implements FunctionFactory {
    private ArrayList<FunctionName> functionList;
    private static FunctionName FIRST = new FunctionNameImpl("first", "geometry");

    public synchronized List<FunctionName> getFunctionNames() {
        if (functionList == null) {
            functionList = new ArrayList<>();
            functionList.add(FIRST);
        }
        return Collections.unmodifiableList(functionList);
    }

    public Function function(String name, List<Expression> args, Literal fallback) {
        return function(new NameImpl(name), args, fallback);
    }

    public Function function(Name name, List<Expression> args, Literal fallback) {
        if (new NameImpl("first").equals(name)) {
            return new AbstractFunction(FIRST, args, fallback) {
                public Geometry evaluate(Object object) {
                    Geometry geom = eval(object, 0, Geometry.class);
                    Coordinate coordinate = geom.getCoordinate();
                    return geom.getFactory().createPoint(coordinate);
                }
            };
        }
        return null; // we do not implement that function
    }
}
