/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid.ortholine;

import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 *
 * @author michael
 */
public class OrthoLineImpl implements OrthoLine {
    private static final double TOL = 1.0e-8;
    private static final GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory(null);

    private final LineOrientation orientation;
    private final int level;
    private final Object value;
    private final CoordinateReferenceSystem crs;
    private final Coordinate v0;
    private final Coordinate v1;

    public OrthoLineImpl(ReferencedEnvelope worldBounds, LineOrientation orientation, 
            double ordinate, int level, Object value) {
        
        this.orientation = orientation;
        this.level = level;
        this.value = value;
        this.crs = worldBounds.getCoordinateReferenceSystem();

        if (orientation == LineOrientation.HORIZONTAL) {
            v0 = new Coordinate(worldBounds.getMinX(), ordinate);
            v1 = new Coordinate(worldBounds.getMaxX(), ordinate);
        } else {
            v0 = new Coordinate(ordinate, worldBounds.getMinY());
            v1 = new Coordinate(ordinate, worldBounds.getMaxY());
        }
    }
    
    public ReferencedEnvelope getBounds() {
        return new ReferencedEnvelope(v0.x, v1.x, v0.y, v1.y, crs);
    }

    public Coordinate[] getVertices() {
        Coordinate[] vertices = new Coordinate[2];
        vertices[0] = v0;
        vertices[1] = v1;
        return vertices;
    }

    public Object getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

    public LineOrientation getOrientation() {
        return orientation;
    }

    public Geometry toGeometry() {
        return geomFactory.createLineString(new Coordinate[]{v0, v1});
    }

    public Geometry toDenseGeometry(double maxSpacing) {
        if (maxSpacing <= 0.0) {
            throw new IllegalArgumentException("maxSpacing must be a positive value");
        }
        
        return Densifier.densify(this.toGeometry(), maxSpacing);
    }

}
