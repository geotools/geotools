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

import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.ScalarEvent;

public abstract class ScalarValidator extends YsldValidateHandler {
    @Override
    public void alias(AliasEvent evt, YsldValidateContext context) {
        // TODO: Validate alias
        context.pop();
    }

    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String message = validate(evt.getValue(), evt, context);
        if (message != null) {
            context.error(message, evt.getStartMark());
        }
        context.pop();
    }

    /**
     * @return Null if the value successfully validates, otherwise a String containing the
     *     validation error message.
     */
    protected abstract String validate(String value, ScalarEvent evt, YsldValidateContext context);
}
