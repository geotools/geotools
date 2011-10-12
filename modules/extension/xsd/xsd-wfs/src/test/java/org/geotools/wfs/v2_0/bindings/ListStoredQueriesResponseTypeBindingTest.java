package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs20.ListStoredQueriesResponseType;
import net.opengis.wfs20.StoredQueryListItemType;
import net.opengis.wfs20.TitleType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ListStoredQueriesResponseTypeBindingTest extends WFSTestSupport {

    public void testEncode() throws Exception {
        Wfs20Factory factory = Wfs20Factory.eINSTANCE;
        
        StoredQueryListItemType sqli = factory.createStoredQueryListItemType();
        sqli.setId("fooId");
        
        TitleType title = factory.createTitleType();
        title.setValue("fooTitle");
        sqli.getTitle().add(title);
        
        sqli.getReturnFeatureType().add(new QName("http://foo.org", "fooName", "foo"));
        
        ListStoredQueriesResponseType lsqr = factory.createListStoredQueriesResponseType();
        lsqr.getStoredQuery().add(sqli);

        Document dom = encode(lsqr, WFS.ListStoredQueriesResponse);
        
        Element e = getElementByQName(dom, WFS.StoredQuery);
        assertEquals("fooId", e.getAttribute("id"));
        
        assertNotNull(getElementByQName(dom, WFS.Title));
        assertNotNull(getElementByQName(e, new QName(WFS.NAMESPACE, "ReturnFeatureType")));
    }
}
