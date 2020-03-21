package org.geotools.gml3.v3_2;

import java.util.HashMap;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.test.XMLTestSupport;
import org.w3c.dom.Node;

public abstract class GML32TestSupport extends XMLTestSupport {

    static {
        HashMap namespaces = new HashMap();
        namespaces.put("gml", GML.NAMESPACE);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    protected Configuration createConfiguration() {
        return new GMLConfiguration(enableArcSurfaceSupport());
    }

    /*
     * To be overriden by subclasses that require the extended arc/surface bindings
     * enabled.
     */
    protected boolean enableArcSurfaceSupport() {
        return false;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        GML3MockData.setGML(GML.getInstance());

        registerNamespaceMapping("gml", GML.NAMESPACE);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        GML3MockData.setGML(null);
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
