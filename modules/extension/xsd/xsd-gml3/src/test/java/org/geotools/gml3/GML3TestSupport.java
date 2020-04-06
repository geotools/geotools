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
package org.geotools.gml3;

import java.util.HashMap;
import java.util.Map;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.test.XMLTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Test bindings by extending this class with test cases that follow this pattern:
 *
 *  Debugging hints:
 *
 *  XMLTestSupport has javadoc on how things work.
 *  TODO - add hints for eclipse breakpoint debugging.
 *
 *  // parse using the binding -
 *  // NB should have separate tests for all the type bindings supported
 *  // test methods must start with "test"
 *
 *    test1D() throws Exception {
 *
 *       // create a DOM representation
 *
 *       GML3MockData.element(GML.pos, document, document);
 *       document.getDocumentElement().appendChild(document.createTextNode("1.0"));
 *
 *  	 //  then parse (using standard XMLTestSupport parse method)
 *
 *       DirectPosition pos = (DirectPosition) parse();
 *
 *       // test aspects of the result
 *
 *       assertNotNull(pos);
 *       assertTrue(pos instanceof DirectPosition1D);
 *       assertEquals(pos.getOrdinate(0), 1.0, 0);
 *    }
 *
 *    // test encodings with something like this:
 *
 *    public void testEncode() throws Exception {
 *        Document dom = encode(GML3MockData.bounds(), GML.Envelope);
 *
 *        // debugging method:
 *        print(dom);
 *
 *        assertEquals("something", dom.getElementsByTagNameNS(GML.NAMESPACE, "lowerCorner").getLength());
 *
 */

public abstract class GML3TestSupport extends XMLTestSupport {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xs", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));

        registerNamespaceMapping("gml", "http://www.opengis.net/gml");
    }

    /*
     * binds to the GMLConfiguration in the current package
     * i.e. this is a GML3 specific binding configuration.
     */
    protected Configuration createConfiguration() {
        return new GMLConfiguration(enableExtendedArcSurfaceSupport());
    }

    protected void checkPosOrdinates(Document doc, int expectedNumOrdinates) {
        checkOrdinates(doc, GML.pos.getLocalPart(), expectedNumOrdinates);
    }

    protected void checkPosListOrdinates(Document doc, int expectedNumOrdinates) {
        checkOrdinates(doc, GML.posList.getLocalPart(), expectedNumOrdinates);
    }

    /**
     * Checks that a posList exists, has a string as content, and the string encodes nOrdinates
     * ordinates correctly (i.e. blank-separated).
     */
    private void checkOrdinates(Document doc, String ordTag, int expectedNumOrdinates) {
        NodeList nl = doc.getElementsByTagNameNS(GML.NAMESPACE, ordTag);
        Node posListNode = nl.item(0);
        assertEquals(1, posListNode.getChildNodes().getLength());
        String content = posListNode.getChildNodes().item(0).getNodeValue();
        String[] ord = content.split("\\s+");
        assertEquals(expectedNumOrdinates, ord.length);
    }

    /** Checks that a given geometry element has an srsDimension attribute with an expected value */
    protected void checkDimension(Document doc, String tag, int expectedDim) {
        NodeList lsNL = doc.getElementsByTagNameNS(GML.NAMESPACE, tag);
        Node geomNode = lsNL.item(0);
        NamedNodeMap attrMap = geomNode.getAttributes();
        Node dimNode = attrMap.getNamedItem("srsDimension");
        assertNotNull(dimNode);
        String dimStr = dimNode.getChildNodes().item(0).getNodeValue();
        int dim = Integer.parseInt(dimStr);
        assertEquals(dim, expectedDim);
    }

    /*
     * To be overriden by subclasses that require the extended arc/surface bindings
     * enabled.
     */
    protected boolean enableExtendedArcSurfaceSupport() {
        return false;
    }

    /**
     * Return the gml:id of a Node (must be an Element).
     *
     * @return the gml:id
     */
    protected String getID(Node node) {
        return node.getAttributes()
                .getNamedItemNS(GML.NAMESPACE, GML.id.getLocalPart())
                .getNodeValue();
    }
}
