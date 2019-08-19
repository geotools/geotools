/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import org.apache.commons.lang3.StringUtils;
import org.geotools.filter.v2_0.FES;
import org.geotools.xsd.EnumSimpleBinding;
import org.opengis.filter.MultiValuedFilter.MatchAction;

/** Binding for encoding {@link MatchAction} enum values. */
public class MatchActionBinding extends EnumSimpleBinding {

    public MatchActionBinding() {
        super(MatchAction.class, FES.MatchActionType);
    }

    @Override
    public String encode(Object object, String value) throws Exception {
        if (StringUtils.isBlank(value)) return value;
        return StringUtils.capitalize(value.toLowerCase());
    }
}
