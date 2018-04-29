/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import static org.geotools.ysld.ProcessUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.geotools.data.Parameter;
import org.geotools.filter.FunctionFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.ysld.ProcessUtil;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/** Handles parsing a Ysld "transform" property into a transformation {@link Function} object. */
public class TransformHandler extends YsldParseHandler {

    FeatureTypeStyle featureStyle;

    int processes = 0;

    FunctionFactory functionFactory = loadProcessFunctionFactory();

    protected TransformHandler(FeatureTypeStyle featureStyle, Factory factory) {
        super(factory);
        this.featureStyle = featureStyle;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        // lookup process function factory, if null means process modules not on classpath
        if (functionFactory == null) {
            LOG.warning(
                    "Unable to load process factory, ignoring transform, ensure process modules installed");
            return;
        }

        YamlMap map = obj.map();

        Function function = process(map);
        featureStyle.setTransformation(function);
    }

    Expression envVar(String name) {
        return factory.filter.function("env", factory.filter.literal(name));
    }

    private Function process(YamlMap map) {
        processes++; // Found a new process in the chain.

        String name = map.str("name");
        if (name == null) {
            throw new IllegalArgumentException("transform must specify a name");
        }

        String input = map.str("input");

        Name qName = processName(name);

        // load process parameter info
        Map<String, Parameter<?>> processInfo = loadProcessInfo(qName);
        if (processInfo == null) {
            throw new IllegalArgumentException("No such process: " + name);
        }

        boolean wmsParams = ProcessUtil.hasWMSParams(processInfo);

        FilterFactory filterFactory = factory.filter;

        // turn properties into inputs for ProcessFunction
        List<Expression> processArgs = new ArrayList<>();

        Expression outputBBOX = null;
        Expression outputWidth = null;
        Expression outputHeight = null;
        if (wmsParams) {
            outputBBOX =
                    paramExpression("outputBBOX", Collections.singletonList(envVar("wms_bbox")));
            outputWidth =
                    paramExpression("outputWidth", Collections.singletonList(envVar("wms_width")));
            outputHeight =
                    paramExpression(
                            "outputHeight", Collections.singletonList(envVar("wms_height")));
        }

        YamlMap params = map.map("params");
        if (params != null) {
            for (Map.Entry<String, Object> e : params.raw().entrySet()) {
                String key = e.getKey();
                Object val = e.getValue();

                List<Expression> valueArgs = new ArrayList<>();

                Parameter<?> p = processInfo.get(key);
                if (p != null) {
                    if (val instanceof String) {
                        Expression expr = Util.expression((String) val, true, factory);
                        if (expr != null) {
                            valueArgs.add(expr);
                        }
                    }

                    if (valueArgs.isEmpty()) {
                        convertAndAdd(val, p, valueArgs);
                    }
                } else {
                    LOG.warning(String.format("unknown transform parameter: %s", key));
                }

                if (valueArgs.isEmpty()) {
                    valueArgs.add(factory.filter.literal(val));
                }
                switch (key) {
                    case "outputBBOX":
                        outputBBOX = paramExpression(key, valueArgs);
                        break;
                    case "outputWidth":
                        outputWidth = paramExpression(key, valueArgs);
                        break;
                    case "outputHeight":
                        outputHeight = paramExpression(key, valueArgs);
                        break;
                    default:
                        processArgs.add(paramExpression(key, valueArgs));
                }
            }
        }
        // If this process is the only one, and no input parameter was specified, use data by
        // default
        if (input == null && processes == 1) {
            input = "data";
        }
        if (input != null) {
            processArgs.add(paramExpression(input, Collections.<Expression>emptyList()));
        }
        if (outputBBOX != null) {
            processArgs.add(outputBBOX);
        }
        if (outputWidth != null) {
            processArgs.add(outputWidth);
        }
        if (outputHeight != null) {
            processArgs.add(outputHeight);
        }
        Function function = functionFactory.function(processName(name), processArgs, null);
        return function;
    }

    private Function paramExpression(String name, List<Expression> valueArgs) {
        List<Expression> paramArgs = new ArrayList<Expression>(valueArgs.size() + 1);
        paramArgs.add(factory.filter.literal(name));
        paramArgs.addAll(valueArgs);
        return factory.filter.function(
                "parameter", paramArgs.toArray(new Expression[paramArgs.size()]));
    }

    void convertAndAdd(Object val, Parameter<?> p, List<Expression> valueArgs) {
        // handle collection case
        if (p.getMaxOccurs() > 1 && val instanceof Collection) {
            for (Object o : (Collection<?>) val) {
                // just add directly
                valueArgs.add(factory.filter.literal(o));
            }
        } else if (val instanceof Map) {
            YamlMap map = YamlMap.<Map<?, ?>>create((Map<?, ?>) val).map();
            valueArgs.add(process(map));
        } else {
            // just add directly
            valueArgs.add(factory.filter.literal(val));
        }
    }
}
