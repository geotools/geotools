package org.geotools.ysld.parse;

import org.geotools.data.Parameter;
import org.geotools.filter.FunctionFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.geotools.ysld.ProcessUtil.*;

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
            LOG.warning("Unable to load process factory, ignoring transform, ensure process modules installed");
            return;
        }

        YamlMap map = obj.map();

        Function function = process(map);
        featureStyle.setTransformation(function);
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
        Map<String,Parameter<?>> processInfo = loadProcessInfo(qName);
        if (processInfo == null) {
            throw new IllegalArgumentException("No such process: " + name);
        }

        FilterFactory filterFactory = factory.filter;

        // turn properties into inputs for ProcessFunction
        List<Expression> processArgs = new ArrayList<>();

        YamlMap params = map.map("params");
        if (params != null) {
            for (Map.Entry<String, Object> e : params.raw().entrySet()) {
                String key = e.getKey();
                Object val = e.getValue();

                List<Expression> paramArgs = new ArrayList<Expression>();
                paramArgs.add(factory.filter.literal(key));

                List<Expression> valueArgs = new ArrayList<>();

                Parameter<?> p = processInfo.get(key);
                if (p != null) {
                    if (val instanceof String) {
                        Expression expr = Util.expression((String)val, true, factory);
                        if (expr != null) {
                            valueArgs.add(expr);
                        }
                    }

                    if (valueArgs.isEmpty()) {
                        convertAndAdd(val, p, valueArgs);
                    }
                }
                else {
                    LOG.warning(String.format("unknown transform parameter: %s", key));
                }

                if (valueArgs.isEmpty()) {
                    valueArgs.add(factory.filter.literal(val));
                }

                paramArgs.addAll(valueArgs);

                processArgs.add(filterFactory.function("parameter", paramArgs.toArray(new Expression[paramArgs.size()])));
            }
        }
        // If this process is the only one, and no input parameter was specified, use data by default
        if( input == null && processes == 1 ) {
            input = "data";
        }
        if( input != null) {
            processArgs.add(filterFactory.function("parameter", filterFactory.literal(input)));
        }
        Function function = functionFactory.function(processName(name), processArgs, null);
        return function;
    }

    void convertAndAdd(Object val, Parameter<?> p, List<Expression> valueArgs) {
        // handle collection case
        if (p.getMaxOccurs() > 1 && val instanceof Collection) {
            for (Object o : (Collection<?>)val) {
                // just add directly
                valueArgs.add(factory.filter.literal(o));
            }
        } else if (val instanceof Map) {
            YamlMap map = YamlMap.<Map<?,?>>create((Map<?,?>)val).map();
            valueArgs.add(process(map));
        } else {
            // just add directly
            valueArgs.add(factory.filter.literal(val));
        }
    }


}
