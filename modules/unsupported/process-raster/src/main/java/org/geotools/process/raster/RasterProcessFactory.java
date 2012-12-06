package org.geotools.process.raster;

import org.geotools.process.factory.AnnotatedBeanProcessFactory;
import org.geotools.text.Text;

public class RasterProcessFactory extends AnnotatedBeanProcessFactory {

    static volatile BeanFactoryRegistry<RasterProcess> registry;

    public static BeanFactoryRegistry<RasterProcess> getRegistry() {
        if (registry == null) {
            synchronized (RasterProcessFactory.class) {
                if (registry == null) {
                    registry = new BeanFactoryRegistry<RasterProcess>(RasterProcess.class);
                }
            }
        }
        return registry;
    }

    public RasterProcessFactory() {
        super(Text.text("Raster processes"), "ras", getRegistry().lookupBeanClasses());
    }

}