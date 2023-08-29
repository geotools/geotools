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
package org.geotools.imagemosaic;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.style.ContrastMethod;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContent;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.util.factory.Hints;

/**
 * A simple viewer for a imageMosaic. You must provide your own mosaic. Which could simply be a
 * folder with image files coupled with a world image file.
 *
 * <p>Testing the multithreading options.
 *
 * <p>Options:
 *
 * <ul>
 *   <li>-t / --threads &lt;number of threads&gt;
 *   <li>-g / --gamma &lt;contrast enhancement with gamma value&gt;
 *   <li>-n / --normalize &lt;contrast enhancement with normalization&gt;
 *   <li>-h / --histogram &lt;contrast enhancement with histogram&gt;
 * </ul>
 *
 * @author Roar Brænden
 */
public class MosaicViewer {

    private static final StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
    private static final FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("You must at least provide the path to the mosaic.");
        }

        String pathMosaic = args[args.length - 1];
        int inxT = findOption(args, "-t", "--threads");
        int numThreads = inxT == -1 ? -1 : Integer.parseInt(args[inxT + 1]);
        boolean normalized = findOption(args, "-n", "--normalized") != -1;
        boolean histogram = findOption(args, "-h", "--histogram") != -1;
        int inxG = findOption(args, "-g", "--gamma");
        double gammaValue = inxG == -1 ? -1 : Double.parseDouble(args[inxG + 1]);

        Style style;
        if (normalized) {
            style = createStyleWithNormalizedEnhancement();
        } else if (histogram) {
            style = createStyleWithHistogramEnhancement();
        } else if (gammaValue != -1) {
            style = createStyleWithGammaValue(gammaValue);
        } else {
            style = createStyle();
        }

        MapContent map = new MapContent();
        map.setTitle("Image mosaic");

        GridReaderLayer layer;

        if (numThreads == -1) {
            // start Create ImageMosaicReader example

            ImageMosaicReader reader = new ImageMosaicReader(new File(pathMosaic));
            layer = new GridReaderLayer(reader, style);

            // end Create ImageMosaicReader example

        } else {
            // start Create Multithreaded ImageMosaicReader example

            ExecutorService executor = Executors.newFixedThreadPool(numThreads);

            ImageMosaicReader reader =
                    new ImageMosaicReader(
                            new File(pathMosaic), new Hints(Hints.EXECUTOR_SERVICE, executor));

            ParameterValue<Boolean> multithreadParam =
                    ImageMosaicFormat.ALLOW_MULTITHREADING.createValue();
            multithreadParam.setValue(true);

            layer =
                    new GridReaderLayer(
                            reader, style, new GeneralParameterValue[] {multithreadParam});

            // end Create Multithreaded ImageMosaicReader example
        }

        map.addLayer(layer);

        JMapFrame.showMap(map);
    }

    /** Creating the simplest possible style to view a coverage */
    private static Style createStyle() {
        // start Create Raster Style

        Style style = styleFactory.createStyle();

        FeatureTypeStyle type = styleFactory.createFeatureTypeStyle();
        style.featureTypeStyles().add(type);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(styleFactory.createRasterSymbolizer());

        type.rules().add(rule);

        // end Create Raster Style
        return style;
    }

    /** Creating a style with contrast enhancement */
    private static Style createStyle(ContrastEnhancement enhancement) {
        Style style = styleFactory.createStyle();

        FeatureTypeStyle type = styleFactory.createFeatureTypeStyle();
        style.featureTypeStyles().add(type);

        Rule rule = styleFactory.createRule();
        RasterSymbolizer symbolizer = styleFactory.createRasterSymbolizer();
        if (enhancement != null) {
            symbolizer.setContrastEnhancement(enhancement);
        }
        rule.symbolizers().add(symbolizer);

        type.rules().add(rule);
        return style;
    }

    private static Style createStyleWithGammaValue(double gammaValue) {
        ContrastEnhancement enhancementElement =
                styleFactory.createContrastEnhancement(filterFactory.literal(gammaValue));
        return createStyle(enhancementElement);
    }

    private static Style createStyleWithNormalizedEnhancement() {
        ContrastEnhancement enhancementElement = styleFactory.createContrastEnhancement();
        enhancementElement.setMethod(ContrastMethod.NORMALIZE);
        return createStyle(enhancementElement);
    }

    private static Style createStyleWithHistogramEnhancement() {
        ContrastEnhancement enhancementElement = styleFactory.createContrastEnhancement();
        enhancementElement.setMethod(ContrastMethod.HISTOGRAM);
        return createStyle(enhancementElement);
    }

    private static int findOption(String[] args, String... checkfor) {
        for (int i = 0; i < args.length; i++) {
            for (String check : checkfor) {
                if (check.equals(args[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
}
