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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation;

import java.util.Map;
import java.util.List;
import java.util.Collections;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Duration;
import javax.vecmath.SingularMatrixException;

import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;

import org.geotools.factory.Hints;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.operation.matrix.XMatrix;
import org.geotools.referencing.operation.matrix.Matrix4;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;

import static org.geotools.referencing.CRS.equalsIgnoreMetadata;
import static org.geotools.referencing.AbstractIdentifiedObject.nameMatches;
import static org.geotools.referencing.operation.ProjectionAnalyzer.createLinearConversion;


/**
 * Creates {@linkplain CoordinateOperation coordinate operations}. This factory is capable to find
 * coordinate {@linkplain Transformation transformations} or {@linkplain Conversion conversions}
 * between two {@linkplain CoordinateReferenceSystem coordinate reference systems}. It delegates
 * most of its work to one or many of {@code createOperationStep} methods. Subclasses can
 * override those methods in order to extend the factory capability to some more CRS.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @tutorial http://docs.codehaus.org/display/GEOTOOLS/Coordinate+Transformation+Services+for+Geotools+2.1
 */
public class DefaultCoordinateOperationFactory extends AbstractCoordinateOperationFactory {
    /**
     * The priority level for this factory.
     */
    static final int PRIORITY = NORMAL_PRIORITY;

    /**
     * Small number for floating point comparaisons.
     */
    private static final double EPS = 1E-10;

    /**
     * A unit of one millisecond.
     */
    private static final Unit<Duration> MILLISECOND = SI.MILLI(SI.SECOND);

    /**
     * The operation to use by {@link #createTransformationStep(GeographicCRS,GeographicCRS)} for
     * datum shift. This string can have one of the following values:
     * <p>
     * <ul>
     *   <li><code>"Abridged_Molodenski"</code> for the abridged Molodenski transformation.</li>
     *   <li><code>"Molodenski"</code> for the Molodenski transformation.</li>
     *   <li>{@code null} for performing datum shifts is geocentric coordinates.</li>
     * </ul>
     */
    private final String molodenskiMethod;

    /**
     * {@code true} if datum shift are allowed even if no Bursa Wolf parameters is available.
     */
    private final boolean lenientDatumShift;

    /**
     * Constructs a coordinate operation factory using the default factories.
     */
    public DefaultCoordinateOperationFactory() {
        this(null);
    }

    /**
     * Constructs a coordinate operation factory using the specified hints.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     *
     * @param userHints The hints, or {@code null} if none.
     */
    public DefaultCoordinateOperationFactory(final Hints userHints) {
        this(userHints, PRIORITY);
    }

    /**
     * Constructs a coordinate operation factory using the specified hints and priority.
     * This constructor recognizes the {@link Hints#CRS_FACTORY CRS}, {@link Hints#CS_FACTORY CS},
     * {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY MATH_TRANSFORM}
     * {@code FACTORY} hints.
     *
     * @param userHints The hints, or {@code null} if none.
     * @param priority The priority for this factory, as a number between
     *        {@link #MINIMUM_PRIORITY MINIMUM_PRIORITY} and
     *        {@link #MAXIMUM_PRIORITY MAXIMUM_PRIORITY} inclusive.
     *
     * @since 2.2
     */
    public DefaultCoordinateOperationFactory(final Hints userHints, final int priority) {
        super(userHints, priority);
        //
        // Default hints values
        //
        String  molodenskiMethod  = "Molodenski"; // Alternative: "Abridged_Molodenski"
        boolean lenientDatumShift = false;
        //
        // Fetchs the user-supplied hints
        //
        if (userHints != null) {
            Object candidate = userHints.get(Hints.DATUM_SHIFT_METHOD);
            if (candidate instanceof String) {
                molodenskiMethod = (String) candidate;
                if (molodenskiMethod.trim().equalsIgnoreCase("Geocentric")) {
                    molodenskiMethod = null;
                }
            }
            candidate = userHints.get(Hints.LENIENT_DATUM_SHIFT);
            if (candidate instanceof Boolean) {
                lenientDatumShift = ((Boolean) candidate).booleanValue();
            }
        }
        //
        // Stores the retained hints
        //
        this.molodenskiMethod  = molodenskiMethod;
        this.lenientDatumShift = lenientDatumShift;
        this.hints.put(Hints.DATUM_SHIFT_METHOD,  molodenskiMethod);
        this.hints.put(Hints.LENIENT_DATUM_SHIFT, Boolean.valueOf(lenientDatumShift));
    }

