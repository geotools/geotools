/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.BasicComponentParameter;


/**
 * A pico container "parameter" which allows one to make setter injection
 * optional.
 * <p>
 * This class is a hack of pico container... it should be used with care.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class OptionalComponentParameter extends BasicComponentParameter {
    public OptionalComponentParameter() {
        super();
    }

    public OptionalComponentParameter(Object componentKey) {
        super(componentKey);
    }

    /**
     * Always return true, because since the setter is optional even its not
     * in teh container we can always resolve to <code>null</code>.
     */
    public boolean isResolvable(PicoContainer container, ComponentAdapter adapter,
        Class expectedType) {
        return true;
    }
}
