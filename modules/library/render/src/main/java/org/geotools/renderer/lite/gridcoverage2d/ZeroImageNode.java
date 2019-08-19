/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.renderer.lite.gridcoverage2d;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.ConstantDescriptor;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.GridCoverage;

/** Returns a constant image with the same data type, size and layout as the first source */
public class ZeroImageNode extends BaseCoverageProcessingNode {

    public ZeroImageNode(Hints hints) {
        super(
                1,
                hints,
                SimpleInternationalString.wrap("ZeroImageNode"),
                SimpleInternationalString.wrap("Returns a static black image"));
    }

    @Override
    protected GridCoverage execute() {
        final CoverageProcessingNode source = getSource(0);
        final GridCoverage2D sourceCoverage = (GridCoverage2D) source.getOutput();
        final RenderedImage sourceImage = sourceCoverage.getRenderedImage();
        final Number[] bandValues = new Byte[] {(byte) 0};

        Hints hints = new Hints(getHints());
        final ImageLayout layout = new ImageLayout(sourceImage);
        layout.unsetValid(ImageLayout.COLOR_MODEL_MASK).unsetValid(ImageLayout.SAMPLE_MODEL_MASK);
        hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
        final RenderedOp constant =
                ConstantDescriptor.create(
                        (float) sourceImage.getWidth(),
                        (float) sourceImage.getHeight(),
                        bandValues,
                        hints);

        final GridCoverageFactory factory = getCoverageFactory();
        Map properties = null;
        final NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(sourceCoverage);
        if (noDataProperty != null) {
            properties = new HashMap();
            properties.put(NoDataContainer.GC_NODATA, new NoDataContainer(255));
        }
        return factory.create(
                "zero", constant, sourceCoverage.getGridGeometry(), null, null, properties);
    }
}
