/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.readers;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.geotools.data.vpf.VPFFeatureType;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.FileConstants;
import org.geotools.data.vpf.io.TripletId;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Creates Geometries for area objects
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 *
 * @source $URL$
 */
public class AreaGeometryFactory extends VPFGeometryFactory
    implements FileConstants {
    /* (non-Javadoc)
     * @see com.ionicsoft.wfs.jdbc.geojdbc.module.vpf.VPFGeometryFactory#createGeometry(java.lang.String, int, int)
     */
    public void createGeometry(VPFFeatureType featureType, SimpleFeature values)
        throws SQLException, IOException, IllegalAttributeException {
        
        int tempEdgeId;
        boolean isLeft = false;
        Coordinate previousCoordinate = null;
        Coordinate coordinate = null;
        List coordinates = null;
        Polygon result = null;
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing outerRing = null;
        List innerRings = new Vector();

        // Get face information
        //TODO: turn these column names into constants
        int faceId = Integer.parseInt(values.getAttribute("fac_id").toString());

        // Retrieve the tile directory
        String baseDirectory = featureType.getFeatureClass().getDirectoryName();
        String tileDirectory = baseDirectory;

        // If the primitive table is there, this coverage is not tiled
        if (!new File(tileDirectory.concat(File.separator).concat(FACE_PRIMITIVE))
                .exists()) {
            Short tileId = new Short(Short.parseShort(
                        values.getAttribute("tile_id").toString()));
            tileDirectory = tileDirectory.concat(File.separator)
                                         .concat(featureType.getFeatureClass()
                                                            .getCoverage()
                                                            .getLibrary()
                                                            .getTileMap()
                                                            .get(tileId)
                                                            .toString()).trim();
        }

        // all edges from this tile that use the face
        String edgeTableName = tileDirectory.concat(File.separator).concat(EDGE_PRIMITIVE);
        VPFFile edgeFile = VPFFileFactory.getInstance().getFile(edgeTableName);

        // Get the rings
        String faceTableName = tileDirectory.concat(File.separator).concat(FACE_PRIMITIVE);
        VPFFile faceFile = VPFFileFactory.getInstance().getFile(faceTableName);
        faceFile.reset();

        String ringTableName = tileDirectory.concat(File.separator).concat(RING_TABLE);
        VPFFile ringFile = VPFFileFactory.getInstance().getFile(ringTableName);
        ringFile.reset();

        SimpleFeature faceFeature = faceFile.readFeature();

        while (faceFeature != null) {
            if (faceFeature.getAttribute("id").equals(new Integer(faceId))) {
                coordinates = new LinkedList();

                int ringId = Integer.parseInt(faceFeature.getAttribute(
                            "ring_ptr").toString());

                // Get the starting edge
                int startEdgeId = ((Number) ringFile.getRowFromId("id", ringId)
                                                    .getAttribute("start_edge"))
                    .intValue();
                int nextEdgeId = startEdgeId;
                int prevNodeId = -1;

                while (nextEdgeId > 0) {
                    SimpleFeature edgeRow = edgeFile.getRowFromId("id", nextEdgeId);

                    // Read all the important stuff from the edge row data
                    int leftFace  = ((TripletId) edgeRow.getAttribute("left_face")).getId();
                    int rightFace = ((TripletId) edgeRow.getAttribute("right_face")).getId();
                    int startNode = ((Integer) edgeRow.getAttribute("start_node")).intValue();
                    int endNode   = ((Integer) edgeRow.getAttribute("end_node")).intValue();
                    int leftEdge  = ((TripletId) edgeRow.getAttribute("left_edge")).getId();
                    int rightEdge = ((TripletId) edgeRow.getAttribute("right_edge")).getId();
                    boolean addPoints = true;

                    // If both faceIds are this faceId then this is a line extending into
                    // the face and not an edge line of the face so don't add it's points
                    // to the coordinates list.  Except if it's the first edge encountered.
                    // ASCII art showing this case:
                    //   /-----------\
                    //   |           |
                    //   +---+       |
                    //   | ^^        |
                    //   | This one  |
                    //   \-----------/
                    if (faceId == leftFace && faceId == rightFace) {
                        addPoints = false;

                        if (prevNodeId == startNode) {
                            isLeft = false;
                            prevNodeId = endNode;
                        } else if (prevNodeId == endNode) {
                            isLeft = true;
                            prevNodeId = startNode;
                        } else if (prevNodeId == -1) {
                            // This edge is the first one to be encountered.  
                            // This is a messy case where we've got to figure out if
                            // we should start to the left or right.  This peeks ahead
                            // at the left and right edges to see which has a start node
                            // that's the same as this edge's end node.  Hopefully someone
                            // smarter can come up with a better solution.
                            int leftEdgeStartNode =
                                ((Integer)edgeFile.getRowFromId("id", leftEdge).getAttribute("start_node")).intValue();
                            int rightEdgeStartNode =
                                ((Integer)edgeFile.getRowFromId("id", rightEdge).getAttribute("start_node")).intValue();
                            
                            if (leftEdgeStartNode == endNode) {
                                isLeft = true;
                                prevNodeId = startNode;
                            } else if (rightEdgeStartNode == endNode) {
                                isLeft = false;
                                prevNodeId = endNode;
                            } else {
                                // Something really bad happened because we should never get here
                                throw new SQLException(
                                    "This edge is not part of this face.");
                            }
                        } else {
                            // Something really bad happened because we should never get here
                            throw new SQLException(
                                "This edge is not part of this face.");
                        }
                    } else if (faceId == rightFace) {
                        isLeft = false;
                        prevNodeId = endNode;
                    } else if (faceId == leftFace) {
                        isLeft = true;
                        prevNodeId = startNode;
                    } else {
                        throw new SQLException(
                            "This edge is not part of this face.");
                    }

                    // Get the geometry of the edge and add it to our line geometry
                    LineString edgeGeometry = (LineString) edgeRow.getAttribute(
                                                                 "coordinates");
                    
                    if ( addPoints )
                    {
                        if (isLeft) {
                            // We must take the coordinate values backwards
                            for (int inx = edgeGeometry.getNumPoints() - 1;
                                 inx >= 0; inx--) {
                                coordinate = edgeGeometry.getCoordinateSequence().getCoordinate(inx);

                                if ((previousCoordinate == null)
                                    || (!coordinate.equals3D(previousCoordinate))) {
                                    coordinates.add(coordinate);
                                    previousCoordinate = coordinate;
                                }
                            }
                        } else {
                            for (int inx = 0; inx < edgeGeometry.getNumPoints(); inx++) {
                                coordinate = edgeGeometry.getCoordinateSequence().getCoordinate(inx);

                                if ((previousCoordinate == null)
                                    || (!coordinate.equals3D(previousCoordinate))) {
                                    coordinates.add(coordinate);
                                    previousCoordinate = coordinate;
                                }
                            }
                        }
                    } else {
                        coordinate = edgeGeometry.getCoordinateSequence().getCoordinate(
                                           isLeft ? 0 : edgeGeometry.getNumPoints() - 1);
                    }
                    
                    tempEdgeId = isLeft ? leftEdge : rightEdge;

                    if (tempEdgeId == startEdgeId) {
                        nextEdgeId = 0;
                    } else {
                        // Here is where we need to consider crossing tiles
                        nextEdgeId = tempEdgeId;
                    }
                }

                // The dorks at JTS insist that you explicitly close your rings. Ugh.
                if (!coordinate.equals(coordinates.get(0))) {
                    coordinates.add(coordinates.get(0));
                }

                Coordinate[] coordinateArray = new Coordinate[coordinates.size()];

                for (int cnx = 0; cnx < coordinates.size(); cnx++) {
                    coordinateArray[cnx] = (Coordinate) coordinates.get(cnx);
                }

                LinearRing ring = null;

                ring = geometryFactory.createLinearRing(coordinateArray);

                if (outerRing == null) {
                    outerRing = ring;
                } else {
                    // I haven't found any data to test this yet. 
                    // If you do and it works, remove this comment.
                    innerRings.add(ring);
                }
            }

            if (faceFile.hasNext()) {
                faceFeature = faceFile.readFeature();
            } else {
                faceFeature = null;
            }
        }

        if (innerRings.isEmpty()) {
            result = geometryFactory.createPolygon(outerRing, null);
        } else {
            LinearRing[] ringArray = new LinearRing[innerRings.size()];

            for (int cnx = 0; cnx < innerRings.size(); cnx++) {
                ringArray[cnx] = (LinearRing) innerRings.get(cnx);
            }

            result = geometryFactory.createPolygon(outerRing, ringArray);
        }

        values.setDefaultGeometry(result);
    }
}
