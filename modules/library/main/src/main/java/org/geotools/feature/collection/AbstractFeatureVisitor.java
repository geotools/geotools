/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import org.geotools.api.feature.FeatureVisitor;

/**
 * An abstract class to reduce the amount of work needed when working with FeatureVisitor.
 *
 * <p>This class is best used when making anonymous inner classes:
 *
 * <pre><code>
 * features.accepts(new AbstractFeatureVisitor(){
 *     public void visit( org.geotools.api.feature.Feature feature ) {
 *         bounds.include(feature.getBounds());
 *     }
 * }, null);
 * </code></pre>
 *
 * @author Jody Garnett
 */
public abstract class AbstractFeatureVisitor implements FeatureVisitor {
    // public void init( FeatureCollection<? extends FeatureType, ? extends Feature> collection ) {
    // }
}
