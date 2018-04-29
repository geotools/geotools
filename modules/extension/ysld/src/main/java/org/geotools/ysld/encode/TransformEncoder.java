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
package org.geotools.ysld.encode;

import static org.geotools.ysld.ProcessUtil.loadProcessFunctionFactory;
import static org.geotools.ysld.ProcessUtil.loadProcessInfo;
import static org.geotools.ysld.ProcessUtil.processName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.Parameter;
import org.geotools.ysld.ProcessUtil;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/** Encodes a Rendering Transform, represented by an {@link Expression} as YSLD. */
public class TransformEncoder extends YsldEncodeHandler<Expression> {
    boolean chained;

    public TransformEncoder(Expression tx) {
        this(tx, false);
    }

    public TransformEncoder(Expression tx, boolean chained) {
        super(tx);
        this.chained = chained;
    }

    @Override
    protected void encode(Expression tx) {
        if (loadProcessFunctionFactory() == null) {
            FeatureStyleEncoder.LOG.warning(
                    "Skipping transform, unable to load process factory, ensure process modules installed");
            return;
        }

        if (!(tx instanceof Function)) {
            FeatureStyleEncoder.LOG.warning(
                    "Skipping transform, expected a function but got: " + tx);
            return;
        }

        Function ftx = (Function) tx;
        Map<String, Parameter<?>> paramInfo = loadProcessInfo(processName(ftx.getName()));

        if (paramInfo == null) {
            FeatureStyleEncoder.LOG.warning(
                    "Skipping transform, unable to locate process named: " + ftx.getName());
            return;
        }
        boolean wmsParams = ProcessUtil.hasWMSParams(paramInfo);

        put("name", ftx.getName());

        Map<String, Object> simpleParams = new LinkedHashMap<String, Object>();
        String input = null;
        for (Expression expr : ftx.getParameters()) {
            if (!(expr instanceof Function)) {
                FeatureStyleEncoder.LOG.warning(
                        "Skipping parameter, expected a function but got: " + expr);
                continue;
            }

            Function fexpr = (Function) expr;
            if (fexpr.getParameters().size() < 1) {
                FeatureStyleEncoder.LOG.warning("Skipping parameter, must have at least one value");
                continue;
            }

            String paramName = fexpr.getParameters().get(0).evaluate(null, String.class);

            final Object paramValue;
            if (fexpr.getParameters().size() == 1) {
                // TODO: handle multiple input parameters.
                input = paramName;
                continue; // It's an input parameter so don't include it in the regular parameter
                // list
            } else if (fexpr.getParameters().size() == 2) {
                paramValue = intermediateExpression(fexpr.getParameters().get(1));
            } else {
                List<Object> l = new ArrayList<Object>();
                for (int i = 1; i < fexpr.getParameters().size(); i++) {
                    l.add(intermediateExpression(fexpr.getParameters().get(i)));
                }
                paramValue = l;
            }
            // If the process is a rendering transformation and the parameter is one of the WMS
            // environment parameters with its default values, then skip it as it will be filled in
            // when parsed.
            if (!(wmsParams && isDefaultWMSParam(paramName, paramValue))) {
                simpleParams.put(paramName, paramValue);
            }
        }

        if (input != null && (chained || !input.equals("data"))) {
            put("input", input);
        }

        push("params").inline(simpleParams);
    }

    private boolean isDefaultWMSParam(String paramName, final Object paramValue) {
        if (paramName.equals("outputBBOX") && paramValue.equals("${env('wms_bbox')}")) return true;
        if (paramName.equals("outputWidth") && paramValue.equals("${env('wms_width')}"))
            return true;
        if (paramName.equals("outputHeight") && paramValue.equals("${env('wms_height')}"))
            return true;
        return false;
    }

    Object intermediateExpression(Expression e) {
        if (ProcessUtil.isProcess(e)) {
            chained = true;
            TransformEncoder enc = new TransformEncoder(e, true);
            enc.reset();
            enc.encode(e);
            return enc.root();
        } else {
            return toObjOrNull(e);
        }
    }
}
