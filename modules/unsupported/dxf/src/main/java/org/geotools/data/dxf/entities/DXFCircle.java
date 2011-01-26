package org.geotools.data.dxf.entities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.io.EOFException;
import java.io.IOException;


import java.util.ArrayList;
import java.util.List;
import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFCircle extends DXFEntity {

    private static final Log log = LogFactory.getLog(DXFCircle.class);
    public DXFPoint _point = new DXFPoint();
    public double _radius = 0;

    public DXFCircle(DXFCircle newCircle) {
        this(new DXFPoint(newCircle._point._point.x, newCircle._point._point.y, newCircle.getColor(), newCircle.getRefLayer(), 0, newCircle.getThickness()),
                newCircle._radius, newCircle.getLineType(), newCircle.getColor(), newCircle.getRefLayer(), 0, newCircle.getThickness());

        setType(newCircle.getType());
        setStartingLineNumber(newCircle.getStartingLineNumber());
        setUnivers(newCircle.getUnivers());
    }

    public DXFCircle(DXFPoint p, double r, DXFLineType lineType, int c, DXFLayer l, int visibility, double thickness) {
        super(c, l, visibility, lineType, thickness);
        _point = p;
        _radius = r;
        setName("DXFCircle");
    }

    public static DXFCircle read(DXFLineNumberReader br, DXFUnivers univers) throws NumberFormatException, IOException {

        int visibility = 0, c = 0;
        double x = 0, y = 0, r = 0, thickness = 1;
        DXFLayer l = null;
        DXFLineType lineType = null;

        int sln = br.getLineNumber();
        log.debug(">>Enter at line: " + sln);
        DXFCodeValuePair cvp = null;
        DXFGroupCode gc = null;

        boolean doLoop = true;
        while (doLoop) {
            cvp = new DXFCodeValuePair();
            try {
                gc = cvp.read(br);
            } catch (DXFParseException ex) {
                throw new IOException("DXF parse error" + ex.getLocalizedMessage());
            } catch (EOFException e) {
                doLoop = false;
                break;
            }

            switch (gc) {
                case TYPE:
                    String type = cvp.getStringValue();
                    // geldt voor alle waarden van type
                    br.reset();
                    doLoop = false;
                    break;
                case LINETYPE_NAME: //"6"
                    lineType = univers.findLType(cvp.getStringValue());
                    break;
                case LAYER_NAME: //"8"
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case THICKNESS: //"39"
                    thickness = cvp.getDoubleValue();
                    break;
                case VISIBILITY: //"60"
                    visibility = cvp.getShortValue();
                    break;
                case COLOR: //"62"
                    c = cvp.getShortValue();
                    break;
                case X_1: //"10"
                    x = cvp.getDoubleValue();
                    break;
                case Y_1: //"20"
                    y = cvp.getDoubleValue();
                    break;
                case DOUBLE_1: //"40"
                    r = cvp.getDoubleValue();
                    break;
                default:
                    break;
            }

        }
        DXFCircle e = new DXFCircle(new DXFPoint(x, y, c, l, visibility, 1), r, lineType, c, l, visibility, thickness);
        e.setType(GeometryType.POLYGON);
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);
        log.debug(e.toString(x, y, c, visibility, thickness));
        log.debug(">>Exit at line: " + br.getLineNumber());
        return e;
    }

    public Coordinate[] toCoordinateArray() {
        if (_point == null || _point._point == null || _radius <= 0) {
            addError("coordinate array can not be created.");
            return null;
        }
        List<Coordinate> lc = new ArrayList<Coordinate>();
        double startAngle = 0.0;
        double endAngle = 2 * Math.PI;
        double segAngle = 2 * Math.PI / _radius;

        if(_radius < DXFUnivers.NUM_OF_SEGMENTS){
            segAngle = DXFUnivers.MIN_ANGLE;
        }

        double angle = startAngle;
        for (;;) {
            double x = _point._point.getX() + _radius * Math.cos(angle);
            double y = _point._point.getY() + _radius * Math.sin(angle);
            Coordinate c = new Coordinate(x, y);
            lc.add(c);

            if (angle >= endAngle) {
                break;
            }

            angle += segAngle;
            if (angle > endAngle) {
                angle = endAngle;
            }
        }

        // Fix to avoid error while creating LinearRing
        // If first coord is not equal to last coord
        if (!lc.get(0).equals(lc.get(lc.size() - 1))) {
            // Set last coordinate == first
            lc.set(lc.size() - 1, lc.get(0));
        }


        return rotateAndPlace(lc.toArray(new Coordinate[]{}));
    }

    @Override
    public Geometry getGeometry() {
        if (geometry == null) {
            updateGeometry();
        }
        return super.getGeometry();
    }

    @Override
    public void updateGeometry() {
        Coordinate[] ca = toCoordinateArray();
        if (ca != null && ca.length > 1) {
            LinearRing lr = getUnivers().getGeometryFactory().createLinearRing(ca);
            geometry = getUnivers().getGeometryFactory().createPolygon(lr, null);
        } else {
            addError("coordinate array faulty, size: " + (ca == null ? 0 : ca.length));
        }
    }

    public String toString(double x, double y, int c, int visibility, double thickness) {
        StringBuffer s = new StringBuffer();
        s.append("DXFCircle [");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("color: ");
        s.append(c + ", ");
        s.append("visibility: ");
        s.append(visibility + ", ");
        s.append("thickness: ");
        s.append(thickness);
        s.append("]");
        return s.toString();
    }

    @Override
    public DXFEntity translate(double x, double y) {
        _point._point.x += x;
        _point._point.y += y;
        return this;
    }

    @Override
    public DXFEntity clone() {
        return new DXFCircle(this);
    }
}
