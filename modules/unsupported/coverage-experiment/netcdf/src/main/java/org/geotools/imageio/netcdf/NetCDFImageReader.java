/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import java.awt.image.RenderedImage;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

import org.geotools.imageio.unidata.UnidataImageReader;

/**
 * Base implementation for NetCDF-CF image flat reader. Pixels are assumed
 * organized according the COARDS convention (a precursor of <A
 * HREF="http://www.cfconventions.org/">CF Metadata conventions</A>), i.e. in (<var>t</var>,<var>z</var>,<var>y</var>,<var>x</var>)
 * order, where <var>x</var> varies faster. The image is created from the two
 * last dimensions (<var>x</var>,<var>y</var>).
 * 
 * Each ImageIndex corresponds to a 2D-slice of NetCDF.
 * 
 * {@link NetCDFImageReader} is a {@link ImageReader} able to create
 * {@link RenderedImage} from NetCDF-CF sources.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
public class NetCDFImageReader extends UnidataImageReader {

    /**
     * Explicit Constructor getting {@link ImageReaderSpi} originatingProvider as actual parameter.
     * 
     * @param originatingProvider the {@link ImageReaderSpi}
     */
    public NetCDFImageReader( NetCDFImageReaderSpi originatingProvider ) {
        super(originatingProvider);
    }

}