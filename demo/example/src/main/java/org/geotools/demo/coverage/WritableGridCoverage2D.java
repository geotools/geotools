package org.geotools.demo.coverage;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.util.ArrayList;
import java.util.List;
import javax.media.jai.TiledImage;
import javax.media.jai.iterator.RandomIterFactory;
import javax.media.jai.iterator.WritableRandomIter;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.coverage.CannotEvaluateException;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Michael Bedward
 */
/**
 * @author Michael Bedward
 */
public class WritableGridCoverage2D extends GridCoverage2D {

    private GridCoverage2D wrapped;
    private static final int MAX_PENDING_VALUES = 10000;

    private static class PendingValue {
        Object pos;
        boolean isGeographic;
        Object value;
    }

    private List<PendingValue> pendingValues = new ArrayList<PendingValue>();

    /**
     * Constructor: wraps the given grid
     *
     * @param grid the original grid
     */
    public WritableGridCoverage2D(GridCoverage2D grid) {
        super(grid.getName().toString(), grid);
        this.wrapped = grid;
    }

    @Override
    public Object evaluate(DirectPosition point) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(point);
    }

    @Override
    public byte[] evaluate(DirectPosition coord, byte[] dest) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public int[] evaluate(DirectPosition coord, int[] dest) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public float[] evaluate(DirectPosition coord, float[] dest) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public double[] evaluate(DirectPosition coord, double[] dest) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public int[] evaluate(Point2D coord, int[] dest) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public float[] evaluate(Point2D coord, float[] dest) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public double[] evaluate(Point2D coord, double[] dest) throws CannotEvaluateException {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public int[] evaluate(GridCoordinates2D coord, int[] dest) {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public float[] evaluate(GridCoordinates2D coord, float[] dest) {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    @Override
    public double[] evaluate(GridCoordinates2D coord, double[] dest) {
        flushCache(true);
        return super.evaluate(coord, dest);
    }

    /**
     * @deprecated
     */
    @Override
    public GridCoverage2D geophysics(boolean geo) {
        flushCache(true);
        return super.geophysics(geo);
    }

    @Override
    public RenderableImage getRenderableImage(int xAxis, int yAxis) {
        flushCache(true);
        return super.getRenderableImage(xAxis, yAxis);
    }

    @Override
    public RenderedImage getRenderedImage() {
        flushCache(true);
        return super.getRenderedImage();
    }

    @Override
    public boolean isDataEditable() {
        return true;
    }

    @Override
    public void prefetch(Rectangle2D area) {
        // not implemented
    }

    @Override
    public void show(String title, int xAxis, int yAxis) {
        flushCache(true);
        super.show(title, xAxis, yAxis);
    }

    @Override
    public void show(String title) {
        flushCache(true);
        super.show(title);
    }

    @Override
    public GridCoverage2D view(ViewType type) {
        flushCache(true);
        return super.view(type);
    }

    public void setValue(DirectPosition worldPos, int value) {
        doSetWorldValue(worldPos, value);
    }

    public void setValue(DirectPosition worldPos, float value) {
        doSetWorldValue(worldPos, value);
    }

    public void setValue(DirectPosition worldPos, double value) {
        doSetWorldValue(worldPos, value);
    }

    public void setValue(GridCoordinates2D gridPos, int value) {
        doSetGridValue(gridPos, value);
    }

    public void setValue(GridCoordinates2D gridPos, float value) {
        doSetGridValue(gridPos, value);
    }

    public void setValue(GridCoordinates2D gridPos, double value) {
        doSetGridValue(gridPos, value);
    }

    private void doSetWorldValue(DirectPosition pos, Number value) {
        PendingValue pv = new PendingValue();
        pv.pos = new DirectPosition2D(pos);
        pv.isGeographic = true;
        pv.value = value;
        pendingValues.add(pv);
        flushCache(false);
    }

    private void doSetGridValue(GridCoordinates2D gridPos, Number value) {
        PendingValue pv = new PendingValue();
        pv.pos = new GridCoordinates2D(gridPos);
        pv.isGeographic = false;
        pv.value = value;
        pendingValues.add(pv);
        flushCache(false);
    }

    private void flushCache(boolean force) {
        if (pendingValues.size() >= MAX_PENDING_VALUES || (force && pendingValues.size() > 0)) {

            WritableRenderedImage writableImage = null;
            if (super.isDataEditable()) {
                writableImage = (WritableRenderedImage) image;
            } else {
                writableImage = new TiledImage(wrapped.getRenderedImage(), true);
            }
            WritableRandomIter writeIter = RandomIterFactory.createWritable(writableImage, null);
            int dataType = writableImage.getSampleModel().getDataType();

            GridCoordinates2D gridPos = null;
            for (PendingValue pv : pendingValues) {
                if (pv.isGeographic) {
                    try {
                        gridPos = wrapped.getGridGeometry().worldToGrid((DirectPosition) pv.pos);
                    } catch (TransformException ex) {
                        throw new RuntimeException("Could not transform location [" +
                                pv.pos + "] to grid coords");
                    }
                } else {
                    gridPos = (GridCoordinates2D) pv.pos;
                }

                switch (dataType) {
                    case DataBuffer.TYPE_INT:
                        writeIter.setSample(gridPos.x, gridPos.y, 0, (Integer)pv.value);
                        break;

                    case DataBuffer.TYPE_FLOAT:
                        writeIter.setSample(gridPos.x, gridPos.y, 0, (Float)pv.value);
                        break;

                    case DataBuffer.TYPE_DOUBLE:
                        writeIter.setSample(gridPos.x, gridPos.y, 0, (Double)pv.value);
                        break;
                }
            }

            pendingValues.clear();
        }
    }
}
