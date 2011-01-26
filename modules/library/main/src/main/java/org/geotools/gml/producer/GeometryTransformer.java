/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
/*
 * GeometryTransformer.java
 *
 * Created on October 24, 2003, 1:08 PM
 */
package org.geotools.gml.producer;

import org.geotools.xml.transform.TransformerBase;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequence;


/**
 * Used to walk through GeometryObjects issuing SAX events
 * as needed.
 * <p>
 * Please note that this GeometryTransformer issues GML2 events,
 * the Coordinate 
 *
 * @author Ian Schneider
 * @source $URL$
 */
public class GeometryTransformer extends TransformerBase {
    
    protected boolean useDummyZ = false;
    
    protected int numDecimals = 4;
    
    public void setUseDummyZ(boolean flag){
        useDummyZ = flag;
    }
   
    public void setNumDecimals(int num) {
    	numDecimals = num;
    }
    
    /**
     * @TODO remove constant from GometryTraslator contructor call
     */
    public org.geotools.xml.transform.Translator createTranslator(
            ContentHandler handler) {
        return new GeometryTranslator(handler, numDecimals, useDummyZ);
    }
    
    public static class GeometryTranslator extends TranslatorSupport {
        protected CoordinateWriter coordWriter = new CoordinateWriter();
        
        public GeometryTranslator(ContentHandler handler) {            
            this(handler,"gml",GMLUtils.GML_URL);
        }
       
        public GeometryTranslator(ContentHandler handler, String prefix, String nsUri ) {
            super(handler,prefix,nsUri);
            coordWriter.setPrefix( prefix );
            coordWriter.setNamespaceUri( nsUri );
        }
        
        public GeometryTranslator(ContentHandler handler, int numDecimals) {
            this(handler,"gml",GMLUtils.GML_URL,numDecimals);
        }

        public GeometryTranslator(ContentHandler handler, String prefix, String nsUri, int numDecimals) {
            this(handler,prefix,nsUri);
            coordWriter = new CoordinateWriter(numDecimals, false);
            coordWriter.setPrefix( prefix );
            coordWriter.setNamespaceUri( nsUri );
        }
        
        public GeometryTranslator(ContentHandler handler, int numDecimals, boolean isDummyZEnabled) {
            this(handler,"gml",GMLUtils.GML_URL,numDecimals,isDummyZEnabled);
        }
        
        public GeometryTranslator(ContentHandler handler, String prefix, String nsUri, int numDecimals, boolean isDummyZEnabled) {
            this(handler,prefix,nsUri);
            coordWriter = new CoordinateWriter(numDecimals, isDummyZEnabled);
            coordWriter.setPrefix( prefix );
            coordWriter.setNamespaceUri( nsUri );
        }
        /**
         * Constructor for GeometryTranslator allowing the specification of the number of
         * valid dimension represented in the Coordinates.
         * @param handler
         * @param prefix
         * @param nsUri
         * @param numDecimals
         * @param isDummyZEnabled
         * @param dimension If this value is 3; the coordinate.z will be used rather than dummyZ
         * since 2.4.1
         */
        public GeometryTranslator(ContentHandler handler, String prefix, String nsUri, int numDecimals, boolean isDummyZEnabled, int dimension) {
            this(handler,prefix,nsUri);
            coordWriter = new CoordinateWriter(numDecimals, isDummyZEnabled, dimension );
            coordWriter.setPrefix( prefix );
            coordWriter.setNamespaceUri( nsUri );
        }        
        public boolean isDummyZEnabled(){
            return coordWriter.isDummyZEnabled();
        }
        
        public int getNumDecimals(){
            return coordWriter.getNumDecimals();
        }
        
        public void encode(Object o, String srsName)
        throws IllegalArgumentException {
            if (o instanceof Geometry) {
                encode((Geometry) o, srsName);
            } else {
                throw new IllegalArgumentException("Unable to encode " + o);
            }
        }
        
        public void encode(Object o) throws IllegalArgumentException {
            encode(o, null);
        }
        
        public void encode(Envelope bounds) {
            encode(bounds, null);
        }
        
        public void encode(Envelope bounds, String srsName) {
            // DJB: old behavior for null bounds:
            //
            //<gml:Box srsName="http://www.opengis.net/gml/srs/epsg.xml#0">
            //<gml:coordinates decimal="." cs="," ts=" ">0,0 -1,-1</gml:coordinates>
            //</gml:Box>
            //
            // new behavior:
            // <gml:null>unknown</gml:null>
            if(bounds.isNull()) {
            	encodeNullBounds();
                
                return; // we're done!
            }
            String boxName = boxName();
            
            if ((srsName == null) || srsName.equals("")) {
                start(boxName);
            } else {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "srsName", "srsName", "", srsName);
                start(boxName, atts);
            }
            
