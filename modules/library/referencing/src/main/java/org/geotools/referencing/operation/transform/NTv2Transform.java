/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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

import au.com.objectix.jgridshift.GridShift;
import au.com.objectix.jgridshift.GridShiftFile;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.Parameter;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.gridshift.GridShiftLocator;
import org.geotools.referencing.factory.gridshift.NTv2GridShiftFactory;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.operation.Transformation;

/**
 * The "<cite>NTv2</cite>" coordinate transformation method (EPSG:9615).
 *
 * <p>This transformation depends on an external resource (the NTv2 grid file). If the file is not
 * available, a {@link NoSuchIdentifierException recoverable NoSuchIdentifierException} will be
 * thrown on instantiation.
 *
 * @see {@link IdentifiedObjectSet IdentifiedObjectSet exception handling}.
 * @version $Id$
 * @author Oscar Fonts
 */
public class NTv2Transform extends AbstractMathTransform implements MathTransform2D, Serializable {

    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -3082112044314062512L;

    /** Logger */
    protected static final Logger LOGGER = Logging.getLogger(NTv2Transform.class);

    /** The original grid name */
    private URI grid = null;

    /** The grid file name as set in the constructor. */
    private URL gridLocation = null;

    /** The grid shift to be used */
    private GridShiftFile gridShift;

    /** The factory that loads the grid shift files */
    private static NTv2GridShiftFactory FACTORY = new NTv2GridShiftFactory();

    /** The inverse of this transform. Will be created only when needed. */
    private transient MathTransform2D inverse;

    /**
     * Constructs a {@code NTv2Transform} from the specified grid shift file.
     *
     * <p>This constructor checks for grid shift file availability, but doesn't actually load the
     * full grid into memory to preserve resources.
     *
     * @param file NTv2 grid file name
     * @throws NoSuchIdentifierException if the grid is not available.
     */
    public NTv2Transform(URI file) throws NoSuchIdentifierException {
        if (file == null) {
            throw new NoSuchIdentifierException("No NTv2 Grid File specified.", null);
        }

        this.grid = file;

        gridLocation = locateGrid(grid.toString());
        if (gridLocation == null) {
            throw new NoSuchIdentifierException("Could not locate NTv2 Grid File " + file, null);
        }

        // Search for grid file
        if (!FACTORY.isNTv2Grid(gridLocation)) {
            throw new NoSuchIdentifierException("NTv2 Grid File not available.", file.toString());
        }
    }

