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
    @DescribeProcess(
        title = "Octagonal Envelope",
        description = "Get the octagonal envelope of this Geometry."
    )
    @DescribeResult(description = "octagonal of geom")
    public static Geometry octagonalEnvelope(@DescribeParameter(name = "geom") Geometry geom) {
        return new OctagonalEnvelope(geom).toGeometry(geom.getFactory());
    }
    // octagonalEnvelope end
}
