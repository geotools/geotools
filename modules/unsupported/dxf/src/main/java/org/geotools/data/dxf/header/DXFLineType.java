package org.geotools.data.dxf.header;

import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;


import org.geotools.data.dxf.parser.DXFParseException;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFLineType {

    private static final Log log = LogFactory.getLog(DXFLineType.class);
    public static final String DEFAULT_NAME = "default";
    public String _name = "DXFLineType";                       // 2
    public String _value = "";					// 3
    public float _length = 0;					// 40
    public float _count = 0;					// 73
    public Vector<Float> _spacing = new Vector<Float>();	// 49

    public DXFLineType() {
    }

    public DXFLineType(String nom, String value, float length, float count, Vector<Float> spacing) {
        _name = nom;
        _value = value;
        _length = length;
        _count = count;

        if (spacing != null) {
            _spacing = spacing;
        }
    }

    public static DXFLineType read(DXFLineNumberReader br) throws IOException {
        String value = "", name = "";
        Vector<Float> spacing = new Vector<Float>();
        float count = 0, length = 0;

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
                case VARIABLE_NAME:
                    br.reset();
                    doLoop = false;
                    break;
                case NAME:
                    name = cvp.getStringValue();
                    break;
                case TEXT_OR_NAME_2:
                    value = cvp.getStringValue();
                    break;
                case INT_4:
                    count = cvp.getShortValue();
                    break;
                case DOUBLE_1:
                    length = (float) cvp.getDoubleValue();
                    break;
                case REPEATED_DOUBLE_VALUE:
                    spacing.add((float) cvp.getDoubleValue());
                    break;
                default:
                    break;
            }

        }

        log.debug(">Exit at line: " + br.getLineNumber());
        if (value.equals("") && name.equals("")) {
            return null;
        } else {
            DXFLineType e = new DXFLineType(name, value, length, count, spacing);
            log.debug(e.toString(name, value, length, count));
            return e;
        }
    }

    public String toString(String name, String value, float length, float count) {
        StringBuffer s = new StringBuffer();
        s.append("DXFLineType [");
        s.append("name: ");
        s.append(name + ", ");
        s.append("value: ");
        s.append(value + ", ");
        s.append("length: ");
        s.append(length + ", ");
        s.append("count: ");
        s.append(count);
        s.append("]");
        return s.toString();
    }
}
