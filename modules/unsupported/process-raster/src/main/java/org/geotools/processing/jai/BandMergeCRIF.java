/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014 TOPP - www.openplans.org.
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
package org.geotools.processing.jai;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.ArrayList;
import java.util.List;

import javax.media.jai.CRIFImpl;
import javax.media.jai.ImageLayout;
import javax.media.jai.ROI;

import org.geotools.processing.jai.nodata.Range;

import com.sun.media.jai.opimage.RIFUtil;

/**
 * A <code>CRIF</code> supporting the "BandMerge" operation on rendered and renderable images.
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 */
public class BandMergeCRIF extends CRIFImpl {

    /** Constructor. */
    public BandMergeCRIF() {
        super("bandmergeOp");
    }

    /**
     * Creates a new instance of <code>BandMergeOpImage</code> in the rendered layer.
     * 
     * @param paramBlock The two or more source images to be "Merged" together; if No Data are present, also a NoData Range and a double value for the
     *        destination no data are present.
     * @param renderHints Optionally contains destination image layout.
     */
    public RenderedImage create(ParameterBlock paramBlock, RenderingHints renderHints) {
        // Get ImageLayout from renderHints if any.
        ImageLayout layout = RIFUtil.getImageLayoutHint(renderHints);
        // Get the number of the sources
        int numSources = paramBlock.getNumSources();
        // Creation of a source ArrayList (better than a Vector)
        List sources = new ArrayList(numSources);

        // Addition of the sources to the List
        for (int i = 0; i < numSources; i++) {
            sources.add(paramBlock.getSource(i));
        }

        // Parameters
        Range[] nodata = (Range[]) paramBlock.getObjectParameter(0);
        double destinationNoData = paramBlock.getDoubleParameter(1);

        // Transformation Object
        List<AffineTransform> transform = (List<AffineTransform>) paramBlock.getObjectParameter(2);
        // ROI object
        ROI roi = (ROI) paramBlock.getObjectParameter(3);
        // If the transformations are present, then they are used with the ExtendedBandMergeOpImage
        if (transform != null && !transform.isEmpty()) {
            return new ExtendedBandMergeOpImage(sources, transform, renderHints, nodata, roi,
                    destinationNoData, layout);
        } else {
            return new BandMergeOpImage(sources, renderHints, nodata, roi, destinationNoData,
                    layout);
        }
    }
}