            try {
                double[] coords = new double[4];
                coords[0] = bounds.getMinX();
                coords[1] = bounds.getMinY();
                coords[2] = bounds.getMaxX();
                coords[3] = bounds.getMaxY();
                CoordinateSequence coordSeq = new PackedCoordinateSequence.Double(coords, 2);
                coordWriter.writeCoordinates(coordSeq, contentHandler);
            } catch (SAXException se) {
                throw new RuntimeException(se);
            }
            
            end(boxName);
        }
        
        /**
         * Method to be subclasses in order to allow for gml3 encoding for null enevelope.
         */
        protected void encodeNullBounds() {
        	start("null");
            String text = "unknown";
            try{
                contentHandler.characters(text.toCharArray(), 0, text.length());
            } catch(Exception e) //this shouldnt happen!!
            {
                System.out.println("got exception while writing null boundedby:"+e.getLocalizedMessage());
                e.printStackTrace();
            }
            end("null");
        }
        
        /**
         * Method to be subclassed in order to allow for gml3 encoding of envelopes.
         * @return "Box"
         */
        protected String boxName() {
        	return "Box";
        }
        
        /**
         * Encodes the given geometry with no srsName attribute and forcing 2D
         */
        public void encode(Geometry geometry) {
            encode(geometry, null);
        }
        
        /**
         * Encodes the geometry in plain 2D using the given srsName attribute value
         * @see #encode(Geometry, String, int)
         */
        public void encode(Geometry geometry, String srsName) {
            encode(geometry, srsName, 2);
        }
        
        /**
         * Encodes the given geometry with the provided srsName attribute and for the specified dimensions
         * @param geometry non null geometry to encode
         * @param srsName srsName attribute for the geometry, or <code>null</code>
         * @param dimensions shall laid between 1, 2, or 3. Number of coordinate dimensions to force.
         * TODO: dimensions is not being taken into account currently. Jody?
         */
        public void encode(Geometry geometry, String srsName, final int dimensions) {
            String geomName = GMLUtils.getGeometryName(geometry);
            
            if ((srsName == null) || srsName.equals("")) {
                start(geomName);
            } else {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "srsName", "srsName", "", srsName);
                start(geomName, atts);
            }
            
            int geometryType = GMLUtils.getGeometryType(geometry);
            
            CoordinateSequence coordSeq;
            switch (geometryType) {
                case GMLUtils.POINT:
                    coordSeq = ((Point) geometry).getCoordinateSequence();
                    try {
                        coordWriter.writeCoordinates(coordSeq, contentHandler);
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case GMLUtils.LINESTRING:
                    coordSeq = ((LineString) geometry).getCoordinateSequence();
                    try {
                        coordWriter.writeCoordinates(coordSeq, contentHandler);
                    } catch (SAXException s) {
                        throw new RuntimeException(s);
                    }
    
                    break;
                    
                case GMLUtils.POLYGON:
                    writePolygon((Polygon) geometry);
                    
                    break;
                    
                case GMLUtils.MULTIPOINT:
                case GMLUtils.MULTILINESTRING:
                case GMLUtils.MULTIPOLYGON:
                case GMLUtils.MULTIGEOMETRY:
                    writeMulti((GeometryCollection) geometry,
                            GMLUtils.getMemberName(geometryType));
                    
                    break;
            }
            
            end(geomName);
        }
        private void writePolygon(Polygon geometry) {
            String outBound = "outerBoundaryIs";
            String lineRing = "LinearRing";
            String inBound = "innerBoundaryIs";
            start(outBound);
            start(lineRing);
            
            CoordinateSequence coordSeq;
            try {
                coordSeq = geometry.getExteriorRing().getCoordinateSequence();
                coordWriter.writeCoordinates(coordSeq, contentHandler);
            } catch (SAXException s) {
                throw new RuntimeException(s);
            }
            
            end(lineRing);
            end(outBound);
            
            for (int i = 0, ii = geometry.getNumInteriorRing(); i < ii; i++) {
                start(inBound);
                start(lineRing);
                
                try {
                    coordSeq = geometry.getInteriorRingN(i).getCoordinateSequence();
                    coordWriter.writeCoordinates(coordSeq, contentHandler);
                } catch (SAXException s) {
                    throw new RuntimeException(s);
                }
                
                end(lineRing);
                end(inBound);
            }
        }
        
        private void writeMulti(GeometryCollection geometry, String member) {
            for (int i = 0, n = geometry.getNumGeometries(); i < n; i++) {
                start(member);
                
                encode(geometry.getGeometryN(i));
                
                end(member);
            }
        }
    }
}