    URL locateGrid(String grid) {
        for (GridShiftLocator locator : ReferencingFactoryFinder.getGridShiftLocators(null)) {
            URL result = locator.locateGrid(grid);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /** Returns a hash value for this transform. */
    @Override
    public int hashCode() {
        return this.grid.hashCode();
    }

    /**
     * Compares the specified object with this one for equality. Checks if {@code object} is {@code
     * this} same instance, or a NTv2Transform with the same parameter values.
     *
     * @param object The object to compare with this transform.
     * @return {@code true} if the given object is {@code this}, or a NTv2Transform with same
     *     parameter values, which would mean that given identical source position, the {@linkplain
     *     #transform(DirectPosition,DirectPosition) transformed} position would be the same.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) return true;

        if (object != null && getClass().equals(object.getClass())) {
            final NTv2Transform that = (NTv2Transform) object;
            return Utilities.equals(this.getParameterValues(), that.getParameterValues());
        }
        return false;
    }

    /**
     * Returns the inverse of this transform.
     *
     * @return the inverse of this transform
     */
    @Override
    public synchronized MathTransform2D inverse() {
        if (inverse == null) {
            inverse = new Inverse();
        }
        return inverse;
    }

    /**
     * Transforms a list of coordinate point ordinal values. This method is provided for efficiently
     * transforming many points. The supplied array of ordinal values will contain packed ordinal
     * values. For example, if the source dimension is 3, then the ordinals will be packed in this
     * order: (<var>x<sub>0</sub></var>,<var>y<sub>0</sub></var>,<var>z<sub>0</sub></var>,
     *
     * <p><var>x<sub>1</sub></var>,<var>y<sub>1</sub></var>,<var>z<sub>1</sub></var> ...).
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed in the source array.
     * @param dstPts the array into which the transformed point coordinates are returned. May be the
     *     same than {@code srcPts}.
     * @param dstOff the offset to the location of the first transformed point that is stored in the
     *     destination array.
     * @param numPts the number of point objects to be transformed.
     * @throws TransformException if an IO error occurs reading the grid file.
     */
    @Override
    public void transform(double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        bidirectionalTransform(srcPts, srcOff, dstPts, dstOff, numPts, true);
    }

    /**
     * Inverse transform. See {@link #transform(double[], int, double[], int, int)}
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed in the source array.
     * @param dstPts the array into which the transformed point coordinates are returned. May be the
     *     same than {@code srcPts}.
     * @param dstOff the offset to the location of the first transformed point that is stored in the
     *     destination array.
     * @param numPts the number of point objects to be transformed.
     * @throws TransformException if an IO error occurs reading the grid file.
     */
    public void inverseTransform(
            double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts)
            throws TransformException {
        bidirectionalTransform(srcPts, srcOff, dstPts, dstOff, numPts, false);
    }

    /**
     * Performs the actual transformation.
     *
     * @param srcPts the array containing the source point coordinates.
     * @param srcOff the offset to the first point to be transformed in the source array.
     * @param dstPts the array into which the transformed point coordinates are returned. May be the
     *     same than {@code srcPts}.
     * @param dstOff the offset to the location of the first transformed point that is stored in the
     *     destination array.
     * @param numPts the number of point objects to be transformed.
     * @param forward {@code true} for direct transform, {@code false} for inverse transform.
     * @throws TransformException if an IO error occurs reading the grid file.
     */
    private void bidirectionalTransform(
            double[] srcPts, int srcOff, double[] dstPts, int dstOff, int numPts, boolean forward)
            throws TransformException {

        boolean shifted;

        if (gridShift == null) { // Create grid when first needed.
            try {
                gridShift = FACTORY.createNTv2Grid(gridLocation);
            } catch (FactoryException e) {
                throw new TransformException(
                        "NTv2 Grid " + gridLocation + " Could not be created", e);
            }
        }

        try {
            GridShift shift = new GridShift();
            while (--numPts >= 0) {
                shift.setLonPositiveEastDegrees(srcPts[srcOff++]);
                shift.setLatDegrees(srcPts[srcOff++]);
                if (forward) {
                    shifted = gridShift.gridShiftForward(shift);
                } else {
                    shifted = gridShift.gridShiftReverse(shift);
                }
                if (shifted) {
                    dstPts[dstOff++] = shift.getShiftedLonPositiveEastDegrees();
                    dstPts[dstOff++] = shift.getShiftedLatDegrees();
                } else {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(
                                Level.FINE,
                                "Point ("
                                        + srcPts[srcOff - 2]
                                        + ", "
                                        + srcPts[srcOff - 1]
                                        + ") is not covered by '"
                                        + this.grid
                                        + "' NTv2 grid,"
                                        + " it will not be shifted.");
                    }
                    dstPts[dstOff++] = srcPts[srcOff - 2];
                    dstPts[dstOff++] = srcPts[srcOff - 1];
                }
            }
        } catch (IOException e) {
            throw new TransformException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public int getSourceDimensions() {
        return 2;
    }

    @Override
    public int getTargetDimensions() {
        return 2;
    }

    /**
     * Returns the parameter values for this math transform.
     *
     * @return A copy of the parameter values for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValue<URI> file = new Parameter<URI>(Provider.FILE);
        file.setValue(grid);

        return new ParameterGroup(Provider.PARAMETERS, new ParameterValue[] {file});
    }

    /**
     * Inverse of a {@link NTv2Transform}.
     *
     * @version $Id$
     * @author Oscar Fonts
     */
    private final class Inverse extends AbstractMathTransform.Inverse
            implements MathTransform2D, Serializable {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = -4707304160205218546L;

        /** Default constructor. */
        public Inverse() {
            NTv2Transform.this.super();
        }

        /**
         * Returns the parameter values for this math transform.
         *
         * @return A copy of the parameter values for this math transform.
         */
        @Override
        public ParameterValueGroup getParameterValues() {
            return null;
        }

        /**
         * Inverse transform an array of points.
         *
         * @throws TransformException if the input point is outside the area covered by this grid.
         */
        public void transform(
                final double[] source,
                final int srcOffset,
                final double[] dest,
                final int dstOffset,
                final int length)
                throws TransformException {
            NTv2Transform.this.inverseTransform(source, srcOffset, dest, dstOffset, length);
        }

        /** Returns the original transform. */
        @Override
        public MathTransform2D inverse() {
            return (MathTransform2D) super.inverse();
        }

        /** Restore reference to this object after deserialization. */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            NTv2Transform.this.inverse = this;
        }
    }

    /**
     * The {@link NTv2Transform} provider.
     *
     * @author Oscar Fonts
     */
    public static class Provider extends MathTransformProvider {

        private static final long serialVersionUID = -3710592152744574801L;

        /**
         * The operation parameter descriptor for the "Latitude and longitude difference file"
         * parameter value. The default value is "".
         */
        public static final DefaultParameterDescriptor<URI> FILE =
                new DefaultParameterDescriptor<URI>(
                        toMap(
                                new NamedIdentifier[] {
                                    new NamedIdentifier(
                                            Citations.EPSG,
                                            "Latitude and longitude difference file"),
                                    new NamedIdentifier(Citations.EPSG, "8656")
                                }),
                        URI.class,
                        null,
                        null,
                        null,
                        null,
                        null,
                        true);

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS =
                createDescriptorGroup(
                        new NamedIdentifier[] {
                            new NamedIdentifier(Citations.EPSG, "NTv2"),
                            new NamedIdentifier(Citations.EPSG, "9615")
                        },
                        new ParameterDescriptor[] {FILE});

        /** Constructs a provider. */
        public Provider() {
            super(2, 2, PARAMETERS);
        }

        /** Returns the operation type. */
        @Override
        public Class<Transformation> getOperationType() {
            return Transformation.class;
        }

        /**
         * Creates a math transform from the specified group of parameter values.
         *
         * @param values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         * @throws FactoryException if there is a problem creating this math transform.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException, FactoryException {
            return new NTv2Transform(value(FILE, values));
        }
    }
}