    /**
     * Returns an operation for conversion or transformation between two coordinate reference
     * systems. If an operation exists, it is returned. If more than one operation exists, the
     * default is returned. If no operation exists, then the exception is thrown.
     * <P>
     * The default implementation inspects the CRS and delegates the work to one or
     * many {@code createOperationStep(...)} methods. This method fails if no path
     * between the CRS is found.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws OperationNotFoundException if no operation path was found from {@code sourceCRS}
     *         to {@code targetCRS}.
     * @throws FactoryException if the operation creation failed for some other reason.
     */
    public CoordinateOperation createOperation(final CoordinateReferenceSystem sourceCRS,
                                               final CoordinateReferenceSystem targetCRS)
            throws OperationNotFoundException, FactoryException
    {
        ensureNonNull("sourceCRS", sourceCRS);
        ensureNonNull("targetCRS", targetCRS);
        if (equalsIgnoreMetadata(sourceCRS, targetCRS)) {
            final int dim  = getDimension(sourceCRS);
            assert    dim == getDimension(targetCRS) : dim;
            return createFromAffineTransform(IDENTITY, sourceCRS, targetCRS,
                                             MatrixFactory.create(dim+1));
        } else {
            // Query the database (if any) before to try to find the operation by ourself.
            final CoordinateOperation candidate = createFromDatabase(sourceCRS, targetCRS);
            if (candidate != null) {
                return candidate;
            }
        }
        /*
         * Before to process, performs a special check for CompoundCRS where the target contains
         * every elements included in the source.  CompoundCRS are usually verified last, but in
         * the special case described above there is only axis switch or dimension to remove. If
         * we wait last for processing them, what would have been a simple CoordinateOperation
         * may become interleaved with more complex operation (e.g. projection followed by the
         * inverse projection, before they get simplified by DefaultConcatenatedOperation, etc.).
         */
        if (sourceCRS instanceof CompoundCRS) {
            final List<SingleCRS> sources = DefaultCompoundCRS.getSingleCRS(sourceCRS);
            final List<SingleCRS> targets = DefaultCompoundCRS.getSingleCRS(targetCRS);
            if (containsIgnoreMetadata(sources, targets)) {
                final CompoundCRS source = (CompoundCRS) sourceCRS;
                if (targetCRS instanceof CompoundCRS) {
                    final CompoundCRS target = (CompoundCRS) targetCRS;
                    return createOperationStep(source, target);
                }
                if (targetCRS instanceof SingleCRS) {
                    final SingleCRS target = (SingleCRS) targetCRS;
                    return createOperationStep(source, target);
                }
            }
        }
        /////////////////////////////////////////////////////////////////////
        ////                                                             ////
        ////     Geographic  -->  Geographic, Projected or Geocentric    ////
        ////                                                             ////
        /////////////////////////////////////////////////////////////////////
        if (sourceCRS instanceof GeographicCRS) {
            final GeographicCRS source = (GeographicCRS) sourceCRS;
            if (targetCRS instanceof GeographicCRS) {
                final GeographicCRS target = (GeographicCRS) targetCRS;
                return createOperationStep(source, target);
            }
            if (targetCRS instanceof ProjectedCRS) {
                final ProjectedCRS target = (ProjectedCRS) targetCRS;
                return createOperationStep(source, target);
            }
            if (targetCRS instanceof GeocentricCRS) {
                final GeocentricCRS target = (GeocentricCRS) targetCRS;
                return createOperationStep(source, target);
            }
            if (targetCRS instanceof VerticalCRS) {
                final VerticalCRS target = (VerticalCRS) targetCRS;
                return createOperationStep(source, target);
            }
        }
        /////////////////////////////////////////////////////////
        ////                                                 ////
        ////     Projected  -->  Projected or Geographic     ////
        ////                                                 ////
        /////////////////////////////////////////////////////////
        if (sourceCRS instanceof ProjectedCRS) {
            final ProjectedCRS source = (ProjectedCRS) sourceCRS;
            if (targetCRS instanceof ProjectedCRS) {
                final ProjectedCRS target = (ProjectedCRS) targetCRS;
                return createOperationStep(source, target);
            }
            if (targetCRS instanceof GeographicCRS) {
                final GeographicCRS target = (GeographicCRS) targetCRS;
                return createOperationStep(source, target);
            }
        }
        //////////////////////////////////////////////////////////
        ////                                                  ////
        ////     Geocentric  -->  Geocentric or Geographic    ////
        ////                                                  ////
        //////////////////////////////////////////////////////////
        if (sourceCRS instanceof GeocentricCRS) {
            final GeocentricCRS source = (GeocentricCRS) sourceCRS;
            if (targetCRS instanceof GeocentricCRS) {
                final GeocentricCRS target = (GeocentricCRS) targetCRS;
                return createOperationStep(source, target);
            }
            if (targetCRS instanceof GeographicCRS) {
                final GeographicCRS target = (GeographicCRS) targetCRS;
                return createOperationStep(source, target);
            }
        }
        /////////////////////////////////////////
        ////                                 ////
        ////     Vertical  -->  Vertical     ////
        ////                                 ////
        /////////////////////////////////////////
        if (sourceCRS instanceof VerticalCRS) {
            final VerticalCRS source = (VerticalCRS) sourceCRS;
            if (targetCRS instanceof VerticalCRS) {
                final VerticalCRS target = (VerticalCRS) targetCRS;
                return createOperationStep(source, target);
            }
        }
        /////////////////////////////////////////
        ////                                 ////
        ////     Temporal  -->  Temporal     ////
        ////                                 ////
        /////////////////////////////////////////
        if (sourceCRS instanceof TemporalCRS) {
            final TemporalCRS source = (TemporalCRS) sourceCRS;
            if (targetCRS instanceof TemporalCRS) {
                final TemporalCRS target = (TemporalCRS) targetCRS;
                return createOperationStep(source, target);
            }
        }
        //////////////////////////////////////////////////////////////////
        ////                                                          ////
        ////     Any coordinate reference system -->  Derived CRS     ////
        ////                                                          ////
        //////////////////////////////////////////////////////////////////
        if (targetCRS instanceof GeneralDerivedCRS) {
            // Note: this code is identical to 'createOperationStep(GeographicCRS, ProjectedCRS)'
            //       except that the later invokes directly the right method for 'step1' instead
            //       of invoking 'createOperation' recursively.
            final GeneralDerivedCRS  target = (GeneralDerivedCRS) targetCRS;
            final CoordinateReferenceSystem base = target.getBaseCRS();
            final CoordinateOperation step1 = createOperation(sourceCRS, base);
            final CoordinateOperation step2 = target.getConversionFromBase();
            return concatenate(step1, step2);
        }
        //////////////////////////////////////////////////////////////////
        ////                                                          ////
        ////     Derived CRS -->  Any coordinate reference system     ////
        ////                                                          ////
        //////////////////////////////////////////////////////////////////
        if (sourceCRS instanceof GeneralDerivedCRS) {
            // Note: this code is identical to 'createOperationStep(ProjectedCRS, GeographicCRS)'
            //       except that the later invokes directly the right method for 'step2' instead
            //       of invoking 'createOperation' recursively.
            final GeneralDerivedCRS       source = (GeneralDerivedCRS) sourceCRS;
            final CoordinateReferenceSystem base = source.getBaseCRS();
            final CoordinateOperation      step2 = createOperation(base, targetCRS);
            CoordinateOperation            step1 = source.getConversionFromBase();
            MathTransform              transform = step1.getMathTransform();
            try {
                transform = transform.inverse();
            } catch (NoninvertibleTransformException exception) {
                throw new OperationNotFoundException(getErrorMessage(sourceCRS, base), exception);
            }
            step1 = createFromMathTransform(INVERSE_OPERATION, sourceCRS, base, transform);
            return concatenate(step1, step2);
        }
        ////////////////////////////////////////////
        ////                                    ////
        ////     Compound  -->  various CRS     ////
        ////                                    ////
        ////////////////////////////////////////////
        if (sourceCRS instanceof CompoundCRS) {
            final CompoundCRS source = (CompoundCRS) sourceCRS;
            if (targetCRS instanceof CompoundCRS) {
                final CompoundCRS target = (CompoundCRS) targetCRS;
                return createOperationStep(source, target);
            }
            if (targetCRS instanceof SingleCRS) {
                final SingleCRS target = (SingleCRS) targetCRS;
                return createOperationStep(source, target);
            }
        }
        if (targetCRS instanceof CompoundCRS) {
            final CompoundCRS target = (CompoundCRS) targetCRS;
            if (sourceCRS instanceof SingleCRS) {
                final SingleCRS source = (SingleCRS) sourceCRS;
                return createOperationStep(source, target);
            }
        }
        /////////////////////////////////////////
        ////                                 ////
        ////     Generic  -->  various CS    ////
        ////     Various CS --> Generic      ////
        ////                                 ////
        /////////////////////////////////////////
        if (sourceCRS == DefaultEngineeringCRS.GENERIC_2D ||
            targetCRS == DefaultEngineeringCRS.GENERIC_2D ||
            sourceCRS == DefaultEngineeringCRS.GENERIC_3D ||
            targetCRS == DefaultEngineeringCRS.GENERIC_3D)
        {
            final int dimSource = getDimension(sourceCRS);
            final int dimTarget = getDimension(targetCRS);
            if (dimTarget == dimSource) {
                final Matrix matrix = MatrixFactory.create(dimTarget+1, dimSource+1);
                return createFromAffineTransform(IDENTITY, sourceCRS, targetCRS, matrix);
            }
        }
        throw new OperationNotFoundException(getErrorMessage(sourceCRS, targetCRS));
    }

    /**
     * Returns an operation using a particular method for conversion or transformation
     * between two coordinate reference systems.
     * If the operation exists on the implementation, then it is returned.
     * If the operation does not exist on the implementation, then the implementation has the option
     * of inferring the operation from the argument objects.
     * If for whatever reason the specified operation will not be returned, then the exception is
     * thrown.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @param  method the algorithmic method for conversion or transformation
     * @throws OperationNotFoundException if no operation path was found from {@code sourceCRS}
     *         to {@code targetCRS}.
     * @throws FactoryException if the operation creation failed for some other reason.
     *
     * @deprecated Current implementation ignore the {@code method} argument.
     */
    public CoordinateOperation createOperation(final CoordinateReferenceSystem sourceCRS,
                                               final CoordinateReferenceSystem targetCRS,
                                               final OperationMethod           method)
            throws OperationNotFoundException, FactoryException
    {
        return createOperation(sourceCRS, targetCRS);
    }




    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////                                                         ////////////
    ////////////               N O R M A L I Z A T I O N S               ////////////
    ////////////                                                         ////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Makes sure that the specified geocentric CRS uses standard axis,
     * prime meridian and the specified datum.
     * If {@code crs} already meets all those conditions, then it is
     * returned unchanged. Otherwise, a new normalized geocentric CRS is
     * created and returned.
     *
     * @param  crs The geocentric coordinate reference system to normalize.
     * @param  datum The expected datum.
     * @return The normalized coordinate reference system.
     * @throws FactoryException if the construction of a new CRS was needed but failed.
     */
    private GeocentricCRS normalize(final GeocentricCRS crs,
                                    final GeodeticDatum datum)
            throws FactoryException
    {
        final CartesianCS   STANDARD  = DefaultCartesianCS.GEOCENTRIC;
        final GeodeticDatum candidate = crs.getDatum();
        if (equalsIgnorePrimeMeridian(candidate, datum)) {
            if (getGreenwichLongitude(candidate.getPrimeMeridian()) ==
                getGreenwichLongitude(datum    .getPrimeMeridian()))
            {
                if (hasStandardAxis(crs.getCoordinateSystem(), STANDARD)) {
                    return crs;
                }
            }
        }
        final CRSFactory crsFactory = getFactoryContainer().getCRSFactory();
        return crsFactory.createGeocentricCRS(getTemporaryName(crs), datum, STANDARD);
    }

