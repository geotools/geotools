package org.geotools.process.raster;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.text.Text;

public class RasterProcessFactory extends AnnotatedBeanProcessFactory {

    public RasterProcessFactory() {
        super(Text.text("Raster processes"), "ras",
            AddCoveragesProcess.class, 
            AreaGridProcess.class, 
            ContourProcess.class,
            CropCoverage.class, 
            MultiplyCoveragesProcess.class, 
            PolygonExtractionProcess.class,
            RangeLookupProcess.class,
            RasterAsPointCollectionProcess.class,
            RasterZonalStatistics.class,
            ScaleCoverage.class, 
            StyleCoverage.class);
    }

}