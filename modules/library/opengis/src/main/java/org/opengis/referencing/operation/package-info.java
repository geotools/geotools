/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

/**
 * {@linkplain org.opengis.referencing.operation.CoordinateOperation Coordinate operations} (relationship between
 * any two {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference systems}).
 * The following is adapted from
 * <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">OpenGIS&reg;
 * Spatial Referencing by Coordinates (Topic 2)</A> specification.
 *
 * <P ALIGN="justify">If the relationship between any two coordinate reference
 * systems is known, coordinates can be transformed or converted to another
 * coordinate reference system. The UML model therefore specifies a source and
 * a target coordinate reference system for such coordinate operations. For that
 * reason, a coordinate operation is often popularly said to "transform coordinate
 * reference system <VAR>A</VAR> into coordinate reference system <VAR>B</VAR>".
 * Although this wording may be good enough for conversation, it should be realised
 * that coordinate operations do not operate on coordinate reference systems, but
 * on coordinates.</P>
 *
 * <P ALIGN="justify">Coordinate operations are divided into two subtypes:</P>
 * <UL>
 *   <LI><P ALIGN="justify"><EM>Coordinate conversion</EM> -
 *       mathematical operation on coordinates that does not include any change of
 *       Datum. The best-known example of a coordinate conversion is a map projection.
 *       The parameters describing coordinate conversions are defined rather than
 *       empirically derived. Note that some conversions have no parameters.</P></LI>
 *
 *   <LI><P ALIGN="justify"><EM>Coordinate transformation</EM> -
 *       mathematical operation on coordinates that usually includes a change of Datum.
 *       The parameters of a coordinate transformation are empirically derived from data
 *       containing the coordinates of a series of points in both coordinate reference
 *       systems. This computational process is usually 'over-determined', allowing
 *       derivation of error (or accuracy) estimates for the transformation. Also, the
 *       stochastic nature of the parameters may result in multiple (different) versions
 *       of the same coordinate transformation. Because of this several transformations
 *       may exist for a given pair of coordinate reference systems, differing in their
 *       transformation method, parameter values and accuracy characteristics.</P></LI>
 * </UL>
 *
 * <P ALIGN="justify">A coordinate operation (transformation or conversion) can be
 * time-varying, and must be time-varying if either the source or target CRS is time
 * varying. When the coordinate operation is time-varying, the operation method used
 * will also be time-varying, and some of the parameters used by that operation method
 * will involve time. For example, some of the parameters may have time, velocity, and/or
 * acceleration values and units.</P>
 *
 * <H3>Coordinate conversions</H3>
 * <P ALIGN="justify">Coordinate conversions are coordinate operations that make use
 * of exact, defined (rather than measured or computed), and therefore error-free
 * parameter values. Corresponding pairs of coordinate tuples in each of the two
 * coordinate reference systems connected through a coordinate conversion have a
 * fixed arithmetic relationship. Additionally one of the two tuples cannot exist
 * without specification of the coordinate conversion and the 'source' coordinate
 * reference system. Coordinate conversions are therefore intimately related to the
 * concept of {@linkplain org.opengis.referencing.crs.DerivedCRS Derived CRS}.</P>
 *
 * <P ALIGN="justify">The best-known example of this source-derived relationship
 * is a {@linkplain org.opengis.referencing.crs.ProjectedCRS projected coordinate reference system},
 * which is always based on a source {@linkplain org.opengis.referencing.crs.GeographicCRS geographic
 * coordinate reference system}. The associated map projection effectively defines the
 * projected coordinate reference system from the geographic coordinate system.</P>
 *
 * <H3>Concatenated coordinate operation</H3>
 * <P ALIGN="justify">A concatenated coordinate operation is an ordered sequence of
 * coordinate operations. The sequence of operations is constrained by the requirement
 * that the source coordinate reference system of step (<VAR>n</VAR>+1) must be the
 * same as the target coordinate reference system of step (<VAR>n</VAR>). The source
 * coordinate reference system of the first step and the target coordinate reference
 * system of the last step are the source and target coordinate reference system
 * associated with the concatenated operation.</P>
 *
 * <P ALIGN="justify">The above constraint should not be interpreted as implying
 * that only those coordinate operations can be used in a concatenated operation
 * that have their source and a target coordinate reference system specified through
 * the association pair between {@link org.opengis.referencing.operation.CoordinateOperation} and
 * {@link org.opengis.referencing.crs.CoordinateReferenceSystem}. This would exclude coordinate
 * conversions. Concatenated coordinate operations may contain coordinate transformations
 * and/or coordinate conversions.</P>
 *
 * <P ALIGN="justify">The source and target coordinate reference system of a
 * coordinate conversion are defined in the {@link org.opengis.referencing.crs.GeneralDerivedCRS},
 * by specifying the base (i.e., source) CRS and the defining conversion. The derived
 * coordinate reference system itself is the target CRS in this situation. When used
 * in a concatenated operation, the conversion's source and target coordinate reference
 * system are equally subject to the above constraint as the source and target of a
 * transformation although they are specified in a different manner.</P>
 *
 * <P ALIGN="justify">The concatenated coordinate operation class is primarily intended
 * to provide a mechanism that forces application software to use a preferred path to go
 * from source to target coordinate reference system, if a direct transformation between
 * the two is not available.</P>
 *
 * <H3>Pass-through coordinate operation</H3>
 * <P ALIGN="justify">Coordinate operations require input coordinate tuples of certain
 * dimensions and produce output tuples of certain dimensions. The dimensions of these
 * coordinate tuples and the dimensions of the coordinate reference system they are defined
 * in must be the same.</P>
 *
 * <P ALIGN="justify">The ability to define compound coordinate reference systems
 * combining two or more other coordinate reference systems, not themselves compound,
 * introduces a difficulty. For example, it may be required to transform only the
 * horizontal or only the vertical component of a compound coordinate reference system,
 * which will put them at odds with coordinate operations specified for either horizontal
 * or vertical coordinates only. To the human mind this is a trivial problem, but not so
 * for coordinate transformation software that ought to be capable of automatic operation,
 * without human intervention; the software logic would be confronted with the problem of
 * having to apply a 2-dimensional coordinate operation to 3-dimensional coordinate tuples.</P>
 *
 * <P ALIGN="justify">This problem has been solved by defining a pass-through operation.
 * This operation specifies what subset of a coordinate tuple is subject to a requested
 * transformation. It takes the form of referencing another coordinate operation and specifying
 * a sequence of numbers defining the positions in the coordinate tuple of the coordinates
 * affected by that transformation. The order of the coordinates in a coordinate tuple
 * shall agree with the order of the coordinate system axes as defined for the associated
 * coordinate system.</P>
 *
 * <H3>Operation method and parameters</H3>
 * <P ALIGN="justify">The algorithm used to execute the coordinate operation is defined in the
 * {@linkplain org.opengis.referencing.operation.OperationMethod operation method}. Concatenated operations
 * and pass-through operations do not require a coordinate operation to be specified. Each
 * {@linkplain org.opengis.referencing.operation.OperationMethod operation method} uses a number of
 * {@linkplain org.opengis.parameter.ParameterValue parameters} (although some coordinate conversions
 * use none), and each {@linkplain org.opengis.referencing.operation.CoordinateOperation coordinate operation}
 * assigns value to these parameters.</P>
 *
 * <P ALIGN="justify">As this class comes close to the heart of any coordinate transformation
 * software, it is recommended to make extensive use of identifiers, referencing well-known
 * datasets wherever possible. There is as yet no standard way of spelling or even naming the
 * various coordinate operation methods. Client software requesting a coordinate operation to
 * be executed by a coordinate transformation server implementation may therefore ask for an
 * operation method this server doesn't recognise, although a perfectly valid method may be
 * available. The same holds for coordinate operation parameters used by any coordinate
 * operation method. To facilitate recognition and validation it is recommended that the
 * operation formulae be included or referenced in the relevant object, and if possible a
 * worked example.</P>
 *
 * <H3>Implementation considerations</H3>
 * <P ALIGN="justify">This explanation is not complete without giving some thought to
 * implementations. Coordinate transformation services should be able to automatically
 * derive coordinate operations that are not stored explicitly in any permanent data store,
 * in other words determine their own concatenated or inverse operations. The reason is that
 * is practically impossible to store all possible pairs of coordinate reference systems in
 * explicitly defined coordinate operations. The key to a successful software implementation
 * is the ability to apply meaningful constraints and validations to this process. For example:
 * it may be mathematically possible to derive a concatenated coordinate operation that will
 * transform North American Datum 1983 coordinates to Australian National Datum; but in a
 * practical sense that operation would be meaningless. The key validation that would flag
 * such an operation as invalid would be a comparison of the two areas of validity and the
 * conclusion that there is no overlap between these.</P>
 *
 * <P ALIGN="justify">Coordinate transformation services should also be able to derive or
 * infer the inverse of any coordinate operation (from <VAR>B</VAR> to <VAR>A</VAR>)
 * from its complementary forward operation (<VAR>A</VAR> to <VAR>B</VAR>). Most
 * permanent data stores for coordinate reference parameter data will record only
 * one of the two operations that may exist between any two coordinate reference systems.
 * The inverse operation is then inferred by the application software logic from the stored
 * operation.</P>
 *
 * <P ALIGN="justify">In some cases, the algorithm for the inverse operation is the same
 * as the forward algorithm, and only the signs of the parameter values need to be reversed
 * for the inverse operation to be fully defined. An example is the 7-parameter Helmert
 * transformation (both position vector and coordinate frame rotation convention).</P>
 *
 * <P ALIGN="justify">Some polynomial coordinate operations require the signs of only most,
 * but not all, parameter values to be reversed. Other coordinate operation methods imply
 * two algorithms, one for the forward and one for the inverse operation. The parameters
 * are generally the same in that case. The latter situation generally applies to
 * map projections.</P>
 *
 * <P ALIGN="justify">Finally the same algorithm may be used for the inverse operation,
 * with entirely different parameter values. This is the case with some polynomial and
 * affine operations. In those cases the inverse operation cannot be inferred from the
 * forward operation but must be explicitly defined.</P>
 *
 * <P ALIGN="justify">The logic to derive the inverse transformation should be built
 * into the application software, be it server or client, that performs the coordinate
 * operation.</P>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @since   GeoAPI 1.0
 */
package org.opengis.referencing.operation;
