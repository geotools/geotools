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
 * Quadrilateral grid coverages.
 * The following is adapted from ISO 19123 specification.
 *
 * <P ALIGN="justify">Grid coverages employ a systematic tessellation of the domain. The principal advantage
 * of such tessellations is that they support a sequential enumeration of the elements of the domain, which
 * makes data storage and access more efficient. The tessellation may represent how the data was acquired or
 * how it was computed in a model. The domain of a grid coverage is a set of grid points, including their
 * convex hull in the case of a continuous grid coverage.</P>
 *
 * <H3>Quadrilateral grid geometry</H3>
 * <P ALIGN="justify">A grid is a network composed of two or more sets of curves in which the members of each
 * set intersect the members of the other sets in a systematic way. The curves are called grid lines; the points
 * at which they intersect are {@linkplain org.opengis.coverage.grid.GridPoint grid points}, and the interstices
 * between the grid lines are {@linkplain org.opengis.coverage.grid.GridCell grid cells}.</P>
 *
 * <P ALIGN="justify">The most common case is the one in which the curves are straight lines, and there is one set of grid
 * lines for each dimension of the grid space. In this case the {@linkplain org.opengis.coverage.grid.GridCell grid cells}
 * are parallelograms or parallelepipeds. In its own coordinate system, such a grid is a network composed of two or more
 * sets of equally spaced parallel lines in which the members of each set intersect the members of the other sets at right
 * angles. It has a set of axes equal in number to the {@linkplain org.opengis.coverage.grid.Grid#getDimension dimension of
 * the grid}. It has one set of grid lines parallel to each axis. The size of the grid is described by a sequence of integers,
 * in which each integer is a count of the number of lines parallel to one of the axes. There are
 * {@linkplain org.opengis.coverage.grid.GridPoint grid points} at all grid line intersections. The axes of the grid
 * provide a basis for defining {@linkplain org.opengis.coverage.grid.GridCoordinates grid coordinates}, which are measured
 * along the axes away from their origin, which is distinguished by having coordinate values of 0. Grid coordinates of grid
 * points are integer numbers. The axes need to be identified to support sequencing rules for associating feature attribute
 * value records to the grid points.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>NOTE:</B> The dimensions (axes) of a 2-dimensional grid are often called row and
 * column.</P></BLOCKQUOTE>
 *
 * <P ALIGN="justify">A grid may be defined in terms of an external {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem
 * coordinate reference system}. This requires additional information about the location of the grid's origin within the external
 * coordinate reference system, the orientation of the grid axes, and a measure of the spacing between the grid lines. If the
 * spacing is uniform, then there is an affine relationship between the grid and external coordinate system, and the grid
 * is called a rectified grid. If, in addition, the external coordinate reference system is related to the earth by a datum,
 * the grid is a georectified grid. The grid lines of a rectified grid need not meet at right angles; the spacing between the
 * grid lines is constant along each axis, but need not be the same on every axis. The essential point is that the transformation
 * of grid coordinates to coordinates of the external coordinate reference system is an affine transformation.</P>
 *
 * <BLOCKQUOTE><P ALIGN="justify"><B>NOTE:</B> The word rectified implies a transformation from an image space to another
 * coordinate reference system. However, grids of this form are often defined initially in an earth-based coordinate system
 * and used as a basis for collecting data from sources other than imagery.</P></BLOCKQUOTE>
 *
 * <P ALIGN="justify">A feature attribute value may be of any data type. However, evaluation of a continuous coverage
 * is usually implemented by interpolation methods that can be applied only to numbers or vectors. Other data types are
 * almost always associated with discrete coverages.</P>
 *
 * <P ALIGN="justify">When the relationship between a {@linkplain org.opengis.coverage.grid.Grid grid} and an external
 * {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference system} is not adequate to specify
 * it in terms of an origin, an orientation, and spacing in that coordinate reference system, it may still be possible to
 * transform the grid coordinates into coordinates in the coordinate reference system. This transformation need not
 * be in analytic form; it may be a table, relating the grid points to coordinates in the external coordinate reference
 * system. Such a grid is classified as a referenceable grid. If the external coordinate reference system is related to
 * the earth by a datum, the grid is a georeferenceable grid. A referenceable grid is associated with information that
 * allows the location of all points in the grid to be determined in the coordinate reference system, but the location of
 * the points is not directly available from the grid coordinates, as opposed to a rectified grid where the location of the
 * points in the coordinate reference system is derivable from the properties of the grid itself. The transformation
 * produced by the information associated with a referenceable grid will produce a grid as seen in the coordinate
 * reference system, but the grid lines of that grid need not be straight or orthogonal, and the grid cells may be of
 * different shapes and sizes.</P>
 *
 * <H3>Cell structures</H3>
 * <P ALIGN="justify">The term "grid cell" refers to two concepts: one important from the perspective of data collection
 * and portrayal, the other important from the perspective of grid coverage evaluation. The ambiguity of this term is a
 * common cause of positioning error in evaluating or portraying grid coverages.</P>
 *
 * <P ALIGN="justify">The feature attribute values associated with a {@linkplain org.opengis.coverage.grid.GridPoint grid point}
 * represent characteristics of the real world measured or observed within a small space surrounding a sample point represented
 * by the grid point. The grid lines connecting these points form a set of {@linkplain org.opengis.coverage.grid.GridCell grid cells}.
 * A common simplifying assumption is that the sample space is equally divided among the sample points, so that the sample spaces
 * are represented by a second set of cells congruent to the first but offset so that each has a grid point at its centre.
 * Evaluation of a grid coverage is based on interpolation between grid points, i.e., within a grid cell bounded by the grid
 * lines that connect the grid points that represent the sample points.</P>
 *
 * <P ALIGN="justify">In the ISO 19123 International Standard, the term <cite>grid cell</cite> refers to the cell bounded
 * by the grid lines that connect the grid points. The term <cite>sample space</cite> refers to the observed or measured
 * space surrounding a sample point. The term <cite>footprint</cite> refers to a representation of a sample space in the
 * context of some coordinate reference system.</P>
 *
 * <P ALIGN="justify">In dealing with gridded data, e.g., for processing or portrayal, it is often assumed that the size
 * and shape of the sample spaces are a simple function of the spatial distribution of the sample points, and typically
 * that the grid cells and the sample cells are congruent.</P>
 *
 * <P ALIGN="justify">In fact, the size and shape of the sample space are determined by the method used to measure or
 * calculate the attribute value. In the simplest case, the sample space is the sample point. It is often a disc, a
 * sphere, or a hypersphere surrounding the sample point. In the case of sensed data the size and shape of the sample
 * space is also a function of the sensor model and its position relative to the sample point, and may be quite complex.
 * Adjacent sample spaces may be coterminous or they may overlap or underlap.</P>
 *
 * <P ALIGN="justify">In addition to affecting the size and shape of the sample space, the measurement technique
 * affects the applicability of the observed or measured value to the sample space. It is often assumed that the
 * recorded value represents the mean value for the sample space. In fact, elements of the sample space may not
 * contribute uniformly to the result, so that it is better conceived as a weighted average where the weighting
 * is a function of position within the sample space. Interpolation methods may be designed specifically to deal
 * with characteristics of the sample space.</P>
 *
 * <P ALIGN="justify">Transformation (e.g., rectification) between grid coordinates and an external coordinate
 * reference system may distort the representation of the sample space in a way that causes interpolation errors.</P>
 *
 * @version ISO 19123:2004
 * @since   GeoAPI 2.0
 */
package org.opengis.coverage.grid;
