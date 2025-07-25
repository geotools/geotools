/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.awt.RenderingHints;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.crs.CRSFactory;
import org.geotools.api.referencing.crs.CompoundCRS;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.crs.SingleCRS;
import org.geotools.api.referencing.crs.VerticalCRS;
import org.geotools.api.referencing.cs.CSFactory;
import org.geotools.api.referencing.cs.CartesianCS;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.cs.CoordinateSystemAxis;
import org.geotools.api.referencing.cs.EllipsoidalCS;
import org.geotools.api.referencing.datum.Datum;
import org.geotools.api.referencing.datum.DatumFactory;
import org.geotools.api.referencing.datum.VerticalDatumType;
import org.geotools.api.referencing.operation.Conversion;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.cs.AbstractCS;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.util.XArray;
import org.geotools.util.factory.Factory;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/**
 * A set of utilities methods working on factories. Many of those methods requires more than one factory. Consequently,
 * they can't be a method in a single factory. Furthermore, since they are helper methods and somewhat
 * implementation-dependent, they are not part of GeoAPI.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ReferencingFactoryContainer extends ReferencingFactory {
    /** A factory registry used as a cache for factory groups created up to date. */
    private static FactoryRegistry cache;

    /** The {@linkplain Datum datum} factory. If null, then a default factory will be created only when first needed. */
    private DatumFactory datumFactory;

    /**
     * The {@linkplain CoordinateSystem coordinate system} factory. If null, then a default factory will be created only
     * when first needed.
     */
    private CSFactory csFactory;

    /**
     * The {@linkplain CoordinateReferenceSystem coordinate reference system} factory. If null, then a default factory
     * will be created only when first needed.
     */
    private CRSFactory crsFactory;

    /**
     * The {@linkplain MathTransform math transform} factory. If null, then a default factory will be created only when
     * first needed.
     */
    private MathTransformFactory mtFactory;

    // WARNING: Do NOT put a CoordinateOperationFactory field in this class. We tried that in
    // Geotools 2.2, and removed it in Geotools 2.3 because it leads to very tricky recursivity
    // problems when we try to initialize it with FactoryFinder.getCoordinateOperationFactory.
    // The Datum, CS, CRS and MathTransform factories above are standalone, while the Geotools
    // implementation of CoordinateOperationFactory has complex dependencies with all of those,
    // and even with authority factories.

    /**
     * Creates an instance from the specified hints. This constructor recognizes the {@link Hints#CRS_FACTORY CRS},
     * {@link Hints#CS_FACTORY CS}, {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY
     * MATH_TRANSFORM} {@code FACTORY} hints.
     *
     * <p>This constructor is public mainly for {@link org.geotools.util.factory.FactoryCreator} usage. Consider
     * invoking <code>{@linkplain #createInstance createInstance}(userHints)</code> instead.
     *
     * @param userHints The hints, or {@code null} if none.
     */
    public ReferencingFactoryContainer(final Hints userHints) {
        final Hints reduced = new Hints(userHints);
        /*
         * If hints are provided, we will fetch factory immediately (instead of storing the hints
         * in an inner field) because most factories will retain few hints, while the Hints map
         * may contains big objects. If no hints were provided, we will construct factories only
         * when first needed.
         */
        datumFactory = (DatumFactory) extract(reduced, Hints.DATUM_FACTORY);
        csFactory = (CSFactory) extract(reduced, Hints.CS_FACTORY);
        crsFactory = (CRSFactory) extract(reduced, Hints.CRS_FACTORY);
        mtFactory = (MathTransformFactory) extract(reduced, Hints.MATH_TRANSFORM_FACTORY);
        /*
         * Checks if we still have some hints that need to be taken in account. Since we can't guess
         * which hints are relevant and which ones are not, we have to create all factories now.
         */
        if (!reduced.isEmpty()) {
            setHintsInto(reduced);
            addImplementationHints(reduced);
            initialize();
            hints.clear();
        }
    }

    /**
     * Returns the factory for the specified hint, or {@code null} if the hint is not a factory instance. It could be
     * for example a {@link Class}.
     */
    private static Factory extract(final Map<?, ?> reduced, final Hints.Key key) {
        if (reduced != null) {
            final Object candidate = reduced.get(key);
            if (candidate instanceof Factory) {
                reduced.remove(key);
                return (Factory) candidate;
            }
        }
        return null;
    }

    /**
     * Creates an instance from the specified hints. This method recognizes the {@link Hints#CRS_FACTORY CRS},
     * {@link Hints#CS_FACTORY CS}, {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY
     * MATH_TRANSFORM} {@code FACTORY} hints.
     *
     * @param hints The hints, or {@code null} if none.
     * @return A factory group created from the specified set of hints.
     */
    public static ReferencingFactoryContainer instance(final Hints hints) {
        final Hints completed = GeoTools.getDefaultHints();
        if (hints != null) {
            completed.add(hints);
        }
        /*
         * Use the same synchronization lock than ReferencingFactoryFinder (instead of this class)
         * in order to reduce the risk of dead lock. This is because ReferencingFactoryContainer
         * creation may queries ReferencingFactoryFinder, and some implementations managed by
         * ReferencingFactoryFinder may ask for a ReferencingFactoryContainer in turn.
         */
        synchronized (ReferencingFactoryFinder.class) {
            if (cache == null) {
                cache = new FactoryCreator(Arrays.asList(new Class<?>[] {ReferencingFactoryContainer.class}));
                cache.registerFactory(new ReferencingFactoryContainer(null), ReferencingFactoryContainer.class);
            }
            return cache.getFactory(ReferencingFactoryContainer.class, null, completed, null);
        }
    }

    /**
     * Forces the initialisation of all factories. Implementation note: we try to create the factories in typical
     * dependency order (CRS all because it has the greatest chances to depends on other factories).
     */
    private void initialize() {
        mtFactory = getMathTransformFactory();
        datumFactory = getDatumFactory();
        csFactory = getCSFactory();
        crsFactory = getCRSFactory();
    }

    /** Put all factories available in this group into the specified map of hints. */
    private void setHintsInto(final Map<? super RenderingHints.Key, Object> hints) {
        if (crsFactory != null) hints.put(Hints.CRS_FACTORY, crsFactory);
        if (csFactory != null) hints.put(Hints.CS_FACTORY, csFactory);
        if (datumFactory != null) hints.put(Hints.DATUM_FACTORY, datumFactory);
        if (mtFactory != null) hints.put(Hints.MATH_TRANSFORM_FACTORY, mtFactory);
    }

    /**
     * Returns all factories in this group. The returned map contains values for the {@link Hints#CRS_FACTORY CRS},
     * {@link Hints#CS_FACTORY CS}, {@link Hints#DATUM_FACTORY DATUM} and {@link Hints#MATH_TRANSFORM_FACTORY
     * MATH_TRANSFORM} {@code FACTORY} hints.
     */
    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        synchronized (hints) {
            if (hints.isEmpty()) {
                initialize();
                setHintsInto(hints);
            }
        }
        return super.getImplementationHints();
    }

    /**
     * Returns the hints to be used for lazy creation of <em>default</em> factories in various {@code getFoo} methods.
     * This is different from {@link #getImplementationHints} because the later may returns non-default factories.
     */
    private Hints hints() {
        final Hints completed = new Hints(hints);
        setHintsInto(completed);
        return completed;
    }

    /**
     * Returns the {@linkplain Datum datum} factory.
     *
     * @return The Datum factory.
     */
    public DatumFactory getDatumFactory() {
        if (datumFactory == null) {
            synchronized (hints) {
                datumFactory = ReferencingFactoryFinder.getDatumFactory(hints());
            }
        }
        return datumFactory;
    }

    /**
     * Returns the {@linkplain CoordinateSystem coordinate system} factory.
     *
     * @return The Coordinate System factory.
     */
    public CSFactory getCSFactory() {
        if (csFactory == null) {
            synchronized (hints) {
                csFactory = ReferencingFactoryFinder.getCSFactory(hints());
            }
        }
        return csFactory;
    }

    /**
     * Returns the {@linkplain CoordinateReferenceSystem coordinate reference system} factory.
     *
     * @return The Coordinate Reference System factory.
     */
    public CRSFactory getCRSFactory() {
        if (crsFactory == null) {
            synchronized (hints) {
                crsFactory = ReferencingFactoryFinder.getCRSFactory(hints());
            }
        }
        return crsFactory;
    }

    /**
     * Returns the {@linkplain MathTransform math transform} factory.
     *
     * @return The Math Transform factory.
     */
    public MathTransformFactory getMathTransformFactory() {
        if (mtFactory == null) {
            synchronized (hints) {
                mtFactory = ReferencingFactoryFinder.getMathTransformFactory(hints());
            }
        }
        return mtFactory;
    }

    /**
     * Converts a 2D&nbsp;+&nbsp;1D compound CRS into a 3D CRS, if possible. More specifically, if the specified
     * {@linkplain CompoundCRS compound CRS} is made of a {@linkplain GeographicCRS geographic} (or
     * {@linkplain ProjectedCRS projected}) and a {@linkplain VerticalCRS vertical} CRS, and if the vertical CRS datum
     * type is {@linkplain VerticalDatumType#ELLIPSOIDAL height above the ellipsoid}, then this method converts the
     * compound CRS in a single 3D CRS. Otherwise, the {@code crs} argument is returned unchanged.
     *
     * @param crs The compound CRS to converts in a 3D geographic or projected CRS.
     * @return The 3D geographic or projected CRS, or {@code crs} if the change can't be applied.
     * @throws FactoryException if the object creation failed.
     */
    public CoordinateReferenceSystem toGeodetic3D(final CompoundCRS crs) throws FactoryException {
        List<SingleCRS> components = DefaultCompoundCRS.getSingleCRS(crs);
        final int count = components.size();
        SingleCRS horizontal = null;
        VerticalCRS vertical = null;
        int hi = 0, vi = 0;
        for (int i = 0; i < count; i++) {
            final SingleCRS candidate = components.get(i);
            if (candidate instanceof VerticalCRS) {
                if (vertical == null) {
                    vertical = (VerticalCRS) candidate;
                    if (VerticalDatumType.ELLIPSOIDAL.equals(vertical.getDatum().getVerticalDatumType())) {
                        vi = i;
                        continue;
                    }
                }
                return crs;
            }
            if (candidate instanceof GeographicCRS || candidate instanceof ProjectedCRS) {
                if (horizontal == null) {
                    horizontal = candidate;
                    if (horizontal.getCoordinateSystem().getDimension() == 2) {
                        hi = i;
                        continue;
                    }
                }
                return crs;
            }
        }
        if (horizontal != null && vertical != null && Math.abs(vi - hi) == 1) {
            /*
             * Exactly one horizontal and one vertical CRS has been found, and those two CRS are
             * consecutives. Constructs the new 3D CS. If the two above-cited components are the
             * only ones, the result is returned directly. Otherwise, a new compound CRS is created.
             */
            final boolean xyFirst = hi < vi;
            final SingleCRS single = toGeodetic3D(count == 2 ? crs : null, horizontal, vertical, xyFirst);
            if (count == 2) {
                return single;
            }
            final int i = xyFirst ? hi : vi;
            components = new ArrayList<>(components);
            components.remove(i);
            components.set(i, single);
            final SingleCRS[] c = components.toArray(new SingleCRS[components.size()]);
            return crsFactory.createCompoundCRS(AbstractIdentifiedObject.getProperties(crs), c);
        }
        return crs;
    }

    /**
     * Implementation of {@link #toGeodetic3D(CompoundCRS)} invoked after the horizontal and vertical parts have been
     * identified. This method may invokes itself recursively if the horizontal CRS is a derived one.
     *
     * @param crs The compound CRS to converts in a 3D geographic CRS, or {@code null}. Used only in order to infer the
     *     name properties of objects to create.
     * @param horizontal The horizontal component of {@code crs}.
     * @param vertical The vertical component of {@code crs}.
     * @param xyFirst {@code true} if the horizontal component appears before the vertical component, or {@code false}
     *     for the converse.
     * @return The 3D geographic or projected CRS.
     * @throws FactoryException if the object creation failed.
     */
    private SingleCRS toGeodetic3D(
            final CompoundCRS crs, final SingleCRS horizontal, final VerticalCRS vertical, final boolean xyFirst)
            throws FactoryException {
        /*
         * Creates the set of axis in an order which depends of the xyFirst argument.
         * Then creates the property maps to be given to the object to be created.
         * They are common to whatever CRS type this method will create.
         */
        final CoordinateSystemAxis[] axis = new CoordinateSystemAxis[3];
        final CoordinateSystem cs = horizontal.getCoordinateSystem();
        axis[xyFirst ? 0 : 1] = cs.getAxis(0);
        axis[xyFirst ? 1 : 2] = cs.getAxis(1);
        axis[xyFirst ? 2 : 0] = vertical.getCoordinateSystem().getAxis(0);
        final Map<String, ?> csName, crsName;
        if (crs != null) {
            csName = AbstractIdentifiedObject.getProperties(crs.getCoordinateSystem());
            crsName = AbstractIdentifiedObject.getProperties(crs);
        } else {
            csName = getTemporaryName(cs);
            crsName = getTemporaryName(horizontal);
        }
        final CSFactory csFactory = getCSFactory();
        final CRSFactory crsFactory = getCRSFactory();
        if (horizontal instanceof GeographicCRS) {
            /*
             * Merges a 2D geographic CRS with the vertical CRS. This is the easiest
             * part - we just give the 3 axis all together to a new GeographicCRS.
             */
            final GeographicCRS sourceCRS = (GeographicCRS) horizontal;
            final EllipsoidalCS targetCS = csFactory.createEllipsoidalCS(csName, axis[0], axis[1], axis[2]);
            return crsFactory.createGeographicCRS(crsName, sourceCRS.getDatum(), targetCS);
        }
        if (horizontal instanceof ProjectedCRS) {
            /*
             * Merges a 2D projected CRS with the vertical CRS. This part is more tricky,
             * since we need a defining conversion which does not include axis swapping or
             * unit conversions. We revert them with concatenation of "CS to standardCS"
             * transform. The axis swapping will be added back by createProjectedCRS(...)
             * but not in the same place (they will be performed sooner than they would be
             * otherwise).
             */
            final ProjectedCRS sourceCRS = (ProjectedCRS) horizontal;
            final CartesianCS targetCS = csFactory.createCartesianCS(csName, axis[0], axis[1], axis[2]);
            final GeographicCRS base2D = sourceCRS.getBaseCRS();
            final GeographicCRS base3D = (GeographicCRS) toGeodetic3D(null, base2D, vertical, xyFirst);
            final Matrix prepend = toStandard(base2D, false);
            final Matrix append = toStandard(sourceCRS, true);
            Conversion projection = sourceCRS.getConversionFromBase();
            if (!prepend.isIdentity() || !append.isIdentity()) {
                final MathTransformFactory mtFactory = getMathTransformFactory();
                MathTransform mt = projection.getMathTransform();
                mt = mtFactory.createConcatenatedTransform(
                        mtFactory.createConcatenatedTransform(mtFactory.createAffineTransform(prepend), mt),
                        mtFactory.createAffineTransform(append));
                projection = new DefiningConversion(AbstractCS.getProperties(projection), projection.getMethod(), mt);
            }
            return crsFactory.createProjectedCRS(crsName, base3D, projection, targetCS);
        }
        // Should never happen.
        throw new AssertionError(horizontal);
    }

    private static Matrix toStandard(final CoordinateReferenceSystem crs, final boolean inverse) {
        final CoordinateSystem sourceCS = crs.getCoordinateSystem();
        final CoordinateSystem targetCS = AbstractCS.standard(sourceCS);
        if (inverse) {
            return AbstractCS.swapAndScaleAxis(targetCS, sourceCS);
        } else {
            return AbstractCS.swapAndScaleAxis(sourceCS, targetCS);
        }
    }

    /**
     * Returns a new coordinate reference system with only the specified dimension. This method is used for example in
     * order to get a component of a {@linkplain CompoundCRS compound CRS}.
     *
     * @param crs The original (usually compound) CRS.
     * @param dimensions The dimensions to keep.
     * @return The CRS with only the specified dimensions.
     * @throws FactoryException if the given dimensions can not be isolated in the given CRS.
     */
    public CoordinateReferenceSystem separate(final CoordinateReferenceSystem crs, final int[] dimensions)
            throws FactoryException {
        final int length = dimensions.length;
        final int crsDimension = crs.getCoordinateSystem().getDimension();
        if (length == 0
                || dimensions[0] < 0
                || dimensions[length - 1] >= crsDimension
                || !XArray.isStrictlySorted(dimensions)) {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, "dimension"));
        }
        if (length == crsDimension) {
            return crs;
        }
        /*
         * If the CRS is a compound one, separate each components independently.
         * For each component, we search the sub-array of 'dimensions' that apply
         * to this component and invoke 'separate' recursively.
         */
        if (crs instanceof CompoundCRS) {
            int count = 0, lowerDimension = 0, lowerIndex = 0;
            final List<CoordinateReferenceSystem> sources = ((CompoundCRS) crs).getCoordinateReferenceSystems();
            final CoordinateReferenceSystem[] targets = new CoordinateReferenceSystem[sources.size()];
            search:
            for (final CoordinateReferenceSystem source : sources) {
                final int upperDimension =
                        lowerDimension + source.getCoordinateSystem().getDimension();
                /*
                 * 'source' CRS applies to dimension 'lowerDimension' inclusive to 'upperDimension'
                 * exclusive. Now search the smallest range in the user-specified 'dimensions' that
                 * cover the [lowerDimension .. upperDimension] range.
                 */
                if (lowerIndex == dimensions.length) {
                    break search;
                }
                while (dimensions[lowerIndex] < lowerDimension) {
                    if (++lowerIndex == dimensions.length) {
                        break search;
                    }
                }
                int upperIndex = lowerIndex;
                while (dimensions[upperIndex] < upperDimension) {
                    if (++upperIndex == dimensions.length) {
                        break;
                    }
                }
                if (lowerIndex != upperIndex) {
                    final int[] sub = new int[upperIndex - lowerIndex];
                    for (int j = 0; j < sub.length; j++) {
                        sub[j] = dimensions[j + lowerIndex] - lowerDimension;
                    }
                    targets[count++] = separate(source, sub);
                }
                lowerDimension = upperDimension;
                lowerIndex = upperIndex;
            }
            if (count == 1) {
                return targets[0];
            }
            return getCRSFactory().createCompoundCRS(getTemporaryName(crs), XArray.resize(targets, count));
        }
        /*
         * TODO: Implement other cases here (3D-GeographicCRS, etc.).
         *       It may requires the creation of new CoordinateSystem objects,
         *       which is why this method live in ReferencingFactoryContainer.
         */
        final Object arg0 = crs.getName().getCode();
        throw new FactoryException(MessageFormat.format(ErrorKeys.CANT_SEPARATE_CRS_$1, arg0));
    }

    /** Returns a temporary name for object derived from the specified one. */
    private static Map<String, ?> getTemporaryName(final IdentifiedObject source) {
        return Collections.singletonMap(
                IdentifiedObject.NAME_KEY, source.getName().getCode() + " (3D)");
    }
}
