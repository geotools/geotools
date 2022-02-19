package org.geotools.renderer.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotEquals;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.junit.Assert;
import org.junit.Test;

public class TestExplicitBoundsShape {

    @Test
    public void testConstructorOk() {
        try {

            Shape shape = createMock(Shape.class);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
            Assert.assertNotNull(target);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testConstructor_NullShape() throws Exception {

        try {
            new ExplicitBoundsShape(null);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    @Test
    public void testDelegate_AllMethods() {
        try {
            Shape shape = createMock(Shape.class);

            reset(shape);

            expect(shape.contains((Point2D) null)).andReturn(false);
            expect(shape.contains((Rectangle2D) null)).andReturn(false);
            expect(shape.contains(0.0, 0.0)).andReturn(false);
            expect(shape.contains(0.0, 0.0, 0.0, 0.0)).andReturn(false);
            expect(shape.getBounds()).andReturn(null);
            expect(shape.getBounds2D()).andReturn(null);
            expect(shape.getPathIterator(null)).andReturn(null);
            expect(shape.getPathIterator(null, 0.0)).andReturn(null);
            expect(shape.intersects(null)).andReturn(false);
            expect(shape.intersects(0.0, 0.0, 0.0, 0.0)).andReturn(false);

            replay(shape);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);

            Assert.assertFalse(target.contains((Point2D) null));
            Assert.assertFalse(target.contains((Rectangle2D) null));
            Assert.assertFalse(target.contains(0.0, 0.0));
            Assert.assertFalse(target.contains(0.0, 0.0, 0.0, 0.0));
            Assert.assertNull(target.getBounds());
            Assert.assertNull(target.getBounds2D());
            Assert.assertNull(target.getPathIterator(null));
            Assert.assertNull(target.getPathIterator(null, 0.0));
            Assert.assertFalse(target.intersects(null));
            Assert.assertFalse(target.intersects(0.0, 0.0, 0.0, 0.0));

            verify(shape);
        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testSetBounds() {
        try {

            Shape shape = createMock(Shape.class);

            reset(shape);

            Rectangle shapeBounds = new Rectangle(8, 7, 9, 3);
            expect(shape.getBounds()).andReturn(shapeBounds);
            expect(shape.getBounds2D()).andReturn(shapeBounds);

            replay(shape);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);

            Assert.assertEquals(shapeBounds, target.getBounds());
            Assert.assertEquals(shapeBounds, target.getBounds2D());

            Rectangle bounds = new Rectangle(1, 2, 5, 6);
            target.setBounds(bounds);

            Assert.assertEquals(bounds, target.getBounds());
            Assert.assertEquals(bounds, target.getBounds2D());

            verify(shape);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testEquals_Shape() {
        try {
            Shape shape = createMock(Shape.class);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);

            Assert.assertEquals(target, shape);

            Rectangle bounds = new Rectangle(1, 2, 5, 6);
            target.setBounds(bounds);

            assertNotEquals(target, shape);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testEquals_Null() {
        try {

            Shape shape = createMock(Shape.class);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);

            assertNotEquals(null, target);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testEquals_Object() {
        try {

            Shape shape = createMock(Shape.class);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);

            assertNotEquals(target, new Object());

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testEquals_SameObject() {
        try {
            Shape shape = createMock(Shape.class);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);

            Assert.assertEquals(target, target);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testEquals_OtherObject() {
        try {
            Shape shape = createMock(Shape.class);
            Shape shape2 = createMock(Shape.class);

            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
            ExplicitBoundsShape other = new ExplicitBoundsShape(shape);
            ExplicitBoundsShape another = new ExplicitBoundsShape(shape2);

            Assert.assertEquals(target, other);
            assertNotEquals(target, another);

            Rectangle bounds = new Rectangle(1, 1, 4, 4);
            target.setBounds(bounds);
            assertNotEquals(target, other);

            another.setBounds(bounds);
            assertNotEquals(target, another);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testHashCode() {
        try {
            Shape shape = createMock(Shape.class);
            ExplicitBoundsShape target = new ExplicitBoundsShape(shape);

            Assert.assertEquals(shape.hashCode(), target.hashCode());

            Rectangle bounds = new Rectangle(1, 1, 4, 4);
            target.setBounds(bounds);
            assertNotEquals(shape.hashCode(), target.hashCode());

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail("Should not throw " + e.getClass().getSimpleName());
        }
    }
}
