/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;

import it.geosolutions.imageio.plugins.tiff.TIFFColorConverter;
import it.geosolutions.imageio.plugins.tiff.TIFFCompressor;
import it.geosolutions.imageio.plugins.tiff.TIFFImageWriteParam;
import java.util.Locale;
import javax.imageio.ImageWriteParam;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;

/**
 * Subclass of {@link GeoToolsWriteParams} the allows the user to specify parameters to control the
 * process of writing out a GeoTiff file through standards {@link ImageWriteParam} (with possible
 * extensions).
 *
 * <p>This class allows the user to control the output tile size for the GeoTiff file we are going
 * to create as well as the possible compression.
 *
 * <p>An example of usage of this parameters is as follows:
 *
 * <pre>
 * <code>
 *       		//getting a format
 *       		final GeoTiffFormat format = new GeoTiffFormat();
 *
 *      		//getting the write parameters
 *       		final GeoTiffWriteParams wp = new GeoTiffWriteParams();
 *
 *       		//setting compression to LZW
 *       		wp.setCompressionMode(GeoTiffWriteParams.MODE_EXPLICIT);
 *       		wp.setCompressionType(&quot;LZW&quot;);
 *       		wp.setCompressionQuality(0.75F);
 *
 *       		//setting the tile size to 256X256
 *       		wp.setTilingMode(GeoToolsWriteParams.MODE_EXPLICIT);
 *       		wp.setTiling(256, 256);
 *
 *       		//setting the write parameters for this geotiff
 *       		final ParameterValueGroup params = format.getWriteParameters();
 *       		params.parameter(
 *       				AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName().toString())
 *       				.setValue(wp);
 *
 *       		//get a reader to the input File
 *       		GridCoverageReader reader = new GeoTiffReader(inFile, null);
 *       		GridCoverageWriter writer = null;
 *       		GridCoverage2D gc = null;
 *       		if (reader != null) {
 *
 *       			// reading the coverage
 *       			gc = (GridCoverage2D) reader.read(null);
 *   				if (gc != null) {
 *       					final File writeFile = new File(new StringBuffer(writedir
 *       							.getAbsolutePath()).append(File.separatorChar)
 *       							.append(gc.getName().toString()).append(&quot;.tiff&quot;)
 *       							.toString());
 *       					writer = format.getWriter(writeFile);
 *       					writer.write(gc, (GeneralParameterValue[]) params.values()
 *        			.toArray(new GeneralParameterValue[1]));
 *        		}
 * </code>
 * </pre>
 *
 * @author Simone Giannecchini
 * @since 2.3.x
 * @source $URL$
 */
public class GeoTiffWriteParams extends GeoToolsWriteParams {

    /** Default constructor. */
    public GeoTiffWriteParams() {
        super(new TIFFImageWriteParam(Locale.getDefault()));
    }

    public void setForceToBigTIFF(final boolean forceToBigTIFF) {
        ((TIFFImageWriteParam) adaptee).setForceToBigTIFF(forceToBigTIFF);
    }

    public boolean isForceToBigTIFF() {
        return ((TIFFImageWriteParam) adaptee).isForceToBigTIFF();
    }

    /**
     * Returns the current <code>TIFFColorConverter</code> object that will be used to perform color
     * conversion when writing the image, or <code>null</code> if none is set.
     *
     * @return a <code>TIFFColorConverter</code> object, or <code>null</code>.
     * @see #setColorConverter(TIFFColorConverter, int)
     */
    public TIFFColorConverter getColorConverter() {
        return ((TIFFImageWriteParam) adaptee).getColorConverter();
    }

    /**
     * Returns the current value that will be written to the <code>Photometricinterpretation</code>
     * tag. This method should only be called if a value has been set using the <code>
     * setColorConverter</code> method.
     *
     * @return an <code>int</code> to be used as the value of the <code>PhotometricInterpretation
     *     </code> tag.
     * @see #setColorConverter(TIFFColorConverter, int)
     * @throws IllegalStateException if no value is set.
     */
    public int getPhotometricInterpretation() {
        return ((TIFFImageWriteParam) adaptee).getPhotometricInterpretation();
    }

