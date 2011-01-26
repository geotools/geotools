package org.geotools.data.dxf.entities;

import java.io.EOFException;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.io.IOException;
import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFBlock;
import org.geotools.data.dxf.header.DXFBlockReference;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFInsert extends DXFBlockReference {

    private static final Log log = LogFactory.getLog(DXFInsert.class);
    public DXFPoint _point = new DXFPoint();
    public double _angle = 0.0;

    public DXFInsert(DXFInsert newInsert) {
        this(newInsert._point._point.x, newInsert._point._point.y, newInsert._blockName, newInsert._refBlock, newInsert.getRefLayer(), 0, newInsert.getColor(), newInsert.getLineType(), newInsert._angle);

        setType(newInsert.getType());
        setStartingLineNumber(newInsert.getStartingLineNumber());
        setUnivers(newInsert.getUnivers());
    }

    public DXFInsert(double x, double y, String nomBlock, DXFBlock refBlock, DXFLayer l, int visibility, int c, DXFLineType lineType, double angle) {
        super(c, l, visibility, lineType, nomBlock, refBlock);
        _point = new DXFPoint(x, y, c, null, visibility, 1);
        _angle = angle;
        setName("DXFInsert");
    }

    public static DXFInsert read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        String nomBlock = "";
        DXFInsert m = null;
        DXFLayer l = null;
        double x = 0, y = 0;
        int visibility = 0, c = -1;
        DXFBlock refBlock = null;
        DXFLineType lineType = null;
        double angle = 0.0;

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

                    br.reset();
                    doLoop = false;
                    break;
                case LAYER_NAME: //"8"
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case NAME: //"2"
                    nomBlock = cvp.getStringValue();
                    break;
                case X_1: //"10"
                    x = cvp.getDoubleValue();
                    break;
                case Y_1: //"20"
                    y = cvp.getDoubleValue();
                    break;
                case ANGLE_1: //"20"
                    angle = cvp.getDoubleValue();
                    break;
                case COLOR: //"62"
                    c = cvp.getShortValue();
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

        m = new DXFInsert(x, y, nomBlock, refBlock, l, visibility, c, lineType, angle);
        m.setType(GeometryType.POINT);
        m.setStartingLineNumber(sln);
        m.setUnivers(univers);

        univers.addRefBlockForUpdate(m);

        log.debug(m.toString(x, y, visibility, c, lineType));
        log.debug(">>Exit at line: " + br.getLineNumber());

        return m;
    }

    public String toString(double x, double y, int visibility, int c, DXFLineType lineType) {
        StringBuffer s = new StringBuffer();
        s.append("DXFInsert [");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("visibility: ");
        s.append(visibility + ", ");
        s.append("color: ");
        s.append(c + ", ");
        s.append("line type: ");
        if (lineType != null) {
            s.append(lineType._name);
        }
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
        return new DXFInsert(this);
    }
}