    /**
     * Makes sure that the specified geographic CRS uses standard axis (longitude and latitude in
     * decimal degrees). Optionally, this method can also make sure that the CRS use the Greenwich
     * prime meridian. Other datum properties are left unchanged. If {@code crs} already meets all
     * those conditions, then it is returned unchanged. Otherwise, a new normalized geographic CRS
     * is created and returned.
     *
     * @param  crs The geographic coordinate reference system to normalize.
     * @param  forceGreenwich {@code true} for forcing the Greenwich prime meridian.
     * @return The normalized coordinate reference system.
     * @throws FactoryException if the construction of a new CRS was needed but failed.
     */
    private GeographicCRS normalize(final GeographicCRS      crs,
                                    final boolean forceGreenwich)
            throws FactoryException
    {
              GeodeticDatum datum = crs.getDatum();
        final EllipsoidalCS cs    = crs.getCoordinateSystem();
        final EllipsoidalCS STANDARD = (cs.getDimension() <= 2) ?
                                        DefaultEllipsoidalCS.GEODETIC_2D :
                                        DefaultEllipsoidalCS.GEODETIC_3D;
        if (forceGreenwich && getGreenwichLongitude(datum.getPrimeMeridian()) != 0) {
            datum = new TemporaryDatum(datum);
        } else if (hasStandardAxis(cs, STANDARD)) {
            return crs;
        }
        /*
         * The specified geographic coordinate system doesn't use standard axis
         * (EAST, NORTH) or the greenwich meridian. Create a new one meeting those criterions.
         */
        final CRSFactory crsFactory = getFactoryContainer().getCRSFactory();
        return crsFactory.createGeographicCRS(getTemporaryName(crs), datum, STANDARD);
    }

    /**
     * A datum identical to the specified datum except for the prime meridian, which is replaced
     * by Greenwich. This datum is processed in a special way by {@link #equalsIgnorePrimeMeridian}.
     */
    private static final class TemporaryDatum extends DefaultGeodeticDatum {
        /** For cros-version compatibility. */
        private static final long serialVersionUID = -8964199103509187219L;

        /** The wrapped datum. */
        private final GeodeticDatum datum;

        /** Wrap the specified datum. */
        public TemporaryDatum(final GeodeticDatum datum) {
            super(getTemporaryName(datum), datum.getEllipsoid(), DefaultPrimeMeridian.GREENWICH);
            this.datum = datum;
        }

        /** Unwrap the datum. */
        public static GeodeticDatum unwrap(GeodeticDatum datum) {
            while (datum instanceof TemporaryDatum) {
                datum = ((TemporaryDatum) datum).datum;
            }
            return datum;
        }

        /** Compares this datum with the specified object for equality. */
        @Override
        public boolean equals(final AbstractIdentifiedObject object,
                              final boolean compareMetadata)
        {
            if (super.equals(object, compareMetadata)) {
                final GeodeticDatum other = ((TemporaryDatum) object).datum;
                return compareMetadata ? datum.equals(other) : equalsIgnoreMetadata(datum, other);
            }
            return false;
        }
    }

    /**
     * Returns {@code true} if the specified coordinate system
     * use standard axis and units.
     *
     * @param crs  The coordinate system to test.
     * @param standard The coordinate system that defines the standard. Usually
     *        {@link DefaultEllipsoidalCS#GEODETIC_2D} or
     *        {@link DefaultCartesianCS#PROJECTED}.
     */
    private static boolean hasStandardAxis(final CoordinateSystem cs,
                                           final CoordinateSystem standard)
    {
        final int dimension = standard.getDimension();
        if (cs.getDimension() != dimension) {
            return false;
        }
        for (int i=0; i<dimension; i++) {
            final CoordinateSystemAxis a1 =       cs.getAxis(i);
            final CoordinateSystemAxis a2 = standard.getAxis(i);
            if (!a1.getDirection().equals(a2.getDirection()) ||
                !a1.getUnit()     .equals(a2.getUnit()))
            {
                return false;
            }
        }
        return true;
    }




    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////                                                         ////////////
    ////////////            A X I S   O R I E N T A T I O N S            ////////////
    ////////////                                                         ////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns an affine transform between two ellipsoidal coordinate systems. Only
     * units, axis order (e.g. transforming from (NORTH,WEST) to (EAST,NORTH)) and
     * prime meridian are taken in account. Other attributes (especially the datum)
     * must be checked before invoking this method.
     *
     * @param  sourceCS The source coordinate system.
     * @param  targetCS The target coordinate system.
     * @param  sourcePM The source prime meridian.
     * @param  targetPM The target prime meridian.
     * @return The transformation from {@code sourceCS} to {@code targetCS} as
     *         an affine transform. Only axis orientation, units and prime meridian are
     *         taken in account.
     * @throws OperationNotFoundException If the affine transform can't be constructed.
     */
    private Matrix swapAndScaleAxis(final EllipsoidalCS sourceCS,
                                    final EllipsoidalCS targetCS,
                                    final PrimeMeridian sourcePM,
                                    final PrimeMeridian targetPM)
            throws OperationNotFoundException
    {
        final Matrix matrix = swapAndScaleAxis(sourceCS, targetCS);
        for (int i=targetCS.getDimension(); --i>=0;) {
            final CoordinateSystemAxis axis = targetCS.getAxis(i);
            final AxisDirection direction = axis.getDirection();
            if (AxisDirection.EAST.equals(direction.absolute())) {
                /*
                 * A longitude ordinate has been found (i.e. the axis is oriented toward EAST or
                 * WEST). Compute the amount of angle to add to the source longitude in order to
                 * get the destination longitude. This amount is measured in units of the target
                 * axis.  The affine transform is then updated in order to take this rotation in
                 * account. Note that the resulting longitude may be outside the usual [-180..180°]
                 * range.
                 */
                final Unit<Angle>       unit = axis.getUnit().asType(Angle.class);
                final double sourceLongitude = getGreenwichLongitude(sourcePM, unit);
                final double targetLongitude = getGreenwichLongitude(targetPM, unit);
                final int   lastMatrixColumn = matrix.getNumCol()-1;
                double rotate = sourceLongitude - targetLongitude;
                if (AxisDirection.WEST.equals(direction)) {
                    rotate = -rotate;
                }
                rotate += matrix.getElement(i, lastMatrixColumn);
                matrix.setElement(i, lastMatrixColumn, rotate);
            }
        }
        return matrix;
    }

    /**
     * Returns the longitude value relative to the Greenwich Meridian,
     * expressed in the specified units.
     */
    private static double getGreenwichLongitude(final PrimeMeridian pm, final Unit<Angle> unit) {
        return pm.getAngularUnit().getConverterTo(unit).convert(pm.getGreenwichLongitude());
    }

