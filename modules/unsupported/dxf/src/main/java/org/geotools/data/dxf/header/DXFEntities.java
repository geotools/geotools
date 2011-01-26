package org.geotools.data.dxf.header;

import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;

import org.geotools.data.dxf.entities.DXFArc;
import org.geotools.data.dxf.entities.DXFCircle;
import org.geotools.data.dxf.entities.DXFDimension;
import org.geotools.data.dxf.entities.DXFEllipse;
import org.geotools.data.dxf.entities.DXFEntity;
import org.geotools.data.dxf.entities.DXFInsert;
import org.geotools.data.dxf.entities.DXFLine;
import org.geotools.data.dxf.entities.DXFLwPolyline;
import org.geotools.data.dxf.entities.DXFPoint;
import org.geotools.data.dxf.entities.DXFPolyline;
import org.geotools.data.dxf.entities.DXFSolid;
import org.geotools.data.dxf.entities.DXFSpLine;
import org.geotools.data.dxf.entities.DXFText;
import org.geotools.data.dxf.entities.DXFTrace;
import org.geotools.data.dxf.parser.DXFParseException;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFConstants;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFEntities implements DXFConstants {

    private static final Log log = LogFactory.getLog(DXFEntities.class);
    public Vector<DXFEntity> theEntities = new Vector<DXFEntity>();

    public DXFEntities() {
    }

    public DXFEntities(Vector<DXFEntity> sEntities) {
        if (sEntities == null) {
            sEntities = new Vector<DXFEntity>();
        }
        this.theEntities = sEntities;
    }

    public static DXFEntities readEntities(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        Vector<DXFEntity> sEnt = new Vector<DXFEntity>();

        DXFCodeValuePair cvp = null;
        DXFGroupCode gc = null;

        int sln = br.getLineNumber();
        log.debug(">Enter at line: " + sln);
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
                    DXFEntity dxfe = null;
                    String type = cvp.getStringValue();
                    if (type.equals(ENDSEC) || type.equals(ENDBLK)) {
                        doLoop = false;
                        break;
                    } else if (type.equals(LINE)) {
                        dxfe = DXFLine.read(br, univers);
                    } else if (type.equals(ARC)) {
                        dxfe = DXFArc.read(br, univers);
                    } else if (type.equals(CIRCLE)) {
                        dxfe = DXFCircle.read(br, univers);
                    } else if (type.equals(POLYLINE)) {
                             dxfe = DXFPolyline.read(br, univers);
                    } else if (type.equals(LWPOLYLINE)) {
                               dxfe = DXFLwPolyline.read(br, univers);
                    } else if (type.equals(POINT)) {
                        dxfe = DXFPoint.read(br, univers);
                    } else if (type.equals(SOLID)) {
                        dxfe = DXFSolid.read(br, univers);
                    } else if (type.equals(TEXT)) {
                        dxfe = DXFText.read(br, univers);
                    } else if (type.equals(MTEXT)) {
                        dxfe = DXFText.read(br, univers);
                    } else if (type.equals(INSERT)) {
                        dxfe = DXFInsert.read(br, univers);
                    } else if (type.equals(DIMENSION)) {
                        dxfe = DXFDimension.read(br, univers);
                    } else if (type.equals(TRACE)) {
                        dxfe = DXFTrace.read(br, univers);
                    } else if (type.equals(ELLIPSE)) {
                        dxfe = DXFEllipse.read(br, univers);
                    } else if (type.equals(SPLINE)) {
                        dxfe = DXFSpLine.read(br, univers);
                    }
                    if (dxfe != null) {
                        sEnt.add(dxfe);
                    }
                    break;
                default:
                    break;
            }

        }
        DXFEntities e = new DXFEntities(sEnt);
        log.debug(e.toString(sEnt.size()));
        log.debug(">Exit at line: " + br.getLineNumber());
        return e;
    }

    public String toString(int numEntities) {
        StringBuffer s = new StringBuffer();
        s.append("DXFEntities [");
        s.append("numEntities: ");
        s.append(numEntities);
        s.append("]");
        return s.toString();
    }
}
