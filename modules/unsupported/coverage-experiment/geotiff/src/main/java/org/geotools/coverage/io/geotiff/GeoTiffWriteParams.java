package org.geotools.coverage.io.geotiff;


import java.util.Locale;

import javax.imageio.ImageWriteParam;

import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;

import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;

/**
 * Subclass of {@link GeoToolsWriteParams} the allows the user to specify
 * parameters to control the process of writing out a GeoTiff file through
 * standards {@link ImageWriteParam} (with possible extensions).
 * 
 * <p>
 * This class allows the user to control the output tile size for the GeoTiff
 * file we are going to create as well as the possible compression.
 * 
 * <p>
 * An example of usage of this parameters is as follows:
 * 
 * <pre>
 * <code>
 *       		//getting a format
 *       		final GeoTiffAccessFactory format = new GeoTiffAccessFactory();
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
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3.x
 * 
 */
class GeoTiffWriteParams extends GeoToolsWriteParams {

	/**
	 * Default constructor.
	 */
	public GeoTiffWriteParams() {
		super(new TIFFImageWriteParam(Locale.getDefault()));
	}

}
