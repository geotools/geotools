package org.geotools.ysld.encode;


import org.geotools.data.Parameter;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.util.logging.Logging;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.geotools.ysld.ProcessUtil.loadProcessFunctionFactory;
import static org.geotools.ysld.ProcessUtil.loadProcessInfo;
import static org.geotools.ysld.ProcessUtil.processName;

public class FeatureStyleEncoder extends Encoder<FeatureTypeStyle> {

    static Logger LOG = Logging.getLogger(FeatureStyleEncoder.class);

    public FeatureStyleEncoder(Style style) {
        super(style.featureTypeStyles().iterator());
    }

    @Override
    protected void encode(FeatureTypeStyle featureStyle) {
        put("name", featureStyle.getName());
        put("title", featureStyle.getTitle());
        put("abstract", featureStyle.getAbstract());
        if (featureStyle.getTransformation() != null) {
            push("transform").inline(new TransformEncoder(featureStyle.getTransformation()));
        }
        put("rules", new RuleEncoder(featureStyle));
    }


    class TransformEncoder extends Encoder<Expression> {
        public TransformEncoder(Expression tx) {
            super(tx);
        }

        @Override
        protected void encode(Expression tx) {
            if (loadProcessFunctionFactory() == null) {
                LOG.warning("Skipping transform, unable to load process factory, ensure process modules installed");
                return;
            }

            if (!(tx instanceof Function)) {
                LOG.warning("Skipping transform, expected a function but got: " + tx);
                return;
            }

            Function ftx = (Function)tx;
            Map<String,Parameter> paramInfo = loadProcessInfo(processName(ftx.getName()));

            if (paramInfo == null) {
                LOG.warning("Skipping transform, unable to locate process named: " + ftx.getName());
                return;
            }

            put("name", ftx.getName());

            Map<String,Object> params = new LinkedHashMap<String, Object>();

            for (Expression expr : ftx.getParameters()) {
                if (!(expr instanceof Function)) {
                    LOG.warning("Skipping parameter, expected a function but got: " + expr);
                    continue;
                }

                Function fexpr = (Function) expr;
                if (fexpr.getParameters().size() < 2) {
                    LOG.warning("Skipping parameter, must have at least two values");
                    continue;
                }

                String paramName = fexpr.getParameters().get(0).evaluate(null, String.class);
                Parameter p = paramInfo.get(paramName);

                Object paramValue = toObjOrNull(fexpr.getParameters().get(1));
                if (fexpr.getParameters().size() > 2) {
                    List<Object> l = new ArrayList<Object>();
                    l.add(paramValue);

                    for (int i = 2; i < fexpr.getParameters().size(); i++) {
                        l.add(toObjOrNull(fexpr.getParameters().get(i)));
                    }

                    paramValue = l;
                }

                params.put(paramName, paramValue);
            }

            push("params").inline(params);
        }
    }
}
