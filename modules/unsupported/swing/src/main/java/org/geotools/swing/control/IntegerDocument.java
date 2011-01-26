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

package org.geotools.swing.control;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A document class to handle integer verification and parsing for
 * {@code JIntTextField} and other classes wishing to restrict user input
 * to integer values.
 *
 * @author Michael Bedward
 * @since 2.6.1
 * @source $URL$
 * @version $Id$
 */
class IntegerDocument extends PlainDocument {

    private int value;
    private boolean allowNegative;

    /**
     * Creates a new document that will allow both positive and negative values
     */
    public IntegerDocument() {
        this(true);
    }

    /**
     * Creates a new document
     *
     * @param allowNegative true to allow negative values; false to allow only positive values
     */
    public IntegerDocument(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    /**
     * Get the value corresponding to the current text
     * @return the current value
     */
    public int getValue() {
        return value;
    }

    /**
     * Test if this document allows negative values
     *
     * @return true if negative values are allowed; false if only positive values are allowed
     */
    public boolean getAllowsNegative() {
        return allowNegative;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertString(int offset, String text, AttributeSet attributes) throws BadLocationException {
        if (text != null) {
            String newText;

            if (getLength() == 0) {
                newText = text;

            } else {
                StringBuilder sb = new StringBuilder(getText(0, getLength()));
                sb.insert(offset, text);
                newText = sb.toString();
            }

            if (allowNegative && offset == 0 && newText.equals("-")) {
                value = 0;
            } else {
                value = checkInput(newText, offset);
            }
            super.insertString(offset, text, attributes);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int offset, int length) throws BadLocationException {
        final int curLen = getLength();
        final String currentContent = getText(0, curLen);
        final String newText = currentContent.substring(0, offset) +
                currentContent.substring(length + offset, curLen);

        if (allowNegative && offset == 0 && newText.equals("-")) {
            value = 0;
        } else {
            value = checkInput(newText, offset);
        }
        super.remove(offset, length);
    }

    /**
     * Check the proposed text and, if it is valid, parse it as an integer value.
     *
     * @param proposedText the proposed text value
     * @param offset position in the document
     * @return the parsed integer value
     *
     * @throws BadLocationException if the string did not represent a value integer
     */
    public int checkInput(String proposedText, int offset) throws BadLocationException {
        int newValue = 0;

        if (proposedText != null && proposedText.length() > 0) {
            try {
                newValue = Integer.parseInt(proposedText);
            } catch (NumberFormatException ex) {
                throw new BadLocationException(proposedText, offset);
            }
        }

        return newValue;
    }
}
