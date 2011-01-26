package org.geotools.data.dxf.entities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import java.io.EOFException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFArc extends DXFEntity {

    private static final Log log = LogFactory.getLog(DXFArc.class);
    public DXFPoint _point = new DXFPoint();
    public double _radius = 0;
    protected double _angle1 = 0;
    protected double _angle2 = 0;

    public DXFArc(DXFArc newArc) {
        this(newArc.getAngle1(), newArc.getAngle2(), newArc._point, newArc._radius, newArc.getLineType(), newArc.getColor(), newArc.getRefLayer(), 0, newArc.getThickness());

        setType(newArc.getType());
        setStartingLineNumber(newArc.getStartingLineNumber());
        setUnivers(newArc.getUnivers());
    }

    public DXFArc(double a1, double a2, DXFPoint p, double r, DXFLineType lineType, int c, DXFLayer l, int visibility, double thickness) {
        super(c, l, visibility, lineType, thickness);
        _point = p;
        _radius = r;
        _angle1 = a1;
        _angle2 = a2;
        setThickness(thickness);
        setName("DXFArc");
    }

    public double getAngle1() {
        return _angle1;
    }

    public double getAngle2() {
        return _angle2;
    }

    public static DXFArc read(DXFLineNumberReader br, DXFUnivers univers) throws NumberFormatException, IOException {
        double x = 0, y = 0, r = 0, a1 = 0, a2 = 0, thickness = 0;
        int visibility = 0, c = 0;
        DXFLineType lineType = null;
        DXFLayer l = null;

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
                case LAYER_NAME: //"8"
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case COLOR: //"62"
                    c = cvp.getShortValue();
                    break;
                case LINETYPE_NAME: //"6"
                    lineType = univers.findLType(cvp.getStringValue());
                    break;
                case DOUBLE_1: //"40"
                    r = cvp.getDoubleValue();
                    break;
                case X_1: //"10"
                    x = cvp.getDoubleValue();
                    break;
                case Y_1: //"20"
                    y = cvp.getDoubleValue();
                    break;
                case ANGLE_1: //"50"
                    a1 = cvp.getDoubleValue();
                    break;
                case ANGLE_2: //"51"
                    a2 = cvp.getDoubleValue();
                    break;
                case VISIBILITY: //"60"
                    visibility = cvp.getShortValue();
                    break;
                case THICKNESS:
                    thickness = cvp.getDoubleValue();
                    break;
                default:
                    break;
            }

        }
        DXFArc e = new DXFArc(a1, a2, new DXFPoint(x, y, c, null, visibility, 1), r, lineType, c, l, visibility, thickness);
        e.setType(GeometryType.LINE);
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);
        log.debug(e.toString(c, r, x, y, a1, a2, visibility, thickness));
        log.debug(">>Exit at line: " + br.getLineNumber());
        return e;
    }

    public Coordinate[] toCoordinateArray() {
        if (_point == null || _point._point == null || _radius <= 0 || _angle1 == _angle2) {
            addError("coordinate array can not be created.");
            return null;
        }
        List<Coordinate> lc = new ArrayList<Coordinate>();
        double startAngle = _angle1 * Math.PI / 180.0;
        double endAngle = _angle2 * Math.PI / 180.0;
        double segAngle = 2 * Math.PI / _radius;

        if(_radius < DXFUnivers.NUM_OF_SEGMENTS){
            segAngle = DXFUnivers.MIN_ANGLE;
        }

        double angle = startAngle;
        for (;;) {
            double x = _point.X() + _radius * Math.cos(angle);
            double y = _point.Y() + _radius * Math.sin(angle);

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
        return rotateAndPlace(lc.toArray(new Coordinate[]{}));
    }

    @Override
    public Geometry getGeometry() {
        if (geometry == null) {
            updateGeometry();
        }
        return super.getGeometry();
    }

    public void updateGeometry() {
        Coordinate[] ca = toCoordinateArray();
        if (ca != null && ca.length > 1) {
            geometry = getUnivers().getGeometryFactory().createLineString(ca);
        } else {
            addError("coordinate array faulty, size: " + (ca == null ? 0 : ca.length));
        }
    }

    public String toString(int c,
            double r,
            double x,
            double y,
            double a1,
            double a2,
            int visibility,
            double thickness) {
        StringBuffer s = new StringBuffer();
        s.append("DXFArc [");
        s.append("color: ");
        s.append(c + ", ");
        s.append("r: ");
        s.append(r + ", ");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("a1: ");
        s.append(a1 + ", ");
        s.append("a2: ");
        s.append(a2 + ", ");
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
        return new DXFArc(this);
    }
}
