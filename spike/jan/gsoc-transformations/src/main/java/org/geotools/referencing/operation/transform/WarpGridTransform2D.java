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
package org.geotools.referencing.operation.transform;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import javax.media.jai.Warp;
import javax.media.jai.WarpGrid;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.Parameter;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.InvalidParameterTypeException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.Transformation;


/**
 * Basic implementation of JAI's WarpGrid Transformation. This class encapsulates WarpGrid into the
 * GeoTools transformations conventions.
 * @author jezekjan
 *
 */
public class WarpGridTransform2D extends WarpTransform2D {
    /** Inverse Math Transform */
    private MathTransform2D inverse;

    /** World to grid math transform */
    private MathTransform worldToGrid;

    /** Warp object */
    private final Warp warp;

    /** warp position (Warp object desn't offer getWarpPosition in the same format as needed)*/
    private final float[] warpPositions;
    
    
    private float[] inversePos;

    /**
     * Constructs WarpGridTransform2D by settings values defining grid values. 
     * See http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/WarpGrid.html
     * for more.
     * 
     * @param xStart Start of the grid in X direction
     * @param xStep Length of the cell in X direction
     * @param xNumCells Number of cells  in X direction
     * @param yStart Start of the grid in Y direction
     * @param yStep Length of the sell in Y direction
     * @param yNumCells Number of cells in Y direction
     * @param warpPositions Array of warp position where the dimension must be equal to (xNumCells+1) * (yNumCells+1)
     * 
     * @throws IllegalArgumentException if the lenght of warpPosition is incorrect.
     */
    public WarpGridTransform2D(int xStart, int xStep, int xNumCells, int yStart, int yStep,
        int yNumCells, float[] warpPositions) throws IllegalArgumentException{
        super(new WarpGrid(xStart, xStep, xNumCells, yStart, yStep, yNumCells, warpPositions), null);

        this.warp = super.getWarp();
        this.warpPositions = warpPositions;
    }

    /**
     * Returns a URL from the string representation. If the string has no
     * path, the default path preference is added.
     *
     * @param str a string representation of a URL
     * @return a URL created from the string representation
     * @throws MalformedURLException if the URL cannot be created
     */
    private static URL makeURL(final String str) throws MalformedURLException {
        //has '/' or '\' or ':', so probably full path to file
        if ((str.indexOf('\\') >= 0) || (str.indexOf('/') >= 0) || (str.indexOf(':') >= 0)) {
            return makeURLfromString(str);
        } else {
            // just a file name , prepend base location
            final Preferences prefs = Preferences.userNodeForPackage(WarpGridTransform2D.class);
            final String baseLocation = prefs.get("GRID_LOCATION", "");

            return makeURLfromString(baseLocation + "/" + str);
        }
    }

