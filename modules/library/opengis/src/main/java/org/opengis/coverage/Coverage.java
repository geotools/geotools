/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage;

import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Specification.ISO_19123;
import static org.opengis.annotation.Specification.OGC_01004;

import java.awt.image.Raster;
import java.awt.image.renderable.RenderableImage;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.opengis.annotation.Extension;
import org.opengis.annotation.Specification;
import org.opengis.annotation.UML;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.Record;
import org.opengis.util.RecordType;


/**
 * A function from a spatial, temporal or spatiotemporal domain to an attribute range. A coverage
 * associates a {@linkplain DirectPosition position} within its domain to a record of values of
 * defined data types. Examples include a raster image, polygon overlay, or digital elevation matrix.
 * The essential property of coverage is to be able to generate a value for any point
 * within its domain. How coverage is represented internally is not a concern.
 *
 * For example consider the following different internal representations of coverage:<br>
 *  <UL>
 *    <li>A coverage may be represented by a set of polygons which exhaustively
 *        tile a plane (that is each point on the plane falls in precisely one polygon).
 *        The value returned by the coverage for a point is the value of an attribute of
 *        the polygon that contains the point.</li>
 *    <li>A coverage may be represented by a grid of values
 *        (a {@linkplain DiscreteGridPointCoverage Discrete Grid Point Coverage}).
 *        If the coverage is a {@linkplain ContinuousQuadrilateralGridCoverage Continuous
 *        Quadrilateral Grid Coverage} using {@linkplain InterpolationMethod#NEAREST_NEIGHBOUR
 *        Nearest Neighbour} interpolation method, then the value returned by the coverage for
 *        a point is that of the grid value whose location is nearest the point.</li>
 *    <li>Coverage may be represented by a mathematical function.
 *        The value returned by the coverage for a point is just the return value
 *        of the function when supplied the coordinates of the point as arguments.</li>
 *    <li>Coverage may be represented by combination of these.
 *        For example, coverage may be represented by a combination of mathematical
 *        functions valid over a set of polynomials.</LI>
 * </UL>
 *
 * <h3>Metadata</h3>
 * The legacy {@linkplain Specification#OGC_01004 OGC 01-004} specification provided some methods for
 * fetching metadata values attached to a coverage. The {@linkplain Specification#ISO_19123 ISO 19123}
 * specification do not provides such methods. Implementations that want to provide such metadata are
 * encouraged to implement the {@link javax.media.jai.PropertySource} or
 * {@link javax.media.jai.WritablePropertySource} interface.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/Coverage.java $
 * @version ISO 19123:2004
 * @author  Stephane Fellah
 * @author  Martin Desruisseaux (IRD)
 * @author  Wim Koolhoven
 * @author  Alexander Petkov
 * @since   GeoAPI 2.1
 */
