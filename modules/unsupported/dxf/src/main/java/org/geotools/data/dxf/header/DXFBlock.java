package org.geotools.data.dxf.header;

import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;
import java.util.Iterator;


import org.geotools.data.dxf.parser.DXFColor;
import org.geotools.data.dxf.parser.DXFParseException;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.geotools.data.dxf.entities.DXFEntity;
import org.geotools.data.dxf.entities.DXFPoint;
import org.geotools.data.dxf.entities.DXFInsert;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFConstants;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFBlock extends DXFEntity implements DXFConstants {

    private static final Log log = LogFactory.getLog(DXFBlock.class);
    public Vector<DXFEntity> theEntities = new Vector<DXFEntity>();
    public DXFPoint _point = new DXFPoint();
    public String _name;
    public int _flag;

    public DXFBlock(DXFBlock newBlock) {
        this(newBlock._point.X(), newBlock._point.Y(), newBlock._flag, newBlock._name, null, newBlock.getColor(), newBlock.getRefLayer());

        // Copy entities
        Iterator iter = newBlock.theEntities.iterator();
        while (iter.hasNext()) {
            theEntities.add(((DXFEntity) iter).clone());
        }
    }

    public DXFBlock(double x, double y, int flag, String name, Vector<DXFEntity> ent, int c, DXFLayer l) {
        super(c, l, 0, null, DXFTables.defaultThickness);
        _point = new DXFPoint(x, y, c, l, 0, 1);
        _name = name;
        _flag = flag;

        if (ent == null) {
            ent = new Vector<DXFEntity>();
        }
        theEntities = ent;
    }

    public static DXFBlock read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        Vector<DXFEntity> sEnt = new Vector<DXFEntity>();
        String name = "";
        double x = 0, y = 0;
        int flag = 0;
        DXFLayer l = null;

        int sln = br.getLineNumber();
        log.debug(">Enter at line: " + sln);

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
                    if (type.equals(ENDBLK)) {
                        doLoop = false;
                    } else if (type.equals(ENDSEC)) {
                        // hack voor als ENDBLK ontbreekt
                        doLoop = false;
                        br.reset();
                    } else if (type.equals(BLOCK)) {
                        doLoop = false;
                        br.reset();
                    } else if (type.equals(INSERT)) {
                        DXFInsert.read(br, univers);
                    } else {
                        // check of dit entities zijn
                        br.reset();
                        sEnt.addAll(DXFEntities.readEntities(br, univers).theEntities);
                    }
                    break;
                case LAYER_NAME:
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case NAME:
                    name = cvp.getStringValue();
                    break;
                case INT_1:
                    flag = cvp.getShortValue();
                    break;
                case X_1:
                    x = cvp.getDoubleValue();
                    break;
                case Y_1:
                    y = cvp.getDoubleValue();
                    break;
                default:
                    break;
            }
        }
        
        DXFBlock e = new DXFBlock(x, y, flag, name, sEnt, DXFColor.getDefaultColorIndex(), l);
        log.debug(e.toString(x, y, flag, name, sEnt.size(), DXFColor.getDefaultColorIndex()));
        log.debug("Exit at line: " + br.getLineNumber());
        return e;
    }

    public String toString(double x, double y, int flag, String name, int numEntities, int c) {
        StringBuffer s = new StringBuffer();
        s.append("DXFBlock [");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("flag: ");
        s.append(flag + ", ");
        s.append("name: ");
        s.append(name + ", ");
        s.append("color: ");
        s.append(c + ", ");
        s.append("numEntities: ");
        s.append(numEntities);
        s.append("]");
        return s.toString();
    }

    @Override
    public DXFEntity translate(double x, double y) {
        // Move all vertices
        Iterator iter = theEntities.iterator();
        while (iter.hasNext()) {
            DXFEntity entity = (DXFEntity) iter.next();
            entity.translate(x, y);
        }
        return this;
    }

    public DXFEntity clone() {
        return new DXFBlock(this);
    }
}
