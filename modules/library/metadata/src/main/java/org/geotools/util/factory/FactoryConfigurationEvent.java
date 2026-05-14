/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.factory;

import java.awt.RenderingHints;
import java.io.Serial;
import java.util.EventObject;
import java.util.Map;

/** FactoryConfigurationEvent is used to notify that GeoTools library configuration has changed. */
public class FactoryConfigurationEvent extends EventObject {
    @Serial
    private static final long serialVersionUID = 3154238322369916489L;

    private final Map<RenderingHints.Key, Object> hints;

    public FactoryConfigurationEvent(Object source) {
        this(source, null);
    }

    public FactoryConfigurationEvent(Object source, Map<RenderingHints.Key, Object> hints) {
        super(source);
        this.hints = hints;
    }

    /**
     * Indications of changes that have occurred, or {@code null} for a system-wide change.
     *
     * @return factory configuration changes
     */
    public Map<RenderingHints.Key, Object> getHints() {
        return hints;
    }
}
