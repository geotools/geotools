/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid;

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.Serializable;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.Cloneable;


/**
 * Describes the valid range of grid coordinates and the math transform to transform grid
 * coordinates to real world coordinates. Grid geometries contains:
 * <p>
 * <ul>
 *   <li>An optional {@linkplain GridEnvelope grid envelope} (a.k.a. "<cite>grid range</cite>"),
 *       usually inferred from the {@linkplain RenderedImage rendered image} size.</li>
 *   <li>An optional "grid to CRS" {@linkplain MathTransform transform}, which may be inferred
 *       from the grid range and the envelope.</li>
 *   <li>An optional {@linkplain Envelope envelope}, which may be inferred from the grid range
 *       and the "grid to CRS" transform.</li>
 *   <li>An optional {@linkplain CoordinateReferenceSystem coordinate reference system} to be
 *       given to the envelope.</li>
 * </ul>
 * <p>
 * All grid geometry attributes are optional because some of them may be inferred from a wider
 * context. For example a grid geometry know nothing about {@linkplain RenderedImage rendered
 * images}, but {@link GridCoverage2D} do. Consequently, the later may infer the {@linkplain
 * GridEnvelope grid range} by itself.
 * <p>
 * By default, any request for an undefined attribute will thrown an
 * {@link InvalidGridGeometryException}. In order to check if an attribute is defined,
 * use {@link #isDefined}.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 * @see GridGeometry2D
 * @see ImageGeometry
 */
