/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

/**
 * {@linkplain org.opengis.coverage.Coverage Coverages} (generate a value for any point).
 * The following is adapted from ISO 19123 specification.
 *
 * <P ALIGN="justify">A {@linkplain org.opengis.coverage.Coverage coverage} is a feature that associates
 * positions within a bounded space (its {@linkplain org.opengis.coverage.Coverage#getDomainElements domain})
 * to feature attribute values (its {@linkplain org.opengis.coverage.Coverage#getRangeElements range}).
 * In other words, it is both a feature and a function. Examples include a raster image, a polygon
 * overlay, or a digital elevation matrix.</P>
 *
 * <P ALIGN="justify">A coverage may represent a single feature or a set of features.</P>
 *
 * <H3>Domain of a coverage</H3>
 * <P ALIGN="justify">A coverage domain is a set of geometric objects described in terms of
 * {@linkplain org.opengis.geometry.DirectPosition direct positions}. It may be
 * extended to all of the direct positions within the convex hull of that set of geometric objects.
 * The direct positions are associated with a spatial or temporal
 * {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference system}.
 * Commonly used domains include point sets, grids, collections of closed rectangles, and other
 * collections of geometric objects. The geometric objects may exhaustively partition the domain,
 * and thereby form a tessellation such as a grid or a TIN. Point sets and other sets of non-conterminous
 * geometric objects do not form tessellations. Coverage subtypes may be defined in terms of their domains.</P>
 *
 * <P ALIGN="justify">Coverage domains differ in both the coordinate dimension of the space in which they
 * exist and in the topological dimension of the geometric objects they contain. Clearly, the geometric
 * objects that make up a domain cannot have a topological dimension greater than the coordinate dimension
 * of the domain. A domain of coordinate dimension 3 may be composed of points, curves, surfaces, or solids,
 * while a domain of coordinate dimension 2 may be composed only of points, curves, or surfaces.
 * {@linkplain org.opengis.annotation.Specification#ISO_19107 ISO 19107} defines a number of geometric objects
 * (subtypes of the interface {@link org.opengis.geometry.Geometry}) to be used for the description
 * of features. Many of these geometric objects can be used to define domains for coverages. In addition, ISO 19108
 * defines {@code TM_GeometricPrimitives} that may also be used to define domains of coverages.</P>
 *
 * <H3>The range of a coverage</H3>
 * <P ALIGN="justify">The range of a coverage is a set of feature attribute values. It may be either a finite or
 * a transfinite set. Coverages often model many associated functions sharing the same domain. Therefore, the value
 * set is represented as a collection of records with a common schema.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>Example:</B>
 * A coverage might assign to each direct position in a county the temperature, pressure, humidity, and wind
 * velocity at noon, today, at that point. The coverage maps every direct position in the county to a record
 * of 4 fields.</P></BLOCKQUOTE>
 *
 * <P ALIGN="justify">A feature attribute value may be of any data type. However, evaluation of a
 * {@linkplain org.opengis.coverage.ContinuousCoverage continuous coverage} is usually implemented
 * by interpolation methods that can be applied only to numbers or vectors. Other data types are
 * almost always associated with {@linkplain org.opengis.coverage.DiscreteCoverage discrete coverages}.</P>
 *
 * <P ALIGN="justify">Given a record from the range of a coverage,
 * {@linkplain org.opengis.coverage.Coverage#evaluateInverse inverse evaluation} is the calculation and exposure
 * of a set of geometric objects associated with specific values of the attributes. Inverse evaluation may return
 * many geometric objects associated with a single feature attribute value.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>Example:</B>
 * Inverse evaluation is used for the extraction of contours from an elevation coverage and the extraction of
 * classified regions in an image.</P></BLOCKQUOTE>
 *
 * <H3>Discrete and continuous coverages</H3>
 * <P ALIGN="justify">Coverages are of two types. A {@linkplain org.opengis.coverage.DiscreteCoverage discrete coverage}
 * has a domain that consists of a finite collection of geometric objects and the direct positions contained in those
 * geometric objects. A discrete coverage maps each geometric object to a single record of feature attribute values.
 * The geometric object and its associated record form a geometry value pair. A discrete coverage is thus a discrete
 * or step function as opposed to a continuous coverage. Discrete functions can be explicitly enumerated as
 * (<var>input</var>, <var>output</var>) pairs. A discrete coverage may be represented as a collection of ordered pairs
 * of independent and dependent variables. Each independent variable is a geometric object and each dependent variable
 * is a record of feature attribute values.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>Example:</B>
 * A coverage that maps a set of polygons to the soil type found within each polygon is an example of a
 * discrete coverage.</P></BLOCKQUOTE>
 *
 * <P ALIGN="justify">A continuous coverage has a domain that consists of a set of direct positions in a coordinate space.
 * A continuous coverage maps direct positions to value records.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>Example:</B>
 * Consider a coverage that maps direct positions in San Diego County to their temperature at noon today. Both the
 * domain and the range may take infinitely many different values. This continuous coverage would be associated with
 * a discrete coverage that holds the temperature values observed at a set of weather stations.</P></BLOCKQUOTE>
 *
 * <P ALIGN="justify">A continuous coverage may consist of no more than a spatially bounded, but transfinite set of
 * direct positions, and a mathematical function that relates direct position to feature attribute value. This is
 * called an analytical coverage.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>Example:</B>
 * A statistical trend surface that relates land value to position relative to a city centre is an example
 * of a continuous coverage.</P></BLOCKQUOTE>
 *
 * <P ALIGN="justify">More often, the domain of a continuous coverage consists of the direct positions
 * in the union or in the convex hull of a finite collection of geometric objects; it is specified by
 * that collection. In most cases, a continuous coverage is also associated with a discrete coverage
 * that provides a set of control values to be used as a basis for evaluating the continuous coverage.
 * Evaluation of the continuous coverage at other direct positions is done by interpolating between the
 * geometry value pairs of the control set. This often depends upon additional geometric objects constructed
 * from those in the control set; these additional objects are typically of higher topological dimension than
 * the control objects. In this set of interfaces, such objects are called geometry value objects. A geometry
 * value object is a geometric object associated with a set of geometry value pairs that provide the control for
 * constructing the geometric object and for evaluating the coverage at direct positions within the geometric object.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>Example:</B>
 * Evaluation of a triangulated irregular network involves interpolation of values within a triangle composed
 * of three neighbouring point value pairs.</P></BLOCKQUOTE>
 *
 * @version ISO 19123:2004
 * @since   GeoAPI 2.0
 */
package org.opengis.coverage;
