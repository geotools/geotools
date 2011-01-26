/**
 * Source code examples for the tutorial documentation.
 * <p>
 * The implementations provided here are spliced into the tutorial documentation using sphinx by
 * making use of "markers" placed into the files such as shown below.
 * <pre>
 * public void sample(){
 *     // begin sample
 *     featureCollection.accepts( new FeatureVisitor(){
 *         public void visit( Feature feature ){
 *             System.out.println( feature.getID() );
 *         }
 *     }, null );
 *     // end sample
 * }
 * </pre>
 * With this in mind please consider the source code in the contenxt of the documentation; it may
 * not always show best practice (if it is part of an example leading up to best practice).
 * @author Jody Garnett
 * @version 2.7
 * @since 2.7
 */
package org.geotools.tutorial;