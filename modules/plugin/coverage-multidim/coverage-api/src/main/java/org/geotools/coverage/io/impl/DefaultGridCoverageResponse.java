/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.impl;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.io.GridCoverageResponse;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.opengis.coverage.CannotEvaluateException;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.Record;
import org.opengis.util.RecordType;

/**
 * Default GridCoverageResponse implementation.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class DefaultGridCoverageResponse implements GridCoverageResponse {

    public DefaultGridCoverageResponse(
            GridCoverage gridCoverage,
            DateRange temporalExtent,
            NumberRange<Double> verticalExtent) {
        this.temporalExtent = temporalExtent;
        this.verticalExtent = verticalExtent;
        this.gridCoverage = gridCoverage;
    }

    private GridCoverage gridCoverage;

    private DateRange temporalExtent;

    private NumberRange<Double> verticalExtent;

    @Override
    public DateRange getTemporalExtent() {
        return temporalExtent;
    }

    @Override
    public NumberRange<Double> getVerticalExtent() {
        return verticalExtent;
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return gridCoverage.getCoordinateReferenceSystem();
    }

    @Override
    public Envelope getEnvelope() {
        return gridCoverage.getEnvelope();
    }

    @Override
    public RecordType getRangeType() {
        return gridCoverage.getRangeType();
    }

    @Override
    public Set<Record> evaluate(DirectPosition p, Collection<String> list)
            throws PointOutsideCoverageException, CannotEvaluateException {
        return gridCoverage.evaluate(p, list);
    }

    @Override
    public Object evaluate(DirectPosition point)
            throws PointOutsideCoverageException, CannotEvaluateException {
        return gridCoverage.evaluate(point);
    }

    @Override
    public boolean[] evaluate(DirectPosition point, boolean[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException,
                    ArrayIndexOutOfBoundsException {
        return gridCoverage.evaluate(point, destination);
    }

    @Override
    public byte[] evaluate(DirectPosition point, byte[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException,
                    ArrayIndexOutOfBoundsException {
        return gridCoverage.evaluate(point, destination);
    }

    @Override
    public int[] evaluate(DirectPosition point, int[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException,
                    ArrayIndexOutOfBoundsException {
        return gridCoverage.evaluate(point, destination);
    }

    @Override
    public float[] evaluate(DirectPosition point, float[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException,
                    ArrayIndexOutOfBoundsException {
        return gridCoverage.evaluate(point, destination);
    }

    @Override
    public double[] evaluate(DirectPosition point, double[] destination)
            throws PointOutsideCoverageException, CannotEvaluateException,
                    ArrayIndexOutOfBoundsException {
        return gridCoverage.evaluate(point, destination);
    }

    @Override
    public int getNumSampleDimensions() {
        return gridCoverage.getNumSampleDimensions();
    }

    @Override
    public SampleDimension getSampleDimension(int index) throws IndexOutOfBoundsException {
        return gridCoverage.getSampleDimension(index);
    }

    @Override
    public RenderableImage getRenderableImage(int xAxis, int yAxis)
            throws UnsupportedOperationException, IndexOutOfBoundsException {
        return gridCoverage.getRenderableImage(xAxis, yAxis);
    }

    @Override
    public boolean isDataEditable() {
        return gridCoverage.isDataEditable();
    }

    @Override
    public GridGeometry getGridGeometry() {
        return gridCoverage.getGridGeometry();
    }

    @Override
    public int[] getOptimalDataBlockSizes() {
        return gridCoverage.getOptimalDataBlockSizes();
    }

    @Override
    public int getNumOverviews() {
        return gridCoverage.getNumOverviews();
    }

    @Override
    public GridGeometry getOverviewGridGeometry(int index) throws IndexOutOfBoundsException {
        return gridCoverage.getOverviewGridGeometry(index);
    }

    @Override
    public GridCoverage getOverview(int index) throws IndexOutOfBoundsException {
        return gridCoverage.getOverview(index);
    }

    @Override
    public List<GridCoverage> getSources() {
        return gridCoverage.getSources();
    }

    @Override
    public RenderedImage getRenderedImage() {
        return gridCoverage.getRenderedImage();
    }

    @Override
    public GridCoverage2D getGridCoverage2D() {
        return (GridCoverage2D) gridCoverage;
    }
}
