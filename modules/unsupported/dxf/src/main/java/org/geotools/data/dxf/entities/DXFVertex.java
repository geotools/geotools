package org.geotools.data.dxf.entities;

import java.io.EOFException;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.io.IOException;

import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFVertex extends DXFPoint {

    private static final Log log = LogFactory.getLog(DXFVertex.class);
    protected double _bulge = 0;

    public DXFVertex(DXFVertex newVertex) {
        this(newVertex._point.x, newVertex._point.y, newVertex._bulge, newVertex.getColor(), newVertex.getRefLayer(), 0);

        setType(newVertex.getType());
        setStartingLineNumber(newVertex.getStartingLineNumber());
        setUnivers(newVertex.getUnivers());
    }

    public DXFVertex(double x, double y, double b, int c, DXFLayer l, int visibility) {
        super(x, y, c, l, visibility, 1);
        setName("DXFVertex");
        _bulge = b;
    }

    public static DXFVertex read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        DXFLayer l = null;
        int visibility = 0, c = -1;
        double x = 0, y = 0, b = 0;

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
                case DOUBLE_3: //"42"
                    b = cvp.getDoubleValue();
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
                default:
                    break;
            }

        }

        DXFVertex e = new DXFVertex(x, y, b, c, l, visibility);
        e.setType(GeometryType.POINT);
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);
        log.debug(e.toString(b, x, y, c, visibility));
        log.debug(">Exit at line: " + br.getLineNumber());
        return e;
    }

    public String toString(double b, double x, double y, int c, int visibility) {
        StringBuffer s = new StringBuffer();
        s.append("DXFVertex [");
        s.append("bulge: ");
        s.append(b + ", ");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("c: ");
        s.append(c + ", ");
        s.append("visibility: ");
        s.append(visibility);
        s.append("]");
        return s.toString();
    }
}
