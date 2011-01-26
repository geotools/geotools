package org.geotools.data.dxf.entities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.awt.geom.Rectangle2D;
import java.io.EOFException;
import java.io.IOException;

import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.header.DXFTables;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFText extends DXFEntity {

    private static final Log log = LogFactory.getLog(DXFText.class);
    public DXFPoint _point = new DXFPoint(); // 10 ,20
    public String _value = ""; // 1
    public double _height = 0; // 40
    public double _rotation = 0; // 50
    public int _align = 0; // 72
    public String _style = ""; // 7
    public double _angle = 0; // 51
    public double _zoomfactor = 1; // 41
    public Rectangle2D.Double _r = new Rectangle2D.Double();

    public DXFText(DXFText newText) {
        this(newText._point._point.x, newText._point._point.y, newText._value, newText._rotation, newText.getThickness(), newText._height,
                newText._align, newText._style, newText.getColor(), newText.getRefLayer(), newText._angle, newText._zoomfactor, 0, newText.getLineType());

        setType(newText.getType());
        setStartingLineNumber(newText.getStartingLineNumber());
        setUnivers(newText.getUnivers());

        // Hack voor label
        setKey(newText._value);
    }

    public DXFText(double x, double y, String value, double rotation, double thickness, double height, int align, String style, int c, DXFLayer l, double angle, double zoomFactor, int visibility, DXFLineType lineType) {
        super(c, l, visibility, lineType, thickness);
        _point = new DXFPoint(x, y, c, l, visibility, thickness);
        _value = value;
        _rotation = rotation;
        _height = height;
        _align = align;
        _style = style;
        _angle = angle;
        _zoomfactor = zoomFactor;
        setName("DXFText");
    }

    public static DXFText read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        DXFLayer l = null;
        String value = "", style = "STANDARD";
        int visibility = 0, align = 0, c = -1;
        DXFLineType lineType = null;
        double x = 0,
                y = 0,
                angle = 0,
                rotation = 0,
                zoomfactor = 1,
                thickness = DXFTables.defaultThickness,
                height = 0;

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
                case X_1: //"10"
                    x = cvp.getDoubleValue();
                    break;
                case Y_1: //"20"
                    y = cvp.getDoubleValue();
                    break;
                case TEXT: //"1"
                    value = cvp.getStringValue();
                    break;
                case ANGLE_1: //"50"
                    rotation = cvp.getDoubleValue();
                    break;
                case THICKNESS: //"39"
                    thickness = cvp.getDoubleValue();
                    break;
                case DOUBLE_1: //"40"
                    height = cvp.getDoubleValue();
                    break;
                case ANGLE_2: //"51"
                    angle = cvp.getDoubleValue();
                    break;
                case DOUBLE_2: //"41"
                    zoomfactor = cvp.getDoubleValue();
                    break;
                case INT_3: //"72"
                    align = cvp.getShortValue();
                    break;
                case LAYER_NAME: //"8"
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case COLOR: //"62"
                    c = cvp.getShortValue();
                    break;
                case TEXT_STYLE_NAME: //"7"
                    style = cvp.getStringValue();
                    break;
                case VISIBILITY: //"60"
                    visibility = cvp.getShortValue();
                    break;
                case LINETYPE_NAME: //"6"
                    lineType = univers.findLType(cvp.getStringValue());
                    break;
                default:
                    break;
            }

        }

        DXFText e = new DXFText(x, y, value, rotation, thickness, height, align, style, c, l, angle, zoomfactor, visibility, lineType);
        e.setType(GeometryType.POINT);
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);
        // Hack voor label
        e.setKey(value);
        log.debug(e.toString(x, y, value, rotation, thickness, height, align, style, c, angle, zoomfactor, visibility));
        log.debug(">Exit at line: " + br.getLineNumber());
        return e;
    }

    @Override
    public Geometry getGeometry() {
        if (geometry == null) {
        }
        return super.getGeometry();
    }

    public void updateGeometry() {
        geometry = getUnivers().getGeometryFactory().createPoint(toCoordinate());
    }

    public Coordinate toCoordinate() {
        if (_point == null || _point._point == null) {
            addError("coordinate can not be created.");
            return null;
        }
        return rotateAndPlace(new Coordinate(_point._point.getX(), _point._point.getY()));
    }

    public String toString(double x, double y, String value, double rotation, double thickness, double height, double align, String style, int c, double angle, double zoomfactor, int visibility) {
        StringBuffer s = new StringBuffer();
        s.append("DXFText [");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("value: ");
        s.append(value + ", ");
        s.append("rotation: ");
        s.append(rotation + ", ");
        s.append("thickness: ");
        s.append(thickness + ", ");
        s.append("height: ");
        s.append(height + ", ");
        s.append("align: ");
        s.append(align + ", ");
        s.append("style: ");
        s.append(style + ", ");
        s.append("color: ");
        s.append(c + ", ");
        s.append("angle: ");
        s.append(angle + ", ");
        s.append("zoomfactor: ");
        s.append(zoomfactor + ", ");
        s.append("visibility: ");
        s.append(visibility);
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
        return new DXFText(this);
    }
}
