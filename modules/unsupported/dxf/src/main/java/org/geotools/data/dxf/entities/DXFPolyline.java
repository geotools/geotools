package org.geotools.data.dxf.entities;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

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

public class DXFPolyline extends DXFEntity {

    private static final Log log = LogFactory.getLog(DXFPolyline.class);
    public String _id;
    public int _flag = 0;
    public Vector<DXFVertex> theVertex = new Vector<DXFVertex>();

    public DXFPolyline(DXFPolyline newPolyLine) {
        super(newPolyLine.getColor(), newPolyLine.getRefLayer(), 0, newPolyLine.getLineType(), newPolyLine.getThickness());
        _id = newPolyLine._id;

        for (int i = 0; i < newPolyLine.theVertex.size(); i++) {
            theVertex.add(new DXFVertex(newPolyLine.theVertex.elementAt(i)));
        }
        _flag = newPolyLine._flag;

        setType(newPolyLine.getType());
        setStartingLineNumber(newPolyLine.getStartingLineNumber());
        setUnivers(newPolyLine.getUnivers());

        setName("DXFPolyline");
    }

    public DXFPolyline(String name, int flag, int c, DXFLayer l, Vector<DXFVertex> v, int visibility, DXFLineType lineType, double thickness) {
        super(c, l, visibility, lineType, thickness);
        _id = name;

        if (v == null) {
            v = new Vector<DXFVertex>();
        }
        theVertex = v;
        _flag = flag;
        setName("DXFPolyline");
    }

    public static DXFPolyline read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        String name = "";
        int visibility = 0, flag = 0, c = -1;
        DXFLineType lineType = null;
        Vector<DXFVertex> lv = new Vector<DXFVertex>();
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
                throw new IOException("DXF parse error " + ex.getLocalizedMessage());
            } catch (EOFException e) {
                doLoop = false;
                break;
            }

            switch (gc) {
                case TYPE:
                    String type = cvp.getStringValue();
                    if (SEQEND.equals(type)) {
                        doLoop = false;
                    } else if (VERTEX.equals(type)) {
                        lv.add(DXFVertex.read(br, univers));
                    } else {
                        br.reset();
                        doLoop = false;
                    }
                    break;
                case NAME: //"2"
                    name = cvp.getStringValue();
                    break;
                case LAYER_NAME: //"8"
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case LINETYPE_NAME: //"6"
                    lineType = univers.findLType(cvp.getStringValue());
                    break;
                case COLOR: //"62"
                    c = cvp.getShortValue();
                    break;
                case INT_1: //"70"
                    flag = cvp.getShortValue();
                    break;
                case VISIBILITY: //"60"
                    visibility = cvp.getShortValue();
                    break;
                default:
                    break;
            }
        }

        DXFPolyline e = new DXFPolyline(name, flag, c, l, lv, visibility, lineType, DXFTables.defaultThickness);
        if ((flag & 1) == 1) {
            e.setType(GeometryType.POLYGON);
        } else {
            e.setType(GeometryType.LINE);
        }
        e.setStartingLineNumber(sln);
        e.setUnivers(univers);
        log.debug(e.toString(name, flag, lv.size(), c, visibility, DXFTables.defaultThickness));
        log.debug(">>Exit at line: " + br.getLineNumber());
        return e;
    }

    public String toString(String name, int flag, int numVert, int c, int visibility, double thickness) {
        StringBuffer s = new StringBuffer();
        s.append("DXFPolyline [");
        s.append("name: ");
        s.append(name + ", ");
        s.append("flag: ");
        s.append(flag + ", ");
        s.append("numVert: ");
        s.append(numVert + ", ");
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
            if (getType() == GeometryType.POLYGON) {
                LinearRing lr = getUnivers().getGeometryFactory().createLinearRing(ca);
                geometry = getUnivers().getGeometryFactory().createPolygon(lr, null);
            } else {
                geometry = getUnivers().getGeometryFactory().createLineString(ca);
            }
        } else {
            addError("coordinate array faulty, size: " + (ca == null ? 0 : ca.length));
        }
    }

    public Coordinate[] toCoordinateArray() {
        if (theVertex == null) {
            addError("coordinate array can not be created.");
            return null;
        }
        Iterator it = theVertex.iterator();
        List<Coordinate> lc = new ArrayList<Coordinate>();
        while (it.hasNext()) {
            DXFVertex v = (DXFVertex) it.next();
            lc.add(v.toCoordinate());
        }

        if (getType() == GeometryType.POLYGON) {
            if (lc.size() >= 2) {
                Coordinate firstc = lc.get(0);
                Coordinate lastc = lc.get(lc.size() - 1);
                if (!firstc.equals2D(lastc)) {
                    lc.add(firstc);
                }
            }
        }

        /* TODO uitzoeken of lijn zichzelf snijdt, zo ja nodding
         * zie jts union:
         * Collection lineStrings = . . .
         * Geometry nodedLineStrings = (LineString) lineStrings.get(0);
         * for (int i = 1; i < lineStrings.size(); i++) {
         * nodedLineStrings = nodedLineStrings.union((LineString)lineStrings.get(i));
         * */
        return rotateAndPlace(lc.toArray(new Coordinate[]{}));
    }

    @Override
    public DXFEntity translate(double x, double y) {
        // Move all vertices
        Iterator iter = theVertex.iterator();
        while (iter.hasNext()) {
            DXFVertex vertex = (DXFVertex) iter.next();
            vertex._point.x += x;
            vertex._point.y += y;
        }

        return this;
    }

    @Override
    public DXFEntity clone() {
        return new DXFPolyline(this);
    }
}

