package org.geotools.data.dxf.header;

import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;

import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFConstants;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFBlocks implements DXFConstants {

    private static final Log log = LogFactory.getLog(DXFBlocks.class);
    public Vector<DXFBlock> theBlocks = new Vector<DXFBlock>();

    public DXFBlocks() {
        theBlocks = new Vector<DXFBlock>();
    }

    public DXFBlocks(Vector<DXFBlock> blocks) {
        if (blocks == null) {
            blocks = new Vector<DXFBlock>();
        }
        theBlocks = blocks;
    }

    public static DXFBlocks readBlocks(DXFLineNumberReader br, DXFUnivers univers) throws IOException {

        Vector<DXFBlock> sBlocks = new Vector<DXFBlock>();

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
                    if (type.equals(ENDSEC)) {
                        doLoop = false;
                        break;
                    } else if (type.equals(BLOCK)) {
                        DXFBlock block = DXFBlock.read(br, univers);
                        // Check if block (Insert) is filtered
                        if (!univers.isFilteredInsert(block._name)) {
                            sBlocks.add(block);
                        }
                    }
                    break;
                default:
                    break;
            }

        }
        DXFBlocks e = new DXFBlocks(sBlocks);
        log.debug(e.toString(sBlocks.size()));
        log.debug(">Exit at line: " + br.getLineNumber());
        return e;
    }

    public String toString(int numEntities) {
        StringBuffer s = new StringBuffer();
        s.append("DXFBlocks [");
        s.append("numEntities: ");
        s.append(numEntities);
        s.append("]");
        return s.toString();
    }
}
