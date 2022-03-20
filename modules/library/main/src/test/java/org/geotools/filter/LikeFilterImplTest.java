package org.geotools.filter;

import static org.junit.Assert.fail;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsLike;

public class LikeFilterImplTest {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

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
    public void testEmptyEscape() {
        try {
            PropertyIsLike l =
                    ff.like(
                            ff.literal(
                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"),
                            "(a+)+",
                            "*",
                            "x",
                            "");
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
