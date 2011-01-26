package org.geotools.renderer.style.shape;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

public class TestExplicitBoundsShape extends TestCase
{
	
	public void testConstructorOk() {
		try {
			
			Shape shape = createMock(Shape.class);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape );
			assertNotNull(target);
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}
	
	public void testConstructor_NullShape() throws Exception {
		
		try
		{
			new ExplicitBoundsShape(null);
			fail("Should throw IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			// ok
		}
	}
	
	public void testDelegate_AllMethods()
	{
		try
		{
			Shape shape = createMock(Shape.class);
			
			reset(shape);
			
			expect(shape.contains((Point2D)null)).andReturn(false);
			expect(shape.contains((Rectangle2D)null)).andReturn(false);
			expect(shape.contains(0.0, 0.0)).andReturn(false);
			expect(shape.contains(0.0, 0.0, 0.0, 0.0)).andReturn(false);
			expect(shape.getBounds()).andReturn(null);
			expect(shape.getBounds2D()).andReturn(null);
			expect(shape.getPathIterator(null)).andReturn(null);
			expect(shape.getPathIterator(null, 0.0)).andReturn(null);
			expect(shape.intersects(null)).andReturn(false);
			expect(shape.intersects(0.0, 0.0, 0.0, 0.0)).andReturn(false);
			
			replay(shape);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape );
			
			assertFalse(target.contains((Point2D)null));
			assertFalse(target.contains((Rectangle2D)null));
			assertFalse(target.contains(0.0, 0.0));
			assertFalse(target.contains(0.0, 0.0, 0.0, 0.0));
			assertNull(target.getBounds());
			assertNull(target.getBounds2D());
			assertNull(target.getPathIterator(null));
			assertNull(target.getPathIterator(null, 0.0));
			assertFalse(target.intersects(null));
			assertFalse(target.intersects(0.0, 0.0, 0.0, 0.0));
			
			verify(shape);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}
		
	}
	
	
	
	public void testSetBounds() {
		try {
			
			Shape shape = createMock(Shape.class);
			
			reset(shape);
			
			Rectangle shapeBounds = new Rectangle(8,7,9,3);
			expect(shape.getBounds()).andReturn(shapeBounds );
			expect(shape.getBounds2D()).andReturn(shapeBounds );
			
			replay(shape);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
			
			assertEquals(shapeBounds , target.getBounds()) ;
			assertEquals(shapeBounds , target.getBounds2D());
			
			
			Rectangle bounds = new Rectangle(1,2,5,6);
			target.setBounds(bounds );
			
			assertEquals(bounds, target.getBounds());
			assertEquals(bounds, target.getBounds2D());	
			
			verify(shape);
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}
	
	
	
	public void testEquals_Shape() {
		try 
		{
			Shape shape = createMock(Shape.class);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
			
			assertTrue(target.equals(shape));
			
			Rectangle bounds = new Rectangle(1,2,5,6);
			target.setBounds(bounds);
			
			assertFalse(target.equals(shape));
	
		} 
		catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}
	
	
	
	public void testEquals_Null() {
		try 
		{
			
			Shape shape = createMock(Shape.class);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
			
			assertFalse(target.equals(null));
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}
	
	
	public void testEquals_Object() {
		try 
		{
			
			Shape shape = createMock(Shape.class);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
			
			assertFalse(target.equals(new Object()));
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}
	
	
	
	public void testEquals_SameObject() {
		try 
		{
			Shape shape = createMock(Shape.class);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
			
			assertTrue(target.equals(target));
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}
	
	public void testEquals_OtherObject() {
		try 
		{
			Shape shape = createMock(Shape.class);
			Shape shape2 = createMock(Shape.class);
			
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
			ExplicitBoundsShape other = new ExplicitBoundsShape(shape);
			ExplicitBoundsShape another = new ExplicitBoundsShape(shape2);
			
			assertTrue(target.equals(other));
			assertFalse(target.equals(another));
			
			Rectangle bounds = new Rectangle(1,1,4,4);
			target.setBounds(bounds );
			assertFalse(target.equals(other));
			
			
			another.setBounds(bounds);
			assertFalse(target.equals(another));
			
			
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}
	
	
	
	
	public void testHashCode() {
		try 
		{
			Shape shape = createMock(Shape.class);
			ExplicitBoundsShape target = new ExplicitBoundsShape(shape);
			
			assertEquals(shape.hashCode(), target.hashCode());
			
			Rectangle bounds = new Rectangle(1,1,4,4);
			target.setBounds(bounds);
			assertFalse(shape.hashCode() == target.hashCode());
			
		} catch (Throwable e) {
			e.printStackTrace();
			fail("Should not throw " + e.getClass().getSimpleName());
		}

	}	
	
	

}