@UML(identifier="CV_Coverage", specification=ISO_19123)
public interface Coverage {
    /**
     * Returns the coordinate reference system to which the objects in its domain are referenced.
     * This is the CRS used when accessing a coverage or grid coverage with the {@code evaluate(...)}
     * methods. This coordinate reference system is usually different than coordinate system of the
     * grid. It is the target coordinate reference system of the
     * {@link org.opengis.coverage.grid.GridGeometry#getGridToCRS gridToCRS} math transform.
     * <p>
     * Grid coverage can be accessed (re-projected) with new coordinate reference system with the
     * {@link org.opengis.coverage.processing.GridCoverageProcessor} component. In this case, a new
     * instance of a grid coverage is created.
     *
     * @return The coordinate reference system used when accessing a coverage or
     *         grid coverage with the {@code evaluate(...)} methods.
     */
    @UML(identifier="CRS", obligation=MANDATORY, specification=ISO_19123)
    CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * The bounding box for the coverage domain in {@linkplain #getCoordinateReferenceSystem
     * coordinate reference system} coordinates. For grid coverages, the grid cells are centered
     * on each grid coordinate. The envelope for a 2-D grid coverage includes the following corner
     * positions.
     *
     * <blockquote><pre>
     * (Minimum row - 0.5, Minimum column - 0.5) for the minimum coordinates
     * (Maximum row - 0.5, Maximum column - 0.5) for the maximum coordinates
     * </pre></blockquote>
     *
     * If a grid coverage does not have any associated coordinate reference system,
     * the minimum and maximum coordinate points for the envelope will be empty sequences.
     *
     * @return The bounding box for the coverage domain in coordinate system coordinates.
     *
     * @todo We need to explain the relationship with {@link #getDomainExtents}, if any.
     */
    @UML(identifier="envelope", obligation=MANDATORY, specification=OGC_01004)
    Envelope getEnvelope();

    /**
     * Describes the range of the coverage. It consists of a list of attribute name/data type pairs.
     * A simple list is the most common form of range type, but {@code RecordType} can be used
     * recursively to describe more complex structures. The range type for a specific coverage
     * shall be specified in an application schema.
     *
     * @return The coverage range.
     */
    @UML(identifier="rangeType", obligation=MANDATORY, specification=ISO_19123)
    RecordType getRangeType();

    /**
     * Returns a set of records of feature attribute values for the specified direct position. The
     * parameter {@code list} is a sequence of feature attribute names each of which identifies a
     * field of the range type. If {@code list} is null, the operation shall return a value for
     * every field of the range type. Otherwise, it shall return a value for each field included in
     * {@code list}. If the direct position passed is not in the domain of the coverage, then an
     * exception is thrown. If the input direct position falls within two or more geometric objects
     * within the domain, the operation shall return records of feature attribute values computed
     * according to the {@linkplain #getCommonPointRule common point rule}.
     * <P>
     * <B>NOTE:</B> Normally, the operation will return a single record of feature attribute values.
     *
     * @param  p The position where to evaluate.
     * @param  list The field of interest, or {@code null} for every fields.
     * @return The feature attributes.
     * @throws PointOutsideCoverageException if the point is outside the coverage domain.
     * @throws CannotEvaluateException If the point can't be evaluated for some other reason.
     */
    @UML(identifier="evaluate", obligation=MANDATORY, specification=ISO_19123)
    Set<Record> evaluate(DirectPosition p, Collection<String> list)
            throws PointOutsideCoverageException, CannotEvaluateException;

    /**
     * Return the value vector for a given point in the coverage.
     * A value for each sample dimension is included in the vector.
     * The default interpolation type used when accessing grid values for points
     * which fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @param  point Point at which to find the grid values.
     * @return The value vector for a given point in the coverage.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException If the point can't be evaluated for some other reason.
     * @see Raster#getDataElements(int, int, Object)
     */
    @UML(identifier="evaluate", obligation=MANDATORY, specification=OGC_01004)
    Object evaluate(DirectPosition point) throws PointOutsideCoverageException, CannotEvaluateException;

    /**
     * Return a sequence of boolean values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the coverage values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of boolean values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     */
    @UML(identifier="evaluateAsBoolean", obligation=MANDATORY, specification=OGC_01004)
    boolean[] evaluate(DirectPosition point, boolean[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of unsigned byte values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the coverage values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of unsigned byte values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     */
    @UML(identifier="evaluateAsByte", obligation=MANDATORY, specification=OGC_01004)
    byte[] evaluate(DirectPosition point, byte[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of integer values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the grid values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of integer values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     *
     * @see Raster#getPixel(int, int, int[])
     */
    @UML(identifier="evaluateAsInteger", obligation=MANDATORY, specification=OGC_01004)
    int[] evaluate(DirectPosition point, int[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of float values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the grid values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of float values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException if the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     *
     * @see Raster#getPixel(int, int, float[])
     */
    float[] evaluate(DirectPosition point, float[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * Return a sequence of double values for a given point in the coverage.
     * A value for each sample dimension is included in the sequence.
     * The default interpolation type used when accessing grid values for points which
     * fall between grid cells is nearest neighbor.
     * <p>
     * The coordinate reference system of the point is the same as the grid coverage coordinate
     * reference system (specified by the {@link #getCoordinateReferenceSystem} method).
     *
     * @param  point Point at which to find the grid values.
     * @param  destination An optionally preallocated array in which to store the values,
     *         or {@code null} if none.
     * @return A sequence of double values for a given point in the coverage.
     *         If {@code destination} was non-null, then it is returned.
     *         Otherwise, a new array is allocated and returned.
     * @throws PointOutsideCoverageException if the point is outside the coverage
     *         {@linkplain #getEnvelope envelope}.
     * @throws CannotEvaluateException If the point can't be evaluated for some othe reason.
     * @throws ArrayIndexOutOfBoundsException if the {@code destination} array is not null
     *         and too small to hold the output.
     *
     * @see Raster#getPixel(int, int, double[])
     */
    @UML(identifier="evaluateAsDouble", obligation=MANDATORY, specification=OGC_01004)
    double[] evaluate(DirectPosition point, double[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException, ArrayIndexOutOfBoundsException;

    /**
     * The number of sample dimensions in the coverage.
     * For grid coverages, a sample dimension is a band.
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @return The number of sample dimensions in the coverage.
     */
    @UML(identifier="numSampleDimensions", obligation=MANDATORY, specification=OGC_01004)
    int getNumSampleDimensions();

    /**
     * Retrieve sample dimension information for the coverage.
     * For a grid coverage a sample dimension is a band. The sample dimension information
     * include such things as description, data type of the value (bit, byte, integer...),
     * the no data values, minimum and maximum values and a color table if one is
     * associated with the dimension. A coverage must have at least one sample dimension.
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @param  index Index for sample dimension to retrieve. Indices are numbered 0 to
     *         (<var>{@linkplain #getNumSampleDimensions n}</var>-1).
     * @return Sample dimension information for the coverage.
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds.
     */
    @UML(identifier="getSampleDimension", obligation=MANDATORY, specification=OGC_01004)
    SampleDimension getSampleDimension(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the sources data for a coverage.
     * This is intended to allow applications to establish what {@code Coverage}s
     * will be affected when others are updated, as well as to trace back to the "raw data".
     * <p>
     * This implementation specification does not include interfaces for creating
     * collections of coverages therefore the list size will usually be one indicating
     * an adapted grid coverage, or zero indicating a raw grid coverage.
     * <p>
     * <strong>WARNING:</strong> This method is inherited from the legacy OGC 01-004
     * specification and may be deprecated in a future version. We are for more experience
     * and feedbacks on the value of this method.
     *
     * @return The list of sources data for a coverage.
     */
    @UML(identifier="getSource, numSource", obligation=MANDATORY, specification=OGC_01004)
    List<? extends Coverage> getSources();

    /**
     * Returns 2D view of this coverage as a renderable image.
     * This optional operation allows interoperability with
     * <A HREF="http://java.sun.com/products/java-media/2D/">Java2D</A>.
     * If this coverage is a {@link org.opengis.coverage.grid.GridCoverage} backed
     * by a {@link java.awt.image.RenderedImage}, the underlying image can be obtained
     * with:
     *
     * <code>getRenderableImage(0,1).{@linkplain RenderableImage#createDefaultRendering()
     * createDefaultRendering()}</code>
     *
     * @param  xAxis Dimension to use for the <var>x</var> axis.
     * @param  yAxis Dimension to use for the <var>y</var> axis.
     * @return A 2D view of this coverage as a renderable image.
     * @throws UnsupportedOperationException if this optional operation is not supported.
     * @throws IndexOutOfBoundsException if {@code xAxis} or {@code yAxis} is out of bounds.
     */
    @Extension
    RenderableImage getRenderableImage(int xAxis, int yAxis)
            throws UnsupportedOperationException, IndexOutOfBoundsException;
}
