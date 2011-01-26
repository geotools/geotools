package org.geotools.data.dxf.entities;

import java.io.EOFException;
import java.util.Vector;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.io.IOException;

import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFTables;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFSpLine extends DXFPolyline {

    private static final Log log = LogFactory.getLog(DXFSpLine.class);
    public DXFPoint _a = new DXFPoint();
    public DXFPoint _b = new DXFPoint();

    public DXFSpLine(DXFPolyline newPolyLine) {
        super(newPolyLine);
        setName("DXFSpLine");
    }

    public DXFSpLine(String name, int flag, int c, DXFLayer l, Vector<DXFVertex> v, int visibility, DXFLineType lineType, double thickness) {
        super(name, flag, c, l, v, visibility, lineType, thickness);
        setName("DXFSpLine");
    }

    public static DXFSpLine read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        DXFLayer l = null;
        int flag = 0, visibility = 0, c = -1;
        String name = "";
        DXFLineType lineType = null;
        Vector<DXFVertex> lv = new Vector<DXFVertex>();

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

            // Unsupported GroupCodes:
            // HANDLE, SOFT_POINTER_HANDLE_1, SUBCLASS_DATA_MARKER, INT_1, INT_2, INT_3, INT_4, INT_5, DOUBLE_3, DOUBLE_4, DOUBLE_1, Z_1

            switch (gc) {
                case TYPE:
                    // geldt voor alle waarden van type
                    br.reset();
                    doLoop = false;
                    break;
                case LAYER_NAME: //"8"
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case X_1:
                    double x = cvp.getDoubleValue();
                    lv.add(new DXFVertex(x, -1, 1, c, l, visibility));
                case Y_1:
                    int lastIndex = lv.size() - 1;
                    DXFVertex lastCoord = lv.get(lastIndex);
                    lastCoord.setY(cvp.getDoubleValue());
                    lv.set(lastIndex, lastCoord);
                    break;
                default:
                    break;
            }
        }

        DXFSpLine e = new DXFSpLine(name, 4, c, l, lv, visibility, lineType, DXFTables.defaultThickness);
        e.setType(GeometryType.LINE);
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);

        log.debug(e.toString(lv.get(0).X(), lv.get(0).Y(), lv.get(lv.size() - 1).X(), lv.get(lv.size() - 1).Y(), c, visibility));
        log.debug(">Exit at line: " + br.getLineNumber());
        return e;
    }

    public String toString(double x1, double y1, double x2, double y2, int c, int visibility) {
        StringBuffer s = new StringBuffer();
        s.append("DXFSpLine [");
        s.append("start x: ");
        s.append(x1 + ", ");
        s.append("start y: ");
        s.append(y1 + ", ");
        s.append("end x: ");
        s.append(x2 + ", ");
        s.append("end y: ");
        s.append(y2 + ", ");
        s.append("color: ");
        s.append(c + ", ");
        s.append("visibility: ");
        s.append(visibility);
        s.append("]");
        return s.toString();
    }
}
