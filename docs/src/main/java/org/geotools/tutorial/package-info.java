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
/**
 * Source code examples for the tutorial documentation.
 *
 * <p>The implementations provided here are spliced into the tutorial documentation using sphinx by
 * making use of "markers" placed into the files such as shown below.
 *
 * <pre>
 * public void sample(){
 *     // sample start
 *     featureCollection.accepts( new FeatureVisitor(){
 *         public void visit( Feature feature ){
 *             System.out.println( feature.getID() );
 *         }
 *     }, null );
 *     // sample end
 * }
 * </pre>
 *
 * With this in mind please consider the source code in the contenxt of the documentation; it may
 * not always show best practice (if it is part of an example leading up to best practice).
 *
 * @author Jody Garnett
 * @version 2.7
 * @since 2.7
 */
package org.geotools.tutorial;
