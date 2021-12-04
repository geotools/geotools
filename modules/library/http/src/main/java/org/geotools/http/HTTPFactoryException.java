/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.http;

import java.util.List;
import java.util.stream.Collectors;
import org.geotools.util.factory.Hints;

/** @author Roar Brænden */
public class HTTPFactoryException extends RuntimeException {

    /** */
    private static final long serialVersionUID = 3038706500613959333L;

    public HTTPFactoryException(
            String message, Hints hints, List<Class<? extends HTTPBehavior>> behaviors) {
        super(createMessage(message, hints, behaviors));
    }

    private static String createMessage(
            String message, Hints hints, List<Class<? extends HTTPBehavior>> behaviors) {
        if (hints.containsKey(Hints.HTTP_CLIENT_FACTORY) || hints.containsKey(Hints.HTTP_CLIENT)) {
            message =
                    String.format(
                            "%s\nHTTP_CLIENT_FACTORY(%s) HTTP_CLIENT(%s)",
                            message,
                            hints.get(Hints.HTTP_CLIENT_FACTORY),
                            hints.get(Hints.HTTP_CLIENT));
        }
        if (!behaviors.isEmpty()) {
            message =
                    String.format(
                            "%s\nBehaviors:%s",
                            message,
                            behaviors.stream()
                                    .map(behavior -> behavior.getSimpleName())
                                    .collect(Collectors.joining(",")));
        }
        return message;
    }
}