    /**
     * Returns the <code>TIFFCompressor</code> that is currently set to be used by the <code>
     * ImageWriter</code> to encode each image strip or tile, or <code>null</code> if none has been
     * set.
     *
     * @return compressor the <code>TIFFCompressor</code> to be used for encoding, or <code>null
     *     </code> if none has been set (allowing the writer to choose its own).
     * @throws IllegalStateException if the compression mode is not <code>MODE_EXPLICIT</code>.
     * @see #setTIFFCompressor(TIFFCompressor)
     */
    public TIFFCompressor getTIFFCompressor() {
        return ((TIFFImageWriteParam) adaptee).getTIFFCompressor();
    }

    public boolean isCompressionLossless() {
        return ((TIFFImageWriteParam) adaptee).isCompressionLossless();
    }

    /**
     * Sets the <code>TIFFColorConverter</code> object describing the color space to which the input
     * data should be converted for storage in the input stream. In addition, the value to be
     * written to the <code>PhotometricInterpretation</code> tag is supplied.
     *
     * @param colorConverter a <code>TIFFColorConverter</code> object, or <code>null</code>.
     * @param photometricInterpretation the value to be written to the <code>
     *     PhotometricInterpretation</code> tag in the root IFD.
     * @see #getColorConverter
     * @see #getPhotometricInterpretation
     */
    public void setColorConverter(
            TIFFColorConverter colorConverter, int photometricInterpretation) {
        ((TIFFImageWriteParam) adaptee)
                .setColorConverter(colorConverter, photometricInterpretation);
    }

    /**
     * Sets the <code>TIFFCompressor</code> object to be used by the <code>ImageWriter</code> to
     * encode each image strip or tile. A value of <code>null</code> allows the writer to choose its
     * own TIFFCompressor.
     *
     * <p>Note that invoking this method is not sufficient to set the compression type: {@link
     * ImageWriteParam#setCompressionType(String) <code>setCompressionType()</code>} must be invoked
     * explicitly for this purpose. The following code illustrates the correct procedure:
     *
     * <pre>
     * TIFFImageWriteParam writeParam;
     * TIFFCompressor compressor;
     * writeParam.setCompressionMode(writeParam.MODE_EXPLICIT);
     * writeParam.setTIFFCompressor(compressor);
     * writeParam.setCompressionType(compressor.getCompressionType());
     * </pre>
     *
     * If <code>compressionType</code> is set to a value different from that supported by the <code>
     * TIFFCompressor</code> then the compressor object will not be used.
     *
     * <p>If the compression type supported by the supplied <code>TIFFCompressor</code> is not among
     * those in {@link ImageWriteParam#compressionTypes <code>compressionTypes</code>}, then it will
     * be appended to this array after removing any previously appended compression type. If <code>
     * compressor</code> is <code>null</code> this will also cause any previously appended type to
     * be removed from the array.
     *
     * @param compressor the <code>TIFFCompressor</code> to be used for encoding, or <code>null
     *     </code> to allow the writer to choose its own.
     * @throws IllegalStateException if the compression mode is not <code>MODE_EXPLICIT</code>.
     * @see #getTIFFCompressor
     */
    public void setTIFFCompressor(TIFFCompressor compressor) {
        ((TIFFImageWriteParam) adaptee).setTIFFCompressor(compressor);
    }

    /**
     * Removes any currently set <code>ColorConverter</code> object and <code>
     * PhotometricInterpretation</code> tag value.
     *
     * @see #setColorConverter(TIFFColorConverter, int)
     */
    public void unsetColorConverter() {
        ((TIFFImageWriteParam) adaptee).unsetColorConverter();
    }
}
