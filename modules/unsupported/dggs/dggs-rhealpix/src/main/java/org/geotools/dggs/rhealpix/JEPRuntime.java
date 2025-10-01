/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.rhealpix;

import jep.JepException;
import jep.SharedInterpreter;

/** Handles the {@link jep.SharedInterpreter}, making sure no threading issues happen. */
public interface JEPRuntime {
    interface Initializer {
        void initalize(SharedInterpreter interpreter) throws JepException;
    }

    interface InterpreterFunction<R> {
        R accept(SharedInterpreter t) throws JepException;
    }

    interface ExceptionHandler<E extends Exception> {
        E accept(JepException exception);
    }

    interface RuntimeExceptionHandler<E extends RuntimeException> {
        E accept(JepException exception);
    }

    SharedInterpreter getInterpreter() throws JepException;
}