    /**
     * Returns a URL based on a string representation. If no protocol is given,
     * it is assumed to be a local file.
     *
     * @param str a string representation of a URL
     * @return a URL created from the string representation
     * @throws MalformedURLException if the URL cannot be created
     */
    private static URL makeURLfromString(final String str)
        throws MalformedURLException {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            //try making this with a file protocal
            return new URL("file", "", str);
        }
    }

    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * Returns the parameter values for this math transform.
     */
    public ParameterValueGroup getParameterValues() {
        if (this.warp instanceof WarpGrid) {
           // final WarpGrid wGrid = (WarpGrid) warp;
            final ParameterValue[] p = new ParameterValue[7];
            int c = 0;
            p[c++] = new Parameter(Provider.X_START, ((WarpGrid) getWarp()).getXStart()); //new Integer(((WarpGrid)super.getWarp()).getXStart()));
            p[c++] = new Parameter(Provider.X_STEP, ((WarpGrid) getWarp()).getXStep());
            p[c++] = new Parameter(Provider.X_NUMCELLS, ((WarpGrid) getWarp()).getXNumCells());
            p[c++] = new Parameter(Provider.Y_START, ((WarpGrid) getWarp()).getYStart());
            p[c++] = new Parameter(Provider.Y_STEP, ((WarpGrid) getWarp()).getYStep());
            p[c++] = new Parameter(Provider.Y_NUMCELLS, ((WarpGrid) getWarp()).getYNumCells());

            p[c++] = new Parameter(Provider.WARP_POSITIONS, (Object) this.warpPositions.clone());

            return new ParameterGroup(getParameterDescriptors(),
                (ParameterValue[]) XArray.resize(p, c));
        } else {
            return super.getParameterValues();
        }
    }

    public void setWorldtoGridTransform(MathTransform worldToGrid) {
        this.worldToGrid = worldToGrid;
    }

    public MathTransform getWorldtoGridTransform() {
        return worldToGrid;
    }

    public void transform(final double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
       {
        // TODO Auto-generated method stub
        //transformToGrid(srcPts, srcOff, srcPts, srcOff, numPts, false);
        try {
			double[] helperPts = new double[srcPts.length];

			
			    if (worldToGrid != null) {
			        worldToGrid.transform(srcPts, srcOff, helperPts, srcOff, numPts);
			    }
      

			
				super.transform(helperPts, srcOff, dstPts, dstOff, numPts);
			

			
			    if (worldToGrid != null) {
			        worldToGrid.inverse().transform(dstPts, dstOff, dstPts, dstOff, numPts);
			    }
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void transform(float[] srcPts, int srcOff, float[] dstPts, int dstOff, int numPts) {
        try {
            if (worldToGrid != null) {
                worldToGrid.transform(srcPts, srcOff, srcPts, srcOff, numPts);
            }
        } catch (TransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        super.transform(srcPts, srcOff, dstPts, dstOff, numPts);

        try {
            if (worldToGrid != null) {
                worldToGrid.inverse().transform(dstPts, dstOff, dstPts, dstOff, numPts);
            }
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        } catch (TransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Point2D transform(Point2D ptSrc, Point2D ptDst) {
        try {
            if (worldToGrid != null) {
                worldToGrid.transform((DirectPosition) ptSrc, (DirectPosition) ptSrc);
            }
        } catch (TransformException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ptDst = super.transform(ptSrc, ptDst);

        try {
            if (worldToGrid != null) {
                worldToGrid.inverse().transform((DirectPosition) ptDst, (DirectPosition) ptDst);
            }
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        } catch (TransformException e) {
            e.printStackTrace();
        }

        return ptDst;
    }

    /**
     * TODO - make static as soon as we get rid of worldToGrid transform
     * Calculation inverse values. Calculation is not exact, but should provide good results
     * when shifts are smaller than grid cells.
     * @param xStart
     * @param xStep
     * @param xNumCells
     * @param yStart
     * @param yStep
     * @param yNumCells
     * @param warpPositions
     * @return
     */
    private WarpGridTransform2D calculateInverse(int xStart, int xStep, int xNumCells, int yStart,
        int yStep, int yNumCells, float[] warpPositions) {
    	
    	if ( inversePos == null){
        inversePos = new float[warpPositions.length];

        for (int i = 0; i <= yNumCells; i++) {
            for (int j = 0; j <= xNumCells; j++) {
                inversePos[(i * ((1 + xNumCells) * 2)) + (2 * j)] = (2 * ((j * xStep) + xStart))
                    - warpPositions[(i * ((1 + xNumCells) * 2)) + (2 * j)];

                inversePos[(i * ((1 + xNumCells) * 2)) + (2 * j) + 1] = (2 * ((i * yStep) + yStart))
                    - warpPositions[(i * ((1 + xNumCells) * 2)) + (2 * j) + 1];
            }
        }

    	}
        WarpGridTransform2D wgt = new WarpGridTransform2D(xStart, xStep, xNumCells, yStart, yStep,
                yNumCells, inversePos);

        wgt.setWorldtoGridTransform(this.worldToGrid);

        return wgt;
    }

    public MathTransform2D inverse() throws NoninvertibleTransformException {
        // TODO Auto-generated method stub
     //   if (inverse == null) {
            inverse = calculateInverse(((WarpGrid) getWarp()).getXStart(),
                    ((WarpGrid) getWarp()).getXStep(), ((WarpGrid) getWarp()).getXNumCells(),
                    ((WarpGrid) getWarp()).getYStart(), ((WarpGrid) getWarp()).getYStep(),
                    ((WarpGrid) getWarp()).getYNumCells(), warpPositions);
       // }

        return inverse;
    }

   
    /**
     *
     * The provider for the {@linkplain WarpGridTransform2D}. This provider constructs a JAI
     * {@linkplain WarpGrid image warp} from a set of mapped positions,
     * and wrap it in a {@linkplain WarpGridTransform2D} object.
     *
     * @author jezekjan
     *
     */
    public static class Provider extends MathTransformProvider {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -1126785723468L;

        /** Descriptor for the "{@link WarpGrid#getXStart  xStart}" parameter value. */
        public static final ParameterDescriptor X_START = new DefaultParameterDescriptor("xStart",
        		Integer.class, null, null);

        /** Descriptor for the "{@link WarpGrid#getXStep xStep}" parameter value. */
        public static final ParameterDescriptor X_STEP = new DefaultParameterDescriptor("xStep",
        		Integer.class, null, null);

        /** Descriptor for the "{@link WarpGrid#getXNumCells xNumCells}" parameter value. */
        public static final ParameterDescriptor X_NUMCELLS = new DefaultParameterDescriptor("xNumCells",
                Integer.class, null, null);

        /** Descriptor for the "{@link WarpGrid#getYStart yStart}" parameter value. */
        public static final ParameterDescriptor Y_START = new DefaultParameterDescriptor("yStart",
        		Integer.class, null, null);

        /** Descriptor for the "{@link WarpGrid#getYStep yStep}" parameter value. */
        public static final ParameterDescriptor Y_STEP = new DefaultParameterDescriptor("yStep",
        		Integer.class, null, null);

        /** Descriptor for the "{@link WarpGrid#getYNumCells yNumCells}" parameter value. */
        public static final ParameterDescriptor Y_NUMCELLS = new DefaultParameterDescriptor("yNumCells",
        		Integer.class, null, null);

        /** Descriptor for the warpPositions parameter value. This the target coordinates of weach cell (not deltas) */
        public static final ParameterDescriptor<float[]> WARP_POSITIONS = new DefaultParameterDescriptor("warpPositions",
                float[].class, null, null);

        /**
         * The parameters group.
         */
        private static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTOOLS, "Warp Grid")
                },
                new ParameterDescriptor[] {
                    X_START, X_STEP, X_NUMCELLS, Y_START, Y_STEP, Y_NUMCELLS, WARP_POSITIONS
                });

        public ParameterDescriptorGroup getParameters(){
        	return PARAMETERS;
        }
        /**
         * Create a provider for warp transforms.
         */
        public Provider() {
            super(2, 2, PARAMETERS);
        }

        /**
         * Returns the operation type.
         */
        public Class getOperationType() {
            return Transformation.class;
        }

        /**
         * Creates a warp transform from the specified group of parameter values.
         *
         * @param  values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup values)
            throws ParameterNotFoundException {
            final int xStart = intValue(X_START, values);
            final int xStep = intValue(X_STEP, values);
            final int xNumCells = intValue(X_NUMCELLS, values);
            final int yStart = intValue(Y_START, values);
            final int yStep = intValue(Y_STEP, values);
            final int yNumCells = intValue(Y_NUMCELLS, values);
            final float[] warpPos = (float[]) value(WARP_POSITIONS, values);

            WarpGridTransform2D wgt = new WarpGridTransform2D(xStart, xStep, xNumCells, yStart,
                    yStep, yNumCells, warpPos);

            return wgt;
        }
    }

    /**
     * The provider for {@link NADCONTransform}. This provider will construct
     * transforms from {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS
     * geographic} to {@linkplain org.geotools.referencing.crs.DefaultGeographicCRS
     * geographic} coordinate reference systems.
     *
     */
    public static class ProviderFile extends MathTransformProvider {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -42356975310348L;

        /**
         * The operation parameter descriptor for the "Latitude_difference_file"
         * parameter value. The default value is "conus.las".
         */
        public static final ParameterDescriptor X_DIFF_FILE = new DefaultParameterDescriptor("X_difference_file",
                String.class, null, "");

        /**
         * The operation parameter descriptor for the "Longitude_difference_file"
         * parameter value. The default value is "conus.los".
         */
        public static final ParameterDescriptor Y_DIFF_FILE = new DefaultParameterDescriptor("Y_difference_file",
                String.class, null, "");

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                    new NamedIdentifier(Citations.EPSG, "9613"),
                    new NamedIdentifier(Citations.GEOTOOLS, "Warp Grid (from file)")
                }, new ParameterDescriptor[] { X_DIFF_FILE, Y_DIFF_FILE });

        /**
         * Constructs a provider.
         */
        public ProviderFile() {
            super(2, 2, PARAMETERS);
        }

        /**
         * Returns the operation type.
         */
        public Class getOperationType() {
            return Transformation.class;
        }

        public MathTransform createMathTransform(final ParameterValueGroup values)
            throws ParameterNotFoundException, InvalidParameterTypeException, FactoryException {
            try {
                return createWarpGrid(values.parameter("X_difference_file").stringValue(),
                    values.parameter("Y_difference_file").stringValue());
            } catch (MalformedURLException e) {
                throw new FactoryException(Errors.format(ErrorKeys.UNSUPPORTED_FILE_TYPE_$2), e);
            } catch (IOException e) {
                throw new FactoryException(Errors.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1), e);
            }
        }

        /**
         *
         * @param latGridName
         * @param longGridName
         * @return
         */
        private static WarpGridTransform2D createWarpGrid(final String xGridName,
            final String yGridName) throws MalformedURLException, IOException, FactoryException {
            final URL xGridURL = makeURL(xGridName);
            final URL yGridURL = makeURL(yGridName);

            return loadTextGrid(xGridURL, yGridURL);
        }

        /**
         *
         * @param latGridUrl
         * @param longGridUrl
         * @throws IOException
         * @throws FactoryException
         */
        private static WarpGridTransform2D loadTextGrid(URL xGridUrl, URL longGridUrl)
            throws IOException, FactoryException {
            String xLine;
            String longLine;
            StringTokenizer xSt;
            StringTokenizer longSt;

            ////////////////////////
            //setup
            ////////////////////////
            InputStreamReader xIsr = new InputStreamReader(xGridUrl.openStream());
            BufferedReader xBr = new BufferedReader(xIsr);

            InputStreamReader longIsr = new InputStreamReader(longGridUrl.openStream());
            BufferedReader longBr = new BufferedReader(longIsr);

            ////////////////////////
            //read header info
            ////////////////////////
            xLine = xBr.readLine(); //skip header description        
            xLine = xBr.readLine();
            xSt = new StringTokenizer(xLine, " ");

            if (xSt.countTokens() > 8) {
                throw new FactoryException(Errors.format(ErrorKeys.HEADER_UNEXPECTED_LENGTH_$1,
                        String.valueOf(xSt.countTokens())));
            }

            int nc = Integer.parseInt(xSt.nextToken());
            int nr = Integer.parseInt(xSt.nextToken());
            int nz = Integer.parseInt(xSt.nextToken());

            float xStart = Float.parseFloat(xSt.nextToken());
            float xStep = Float.parseFloat(xSt.nextToken());
            float yStart = Float.parseFloat(xSt.nextToken());
            float yStep = Float.parseFloat(xSt.nextToken());

            // float angle = Float.parseFloat(latSt.nextToken());
            float xmax = xStart + ((nc - 1) * xStart);
            float ymax = yStart + ((nr - 1) * yStep);

            //now read long shift grid
            longLine = longBr.readLine(); //skip header description
            longLine = longBr.readLine();
            longSt = new StringTokenizer(longLine, " ");

            if (longSt.countTokens() > 8) {
                throw new FactoryException(Errors.format(ErrorKeys.HEADER_UNEXPECTED_LENGTH_$1,
                        String.valueOf(longSt.countTokens())));
            }

            //check that latitude grid header is the same as for latitude grid
            if ((nc != Integer.parseInt(longSt.nextToken()))
                    || (nr != Integer.parseInt(longSt.nextToken()))
                    || (nz != Integer.parseInt(longSt.nextToken()))
                    || (xStart != Float.parseFloat(longSt.nextToken()))
                    || (xStep != Float.parseFloat(longSt.nextToken()))
                    || (yStart != Float.parseFloat(longSt.nextToken()))
                    || (yStep != Float.parseFloat(longSt.nextToken()))) {
                // || (angle != Float.parseFloat(longSt.nextToken()))) {
                throw new FactoryException(Errors.format(ErrorKeys.GRID_LOCATIONS_UNEQUAL));
            }

            ////////////////////////
            //read grid shift data into LocalizationGrid
            ////////////////////////    
            int i = 0;
            int j = 0;
            float[] warpPos = new float[2 * (nc) * (nr)];

            for (i = 0; i < nr; i++) {
                for (j = 0; j < nc;) {
                    xLine = xBr.readLine();
                    xSt = new StringTokenizer(xLine, " ");
                    longLine = longBr.readLine();
                    longSt = new StringTokenizer(longLine, " ");

                    while (xSt.hasMoreTokens() && longSt.hasMoreTokens()) {
                        warpPos[(2 * j) + (nc * i * 2)] = xStart + (j * xStep)
                            + (float) -Float.parseFloat(xSt.nextToken());
                        warpPos[(2 * j) + (nc * i * 2) + 1] = yStart + (i * yStep)
                            + (float) Float.parseFloat(longSt.nextToken());
                        ++j;
                    }
                }
            }

            return new WarpGridTransform2D((int) xStart, (int) xStep, nc - 1, (int) yStart,
                (int) yStep, nr - 1, warpPos);
        }
    }
}
