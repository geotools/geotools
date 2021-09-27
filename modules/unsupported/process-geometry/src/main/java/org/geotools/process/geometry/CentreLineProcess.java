package org.geotools.process.geometry;

import java.util.logging.Logger;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.text.Text;
import org.locationtech.jts.geom.Geometry;
import org.opengis.util.InternationalString;

@SuppressWarnings("rawtypes")
public class CentreLineProcess extends StaticMethodsProcessFactory {
    private static final Logger LOG =
            Logger.getLogger("com.ianturton.cookbook.processes.CentreLineProcess");

    @SuppressWarnings("unchecked")
    public CentreLineProcess() {

        super(Text.text("CentreLine"), "CentreLine", CentreLineProcess.class);
    }

    @SuppressWarnings("unchecked")
    public CentreLineProcess(InternationalString title, String namespace, Class<?> targetClass) {
        super(title, namespace, targetClass);
    }

    @DescribeProcess(title = "Centre Line", description = "Extract Centre Line of a Polygon")
    @DescribeResult(description = "A Geometry that is the centre line (skeleton) of the input")
    public static Geometry centreLine(
            @DescribeParameter(
                        name = "geometry",
                        description = "The Geometry to extract the centre line from",
                        min = 1,
                        max = 1
                    )
                    Geometry geometry) {
        LOG.info("got " + geometry.getClass());
        Geometry ret = CentreLine.getCentreLine(geometry, 5.0);
        LOG.info("Returning " + ret.getClass());
        return ret;
    }
}
