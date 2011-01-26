package org.geotools.data.dxf.entities;

import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.io.IOException;

import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFTrace extends DXFSolid {

    private static final Log log = LogFactory.getLog(DXFTrace.class);

    public DXFTrace(DXFTrace newTrace) {
        this(new DXFPoint(newTrace._p1._point.x, newTrace._p1._point.y, newTrace.getColor(), null, 0, newTrace.getThickness()),
                new DXFPoint(newTrace._p2._point.x, newTrace._p2._point.y, newTrace.getColor(), null, 0, newTrace.getThickness()),
                new DXFPoint(newTrace._p3._point.x, newTrace._p3._point.y, newTrace.getColor(), null, 0, newTrace.getThickness()),
                new DXFPoint(newTrace._p4._point.x, newTrace._p4._point.y, newTrace.getColor(), null, 0, newTrace.getThickness()),
                newTrace.getThickness(), newTrace.getColor(), newTrace.getRefLayer(), 0, newTrace.getLineType());

        setType(newTrace.getType());
        setStartingLineNumber(newTrace.getStartingLineNumber());
        setUnivers(newTrace.getUnivers());
    }

    public DXFTrace(DXFPoint p1, DXFPoint p2, DXFPoint p3, DXFPoint p4, double thickness, int c, DXFLayer l, int visibility, DXFLineType lineType) {
        super(p1, p2, p3, p4, thickness, c, l, visibility, lineType);
        setName("DXFTrace");
    }

    public static DXFEntity read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        int sln = br.getLineNumber();
        log.debug(">>Enter at line: " + sln);

        int visibility = 0;
        DXFSolid s = (DXFSolid) DXFSolid.read(br, univers);
        if (!s.isVisible()) {
            visibility = 1;
        }
        DXFTrace e = new DXFTrace(s._p1, s._p2, s._p3, s._p4, s.getThickness(), s.getColor(), s.getRefLayer(), visibility, s.getLineType());
        e.setType(GeometryType.UNSUPPORTED);
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);

        log.debug(e.toString());
        return e;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("DXFTrace [");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append(": ");
        s.append(", ");
        s.append("]");
        return s.toString();
    }

    @Override
    public DXFEntity translate(double x, double y) {
        _p1._point.x += x;
        _p1._point.y += y;

        _p2._point.x += x;
        _p2._point.y += y;

        _p3._point.x += x;
        _p3._point.y += y;

        _p4._point.x += x;
        _p4._point.y += y;
        
        return this;
    }
}
