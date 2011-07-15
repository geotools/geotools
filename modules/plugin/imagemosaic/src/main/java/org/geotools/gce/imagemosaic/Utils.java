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
package org.geotools.gce.imagemosaic;

import it.geosolutions.imageio.stream.input.spi.URLImageInputStreamSpi;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.Histogram;
import javax.media.jai.Interpolation;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.remote.SerializableRenderedImage;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder.ExceptionEvent;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilder.ProcessingEvent;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;

import com.sun.media.jai.operator.ImageReadDescriptor;
import com.sun.media.jai.util.Rational;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Sparse utilities for the various mosaic classes. I use them to extract
 * complex code from other places.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/main/java/org/geotools/gce/imagemosaic/Utils.java $
 */
public class Utils {
    
    /** EHCache instance to cache histograms */ 
    private static Cache ehcache;    
    
    /** RGB to GRAY coefficients (for Luminance computation) */
    public final static double RGB_TO_GRAY_MATRIX [][]= {{ 0.114, 0.587, 0.299, 0 }};
    
    /** 
     * Flag indicating whether to compute optimized crop ops (instead of standard
     * mosaicking op) when possible (As an instance when mosaicking a single granule) 
     */
    final static boolean OPTIMIZE_CROP; 
        
    static {
        final String prop = System.getProperty("org.geotools.imagemosaic.optimizecrop");
        if (prop != null && prop.equalsIgnoreCase("FALSE")){
            OPTIMIZE_CROP = false;
        } else {
            OPTIMIZE_CROP = true;
        }
    }
    
    static class Prop {
        final static String LOCATION_ATTRIBUTE = "LocationAttribute";
        final static String ENVELOPE2D = "Envelope2D";
        final static String LEVELS_NUM = "LevelsNum";
        final static String LEVELS = "Levels";
        final static String SUGGESTED_SPI = "SuggestedSPI";
        final static String EXP_RGB = "ExpandToRGB";
        final static String ABSOLUTE_PATH = "AbsolutePath";
        final static String NAME = "Name";
        final static String FOOTPRINT_MANAGEMENT = "FootprintManagement";
        final static String HETEROGENEOUS = "Heterogeneous";
        static final String TIME_ATTRIBUTE = "TimeAttribute";
        static final String ELEVATION_ATTRIBUTE = "ElevationAttribute";
        final static String CACHING= "Caching";
        static final String RUN_TIME = "RuntimeAttribute";
    }
    // FORMULAE FOR FORWARD MAP are derived as follows
    //     Nearest
    //        Minimum:
    //            srcMin = floor ((dstMin + 0.5 - trans) / scale)
    //            srcMin <= (dstMin + 0.5 - trans) / scale < srcMin + 1
    //            srcMin*scale <= dstMin + 0.5 - trans < (srcMin + 1)*scale
    //            srcMin*scale - 0.5 + trans
    //                       <= dstMin < (srcMin + 1)*scale - 0.5 + trans
    //            Let A = srcMin*scale - 0.5 + trans,
    //            Let B = (srcMin + 1)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            Note that srcMax is defined to be srcMin + dimension - 1
    //            srcMax = floor ((dstMax + 0.5 - trans) / scale)
    //            srcMax <= (dstMax + 0.5 - trans) / scale < srcMax + 1
    //            srcMax*scale <= dstMax + 0.5 - trans < (srcMax + 1)*scale
    //            srcMax*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1) * scale - 0.5 + trans
    //            Let float A = (srcMax + 1) * scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    //     Other interpolations
    //
    //        First the source should be shrunk by the padding that is
    //        required for the particular interpolation. Then the
    //        shrunk source should be forward mapped as follows:
    //
    //        Minimum:
    //            srcMin = floor (((dstMin + 0.5 - trans)/scale) - 0.5)
    //            srcMin <= ((dstMin + 0.5 - trans)/scale) - 0.5 < srcMin+1
    //            (srcMin+0.5)*scale <= dstMin+0.5-trans <
    //                                                  (srcMin+1.5)*scale
    //            (srcMin+0.5)*scale - 0.5 + trans
    //                       <= dstMin < (srcMin+1.5)*scale - 0.5 + trans
    //            Let A = (srcMin+0.5)*scale - 0.5 + trans,
    //            Let B = (srcMin+1.5)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            srcMax is defined as srcMin + dimension - 1
    //            srcMax = floor (((dstMax + 0.5 - trans) / scale) - 0.5)
    //            srcMax <= ((dstMax + 0.5 - trans)/scale) - 0.5 < srcMax+1
    //            (srcMax+0.5)*scale <= dstMax + 0.5 - trans <
    //                                                   (srcMax+1.5)*scale
    //            (srcMax+0.5)*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1.5)*scale - 0.5 + trans
    //            Let float A = (srcMax+1.5)*scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    private static float rationalTolerance = 0.000001F;
    static Rectangle2D layoutHelper(RenderedImage source,
                                            float scaleX,
                                            float scaleY,
                                            float transX,
                                            float transY,
                                            Interpolation interp) {

        // Represent the scale factors as Rational numbers.
                // Since a value of 1.2 is represented as 1.200001 which
                // throws the forward/backward mapping in certain situations.
                // Convert the scale and translation factors to Rational numbers
                Rational scaleXRational = Rational.approximate(scaleX,rationalTolerance);
                Rational scaleYRational = Rational.approximate(scaleY,rationalTolerance);

                long scaleXRationalNum = (long) scaleXRational.num;
                long scaleXRationalDenom = (long) scaleXRational.denom;
                long scaleYRationalNum = (long) scaleYRational.num;
                long scaleYRationalDenom = (long) scaleYRational.denom;

                Rational transXRational = Rational.approximate(transX,rationalTolerance);
                Rational transYRational = Rational.approximate(transY,rationalTolerance);

                long transXRationalNum = (long) transXRational.num;
                long transXRationalDenom = (long) transXRational.denom;
                long transYRationalNum = (long) transYRational.num;
                long transYRationalDenom = (long) transYRational.denom;

                int x0 = source.getMinX();
                int y0 = source.getMinY();
                int w = source.getWidth();
                int h = source.getHeight();

                // Variables to store the calculated destination upper left coordinate
                long dx0Num, dx0Denom, dy0Num, dy0Denom;

                // Variables to store the calculated destination bottom right
                // coordinate
                long dx1Num, dx1Denom, dy1Num, dy1Denom;

                // Start calculations for destination

                dx0Num = x0;
                dx0Denom = 1;

                dy0Num = y0;
                dy0Denom = 1;

                // Formula requires srcMaxX + 1 = (x0 + w - 1) + 1 = x0 + w
                dx1Num = x0 + w;
                dx1Denom = 1;

                // Formula requires srcMaxY + 1 = (y0 + h - 1) + 1 = y0 + h
                dy1Num = y0 + h;
                dy1Denom = 1;

                dx0Num *= scaleXRationalNum;
                dx0Denom *= scaleXRationalDenom;

                dy0Num *= scaleYRationalNum;
                dy0Denom *= scaleYRationalDenom;

                dx1Num *= scaleXRationalNum;
                dx1Denom *= scaleXRationalDenom;

                dy1Num *= scaleYRationalNum;
                dy1Denom *= scaleYRationalDenom;

                // Equivalent to subtracting 0.5
                dx0Num = 2 * dx0Num - dx0Denom;
                dx0Denom *= 2;

                dy0Num = 2 * dy0Num - dy0Denom;
                dy0Denom *= 2;

                // Equivalent to subtracting 1.5
                dx1Num = 2 * dx1Num - 3 * dx1Denom;
                dx1Denom *= 2;

                dy1Num = 2 * dy1Num - 3 * dy1Denom;
                dy1Denom *= 2;

                // Adding translation factors

                // Equivalent to float dx0 += transX
                dx0Num = dx0Num * transXRationalDenom + transXRationalNum * dx0Denom;
                dx0Denom *= transXRationalDenom;

                // Equivalent to float dy0 += transY
                dy0Num = dy0Num * transYRationalDenom + transYRationalNum * dy0Denom;
                dy0Denom *= transYRationalDenom;

                // Equivalent to float dx1 += transX
                dx1Num = dx1Num * transXRationalDenom + transXRationalNum * dx1Denom;
                dx1Denom *= transXRationalDenom;

                // Equivalent to float dy1 += transY
                dy1Num = dy1Num * transYRationalDenom + transYRationalNum * dy1Denom;
                dy1Denom *= transYRationalDenom;

                // Get the integral coordinates
                int l_x0, l_y0, l_x1, l_y1;

                l_x0 = Rational.ceil(dx0Num, dx0Denom);
                l_y0 = Rational.ceil(dy0Num, dy0Denom);

                l_x1 = Rational.ceil(dx1Num, dx1Denom);
                l_y1 = Rational.ceil(dy1Num, dy1Denom);

                // Set the top left coordinate of the destination
                final Rectangle2D retValue= new Rectangle2D.Double();
                retValue.setFrame(l_x0, l_y0, l_x1 - l_x0 + 1, l_y1 - l_y0 + 1);
                return retValue;
        }
    
