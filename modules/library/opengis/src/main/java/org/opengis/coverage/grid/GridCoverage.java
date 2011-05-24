/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.coverage.grid;

import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Obligation.OPTIONAL;
import static org.opengis.annotation.Specification.OGC_01004;

import java.awt.image.RenderedImage;
import java.util.List;

import org.opengis.annotation.UML;
import org.opengis.coverage.Coverage;


/**
 * Represent the basic implementation which provides access to grid coverage data.
 * A {@code GridCoverage} implementation may provide the ability to update
 * grid values.
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/coverage/grid/GridCoverage.java $
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see RenderedImage
 * @see javax.media.jai.PixelAccessor
 */
@UML(identifier="CV_GridCoverage", specification=OGC_01004)
public interface GridCoverage extends Coverage {
    /**
     * Returns {@code true} if grid data can be edited.
     *
     * @return {@code true} if grid data can be edited.
     */
    @UML(identifier="dataEditable", obligation=MANDATORY, specification=OGC_01004)
    boolean isDataEditable();

    /**
     * Information for the grid coverage geometry.
     * Grid geometry includes the valid range of grid coordinates and the georeferencing.
     *
     * @return The information for the grid coverage geometry.
     */
    @UML(identifier="gridGeometry", obligation=MANDATORY, specification=OGC_01004)
    GridGeometry getGridGeometry();

    /**
     * Optimal size to use for each dimension when accessing grid values.
     * These values together give the optimal block size to use when retrieving
     * grid coverage values.
     * For example, a client application can achieve better performance for a 2-D grid
     * coverage by reading blocks of 128 by 128 if the grid is tiled into blocks of
     * this size.
     * The sequence is ordered by dimension.
     * If the implementation does not have optimal sizes, the sequence will be {@code null}.
     *
     * @return The optimal size to use for each dimension when accessing grid values,
     *         or {@code null} if none.
     */
    @UML(identifier="optimalDataBlockSizes", obligation=OPTIONAL, specification=OGC_01004)
    int[] getOptimalDataBlockSizes();

    /**
     * Number of predetermined overviews for the grid.
     *
     * @return The number of predetermined overviews for the grid.
     */
    @UML(identifier="numOverviews", obligation=MANDATORY, specification=OGC_01004)
    int getNumOverviews();

    /**
     * Returns the grid geometry for an overview.
     *
     * @param  index Overview index for which to retrieve grid geometry. Indices start at 0.
     * @return The grid geometry for an overview.
     * @throws IndexOutOfBoundsException if {@code overviewIndex} is out of bounds.
     */
    @UML(identifier="getOverviewGridGeometry", obligation=MANDATORY, specification=OGC_01004)
    GridGeometry getOverviewGridGeometry(int index) throws IndexOutOfBoundsException;

    /**
     * Returns a pre-calculated overview for a grid coverage. The overview indices are numbered
     * from 0 to <code>{@linkplain #getNumOverviews numberOverviews}-1</code>.
     * The overviews are ordered from highest (index 0) to lowest
     * (<code>{@linkplain #getNumOverviews numberOverviews}-1</code>) resolution.
     * Overview grid coverages will have overviews which are the overviews for
     * the grid coverage with lower resolution than the overview.
     * For example, a 1 meter grid coverage with 3, 9, and 27 meter overviews
     * will be ordered as in the left side below. The 3 meter overview will have
     * 2 overviews as in the right side below:
     *
     * <blockquote><table border=0>
     * <tr>
     *   <th align="center">1 meter GC</th> <th>&nbsp;</th>
     *   <th align="center">3 meter overview</th>
     * </tr>
     * <tr>
     *   <td valign="top"><table border=0 align="center">
     *     <tr> <th>Index&nbsp;</th>      <th>&nbsp;resolution</th>  </tr>
     *     <tr> <td align="center">0</td> <td align="center"> 3</td> </tr>
     *     <tr> <td align="center">1</td> <td align="center"> 9</td> </tr>
     *     <tr> <td align="center">2</td> <td align="center">27</td> </tr>
     *   </table></td>
     *   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
     *   <td valign="top"><table border=0 align="center">
     *     <tr> <th>Index&nbsp;</th>      <th>&nbsp;resolution</th>  </tr>
     *     <tr> <td align="center">0</td> <td align="center"> 9</td> </tr>
     *     <tr> <td align="center">1</td> <td align="center">27</td> </tr>
     *   </table></td>
     * </table></blockquote>
     *
     * @param  index Index of grid coverage overview to retrieve. Indexes start at 0.
     * @return A pre-calculated overview for a grid coverage.
     * @throws IndexOutOfBoundsException if {@code overviewIndex} is out of bounds.
     */
    @UML(identifier="getOverview", obligation=MANDATORY, specification=OGC_01004)
    GridCoverage getOverview(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the sources data for a grid coverage. If the {@code GridCoverage} was
     * produced from an underlying dataset (by {@link GridCoverageReader#read read(...)}
     * for instance), this method should returns an empty list.
     *
     * If the {@code GridCoverage} was produced using
     * {link org.opengis.coverage.processing.GridCoverageProcessor} then it should return the
     * source grid coverages of the one used as input to {@code GridCoverageProcessor}.
     * In general this method is intended to return the original {@code GridCoverage}
     * on which it depends.
     *
     * This is intended to allow applications to establish what {@code GridCoverage}s
     * will be affected when others are updated, as well as to trace back to the "raw data".
     *
     * @return The sources data for a grid coverage.
     */
    List<GridCoverage> getSources();
}
