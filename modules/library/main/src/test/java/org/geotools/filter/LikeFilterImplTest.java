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
package org.geotools.filter;

import static org.junit.Assert.fail;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

public class LikeFilterImplTest {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testEmptyWildCard() {
        try {
            PropertyIsLike l = ff.like(ff.literal("foo"), "foo", "", "?", "-");
            l.evaluate(null);
            fail();
        } catch (IllegalArgumentException e) {
            // as expected, no OOM anylonger
        }
    }

    @Test
    public void testEmptySingleWildCard() {
        try {
            PropertyIsLike l = ff.like(ff.literal("foo"), "foo", "*", "", "-");
            l.evaluate(null);
            fail();
        } catch (IllegalArgumentException e) {
            // as expected, no OOM anylonger
        }
    }

    @Test
    public void testReDOS1() {
        try {
            PropertyIsLike l =
                    ff.like(
                            ff.function(
                                    "strTrim",
                                    ff.literal(
                                            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab")),
                            "$",
                            "*",
                            "?",
                            "(a+)+");
            l.evaluate(null);
            fail();
        } catch (IllegalArgumentException e) {
            // as expected, no OOM anylonger
        }
    }

    @Test
    public void testReDOS2() {
        try {
            PropertyIsLike l =
                    ff.like(
                            ff.literal(
                                    "hchcchicihcchciiicichhcichcihcchiihichiciiiihhcchicchhcihchcihiihciichhccciccichcichiihcchcihhicchcciicchcccihiiihhihihihichicihhcciccchihhhcchichchciihiicihciihcccciciccicciiiiiiiiicihhhiiiihchccchchhhhiiihchihcccchhhiiiiiiiicicichicihcciciihichhhhchihciiihhiccccccciciihhichiccchhicchicihihccichicciihcichccihhiciccccccccichhhhihihhcchchihihiihhihihihicichihiiiihhhhihhhchhichiicihhiiiiihchccccchichci"),
                            "$",
                            "*",
                            "?",
                            "(h|h|ih(((i|a|c|c|a|i|i|j|b|a|i|b|a|a|j))+h)ahbfhba|c|i)*");
            l.evaluate(null);
            fail();
        } catch (IllegalArgumentException e) {
            // as expected, no OOM anylonger
        }
    }
}
