/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.gtopo30;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverageWriter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.processing.operation.Resample;
import org.geotools.coverage.processing.operation.SelectSampleDimension;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.image.io.ImageIOExt;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;

import com.sun.media.jai.operator.ImageWriteDescriptor;

/**
 * Class used for encoding a {@link GridCoverage2D} into a GTOPO30 file.
 * 
 * 
 * @author Simone Giannecchini
 * @author jeichar
 * @author mkraemer
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/plugin/gtopo30/src/org/geotools/gce/gtopo30/GTopo30Writer.java $
 */
final public class GTopo30Writer extends AbstractGridCoverageWriter implements
		GridCoverageWriter {

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.gce.gtopo30");

	static {
		// register new JAI operation
		NoDataReplacerOpImage.register(JAI.getDefaultInstance());
	}

	/** Cached factory for a {@link SelectSampleDimension} operation. */
	private final static SelectSampleDimension sdFactory = new SelectSampleDimension();





	/** Cached factory for {@link Resample} operation. */
	private final static Resample resampleFactory = new Resample();

	/**
	 * Standard width for the GIF image.
	 */
	private final static int GIF_WIDTH = 640;

	/** Standard height for the GIF image. */
	private final static int GIF_HEIGHT = 480;

	/**
	 * Creates a {@link GTopo30Writer}.
	 * 
	 * @param dest
	 *            The destination object can be a {@link File} (a directory
	 *            actually), an {@link URL} to a directory, or a String
	 *            representing a directory or an {@link URL} to a directory.
	 * @throws DataSourceException
	 */
	public GTopo30Writer(final Object dest) throws DataSourceException {
		this(dest, null);
	}

	/**
	 * Creates a {@link GTopo30Writer}.
	 * 
	 * @param dest
	 *            The destination object can be a File (a directory actually),
	 *            an URL to a directory, or a String representing a directory or
	 *            an URL to a directory.
	 * @throws DataSourceException
	 */
	public GTopo30Writer(final Object dest, final Hints hints)
			throws DataSourceException {

		// /////////////////////////////////////////////////////////////////////
		//
		// Initial checks
		//
		// /////////////////////////////////////////////////////////////////////
		if (dest == null) {
			throw new NullPointerException("The provided destination is null.");
		}

		destination = dest;
		final File temp;
		final URL url;

		// we only accept a directory as a path
		if (dest instanceof String) {
			temp = new File((String) dest);

			// if it exists and it is not a directory that 's not good
			if ((temp.exists() && !temp.isDirectory()) || !temp.exists()) {
				destination = null; // we cannot write
			} else if (!temp.exists()) {
				// well let's create it!
				if (!temp.mkdir()) {
					destination = null;
				} else {
					destination = temp.getAbsolutePath();
				}
			}
		} else if (dest instanceof File) {
			temp = (File) dest;

			if (temp.exists() && !temp.isDirectory()) {
				this.destination = null;
			} else if (!temp.exists()) {
				// let's create it
				if (temp.mkdir()) {
					destination = temp.getAbsolutePath();
				} else {
					destination = null;
				}
			}
		} else if (dest instanceof URL) {
			url = (URL) dest;

			if (url.getProtocol().compareToIgnoreCase("file") != 0) {
				destination = null;
			}

			temp = DataUtilities.urlToFile(url);

			if (temp.exists() && !temp.isDirectory()) {
				destination = null;
			} else if (!temp.exists()) {
				// let's create it
				if (temp.mkdir()) {
					destination = temp.getAbsolutePath();
				} else {
					destination = null;
				}
			}
		} else if (dest instanceof ImageOutputStream) {
			this.destination = dest;
		} else {
			throw new IllegalArgumentException(
					"The provided destination is of an incorrect type.");

		}

		// /////////////////////////////////////////////////////////////////////
		//
		// managing hints
		//
		// /////////////////////////////////////////////////////////////////////
		// //
		//
		// managing hints
		//
		// //
		if (this.hints == null)
			this.hints= new Hints();	
		if (hints != null) {
			// prevent the use from reordering axes
			this.hints.add(hints);
		}
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageWriter#getFormat()
	 */
    public Format getFormat() {
		return new GTopo30Format();
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageWriter#write(org.opengis.coverage.grid.GridCoverage,
	 *      org.opengis.parameter.GeneralParameterValue[])
	 */
	public void write(final GridCoverage coverage,
			final GeneralParameterValue[] params)
			throws java.lang.IllegalArgumentException, java.io.IOException {

		// /////////////////////////////////////////////////////////////////////
		//
		// Checking input params
		//
		// /////////////////////////////////////////////////////////////////////
		if (coverage == null)
			throw new NullPointerException(
					"The provided source coverage is null");
		// the source GridCoverage2D
		GridCoverage2D gc2D = (GridCoverage2D) coverage;

		// /////////////////////////////////////////////////////////////////////
		//
		// Checking writing params
		//
		// /////////////////////////////////////////////////////////////////////
		GeoToolsWriteParams gtParams = null;
		if (params != null) {

			if (params != null) {
				Parameter<?> param;
				final int length = params.length;
				for (int i = 0; i < length; i++) {
					param = (Parameter<?>) params[i];
					if (param.getDescriptor().getName().getCode().equals(
							AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName()
									.toString())) {
						gtParams = (GeoToolsWriteParams) param.getValue();
					}
				}
			}
		}
		if (gtParams == null)
			gtParams = new GTopo30WriteParams();
		// compression
		final boolean compressed = gtParams.getCompressionMode() == ImageWriteParam.MODE_EXPLICIT;
		// write band
		int[] writeBands = gtParams.getSourceBands();
		int writeBand = CoverageUtilities.getVisibleBand(gc2D
				.getRenderedImage());
		if ((writeBands == null || writeBands.length == 0 || writeBands.length > 1)
				&& (writeBand < 0 || writeBand > gc2D.getNumSampleDimensions()))
			throw new IllegalArgumentException(
					"You need to supply a valid index for deciding which band to write.");
		if (!((writeBands == null || writeBands.length == 0 || writeBands.length > 1)))
			writeBand = writeBands[0];

		// destination file name
		String fileName = gc2D.getName().toString();

		// destination XXX HAS to be a dir
		if (compressed) {
			destination = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(new File((File) destination,
							new StringBuffer(fileName).append(".zip")
									.toString()))));
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// STEP 1
		//
		// We might need to do a band select in order to cope with the GTOPO30
		// limitation.
		//
		// /////////////////////////////////////////////////////////////////////
		final ParameterValueGroup pvg = sdFactory.getParameters();
		pvg.parameter("Source").setValue(gc2D);
		pvg.parameter("SampleDimensions").setValue(new int[]{writeBand});
		pvg.parameter("VisibleSampleDimension").setValue(writeBand);
		gc2D = (GridCoverage2D) sdFactory.doOperation(pvg, hints);

		// /////////////////////////////////////////////////////////////////////
		//
		// STEP 2
		//
		// We might need to reformat the selected band for this coverage in
		// order to cope with the GTOPO30 limitation.
		//
		// /////////////////////////////////////////////////////////////////////
		final PlanarImage reFormattedData2Short = reFormatCoverageImage(gc2D,
				DataBuffer.TYPE_SHORT);

		// /////////////////////////////////////////////////////////////////////
		//
		// STEP 2
		//
		// Start with writing things out.
		//
		// /////////////////////////////////////////////////////////////////////
		// //
		//
		// write DEM
		//
		// //
		this.writeDEM(reFormattedData2Short, fileName, destination);

		// //
		//
		// write statistics
		//
		// //
		this.writeStats(reFormattedData2Short, fileName, destination, gc2D);
		// we won't use this image anymore let's release the resources.

		// //
		//
		// write world file
		//
		// //
		this.writeWorldFile(gc2D, fileName, destination);

		// //
		//
		// write projection
		//
		// //
		this.writePRJ(gc2D, fileName, destination);

		// //
		//
		// write HDR
		//
		// //
		this.writeHDR(gc2D, fileName, destination);

		// //
		//
		// write gif
		//
		// //
		this.writeGIF(gc2D, fileName, destination);

		// //
		//
		// write src
		//
		// //
		this.writeSRC(gc2D, fileName, destination);

		if (compressed) {

			((ZipOutputStream) destination).close();
		}
	}

	/**
	 * Reformats the input single banded planar image to having
	 * {@link DataBuffer#TYPE_SHORT} sample type as requested by the GTOPO30
	 * format.
	 * 
	 * @param gc2D
	 *            source {@link GridCoverage2D} from which to take the input
	 *            {@link PlanarImage}.
	 * 
	 * @return the reformated {@link PlanarImage}.
	 */
	private PlanarImage reFormatCoverageImage(final GridCoverage2D gc2D,
			int writeBand) {
		// internal image
		PlanarImage image = (PlanarImage) gc2D.getRenderedImage();
		// sample dimension type
		final int origDataType = image.getSampleModel().getDataType();
		// short?
		if (DataBuffer.TYPE_SHORT == origDataType) {
			return image;
		}

		final GridSampleDimension visibleSD = ((GridSampleDimension) gc2D
				.getSampleDimension(0)).geophysics(true);

		// getting categories
		final List oldCategories = visibleSD.getCategories();

		// removing old nodata category
		// candidate
		Category candidate = null;
		NumberRange candidateRange = null;
		final Iterator it = oldCategories.iterator();

		while (it.hasNext()) {
			candidate = (Category) it.next();

			// removing candidate for NaN
			if (candidate.getName().toString().equalsIgnoreCase("no data")) {
				candidateRange = candidate.getRange();

				break;
			}
		}

		// new no data category
		final double oldNoData = candidateRange.getMinimum();
		final ParameterBlockJAI pbjM = new ParameterBlockJAI(
				"org.geotools.gce.gtopo30.NoDataReplacer");
		pbjM.addSource(image);
		pbjM.setParameter("oldNoData", oldNoData);
		image = JAI.create("org.geotools.gce.gtopo30.NoDataReplacer", pbjM,
				hints);
		return image;
	}

	/**
	 * Writing down the header file for the gtopo30 format:
	 * 
	 * @param coverage
	 *            The GridCoverage to write
	 * @param file
	 *            The destination object (can be a File or ZipOutputStream)
	 * 
	 * @throws IOException
	 *             If the file could not be written
	 */
	private void writeHDR(final GridCoverage2D gc, final String name,
			final Object dest) throws IOException {

		// final GeneralEnvelope envelope = (GeneralEnvelope) gc.getEnvelope();
		final double noData = -9999.0;

			// checking the directions of the axes.
			// we need to understand how the axes of this gridcoverage are
			// specified
	/*
	 * Note from Martin: I suggest to replace all the above lines by the commented code below.
	 * The change need to be tested in order to make sure that I didn't made a mistake in the
	 * mathematic. Note that 'lonFirst' totally vanish.
	 */
			final AffineTransform gridToWorld = (AffineTransform) gc
					.getGridGeometry().getGridToCRS2D();
			boolean lonFirst = (XAffineTransform.getSwapXY(gridToWorld) != -1);
	
			final double geospatialDx = Math.abs((lonFirst) ? gridToWorld
					.getScaleX() : gridToWorld.getShearY());
			final double geospatialDy = Math.abs((lonFirst) ? gridToWorld
					.getScaleY() : gridToWorld.getShearX());
	
			// getting corner coordinates of the left upper corner
			final double xUpperLeft = lonFirst ? gridToWorld.getTranslateX()
					: gridToWorld.getTranslateY();
			final double yUpperLeft = lonFirst ? gridToWorld.getTranslateY()
					: gridToWorld.getTranslateX();
	/*
				final AffineTransform worldToGrid = (AffineTransform) gc
						.getGridGeometry().getGridToCoordinateSystem().inverse();
	
				final double geospatialDx = 1 / XAffineTransform.getScaleX0(worldToGrid);
				final double geospatialDy = 1 / XAffineTransform.getScaleY0(worldToGrid);
	
				// getting corner coordinates of the left upper corner
				final double xUpperLeft = -worldToGrid.getTranslateX() * geospatialDx;
				final double yUpperLeft = -worldToGrid.getTranslateY() * geospatialDy;
	*/
	
			// calculating the physical resolution over x and y.
			final int geometryWidth = gc.getGridGeometry().getGridRange().getSpan(0);
			final int geometryHeight = gc.getGridGeometry().getGridRange().getSpan(1);
	
			if (dest instanceof File) {
	
				final PrintWriter out = new PrintWriter(
						new BufferedOutputStream(new FileOutputStream(new File(
								(File) dest, new StringBuffer(name).append(
										".HDR").toString()))));
	
				// output header and assign header fields
				out.print("BYTEORDER");
				out.print(" ");
				out.println("M");
	
				out.print("LAYOUT");
				out.print(" ");
				out.println("BIL");
	
				out.print("NROWS");
				out.print(" ");
				out.println(geometryHeight);
	
				out.print("NCOLS");
				out.print(" ");
				out.println(geometryWidth);
	
				out.print("NBANDS");
				out.print(" ");
				out.println("1");
	
				out.print("NBITS");
				out.print(" ");
				out.println("16");
	
				out.print("BANDROWBYTES");
				out.print(" ");
				out.println(geometryWidth * 2);
	
				out.print("TOTALROWBYTES");
				out.print(" ");
				out.println(geometryWidth * 2);
	
				out.print("BANDGAPBYTES");
				out.print(" ");
				out.println(0);
	
				out.print("NODATA");
				out.print(" ");
				out.println((int) noData);
	
				out.print("ULXMAP");
				out.print(" ");
				out.println(xUpperLeft);
	
				out.print("ULYMAP");
				out.print(" ");
				out.println(yUpperLeft);
	
				out.print("XDIM");
				out.print(" ");
				out.println(geospatialDx);
	
				out.print("YDIM");
				out.print(" ");
				out.println(geospatialDy);
				out.flush();
				out.close();
			} else {
				final ZipOutputStream outZ = (ZipOutputStream) dest;
				final ZipEntry e = new ZipEntry(gc.getName().toString()
						+ ".HDR");
				outZ.putNextEntry(e);
	
				// writing world file
				outZ.write("BYTEORDER".getBytes());
				outZ.write(" ".getBytes());
				outZ.write("M".getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("LAYoutZ".getBytes());
				outZ.write(" ".getBytes());
				outZ.write("BIL".getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("NROWS".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Integer.toString(geometryHeight).getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("NCOLS".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Integer.toString(geometryWidth).getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("NBANDS".getBytes());
				outZ.write(" ".getBytes());
				outZ.write("1".getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("NBITS".getBytes());
				outZ.write(" ".getBytes());
				outZ.write("16".getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("BANDROWBYTES".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Integer.toString(geometryWidth * 2).getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("TOTALROWBYTES".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Integer.toString(geometryWidth * 2).getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("BANDGAPBYTES".getBytes());
				outZ.write(" ".getBytes());
				outZ.write("0".getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("NODATA".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Integer.toString((int) noData).getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("ULXMAP".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Double.toString(xUpperLeft + (geospatialDx / 2))
						.getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("ULYMAP".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Double.toString(yUpperLeft - (geospatialDy / 2))
						.getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("XDIM".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Double.toString(geospatialDx).getBytes());
				outZ.write("\n".getBytes());
	
				outZ.write("YDIM".getBytes());
				outZ.write(" ".getBytes());
				outZ.write(Double.toString(geospatialDy).toString().getBytes());
				outZ.write("\n".getBytes());
	
				outZ.closeEntry();
	
				((ZipOutputStream) dest).closeEntry();
			}
	}

	/**
	 * Writes the source file (.SRC). The default byte order is BIG_ENDIAN.
	 * 
	 * @param gc
	 *            The GridCoverage to write
	 * @param file
	 *            The destination object (can be a File or ZipOutputStream)
	 * @param dest
	 * 
	 * @throws FileNotFoundException
	 *             If the destination file could not be found
	 * @throws IOException
	 *             If the file could not be written
	 */
	private void writeSRC(GridCoverage2D gc, final String name, Object dest)
			throws FileNotFoundException, IOException {

		// /////////////////////////////////////////////////////////////////////
		// TODO @task @todo
		// Here I am making the assumption that the non geophysiscs view is 8
		// bit but it can also be 16. I should do something more general like a
		// clamp plus a format but for the moment this is enough.
		//
		// We need also to get the one visible band
		//
		// /////////////////////////////////////////////////////////////////////
		gc = gc.geophysics(false);
		ImageOutputStream out = null;

		RenderedImage image = gc.getRenderedImage();
		if (dest instanceof File) {
			final File file = new File((File) dest, new StringBuffer(name).append(".SRC").toString());
            out = ImageIOExt.createImageOutputStream(image, file);
		} else {
			final ZipOutputStream outZ = (ZipOutputStream) dest;
			final ZipEntry e = new ZipEntry(gc.getName().toString() + ".SRC");
			outZ.putNextEntry(e);

			out = ImageIOExt.createImageOutputStream(image, outZ);
		}

		// setting byte order
		out.setByteOrder(java.nio.ByteOrder.BIG_ENDIAN);

		// /////////////////////////////////////////////////////////////////////
		//
		// Prepare to write
		//
		// /////////////////////////////////////////////////////////////////////
		image = untileImage(image);

		final ParameterBlockJAI pbj = new ParameterBlockJAI("imagewrite");
		pbj.addSource(image);
		pbj.setParameter("Format", "raw");
		pbj.setParameter("Output", out);
		final RenderedOp wOp = JAI.create("ImageWrite", pbj);

		// /////////////////////////////////////////////////////////////////////
		//
		// Dispose things
		//
		// /////////////////////////////////////////////////////////////////////
		final Object o = wOp
				.getProperty(ImageWriteDescriptor.PROPERTY_NAME_IMAGE_WRITER);
		if (o instanceof ImageWriter)
			((ImageWriter) o).dispose();

		if (!(dest instanceof File)) {
			((ZipOutputStream) dest).closeEntry();
		}
		out.flush();
		out.close();

	}

	/**
	 * Writing a gif file as an overview for this GTopo30.
	 * 
	 * @param gc
	 *            The GridCoverage to write
	 * @param file
	 *            The destination object (can be a File or ZipOutputStream)
	 * @param dest
	 * 
	 * @throws IOException
	 *             If the file could not be written
	 */
	private void writeGIF(final GridCoverage2D gc, final String name,
			Object dest) throws IOException {
		ImageOutputStream out = null;

	      // rescaling to a smaller resolution in order to save space on storage
        final GridCoverage2D gc1 = rescaleCoverage(gc);

        // get the non-geophysiscs view
        final GridCoverage2D gc2 = gc1.geophysics(false);

        // get the underlying image
        final RenderedImage image = gc2.getRenderedImage();

        // get the image out stream
		if (dest instanceof File) {
			// writing gif image
			final File file = new File((File) dest,
					new StringBuffer(name).append(".GIF").toString());
            out = ImageIOExt.createImageOutputStream(image, file);
		} else {
			final ZipOutputStream outZ = (ZipOutputStream) dest;
			final ZipEntry e = new ZipEntry(gc.getName().toString() + ".GIF");
			outZ.putNextEntry(e);

			out = ImageIOExt.createImageOutputStream(image, outZ);
		}

		// write it down as a gif
		final ParameterBlockJAI pbj = new ParameterBlockJAI("ImageWrite");
		pbj.addSource(image);
		pbj.setParameter("Output", out);
		pbj.setParameter("Format", "gif");
		JAI.create("ImageWrite", pbj, new RenderingHints(JAI.KEY_TILE_CACHE,
				null));

		if (dest instanceof File) {
			out.close();
		} else {
			out.flush();
			((ZipOutputStream) dest).closeEntry();
		}

		// disposing the old unused coverages
		gc1.dispose(false);
	}

	/**
	 * Purpose of this method is rescaling the original coverage in order to
	 * create an overview for the shaded relief gif image to be put inside the
	 * gtopo30 set of files.
	 * 
	 * @param gc
	 *            the original {@link GridCoverage2D}.
	 * 
	 * @return the rescaled {@link GridCoverage2D}.
	 */
	private GridCoverage2D rescaleCoverage(GridCoverage2D gc) {
		final RenderedImage image = gc.getRenderedImage();
		final int width = image.getWidth();
		final int height = image.getHeight();

		if ((height < GIF_HEIGHT) && (width < GIF_WIDTH)) {
			return gc;
		}

		// new grid range
		final GeneralGridEnvelope newGridrange = new GeneralGridEnvelope(new int[] {0, 0 }, new int[] { GIF_WIDTH, GIF_HEIGHT });
		final GridGeometry2D newGridGeometry = new GridGeometry2D(newGridrange,gc.getEnvelope());

		// resample this coverage
		final ParameterValueGroup pvg= resampleFactory.getParameters();
		pvg.parameter("Source").setValue(gc);
		pvg.parameter("GridGeometry").setValue(newGridGeometry);
		pvg.parameter("InterpolationType").setValue(new InterpolationBilinear());
		return (GridCoverage2D) resampleFactory.doOperation(pvg, hints);

	}

	/**
	 * Write a projection file (.PRJ) using wkt
	 * 
	 * @param gc
	 *            The GridCoverage to write
	 * @param file
	 *            The destination object (can be a File or ZipOutputStream)
	 * @param dest
	 * 
	 * @throws IOException
	 *             If the file could not be written
	 */
	private void writePRJ(final GridCoverage2D gc, String name, Object dest)
			throws IOException {
		if (dest instanceof File) {
			// create the file
			final BufferedWriter fileWriter = new BufferedWriter(
					new FileWriter(new File((File) dest, new StringBuffer(name)
							.append(".PRJ").toString())));

			// write information on crs
			fileWriter.write(gc.getCoordinateReferenceSystem().toWKT());
			fileWriter.close();
		} else {
			final ZipOutputStream out = (ZipOutputStream) dest;
			final ZipEntry e = new ZipEntry(new StringBuffer(gc.getName()
					.toString()).append(".PRJ").toString());
			out.putNextEntry(e);
			out.write(gc.getCoordinateReferenceSystem().toWKT().getBytes());
			out.closeEntry();
		}
	}

	/**
	 * Writes the stats file (.STX).
	 * 
	 * @param image
	 *            The GridCoverage to write
	 * @param file
	 *            The destination object (can be a File or ZipOutputStream)
	 * @param gc
	 *            DOCUMENT ME!
	 * 
	 * @throws IOException
	 *             If the file could not be written
	 */
	private void writeStats(final PlanarImage image, String name, Object dest,
			final GridCoverage2D gc) throws IOException {
		ParameterBlock pb = new ParameterBlock();
		// /////////////////////////////////////////////////////////////////////
		//
		// we need to evaluate stats first using jai
		//
		// /////////////////////////////////////////////////////////////////////
		final double[] Max = new double[] { Short.MAX_VALUE };//we should encode more than the maximum short anyway
		final double[] Min = new double[] { Short.MIN_VALUE };//we should avoid No Data values TODO

		// histogram
		pb.addSource(image);
		pb.add(null); // no roi
		pb.add(1);
		pb.add(1);
		pb.add(new int[] { (int) (Max[0] - Min[0] + 1) });
		pb.add(Min);
		pb.add(Max);
		pb.add(1);

		// /////////////////////////////////////////////////////////////////////
		//
		// Create the histogram
		//
		// /////////////////////////////////////////////////////////////////////
		final PlanarImage histogramImage = JAI.create("histogram", pb,
				new RenderingHints(JAI.KEY_TILE_CACHE, null));
		final Histogram hist = (Histogram) histogramImage
				.getProperty("histogram");
		pb.removeParameters();
		pb.removeSources();

		// /////////////////////////////////////////////////////////////////////
		//
		// Write things
		//
		// /////////////////////////////////////////////////////////////////////
		if (dest instanceof File) {
			// files destinations
			// write statistics
			if (dest instanceof File) {
				dest = new File((File) dest, new StringBuffer(name).append(
						".STX").toString());
			}
			// writing world file
			final PrintWriter p = new PrintWriter(new FileOutputStream(
					((File) dest)));
			p.print(1);
			p.print(" ");
			p.print((int) Min[0]);
			p.print(" ");
			p.print((int) Max[0]);
			p.print(" ");
			p.print(hist.getMean()[0]);
			p.print(" ");
			p.print(hist.getStandardDeviation()[0]);
			p.close();
		} else {
			final ZipOutputStream outZ = (ZipOutputStream) dest;
			final ZipEntry e = new ZipEntry(name + ".STX");
			outZ.putNextEntry(e);

			// writing world file
			outZ.write("1".getBytes());
			outZ.write(" ".getBytes());
			outZ.write(new Integer((int) Min[0]).toString().getBytes());
			outZ.write(" ".getBytes());
			outZ.write(new Integer((int) Max[0]).toString().getBytes());
			outZ.write(" ".getBytes());
			outZ.write(new Double(hist.getMean()[0]).toString().getBytes());
			outZ.write(" ".getBytes());
			outZ.write(new Double(hist.getStandardDeviation()[0]).toString()
					.getBytes());
			((ZipOutputStream) dest).closeEntry();
		}

		histogramImage.dispose();
	}

	/**
	 * Writes the world file (.DMW)
	 * 
	 * @param gc
	 *            The GridCoverage to write
	 * @param worldFile
	 *            The destination world file (can be a file or a
	 *            ZipOutputStream)
	 * 
	 * @throws IOException
	 *             if the file could not be written
	 */
	private void writeWorldFile(final GridCoverage2D gc, String name,
			Object dest) throws IOException {
		// final RenderedImage image = (PlanarImage) gc.getRenderedImage();

		/**
		 * It is worth to point out that here I build the values using the axes
		 * order specified in the CRs which can be either LAT,LON or LON,LAT.
		 * This is important since we ned to know how to assocaite the
		 * underlying raster dimensions with the envelope which is in CRS
		 * values.
		 */

		// /////////////////////////////////////////////////////////////////////
		//
		// trying to understand the direction of the first axis in order to
		// understand how to associate the value to the crs.
		//
		// /////////////////////////////////////////////////////////////////////
		final AffineTransform gridToWorld = (AffineTransform) gc
				.getGridGeometry().getGridToCRS2D();
		boolean lonFirst = (XAffineTransform.getSwapXY(gridToWorld) != -1);

		// /////////////////////////////////////////////////////////////////////
		//
		// associate value to crs
		//
		// /////////////////////////////////////////////////////////////////////
		final double xPixelSize = (lonFirst) ? gridToWorld.getScaleX()
				: gridToWorld.getShearY();
		final double rotation1 = (lonFirst) ? gridToWorld.getShearX()
				: gridToWorld.getScaleY();
		final double rotation2 = (lonFirst) ? gridToWorld.getShearY()
				: gridToWorld.getScaleX();
		final double yPixelSize = (lonFirst) ? gridToWorld.getScaleY()
				: gridToWorld.getShearX();
		final double xLoc = lonFirst ? gridToWorld.getTranslateX()
				: gridToWorld.getTranslateY();
		final double yLoc = lonFirst ? gridToWorld.getTranslateY()
				: gridToWorld.getTranslateX();
		if (dest instanceof File) {

			dest = new File((File) dest, new StringBuffer(name).append(
					".DMW").toString());
			// writing world file
			final PrintWriter out = new PrintWriter(new FileOutputStream(
					(File) dest));
			out.println(xPixelSize);
			out.println(rotation1);
			out.println(rotation2);
			out.println(yPixelSize);
			out.println(xLoc);
			out.println(yLoc);
			out.close();
		} else {
			final ZipOutputStream outZ = (ZipOutputStream) dest;
			final ZipEntry e = new ZipEntry(gc.getName().toString()
					+ ".DMW");
			outZ.putNextEntry(e);

			// writing world file
			outZ.write(Double.toString(xPixelSize).getBytes());
			outZ.write("\n".getBytes());
			outZ.write(Double.toString(rotation1).toString().getBytes());
			outZ.write("\n".getBytes());
			outZ.write(Double.toString(rotation2).toString().getBytes());
			outZ.write("\n".getBytes());
			outZ.write(Double.toString(xPixelSize).toString().getBytes());
			outZ.write("\n".getBytes());
			outZ.write(Double.toString(yPixelSize).toString().getBytes());
			outZ.write("\n".getBytes());
			outZ.write(Double.toString(xLoc).toString().getBytes());
			outZ.write("\n".getBytes());
			outZ.write(Double.toString(yLoc).toString().getBytes());
			outZ.write("\n".getBytes());
			((ZipOutputStream) dest).closeEntry();
		}
	}

	/**
	 * Writes the digital elevation model file (.DEM). The default byte order is
	 * BIG_ENDIAN.
	 * 
	 * @param image
	 *            The GridCoverage object to write
	 * @param name
	 *            DOCUMENT ME!
	 * @param dest
	 *            The destination object (can be a File or a ZipOutputStream)
	 * 
	 * @throws FileNotFoundException
	 *             If the destination file could not be found
	 * @throws IOException
	 *             If the file could not be written
	 */
	private void writeDEM(PlanarImage image, final String name, Object dest)
			throws FileNotFoundException, IOException {
		ImageOutputStream out;

		if (dest instanceof File) {
			// write statistics
			dest = new File((File) dest, new StringBuffer(name).append(
						".DEM").toString());
			out = ImageIOExt.createImageOutputStream(image, (File) dest);
		} else {
			final ZipOutputStream outZ = (ZipOutputStream) dest;
			final ZipEntry e = new ZipEntry(name + ".DEM");
			outZ.putNextEntry(e);
			out = ImageIOExt.createImageOutputStream(image, outZ);
		}

		out.setByteOrder(java.nio.ByteOrder.BIG_ENDIAN);

		// untile the image in case it is tiled
		// otherwise we could add tiles which are unexistant in the original
		// data
		// generating failures when reading back the data again.
		image = untileImage(image);

		// requesting an imageio writer
		// setting tile parameters in order to tile the image on the disk
		final ParameterBlockJAI pbjW = new ParameterBlockJAI("ImageWrite");
		pbjW.addSource(image);
		pbjW.setParameter("Format", "raw");
		pbjW.setParameter("Output", out);
		JAI.create("ImageWrite", pbjW);

		if (dest instanceof File) {
			out.flush();
			out.close();
		} else {
			((ZipOutputStream) dest).closeEntry();
		}
	}

	/**
	 * This method has the objective of untiling the final image to write on
	 * disk since we do not want to have tiles added to the file on disk causing
	 * failures when reading it back into memory.
	 * 
	 * @param image
	 *            Image to untile.
	 * 
	 * @return Untiled image.
	 */
	private PlanarImage untileImage(RenderedImage image) {
		final ParameterBlockJAI pbj = new ParameterBlockJAI("format");
		pbj.addSource(image);
		pbj.setParameter("dataType", image.getSampleModel().getTransferType());

		final ImageLayout layout = new ImageLayout(image);
		layout.unsetTileLayout();
		layout.setTileGridXOffset(0);
		layout.setTileGridYOffset(0);
		layout.setTileHeight(image.getHeight());
		layout.setTileWidth(image.getWidth());
		layout.setValid(ImageLayout.TILE_GRID_X_OFFSET_MASK
				| ImageLayout.TILE_GRID_Y_OFFSET_MASK
				| ImageLayout.TILE_HEIGHT_MASK | ImageLayout.TILE_WIDTH_MASK);

		final RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,
				layout);
		// avoid caching this image

		return JAI.create("format", pbj, hints);
	}

}
