/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.operation.cross;

import java.io.File;
import java.net.URL;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.factory.epsg.FactoryUsingWKT;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;

public class TestAuthorityFactory extends FactoryUsingWKT {

    protected static final int PRIORITY = MAXIMUM_PRIORITY - 10;

    /** The TEST authority. */
    public static final CitationImpl TEST;

    static {
        CitationImpl citation = new CitationImpl("TEST");
        citation.getIdentifiers().add(new NamedIdentifier(null, "TEST"));
        citation.freeze();
        TEST = citation;
    }

    public TestAuthorityFactory() {
        this(null);
    }

    public TestAuthorityFactory(Hints hints) {
        super(hints, PRIORITY);
        this.hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, false);
    }

    @Override
    protected Citation[] getAuthorities() {
        return new Citation[] {TEST};
    }

    @Override
    protected URL getDefinitionsURL() {
        return URLs.fileToUrl(
                new File("./src/test/resources/org/geotools/referencing/operation/cross/test.properties"));
    }
}
