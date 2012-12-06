package org.geotools.process.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.factory.FactoryRegistry;
import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.vector.AggregateProcess;
import org.geotools.process.vector.BoundsProcess;
import org.geotools.process.vector.BufferFeatureCollection;
import org.geotools.process.vector.CentroidProcess;
import org.geotools.process.vector.ClipProcess;
import org.geotools.process.vector.CollectGeometries;
import org.geotools.process.vector.CountProcess;
import org.geotools.process.vector.GridProcess;
import org.geotools.process.vector.InclusionFeatureCollection;
import org.geotools.process.vector.IntersectionFeatureCollection;
import org.geotools.process.vector.NearestProcess;
import org.geotools.process.vector.PointBuffers;
import org.geotools.process.vector.QueryProcess;
import org.geotools.process.vector.RectangularClipProcess;
import org.geotools.process.vector.ReprojectProcess;
import org.geotools.process.vector.SimplifyProcess;
import org.geotools.process.vector.SnapProcess;
import org.geotools.process.vector.UnionFeatureCollection;
import org.geotools.process.vector.UniqueProcess;
import org.geotools.process.vector.VectorZonalStatistics;
import org.geotools.text.Text;

/**
 * Factory providing a number of processes for working with feature data.
 * <p>
 * Internally this factory makes use of the information provided by
 * the {@link DescribeProcess} annotations to produce the correct
 * process description.
 * 
 * @author Jody Garnett (LISAsoft)
 *
 * @source $URL$
 */
public class VectorProcessFactory extends AnnotatedBeanProcessFactory {

    static volatile BeanFactoryRegistry<VectorProcess> registry;

    public static BeanFactoryRegistry<VectorProcess> getRegistry() {
        if (registry == null) {
            synchronized (VectorProcessFactory.class) {
                if (registry == null) {
                    registry = new BeanFactoryRegistry<VectorProcess>(VectorProcess.class);
                }
            }
        }
        return registry;
    }

    public VectorProcessFactory() {
        super(Text.text("Vector processes"), "vec", getRegistry().lookupBeanClasses());
    }

}