public class GeneralGridGeometry implements GridGeometry, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 124700383873732132L;
    
    private static boolean assertsEnabled=false;
    static{
        assert assertsEnabled = true;  // Intentional side-effect !!!
    }

    /**
     * A bitmask to specify the validity of the {@linkplain #getCoordinateReferenceSystem
     * coordinate reference system}. This is given as an argument to the {@link #isDefined}
     * method.
     *
     * @since 2.2
     */
    public static final int CRS_BITMASK = 1;

    /**
     * A bitmask to specify the validity of the {@linkplain #getEnvelope envelope}.
     * This is given as an argument to the {@link #isDefined} method.
     *
     * @since 2.2
     */
    public static final int ENVELOPE_BITMASK = 2;

    /**
     * A bitmask to specify the validity of the {@linkplain #getGridRange grid range}.
     * This is given as an argument to the {@link #isDefined} method.
     *
     * @since 2.2
     */
    public static final int GRID_RANGE_BITMASK = 4;

    /**
     * A bitmask to specify the validity of the {@linkplain #getGridToCoordinateSystem grid to CRS}
     * transform. This is given as an argument to the {@link #isDefined} method.
     *
     * @since 2.2
     */
    public static final int GRID_TO_CRS_BITMASK = 8;

    /**
     * The valid coordinate range of a grid coverage, or {@code null} if none. The lowest valid
     * grid coordinate is zero for {@link java.awt.image.BufferedImage}, but may be non-zero for
     * arbitrary {@link RenderedImage}. A grid with 512 cells can have a minimum coordinate of 0
     * and maximum of 512, with 511 as the highest valid index.
     *
     * @see RenderedImage#getMinX
     * @see RenderedImage#getMinY
     * @see RenderedImage#getWidth
     * @see RenderedImage#getHeight
     */
    protected final GridEnvelope gridRange;

    /**
     * The envelope, which is usually the {@linkplain #gridRange grid range}
     * {@linkplain #gridToCRS transformed} to real world coordinates. This
     * envelope contains the {@linkplain CoordinateReferenceSystem coordinate
     * reference system} of "real world" coordinates.
     * <p>
     * This field should be considered as private because envelopes are mutable, and we want to make
     * sure that envelopes are cloned before to be returned to the user. Only {@link GridGeometry2D}
     * and {@link GridCoverage2D} access directly to this field (read only) for performance reason.
     *
     * @since 2.2
     */
    GeneralEnvelope envelope;

    /**
     * The math transform (usually an affine transform), or {@code null} if none.
     * This math transform maps {@linkplain PixelInCell#CELL_CENTER pixel center}
     * to "real world" coordinate using the following line:
     *
     * <pre>gridToCRS.transform(pixels, point);</pre>
     */
    protected MathTransform gridToCRS;

    /**
     * Same as {@link #gridToCRS} but from {@linkplain PixelInCell#CELL_CORNER pixel corner}
     * instead of center. Will be computed only when first needed. Serialized because it may
     * be a value specified explicitly at construction time, in which case it can be more
     * accurate than a computed value.
     */
    private MathTransform cornerToCRS;

    /**
     * Constructs a new grid geometry identical to the specified one except for the CRS.
     * Note that this constructor just defines the CRS; it does <strong>not</strong> reproject
     * the envelope. For this reason, this constructor should not be public. It is for internal
     * use by {@link GridCoverageFactory} only.
     */
    GeneralGridGeometry(final GeneralGridGeometry gm, final CoordinateReferenceSystem crs) {
        gridRange   = gm.gridRange;  // Do not clone; we assume it is safe to share.
        gridToCRS   = gm.gridToCRS;
        cornerToCRS = gm.cornerToCRS;
        envelope    = new GeneralEnvelope(gm.envelope);
        envelope.setCoordinateReferenceSystem(crs);
    }

    /**
     * Creates a new grid geometry with the same values than the given grid geometry. This
     * is a copy constructor useful when the instance must be a {@code GeneralGridGeometry}.
     *
     * @param other The other grid geometry to copy.
     *
     * @since 2.5
     */
    public GeneralGridGeometry(final GridGeometry other) {
        if (other instanceof GeneralGridGeometry) {
            // Uses this path when possible in order to accept null values.
            final GeneralGridGeometry general = (GeneralGridGeometry) other;
            gridRange   = general.gridRange;  // Do not clone; we assume it is safe to share.
            gridToCRS   = general.gridToCRS;
            cornerToCRS = general.cornerToCRS;
            envelope    = general.envelope;
        } else {
            gridRange = other.getGridRange();
            gridToCRS = other.getGridToCRS();
            if (gridRange!=null && gridToCRS!=null) {
                envelope = new GeneralEnvelope(gridRange, PixelInCell.CELL_CENTER, gridToCRS, null);
            } else {
                envelope = null;
            }
        }
    }

    /**
     * Constructs a new grid geometry from a grid range and a {@linkplain MathTransform math transform}
     * mapping {@linkplain PixelInCell#CELL_CENTER pixel center}.
     *
     * @param gridRange The valid coordinate range of a grid coverage, or {@code null} if none.
     * @param gridToCRS The math transform which allows for the transformations from grid
     *                  coordinates (pixel's <em>center</em>) to real world earth coordinates.
     *                  May be {@code null}, but this is not recommanded.
     * @param crs       The coordinate reference system for the "real world" coordinates, or
     *                  {@code null} if unknown. This CRS is given to the
     *                  {@linkplain #getEnvelope envelope}.
     *
     * @throws MismatchedDimensionException if the math transform and the CRS don't have
     *         consistent dimensions.
     * @throws IllegalArgumentException if the math transform can't transform coordinates
     *         in the domain of the specified grid range.
     *
     * @since 2.2
     */
    public GeneralGridGeometry(final GridEnvelope        gridRange,
                               final MathTransform       gridToCRS,
                               final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException, IllegalArgumentException
    {
        this(gridRange, PixelInCell.CELL_CENTER, gridToCRS, crs);
    }

    /**
     * Constructs a new grid geometry from a grid range and a {@linkplain MathTransform math transform}
     * mapping pixel {@linkplain PixelInCell#CELL_CENTER center} or {@linkplain PixelInCell#CELL_CORNER
     * corner}. This is the most general constructor, the one that gives the maximal control over
     * the grid geometry to be created.
     *
     * @param gridRange The valid coordinate range of a grid coverage, or {@code null} if none.
     * @param anchor    {@link PixelInCell#CELL_CENTER CELL_CENTER} for OGC conventions or
     *                  {@link PixelInCell#CELL_CORNER CELL_CORNER} for Java2D/JAI conventions.
     * @param gridToCRS The math transform which allows for the transformations from grid
     *                  coordinates to real world earth coordinates. May be {@code null},
     *                  but this is not recommended.
     * @param crs       The coordinate reference system for the "real world" coordinates, or
     *                  {@code null} if unknown. This CRS is given to the
     *                  {@linkplain #getEnvelope envelope}.
     *
     * @throws MismatchedDimensionException if the math transform and the CRS don't have
     *         consistent dimensions.
     * @throws IllegalArgumentException if the math transform can't transform coordinates
     *         in the domain of the specified grid range.
     *
     * @since 2.5
     */
    public GeneralGridGeometry(final GridEnvelope        gridRange,
                               final PixelInCell         anchor,
                               final MathTransform       gridToCRS,
                               final CoordinateReferenceSystem crs)
            throws MismatchedDimensionException, IllegalArgumentException
    {
    	
        this.gridRange = clone(gridRange);
        this.gridToCRS = PixelTranslation.translate(gridToCRS, anchor, PixelInCell.CELL_CENTER);
        if (PixelInCell.CELL_CORNER.equals(anchor)) {
            cornerToCRS = gridToCRS;
        }
        if (gridRange!=null && gridToCRS!=null) {
            envelope = new GeneralEnvelope(gridRange, anchor, gridToCRS, crs);
        } else if (crs != null) {
            envelope = new GeneralEnvelope(crs);
            envelope.setToNull();
        } else {
            envelope = null;
        }
    }
    
    /**
     * Casts the specified grid range into an envelope. This is used before to transform
     * the envelope using {@link CRSUtilities#transform(MathTransform, Envelope)}.
     */
    private static Envelope toEnvelope(final GridEnvelope gridRange) {
        final int dimension = gridRange.getDimension();
        final double[] lower = new double[dimension];
        final double[] upper = new double[dimension];
        for (int i=0; i<dimension; i++) {
            lower[i] = gridRange.getLow(i);
            upper[i] = gridRange.getHigh(i) + 1;
        }
        return new GeneralEnvelope(lower, upper);
    }
    
    /**
     * Constructs a new grid geometry from an envelope and a {@linkplain MathTransform math
     * transform}. According OGC specification, the math transform should map {@linkplain
     * PixelInCell#CELL_CENTER pixel center}. But in Java2D/JAI conventions, the transform
     * is rather expected to maps {@linkplain PixelInCell#CELL_CORNER pixel corner}. The
     * convention to follow can be specified by the {@code anchor} argument.
     *
     * <p>
     * It is worth to point a few guidelines for the usage of the gridMaxInclusive parameter.
     * In case we are using this contructor for a reprojection it might be worth using a <code>false</code>
     * value for this parameter since we would not force to create a new raster that might be slightly bigger than 
     * the original one, causing problems with black or nodata collars appearing after a resample.
     * In case we are trying to build a source raster area to be used at reading time, in that case we may want to provide 
     * <code>true</code> for gridMaxInclusive since we may want to read an area that is slightly bigger than what we need
     * to be sure we actually reading something.
     * 
     * @param anchor    {@link PixelInCell#CELL_CENTER CELL_CENTER} for OGC conventions or
     *                  {@link PixelInCell#CELL_CORNER CELL_CORNER} for Java2D/JAI conventions.
     * @param gridToCRS The math transform which allows for the transformations from grid
     *                  coordinates to real world earth coordinates. May be {@code null},
     *                  but this is not recommended.
     * @param envelope  The envelope (including CRS) of a grid coverage, or {@code null} if none.
     * @param gridMaxInclusive Tells us whether or not the resulting gridRange max values should be
     *                  inclusive or not. See notes above.
     * @param preserveGridToCRS Tells us whether we should try to preserve the the gridToCRS or the envelope 
     *                  most part of the time we won't be able to preserve both for our purposes.
     *
     * @throws MismatchedDimensionException if the math transform and the envelope doesn't have
     *         consistent dimensions.
     * @throws IllegalArgumentException if the math transform can't transform coordinates
     *         in the domain of the grid range.
     *
     * @since 2.7
     */
    public GeneralGridGeometry(final PixelInCell   anchor,
                               final MathTransform gridToCRS,
                               final Envelope      envelope,
                               final boolean 	   gridMaxInclusive,
                               final boolean 	   preserveGridToCRS)
            throws MismatchedDimensionException, IllegalArgumentException
    {
        this.gridToCRS = PixelTranslation.translate(gridToCRS, anchor, PixelInCell.CELL_CENTER);
        if (PixelInCell.CELL_CORNER.equals(anchor)) {
            cornerToCRS = gridToCRS;
        }
        else
        	 cornerToCRS =PixelTranslation.translate(gridToCRS, anchor, PixelInCell.CELL_CORNER);
        if (envelope == null) {
            this.envelope  = null;
            this.gridRange = null;
            return;
        }
        this.envelope = new GeneralEnvelope(envelope);
        if (gridToCRS == null) {
            this.gridRange = null;
            return;
        }
        final GeneralEnvelope transformed;
        try {
            transformed = org.geotools.referencing.CRS.transform(cornerToCRS.inverse(), envelope);
        } catch (TransformException exception) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_TRANSFORM_$1,
                    Classes.getClass(gridToCRS)), exception);
        }
        
        // making this inclusive may cause problems when we use for warping of something since we might try to include more data 
        // than we actually havein the source imagery
        gridRange = new GeneralGridEnvelope(transformed, PixelInCell.CELL_CORNER,gridMaxInclusive);
        
       
        //
        // CORRECTIONS
        //
        // do we need to ajust the gridToCRS or the envelope?
        if(preserveGridToCRS)
        {
        	//preserve firdToCrs
        	GeneralEnvelope tempEnvelope;
			try {
				tempEnvelope = CRS.transform(getGridToCRS(),toEnvelope(gridRange));
			} catch (Throwable e) {
				throw new RuntimeException(e);
			} 
			tempEnvelope.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
			this.envelope=tempEnvelope;
        }else
        {
        	// preserve envelope
        	if(gridToCRS!=null){

                // all we need is a simple scale and translate to take care of the fact that we might have approximated a bit the 
                // raster area
                final double xScale=transformed.getSpan(0)/gridRange.getSpan(0);
                final double yScale=transformed.getSpan(1)/gridRange.getSpan(1);    
                final double xTrans=transformed.getMinimum(0)-xScale*gridRange.getLow(0);
                final double yTrans=transformed.getMinimum(1)-yScale*gridRange.getLow(1);
	        	final AffineTransform newTr=new AffineTransform(xScale,0,0,yScale,xTrans,yTrans);
	        	newTr.preConcatenate((AffineTransform)this.cornerToCRS);
				this.cornerToCRS=
	        			ProjectiveTransform.create(
	        					newTr
	        			);
	        	
				
	        	this.gridToCRS=PixelTranslation.translate(cornerToCRS, PixelInCell.CELL_CORNER, PixelInCell.CELL_CENTER);
	        	
		        // Now 'assertsEnabled' is set to the correct value
		        if(assertsEnabled){
			        try {
			        	final MathTransform tr=cornerToCRS;
			        	if(!(tr instanceof AffineTransform))
			        		return;
			        	final AffineTransform transform=(AffineTransform)tr;
			        	final double scale= Math.min(XAffineTransform.getScaleX0(transform), XAffineTransform.getScaleY0(transform));
						final GeneralEnvelope tempEnvelope = CRS.transform(tr,toEnvelope(gridRange));
						tempEnvelope.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
						assert tempEnvelope.equals(envelope,scale*1E-3,true):"Unable to preserve the envelope for this GridGeometry";
					} catch (Throwable e) {
						throw new RuntimeException(e);
					}
		        }				
				
				
        	}
        }
        

    }
    /**
     * Constructs a new grid geometry from an envelope and a {@linkplain MathTransform math
     * transform}. According OGC specification, the math transform should map {@linkplain
     * PixelInCell#CELL_CENTER pixel center}. But in Java2D/JAI conventions, the transform
     * is rather expected to maps {@linkplain PixelInCell#CELL_CORNER pixel corner}. The
     * convention to follow can be specified by the {@code anchor} argument.
     *
     * @param anchor    {@link PixelInCell#CELL_CENTER CELL_CENTER} for OGC conventions or
     *                  {@link PixelInCell#CELL_CORNER CELL_CORNER} for Java2D/JAI conventions.
     * @param gridToCRS The math transform which allows for the transformations from grid
     *                  coordinates to real world earth coordinates. May be {@code null},
     *                  but this is not recommended.
     * @param envelope  The envelope (including CRS) of a grid coverage, or {@code null} if none.
     *
     * @throws MismatchedDimensionException if the math transform and the envelope doesn't have
     *         consistent dimensions.
     * @throws IllegalArgumentException if the math transform can't transform coordinates
     *         in the domain of the grid range.
     *
     * @since 2.5
     */
    public GeneralGridGeometry(final PixelInCell   anchor,
                               final MathTransform gridToCRS,
                               final Envelope      envelope)
            throws MismatchedDimensionException, IllegalArgumentException
    {
        this(anchor, gridToCRS, envelope, false,false);
    }

    /**
     * Constructs a new grid geometry from an {@linkplain Envelope envelope}. An {@linkplain
     * java.awt.geom.AffineTransform affine transform} will be computed automatically from the
     * specified envelope using heuristic rules described in {@link GridToEnvelopeMapper} javadoc.
     * More specifically, heuristic rules are applied for:
     * <p>
     * <ul>
     *   <li>{@linkplain GridToEnvelopeMapper#getSwapXY axis swapping}</li>
     *   <li>{@linkplain GridToEnvelopeMapper#getReverseAxis axis reversal}</li>
     * </ul>
     *
     * @param gridRange The valid coordinate range of a grid coverage.
     * @param userRange The corresponding coordinate range in user coordinate. This rectangle must
     *                  contains entirely all pixels, i.e. the rectangle's upper left corner must
     *                  coincide with the upper left corner of the first pixel and the rectangle's
     *                  lower right corner must coincide with the lower right corner of the last
     *                  pixel.
     *
     * @throws MismatchedDimensionException if the grid range and the envelope doesn't have
     *         consistent dimensions.
     *
     * @since 2.2
     */
    public GeneralGridGeometry(final GridEnvelope gridRange, final Envelope userRange)
            throws MismatchedDimensionException
    {
        this(gridRange, userRange, null, false, true);
    }

    /**
     * Implementation of heuristic constructors.
     */
    GeneralGridGeometry(final GridEnvelope gridRange,
                        final Envelope  userRange,
                        final boolean[] reverse,
                        final boolean   swapXY,
                        final boolean   automatic)
            throws MismatchedDimensionException
    {
        this.gridRange = clone(gridRange);
        this.envelope  = new GeneralEnvelope(userRange);
        final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper(gridRange, userRange);
        if (!automatic) {
            mapper.setReverseAxis(reverse);
            mapper.setSwapXY(swapXY);
        }
        gridToCRS = mapper.createTransform();
    }

    /**
     * Clones the given grid range if necessary. This is mostly a protection for {@link GridRange2D}
     * which is mutable, at the opposite of {@link GeneralGridRange} which is immutable. We test for
     * the {@link GridRange2D} super-class which defines a {@code clone()} method, instead of
     * {@link GridRange2D} itself, for gaining some generality.
     */
    private static GridEnvelope clone(GridEnvelope gridRange) {
        if (gridRange instanceof Cloneable) {
            gridRange = (GridEnvelope) ((Cloneable) gridRange).clone();
        }
        return gridRange;
    }

    /**
     * Returns the number of dimensions.
     *
     * @return The number of dimensions.
     */
    public int getDimension() {
        if (gridToCRS != null) {
            return gridToCRS.getSourceDimensions();
        }
        return gridRange.getDimension();
    }

    /**
     * Returns the "real world" coordinate reference system.
     *
     * @return The coordinate reference system (never {@code null}).
     * @throws InvalidGridGeometryException if this grid geometry has no CRS (i.e.
     *         <code>{@linkplain #isDefined isDefined}({@linkplain #CRS_BITMASK})</code>
     *         returned {@code false}).
     *
     * @see GridGeometry2D#getCoordinateReferenceSystem2D
     *
     * @since 2.2
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem()
            throws InvalidGridGeometryException
    {
        if (envelope != null) {
            final CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
            if (crs != null) {
                assert isDefined(CRS_BITMASK);
                return crs;
            }
        }
        assert !isDefined(CRS_BITMASK);
        throw new InvalidGridGeometryException(ErrorKeys.UNSPECIFIED_CRS);
    }

    /**
     * Returns the bounding box of "real world" coordinates for this grid geometry. This envelope is
     * the {@linkplain #getGridRange grid range} {@linkplain #getGridToCoordinateSystem transformed}
     * to the "real world" coordinate system.
     *
     * @return The bounding box in "real world" coordinates (never {@code null}).
     * @throws InvalidGridGeometryException if this grid geometry has no envelope (i.e.
     *         <code>{@linkplain #isDefined isDefined}({@linkplain #ENVELOPE_BITMASK})</code>
     *         returned {@code false}).
     *
     * @see GridGeometry2D#getEnvelope2D
     */
    public Envelope getEnvelope() throws InvalidGridGeometryException {
        if (envelope!=null && !envelope.isNull()) {
            assert isDefined(ENVELOPE_BITMASK);
            return envelope.clone();
        }
        assert !isDefined(ENVELOPE_BITMASK);
        throw new InvalidGridGeometryException(gridToCRS == null ?
                    ErrorKeys.UNSPECIFIED_TRANSFORM : ErrorKeys.UNSPECIFIED_IMAGE_SIZE);
    }

    /**
     * Returns the valid coordinate range of a grid coverage. The lowest valid grid coordinate
     * is zero for {@link java.awt.image.BufferedImage}, but may be non-zero for arbitrary
     * {@link RenderedImage}. A grid with 512 cells can have a minimum coordinate of 0 and
     * maximum of 512, with 511 as the highest valid index.
     *
     * @return The grid range (never {@code null}).
     * @throws InvalidGridGeometryException if this grid geometry has no grid range (i.e.
     *         <code>{@linkplain #isDefined isDefined}({@linkplain #GRID_RANGE_BITMASK})</code>
     *         returned {@code false}).
     *
     * @see GridGeometry2D#getGridRange2D
     */
    public GridEnvelope getGridRange() throws InvalidGridGeometryException {
        if (gridRange != null) {
            assert isDefined(GRID_RANGE_BITMASK);
            return clone(gridRange);
           
        }
        assert !isDefined(GRID_RANGE_BITMASK);
        throw new InvalidGridGeometryException(ErrorKeys.UNSPECIFIED_IMAGE_SIZE);
    }

    /**
     * Returns the transform from grid coordinates to real world earth coordinates.
     * The transform is often an affine transform. The coordinate reference system of the
     * real world coordinates is given by
     * {@link org.opengis.coverage.Coverage#getCoordinateReferenceSystem}.
     * <p>
     * <strong>Note:</strong> OpenGIS requires that the transform maps <em>pixel centers</em>
     * to real world coordinates. This is different from some other systems that map pixel's
     * upper left corner.
     *
     * @return The transform (never {@code null}).
     * @throws InvalidGridGeometryException if this grid geometry has no transform (i.e.
     *         <code>{@linkplain #isDefined isDefined}({@linkplain #GRID_TO_CRS_BITMASK})</code>
     *         returned {@code false}).
     *
     * @see GridGeometry2D#getGridToCRS2D()
     *
     * @since 2.3
     */
    public MathTransform getGridToCRS() throws InvalidGridGeometryException {
        if (gridToCRS != null) {
            assert isDefined(GRID_TO_CRS_BITMASK);
            return gridToCRS;
        }
        assert !isDefined(GRID_TO_CRS_BITMASK);
        throw new InvalidGridGeometryException(ErrorKeys.UNSPECIFIED_TRANSFORM);
    }

    /**
     * Returns the transform from grid coordinates to real world earth coordinates.
     * This is similar to {@link #getGridToCRS()} except that the transform may maps
     * other parts than {@linkplain PixelInCell#CELL_CENTER pixel center}.
     *
     * @param  anchor The pixel part to map.
     * @return The transform (never {@code null}).
     * @throws InvalidGridGeometryException if this grid geometry has no transform (i.e.
     *         <code>{@linkplain #isDefined isDefined}({@linkplain #GRID_TO_CRS_BITMASK})</code>
     *         returned {@code false}).
     *
     * @see GridGeometry2D#getGridToCRS(org.opengis.referencing.datum.PixelInCell)
     *
     * @since 2.3
     */
    public MathTransform getGridToCRS(final PixelInCell anchor) throws InvalidGridGeometryException {
        if (gridToCRS == null) {
            throw new InvalidGridGeometryException(ErrorKeys.UNSPECIFIED_TRANSFORM);
        }
        if (PixelInCell.CELL_CENTER.equals(anchor)) {
            return gridToCRS;
        }
        if (PixelInCell.CELL_CORNER.equals(anchor)) {
            synchronized (this) {
                if (cornerToCRS == null) {
                    cornerToCRS = PixelTranslation.translate(gridToCRS, PixelInCell.CELL_CENTER, anchor);
                }
            }
            assert !cornerToCRS.equals(gridToCRS) : cornerToCRS;
            return cornerToCRS;
        }
        return PixelTranslation.translate(gridToCRS, PixelInCell.CELL_CENTER, anchor);
    }

    /**
     * Returns {@code true} if all the parameters specified by the argument are set.
     *
     * @param  bitmask Any combinaison of {@link #CRS_BITMASK}, {@link #ENVELOPE_BITMASK}, {@link #GRID_RANGE_BITMASK}
     *         and {@link #GRID_TO_CRS_BITMASK}.
     * @return {@code true} if all specified attributes are defined (i.e. invoking the
     *         corresponding method will not thrown an {@link InvalidGridGeometryException}).
     * @throws IllegalArgumentException if the specified bitmask is not a combinaison of known
     *         masks.
     *
     * @since 2.2
     *
     * @see javax.media.jai.ImageLayout#isValid
     */
    public boolean isDefined(final int bitmask) throws IllegalArgumentException {
        if ((bitmask & ~(CRS_BITMASK | ENVELOPE_BITMASK | GRID_RANGE_BITMASK | GRID_TO_CRS_BITMASK)) != 0) {
            throw new IllegalArgumentException(Errors.format(
                    ErrorKeys.ILLEGAL_ARGUMENT_$2, "bitmask", bitmask));
        }
        return ((bitmask & CRS_BITMASK)         == 0 || (envelope  != null && envelope.getCoordinateReferenceSystem() != null))
            && ((bitmask & ENVELOPE_BITMASK)    == 0 || (envelope  != null && !envelope.isNull()))
            && ((bitmask & GRID_RANGE_BITMASK)  == 0 || (gridRange != null))
            && ((bitmask & GRID_TO_CRS_BITMASK) == 0 || (gridToCRS != null));
    }

    /**
     * Returns a hash value for this grid geometry. This value need not remain
     * consistent between different implementations of the same class.
     */
    @Override
    public int hashCode() {
        int code = (int) serialVersionUID;
        if (gridToCRS != null) {
            code += gridToCRS.hashCode();
        }
        if (gridRange != null) {
            code += gridRange.hashCode();
        }
        // We do not check the envelope, since it usually has
        // a determinist relationship with other attributes.
        return code;
    }

    /**
     * Compares the specified object with this grid geometry for equality.
     *
     * @param object The object to compare with.
     * @return {@code true} if the given object is equals to this grid geometry.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final GeneralGridGeometry that = (GeneralGridGeometry) object;
            return Utilities.equals(this.gridRange, that.gridRange) &&
                   Utilities.equals(this.gridToCRS, that.gridToCRS) &&
                   Utilities.equals(this.envelope , that.envelope );
            // Do not compare cornerToCRS since it may not be computed yet,
            // and should be strictly derived from gridToCRS anyway.
        }
        return false;
    }

    /**
     * Returns a string representation of this grid geometry. The returned string
     * is implementation dependent. It is usually provided for debugging purposes.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) + '[' + gridRange + ", " + gridToCRS + ']';
    }

	/**
	 * Makes sure that an argument is non-null.
	 *
	 * @param  name   Argument name.
	 * @param  object User argument.
	 * @throws IllegalArgumentException if {@code object} is null.
	 */
	static void ensureNonNull(final String name, final Object object)
	    throws IllegalArgumentException
	{
	    if (object == null) {
	        throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
	    }
	}
}
