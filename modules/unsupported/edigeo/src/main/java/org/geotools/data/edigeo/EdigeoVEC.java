/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
 * 
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.edigeo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataSourceException;
import org.geotools.data.edigeo.EdigeoFeatureReader.Visitor;
import org.opengis.feature.IllegalAttributeException;

/**
 *
 * @author mcoudert
 *
 * @source $URL$
 */
public class EdigeoVEC {

    private File vecFile = null;

    // Edigeo VEC extension file
    private static final String VECExtension = "vec";
    // Field separator
    private static final String DS = ":";
    private static final String VS = ";";

    // Geo Primitives
    private static final String nodeDesc = "PNO";
    private static final String faceDesc = "PFE";
    private static final String arcDesc = "PAR";
    // Descriptor header
    private static final String objIdDesc = "RIDSA";
    private static final String lnkDesc = "FTPCP";
    private static final String typDesc = "RTYSA";
    private static final String nbNodeDesc = "PTCSN";
    private static final String nbAttDesc = "ATCSN";
    private static final String idAttDesc = "ATPCP";
    private static final String valAttrDesc = "ATVS";
    private static final String coordDesc = "CORCC";
    private static final String POINT = "POINT";
    private static final String LINESTRING = "LINESTRING";
    private static final String POLYGON = "POLYGON";
    private static final String MULTIPOLYGON = "MULTIPOLYGON";
    protected static String geoType = null;
    private int nbObjs = 0;
    private int nbarc = 0;
    private int nbPoint = 0;
    protected boolean topo = false;
    private Logger logger = Logger.getLogger("org.geotools.data.edigeo");

    /**
     * <p>
     * This constructor opens an existing THF file
     * </p>
     *
     * @param path Full pathName of the thf file, can be specified without the
     *        .thf extension
     *
     * @throws IOException If the specified thf file could not be opened
     */
    public EdigeoVEC(String path) throws IOException {
        super();
        vecFile = EdigeoFileFactory.setFile(path, VECExtension, true);
    }

