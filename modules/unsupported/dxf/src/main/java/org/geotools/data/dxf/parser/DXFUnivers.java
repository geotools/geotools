package org.geotools.data.dxf.parser;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

import java.io.EOFException;
import java.io.IOException;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

import org.geotools.data.GeometryType;
import org.geotools.data.dxf.entities.DXFEntity;
import org.geotools.data.dxf.entities.DXFInsert;
import org.geotools.data.dxf.entities.DXFPoint;
import org.geotools.data.dxf.header.DXFBlock;
import org.geotools.data.dxf.header.DXFBlockReference;
import org.geotools.data.dxf.header.DXFBlocks;
import org.geotools.data.dxf.header.DXFEntities;
import org.geotools.data.dxf.header.DXFHeader;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.header.DXFTables;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DXFUnivers implements DXFConstants {

    private static final Log log = LogFactory.getLog(DXFUnivers.class);
    public static final PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
    public static final int NUM_OF_SEGMENTS = 16; // Minimum number of segments for a circle (also used for arc)
    public static final double MIN_ANGLE = 2 * Math.PI / NUM_OF_SEGMENTS; // Minimum number of segments for a circle (also used for arc)
    private Vector<DXFBlockReference> _entForUpdate = new Vector<DXFBlockReference>();
    public Vector<DXFTables> theTables = new Vector<DXFTables>();
    public Vector<DXFBlock> theBlocks = new Vector<DXFBlock>();
    public Vector<DXFEntity> theEntities = new Vector<DXFEntity>();
    private DXFHeader _header;
    private GeometryFactory geometryFactory = null;
    private Geometry errorGeometry = null;
    private HashMap insertsFound = new HashMap();
    private ArrayList dxfInsertsFilter;
    private String info = ""; // Used for getInfo();  returns this string with information about the file

    public DXFUnivers(ArrayList dxfInsertsFilter) {
        this.dxfInsertsFilter = dxfInsertsFilter;
    }

    public boolean isFilteredInsert(String blockName) {
        return dxfInsertsFilter.contains(blockName);
    }

    public void read(DXFLineNumberReader br) throws IOException {
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
                    if (type.equals(SECTION)) {
                        readSection(br);
                    }
                    break;
                default:
                    break;
            }
        }
        updateRefBlock();
    }

    public void readSection(DXFLineNumberReader br) throws IOException {
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
                    }
                    break;
                case NAME:
                    String name = cvp.getStringValue();
                    if (name.equals(HEADER)) {
                        _header = DXFHeader.read(br);
                        if (_header._EXTMAX == null || _header._EXTMIN == null) {
                            _header = new DXFHeader();
                        }
                        /* construct geometry factory */
                        geometryFactory = new GeometryFactory(precisionModel, _header._SRID);
                    } else if (name.equals(TABLES)) {
                        DXFTables at = DXFTables.readTables(br, this);
                        theTables.add(at);
                    } else if (name.equals(BLOCKS)) {
                        DXFBlocks ab = DXFBlocks.readBlocks(br, this);
                        theBlocks.addAll(ab.theBlocks);
                    } else if (name.equals(ENTITIES)) {
                        DXFEntities dxfes = DXFEntities.readEntities(br, this);
                        theEntities.addAll(dxfes.theEntities);
                    // toevoegen aan layer doen we even niet, waarschijnlijk niet nodig
                    //if (o != null && o._refLayer != null) {
                    //    o._refLayer.theEnt.add(o);
                    //}
                    }
                    break;
                default:
                    break;
            }

        }
    }

    public DXFBlock findBlock(String nom) {
        DXFBlock b = null;
        for (int i = 0; i < theBlocks.size(); i++) {
            if (theBlocks.elementAt(i)._name.equals(nom)) {
                insertsFound.put(nom, true);
                return theBlocks.elementAt(i);
            }
        }
        return b;
    }

    public DXFLayer findLayer(String nom) {
        DXFLayer l = null;
        for (int i = 0; i < theTables.size(); i++) {
            for (int j = 0; j < theTables.elementAt(i).theLayers.size(); j++) {
                if (theTables.elementAt(i).theLayers.elementAt(j).getName().equals(nom)) {
                    l = theTables.elementAt(i).theLayers.elementAt(j);
                    return l;
                }
            }
        }

        l = new DXFLayer(nom, DXFColor.getDefaultColorIndex());

        if (theTables.size() < 1) {
            theTables.add(new DXFTables());
        }

        theTables.elementAt(0).theLayers.add(l);
        return l;
    }

    public DXFLineType findLType(String name) {
        for (int i = 0; i < theTables.size(); i++) {
            for (int j = 0; j < theTables.elementAt(i).theLineTypes.size(); j++) {
                if (theTables.elementAt(i).theLineTypes.elementAt(j)._name.equals(name)) {
                    return theTables.elementAt(i).theLineTypes.elementAt(j);
                }
            }
        }
        return null;
    }

    public void addRefBlockForUpdate(DXFBlockReference obj) {
        _entForUpdate.add(obj);
    }

    // Use only once!
    public void updateRefBlock() {
        DXFBlockReference bro = null;
        int numInserts = 0;

        for (int i = 0; i < _entForUpdate.size(); i++) {
            bro = _entForUpdate.get(i);
            boolean isInsert = false;

            if (bro instanceof DXFInsert) {
                numInserts++;
                isInsert = true;
            }

            if (!insertsFound.containsKey(bro._blockName)) {
                insertsFound.put(bro._blockName, false);
            }

            DXFBlock b = findBlock(bro._blockName);

            if (b != null && bro.getType() != GeometryType.UNSUPPORTED) {
                double x = b._point.X();
                double y = b._point.Y();

                if (isInsert) {
                    DXFPoint entPoint = ((DXFInsert) bro)._point;
                    x = entPoint._point.getX();
                    y = entPoint._point.getY();
                }

                Vector<DXFEntity> refBlockEntities = b.theEntities;
                if (refBlockEntities != null) {
                    Iterator it = refBlockEntities.iterator();
                    while (it.hasNext()) {
                        // Create clone if blockEntity is a insert
                        DXFEntity e = ((isInsert) ? ((DXFEntity) it.next()).clone() : (DXFEntity) it.next());

                        if (isInsert) {
                            e.setBase(((DXFInsert) bro)._point.toCoordinate());
                            e.setAngle(((DXFInsert) bro)._angle);
                        }

                        e.updateGeometry();
                        theEntities.add(e);
                    }
                }
            } else {
                log.error("Can not update refblock: " + bro.getName() + " - " + bro._blockName + " at " + bro.getStartingLineNumber());
            }

            if (!isInsert) {
                theEntities.remove(bro);
            }
        }

        // Set information
        if (_entForUpdate.size() > 0) {
            info += "Num of Blocks: " + _entForUpdate.size() + " of which Inserts: " + numInserts + "\n";
            java.util.HashMap list = new java.util.HashMap();

            // Count inserts in updateList
            for (int i = 0; i < _entForUpdate.size(); i++) {
                if (_entForUpdate.get(i) instanceof DXFInsert) {
                    DXFInsert fg = (DXFInsert) _entForUpdate.get(i);
                    String name = fg._blockName;

                    if (!list.containsKey(name)) {
                        list.put(name, 1);
                    } else {
                        list.put(name, ((Integer) list.get(name)) + 1);
                    }
                }
            }

            ArrayList arrayList = new ArrayList(list.keySet());
            Collections.sort(arrayList);

            for (java.util.Iterator iter = arrayList.iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                String value = Integer.toString((Integer) list.get(key));
                String found = ((Boolean) insertsFound.get(key) ? "" : (isFilteredInsert(key) ? "Filtered" : "Missing"));

                key = makeTabs(key, 31);
                value = makeTabs(value, 12);

                info += key + value + found + "\n";
            }
        }

        _entForUpdate.removeAllElements();
    }

    public String getInfo() {
        return info;
    }

    public GeometryFactory getGeometryFactory() {
        if (geometryFactory == null) {
            geometryFactory = new GeometryFactory(precisionModel);
        }
        return geometryFactory;
    }

    public void setGeometryFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    public Geometry getErrorGeometry() {
        if (errorGeometry == null && geometryFactory != null) {
            errorGeometry = geometryFactory.createPoint(new Coordinate(0.0, 0.0));
        }
        return errorGeometry;
    }

    public void setErrorGeometry(Geometry errorGeometry) {
        this.errorGeometry = errorGeometry;
    }

    public static String makeTabs(String text, int length) {
        for (int i = text.length() - 1; i < length; i++) {
            text += " ";
        }
        return text;
    }
}
