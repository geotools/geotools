package org.geotools.data.simple;

import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * SimpleFeatureReader with no content.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/data/simple/EmptySimpleFeatureReader.java $
 */
public class EmptySimpleFeatureReader extends EmptyFeatureReader<SimpleFeatureType, SimpleFeature> implements SimpleFeatureReader{

    public EmptySimpleFeatureReader(SimpleFeatureType featureType) {
        super(featureType);        
    }

}