    /**
     * Returns the longitude value relative to the Greenwich Meridian, expressed in decimal degrees.
     */
    private static double getGreenwichLongitude(final PrimeMeridian pm) {
        return getGreenwichLongitude(pm, NonSI.DEGREE_ANGLE);
    }




    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////                                                         ////////////
    ////////////        T R A N S F O R M A T I O N S   S T E P S        ////////////
    ////////////                                                         ////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates an operation between two temporal coordinate reference systems.
     * The default implementation checks if both CRS use the same datum, and
     * then adjusts for axis direction, units and epoch.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final TemporalCRS sourceCRS,
                                                      final TemporalCRS targetCRS)
            throws FactoryException
    {
        final TemporalDatum sourceDatum = sourceCRS.getDatum();
        final TemporalDatum targetDatum = targetCRS.getDatum();
        if (!equalsIgnoreMetadata(sourceDatum, targetDatum)) {
            throw new OperationNotFoundException(getErrorMessage(sourceDatum, targetDatum));
        }
        /*
         * Compute the epoch shift.  The epoch is the time "0" in a particular coordinate
         * reference system. For example, the epoch for java.util.Date object is january 1,
         * 1970 at 00:00 UTC.  We compute how much to add to a time in 'sourceCRS' in order
         * to get a time in 'targetCRS'. This "epoch shift" is in units of 'targetCRS'.
         */
        final TimeCS sourceCS = sourceCRS.getCoordinateSystem();
        final TimeCS targetCS = targetCRS.getCoordinateSystem();
        final Unit targetUnit = targetCS.getAxis(0).getUnit();
        double epochShift = sourceDatum.getOrigin().getTime() -
                            targetDatum.getOrigin().getTime();
        epochShift = MILLISECOND.getConverterTo(targetUnit).convert(epochShift);
        /*
         * Check axis orientation.  The method 'swapAndScaleAxis' should returns a matrix
         * of size 2x2. The element at index (0,0) may be 1 if sourceCRS and targetCRS axis
         * are in the same direction, or -1 if there are in opposite direction (e.g.
         * "PAST" vs "FUTURE"). This number may be something else than -1 or +1 if a unit
         * conversion was applied too,  for example 60 if time in 'sourceCRS' was in hours
         * while time in 'targetCRS' was in minutes.
         *
         * The "epoch shift" previously computed is a translation.
         * Consequently, it is added to element (0,1).
         */
        final Matrix matrix = swapAndScaleAxis(sourceCS, targetCS);
        final int translationColumn = matrix.getNumCol()-1;
        if (translationColumn >= 0) { // Paranoiac check: should always be 1.
            final double translation = matrix.getElement(0, translationColumn);
            matrix.setElement(0, translationColumn, translation+epochShift);
        }
        return createFromAffineTransform(AXIS_CHANGES, sourceCRS, targetCRS, matrix);
    }

    /**
     * Creates an operation between two vertical coordinate reference systems.
     * The default implementation checks if both CRS use the same datum, and
     * then adjusts for axis direction and units.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final VerticalCRS sourceCRS,
                                                      final VerticalCRS targetCRS)
            throws FactoryException
    {
        final VerticalDatum sourceDatum = sourceCRS.getDatum();
        final VerticalDatum targetDatum = targetCRS.getDatum();
        if (!equalsIgnoreMetadata(sourceDatum, targetDatum)) {
            throw new OperationNotFoundException(getErrorMessage(sourceDatum, targetDatum));
        }
        final VerticalCS sourceCS = sourceCRS.getCoordinateSystem();
        final VerticalCS targetCS = targetCRS.getCoordinateSystem();
        final Matrix     matrix   = swapAndScaleAxis(sourceCS, targetCS);
        return createFromAffineTransform(AXIS_CHANGES, sourceCRS, targetCRS, matrix);
    }

    /**
     * Creates an operation between a geographic and a vertical coordinate reference systems.
     * The default implementation accepts the conversion only if the geographic CRS is a tri
     * dimensional one and the vertical CRS is for {@linkplain VerticalDatumType#ELLIPSOIDAL
     * height above the ellipsoid}. More elaborated operation, like transformation from
     * ellipsoidal to geoidal height, should be implemented here.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     *
     * @todo Implement GEOT-352 here.
     */
    protected CoordinateOperation createOperationStep(final GeographicCRS sourceCRS,
                                                      final VerticalCRS   targetCRS)
            throws FactoryException
    {
        if (VerticalDatumType.ELLIPSOIDAL.equals(targetCRS.getDatum().getVerticalDatumType())) {
            final Matrix matrix = swapAndScaleAxis(sourceCRS.getCoordinateSystem(),
                                                   targetCRS.getCoordinateSystem());
            return createFromAffineTransform(AXIS_CHANGES, sourceCRS, targetCRS, matrix);
        }
        throw new OperationNotFoundException(getErrorMessage(sourceCRS, targetCRS));
    }

    /**
     * Creates an operation between two geographic coordinate reference systems. The default
     * implementation can adjust axis order and orientation (e.g. transforming from
     * {@code (NORTH,WEST)} to {@code (EAST,NORTH)}), performs units conversion
     * and apply datum shifts if needed.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     *
     * @todo When rotating the prime meridian, we should ensure that
     *       transformed longitudes stay in the range [-180..+180°].
     */
    protected CoordinateOperation createOperationStep(final GeographicCRS sourceCRS,
                                                      final GeographicCRS targetCRS)
            throws FactoryException
    {
        final EllipsoidalCS sourceCS    = sourceCRS.getCoordinateSystem();
        final EllipsoidalCS targetCS    = targetCRS.getCoordinateSystem();
        final GeodeticDatum sourceDatum = sourceCRS.getDatum();
        final GeodeticDatum targetDatum = targetCRS.getDatum();
        final PrimeMeridian sourcePM    = sourceDatum.getPrimeMeridian();
        final PrimeMeridian targetPM    = targetDatum.getPrimeMeridian();
        if (equalsIgnorePrimeMeridian(sourceDatum, targetDatum)) {
            /*
             * If both geographic CRS use the same datum, then there is no need for a datum shift.
             * Just swap axis order, and rotate the longitude coordinate if prime meridians are
             * different. Note: this special block is mandatory for avoiding never-ending loop,
             * since it is invoked by 'createOperationStep(GeocentricCRS...)'.
             *
             * TODO: We should ensure that longitude is in range [-180..+180°].
             */
            final Matrix matrix = swapAndScaleAxis(sourceCS, targetCS, sourcePM, targetPM);
            return createFromAffineTransform(AXIS_CHANGES, sourceCRS, targetCRS, matrix);
        }
        /*
         * The two geographic CRS use different datum. If Molodenski transformations
         * are allowed, try them first. Note that is some case if the datum shift can't
         * be performed in a single Molodenski transformation step (i.e. if we need to
         * go through at least one intermediate datum), then we will use the geocentric
         * transform below instead: it allows to concatenates many Bursa Wolf parameters
         * in a single affine transform.
         */
        if (molodenskiMethod != null) {
            ReferenceIdentifier identifier = DATUM_SHIFT;
            BursaWolfParameters bursaWolf  = null;
            if (sourceDatum instanceof DefaultGeodeticDatum) {
                bursaWolf = ((DefaultGeodeticDatum) sourceDatum).getBursaWolfParameters(targetDatum);
            }
            if (bursaWolf == null) {
                /*
                 * No direct path found. Try the more expensive matrix calculation, and
                 * see if we can retrofit the result in a BursaWolfParameters object.
                 */
                final Matrix shift = DefaultGeodeticDatum.getAffineTransform(sourceDatum, targetDatum);
                if (shift != null) try {
                    bursaWolf = new BursaWolfParameters(targetDatum);
                    bursaWolf.setAffineTransform(shift, 1E-4);
                } catch (IllegalArgumentException ignore) {
                    /*
                     * A matrix exists, but we are unable to retrofit it as a set of Bursa-Wolf
                     * parameters. Do NOT set the 'bursaWolf' variable: it must stay null, which
                     * means to perform the datum shift using geocentric coordinates.
                     */
                } else if (lenientDatumShift) {
                    /*
                     * No BursaWolf parameters available. No affine transform to be applied in
                     * geocentric coordinates are available neither (the "shift" matrix above),
                     * so performing a geocentric transformation will not help. But the user wants
                     * us to perform the datum shift anyway. We will notify the user through
                     * positional accuracy, which is set indirectly through ELLIPSOID_SHIFT.
                     */
                    bursaWolf  = new BursaWolfParameters(targetDatum);
                    identifier = ELLIPSOID_SHIFT;
                }
            }
            /*
             * Applies the Molodenski transformation now. Note: in current parameters, we can't
             * specify a different input and output dimension. However, our Molodenski transform
             * allows that. We should expand the parameters block for this case (TODO).
             */
            if (bursaWolf!=null && bursaWolf.isTranslation()) {
                final Ellipsoid sourceEllipsoid = sourceDatum.getEllipsoid();
                final Ellipsoid targetEllipsoid = targetDatum.getEllipsoid();
                if (bursaWolf.isIdentity() && equalsIgnoreMetadata(sourceEllipsoid, targetEllipsoid)) {
                    final Matrix matrix = swapAndScaleAxis(sourceCS, targetCS, sourcePM, targetPM);
                    return createFromAffineTransform(identifier, sourceCRS, targetCRS, matrix);
                }
                final int sourceDim = getDimension(sourceCRS);
                final int targetDim = getDimension(targetCRS);
                final ParameterValueGroup parameters;
                parameters = getMathTransformFactory().getDefaultParameters(molodenskiMethod);
                parameters.parameter("src_semi_major").setValue(sourceEllipsoid.getSemiMajorAxis());
                parameters.parameter("src_semi_minor").setValue(sourceEllipsoid.getSemiMinorAxis());
                parameters.parameter("tgt_semi_major").setValue(targetEllipsoid.getSemiMajorAxis());
                parameters.parameter("tgt_semi_minor").setValue(targetEllipsoid.getSemiMinorAxis());
                parameters.parameter("dx")            .setValue(bursaWolf.dx);
                parameters.parameter("dy")            .setValue(bursaWolf.dy);
                parameters.parameter("dz")            .setValue(bursaWolf.dz);
                parameters.parameter("dim")           .setValue(sourceDim);
                if (sourceDim == targetDim) {
                    final CoordinateOperation step1, step2, step3;
                    final GeographicCRS normSourceCRS = normalize(sourceCRS, true);
                    final GeographicCRS normTargetCRS = normalize(targetCRS, true);
                    step1 = createOperationStep(sourceCRS, normSourceCRS);
                    step2 = createFromParameters(identifier, normSourceCRS, normTargetCRS, parameters);
                    step3 = createOperationStep(normTargetCRS, targetCRS);
                    return concatenate(step1, step2, step3);
                } else {
                    // TODO: Need some way to pass 'targetDim' to Molodenski.
                    //       Fallback on geocentric transformations for now.
                }
            }
        }
        /*
         * If the two geographic CRS use different datum, transform from the
         * source to target datum through the geocentric coordinate system.
         * The transformation chain is:
         *
         *     source geographic CRS                                               -->
         *     geocentric CRS with a preference for datum using Greenwich meridian -->
         *     target geographic CRS
         */
        final CartesianCS STANDARD = DefaultCartesianCS.GEOCENTRIC;
        final GeocentricCRS stepCRS;
        final CRSFactory crsFactory = getFactoryContainer().getCRSFactory();
        if (getGreenwichLongitude(targetPM) == 0) {
            stepCRS = crsFactory.createGeocentricCRS(
                      getTemporaryName(targetCRS), targetDatum, STANDARD);
        } else {
            stepCRS = crsFactory.createGeocentricCRS(
                      getTemporaryName(sourceCRS), sourceDatum, STANDARD);
        }
        final CoordinateOperation step1 = createOperationStep(sourceCRS, stepCRS);
        final CoordinateOperation step2 = createOperationStep(stepCRS, targetCRS);
        return concatenate(step1, step2);
    }

    /**
     * Creates an operation between two projected coordinate reference systems.
     * The default implementation can adjust axis order and orientation. It also
     * performs units conversion if it is the only extra change needed. Otherwise,
     * it performs three steps:
     *
     * <ul>
     *   <li>Unproject from {@code sourceCRS} to its base
     *       {@linkplain GeographicCRS geographic CRS}.</li>
     *   <li>Convert the source to target base geographic CRS.</li>
     *   <li>Project from the base {@linkplain GeographicCRS geographic CRS}
     *       to the {@code targetCRS}.</li>
     * </ul>
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final ProjectedCRS sourceCRS,
                                                      final ProjectedCRS targetCRS)
            throws FactoryException
    {
        /*
         * First, check if a linear path exists from sourceCRS to targetCRS.
         * If both projected CRS use the same projection and the same horizontal datum,
         * then only axis orientation and units may have been changed. We do not need
         * to perform the tedious  ProjectedCRS --> GeographicCRS --> ProjectedCRS  chain.
         * We can apply a much shorter conversion using only an affine transform.
         *
         * This shorter path is essential for proper working of
         * createOperationStep(GeographicCRS,ProjectedCRS).
         */
        final Matrix linear = createLinearConversion(sourceCRS, targetCRS, EPS);
        if (linear != null) {
            return createFromAffineTransform(AXIS_CHANGES, sourceCRS, targetCRS, linear);
        }
        /*
         * Apply the transformation in 3 steps (the 3 arrows below):
         *
         *     source projected CRS   --(unproject)-->
         *     source geographic CRS  --------------->
         *     target geographic CRS  ---(project)--->
         *     target projected CRS
         */
        final GeographicCRS sourceGeo = sourceCRS.getBaseCRS();
        final GeographicCRS targetGeo = targetCRS.getBaseCRS();
        CoordinateOperation step1, step2, step3;
        step1 = tryDB(sourceCRS, sourceGeo); if (step1==null) step1 = createOperationStep(sourceCRS, sourceGeo);
        step2 = tryDB(sourceGeo, targetGeo); if (step2==null) step2 = createOperationStep(sourceGeo, targetGeo);
        step3 = tryDB(targetGeo, targetCRS); if (step3==null) step3 = createOperationStep(targetGeo, targetCRS);
        return concatenate(step1, step2, step3);
    }

    /**
     * Creates an operation from a geographic to a projected coordinate reference system.
     * The default implementation constructs the following operation chain:
     *
     * <blockquote><pre>
     * sourceCRS  &rarr;  {@linkplain ProjectedCRS#getBaseCRS baseCRS}  &rarr;  targetCRS
     * </pre></blockquote>
     *
     * where the conversion from {@code baseCRS} to {@code targetCRS} is obtained
     * from <code>targetCRS.{@linkplain ProjectedCRS#getConversionFromBase
     * getConversionFromBase()}</code>.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final GeographicCRS sourceCRS,
                                                      final ProjectedCRS  targetCRS)
            throws FactoryException
    {
        GeographicCRS       base  = targetCRS.getBaseCRS();
        CoordinateOperation step2 = targetCRS.getConversionFromBase();
        CoordinateOperation step1 = tryDB(sourceCRS, base);
        if (step1 == null) {
            step1 = createOperationStep(sourceCRS, base);
        }
        return concatenate(step1, step2);
    }

    /**
     * Creates an operation from a projected to a geographic coordinate reference system.
     * The default implementation constructs the following operation chain:
     *
     * <blockquote><pre>
     * sourceCRS  &rarr;  {@linkplain ProjectedCRS#getBaseCRS baseCRS}  &rarr;  targetCRS
     * </pre></blockquote>
     *
     * where the conversion from {@code sourceCRS} to {@code baseCRS} is obtained
     * from the inverse of
     * <code>sourceCRS.{@linkplain ProjectedCRS#getConversionFromBase
     * getConversionFromBase()}</code>.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     *
     * @todo Provides a non-null method.
     */
    protected CoordinateOperation createOperationStep(final ProjectedCRS  sourceCRS,
                                                      final GeographicCRS targetCRS)
            throws FactoryException
    {
        final GeographicCRS base  = sourceCRS.getBaseCRS();
        CoordinateOperation step1 = sourceCRS.getConversionFromBase();
        CoordinateOperation step2 = tryDB(base, targetCRS);
        if (step2 == null) {
            step2 = createOperationStep(base, targetCRS);
        }
        MathTransform transform = step1.getMathTransform();
        try {
            transform = transform.inverse();
        } catch (NoninvertibleTransformException exception) {
            throw new OperationNotFoundException(getErrorMessage(sourceCRS, base), exception);
        }
        step1 = createFromMathTransform(INVERSE_OPERATION, sourceCRS, base, transform);
        return concatenate(step1, step2);
    }

    /**
     * Creates an operation between two geocentric coordinate reference systems.
     * The default implementation can adjust for axis order and orientation,
     * performs units conversion and apply Bursa Wolf transformation if needed.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     *
     * @todo Rotation of prime meridian not yet implemented.
     * @todo Transformation version set to "(unknow)". We should search this information somewhere.
     */
    protected CoordinateOperation createOperationStep(final GeocentricCRS sourceCRS,
                                                      final GeocentricCRS targetCRS)
            throws FactoryException
    {
        final GeodeticDatum sourceDatum = sourceCRS.getDatum();
        final GeodeticDatum targetDatum = targetCRS.getDatum();
        final CoordinateSystem sourceCS = sourceCRS.getCoordinateSystem();
        final CoordinateSystem targetCS = targetCRS.getCoordinateSystem();
        final double sourcePM, targetPM;
        sourcePM = getGreenwichLongitude(sourceDatum.getPrimeMeridian());
        targetPM = getGreenwichLongitude(targetDatum.getPrimeMeridian());
        if (equalsIgnorePrimeMeridian(sourceDatum, targetDatum)) {
            if (sourcePM == targetPM) {
                /*
                 * If both CRS use the same datum and the same prime meridian,
                 * then the transformation is probably just axis swap or unit
                 * conversions.
                 */
                final Matrix matrix = swapAndScaleAxis(sourceCS, targetCS);
                return createFromAffineTransform(AXIS_CHANGES, sourceCRS, targetCRS, matrix);
            }
            // Prime meridians are differents. Performs the full transformation.
        }
        if (sourcePM != targetPM) {
            throw new OperationNotFoundException("Rotation of prime meridian not yet implemented");
        }
        /*
         * Transform between differents ellipsoids using Bursa Wolf parameters.
         * The Bursa Wolf parameters are used with "standard" geocentric CS, i.e.
         * with x axis towards the prime meridian, y axis towards East and z axis
         * toward North. The following steps are applied:
         *
         *     source CRS                      -->
         *     standard CRS with source datum  -->
         *     standard CRS with target datum  -->
         *     target CRS
         */
        final CartesianCS STANDARD = DefaultCartesianCS.GEOCENTRIC;
        final XMatrix matrix;
        ReferenceIdentifier identifier = DATUM_SHIFT;
        try {
            Matrix datumShift = DefaultGeodeticDatum.getAffineTransform(
                                    TemporaryDatum.unwrap(sourceDatum),
                                    TemporaryDatum.unwrap(targetDatum));
            if (datumShift == null) {
                if (lenientDatumShift) {
                    datumShift = new Matrix4(); // Identity transform.
                    identifier = ELLIPSOID_SHIFT;
                } else {
                    throw new OperationNotFoundException(Errors.format(
                                ErrorKeys.BURSA_WOLF_PARAMETERS_REQUIRED));
                }
            }
            final Matrix normalizeSource = swapAndScaleAxis(sourceCS, STANDARD);
            final Matrix normalizeTarget = swapAndScaleAxis(STANDARD, targetCS);
            /*
             * Since all steps are matrix, we can multiply them into a single matrix operation.
             * Note: XMatrix.multiply(XMatrix) is equivalents to AffineTransform.concatenate(...):
             *       First transform by the supplied transform and then transform the result
             *       by the original transform.
             *
             * We compute: matrix = normalizeTarget * datumShift * normalizeSource
             */
            matrix = new Matrix4(normalizeTarget);
            matrix.multiply(datumShift);
            matrix.multiply(normalizeSource);
        } catch (SingularMatrixException cause) {
            throw new OperationNotFoundException(getErrorMessage(sourceDatum, targetDatum), cause);
        }
        return createFromAffineTransform(identifier, sourceCRS, targetCRS, matrix);
    }

    /**
     * Creates an operation from a geographic to a geocentric coordinate reference systems.
     * If the source CRS doesn't have a vertical axis, height above the ellipsoid will be
     * assumed equals to zero everywhere. The default implementation uses the
     * {@code "Ellipsoid_To_Geocentric"} math transform.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final GeographicCRS sourceCRS,
                                                      final GeocentricCRS targetCRS)
            throws FactoryException
    {
        /*
         * This transformation is a 3 steps process:
         *
         *    source     geographic CRS  -->
         *    normalized geographic CRS  -->
         *    normalized geocentric CRS  -->
         *    target     geocentric CRS
         *
         * "Normalized" means that axis point toward standards direction (East, North, etc.),
         * units are metres or decimal degrees, prime meridian is Greenwich and height is measured
         * above the ellipsoid. However, the horizontal datum is preserved.
         */
        final GeographicCRS normSourceCRS = normalize(sourceCRS, true);
        final GeodeticDatum datum         = normSourceCRS.getDatum();
        final GeocentricCRS normTargetCRS = normalize(targetCRS, datum);
        final Ellipsoid         ellipsoid = datum.getEllipsoid();
        final Unit                   unit = ellipsoid.getAxisUnit();
        final ParameterValueGroup   param;
        param = getMathTransformFactory().getDefaultParameters("Ellipsoid_To_Geocentric");
        param.parameter("semi_major").setValue(ellipsoid.getSemiMajorAxis(), unit);
        param.parameter("semi_minor").setValue(ellipsoid.getSemiMinorAxis(), unit);
        param.parameter("dim")       .setValue(getDimension(normSourceCRS));

        final CoordinateOperation step1, step2, step3;
        step1 = createOperationStep (sourceCRS, normSourceCRS);
        step2 = createFromParameters(GEOCENTRIC_CONVERSION, normSourceCRS, normTargetCRS, param);
        step3 = createOperationStep (normTargetCRS, targetCRS);
        return concatenate(step1, step2, step3);
    }

    /**
     * Creates an operation from a geocentric to a geographic coordinate reference systems.
     * The default implementation use the <code>"Geocentric_To_Ellipsoid"</code> math transform.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final GeocentricCRS sourceCRS,
                                                      final GeographicCRS targetCRS)
            throws FactoryException
    {
        final GeographicCRS normTargetCRS = normalize(targetCRS, true);
        final GeodeticDatum datum         = normTargetCRS.getDatum();
        final GeocentricCRS normSourceCRS = normalize(sourceCRS, datum);
        final Ellipsoid         ellipsoid = datum.getEllipsoid();
        final Unit                   unit = ellipsoid.getAxisUnit();
        final ParameterValueGroup   param;
        param = getMathTransformFactory().getDefaultParameters("Geocentric_To_Ellipsoid");
        param.parameter("semi_major").setValue(ellipsoid.getSemiMajorAxis(), unit);
        param.parameter("semi_minor").setValue(ellipsoid.getSemiMinorAxis(), unit);
        param.parameter("dim")       .setValue(getDimension(normTargetCRS));

        final CoordinateOperation step1, step2, step3;
        step1 = createOperationStep (sourceCRS, normSourceCRS);
        step2 = createFromParameters(GEOCENTRIC_CONVERSION, normSourceCRS, normTargetCRS, param);
        step3 = createOperationStep (normTargetCRS, targetCRS);
        return concatenate(step1, step2, step3);
    }

    /**
     * Creates an operation from a compound to a single coordinate reference systems.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     *
     * @todo (GEOT-401) This method work for some simple cases (e.g. no datum change), and give up
     *       otherwise. Before to give up at the end of this method, we should try the following:
     *       <ul>
     *         <li>Maybe {@code sourceCRS} uses a non-ellipsoidal height. We should replace
     *             the non-ellipsoidal height by an ellipsoidal one, create a transformation step
     *             for that (to be concatenated), and then try again this operation step.</li>
     *
     *         <li>Maybe {@code sourceCRS} contains some extra axis, like a temporal CRS.
     *             We should revisit this code in other to lets supplemental ordinates to be
     *             pass through or removed.</li>
     *       </ul>
     */
    protected CoordinateOperation createOperationStep(final CompoundCRS sourceCRS,
                                                      final SingleCRS   targetCRS)
            throws FactoryException
    {
        final List<SingleCRS> sources = DefaultCompoundCRS.getSingleCRS(sourceCRS);
        if (sources.size() == 1) {
            return createOperation(sources.get(0), targetCRS);
        }
        if (!needsGeodetic3D(sources, targetCRS)) {
            // No need for a datum change (see 'needGeodetic3D' javadoc).
            final List<SingleCRS> targets = Collections.singletonList(targetCRS);
            return createOperationStep(sourceCRS, sources, targetCRS, targets);
        }
        /*
         * There is a change of datum.  It may be a vertical datum change (for example from
         * ellipsoidal to geoidal height), in which case geographic coordinates are usually
         * needed. It may also be a geodetic datum change, in which case the height is part
         * of computation. Try to convert the source CRS into a 3D-geodetic CRS.
         */
        final CoordinateReferenceSystem source3D = getFactoryContainer().toGeodetic3D(sourceCRS);
        if (source3D != sourceCRS) {
            return createOperation(source3D, targetCRS);
        }
        /*
         * TODO: Search for non-ellipsoidal height, and lets supplemental axis (e.g. time)
         *       pass through. See javadoc comments above.
         */
        throw new OperationNotFoundException(getErrorMessage(sourceCRS, targetCRS));
    }

    /**
     * Creates an operation from a single to a compound coordinate reference system.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final SingleCRS   sourceCRS,
                                                      final CompoundCRS targetCRS)
            throws FactoryException
    {
        final List<SingleCRS> targets = DefaultCompoundCRS.getSingleCRS(targetCRS);
        if (targets.size() == 1) {
            return createOperation(sourceCRS, targets.get(0));
        }
        /*
         * This method has almost no chance to succeed (we can't invent ordinate values!) unless
         * 'sourceCRS' is a 3D-geodetic CRS and 'targetCRS' is a 2D + 1D one. Test for this case.
         * Otherwise, the 'createOperationStep' invocation will throws the appropriate exception.
         */
        final CoordinateReferenceSystem target3D = getFactoryContainer().toGeodetic3D(targetCRS);
        if (target3D != targetCRS) {
            return createOperation(sourceCRS, target3D);
        }
        final List<SingleCRS> sources = Collections.singletonList(sourceCRS);
        return createOperationStep(sourceCRS, sources, targetCRS, targets);
    }

    /**
     * Creates an operation between two compound coordinate reference systems.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    protected CoordinateOperation createOperationStep(final CompoundCRS sourceCRS,
                                                      final CompoundCRS targetCRS)
            throws FactoryException
    {
        final List<SingleCRS> sources = DefaultCompoundCRS.getSingleCRS(sourceCRS);
        final List<SingleCRS> targets = DefaultCompoundCRS.getSingleCRS(targetCRS);
        if (targets.size() == 1) {
            return createOperation(sourceCRS, targets.get(0));
        }
        if (sources.size() == 1) { // After 'targets' because more likely to fails to transform.
            return createOperation(sources.get(0), targetCRS);
        }
        /*
         * If the source CRS contains both a geodetic and a vertical CRS, then we can process
         * only if there is no datum change. If at least one of those CRS appears in the target
         * CRS with a different datum, then the datum shift must be applied on the horizontal and
         * vertical components together.
         */
        for (final SingleCRS target : targets) {
            if (needsGeodetic3D(sources, target)) {
                final ReferencingFactoryContainer factories = getFactoryContainer();
                final CoordinateReferenceSystem source3D = factories.toGeodetic3D(sourceCRS);
                final CoordinateReferenceSystem target3D = factories.toGeodetic3D(targetCRS);
                if (source3D!=sourceCRS || target3D!=targetCRS) {
                    return createOperation(source3D, target3D);
                }
                /*
                 * TODO: Search for non-ellipsoidal height, and lets supplemental axis pass through.
                 *       See javadoc comments for createOperation(CompoundCRS, SingleCRS).
                 */
                throw new OperationNotFoundException(getErrorMessage(sourceCRS, targetCRS));
            }
        }
        // No need for a datum change (see 'needGeodetic3D' javadoc).
        return createOperationStep(sourceCRS, sources, targetCRS, targets);
    }

    /**
     * Implementation of transformation step on compound CRS.
     * <p>
     * <strong>NOTE:</strong>
     * If there is a horizontal (geographic or projected) CRS together with a vertical CRS,
     * then we can't performs the transformation since the vertical value has an impact on
     * the horizontal value, and this impact is not taken in account if the horizontal and
     * vertical components are not together in a 3D geographic CRS.  This case occurs when
     * the vertical CRS is not a height above the ellipsoid. It must be checked by the
     * caller before this method is invoked.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  sources   The source CRS components.
     * @param  targetCRS Output coordinate reference system.
     * @param  targets   The target CRS components.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If the operation can't be constructed.
     */
    private CoordinateOperation createOperationStep(final CoordinateReferenceSystem sourceCRS,
                                                    final List<SingleCRS>           sources,
                                                    final CoordinateReferenceSystem targetCRS,
                                                    final List<SingleCRS>           targets)
            throws FactoryException
    {
        /*
         * Try to find operations from source CRSs to target CRSs. All pairwise combinaisons are
         * tried, but the preference is given to CRS in the same order (source[0] with target[0],
         * source[1] with target[1], etc.). Operations found are stored in 'steps', but are not
         * yet given to pass through transforms. We need to know first if some ordinate values
         * need reordering (for matching the order of target CRS) if any ordinates reordering and
         * source ordinates drops are required.
         */
        final CoordinateReferenceSystem[] ordered = new CoordinateReferenceSystem[targets.size()];
        final CoordinateOperation[]       steps   = new CoordinateOperation      [targets.size()];
        final boolean[]                   done    = new boolean                  [sources.size()];
        final int[]                       indices = new int[getDimension(sourceCRS)];
        int count=0, dimensions=0;
search: for (int j=0; j<targets.size(); j++) {
            int lower, upper=0;
            final CoordinateReferenceSystem target = targets.get(j);
            OperationNotFoundException cause = null;
            for (int i=0; i<sources.size(); i++) {
                final CoordinateReferenceSystem source = sources.get(i);
                lower  = upper;
                upper += getDimension(source);
                if (done[i]) continue;
                try {
                    steps[count] = createOperation(source, target);
                } catch (OperationNotFoundException exception) {
                    // No operation path for this pair.
                    // Search for an other pair.
                    if (cause==null || i==j) {
                        cause = exception;
                    }
                    continue;
                }
                ordered[count++] = source;
                while (lower < upper) {
                    indices[dimensions++] = lower++;
                }
                done[i] = true;
                continue search;
            }
            /*
             * No source CRS was found for current target CRS.
             * Consequently, we can't get a transformation path.
             */
            throw new OperationNotFoundException(getErrorMessage(sourceCRS, targetCRS), cause);
        }
        /*
         * A transformation has been found for every source and target CRS pairs.
         * Some reordering of ordinate values may be needed. Prepare it now as an
         * affine transform. This transform also drop source dimensions not used
         * for any target coordinates.
         */
        assert count == targets.size() : count;
        while (count!=0 && steps[--count].getMathTransform().isIdentity());
        final ReferencingFactoryContainer factories = getFactoryContainer();
        CoordinateOperation operation = null;
        CoordinateReferenceSystem sourceStepCRS = sourceCRS;
        final XMatrix select = MatrixFactory.create(dimensions+1, indices.length+1);
        select.setZero();
        select.setElement(dimensions, indices.length, 1);
        for (int j=0; j<dimensions; j++) {
            select.setElement(j, indices[j], 1);
        }
        if (!select.isIdentity()) {
            if (ordered.length == 1) {
                sourceStepCRS = ordered[0];
            } else {
                sourceStepCRS = factories.getCRSFactory().createCompoundCRS(
                                    getTemporaryName(sourceCRS), ordered);
            }
            operation = createFromAffineTransform(AXIS_CHANGES, sourceCRS, sourceStepCRS, select);
        }
        /*
         * Now creates the pass through transforms for each transformation steps found above.
         * We get (or construct temporary) source and target CRS for this step. They will be
         * given to the constructor of the pass through operation, after the construction of
         * pass through transform.
         */
        int lower, upper=0;
        for (int i=0; i<targets.size(); i++) {
            CoordinateOperation step = steps[i];
            final Map<String,?> properties = AbstractIdentifiedObject.getProperties(step);
            final CoordinateReferenceSystem source = ordered[i];
            final CoordinateReferenceSystem target = targets.get(i);
            final CoordinateReferenceSystem targetStepCRS;
            ordered[i] = target; // Used for the construction of targetStepCRS.
            MathTransform mt = step.getMathTransform();
            if (i >= count) {
                targetStepCRS = targetCRS;
            } else if (mt.isIdentity()) {
                targetStepCRS = sourceStepCRS;
            } else if (ordered.length == 1) {
                targetStepCRS = ordered[0];
            } else {
                targetStepCRS = factories.getCRSFactory().createCompoundCRS(
                                    getTemporaryName(target), ordered);
            }
            lower  = upper;
            upper += getDimension(source);
            if (lower!=0 || upper!=dimensions) {
                /*
                 * Constructs the pass through transform only if there is at least one ordinate to
                 * pass. Actually, the code below would give an acceptable result even if this check
                 * was not performed, except for creation of intermediate objects.
                 */
                if (!(step instanceof Operation)) {
                    final MathTransform stepMT = step.getMathTransform();
                    step = DefaultOperation.create(AbstractIdentifiedObject.getProperties(step),
                            step.getSourceCRS(), step.getTargetCRS(), stepMT,
                            new DefaultOperationMethod(stepMT), step.getClass());
                }
                mt = getMathTransformFactory().createPassThroughTransform(lower, mt, dimensions-upper);
                step = new DefaultPassThroughOperation(properties, sourceStepCRS, targetStepCRS,
                                                       (Operation) step, mt);
            }
            operation     = (operation==null) ? step : concatenate(operation, step);
            sourceStepCRS = targetStepCRS;
        }
        assert upper == dimensions : upper;
        return operation;
    }

    /**
     * Returns {@code true} if a transformation path from {@code sourceCRS} to
     * {@code targetCRS} is likely to requires a tri-dimensional geodetic CRS as an
     * intermediate step. More specifically, this method returns {@code false} if at
     * least one of the following conditions is meet:
     *
     * <ul>
     *   <li>The target datum is not a vertical or geodetic one (the two datum that must work
     *       together). Consequently, a potential datum change is not the caller's business.
     *       It will be handled by the generic method above.</li>
     *
     *   <li>The target datum is vertical or geodetic, but there is no datum change. It is
     *       better to not try to create 3D-geodetic CRS, since they are more difficult to
     *       separate in the generic method above. An exception to this rule occurs when
     *       the target datum is used in a three-dimensional CRS.</li>
     *
     *   <li>A datum change is required, but source CRS doesn't have both a geodetic
     *       and a vertical CRS, so we can't apply a 3D datum shift anyway.</li>
     * </ul>
     */
    private static boolean needsGeodetic3D(final List<SingleCRS> sourceCRS, final SingleCRS targetCRS) {
        final boolean targetGeodetic;
        final Datum targetDatum = targetCRS.getDatum();
        if (targetDatum instanceof GeodeticDatum) {
            targetGeodetic = true;
        } else if (targetDatum instanceof VerticalDatum) {
            targetGeodetic = false;
        } else {
            return false;
        }
        boolean horizontal = false;
        boolean vertical   = false;
        boolean shift      = false;
        for (final SingleCRS crs : sourceCRS) {
            final Datum sourceDatum = crs.getDatum();
            final boolean sourceGeodetic;
            if (sourceDatum instanceof GeodeticDatum) {
                horizontal     = true;
                sourceGeodetic = true;
            } else if (sourceDatum instanceof VerticalDatum) {
                vertical       = true;
                sourceGeodetic = false;
            } else {
                continue;
            }
            if (!shift && sourceGeodetic == targetGeodetic) {
                shift = !equalsIgnoreMetadata(sourceDatum, targetDatum);
                assert Classes.sameInterfaces(sourceDatum.getClass(),
                                              targetDatum.getClass(), Datum.class);
            }
        }
        return horizontal && vertical &&
               (shift || targetCRS.getCoordinateSystem().getDimension() >= 3);
    }





    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////
    ////////////                                                         ////////////
    ////////////                M I S C E L L A N E O U S                ////////////
    ////////////                                                         ////////////
    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * Compares the specified datum for equality, except the prime meridian.
     *
     * @param  object1 The first object to compare (may be null).
     * @param  object2 The second object to compare (may be null).
     * @return {@code true} if both objects are equals.
     */
    private static boolean equalsIgnorePrimeMeridian(GeodeticDatum object1,
                                                     GeodeticDatum object2)
    {
        object1 = TemporaryDatum.unwrap(object1);
        object2 = TemporaryDatum.unwrap(object2);
        if (equalsIgnoreMetadata(object1.getEllipsoid(), object2.getEllipsoid())) {
            return nameMatches(object1, object2.getName().getCode()) ||
                   nameMatches(object2, object1.getName().getCode());
        }
        return false;
    }

    /**
     * Returns {@code true} if {@code container} contains all CRS listed in {@code candidates},
     * ignoring metadata.
     */
    private static boolean containsIgnoreMetadata(final List<SingleCRS> container,
                                                  final List<SingleCRS> candidates)
    {
search: for (final SingleCRS crs : candidates) {
            for (final SingleCRS c : container) {
                if (equalsIgnoreMetadata(crs, c)) {
                    continue search;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Tries to get a coordinate operation from a database (typically EPSG). The exact behavior
     * depends on the {@link AuthorityBackedFactory} implementation (the most typical subclass),
     * but usually the database query is degelated to some instance of
     * {@link org.opengis.referencing.operation.CoordinateOperationAuthorityFactory}.
     * If no coordinate operation was found in the database, then this method returns {@code null}.
     */
    private final CoordinateOperation tryDB(final SingleCRS sourceCRS, final SingleCRS targetCRS) {
        return (sourceCRS == targetCRS) ? null : createFromDatabase(sourceCRS, targetCRS);
    }

    /**
     * If the coordinate operation is explicitly defined in some database (typically EPSG),
     * returns it. Otherwise (if there is no database, or if the database doesn't contains
     * an explicit operation from {@code sourceCRS} to {@code targetCRS}, or if this method
     * failed to create an operation from the database), returns {@code null}.
     * <p>
     * The default implementation always returns {@code null}, since there is no database
     * connected to a {@code DefaultCoordinateOperationFactory} instance. In other words,
     * the default implementation is "standalone": it tries to figure out transformation
     * paths by itself. Subclasses should override this method if they can fetch a more
     * accurate operation from some database. The mean subclass doing so is
     * {@link AuthorityBackedFactory}.
     * <p>
     * This method is invoked by <code>{@linkplain #createOperation createOperation}(sourceCRS,
     * targetCRS)</code> before to try to figure out a transformation path by itself. It is also
     * invoked by various {@code createOperationStep(...)} methods when an intermediate CRS was
     * obtained by {@link GeneralDerivedCRS#getBaseCRS()} (this case occurs especially during
     * {@linkplain GeographicCRS geographic} from/to {@linkplain ProjectedCRS projected} CRS
     * operations). This method is <strong>not</strong> invoked for synthetic CRS generated by
     * {@code createOperationStep(...)}, since those temporary CRS are not expected to exist
     * in a database.
     *
     * @param  sourceCRS Input coordinate reference system.
     * @param  targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS} if and only if
     *         one is explicitly defined in some underlying database, or {@code null} otherwise.
     *
     * @since 2.3
     */
    protected CoordinateOperation createFromDatabase(final CoordinateReferenceSystem sourceCRS,
                                                     final CoordinateReferenceSystem targetCRS)
    {
        return null;
    }
}
