/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools;

import static org.junit.Assert.fail;

/**
 * This is a little helper class to provide more rigorous checking of exceptions. It allows you to
 * set an expectation about exactly what the error message should say, instead of just checking the
 * type.
 *
 * <p>Just because you happened to get an IOException, for instance, it doesn't mean it was the
 * IOException you thought you should get - it could have been for a different reason, which means
 * your test wouldn't actually be verifying what you intended.
 */
public final class ExceptionChecker {
    /**
     * Assert that the provided exception's message is the same as the one provided. If the messages
     * don't match then junit's fail(...) method is called, if they do match then the original
     * exception is re-thrown to be handled by the calling code - usually by a Test(expected =
     * ***Exception.class) annotation.
     *
     * @param exception The exception you want to check.
     * @param expectedMessage The message you expect the exception to include.
     * @throws Exception Throws the original exception back if the message DOES match. This means
     *     you still need to have the annotation: Test(expected = ***Exception.class)
     */
    public static void assertExceptionMessage(Exception exception, String expectedMessage)
            throws Exception {
        String actualMessage = exception.getMessage();
        if (actualMessage.compareTo(expectedMessage) != 0) {
            fail(
                    String.format(
                            "Expected %s to say: '%s' but got: '%s'",
                            exception.getClass().getSimpleName(), expectedMessage, actualMessage));
        }

        throw exception;
    }
}
