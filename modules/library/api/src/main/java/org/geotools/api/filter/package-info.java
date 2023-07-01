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
 * Filters {@linkplain org.geotools.api.feature.Feature features} according their properties.
 * A <CITE>filter expression</CITE> is a construct used to constraints the property
 * values of an object type for the purpose of identifying a subset of object instances
 * to be operated upon in some manner.
 * <p>
 * The following is adapted from the <a href="http://www.opengeospatial.org/standards/filter">OpenGIS&reg;
 * Filter Encoding Implementation Specification</a>:
 * <ul>
 * <li><a href="http://portal.opengeospatial.org/files/?artifact_id=1171">02-059 1.0 Filter Encoding</a>
 * <li><a href="http://portal.opengeospatial.org/files/?artifact_id=1171">04-095 1.1 Filter Encoding Implementation Specification</a>
 * </ul>
 *
 * <H3>Comparison operators</H3>
 * <P ALIGN="justify">A comparison operator is used to form expressions that evaluate
 * the mathematical comparison between two arguments. If the arguments satisfy the comparison
 * then the expression {@linkplain org.geotools.api.filter.Filter#evaluate evaluates} to {@code true}.
 * Otherwise the expression evaluates to {@code false}.</P>
 *
 * <P ALIGN="justify">In addition to the standard set
 * ({@code =},{@code <},{@code >},{@code >=},{@code <=},{@code <>}) of comparison operators,
 * this package defines the elements {@link org.geotools.api.filter.PropertyIsLike},
 * {@link org.geotools.api.filter.PropertyIsBetween} and {@link org.geotools.api.filter.PropertyIsNull}.</P>
 *
 * <H3>Logical operators</H3>
 * <P ALIGN="justify">A logical operator can be used to combine one or more conditional expressions.
 * The logical operator {@link org.geotools.api.filter.And} evaluates to {@code true} if all the combined
 * expressions evaluate to {@code true}. The operator {@link org.geotools.api.filter.Or} operator evaluates
 * to {@code true} is any of the combined expressions evaluate to {@code true}. The
 * {@link org.geotools.api.filter.Not} operator reverses the logical value of an expression.
 * The elements {@code And}, {@code Or} and {@code Not} can be used to combine scalar,
 * spatial and other logical expressions to form more complex compound expressions.</P>
 *
 * <H3>Identity</H3>
 * <P ALIGN="justify">Identity can be checked using {@link org.geotools.api.filter.Id}, selected objects
 * will are matched against a set of {@link org.geotools.api.filter.identiy.Identifier}.</p>
 */
package org.geotools.api.filter;

// Annotations