    /**
     * 
     * @param obj
     */
    public void readVECFile(String obj, Visitor visitor) throws IOException, IllegalAttributeException {
        EdigeoParser vecParser = new EdigeoParser(vecFile);
        String idObj = null;
        String buffer = "";
        String att = "";
        String value = "";

        if (obj.equals("PARCELLE_id")) {
            topo = true;
        }

        geoType = EdigeoDataStore.scdObj.get(obj).get("type").toUpperCase();

        if (logger.isLoggable(Level.INFO)) {
            logger.info("Creating Edigeo Datastore, please wait, it may take a few minutes...");
        }

        while (vecParser.readLine()) {

            if (vecParser.line.contains(VS + obj) && buffer.contains(objIdDesc)) {

                // we've got a feature
                HashMap<String, String> atts = new HashMap<String, String>();
                idObj = getValue(buffer, objIdDesc);
                List<Coordinate[]> geoms = getRelation(idObj);
                nbObjs++;

                while (vecParser.readLine()) {
                    if (vecParser.line.contains(nbAttDesc)) {
                        int nbatt = Integer.parseInt(vecParser.getValue(nbAttDesc));
                        for (int i = 0; i < nbatt; i++) {

//                            String charsetBuffer = "";
                            while (vecParser.readLine()) {
                                if (vecParser.line.contains(idAttDesc)) {
                                    att = vecParser.getValue(idAttDesc);
                                    att = att.substring(att.lastIndexOf(";") + 1);
                                    continue;
                                }
                                if (vecParser.line.contains(valAttrDesc)) {
//                                    if (false && charsetBuffer.equals("TEXT 06:8859-1")) {
//                                        CharsetDecoder dec = Charset.forName("ISO-8859-1").newDecoder();
//                                        dec.onMalformedInput(CodingErrorAction.REPORT);
//                                        dec.onUnmappableCharacter(CodingErrorAction.REPORT);
//                                        CharBuffer cb = dec.decode(ByteBuffer.wrap(vecParser.getValue("ATVS").getBytes()));
//                                        value = cb.toString();
//                                        value = new String(vecParser.getValue("ATVS").getBytes("ISO-8859-1"));
//                                    } else {
//                                        value = vecParser.getValue("ATVS");
//                                    }
                                    value = vecParser.getValue(valAttrDesc);
                                    value = getPrecodedValue(att, value);

                                    atts.put(att, value);
                                    break;
                                }
//                                charsetBuffer = vecParser.line;
                            }
                        }
                        break;
                    }
                }
                // Create the feature
                Object[] values = null;
                int nbAtt;

                // with attributes if exist
                if (!EdigeoDataStore.ftAtt.isEmpty()) {
                    int cpt = 0;
                    nbAtt = EdigeoDataStore.ftAtt.size();
                    values = new Object[nbAtt + 1];
                    Iterator<String> it = EdigeoDataStore.ftAtt.keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (atts.containsKey(key)) {
                            values[cpt] = atts.get(key);
                        } else {
                            values[cpt] = null;
                        }
                        cpt++;
                    }
                } else {
                    values = new Object[1];
                    nbAtt = 0;
                }

                Geometry geom = null;
                if (!geoms.isEmpty()) {
                    geom = createGeometry(geoms, idObj);
                    values[nbAtt] = geom;
                    visitor.visit(values, idObj);
                } else {
                    if (logger.isLoggable(Level.WARNING)) {
                        logger.warning("Unable to find geometry relation for FID#" + idObj);
                    }
                }
            }
            buffer = vecParser.line;
        }
        vecParser.close();
    }

    /**
     * 
     * @param id
     * @throws java.io.FileNotFoundException
     */
    protected List<Coordinate[]> getRelation(String id) throws IOException {
        EdigeoParser parser = new EdigeoParser(vecFile);
        String lnk = "";
        String buffern1 = "";
        String otherLnk = "";
        List<Coordinate[]> geoms = new LinkedList<Coordinate[]>();

        while (parser.readLine()) {
            // Parse all link description for element defined by its id
        	if (parser.line.contains(VS + id) || buffern1.contains(VS + id)) {
        		if (buffern1.contains(lnkDesc) && parser.line.contains(lnkDesc)) {
        			if (buffern1.contains(VS + id)) {
                        lnk = new String(parser.line);
                    } else {
                        lnk = new String(buffern1);
                    }
        			// try to look for other link description (lnkDesc)
        			while (parser.readLine()) {
        				if (parser.line.contains(lnkDesc)) {
        					otherLnk = getValue(parser.line,lnkDesc);
        					List<Coordinate[]> test = getType(otherLnk.substring(otherLnk.lastIndexOf(VS) + 1));
        					geoms.addAll(test);
        				} else
        					break;
        			}
                    lnk = getValue(lnk, lnkDesc);
                    List<Coordinate[]> list = getType(lnk.substring(lnk.lastIndexOf(VS) + 1));
                    if (!list.isEmpty()) {
                        if (!topo) {
                            geoms.addAll(list);
                            break;
                        } else {
                            geoms.addAll(list);
                        }
                    }
                }
            }
            buffern1 = new String(parser.line);
        }
        parser.close();
        return geoms;
    }

    /**
     * Gets value of the specified descriptor
     * 
     * @param target Descriptor
     * @return String
     */
    public String getValue(String line, String target) {
        int index = line.indexOf(target);
        int nbchar = Integer.parseInt(line.substring(index + 5, index + 7));
        // Avoid index out of range exception 
        if (index + nbchar + 8 > line.length()) {
            return line.substring(index + 8, line.length());
        }
        String value = line.substring(index + 8, index + nbchar + 8);
        return value;
    }

    /**
     * 
     * @param id
     * @param FID
     * @return List<Coordinate[]>
     * @throws java.io.IOException
     */
    protected List<Coordinate[]> getType(String id) throws IOException {
    	EdigeoParser parser = new EdigeoParser(vecFile);
        String buffern1 = "";
        String type = "";
        List<Coordinate[]> geoms = new LinkedList<Coordinate[]>();

        while (parser.readLine()) {
            if (parser.line.contains(DS + id)) {
                type = getValue(buffern1, typDesc);
                if (type.equals(faceDesc)) {
                    geoms = getRelation(id);
                    break;
                } else if (type.equals(arcDesc)) {
                    geoms.add(getCoordinates(parser, false));
                    nbarc++;
                } else if (type.equals(nodeDesc)) {
                    geoms.add(getCoordinates(parser, true));
                    nbPoint++;
                }
                break;
            }
            buffern1 = new String(parser.line);
        }

        parser.close();
        return geoms;
    }

    /**
     * 
     * @param parser
     * @param isNode
     * @return Coordinate[]
     */
    private Coordinate[] getCoordinates(EdigeoParser parser, boolean isNode) {
        Coordinate[] coords = null;
        while (parser.readLine()) {
            if (isNode) {
                if (parser.line.contains(coordDesc)) {
                    coords = new Coordinate[1];
                    String xy = getXY(parser.line);
                    coords[0] = parseCoordinate(xy);
                    break;
                }
            } else {
                if (parser.line.contains(nbNodeDesc)) {
                    int size = Integer.parseInt(getValue(parser.line, nbNodeDesc));
                    coords = new Coordinate[size];
                    for (int i = 0; i < size; i++) {
                        parser.readLine();
                        if (parser.line.contains(coordDesc)) {
                            String xy = getXY(parser.line);
                            coords[i] = parseCoordinate(xy);
                        }
                    }
                    break;
                }
            }
        }
        return coords;
    }

    /**
     * 
     * @param coord
     * @return Coordinate
     */
    protected Coordinate parseCoordinate(String coord) {
        double x = Double.parseDouble(coord.substring(0, coord.indexOf(" ")));
        double y = Double.parseDouble(coord.substring(coord.indexOf(" ")));
        Coordinate coordinate = new Coordinate(x, y);
        return coordinate;
    }

    /**
     * 
     * @param line
     * @return
     */
    protected String getXY(String line) {
        String coord = "";
        coord = getValue(line, coordDesc);
        return coord.replace("+", "").replaceAll(";", " ");
    }

    /**
     * Get precoded attribute values
     * @param value
     * @return
     */
    private String getPrecodedValue(String attribut, String value) {
        if (EdigeoDataStore.ftAtt.get(attribut).get("precoded").equals("true")) {
            value = EdigeoDataStore.ftAtt.get(attribut).get(value);
        }
        return value;
    }

    /**
     * Creates geometry from a list of Coordinate array.
     * @param geo
     * @param FID
     * @return
     * @throws org.geotools.data.DataSourceException
     */
    protected Geometry createGeometry(List<Coordinate[]> coords, String FID) throws DataSourceException {

        GeometryFactory geomFact = new GeometryFactory();
        Geometry geom = null;
        LinearRing shell = null;
        LinearRing[] holes = null;
        Polygon[] polys = null;

        if (geoType.equals(POINT)) {
            geom = (Geometry) geomFact.createPoint(coords.get(0)[0]);
        } else if (geoType.equals(LINESTRING)) {
            geom = (Geometry) geomFact.createLineString(coords.get(0));
        } else if (geoType.equals(POLYGON) && !topo) {
        	// Edigeo polygon objects are handled as polygon with holes
        	shell = geomFact.createLinearRing(coords.get(0));
        	if (coords.size()>1) {
        		// handle polygons with holes
        		holes = new LinearRing[coords.size() - 1];
        		for (int i = 1; i < coords.size(); i++) {
                    holes[i - 1] = geomFact.createLinearRing(coords.get(i));
                }
            }
        	geom = (Geometry) geomFact.createPolygon(shell, holes);
        	
        } else if (geoType.equals(POLYGON) && topo) {
        	// Constructs polygon for Edigeo topologic object (ie PARCELLE_id)
            List<Coordinate[]> polygonCoords = getPolygonCoordinates(coords);
            shell = geomFact.createLinearRing(polygonCoords.get(0));
            holes = new LinearRing[polygonCoords.size() - 1];
            for (int i = 1; i < polygonCoords.size(); i++) {
                holes[i - 1] = geomFact.createLinearRing(polygonCoords.get(i));
            }
            geom = (Geometry) geomFact.createPolygon(shell, holes);

        } else if (geoType.equals(MULTIPOLYGON)) {
        	// Constructs multipolygon for Edigeo object (ie SECTION_id)
        	polys = new Polygon[coords.size()];
    		Iterator<Coordinate[]> it = coords.iterator();
        	int cpt = 0;
        	while (it.hasNext()) {
        		shell = geomFact.createLinearRing(it.next());
        		polys[cpt] = geomFact.createPolygon(shell, holes);
        		cpt++;
        	}
        	geom = geomFact.createMultiPolygon(polys);
        } else {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe("Can't handle the type " + geoType + " for fid#" + FID);
            }
            throw new DataSourceException("Can't handle the type " + geoType + " for fid#" + FID);
        }

        return geom;
    }

    /**
     * 
     * @param coordList
     * @param FID
     * @return
     */
    private List<Coordinate[]> getPolygonCoordinates(List<Coordinate[]> coordList) {

        List<Coordinate[]> polygonCoordList = new LinkedList<Coordinate[]>();

        while (coordList.size() > 0) {
            polygonCoordList.add(getRingCoordinates(coordList));
        }

        return polygonCoordList;
    }

    /**
     * 
     * @param listCoord
     * @return
     */
    private Coordinate[] getRingCoordinates(List<Coordinate[]> listCoord) {

        int bufferSize = 0;
        Iterator<Coordinate[]> it = listCoord.iterator();
        Vector<Coordinate> aggCoords = new Vector<Coordinate>(Arrays.asList(it.next()));
        it.remove();
        it = null;

        while (listCoord.size() > 0) {

            Iterator<Coordinate[]> iter = listCoord.iterator();

            while (iter.hasNext()) {
                Coordinate[] currentCoordArray = iter.next();
                if (aggCoords.get(aggCoords.size() - 1).equals(currentCoordArray[0])) {
                    for (int i = 1; i < currentCoordArray.length; i++) {
                        aggCoords.add(currentCoordArray[i]);
                    }
                    iter.remove();
                } else if (aggCoords.get(aggCoords.size() - 1).equals(currentCoordArray[currentCoordArray.length - 1])) {
                    for (int i = currentCoordArray.length - 2; i >= 0; i--) {
                        aggCoords.add(currentCoordArray[i]);
                    }
                    iter.remove();
                }
            }
            iter = null;

            if (bufferSize == listCoord.size() && bufferSize != 0) {
                break;
            }
            bufferSize = listCoord.size();
        }

        return aggCoords.toArray(new Coordinate[aggCoords.size()]);
    }
}
