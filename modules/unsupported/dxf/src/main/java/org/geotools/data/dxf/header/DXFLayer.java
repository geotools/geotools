package org.geotools.data.dxf.header;

import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;


import org.geotools.data.dxf.parser.DXFParseException;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.geotools.data.dxf.entities.DXFEntity;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFConstants;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFLayer extends DXFEntity implements DXFConstants {

    private static final Log log = LogFactory.getLog(DXFLayer.class);
    
    public static final String DEFAULT_NAME = "default";
    public int _flag = 0;
    public Vector<DXFEntity> theEnt = new Vector<DXFEntity>();

    public DXFLayer(String nom, int c) {
        super(c, null, 0, null, DXFTables.defaultThickness);
        setName(nom);
    }

    public DXFLayer(String nom, int c, int flag) {
        super(c, null, 0, null, DXFTables.defaultThickness);
        setName(nom);
        _flag = flag;
    }

    @Override
    public void setVisible(boolean bool) {
        super.setVisible(bool); // FIXED; added 'super.'
        for (int i = 0; i < theEnt.size(); i++) {
            ((DXFEntity) theEnt.get(i)).setVisible(bool);
        }
    }

    public static DXFLayer read(DXFLineNumberReader br) throws NumberFormatException, IOException {
        String name = "";
        int f = 0, color = 0;

        DXFCodeValuePair cvp = null;
        DXFGroupCode gc = null;

        int sln = br.getLineNumber();
        log.debug(">>Enter at line: " + sln);
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
                case VARIABLE_NAME:
                    br.reset();
                    doLoop = false;
                    break;
                case NAME:
                    name = cvp.getStringValue();
                    break;
                case COLOR:
                    color = cvp.getShortValue();
                    break;
                case INT_1:
                    f = cvp.getShortValue();
                    break;
                default:
            }
        }

        DXFLayer l = new DXFLayer(name, color, f);
        if (color < 0) {
            l.setVisible(false);
        }
        log.debug(l.toString(name, color, f));
        log.debug(">Exit at line: " + br.getLineNumber());
        return l;
    }

    public String toString(String name, int color, int f) {
        StringBuffer s = new StringBuffer();
        s.append("DXFLayer [");
        s.append("name: ");
        s.append(name + ", ");
        s.append("color: ");
        s.append(color + ", ");
        s.append("f: ");
        s.append(f);
        s.append("]");
        return s.toString();
    }
    
    @Override
    public DXFEntity translate(double x, double y) {
        return this;
    }

    public DXFEntity clone(){
        return this;
    }
}

