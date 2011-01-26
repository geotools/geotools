package org.geotools.data.dxf.entities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.awt.geom.Point2D;
import java.io.EOFException;
import java.io.IOException;

import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFTables;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFPoint extends DXFEntity {

    private static final Log log = LogFactory.getLog(DXFPoint.class);
    public Point2D.Double _point = new Point2D.Double(0, 0);

    public DXFPoint(DXFPoint newPoint) {
        this(newPoint._point.x, newPoint._point.y, newPoint.getColor(), newPoint.getRefLayer(), 0, newPoint.getThickness());

        setType(newPoint.getType());
        setStartingLineNumber(newPoint.getStartingLineNumber());
        setUnivers(newPoint.getUnivers());
    }

    public DXFPoint(Point2D.Double p, int c, DXFLayer l, int visibility, float thickness) {
        super(c, l, visibility, null, thickness);
        if (p == null) {
            p = new Point2D.Double(0, 0);
        }
        _point = p;
        setName("DXFPoint");
    }

    public DXFPoint(Point2D.Double p) {
        super(-1, null, 0, null, DXFTables.defaultThickness);
        if (p == null) {
            p = new Point2D.Double(0, 0);
        }
        _point = p;
        setName("DXFPoint");
    }

    public DXFPoint() {
        super(-1, null, 0, null, DXFTables.defaultThickness);
        setName("DXFPoint");
    }

    public DXFPoint(double x, double y, int c, DXFLayer l, int visibility, double thickness) {
        super(c, l, visibility, null, DXFTables.defaultThickness);
        _point = new Point2D.Double(x, y);
        setName("DXFPoint");
    }

    public void setX(double x) {
        _point.x = x;
    }

    public void setY(double y) {
        _point.y = y;
    }

    public double X() {
        return _point.getX();
    }

    public double Y() {
        return _point.getY();
    }

    public static DXFPoint read(DXFLineNumberReader br, DXFUnivers univers) throws NumberFormatException, IOException {
        DXFLayer l = null;
        int visibility = 0, c = -1;
        double x = 0, y = 0, thickness = 0;

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
                case X_1: //"10"
                    x = cvp.getDoubleValue();
                    break;
                case Y_1: //"20"
                    y = cvp.getDoubleValue();
                    break;
                case COLOR: //"62"
                    c = cvp.getShortValue();
                    break;
                case VISIBILITY: //"60"
                    visibility = cvp.getShortValue();
                    break;
                case THICKNESS: //"39"
                    thickness = cvp.getDoubleValue();
                    break;
                default:
                    break;
            }
        }

        DXFPoint e = new DXFPoint(x, y, c, l, visibility, thickness);
        e.setType(GeometryType.POINT);
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);
        log.debug(e.toString(x, y, visibility, c, thickness));
        log.debug(">>Exit at line: " + br.getLineNumber());
        return e;
    }

    @Override
    public Geometry getGeometry() {
        if (geometry == null) {
           updateGeometry();
        }
        return super.getGeometry();
    }

    @Override
    public void updateGeometry(){
         geometry = getUnivers().getGeometryFactory().createPoint(toCoordinate());
    }


    public Coordinate toCoordinate() {
        if (_point == null) {
            addError("coordinate can not be created.");
            return null;
        }

        return rotateAndPlace(new Coordinate(_point.getX(), _point.getY()));
    }

    public String toString(double x, double y, int visibility, int c, double thickness) {
        StringBuffer s = new StringBuffer();
        s.append("DXFPoint [");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("visibility: ");
        s.append(visibility + ", ");
        s.append("color: ");
        s.append(c + ", ");
        s.append("thickness: ");
        s.append(thickness);
        s.append("]");
        return s.toString();
    }

    @Override
    public DXFEntity translate(double x, double y) {
        _point.x += x;
        _point.y += y;

        return this;
    }

    @Override
    public DXFEntity clone() {
        return new DXFPoint(this);
    }
}