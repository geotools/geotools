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
package org.geotools.process;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.vector.TransformProcess;

public class FeatureProcessExamples {

    public void exampleTransformProcess(SimpleFeatureCollection featureCollection) {
        // transform start
        String transform = "the_geom=the_geom\n" + "name=name\n" + "area=area( the_geom )";

        TransformProcess process = new TransformProcess();

        SimpleFeatureCollection features = process.execute(featureCollection, transform);
        // transform end
    }
}
