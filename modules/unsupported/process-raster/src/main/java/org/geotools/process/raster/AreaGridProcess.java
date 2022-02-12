/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.process.raster;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * A process that build a regular cell grid where each pixel represents its effective area in the
 * envelope in square meters.
 *
 * <p>Internally the process uses a reprojection to EckertIV to ensure proper area computation.
 * Current limitations:
 *
 * <ul>
 *   <li>won't work for very large rasters since it allocates the entire grid in memory
 *   <li>area accuracy increases as the cell size shrinks, avoid having cells that occupy sizeable
 *       chunks of the world
 * </ul>
 *
 * @author Luca Paolino - GeoSolutions
 */
@DescribeProcess(
        title = "Area Grid",
        description =
                "Computes a raster grid of given geographic extent with cell values equal to the area the cell represents on the surface of the earth.  Area is computed using the EckertIV projection.")
public class AreaGridProcess implements RasterProcess {
    private static final String targetCRSWKT =
            "PROJCS[\"World_Eckert_IV\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Eckert_IV\"],PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]";

    @DescribeResult(name = "result", description = "Output raster")
    public GridCoverage2D execute(
            @DescribeParameter(
                            name = "envelope",
                            description =
                                    "Bounding box for the computed raster, in WGS84 geographic coordinates.")
                    ReferencedEnvelope bounds,
            @DescribeParameter(
                            name = "width",
                            description = "Width of the output raster in pixels",
                            minValue = 1)
                    int width,
            @DescribeParameter(
                            name = "height",
                            description = "Height of the output raster in pixels",
                            minValue = 1)
                    int height)
            throws ProcessException {

        // basic checks
        if (height <= 0 || width <= 0) {
            throw new ProcessException("height and width parameters must be greater than 0");
        }
        if (bounds.getCoordinateReferenceSystem() == null) {
            throw new ProcessException("Envelope CRS must not be null");
        }
        // build the grid
        GeometryFactory geomFactory = new GeometryFactory();
        try {
            Polygon polygon = null;

            CoordinateReferenceSystem sourceCRS =
                    org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
            CoordinateReferenceSystem targetCRS = CRS.parseWKT(targetCRSWKT);
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
            double pX = bounds.getMinX();
            double pY = bounds.getMaxY();
            double stepX = (bounds.getMaxX() - bounds.getMinX()) / width;
            double stepY = (bounds.getMaxY() - bounds.getMinY()) / height;
            float[][] matrix = new float[height][width];
            Coordinate[] tempCoordinates = new Coordinate[5];

            // scroll through every cell (by row and then by col)
            for (int i = 0; i < height; i++) {
                // start of the row
                pX = bounds.getMinX();
                for (int j = 0; j < width; j++) {
                    double nX = pX + stepX;
                    double nY = pY - stepY;

                    if (polygon == null) {
                        tempCoordinates[0] = new Coordinate(pX, pY);
                        tempCoordinates[1] = new Coordinate(nX, pY);
                        tempCoordinates[2] = new Coordinate(nX, nY);
                        tempCoordinates[3] = new Coordinate(pX, nY);
                        tempCoordinates[4] = tempCoordinates[0];
                        LinearRing linearRing = geomFactory.createLinearRing(tempCoordinates);
                        polygon = geomFactory.createPolygon(linearRing, null);
                    } else {
                        tempCoordinates[0].x = pX;
                        tempCoordinates[0].y = pY;
                        tempCoordinates[1].x = nX;
                        tempCoordinates[1].y = pY;
                        tempCoordinates[2].x = nX;
                        tempCoordinates[2].y = nY;
                        tempCoordinates[3].x = pX;
                        tempCoordinates[3].y = nY;
                        polygon.geometryChanged();
                    }

                    // transform to EckertIV and compute area
                    Geometry targetGeometry = JTS.transform(polygon, transform);
                    matrix[i][j] = (float) targetGeometry.getArea();
                    // move on
                    pX = pX + stepX;
                }
                // move to next row
                pY = pY - stepY;
            }

            // build the grid coverage
            GridCoverageFactory coverageFactory = new GridCoverageFactory();
            GridCoverage2D grid = coverageFactory.create("AreaGridCoverage", matrix, bounds);
            return grid;

        } catch (org.opengis.referencing.FactoryException ef) {
            throw new ProcessException("Unable to create the target CRS", ef);
        } catch (org.opengis.referencing.operation.TransformException et) {
            throw new ProcessException("Unable to tranform the coordinate system", et);
        }
    }
}
