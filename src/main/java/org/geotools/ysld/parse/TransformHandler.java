package org.geotools.ysld.parse;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FunctionFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.util.Converters;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class TransformHandler extends YsldParseHandler {

    FeatureTypeStyle featureStyle;

    protected TransformHandler(FeatureTypeStyle featureStyle, Factory factory) {
        super(factory);
        this.featureStyle = featureStyle;
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        // lookup process function factory, if null means process modules not on classpath
        FunctionFactory functionFactory = loadProcessFunctionFactory();
        if (functionFactory == null) {
            LOG.warning("Unable to load process factory, ignoring transform, ensure process modules installed");
            return;
        }

        YamlMap map = obj.map();

        String name = map.str("name");
        if (name == null) {
            throw new IllegalArgumentException("transform must specify a name");
        }

        String input = map.strOr("input", "data");

        Name qName = qName(name);

        // load process parameter info
        Map<String,Parameter> processInfo = loadProcessInfo(qName);
        if (processInfo == null) {
            throw new IllegalArgumentException("No such process: " + name);
        }

        FilterFactory filterFactory = factory.filter;

        // turn properties into inputs for ProcessFunction
        List<Expression> processArgs = new ArrayList();
        processArgs.add(filterFactory.function("parameter", filterFactory.literal(input)));

        YamlMap params = map.map("params");
        if (params != null) {
            for (Map.Entry<String, Object> e : params.raw().entrySet()) {
                String key = e.getKey();
                Object val = e.getValue();

                List<Expression> paramArgs = new ArrayList<Expression>();
                paramArgs.add(factory.filter.literal(key));

                List<Expression> valueArgs = new ArrayList();

                Parameter p = processInfo.get(key);
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

        featureStyle.setTransformation(functionFactory.function(qName(name), processArgs, null));
    }

    void convertAndAdd(Object val, Parameter p, List<Expression> valueArgs) {
        // handle collection case
        if (p.getMaxOccurs() > 1 && val instanceof Collection) {
            for (Object o : (Collection)val) {
                // just add directly
                valueArgs.add(factory.filter.literal(o));
            }
        }
        else {
            // just add directly
            valueArgs.add(factory.filter.literal(val));
        }
    }

    Name qName(String name) {
        String[] split = name.split(":");
        if (split.length == 1) {
            return new NameImpl(split[0]);
        }

        return new NameImpl(split[0], split[1]);
    }

    FunctionFactory loadProcessFunctionFactory() {
        Class functionFactoryClass = null;
        try {
            functionFactoryClass =
                    Class.forName("org.geotools.process.function.ProcessFunctionFactory");
        }
        catch(ClassNotFoundException e) {
            return null;
        }

        try {
            return (FunctionFactory) functionFactoryClass.newInstance();
        }
        catch(Exception e) {
            LOG.log(Level.WARNING, "Error creating process function factory", e);
        }

        return null;
    }

    Map<String,Parameter> loadProcessInfo(Name name) {
        Class processorsClass = null;
        try {
            processorsClass = Class.forName("org.geotools.process.Processors");
            Method getParameterInfo = processorsClass.getMethod("getParameterInfo", Name.class);
            return (Map<String,Parameter>) getParameterInfo.invoke(null, name);
        }
        catch(Exception e) {
            throw new RuntimeException("Error looking up process info", e);
        }
    }
}
