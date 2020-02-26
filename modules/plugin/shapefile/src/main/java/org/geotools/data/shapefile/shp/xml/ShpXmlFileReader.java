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
 */
package org.geotools.data.shapefile.shp.xml;

import static javax.xml.stream.XMLInputFactory.IS_COALESCING;
import static javax.xml.stream.XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES;
import static javax.xml.stream.XMLInputFactory.SUPPORT_DTD;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.data.shapefile.files.FileReader;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.locationtech.jts.geom.Envelope;

public class ShpXmlFileReader implements FileReader {

    private ShpFiles shapefileFiles;

    /**
     * Parse metadataFile (currently for bounding box information).
     *
     * <p>
     */
    public ShpXmlFileReader(ShpFiles shapefileFiles) {
        this.shapefileFiles = shapefileFiles;
    }

    private XMLStreamReader reader(InputStream in) throws XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        // disable resolving of external DTD entities (coalescing needs to be false)
        inputFactory.setProperty(IS_COALESCING, false);
        inputFactory.setProperty(IS_REPLACING_ENTITY_REFERENCES, false);
        // disallow DTDs entirely
        inputFactory.setProperty(SUPPORT_DTD, false);

        return inputFactory.createXMLStreamReader(in, "UTF-8");
    }

    public Metadata parse() {
        try (InputStream inputStream = shapefileFiles.getInputStream(ShpFileType.SHP_XML, this)) {
            return parseMetadata(reader(inputStream));
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    protected Metadata parseMetadata(XMLStreamReader reader) throws XMLStreamException {
        Metadata meta = new Metadata();
        int tag;
        while ((tag = reader.next()) != END_DOCUMENT) {
            if (START_ELEMENT == tag) {
                switch (reader.getLocalName()) {
                    case "idinfo":
                        IdInfo idInfo = parseIdInfo(reader);
                        meta.setIdinfo(idInfo);
                        break; // break, no other contents being parsed atm
                }
            }
        }
        return meta;
    }

    protected IdInfo parseIdInfo(XMLStreamReader reader) throws XMLStreamException {
        reader.require(START_ELEMENT, null, "idinfo");

        Envelope bounding = new Envelope();
        Envelope lbounding = new Envelope();
        if (seekElement(reader, "spdom")) {
            int tag;
            while ((tag = reader.nextTag()) != END_ELEMENT
                    && !"spdom".equals(reader.getLocalName())) {
                if (tag == START_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "bounding":
                            bounding = parseBounding(reader);
                            break;
                        case "lbounding":
                            lbounding = parseBounding(reader);
                            break;
                    }
                }
            }
        }
        IdInfo idInfo = new IdInfo();
        idInfo.setBounding(bounding);
        idInfo.setLbounding(lbounding);
        return idInfo;
    }

    private boolean seekElement(XMLStreamReader reader, String elementName)
            throws XMLStreamException {
        reader.require(START_ELEMENT, null, null);
        final String thisElem = reader.getLocalName();
        while (true) {
            int tag = reader.next();
            if (tag == START_ELEMENT && elementName.equals(reader.getLocalName())) {
                return true;
            }
            if (tag == END_ELEMENT && thisElem.equals(reader.getLocalName())) {
                return false;
            }
        }
    }

    protected Envelope parseBounding(XMLStreamReader reader) throws XMLStreamException {
        reader.require(START_ELEMENT, null, null);
        double minX = Double.NaN;
        double maxX = Double.NaN;
        double minY = Double.NaN;
        double maxY = Double.NaN;
        final String thisElem = reader.getLocalName();
        while (reader.nextTag() != END_ELEMENT && !thisElem.equals(reader.getLocalName())) {
            switch (reader.getLocalName()) {
                case "westbc":
                    minX = Double.parseDouble(reader.getElementText());
                    break;
                case "eastbc":
                    maxX = Double.parseDouble(reader.getElementText());
                    break;
                case "southbc":
                    minY = Double.parseDouble(reader.getElementText());
                    break;
                case "northbc":
                    maxY = Double.parseDouble(reader.getElementText());
                    break;
            }
        }
        return new Envelope(minX, maxX, minY, maxY);
    }

    public String id() {
        return "Shp Xml Reader";
    }
}
