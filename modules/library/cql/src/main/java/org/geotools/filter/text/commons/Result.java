/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.commons;

import org.geotools.filter.text.commons.IToken;


/**
 * Maintains the result of building process.
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 * @since 2.4
 * @author Mauricio Pazos - Axios Engineering
 * @author Gabriel Roldan - Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 */
public final class Result {
    private int nodeType = 0;
    private Object built = null;
    private IToken token = null;

    public Result(Object built, IToken token, int nodeType) {
        this.built = built;
        this.token = token;
        this.nodeType = nodeType;
    }

    public Result(Object built2, org.geotools.filter.text.generated.parsers.Token token2,
            int type) {
    }

    public String toString() {
        assert this.token != null;

        return "Result [TOKEN(" + this.token.toString() + ");" + "BUILT(" + built + "); NODE_TYPE("
        + new Integer(nodeType) + ") ]";
    }

    public final Object getBuilt() {
        return this.built;
    }

    public final int getNodeType() {
        return this.nodeType;
    }

    public final IToken getToken() {
        return this.token;
    }
}
