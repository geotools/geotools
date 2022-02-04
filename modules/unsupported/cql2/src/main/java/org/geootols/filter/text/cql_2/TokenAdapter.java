/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geootols.filter.text.cql_2;

import org.geotools.filter.text.commons.IToken;
import org.geotools.filter.text.cql_2.parsers.Token;

/**
 * Copy from gt-cql, as it needs to adapt a different Token class. Can probably be removed once we
 * merge gt-cql2-text with gt-cql
 */
class TokenAdapter implements IToken {

    private final Token cqlToken;

    private TokenAdapter(Token token) {
        this.cqlToken = token;
    }

    public static IToken newAdapterFor(Token token) {
        return new TokenAdapter(token);
    }

    @Override
    public String toString() {
        return this.cqlToken.toString();
    }

    @Override
    public boolean hasNext() {
        return this.cqlToken.next != null;
    }

    @Override
    public IToken next() {
        return newAdapterFor(this.cqlToken.next);
    }

    @Override
    public int beginColumn() {
        return this.cqlToken.beginColumn;
    }

    @Override
    public int endColumn() {
        return this.cqlToken.endColumn;
    }

    // Doe not really seem in use?
    @Override
    public org.geotools.filter.text.generated.parsers.Token getAdapted() {
        throw new UnsupportedOperationException();
    }
}
