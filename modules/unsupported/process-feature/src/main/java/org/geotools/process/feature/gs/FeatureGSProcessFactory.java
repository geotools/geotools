package org.geotools.process.feature.gs;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.feature.gs.AggregateProcess;
import org.geotools.process.feature.gs.BoundsProcess;
import org.geotools.process.feature.gs.BufferFeatureCollection;
import org.geotools.process.feature.gs.CentroidProcess;
import org.geotools.process.feature.gs.ClipProcess;
import org.geotools.process.feature.gs.CollectGeometries;
import org.geotools.process.feature.gs.CountProcess;
import org.geotools.process.feature.gs.GridProcess;
import org.geotools.process.feature.gs.InclusionFeatureCollection;
import org.geotools.process.feature.gs.IntersectionFeatureCollection;
import org.geotools.process.feature.gs.NearestProcess;
import org.geotools.process.feature.gs.PointBuffers;
import org.geotools.process.feature.gs.QueryProcess;
import org.geotools.process.feature.gs.RectangularClipProcess;
import org.geotools.process.feature.gs.ReprojectProcess;
import org.geotools.process.feature.gs.SimplifyProcess;
import org.geotools.process.feature.gs.SnapProcess;
import org.geotools.process.feature.gs.UnionFeatureCollection;
import org.geotools.process.feature.gs.UniqueProcess;
import org.geotools.process.feature.gs.VectorZonalStatistics;
import org.geotools.text.Text;
import org.opengis.util.InternationalString;

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
public class FeatureGSProcessFactory extends AnnotatedBeanProcessFactory {

    public FeatureGSProcessFactory() {
        super(Text.text("Geospatial Feature Process Factory"), "gs",
                AggregateProcess.class,
                BoundsProcess.class,
                BufferFeatureCollection.class,
                CentroidProcess.class,
                ClipProcess.class,
                CollectGeometries.class,
                CountProcess.class,
                FeatureProcess.class,
                GridProcess.class,
                InclusionFeatureCollection.class,
                IntersectionFeatureCollection.class,
                NearestProcess.class,
                PointBuffers.class,
                QueryProcess.class,
                RectangularClipProcess.class,
                ReprojectProcess.class,
                SimplifyProcess.class,
                SnapProcess.class,
                UnionFeatureCollection.class,
                UniqueProcess.class,
                VectorZonalStatistics.class,
                TransformProcess.class,
                LRSGeocodeProcess.class,
                LRSMeasureProcess.class,
                LRSSegmentProcess.class);
    }

}
