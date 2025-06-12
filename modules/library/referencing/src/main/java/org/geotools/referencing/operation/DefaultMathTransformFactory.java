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

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchIdentifierException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.operation.Conversion;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransformFactory;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.api.referencing.operation.Operation;
import org.geotools.api.referencing.operation.OperationMethod;
import org.geotools.api.referencing.operation.Projection;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.ParameterWriter;
import org.geotools.parameter.Parameters;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.cs.AbstractCS;
import org.geotools.referencing.factory.ReferencingFactory;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.PassThroughTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.referencing.util.CRSUtilities;
import org.geotools.referencing.wkt.MathTransformParser;
import org.geotools.referencing.wkt.Symbols;
import org.geotools.util.Arguments;
import org.geotools.util.CanonicalSet;
import org.geotools.util.LazySet;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.Hints;

/**
 * Low level factory for creating {@linkplain MathTransform math transforms}. Many high level GIS applications will
 * never need to use this factory directly; they can use a {@linkplain DefaultCoordinateOperationFactory coordinate
 * operation factory} instead. However, the {@code MathTransformFactory} interface can be used directly by applications
 * that wish to transform other types of coordinates (e.g. color coordinates, or image pixel coordinates).
 *
 * <p>A {@linkplain MathTransform math transform} is an object that actually does the work of applying formulae to
 * coordinate values. The math transform does not know or care how the coordinates relate to positions in the real
 * world. This lack of semantics makes implementing {@code MathTransformFactory} significantly easier than it would be
 * otherwise.
 *
 * <p>For example the affine transform applies a matrix to the coordinates without knowing how what it is doing relates
 * to the real world. So if the matrix scales <var>Z</var> values by a factor of 1000, then it could be converting
 * meters into millimeters, or it could be converting kilometers into meters.
 *
 * <p>Because {@linkplain MathTransform math transforms} have low semantic value (but high mathematical value),
 * programmers who do not have much knowledge of how GIS applications use coordinate systems, or how those coordinate
 * systems relate to the real world can implement {@code MathTransformFactory}. The low semantic content of
 * {@linkplain MathTransform math transforms} also means that they will be useful in applications that have nothing to
 * do with GIS coordinates. For example, a math transform could be used to map color coordinates between different color
 * spaces, such as converting (red, green, blue) colors into (hue, light, saturation) colors.
 *
 * <p>Since a {@linkplain MathTransform math transform} does not know what its source and target coordinate systems
 * mean, it is not necessary or desirable for a math transform object to keep information on its source and target
 * coordinate systems.
 *
 * @since 2.1
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultMathTransformFactory extends ReferencingFactory implements MathTransformFactory {
    /** The hints to provide to math transform providers. Null for now, but may be non-null in some future version. */
    private static final Hints HINTS = null;

    /**
     * The object to use for parsing <cite>Well-Known Text</cite> (WKT) strings. Will be created only when first needed.
     */
    private transient MathTransformParser parser;

    /**
     * The last value returned by {@link #getProvider}. Stored as an optimization since the same provider is often asked
     * many times.
     */
    private transient MathTransformProvider lastProvider;

    /** The operation method for the last transform created. */
    private static final ThreadLocal<OperationMethod> lastMethod = new ThreadLocal<>();

    /**
     * A pool of math transform. This pool is used in order to returns instance of existing math transforms when
     * possible.
     */
    private final CanonicalSet<MathTransform> pool;

    /** The service registry for finding {@link MathTransformProvider} implementations. */
    private final FactoryRegistry registry;

    /** Constructs a default {@link MathTransform math transform} factory. */
    public DefaultMathTransformFactory() {
        this(MathTransformProvider.class);
    }

    /**
     * Constructs a default {@link MathTransform math transform} factory using the specified
     * {@linkplain MathTransformProvider transform providers} categories.
     *
     * @param categories The providers categories, as implementations of {@link MathTransformProvider}.
     */
    private DefaultMathTransformFactory(final Class<?>... categories) {
        registry = new FactoryRegistry(Arrays.asList(categories));
        pool = CanonicalSet.newInstance(MathTransform.class);
    }

    /**
     * Returns the vendor responsible for creating this factory implementation. Many implementations may be available
     * for the same factory interface. The default implementation returns {@linkplain Citations#GEOTOOLS Geotools}.
     *
     * @return The vendor for this factory implementation.
     */
    @Override
    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    /**
     * Returns a set of available methods for {@linkplain MathTransform math transforms}. For each element in this set,
     * the {@linkplain OperationMethod#getName operation method name} must be known to the {@link #getDefaultParameters}
     * method in this factory. The set of available methods is implementation dependent.
     *
     * @param type <code>{@linkplain Operation}.class</code> for fetching all operation methods, or <code>
     *     {@linkplain Projection}.class</code> for fetching only map projection methods.
     * @return All {@linkplain MathTransform math transform} methods available in this factory.
     * @see #getDefaultParameters
     * @see #createParameterizedTransform
     */
    @Override
    public Set<OperationMethod> getAvailableMethods(final Class<? extends Operation> type) {
        return new LazySet<>(registry.getFactories(
                MathTransformProvider.class, (type != null) ? new MethodFilter(type) : null, HINTS));
    }

    /** A filter for the set of available operations. */
    private static final class MethodFilter implements Predicate<MathTransformProvider> {
        /** The expected type ({@code Projection.class}) for projections). */
        private final Class<? extends Operation> type;

        /** Constructs a filter for the set of math operations methods. */
        public MethodFilter(final Class<? extends Operation> type) {
            this.type = type;
        }

        /**
         * Returns {@code true} if the specified element should be included. If the type is unknown, conservatively
         * returns {@code true}.
         */
        @Override
        public boolean test(MathTransformProvider element) {
            if (element != null) {
                final Class<? extends Operation> t = element.getOperationType();
                if (t != null && !type.isAssignableFrom(t)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns the operation method used for the latest call to {@link #createParameterizedTransform
     * createParameterizedTransform} in the currently running thread. Returns {@code null} if not applicable.
     *
     * @see #createParameterizedTransform
     * @since 2.5
     */
    @Override
    public OperationMethod getLastMethodUsed() {
        return lastMethod.get();
    }

    /**
     * Returns the operation method for the specified name.
     *
     * @param name The case insensitive {@linkplain org.geotools.api.metadata.Identifier#getCode identifier code} of the
     *     operation method to search for (e.g. {@code "Transverse_Mercator"}).
     * @return The operation method.
     * @throws NoSuchIdentifierException if there is no operation method registered for the specified name.
     * @since 2.2
     */
    public OperationMethod getOperationMethod(String name) throws NoSuchIdentifierException {
        return getProvider(name);
    }

    /**
     * Returns the math transform provider for the specified operation method. This provider can be used in order to
     * query parameter for a method name (e.g. <code>
     * getProvider("Transverse_Mercator").getParameters()</code>), or any of the alias in a given locale.
     *
     * @param method The case insensitive {@linkplain org.geotools.api.metadata.Identifier#getCode identifier code} of
     *     the operation method to search for (e.g. {@code "Transverse_Mercator"}).
     * @return The math transform provider.
     * @throws NoSuchIdentifierException if there is no provider registered for the specified method.
     */
    private MathTransformProvider getProvider(final String method) throws NoSuchIdentifierException {
        /*
         * Copies the 'lastProvider' reference in order to avoid synchronization. This is safe
         * because copy of object references are atomic operations.  Note that this is not the
         * deprecated "double check" idiom since we are not creating new objects, but checking
         * for existing ones.
         */
        MathTransformProvider provider = lastProvider;
        if (provider != null && provider.nameMatches(method)) {
            return provider;
        }

        provider = registry.getFactories(MathTransformProvider.class, null, HINTS)
                .filter(prov -> prov.nameMatches(method))
                .findAny()
                .orElseThrow(() -> new NoSuchIdentifierException(
                        MessageFormat.format(ErrorKeys.NO_TRANSFORM_FOR_CLASSIFICATION_$1, method), method));

        return lastProvider = provider;
    }

    /**
     * Returns the default parameter values for a math transform using the given method. The method argument is the name
     * of any operation method returned by the {@link #getAvailableMethods} method. A typical example is <code>
     * "<A HREF="http://www.remotesensing.org/geotiff/proj_list/transverse_mercator.html">Transverse_Mercator</A>"
     * </code>).
     *
     * <p>This method creates new parameter instances at every call. It is intented to be modified by the user before to
     * be passed to <code>{@linkplain #createParameterizedTransform
     * createParameterizedTransform}(parameters)</code>.
     *
     * @param method The case insensitive name of the method to search for.
     * @return The default parameter values.
     * @throws NoSuchIdentifierException if there is no transform registered for the specified method.
     * @see #getAvailableMethods
     * @see #createParameterizedTransform
     * @see org.geotools.referencing.operation.transform.AbstractMathTransform#getParameterValues
     */
    @Override
    public ParameterValueGroup getDefaultParameters(final String method) throws NoSuchIdentifierException {
        return getProvider(method).getParameters().createValue();
    }

    /**
     * Creates a {@linkplain #createParameterizedTransform parameterized transform} from a base CRS to a derived CS. If
     * the {@code "semi_major"} and {@code "semi_minor"} parameters are not explicitly specified, they will be inferred
     * from the {@linkplain Ellipsoid ellipsoid} and added to {@code parameters}. In addition, this method performs axis
     * switch as needed.
     *
     * <p>The {@linkplain OperationMethod operation method} used can be obtained by a call to
     * {@link #getLastUsedMethod}.
     *
     * @param baseCRS The source coordinate reference system.
     * @param parameters The parameter values for the transform.
     * @param derivedCS the target coordinate system.
     * @return The parameterized transform.
     * @throws NoSuchIdentifierException if there is no transform registered for the method.
     * @throws FactoryException if the object creation failed. This exception is thrown if some required parameter has
     *     not been supplied, or has illegal value.
     */
    @Override
    public MathTransform createBaseToDerived(
            final CoordinateReferenceSystem baseCRS,
            final ParameterValueGroup parameters,
            final CoordinateSystem derivedCS)
            throws NoSuchIdentifierException, FactoryException {
        /*
         * If the user's parameter do not contains semi-major and semi-minor axis length, infers
         * them from the ellipsoid. This is a convenience service since the user often omit those
         * parameters (because they duplicate datum information).
         */
        final Ellipsoid ellipsoid = CRSUtilities.getHeadGeoEllipsoid(baseCRS);
        if (ellipsoid != null) {
            final Unit<Length> axisUnit = ellipsoid.getAxisUnit();
            Parameters.ensureSet(parameters, "semi_major", ellipsoid.getSemiMajorAxis(), axisUnit, false);
            Parameters.ensureSet(parameters, "semi_minor", ellipsoid.getSemiMinorAxis(), axisUnit, false);
        }
        MathTransform baseToDerived = createParameterizedTransform(parameters);
        final OperationMethod method = lastMethod.get();
        baseToDerived = createBaseToDerived(baseCRS, baseToDerived, derivedCS);
        lastMethod.set(method);
        return baseToDerived;
    }

    /**
     * Creates a transform from a base CRS to a derived CS. This method expects a "raw" transform without unit
     * conversion or axis switch, typically a map projection working on (<cite>longitude</cite>, <cite>latitude</cite>)
     * axes in degrees and (<cite>x</cite>, <cite>y</cite>) axes in metres. This method inspects the coordinate systems
     * and prepend or append the unit conversions and axis switchs automatically.
     *
     * @param baseCRS The source coordinate reference system.
     * @param projection The "raw" <cite>base to derived</cite> transform.
     * @param derivedCS the target coordinate system.
     * @return The parameterized transform.
     * @throws FactoryException if the object creation failed. This exception is thrown if some required parameter has
     *     not been supplied, or has illegal value.
     * @since 2.5
     */
    public MathTransform createBaseToDerived(
            final CoordinateReferenceSystem baseCRS, final MathTransform projection, final CoordinateSystem derivedCS)
            throws FactoryException {
        /*
         * Computes matrix for swapping axis and performing units conversion.
         * There is one matrix to apply before projection on (longitude,latitude)
         * coordinates, and one matrix to apply after projection on (easting,northing)
         * coordinates.
         */
        final CoordinateSystem sourceCS = baseCRS.getCoordinateSystem();
        final Matrix swap1, swap3;
        try {
            swap1 = AbstractCS.swapAndScaleAxis(sourceCS, AbstractCS.standard(sourceCS));
            swap3 = AbstractCS.swapAndScaleAxis(AbstractCS.standard(derivedCS), derivedCS);
        } catch (IllegalArgumentException cause) {
            // User-specified axis don't match.
            throw new FactoryException(cause);
        }
        /*
         * Prepares the concatenation of the matrix computed above and the projection.
         * Note that at this stage, the dimensions between each step may not be compatible.
         * For example the projection (step2) is usually two-dimensional while the source
         * coordinate system (step1) may be three-dimensional if it has a height.
         */
        MathTransform step1 = createAffineTransform(swap1);
        MathTransform step3 = createAffineTransform(swap3);
        MathTransform step2 = projection;
        /*
         * If the target coordinate system has a height, instructs the projection to pass
         * the height unchanged from the base CRS to the target CRS. After this block, the
         * dimensions of 'step2' and 'step3' should match.
         */
        final int numTrailingOrdinates = step3.getSourceDimensions() - step2.getTargetDimensions();
        if (numTrailingOrdinates > 0) {
            step2 = createPassThroughTransform(0, step2, numTrailingOrdinates);
        }
        /*
         * If the source CS has a height but the target CS doesn't, drops the extra coordinates.
         * After this block, the dimensions of 'step1' and 'step2' should match.
         */
        final int sourceDim = step1.getTargetDimensions();
        final int targetDim = step2.getSourceDimensions();
        if (sourceDim > targetDim) {
            final Matrix drop = MatrixFactory.create(targetDim + 1, sourceDim + 1);
            drop.setElement(targetDim, sourceDim, 1);
            step1 = createConcatenatedTransform(createAffineTransform(drop), step1);
        }
        return createConcatenatedTransform(createConcatenatedTransform(step1, step2), step3);
    }

    /**
     * Creates a transform from a group of parameters. The method name is inferred from the
     * {@linkplain org.geotools.api.parameter.ParameterDescriptorGroup#getName parameter group name}. Example:
     *
     * <blockquote>
     *
     * <pre>
     * ParameterValueGroup p = factory.getDefaultParameters("Transverse_Mercator");
     * p.parameter("semi_major").setValue(6378137.000);
     * p.parameter("semi_minor").setValue(6356752.314);
     * MathTransform mt = factory.createParameterizedTransform(p);
     * </pre>
     *
     * </blockquote>
     *
     * @param parameters The parameter values.
     * @return The parameterized transform.
     * @throws NoSuchIdentifierException if there is no transform registered for the method.
     * @throws FactoryException if the object creation failed. This exception is thrown if some required parameter has
     *     not been supplied, or has illegal value.
     * @see #getDefaultParameters
     * @see #getAvailableMethods
     * @see #getLastUsedMethod
     */
    @Override
    public MathTransform createParameterizedTransform(ParameterValueGroup parameters)
            throws NoSuchIdentifierException, FactoryException {
        MathTransform transform;
        OperationMethod method = null;
        try {
            final String classification = parameters.getDescriptor().getName().getCode();
            final MathTransformProvider provider = getProvider(classification);
            method = provider;
            try {
                parameters = provider.ensureValidValues(parameters);
                transform = provider.createMathTransform(parameters);
            } catch (IllegalArgumentException exception) {
                /*
                 * Catch only exceptions which may be the result of improper parameter
                 * usage (e.g. a value out of range). Do not catch exception caused by
                 * programming errors (e.g. null pointer exception).
                 */
                throw new FactoryException(exception);
            }
            if (transform instanceof MathTransformProvider.Delegate) {
                final MathTransformProvider.Delegate delegate = (MathTransformProvider.Delegate) transform;
                method = delegate.method;
                transform = delegate.transform;
            }
            transform = pool.unique(transform);
        } finally {
            lastMethod.set(method); // May be null in case of failure, which is intented.
        }
        return transform;
    }

    /**
     * Creates an affine transform from a matrix. If the transform's input dimension is {@code M}, and output dimension
     * is {@code N}, then the matrix will have size {@code [N+1][M+1]}. The +1 in the matrix dimensions allows the
     * matrix to do a shift, as well as a rotation. The {@code [M][j]} element of the matrix will be the j'th ordinate
     * of the moved origin. The {@code [i][N]} element of the matrix will be 0 for <var>i</var> less than {@code M}, and
     * 1 for <var>i</var> equals {@code M}.
     *
     * @param matrix The matrix used to define the affine transform.
     * @return The affine transform.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public MathTransform createAffineTransform(final Matrix matrix) throws FactoryException {
        lastMethod.remove(); // To be strict, we should set ProjectiveTransform.Provider
        return pool.unique(ProjectiveTransform.create(matrix));
    }

    /**
     * Creates a transform by concatenating two existing transforms. A concatenated transform acts in the same way as
     * applying two transforms, one after the other.
     *
     * <p>The dimension of the output space of the first transform must match the dimension of the input space in the
     * second transform. If you wish to concatenate more than two transforms, then you can repeatedly use this method.
     *
     * @param transform1 The first transform to apply to points.
     * @param transform2 The second transform to apply to points.
     * @return The concatenated transform.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public MathTransform createConcatenatedTransform(final MathTransform transform1, final MathTransform transform2)
            throws FactoryException {
        MathTransform tr;
        try {
            tr = ConcatenatedTransform.create(transform1, transform2);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        tr = pool.unique(tr);
        return tr;
    }

    /**
     * Creates a transform which passes through a subset of ordinates to another transform. This allows transforms to
     * operate on a subset of ordinates. For example, if you have (<var>Lat</var>,<var>Lon</var>,<var>Height</var>)
     * coordinates, then you may wish to convert the height values from meters to feet without affecting the
     * (<var>Lat</var>,<var>Lon</var>) values.
     *
     * @param firstAffectedOrdinate The lowest index of the affected ordinates.
     * @param subTransform Transform to use for affected ordinates.
     * @param numTrailingOrdinates Number of trailing ordinates to pass through. Affected ordinates will range from
     *     {@code firstAffectedOrdinate} inclusive to {@code dimTarget-numTrailingOrdinates} exclusive.
     * @return A pass through transform with the following dimensions:<br>
     *     <pre>
     * Source: firstAffectedOrdinate + subTransform.getSourceDimensions() + numTrailingOrdinates
     * Target: firstAffectedOrdinate + subTransform.getTargetDimensions() + numTrailingOrdinates
     *     </pre>
     *
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public MathTransform createPassThroughTransform(
            final int firstAffectedOrdinate, final MathTransform subTransform, final int numTrailingOrdinates)
            throws FactoryException {
        MathTransform tr;
        try {
            tr = PassThroughTransform.create(firstAffectedOrdinate, subTransform, numTrailingOrdinates);
        } catch (IllegalArgumentException exception) {
            throw new FactoryException(exception);
        }
        tr = pool.unique(tr);
        return tr;
    }

    /**
     * Creates a math transform object from a XML string. The default implementation always throws an exception, since
     * this method is not yet implemented.
     *
     * @param xml Math transform encoded in XML format.
     * @throws FactoryException if the object creation failed.
     */
    @Override
    public MathTransform createFromXML(String xml) throws FactoryException {
        throw new FactoryException("Not yet implemented.");
    }

    /**
     * Creates a math transform object from a <A
     * HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well Known
     * Text</cite> (WKT)</A>.
     *
     * @param text Math transform encoded in Well-Known Text format.
     * @return The math transform (never {@code null}).
     * @throws FactoryException if the Well-Known Text can't be parsed, or if the math transform creation failed from
     *     some other reason.
     */
    @Override
    public synchronized MathTransform createFromWKT(final String text) throws FactoryException {
        // Note: while this factory is thread safe, the WKT parser is not.
        //       Since we share a single instance of this parser, we must
        //       synchronize.
        if (parser == null) {
            parser = new MathTransformParser(Symbols.DEFAULT, this);
        }
        try {
            return parser.parseMathTransform(text);
        } catch (ParseException exception) {
            final Throwable cause = exception.getCause();
            if (cause instanceof FactoryException) {
                throw (FactoryException) cause;
            }
            throw new FactoryException(exception);
        }
    }

    /**
     * Scans for factory plug-ins on the application class path. This method is needed because the application class
     * path can theoretically change, or additional plug-ins may become available. Rather than re-scanning the classpath
     * on every invocation of the API, the class path is scanned automatically only on the first invocation. Clients can
     * call this method to prompt a re-scan. Thus this method need only be invoked by sophisticated applications which
     * dynamically make new plug-ins available at runtime.
     */
    public void scanForPlugins() {
        registry.scanForPlugins();
    }

    /**
     * Dump to the standard output stream a list of available operation methods. This method can be invoked from the
     * command line. It provides a mean to verify which transforms were found in the classpath. The syntax is: <br>
     *
     * <BLOCKQUOTE>
     *
     * <CODE>
     * java org.geotools.referencing.operation.DefaultMathTransformFactory
     * <VAR>&lt;options&gt;</VAR> <VAR>&lt;method&gt;</VAR>
     * </CODE>
     *
     * </BLOCKQUOTE>
     *
     * <p>where options are:
     *
     * <TABLE CELLPADDING='0' CELLSPACING='0'>
     *   <TR><TD NOWRAP><CODE>-projections</CODE></TD>
     *       <TD NOWRAP>&nbsp;List only projections</TD></TR>
     *   <TR><TD NOWRAP><CODE>-conversions</CODE></TD>
     *       <TD NOWRAP>&nbsp;List only conversions</TD></TR>
     *   <TR><TD NOWRAP><CODE>-all</CODE></TD>
     *       <TD NOWRAP>&nbsp;List the parameters for all transforms</TD></TR>
     *   <TR><TD NOWRAP><CODE>-encoding</CODE> <VAR>&lt;code&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;Set the character encoding</TD></TR>
     *   <TR><TD NOWRAP><CODE>-locale</CODE> <VAR>&lt;language&gt;</VAR></TD>
     *       <TD NOWRAP>&nbsp;Set the language for the output (e.g. "fr" for French)</TD></TR>
     * </TABLE>
     *
     * <p>and <VAR>&lt;method&gt;</VAR> is the optional name of an operation method (e.g. <CODE>
     * "Affine"</CODE>, <CODE>"EPSG:9624"</CODE> or just <CODE>"9624"</CODE> for the affine transform method).
     *
     * <p><strong>Note for Windows users:</strong> If the output contains strange symbols, try to supply an
     * "{@code -encoding}" argument. Example:
     *
     * <blockquote>
     *
     * <code>
     * java org.geotools.referencing.operation.DefaultMathTransformFactory -encoding Cp850
     * </code>
     *
     * </blockquote>
     *
     * <p>The codepage number (850 in the previous example) can be obtained from the DOS commande line using the
     * "{@code chcp}" command with no arguments.
     *
     * @param args Command line arguments.
     */
    public static void main(String... args) {
        /*
         * Parse the command-line arguments and print the summary.
         */
        final Arguments arguments = new Arguments(args);
        final boolean printAll = arguments.getFlag("-all");
        Class<? extends Operation> type = null;
        if (arguments.getFlag("-projections")) type = Projection.class;
        if (arguments.getFlag("-conversions")) type = Conversion.class;
        args = arguments.getRemainingArguments(1);
        try (ParameterWriter writer = new ParameterWriter(arguments.out)) {
            final DefaultMathTransformFactory factory = new DefaultMathTransformFactory();

            writer.setLocale(arguments.locale);
            Set<OperationMethod> methods = Collections.emptySet();
            if (printAll || args.length == 0) {
                final Set<String> scopes = new HashSet<>();
                //              scopes.add("OGC");  // Omitted because usually the same than
                // 'identifier'.
                scopes.add("EPSG");
                scopes.add("Geotools"); // Limit the number of columns to output.
                methods = new TreeSet<>(AbstractIdentifiedObject.NAME_COMPARATOR);
                methods.addAll(factory.getAvailableMethods(type));
                writer.summary(methods, scopes);
            }
            if (!printAll) {
                if (args.length == 0) {
                    methods = Collections.emptySet();
                } else {
                    methods = Collections.singleton(factory.getProvider(args[0]));
                }
            }
            /*
             * Iterates through all math transform to print. It may be a singleton
             * if the user ask for a specific math transform.
             */
            final String lineSeparator = System.getProperty("line.separator", "\n");
            for (final OperationMethod method : methods) {
                arguments.out.write(lineSeparator);
                writer.format(method);
            }
            arguments.out.flush();
        } catch (NoSuchIdentifierException exception) {
            arguments.err.println(exception.getLocalizedMessage());
        } catch (Exception exception) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", exception);
        }
    }

    /** Cleans up the thread local set in this thread. They can prevent web applications from proper shutdown */
    public static void cleanupThreadLocals() {
        lastMethod.remove();
    }
}
