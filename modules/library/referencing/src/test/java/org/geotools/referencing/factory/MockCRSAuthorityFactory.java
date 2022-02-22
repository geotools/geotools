/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.text.Text;
import org.geotools.util.factory.Hints;
import org.geotools.util.factory.Hints.Key;
import org.geotools.util.factory.OptionalFactory;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

public abstract class MockCRSAuthorityFactory extends AbstractAuthorityFactory
        implements CRSAuthorityFactory, OptionalFactory {

    public static final String USE_MOCK_CRS_FACTORY =
            "org.geotools.referencing.crs.usemockcrsfactory";

    private final String name;

    private final Citation authority;

    private final Set<String> codes;

    protected MockCRSAuthorityFactory(String name, Citation authority, int priority, Hints hints) {
        super(priority);
        this.name = name;
        this.authority = authority;
        if (hints != null) {
            for (Entry<Object, Object> entry : hints.entrySet()) {
                this.hints.putIfAbsent((Key) entry.getKey(), entry.getValue());
            }
        }
        this.codes = Collections.singleton("EPSG:99999");
    }

    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type)
            throws FactoryException {
        return codes;
    }

    @Override
    public boolean isAvailable() {
        final String useMock = System.getProperty(USE_MOCK_CRS_FACTORY);
        return Boolean.parseBoolean(useMock);
    }

    @Override
    public IdentifiedObject createObject(String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        if (codes.contains(code)) {
            return new MockCRS();
        }
        return super.createObject(code);
    }

    @Override
    public InternationalString getDescriptionText(String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        return Text.text("description");
    }

    @Override
    public Citation getAuthority() {
        return authority;
    }

    class MockCRS implements CoordinateReferenceSystem {

        @Override
        public ReferenceIdentifier getName() {
            return new NamedIdentifier(authority, name);
        }

        @Override
        public Extent getDomainOfValidity() {
            throw new UnsupportedOperationException();
        }

        @Override
        public InternationalString getScope() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Collection<GenericName> getAlias() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<ReferenceIdentifier> getIdentifiers() {
            throw new UnsupportedOperationException();
        }

        @Override
        public InternationalString getRemarks() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toWKT() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public CoordinateSystem getCoordinateSystem() {
            throw new UnsupportedOperationException();
        }
    }
}
