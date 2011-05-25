/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.process.feature;

import java.util.Map;

import org.geotools.process.impl.AbstractProcess;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A Process for feature collections.
 *
 * @author Justin Deoliveira, OpenGEO
 * @author Michael Bedward
 * @since 2.6
 *
 *
 * @source $URL$
 */
public abstract class AbstractFeatureCollectionProcess extends AbstractProcess {

    /**
     * Constructor
     *
     * @param factory
     */
    public AbstractFeatureCollectionProcess(AbstractFeatureCollectionProcessFactory factory) {
        super(factory);
    }

    /**
     * Performs an operation on a single feature in the collection.
     * <p>
     * This method should do some work based on the feature and then set any attributes on the feature
     * as necessary. Example of a simple buffering operation:
     * <pre>
     * protected void processFeature(SimpleFeature feature, Map<String, Object> input) throws Exception {
     *    Double buffer = (Double) input.get( BufferFeatureCollectionFactory.BUFFER.key );
     *
     *    Geometry g = (Geometry) feature.getDefaultGeometry();
     *    g = g.buffer( buffer );
     *
     *    feature.setDefaultGeometry( g );
     * }
     * </pre>
     * </p>
     *
     * @param feature the feature being processed
     * @param input a Map of input parameters
     *
     * @throws Exception
     */
    protected abstract void processFeature( SimpleFeature feature, Map<String,Object> input )
        throws Exception;
}
