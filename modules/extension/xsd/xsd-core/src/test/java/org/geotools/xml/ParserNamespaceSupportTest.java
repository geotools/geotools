package org.geotools.xml;

import junit.framework.TestCase;
import org.xml.sax.helpers.NamespaceSupport;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ParserNamespaceSupportTest extends TestCase {

    public void testLookup() {
        ParserNamespaceSupport nsSupport = new ParserNamespaceSupport();
        assertNull(nsSupport.getURI("foo"));

        NamespaceSupport delegate = new NamespaceSupport();
        delegate.declarePrefix("foo", "http://foo.org");
        nsSupport.add(delegate);

        assertEquals("http://foo.org", nsSupport.getURI("foo"));

        nsSupport.declarePrefix("foo", "http://bar.org");
        assertEquals("http://bar.org", nsSupport.getURI("foo"));
    }

    public void testGetPrefixes() {
        ParserNamespaceSupport nsSupport = new ParserNamespaceSupport();
        nsSupport.declarePrefix("foo", "http://foo.org");

        NamespaceSupport delegate = new NamespaceSupport();
        delegate.declarePrefix("bar", "http://bar.org");

        nsSupport.add(delegate);

        List<String> prefixes = list(nsSupport.getPrefixes());
        assertTrue(prefixes.contains("foo"));
        assertTrue(prefixes.contains("bar"));

        assertTrue(prefixes.indexOf("foo") < prefixes.indexOf("bar"));
    }

    List list(Enumeration e) {
        List<Object> l = new ArrayList<Object>();
        while(e.hasMoreElements()) {
            l.add(e.nextElement());
        }
        return l;
    }
}
