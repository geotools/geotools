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
package org.geotools.coverage;

// J2SE and JAI dependencies
import javax.media.jai.PropertySource;

import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.coverage.CannotEvaluateException;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.coverage.SampleDimension;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


/**
 * A coverage wrapping an other one with a different coordinate reference system. The coordinate
 * transformation is applied on the fly every time an {@code evaluate} method is invoked. It may
 * be efficient if few points are queried, but become ineficient if a large amount of points is
 * queried. In the later case, consider reprojecting the whole grid coverage instead.
 * <br><br>
 * <strong>Note:</strong> This class is not thread safe for performance reasons. If desired,
 * users should create one instance of {@code TransformedCoverage} for each thread.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class TransformedCoverage extends AbstractCoverage {
    /**
     * The hints for the creation of coordinate operation.
     * The default coordinate operation factory should be suffisient.
     */
    private static final Hints HINTS = null;

    /**
     * The wrapped coverage.
     */
    protected final Coverage coverage;

    /**
     * The transform from this coverage CRS to the wrapped coverage CRS.
     */
    private final MathTransform toWrapped;

    /**
     * The projected point.
     */
    private final GeneralDirectPosition position;
    
    /**
     * Creates a new coverage wrapping the specified one.
     *
     * @param  name     The name for this new coverage.
     * @param  crs      The crs for this coverage.
     * @param  coverage The coverage to wraps.
     * @throws FactoryException if no transformation can be found from the coverage CRS to the
     *         specified CRS.
     */
    protected TransformedCoverage(final CharSequence name,
                                  final CoordinateReferenceSystem crs,
                                  final Coverage coverage) throws FactoryException
    {
        super(name, crs,
              (coverage instanceof PropertySource) ? ((PropertySource) coverage) : null, null);
        this.coverage = coverage;
        position = new GeneralDirectPosition(crs.getCoordinateSystem().getDimension());
        toWrapped = ReferencingFactoryFinder.getCoordinateOperationFactory(HINTS)
                                 .createOperation(crs, coverage.getCoordinateReferenceSystem())
                                 .getMathTransform();
    }

    /**
     * Creates a new coverage wrapping the specified one with a different CRS.
     * If the specified coverage already uses the specified CRS (or an equivalent one),
     * it is returned unchanged.
     *
     * @param  name     The name for this new coverage.
     * @param  crs      The crs for this coverage.
     * @param  coverage The coverage to wraps.
     * @return A coverage using the specified CRS.
     * @throws FactoryException if no transformation can be found from the coverage CRS to the
     *         specified CRS.
     */
    public static Coverage reproject(final CharSequence name, final CoordinateReferenceSystem crs,
                                     Coverage coverage) throws FactoryException
    {
        while (true) {
            if (CRS.equalsIgnoreMetadata(coverage.getCoordinateReferenceSystem(), crs)) {
                return coverage;
            }
            if (TransformedCoverage.class.equals(coverage.getClass())) {
                coverage = ((TransformedCoverage) coverage).coverage;
                continue;
            }
            break;
        }
        return new TransformedCoverage(name, crs, coverage);
    }

    /**
     * The number of sample dimensions in the coverage.
     * For grid coverages, a sample dimension is a band.
     *
     * @return The number of sample dimensions in the coverage.
     */
    public int getNumSampleDimensions() {
        return coverage.getNumSampleDimensions();
    }

    /**
     * Retrieve sample dimension information for the coverage.
     *
     * @param  index Index for sample dimension to retrieve. Indices are numbered 0 to
     *         (<var>{@linkplain #getNumSampleDimensions n}</var>-1).
     * @return Sample dimension information for the coverage.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    public SampleDimension getSampleDimension(final int index) throws IndexOutOfBoundsException {
        return coverage.getSampleDimension(index);
    }

    /**
     * Wraps the checked exception into an unchecked one.
     *
     * @todo Provides a localized message.
     */
    private final CannotEvaluateException transformationFailed(final TransformException cause) {
        return new CannotEvaluateException("Transformation failed", cause);
    }

    /**
     * Returns the envelope.
     */
    public Envelope getEnvelope() {
        final GeneralEnvelope envelope;
        try {
            envelope = CRS.transform(toWrapped.inverse(), coverage.getEnvelope());
        } catch (TransformException exception) {
            throw transformationFailed(exception);
        }
        envelope.setCoordinateReferenceSystem(crs);
        return envelope;
    }

    /**
     * Returns the value vector for a given point in the coverage.
     *
     * @param  coord The coordinate point where to evaluate.
     * @throws PointOutsideCoverageException if {@code coord} is outside coverage.
     * @throws CannotEvaluateException if the computation failed for some other reason.
     */
    public final Object evaluate(final DirectPosition coord)
            throws CannotEvaluateException
    {
        try {
            return coverage.evaluate(toWrapped.transform(coord, position));
        } catch (TransformException exception) {
            throw transformationFailed(exception);
        }
    }
    
    /**
     * Returns a sequence of boolean values for a given point in the coverage.
     */
    public final boolean[] evaluate(final DirectPosition coord, boolean[] dest)
            throws CannotEvaluateException
    {
        try {
            return coverage.evaluate(toWrapped.transform(coord, position), dest);
        } catch (TransformException exception) {
            throw transformationFailed(exception);
        }
    }
    
    /**
     * Returns a sequence of byte values for a given point in the coverage.
     */
    public final byte[] evaluate(final DirectPosition coord, byte[] dest)
            throws CannotEvaluateException
    {
        try {
            return coverage.evaluate(toWrapped.transform(coord, position), dest);
        } catch (TransformException exception) {
            throw transformationFailed(exception);
        }
    }
    
    /**
     * Returns a sequence of integer values for a given point in the coverage.
     */
    public final int[] evaluate(final DirectPosition coord, int[] dest)
            throws CannotEvaluateException
    {
        try {
            return coverage.evaluate(toWrapped.transform(coord, position), dest);
        } catch (TransformException exception) {
            throw transformationFailed(exception);
        }
    }

    /**
     * Returns a sequence of float values for a given point in the coverage.
     */
    public final float[] evaluate(final DirectPosition coord, float[] dest)
            throws CannotEvaluateException
    {
        try {
            return coverage.evaluate(toWrapped.transform(coord, position), dest);
        } catch (TransformException exception) {
            throw transformationFailed(exception);
        }
    }

    /**
     * Returns a sequence of double values for a given point in the coverage.
     */
    public final double[] evaluate(final DirectPosition coord, final double[] dest)
            throws CannotEvaluateException
    {
        try {
            return coverage.evaluate(toWrapped.transform(coord, position), dest);
        } catch (TransformException exception) {
            throw transformationFailed(exception);
        }
    }    
}
