/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
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
