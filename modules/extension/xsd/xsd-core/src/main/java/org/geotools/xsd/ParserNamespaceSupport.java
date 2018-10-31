/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014
 *    , Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * NamespaceSupport implementation that allows additional NamespaceSupport objects to be part of the
 * look up process.
 */
public class ParserNamespaceSupport extends NamespaceSupport {

    List<NamespaceSupport> others = new ArrayList<NamespaceSupport>();

    @Override
    public String getURI(String prefix) {
        String uri = super.getURI(prefix);
        if (uri == null) {
            Iterator<NamespaceSupport> it = others.iterator();
            while (uri == null && it.hasNext()) {
                uri = it.next().getURI(prefix);
            }
        }
        return uri;
    }

    @Override
    public String getPrefix(String uri) {
        String prefix = super.getPrefix(uri);
        if (prefix == null) {
            Iterator<NamespaceSupport> it = others.iterator();
            while (prefix == null && it.hasNext()) {
                prefix = it.next().getPrefix(uri);
            }
        }
        return prefix;
    }

    @Override
    public Enumeration getPrefixes() {
        return new CompositeEnumeration(super.getPrefixes()) {
            @Override
            Enumeration next(NamespaceSupport nsSupport) {
                return nsSupport.getPrefixes();
            }
        };
    }

    @Override
    public Enumeration getPrefixes(final String uri) {
        return new CompositeEnumeration(super.getPrefixes(uri)) {
            @Override
            Enumeration next(NamespaceSupport nsSupport) {
                return nsSupport.getPrefixes(uri);
            }
        };
    }

    @Override
    public Enumeration getDeclaredPrefixes() {
        return new CompositeEnumeration(super.getDeclaredPrefixes()) {
            @Override
            Enumeration next(NamespaceSupport nsSupport) {
                return nsSupport.getDeclaredPrefixes();
            }
        };
    }

    public void add(NamespaceSupport nsSupport) {
        others.add(nsSupport);
    }

    abstract class CompositeEnumeration implements Enumeration {

        Enumeration e;
        Iterator<NamespaceSupport> it;

        CompositeEnumeration(Enumeration e) {
            this.e = e;
            this.it = others.iterator();
        }

        @Override
        public boolean hasMoreElements() {
            if (e.hasMoreElements()) {
                return true;
            }

            while (it.hasNext() && !e.hasMoreElements()) {
                e = next(it.next());
            }

            return e.hasMoreElements();
        }

        @Override
        public Object nextElement() {
            return e.nextElement();
        }

        abstract Enumeration next(NamespaceSupport nsSupport);
    }
}
