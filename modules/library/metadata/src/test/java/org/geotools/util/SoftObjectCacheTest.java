package org.geotools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests the SoftObjectCache with simple tests.
 * 
 * @author Emily Gouge (Refractions Research)
 *
 * @source $URL$
 */
public class SoftObjectCacheTest {
	
	private Integer key1 = 1;
	private Integer key2 = 2;
	private String value1 = new String("value 1");
	private String value2 = new String("value 2");
	private String value3 = new String("value 3");

	@Test
	public void testSimple() {
		ObjectCache cache = new SoftObjectCache();
		assertNotNull(cache);

		assertEquals(null, cache.get(key1));

		cache.writeLock(key1);
		cache.put(key1, value1);
		cache.writeUnLock(key1);
		assertEquals(value1, cache.get(key1));

		assertEquals(null, cache.get(key2));

		// test getKeys()
		assertEquals(1, cache.getKeys().size());
		assertEquals(key1, cache.getKeys().iterator().next());
	}

	@Test
	public void testRemoveSimple() {
		ObjectCache cache = new SoftObjectCache();
		assertNotNull(cache);

		assertEquals(null, cache.get(key1));

		cache.writeLock(key1);
		cache.put(key1, value1);
		cache.writeUnLock(key1);
		assertEquals(value1, cache.get(key1));

		assertEquals(null, cache.get(key2));

		// test getKeys()
		assertEquals(1, cache.getKeys().size());
		assertEquals(key1, cache.getKeys().iterator().next());

		// remove the key
		cache.remove(key1);
		assertEquals(0, cache.getKeys().size());
	}

	@Test
	public void testConcurrent() throws InterruptedException {
		ObjectCache cache = new SoftObjectCache();

		// lock the cache as if we were writing
		cache.writeLock(key1);

		// create another thread which starts writing and blocks
		Runnable thread1 = new WriterThread(cache);
		Thread t1 = new Thread(thread1);
		t1.start();
		Thread.yield();

		// write
		cache.put(key1, value2);

		// check that the write thread was blocked
		Object[] values = ((WriterThread) thread1).getValue();
		assertEquals(null, values);
		assertEquals(value2, cache.peek(key1));
		assertEquals(1, cache.getKeys().size());

		// check that a separate write thread can get through
		cache.writeLock(key2);
		cache.put(key2, value3);
		cache.writeUnLock(key2);
		assertEquals(2, cache.getKeys().size());

		// unlock
		try {
			cache.writeUnLock(key1);
		} catch (Exception e) {
			fail("couldn't unlock");
		}

		// check that the write thread is unblocked
		t1.join();
		values = ((WriterThread) thread1).getValue();
		assertNotNull(values);
		assertEquals(value1, values[0]);

		assertEquals(2, cache.getKeys().size());
		assertTrue(cache.getKeys().contains(key1));
		assertTrue(cache.getKeys().contains(key2));
	}

	private class WriterThread implements Runnable {

		ObjectCache cache = null;
		Object[] values = null;

		public WriterThread(ObjectCache cache) {
			this.cache = cache;
		}

		public void run() {
			try {
				cache.writeLock(key1);
				cache.put(key1, value1);
				values = new Object[] { cache.get(key1) };
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					cache.writeUnLock(key1);
				} catch (Exception e) {
					fail("couldn't unlock");
				}
			}
		}

		public Object[] getValue() {
			return values;
		}
	}
}
