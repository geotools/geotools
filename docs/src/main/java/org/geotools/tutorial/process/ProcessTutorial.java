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
package org.geotools.tutorial.process;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.text.Text;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.OctagonalEnvelope;

public class ProcessTutorial extends StaticMethodsProcessFactory<ProcessTutorial> {
    // constructor start
    public ProcessTutorial() {
        super(Text.text("Tutorial"), "tutorial", ProcessTutorial.class);
    }
    // constructor end

    // octagonalEnvelope start
    @DescribeProcess(title = "Octagonal Envelope", description = "Get the octagonal envelope of this Geometry.")
    @DescribeResult(description = "octagonal of geom")
    public static Geometry octagonalEnvelope(@DescribeParameter(name = "geom") Geometry geom) {
        return new OctagonalEnvelope(geom).toGeometry(geom.getFactory());
    }
    // octagonalEnvelope end
}
