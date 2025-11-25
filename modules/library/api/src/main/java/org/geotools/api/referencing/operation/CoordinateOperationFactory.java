/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.operation;

import java.util.Map;
import java.util.Set;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.ObjectFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

/**
 * Creates {@linkplain CoordinateOperation coordinate operations}. This factory is capable to find coordinate
 * {@linkplain Transformation transformations} or {@linkplain Conversion conversions} between two
 * {@linkplain CoordinateReferenceSystem coordinate reference systems}.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface CoordinateOperationFactory extends ObjectFactory {
    /**
     * Returns an operation for conversion or transformation between two coordinate reference systems.
     *
     * <ul>
     *   <li>If an operation exists, it is returned.
     *   <li>If more than one operation exists, the default is returned.
     *   <li>If no operation exists, then the exception is thrown.
     * </ul>
     *
     * <p>Implementations may try to
     * {@linkplain CoordinateOperationAuthorityFactory#createFromCoordinateReferenceSystemCodes query an authority
     * factory} first, and compute the operation next if no operation from {@code source} to {@code target} code was
     * explicitly defined by the authority.
     *
     * @param sourceCRS Input coordinate reference system.
     * @param targetCRS Output coordinate reference system.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws OperationNotFoundException if no operation path was found from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException if the operation creation failed for some other reason.
     */
    CoordinateOperation createOperation(CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS)
            throws OperationNotFoundException, FactoryException;

    /**
     * Returns an operation using a particular method for conversion or transformation between two coordinate reference
     * systems.
     *
     * <ul>
     *   <li>If the operation exists on the implementation, then it is returned.
     *   <li>If the operation does not exist on the implementation, then the implementation has the option of inferring
     *       the operation from the argument objects.
     *   <li>If for whatever reason the specified operation will not be returned, then the exception is thrown.
     * </ul>
     *
     * <p><b>Example:</b> A transformation between two {@linkplain org.geotools.api.referencing.crs.GeographicCRS
     * geographic CRS} using different {@linkplain org.geotools.api.referencing.datum.GeodeticDatum datum} requires a
     * <cite>datum shift</cite>. Many methods exist for this purpose, including interpolations in a grid, a
     * scale/rotation/translation in geocentric coordinates or the Molodenski approximation. When invoking
     * {@code createOperation} without operation method, this factory may select by default the most accurate
     * transformation (typically interpolation in a grid). When invoking {@code createOperation} with an operation
     * method, user can force usage of Molodenski approximation for instance.
     *
     * @param sourceCRS Input coordinate reference system.
     * @param targetCRS Output coordinate reference system.
     * @param method The algorithmic method for conversion or transformation.
     * @return A coordinate operation from {@code sourceCRS} to {@code targetCRS}.
     * @throws OperationNotFoundException if no operation path was found from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException if the operation creation failed for some other reason.
     */
    CoordinateOperation createOperation(
            CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, OperationMethod method)
            throws OperationNotFoundException, FactoryException;

    /**
     * Creates a concatenated operation from a sequence of operations.
     *
     * @param properties Name and other properties to give to the new object. Available properties are
     *     {@linkplain ObjectFactory listed there}.
     * @param operations The sequence of operations.
     * @return The concatenated operation.
     * @throws FactoryException if the object creation failed.
     */
    CoordinateOperation createConcatenatedOperation(Map<String, ?> properties, CoordinateOperation... operations)
            throws FactoryException;

    /**
     * Constructs a defining conversion from a set of properties. Defining conversions have no
     * {@linkplain Conversion#getSourceCRS source} and {@linkplain Conversion#getTargetCRS target CRS}, and do not need
     * to have a {@linkplain Conversion#getMathTransform math transform}. Their sole purpose is to be given as an
     * argument to {@linkplain org.geotools.api.referencing.crs.CRSFactory#createDerivedCRS derived CRS} and
     * {@linkplain org.geotools.api.referencing.crs.CRSFactory#createProjectedCRS projected CRS} constructors.
     *
     * <p>Some available properties are {@linkplain ObjectFactory listed there}. Additionally, the following properties
     * are understood by this construtor:
     *
     * <p>
     *
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.geotools.api.referencing.operation.CoordinateOperation#OPERATION_VERSION_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link CoordinateOperation#getOperationVersion}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.geotools.api.referencing.operation.CoordinateOperation#COORDINATE_OPERATION_ACCURACY_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;<code>{@linkplain org.geotools.api.metadata.quality.PositionalAccuracy}[]</code>&nbsp;</td>
     *     <td nowrap>&nbsp;{@link CoordinateOperation#getCoordinateOperationAccuracy}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.geotools.api.referencing.operation.CoordinateOperation#DOMAIN_OF_VALIDITY_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link org.geotools.api.metadata.extent.Extent}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link CoordinateOperation#getDomainOfValidity}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@value org.geotools.api.referencing.operation.CoordinateOperation#SCOPE_KEY}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String} or {@link org.geotools.api.util.InternationalString}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link CoordinateOperation#getScope}</td>
     *   </tr>
     * </table>
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param method The operation method.
     * @param parameters The parameter values.
     * @return The defining conversion.
     * @throws FactoryException if the object creation failed.
     * @see org.geotools.api.referencing.crs.CRSFactory#createProjectedCRS
     * @see org.geotools.api.referencing.crs.CRSFactory#createDerivedCRS
     * @since GeoAPI 2.1
     */
    Conversion createDefiningConversion(
            Map<String, ?> properties, OperationMethod method, ParameterValueGroup parameters) throws FactoryException;

    /**
     * Returns all the available operations for conversion or transformation between two coordinate reference systems.
     * An empty set is returned if no operation exists.
     *
     * @param sourceCRS Input coordinate reference system.
     * @param targetCRS Output coordinate reference system.
     * @return A set of coordinate operations from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException if there was a failure retrieving or creating the operations.
     */
    Set<CoordinateOperation> findOperations(CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS)
            throws FactoryException;

    /**
     * Returns a list of available coordinate operations explicitly defined in some database (typically EPSG), for the
     * provided CRS pair. Otherwise (if there is no database, or if the database doesn't contains any explicit operation
     * from {@code sourceCRS} to {@code targetCRS}, or if this method failed to create the operations from the
     * database), returns an empty {@link Set}.
     *
     * <p>The default implementation always returns an empty {@link Set}. Subclasses should override this method if they
     * can fetch a more accurate operation from some database.
     *
     * <p>This method is normally invoked by <code>{@linkplain #findOperations findOperations}(sourceCRS,
     * targetCRS)</code> before to try to figure out a transformation path by itself.
     *
     * <p>The default implementation returns an empty set, subclasses working with an actual transformation database can
     * override
     *
     * @param sourceCRS Input coordinate reference system.
     * @param targetCRS Output coordinate reference system.
     * @param limit The maximum number of operations to be returned. Use -1 to return all the available operations. Use
     *     1 to return just one operation. Currently, the behavior for other values of {@code limit} is undefined.
     * @return A set of coordinate operations from {@code sourceCRS} to {@code targetCRS} if and only if one is
     *     explicitly defined in some underlying database, or an empty {@link Set} otherwise.
     */
    default Set<CoordinateOperation> findFromDatabase(
            CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, int limit) {
        return Set.of();
    }
}
