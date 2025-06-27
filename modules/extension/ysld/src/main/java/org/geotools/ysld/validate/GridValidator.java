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
package org.geotools.ysld.validate;

import org.geotools.styling.zoom.ZoomContext;
import org.geotools.ysld.parse.Util;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

/** Validator for gridset definitions */
public class GridValidator extends YsldValidateHandler {

    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String key = evt.getValue();
        if ("name".equals(key)) {
            context.push(new ZoomContextNameValidator());
        }
    }

    static class ZoomContextNameValidator extends ScalarValidator {

        /** {@inheritDoc} */
        @Override
        protected String validate(String value, ScalarEvent evt, YsldValidateContext context) {
            try {

                ZoomContext namedZoomContext = Util.getNamedZoomContext(value, context.zCtxtFinders);
                if (namedZoomContext != null) {
                    context.zCtxt = namedZoomContext;
                    return null;
                }
                return String.format("Unknown Grid: %s", value);
            } catch (IllegalArgumentException ex) {
                return ex.getMessage();
            }
        }
    }

    @Override
    public void endMapping(MappingEndEvent evt, YsldValidateContext context) {
        context.pop();
    }
}
