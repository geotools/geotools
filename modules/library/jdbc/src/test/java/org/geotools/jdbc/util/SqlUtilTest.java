package org.geotools.jdbc.util;

import junit.framework.TestCase;

public class SqlUtilTest extends TestCase {

    public void testQuoteIdentifier() {
        assertEquals("\"abc\"", SqlUtil.quoteIdentifier("abc"));
        assertEquals("\"a\"\"b\"", SqlUtil.quoteIdentifier("a\"b"));
    }

    public void testQuoteIdentifiers() {
        assertEquals("\"abc\"", SqlUtil.quoteIdentifiers("abc"));
        assertEquals("\"abc\".\"def\"", SqlUtil.quoteIdentifiers("abc", "def"));
        assertEquals("\"abc\".\"def\".\"ghi\"", SqlUtil.quoteIdentifiers("abc", "def", "ghi"));
    }
}
