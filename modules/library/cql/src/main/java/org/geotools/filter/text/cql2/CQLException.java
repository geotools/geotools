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
package org.geotools.filter.text.cql2;

import org.geotools.filter.text.commons.IToken;
import org.geotools.filter.text.generated.parsers.ParseException;


/**
 * This exception is produced when the cql input string has syntax errors.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * 
 *
 *
 * @source $URL$
 * @version $Id$
 * @since 2.4
 */
public class CQLException extends ParseException {
    /** for interoperability */
    private static final long serialVersionUID = 8873756073711225699L;

    protected Throwable cause = null;
    private String cqlSource = null;
    private IToken currentToken = null;

    /**
     * New instance of CQLException
     *
     * @param message   exception description
     * @param token     current token
     * @param cause     the cause
     * @param cqlSource string analyzed
     */
    public CQLException(final String message, final IToken token, final Throwable cause, final String cqlSource) {
        super(message);
        
        assert message != null : "message can not be null";
        assert cqlSource != null : "cqlSource can not be null";

        this.currentToken = token;
        this.cause = cause;
        this.cqlSource = cqlSource;
    }

    /**
     * New instance of CQLException
     * 
     * @param message   exception description
     * @param token     current token
     * @param cqlSource analyzed string
     */
    public CQLException(final String message, final IToken token, final String cqlSource) {
        this(message, token, null, cqlSource);
    }

    /**
     * New instance of CQLException
     * @param message
     * @param cqlSource
     */
    public CQLException(final String message, final String cqlSource) {
        this(message, null, null, cqlSource);
    }

    /**
     * New instance of CQLException
     * 
     * @param message
     */
    public CQLException(final String message) {
        this(message, null, null, "");
    }

    /**
     * Returns the cause
     *
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * Returns the exception message
     *
     * @return a message
     */
    public String getMessage() {

        return super.getMessage() + " Parsing : " + this.cqlSource + ".";
    }

    /**
     * Returns the syntax error presents in the last sequence of characters analyzed.
     * 
     * @return the syntax error
     */
    public String getSyntaxError() {

        if(currentToken == null){
            return getMessage();
        }

        // builds two lines the first has the source string, the second has
        // the pointer to syntax error.

        // First Line
        StringBuffer msg = new StringBuffer(this.cqlSource);
        msg.append('\n');

        // Second Line
        // searches the last token recognized 
        IToken curToken = this.currentToken;

        while (curToken.hasNext() )
            curToken = curToken.next();

        // add the pointer to error
        int column = curToken.beginColumn() - 1;

        for (int i = 0; i < column; i++) {
            msg.append(' ');
        }

        msg.append('^').append('\n');
        msg.append(super.getMessage());

        return msg.toString();
    }
}
