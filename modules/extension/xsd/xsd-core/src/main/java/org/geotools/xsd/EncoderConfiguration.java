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
package org.geotools.xsd;

import org.picocontainer.MutablePicoContainer;

/**
 * Used to configure an instance of {@link org.geotools.xml.Writer}.
 *
 * <p>Implementations supply a series of {@link Encoder} implementations (typically one for each
 * type of object in your model that must be encoded.
 *
 * <p>Encoder implementations are supplied by registering them with the supplied container.
 *
 * <pre>
 *         <code>
 *        class MyEncoderConfiguration implements EncoderConfiguration {
 *                void configure(MutablePicoContainer container) {
 *                         container.registerComponentImplementation(FooEncoder.class);
 *                  container.registerComponentImplementation(BarEncoder.class);
 *                  ...
 *                }
 *         }
 *         </code>
 * </pre>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface EncoderConfiguration {
    /**
     * Populates the container with implementations of {@link Encoder}.
     *
     * @param container The container used to store encoder implementations.
     */
    void confgiure(MutablePicoContainer container);
}