	/**
	 * Logger.
	 */
	private final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(Utils.class.toString());
	/**
	 * Default wildcard for creating mosaics.
	 */
	public static final String DEFAULT_WILCARD = "*.*";

	/**
	 * Default path behavior with respect to absolute paths.
	 */
	public static final boolean DEFAULT_PATH_BEHAVIOR = false;

	/**
	 * Default path behavior with respect to index caching.
	 */
	private static final boolean DEFAULT_CACHING_BEHAVIOR = false;

	/**
	 * Cached instance of {@link URLImageInputStreamSpi} for creating
	 * {@link ImageInputStream} instances.
	 */
	private static ImageInputStreamSpi CACHED_STREAM_SPI = new URLImageInputStreamSpi();
	
	/**
	 * Creates a mosaic for the provided input parameters.
	 * 
	 * @param location
	 *            path to the directory where to gather the elements for the
	 *            mosaic.
	 * @param indexName
	 *            name to give to this mosaic
	 * @param wildcard
	 *            wildcard to use for walking through files. We are using
	 *            commonsIO for this task
	 * @param absolutePath
	 * 			  tells the catalogue builder to use absolute paths.
	 * @param hints hints to control reader instantiations
	 * @return <code>true</code> if everything is right, <code>false</code>if
	 *         something bad happens, in which case the reason should be logged
	 *         to the logger.
	 */
	static boolean createMosaic(
			final String location, 
			final String indexName,
			final String wildcard, 
			final boolean absolutePath, 
			final Hints hints) {

		// create a mosaic index builder and set the relevant elements
		final CatalogBuilderConfiguration configuration = new CatalogBuilderConfiguration();
		configuration.setAbsolute(absolutePath);
		configuration.setHints(hints);
		configuration.setRootMosaicDirectory(location);
		configuration.setIndexingDirectories(Arrays.asList(location));
		configuration.setIndexName(indexName);

		// look for and indexed.properties file
		final File parent = new File(location);
		final File indexerProperties = new File(parent, "indexer.properties");
		if (Utils.checkFileReadable(indexerProperties)) {
			// load it and parse it
			final Properties props = Utils.loadPropertiesFromURL(DataUtilities
					.fileToURL(indexerProperties));

			// name
			if (props.containsKey("Name"))
				configuration.setIndexName(props.getProperty("Name"));

			// absolute
			if (props.containsKey("Absolute"))
				configuration.setAbsolute(Boolean.getBoolean(props
						.getProperty("Absolute")));

			// recursive
			if (props.containsKey("Recursive"))
				configuration.setRecursive(Boolean.valueOf(props
						.getProperty("Recursive")));

			// wildcard
			if (props.containsKey("Wildcard"))
				configuration.setWildcard(props.getProperty("Wildcard"));

			// schema
			if (props.containsKey("Schema"))
				configuration.setSchema(props.getProperty("Schema"));

			// time attr
			if (props.containsKey("TimeAttribute"))
				configuration.setTimeAttribute(props.getProperty("TimeAttribute"));
			
			// elevation attr
			if (props.containsKey("ElevationAttribute"))
				configuration.setElevationAttribute(props.getProperty("ElevationAttribute"));			
			
			// runtime attr
			if (props.containsKey("RuntimeAttribute"))
				configuration.setRuntimeAttribute(props.getProperty("RuntimeAttribute"));
			
			// imposed BBOX
			if (props.containsKey("Envelope2D"))
				configuration.setEnvelope2D(props.getProperty("Envelope2D"));	
			
			// imposed Pyramid Layout
			if (props.containsKey("ResolutionLevels"))
				configuration.setResolutionLevels(props.getProperty("ResolutionLevels"));			

			// collectors
			if (props.containsKey("PropertyCollectors"))
				configuration.setPropertyCollectors(props.getProperty("PropertyCollectors"));
			
			if (props.containsKey("Caching"))
				configuration.setCaching(Boolean.valueOf(props.getProperty("Caching")));
		}

		// create the builder
		final CatalogBuilder catalogBuilder = new CatalogBuilder(configuration);
		// this is going to help us with catching exceptions and logging them
		final Queue<Throwable> exceptions = new LinkedList<Throwable>();
		try {

			final CatalogBuilder.ProcessingEventListener listener = new CatalogBuilder.ProcessingEventListener() {

				@Override
				public void exceptionOccurred(ExceptionEvent event) {
					final Throwable t = event.getException();
					exceptions.add(t);
					if (LOGGER.isLoggable(Level.SEVERE))
						LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);

				}

				@Override
				public void getNotification(ProcessingEvent event) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.fine(event.getMessage());

				}

			};
			catalogBuilder.addProcessingEventListener(listener);
			catalogBuilder.run();
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, "Unable to build mosaic", e);
			return false;
		} finally {
			catalogBuilder.dispose();
		}

		// check that nothing bad happened
		if (exceptions.size() > 0)
			return false;
		return true;
	}

	public static String getMessageFromException(Exception exception) {
		if (exception.getLocalizedMessage() != null)
			return exception.getLocalizedMessage();
		else
			return exception.getMessage();
	}

	static URL checkSource(Object source) throws MalformedURLException,
			DataSourceException {
		return checkSource(source, null);
	}

	

	static MosaicConfigurationBean loadMosaicProperties(final URL sourceURL,
                final String defaultLocationAttribute) {
	    return loadMosaicProperties(sourceURL, defaultLocationAttribute, null);
	}
	
	static MosaicConfigurationBean loadMosaicProperties(
	        final URL sourceURL,
		final String defaultLocationAttribute, 
		final Set<String> ignorePropertiesSet) {
		// ret value
		final MosaicConfigurationBean retValue = new MosaicConfigurationBean();
		final boolean ignoreSome = ignorePropertiesSet != null && !ignorePropertiesSet.isEmpty();

		//
		// load the properties file
		//
		URL propsURL = sourceURL;
		if (!sourceURL.toExternalForm().endsWith(".properties"))
			propsURL = DataUtilities.changeUrlExt(sourceURL, "properties");
		final Properties properties = loadPropertiesFromURL(propsURL);
		if (properties == null) {
			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("Unable to load mosaic properties file");
			return null;
		}

		String[] pairs = null;
		String pair[] = null;
		
		//
		// imposed bbox is optional
		//              
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ENVELOPE2D)) {
			String bboxString = properties.getProperty(Prop.ENVELOPE2D, null);
			if(bboxString!=null){
				bboxString=bboxString.trim();
				try{
					ReferencedEnvelope bbox = parseEnvelope(bboxString);
					if(bbox!=null)
						retValue.setEnvelope(bbox);
					else
						if (LOGGER.isLoggable(Level.INFO))
							LOGGER.info("Cannot parse imposed bbox.");
				}catch (Exception e) {
					if (LOGGER.isLoggable(Level.INFO))
						LOGGER.log(Level.INFO,"Cannot parse imposed bbox.",e);
				}
			}
				
		}
		
		//
		// resolutions levels
		//              
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.LEVELS)) {
			int levelsNumber = Integer.parseInt(properties.getProperty(
					Prop.LEVELS_NUM, "1").trim());
			retValue.setLevelsNum(levelsNumber);
			if (!properties.containsKey(Prop.LEVELS)) {
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER.info("Required key Levels not found.");
				return null;
			}
			final String levels = properties.getProperty(Prop.LEVELS).trim();
			pairs = levels.split(" ");
			if (pairs == null || pairs.length != levelsNumber) {
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER
							.info("Levels number is different from the provided number of levels resoltion.");
				return null;
			}
			final double[][] resolutions = new double[levelsNumber][2];
			for (int i = 0; i < levelsNumber; i++) {
				pair = pairs[i].split(",");
				if (pair == null || pair.length != 2) {
					if (LOGGER.isLoggable(Level.INFO))
						LOGGER
								.info("OverviewLevel number is different from the provided number of levels resoltion.");
					return null;
				}
				resolutions[i][0] = Double.parseDouble(pair[0]);
				resolutions[i][1] = Double.parseDouble(pair[1]);
			}
			retValue.setLevels(resolutions);
		}

		//
		// suggested spi is optional
		//
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.SUGGESTED_SPI)) {
			if (properties.containsKey(Prop.SUGGESTED_SPI)) {
				final String suggestedSPI = properties.getProperty(
						Prop.SUGGESTED_SPI).trim();
				retValue.setSuggestedSPI(suggestedSPI);
			}
		}

		//
		// time attribute is optional
		//
		if (properties.containsKey(Prop.TIME_ATTRIBUTE)) {
		        final String timeAttribute = properties.getProperty("TimeAttribute").trim();
			retValue.setTimeAttribute(timeAttribute);
		}

		//
		// elevation attribute is optional
		//
		if (properties.containsKey(Prop.ELEVATION_ATTRIBUTE)) {
		        final String elevationAttribute = properties.getProperty("ElevationAttribute").trim();
			retValue.setElevationAttribute(elevationAttribute);
		}

		//
		// runtime attribute is optional
		//
		if (properties.containsKey(Prop.RUN_TIME)) {
		        final String runtimeAttribute = properties.getProperty("RuntimeAttribute").trim();
			retValue.setRuntimeAttribute(runtimeAttribute);
		}

		//
		// caching
		//
		if (properties.containsKey(Prop.CACHING)) {
			String caching = properties.getProperty("Caching").trim();
			try {
				retValue.setCaching(Boolean.valueOf(caching));
			} catch (Throwable e) {
				retValue.setCaching(Boolean.valueOf(Utils.DEFAULT_CACHING_BEHAVIOR));
			}
		}

		//
		// name is not optional
		//
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.NAME)){
                    if(!properties.containsKey(Prop.NAME)) {
                            if(LOGGER.isLoggable(Level.SEVERE))
                                    LOGGER.severe("Required key Name not found.");          
                            return  null;
                    }                       
                    String coverageName = properties.getProperty(Prop.NAME).trim();
                    retValue.setName(coverageName);
                }

		// need a color expansion?
		// this is a newly added property we have to be ready to the case where
		// we do not find it.
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.EXP_RGB)) {
			final boolean expandMe = Boolean.valueOf(properties.getProperty(
					Prop.EXP_RGB, "false").trim());
			retValue.setExpandToRGB(expandMe);
		}
		
		// 
		// Is heterogeneous granules mosaic
		//
                if (!ignoreSome || !ignorePropertiesSet.contains(Prop.HETEROGENEOUS)) {
                    final boolean heterogeneous = Boolean.valueOf(properties.getProperty(
                            Prop.HETEROGENEOUS, "false").trim());
                    retValue.setHeterogeneous(heterogeneous);
                }

		//
		// Absolute or relative path
		//
		if (!ignoreSome || !ignorePropertiesSet.contains(Prop.ABSOLUTE_PATH)) {
			final boolean absolutePath = Boolean.parseBoolean(properties
					.getProperty(Prop.ABSOLUTE_PATH,
							Boolean.toString(Utils.DEFAULT_PATH_BEHAVIOR))
					.trim());
			retValue.setAbsolutePath(absolutePath);
		}

		//
		// Footprint management
		//
		if (!ignoreSome
				|| !ignorePropertiesSet.contains(Prop.FOOTPRINT_MANAGEMENT)) {
			final boolean footprintManagement = Boolean.valueOf(properties
					.getProperty(Prop.FOOTPRINT_MANAGEMENT, "false").trim());
			retValue.setFootprintManagement(footprintManagement);
		}

		//
		// location
		//  
		if (!ignoreSome
				|| !ignorePropertiesSet.contains(Prop.LOCATION_ATTRIBUTE)) {
			retValue.setLocationAttribute(properties.getProperty(
					Prop.LOCATION_ATTRIBUTE, Utils.DEFAULT_LOCATION_ATTRIBUTE)
					.trim());
		}

		// return value
		return retValue;
	}

	/**
	 * Parses a bbox in the form of MIX,MINY MAXX,MAXY
	 * @param bboxString the string to parse the bbox from
	 * @return a {@link ReferencedEnvelope} with the parse bbox or null
	 */
	public static ReferencedEnvelope parseEnvelope(final String bboxString) {
		if(bboxString==null||bboxString.length()==0)
			return null;
		
		final String[] pairs = bboxString.split(" ");
		if (pairs != null &&pairs.length == 2) {
		
			String[] pair1 = pairs[0].split(",");
			String[] pair2 = pairs[1].split(",");
			if(pair1!=null&&pair1.length==2&&pair2!=null&&pair2.length==2)
				return new ReferencedEnvelope(
						 Double.parseDouble(pair1[0]),
						 Double.parseDouble(pair2[0]),
						 Double.parseDouble(pair1[1]),
						 Double.parseDouble(pair2[1]),
						 null);
	
		}
		// something bad happened
		return null;
	}

	public static Properties loadPropertiesFromURL(URL propsURL) {
		final Properties properties = new Properties();
		InputStream stream = null;
		InputStream openStream = null;
		try {
			openStream = propsURL.openStream();
			stream = new BufferedInputStream(openStream);
			properties.load(stream);
		} catch (FileNotFoundException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		} finally {

			if (stream != null)
				IOUtils.closeQuietly(stream);

			if (openStream != null)
				IOUtils.closeQuietly(openStream);

		}
		return properties;
	}

	/**
	 * Builds a {@link ReferencedEnvelope} in WGS84 from a {@link GeneralEnvelope}.
	 * 
	 * @param coverageEnvelope
	 *            the {@link GeneralEnvelope} to convert.
	 * @return an instance of {@link ReferencedEnvelope} in WGS84 or <code>null</code> in case a problem during the conversion occurs.
	 */
	static ReferencedEnvelope getWGS84ReferencedEnvelope(
			final GeneralEnvelope coverageEnvelope) {
		Utilities.ensureNonNull("coverageEnvelope", coverageEnvelope);
		final ReferencedEnvelope refEnv= new ReferencedEnvelope(coverageEnvelope);
		try{
		    return refEnv.transform(DefaultGeographicCRS.WGS84, true);
		}catch (Exception e) {
                    return null;
                }
	}

	static ImageReadParam cloneImageReadParam(ImageReadParam param) {

		// The ImageReadParam passed in is non-null. As the
		// ImageReadParam class is not Cloneable, if the param
		// class is simply ImageReadParam, then create a new
		// ImageReadParam instance and set all its fields
		// which were set in param. This will eliminate problems
		// with concurrent modification of param for the cases
		// in which there is not a special ImageReadparam used.

		// Create a new ImageReadParam instance.
		ImageReadParam newParam = new ImageReadParam();

		// Set all fields which need to be set.

		// IIOParamController field.
		if (param.hasController()) {
			newParam.setController(param.getController());
		}

		// Destination fields.
		newParam.setDestination(param.getDestination());
		if (param.getDestinationType() != null) {
			// Set the destination type only if non-null as the
			// setDestinationType() clears the destination field.
			newParam.setDestinationType(param.getDestinationType());
		}
		newParam.setDestinationBands(param.getDestinationBands());
		newParam.setDestinationOffset(param.getDestinationOffset());

		// Source fields.
		newParam.setSourceBands(param.getSourceBands());
		newParam.setSourceRegion(param.getSourceRegion());
		if (param.getSourceMaxProgressivePass() != Integer.MAX_VALUE) {
			newParam.setSourceProgressivePasses(param
					.getSourceMinProgressivePass(), param
					.getSourceNumProgressivePasses());
		}
		if (param.canSetSourceRenderSize()) {
			newParam.setSourceRenderSize(param.getSourceRenderSize());
		}
		newParam.setSourceSubsampling(param.getSourceXSubsampling(), param
				.getSourceYSubsampling(), param.getSubsamplingXOffset(), param
				.getSubsamplingYOffset());

		// Replace the local variable with the new ImageReadParam.
		return newParam;

	}

	public static IOFileFilter excludeFilters(final IOFileFilter inputFilter,
			IOFileFilter... filters) {
		IOFileFilter retFilter = inputFilter;
		for (IOFileFilter filter : filters) {
			retFilter = FileFilterUtils.andFileFilter(retFilter,
					FileFilterUtils.notFileFilter(filter));
		}
		return retFilter;
	}

	/**
	 * Look for an {@link ImageReader} instance that is able to read the
	 * provided {@link ImageInputStream}, which must be non null.
	 * 
	 * <p>
	 * In case no reader is found, <code>null</code> is returned.
	 * 
	 * @param inStream
	 *            an instance of {@link ImageInputStream} for which we need to
	 *            find a suitable {@link ImageReader}.
	 * @return a suitable instance of {@link ImageReader} or <code>null</code>
	 *         if one cannot be found.
	 */
	static ImageReader getReader(final ImageInputStream inStream) {
		Utilities.ensureNonNull("inStream", inStream);
		// get a reader
		inStream.mark();
		final Iterator<ImageReader> readersIt = ImageIO
				.getImageReaders(inStream);
		if (!readersIt.hasNext()) {
			return null;
		}
		return readersIt.next();
	}

	/**
	 * Retrieves the dimensions of the {@link RenderedImage} at index
	 * <code>imageIndex</code> for the provided {@link ImageReader} and
	 * {@link ImageInputStream}.
	 * 
	 * <p>
	 * Notice that none of the input parameters can be <code>null</code> or a
	 * {@link NullPointerException} will be thrown. Morevoer the
	 * <code>imageIndex</code> cannot be negative or an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param imageIndex
	 *            the index of the image to get the dimensions for.
	 * @param inStream
	 *            the {@link ImageInputStream} to use as an input
	 * @param reader
	 *            the {@link ImageReader} to decode the image dimensions.
	 * @return a {@link Rectangle} that contains the dimensions for the image at
	 *         index <code>imageIndex</code>
	 * @throws IOException
	 *             in case the {@link ImageReader} or the
	 *             {@link ImageInputStream} fail.
	 */
	static Rectangle getDimension(final int imageIndex,
			final ImageInputStream inStream, final ImageReader reader)
			throws IOException {
		Utilities.ensureNonNull("inStream", inStream);
		Utilities.ensureNonNull("reader", reader);
		if (imageIndex < 0)
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, imageIndex));
		inStream.reset();
		reader.setInput(inStream);
		return new Rectangle(0, 0, reader.getWidth(imageIndex), reader
				.getHeight(imageIndex));
	}

	/**
	 * Retrieves an {@link ImageInputStream} for the provided input {@link File}
	 * .
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	static ImageInputStream getInputStream(final File file) throws IOException {
        Utilities.ensureNonNull("file", file);
		final ImageInputStream inStream = ImageIO.createImageInputStream(file);
		if (inStream == null)
			return null;
		return inStream;
	}

	/**
	 * Retrieves an {@link ImageInputStream} for the provided input {@link URL}.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	static ImageInputStream getInputStream(final URL url) throws IOException {
        Utilities.ensureNonNull("url", url);
		final ImageInputStream inStream = CACHED_STREAM_SPI
				.createInputStreamInstance(url, ImageIO.getUseCache(), ImageIO
						.getCacheDirectory());
		if (inStream == null)
			return null;
		return inStream;
	}

	/**
	 * Default priority for the underlying {@link Thread}.
	 */
	public static final int DEFAULT_PRIORITY = Thread.NORM_PRIORITY;
	/**
	 * Default location attribute name.
	 */
	public static final String DEFAULT_LOCATION_ATTRIBUTE = "location";

	public static final String DEFAULT_INDEX_NAME = "index";

	/**
	 * Checks that a {@link File} is a real file, exists and is readable.
	 * 
	 * @param file
	 *            the {@link File} instance to check. Must not be null.
	 * 
	 * @return <code>true</code> in case the file is a real file, exists and is
	 *         readable; <code>false </code> otherwise.
	 */
	public static boolean checkFileReadable(final File file) {
		if (LOGGER.isLoggable(Level.FINE)) {
			final StringBuilder builder = new StringBuilder();
			builder.append("Checking file:").append(
					FilenameUtils.getFullPath(file.getAbsolutePath())).append(
					"\n");
			builder.append("canRead:").append(file.canRead()).append("\n");
			builder.append("isHidden:").append(file.isHidden()).append("\n");
			builder.append("isFile").append(file.isFile()).append("\n");
			builder.append("canWrite").append(file.canWrite()).append("\n");
			LOGGER.fine(builder.toString());
		}
		if (!file.exists() || !file.canRead() || !file.isFile())
			return false;
		return true;
	}

	/**
	 * @param testingDirectory
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static String checkDirectory(String testingDirectory)
			throws IllegalArgumentException {
		File inDir = new File(testingDirectory);
		if (!inDir.isDirectory() || !inDir.canRead()) {
			LOGGER.severe("Provided input dir does not exist or is not a dir!");
			throw new IllegalArgumentException(
					"Provided input dir does not exist or is not a dir!");
		}
		try {
			testingDirectory = inDir.getCanonicalPath();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		testingDirectory = FilenameUtils.normalize(testingDirectory);
		if (!testingDirectory.endsWith(File.separator))
			testingDirectory = testingDirectory + File.separator;
		// test to see if things are still good
		inDir = new File(testingDirectory);
		if (!inDir.isDirectory() || !inDir.canRead()) {
			LOGGER.severe("Provided input dir does not exist or is not a dir!");
			throw new IllegalArgumentException(
					"Provided input dir does not exist or is not a dir!");
		}
		return testingDirectory;
	}

	static boolean checkURLReadable(URL url) {
		try {
			url.openStream().close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static final DataStoreFactorySpi SHAPE_SPI = new ShapefileDataStoreFactory();

	public static final DataStoreFactorySpi INDEXED_SHAPE_SPI = new ShapefileDataStoreFactory();

	static final String DIRECT_KAKADU_PLUGIN = "it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReader";

	public static final boolean DEFAULT_RECURSION_BEHAVIOR = true;

	/**
	 * 
	 * @param datastoreProperties
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Serializable> createDataStoreParamsFromPropertiesFile(
			final URL datastoreProperties)
			throws IOException {
		// read the properties file
		Properties properties = loadPropertiesFromURL(datastoreProperties);
		if (properties == null)
			return null;

		// SPI
		final String SPIClass = properties.getProperty("SPI");
		try {
			// create a datastore as instructed
			final DataStoreFactorySpi spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();
			return createDataStoreParamsFromPropertiesFile(properties, spi);
		} catch (ClassNotFoundException e) {
			final IOException ioe = new IOException();
			throw (IOException) ioe.initCause(e);
		} catch (InstantiationException e) {
			final IOException ioe = new IOException();
			throw (IOException) ioe.initCause(e);
		} catch (IllegalAccessException e) {
			final IOException ioe = new IOException();
			throw (IOException) ioe.initCause(e);
		}
	}

	/**
	 * Store a sample image from which we can derive the default SM and CM
	 * 
	 * @param sampleImageFile
	 *            where we should store the image
	 * @param defaultSM
	 *            the {@link SampleModel} for the sample image.
	 * @param defaultCM
	 *            the {@link ColorModel} for the sample image.
	 * @throws IOException
	 *             in case something bad occurs during writing.
	 */
	public static void storeSampleImage(final File sampleImageFile,
			final SampleModel defaultSM, final ColorModel defaultCM)
			throws IOException {
		// create 1X1 image
		final SampleModel sm = defaultSM.createCompatibleSampleModel(1, 1);
		final WritableRaster raster = RasterFactory.createWritableRaster(sm,
				null);
		final BufferedImage sampleImage = new BufferedImage(defaultCM, raster,
				false, null);

		// serialize it
		OutputStream outStream = null;
		ObjectOutputStream ooStream = null;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(
					sampleImageFile));
			ooStream = new ObjectOutputStream(outStream);
			ooStream.writeObject(new SerializableRenderedImage(sampleImage));
		} finally {
			try {
				if (ooStream != null)
					ooStream.close();
			} catch (Throwable e) {
				IOUtils.closeQuietly(ooStream);
			}
			try {
				if (outStream != null)
					outStream.close();
			} catch (Throwable e) {
				IOUtils.closeQuietly(outStream);
			}
		}
	}

	/**
	 * Load a sample image from which we can take the sample model and color
	 * model to be used to fill holes in responses.
	 * 
	 * @param sampleImageFile
	 *            the path to sample image.
	 * @return a sample image from which we can take the sample model and color
	 *         model to be used to fill holes in responses.
	 */
	public static RenderedImage loadSampleImage(final File sampleImageFile) {
		// serialize it
		InputStream inStream = null;
		ObjectInputStream oiStream = null;
		try {

			// do we have the sample image??
			if (Utils.checkFileReadable(sampleImageFile)) {
				inStream = new BufferedInputStream(new FileInputStream(
						sampleImageFile));
				oiStream = new ObjectInputStream(inStream);

				// load the image
				return (RenderedImage) oiStream.readObject();

			} else {
				if (LOGGER.isLoggable(Level.WARNING))
					LOGGER.warning("Unable to find sample image for path "
							+ sampleImageFile);
				return null;
			}
		} catch (FileNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
			return null;
		} finally {
			try {
				if (inStream != null)
					inStream.close();
			} catch (Throwable e) {

				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
			try {
				if (oiStream != null)
					oiStream.close();
			} catch (Throwable e) {

				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * A transparent color for missing data.
	 */
	static final Color TRANSPARENT = new Color(0,0,0,0);
        
	final static Boolean IGNORE_FOOTPRINT = Boolean.getBoolean("org.geotools.footprint.ignore");
	
    public static final boolean DEFAULT_FOOTPRINT_MANAGEMENT = true;
	
	public static final boolean DEFAULT_CONFIGURATION_CACHING = true;
	/** 
	     * Build a background values array using the same dataType of the input {@link SampleModel} (if available). 
	     * 
	     * @param sampleModel
	     * @param backgroundValues
	     * @return
	     */
	    static Number[] getBackgroundValues(final SampleModel sampleModel, final double[] backgroundValues) {
	        Number[] values = null;
	        final int dataType = sampleModel != null ? sampleModel.getDataType() : DataBuffer.TYPE_DOUBLE;
	        final int numBands=sampleModel != null? sampleModel.getNumBands() : 1;
	        switch (dataType){
	            case DataBuffer.TYPE_BYTE:
	                values = new Byte[numBands];
	                 if (backgroundValues == null){                          
	                         Arrays.fill(values, Byte.valueOf((byte)0));
	                 }
	                 else{
	                        //we have background values available
	                     for (int i = 0; i < values.length; i++)
	                         values[i] = i>=backgroundValues.length?Byte.valueOf((byte)backgroundValues[0]):Byte.valueOf((byte)backgroundValues[i]);
	                 }
	                 break;
	            case DataBuffer.TYPE_SHORT:
	            case DataBuffer.TYPE_USHORT:
	                 values = new Short[numBands] ;
	                 if (backgroundValues == null)
	                         Arrays.fill(values, Short.valueOf((short)0));
	                 else {
	                        //we have background values available
        	                 for (int i = 0; i < values.length; i++)
        	                     values[i] = i>=backgroundValues.length?Short.valueOf((short)backgroundValues[0]):Short.valueOf((short)backgroundValues[i]);
	                 }
	                 break;
	            case DataBuffer.TYPE_INT:
	                values = new Integer[numBands] ;
                        if (backgroundValues == null)
                            Arrays.fill(values, Integer.valueOf((int) 0));
                        else {
                            // we have background values available
                            for (int i = 0; i < values.length; i++)
                                values[i] = i >= backgroundValues.length ? Integer.valueOf((int) backgroundValues[0]) : Integer.valueOf((int) backgroundValues[i]);
            
                        }
	                 break;
	            case DataBuffer.TYPE_FLOAT:
	                values = new Float[numBands] ;
	                 if (backgroundValues == null)
	                        Arrays.fill(values, Float.valueOf(0.f));
	                 else{
	                        //we have background values available
	                     for (int i = 0; i < values.length; i++)
	                         values[i] = i>=backgroundValues.length?Float.valueOf((float)backgroundValues[0]):Float.valueOf((float)backgroundValues[i]);
	                 }
	                 break;
	            case DataBuffer.TYPE_DOUBLE:
	                values = new Double[numBands] ;
	                 if (backgroundValues == null)
	                        Arrays.fill(values, Double.valueOf(0.d));
	                 else {
	                        //we have background values available
	                     for (int i = 0; i < values.length; i++)
	                         values[i] = i>=backgroundValues.length?Double.valueOf((Double)backgroundValues[0]):Double.valueOf((Double)backgroundValues[i]);
	                 }
	                 break;
	            }
	        return values;
	    }

	public static Map<String, Serializable> createDataStoreParamsFromPropertiesFile(
			Properties properties, DataStoreFactorySpi spi) throws IOException {
		// get the params
		final Map<String, Serializable> params = new HashMap<String, Serializable>();
		final Param[] paramsInfo = spi.getParametersInfo();
		for (Param p : paramsInfo) {
			// search for this param and set the value if found
			if (properties.containsKey(p.key))
				params.put(p.key, (Serializable) Converters.convert(properties.getProperty(p.key), p.type));
			else if (p.required && p.sample == null)
				throw new IOException("Required parameter missing: "+ p.toString());
		}
		
		return params;
	}

    static URL checkSource(Object source, Hints hints) {
        URL sourceURL = null;
        File sourceFile = null;
        // /////////////////////////////////////////////////////////////////////
        //
        // Check source
        //
        // /////////////////////////////////////////////////////////////////////
        // if it is a URL or a String let's try to see if we can get a file to
        // check if we have to build the index
        if (source instanceof File) {
                sourceFile = (File) source;
                sourceURL = DataUtilities.fileToURL(sourceFile);
        } else if (source instanceof URL) {
            sourceURL = (URL) source;
            if (sourceURL.getProtocol().equals("file")) {
                sourceFile = DataUtilities.urlToFile(sourceURL);
            }
        } else if (source instanceof String) {
                // is it a File?
                final String tempSource = (String) source;
                File tempFile = new File(tempSource);
                if (!tempFile.exists()) {
                        // is it a URL
                        try {
                                sourceURL = new URL(tempSource);
                                source = DataUtilities.urlToFile(sourceURL);
                        } catch (MalformedURLException e) {
                                sourceURL = null;
                                source = null;
                        }
                } else {
                        sourceURL = DataUtilities.fileToURL(tempFile);

                        // so that we can do our magic here below
                        sourceFile = tempFile;
                }
        }

        // //
        //
        // at this point we have tried to convert the thing to a File as hard as
        // we could, let's see what we can do
        //
        // //
        if (sourceFile != null) {
                if (!sourceFile.isDirectory())
                        // real file, can only be a shapefile at this stage or a
                        // datastore.properties file
                        sourceURL = DataUtilities.fileToURL((File) sourceFile);
                else {
                        // it's a DIRECTORY, let's look for a possible properties files
                        // that we want to load
                        final String locationPath = sourceFile.getAbsolutePath();
                        final String defaultIndexName = FilenameUtils.getName(locationPath);
                        boolean datastoreFound = false;
                        boolean buildMosaic = false;

                        //
                        // do we have a datastore properties file? It will preempt on
                        // the shapefile
                        //
                        File dataStoreProperties = new File(locationPath,"datastore.properties");

                        // this can be used to look for properties files that do NOT
                        // define a datastore
                        final File[] properties = sourceFile
                                        .listFiles((FilenameFilter) FileFilterUtils
                                                        .andFileFilter(
                                                                        FileFilterUtils
                                                                                        .notFileFilter(FileFilterUtils
                                                                                                        .nameFileFilter("datastore.properties")),
                                                                        FileFilterUtils
                                                                                        .makeFileOnly(FileFilterUtils
                                                                                                        .suffixFileFilter(".properties"))));

                        // do we have a valid datastore + mosaic properties pair?
                        if (Utils.checkFileReadable(dataStoreProperties)) {
                                // we have a datastore.properties file
                                datastoreFound = true;

                                // check the first valid mosaic properties
                                boolean found = false;
                                for (File propFile : properties)
                                        if (Utils.checkFileReadable(propFile)) {
                                                // load it
                                                if (null != Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile),"location")) {
                                                        found = true;
                                                        break;
                                                }
                                        }

                                // we did not find any good candidate for mosaic.properties
                                // file, this will signal it
                                if (!found)
                                        buildMosaic = true;

                        } else
                        {
                                // we did not find any good candidate for mosaic.properties
                                // file, this will signal it
                                buildMosaic = true;
                                datastoreFound = false;
                        }

                        //
                        // now let's try with shapefile and properties couple
                        //
                        File shapeFile = null;
                        if (!datastoreFound) {
                                for (File propFile : properties) {

                                        // load properties
                                        if (null == Utils.loadMosaicProperties(DataUtilities.fileToURL(propFile), Utils.DEFAULT_LOCATION_ATTRIBUTE))
                                                continue;

                                        // look for a couple shapefile, mosaic properties file
                                        shapeFile = new File(locationPath, FilenameUtils.getBaseName(propFile.getName())+ ".shp");
                                        if (!Utils.checkFileReadable(shapeFile)&& Utils.checkFileReadable(propFile))
                                                buildMosaic = true;
                                        else {
                                                buildMosaic = false;
                                                break;
                                        }
                                }

                        }

                        // did we find anything?
                        if (buildMosaic) {
                                // try to build a mosaic inside this directory and see what
                                // happens
                                createMosaic(locationPath, defaultIndexName,DEFAULT_WILCARD, DEFAULT_PATH_BEHAVIOR,hints);

                                // check that the mosaic properties file was created
                                final File propertiesFile = new File(locationPath,
                                                defaultIndexName + ".properties");
                                if (!Utils.checkFileReadable(propertiesFile)) {
                                        sourceURL = null;
                                        return sourceURL;
                                }

                                // check that the shapefile was correctly created in case it
                                // was needed
                                if (!datastoreFound) {
                                        shapeFile = new File(locationPath, defaultIndexName+ ".shp");

                                        if (!Utils.checkFileReadable(shapeFile))
                                                sourceURL = null;
                                        else
                                                // now set the new source and proceed
                                                sourceURL = DataUtilities.fileToURL(shapeFile);
                                } else {
                                        dataStoreProperties = new File(locationPath,"datastore.properties");

                                        // datastore.properties as the source
                                        if (!Utils.checkFileReadable(dataStoreProperties))
                                                sourceURL = null;
                                        else
                                                sourceURL = DataUtilities.fileToURL(dataStoreProperties);
                                }

                        } else
                                // now set the new source and proceed
                                sourceURL = datastoreFound ? DataUtilities.fileToURL(dataStoreProperties) : DataUtilities.fileToURL(shapeFile); 

                }
        } else {
                // SK: We don't set SourceURL to null now, just because it doesn't
                // point to a file
                // sourceURL=null;
        }
        return sourceURL;
    }
    
    /**
     * Scan back the rendered op chain (navigating the sources) 
     * to find an {@link ImageReader} used to read the main source.
     *  
     * @param rOp
     * @return the {@link ImageReader} related to this operation, if any.
     * {@code null} in case no readers are found.
     */
    public static ImageReader getReader(RenderedImage rOp) {
        if (rOp != null) {
            if (rOp instanceof RenderedOp) {
                RenderedOp renderedOp = (RenderedOp) rOp;

                final int nSources = renderedOp.getNumSources();
                if (nSources > 0) {
                    for (int k = 0; k < nSources; k++) {
                        Object source = null;
                        try {
                            source = renderedOp.getSourceObject(k);
                            
                        } catch (ArrayIndexOutOfBoundsException e) {
                            // Ignore
                        }
                        if (source != null) {
                            if (source instanceof RenderedOp) {
                                getReader((RenderedOp) source);
                            }
                        }
                    }
                } else {
                    // get the reader
                    Object imageReader = rOp.getProperty(ImageReadDescriptor.PROPERTY_NAME_IMAGE_READER);
                    if (imageReader != null && imageReader instanceof ImageReader) {
                        final ImageReader reader = (ImageReader) imageReader;
                        return reader;
                    }
                }
            }
        }
        return null;
    }

    static final double SAMEBBOX_THRESHOLD_FACTOR = 20;

    static final double AFFINE_IDENTITY_EPS = 1E-6;
    
    /**
     * Private constructor to initialize the ehCache instance.
     * It can be configured through a Bean.
     * @param ehcache
     */
    private Utils(Cache ehcache) {
        Utils.ehcache = ehcache;
    }
    
    /**
     * Setup a {@link Histogram} object by deserializing  
     * a file representing a serialized Histogram.
     * 
     * @param file
     * @return the deserialized histogram.
     */
    public static Histogram getHistogram(final String file){
        Utilities.ensureNonNull("file", file);
        Histogram histogram = null;
        
        // Firstly: check if the histogram have been already
        // deserialized and it is available in cache
        if (ehcache != null && ehcache.isKeyInCache(file)){
            if (ehcache.isElementInMemory(file)){
                final Element element = ehcache.get(file);
                if (element != null){
                    final Serializable value = element.getValue();
                    if (value != null && value instanceof Histogram){
                        histogram = (Histogram) value; 
                        return histogram;
                    }
                }
            }
        }
        
        // No histogram in cache. Deserializing...
        if (histogram == null){
            FileInputStream fileStream = null;
            ObjectInputStream objectStream = null;
            try {

                fileStream = new FileInputStream(file);
                objectStream = new ObjectInputStream(fileStream);
                histogram = (Histogram) objectStream.readObject();
                if (ehcache != null){
                    ehcache.put(new Element(file, histogram));
                }
            } catch (FileNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } catch (IOException e) {
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } catch (ClassNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Unable to parse Histogram:" + e.getLocalizedMessage());
                }
            } finally {
                if (objectStream != null){
                    IOUtils.closeQuietly(objectStream);
                }
                if (fileStream != null){
                    IOUtils.closeQuietly(fileStream);
                }
            }
        }
        return histogram;
    }

    /**
         * Check if the provided granule's footprint covers the same area of the granule's bbox.
         * @param granuleFootprint the granule Footprint
         * @param granuleBBOX the granule bbox
         * @return {@code true} in case the footprint isn't covering the full granule's bbox. 
         */
        static boolean areaIsDifferent(
                final Geometry granuleFootprint,
                final AffineTransform baseGridToWorld,
                final ReferencedEnvelope granuleBBOX) {
            
            // // 
            //
            // First preliminar check:
            // check if the footprint's bbox corners are the same of the granule's bbox
            // (Using a threshold)
            //
            // //
            final Envelope envelope = granuleFootprint.getEnvelope().getEnvelopeInternal();
            double deltaMinX = Math.abs(envelope.getMinX() - granuleBBOX.getMinX());
            double deltaMinY = Math.abs(envelope.getMinY() - granuleBBOX.getMinY());
            double deltaMaxX = Math.abs(envelope.getMaxX() - granuleBBOX.getMaxX());
            double deltaMaxY = Math.abs(envelope.getMaxY() - granuleBBOX.getMaxY());
            final double resX = XAffineTransform.getScaleX0(baseGridToWorld);
            final double resY = XAffineTransform.getScaleY0(baseGridToWorld);
            final double toleranceX = resX / Utils.SAMEBBOX_THRESHOLD_FACTOR;
            final double toleranceY = resY / Utils.SAMEBBOX_THRESHOLD_FACTOR;
            
            // Taking note of the area of a single cell
            final double cellArea = resX * resY;
    
            if (deltaMinX > toleranceX || deltaMaxX > toleranceX || deltaMinY > toleranceY || deltaMaxY > toleranceY){
                // delta exceed tolerance. Area is not the same	        
                return true;
            }
            
            // //
            //
            // Second check:
            // Here, the footprint's bbox and the granule's bbox are equal.
            // However this is not enough:
            // - suppose the footprint is a diamond
            // - Create a rectangle by circumscribing the diamond
            // - If this rectangle match with the granule's bbox, this doesn't imply
            // that the diamond covers the same area of the bbox.
            // Therefore, we need to compute the area and compare them.
            //
            // //
            final double footprintArea = granuleFootprint.getArea();
            //final double bboxArea = granuleBBOX.getArea();
            final double bboxArea = granuleBBOX.getHeight() * granuleBBOX.getWidth();
            	    
            // If 2 areas are different more than the cellArea, then they are not the same area
            if (Math.abs(footprintArea - bboxArea) > cellArea)
                return true;
            return false;
    }
}
