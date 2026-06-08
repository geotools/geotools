/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Level;
import org.geotools.util.logging.Logging;

/**
 * Utility class for managing memory mapped buffers.
 *
 * @since 2.0
 * @version $Id$
 * @author Andrea Aimes
 */
public final class NIOUtilities {

    /** The buffer cache, partitioned by buffer size and fully concurrent */
    static Map<Integer, Queue<Object>> cache = new ConcurrentHashMap<>();

    /**
     * The maximum size of the hard reference cache (the soft one can be unbounded, the GC will regulate its size
     * according to the memory pressure)
     */
    static int maxCacheSize = 2 * 1024 * 1024;

    /** The current hard cache size */
    static AtomicInteger hardCacheSize = new AtomicInteger(0);

    /** A zero filled byte used to quickly reset the byte buffers */
    static final byte[] ZEROES = new byte[4096];

    /** {@code true} if a warning has already been logged. */
    private static boolean warned = false;

    /** Wheter direct buffers usage is enabled, or not */
    private static boolean directBuffersEnabled = true;

    /** A function to implement {@link #clean(java.nio.ByteBuffer) }. */
    private static final Function<ByteBuffer, Boolean> CLEANUP_FUNCTION;

    static {
        String directBuffers = System.getProperty("geotools.nioutilities.direct", "true");
        directBuffersEnabled = "TRUE".equalsIgnoreCase(directBuffers);

        // set the cleanup function based on the system property.
        String cleanup = System.getProperty("geotools.nioutilities.cleanup", "");
        if (cleanup.equalsIgnoreCase("memorysegment")) {
            CLEANUP_FUNCTION = memorySegmentCleanup().orElseThrow();
        } else if (cleanup.equalsIgnoreCase("unsafe")) {
            CLEANUP_FUNCTION = cleanupAfterJdk8(true).orElseThrow();
        } else if (cleanup.equalsIgnoreCase("noop")) {
            CLEANUP_FUNCTION = NIOUtilities::noopClean;
        } else {
            // user hasn't made a choice (or an unknown choice) so we decide what's best
            // cleanupAfterJdk8 is the best clean up available currently but may trigger deprecated API warnings
            // on newer JVMs. If you need to stop these warnings more than you need the better cleanup then
            // setting skipUnsafeVersionCheck to false will prevent cleanupAfterJdk8 being used on these JVMs
            CLEANUP_FUNCTION = cleanupAfterJdk8("TRUE"
                            .equalsIgnoreCase(
                                    System.getProperty("geotools.nioutilities.skipUnsafeVersionCheck", "true")))
                    .or(() -> memorySegmentCleanup())
                    .orElse(NIOUtilities::noopClean);
        }
    }

    /** Wheter direct buffers are used, or not (defaults to true) */
    public static boolean isDirectBuffersEnabled() {
        return directBuffersEnabled;
    }

    /**
     * If the flag is true {@link #allocate(int)} will allocate a direct buffer,, otherwise heap buffers will be used.
     * Direct buffers are normally faster, but their cleanup is platform dependent and not guaranteed, under high load
     * and in combination with some garbage collectors that might result in a JVM crash (failure to perform native
     * memory allocation)
     */
    public static void setDirectBuffersEnabled(boolean directBuffersEnabled) {
        NIOUtilities.directBuffersEnabled = directBuffersEnabled;
    }

    /** Do not allows instantiation of this class. */
    private NIOUtilities() {}

    /**
     * Sets the maximum byte buffer cache size, in bytes (set to 0 to only use soft references in the case, a positive
     * value will make the cache use hard references up to the max cache size)
     */
    public static void setMaxCacheSize(int maxCacheSize) {
        NIOUtilities.maxCacheSize = maxCacheSize;
    }

    /**
     * Allocates and returns a {@link ByteBuffer}. The buffer capacity will generally be greater than of two that can
     * contain the specified limit, the buffer limit will be set at the specified value. The buffers are pooled, so
     * remember to call {@link #clean(ByteBuffer, false)} to return the buffer to the pool.
     */
    public static ByteBuffer allocate(int size) {
        // look for a free cached buffer that has still not been garbage collected
        Queue<Object> buffers = getBuffers(size);
        Object sr = null;
        while ((sr = buffers.poll()) != null) {
            ByteBuffer buffer = null;
            // what did we get, a soft or a hard reference?
            if (sr instanceof BufferSoftReference reference) {
                buffer = reference.get();
            } else {
                // we're removing a hard reference from the cache, lower the usage figure
                buffer = (ByteBuffer) sr;
                hardCacheSize.addAndGet(-buffer.capacity());
            }
            // clean up the buffer and return it
            if (buffer != null) {
                buffer.clear();
                return buffer;
            }
        }

        // we could not find one, then allocated it
        if (directBuffersEnabled) {
            return ByteBuffer.allocateDirect(size);
        } else {
            return ByteBuffer.allocate(size);
        }
    }

    /** Returns the buffer queue associated to the specified size */
    @SuppressWarnings("PMD.DoubleCheckedLocking")
    private static Queue<Object> getBuffers(int size) {
        Queue<Object> result = cache.get(size);
        if (result == null) {
            // this is the only synchronized bit, we don't want multiple queues
            // to be created. result == null will be true only at the application startup
            // for the common byte buffer sizes
            // TODO: replace by cache.computeIfAbsent(...)
            synchronized (cache) {
                result = cache.get(size);
                if (result == null) {
                    result = new ConcurrentLinkedQueue<>();
                    cache.put(size, result);
                }
            }
        }
        return result;
    }

    /**
     * Depending on the type of buffer different cleanup action will be taken:
     *
     * <ul>
     *   <li>if the buffer is memory mapped (as per the specified parameter) the effect is the same as
     *       {@link #clean(ByteBuffer)}
     *   <li>if the buffer is not memory mapped it will be returned to the buffer cache
     * </ul>
     */
    public static boolean clean(final ByteBuffer buffer, boolean memoryMapped) {
        if (memoryMapped) {
            return clean(buffer);
        } else {
            if (returnToCache(buffer)) {
                return true;
            } else {
                return clean(buffer);
            }
        }
    }

    /**
     * Really closes a {@code MappedByteBuffer} without the need to wait for garbage collection. Any problems with
     * closing a buffer on Windows (the problem child in this case) will be logged as {@code SEVERE} to the logger of
     * the package name. To force logging of errors, set the System property "org.geotools.io.debugBuffer" to "true".
     *
     * @param buffer The buffer to close.
     * @return true if the operation was successful, false otherwise.
     * @see java.nio.MappedByteBuffer
     */
    public static boolean clean(final ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect()) {
            return true;
        }
        return CLEANUP_FUNCTION.apply(buffer);
    }

    /**
     * Returns a cleanup function that uses <code>java.lang.foreign.MemorySegment</code> which was added in JDK22.
     *
     * <p>Note: memory segment clean up only works for direct mapped buffers. Other buffers are not cleaned at all.
     *
     * <p>This cleanup is better than nothing but not as good as {@link #cleanupAfterJdk8(boolean) }. In the future it
     * might be possible to allocate direct non-mapped buffers via an Arena which can be cleaned up by closing the Arena
     * that the buffers were allocated from.
     */
    private static Optional<Function<ByteBuffer, Boolean>> memorySegmentCleanup() {
        try {
            // MemorySegment is a new API added in JDK22. If this class cannot be found then the class
            // loader will throw an exception which will be caught below and the empty optional returned.
            final Class<?> memorySegmentClass = Class.forName("java.lang.foreign.MemorySegment");
            final Method ofBufferMethod = memorySegmentClass.getMethod("ofBuffer", Buffer.class);
            final Method isMappedMethod = memorySegmentClass.getMethod("isMapped");
            final Method unloadMethod = memorySegmentClass.getMethod("unload");

            return Optional.of((byteBuffer) -> {
                try {
                    Object memorySegment = ofBufferMethod.invoke(null, byteBuffer);
                    // only mapped memory segments can be unloaded
                    if (Boolean.TRUE.equals(isMappedMethod.invoke(memorySegment))) {
                        unloadMethod.invoke(memorySegment);
                        return Boolean.TRUE;
                    }

                    return Boolean.FALSE;
                } catch (Exception e) {
                    if (isLoggable()) {
                        log(e, byteBuffer);
                    }
                }
                return Boolean.FALSE;
            });
        } catch (Exception e) {
            // There was some problem getting the MemorySegment, most likely because the current JRE
            // is < 22 so MemorySegment cannot be found. no need to log this is expected
            return Optional.empty();
        }
    }

    /** a clean function that does nothing except leave cleanup to the JVM */
    private static Boolean noopClean(ByteBuffer buffer) {
        return false;
    }

    /**
     * Returns a clean up function that uses the <code>com.sun.Unsafe</code> API.
     *
     * <p>Note, by default this cleanup will not be returned if the JVM version is 24 or higher as this will output
     * terminally deprecated warnings.
     *
     * @param skipVersionCheck when true skip the JVM version check
     */
    private static Optional<Function<ByteBuffer, Boolean>> cleanupAfterJdk8(boolean skipVersionCheck) {
        if (!skipVersionCheck && Runtime.version().feature() >= 24) {
            // The Unsafe API has been marked as terminally deprecated and JVM's >= 24 output warnings
            // should it be used. So
            return Optional.empty();
        }
        try {
            // https://bugs.openjdk.java.net/browse/JDK-8171377
            // https://bugs.openjdk.java.net/browse/JDK-4724038
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            Object theUnsafe = theUnsafeField.get(null);
            Method invokeCleanerMethod = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);

            return Optional.of((buffer) -> {
                boolean success = false;
                try {
                    invokeCleanerMethod.invoke(theUnsafe, buffer);

                    success = true;
                } catch (Exception e) {
                    if (isLoggable()) {
                        log(e, buffer);
                    }
                }
                return success;
            });
        } catch (Exception e) {
            // There was some problem getting the Unsafe API. This won't be expected until it is removed
            // from future JDK versions
            return Optional.empty();
        }
    }

    public static boolean returnToCache(final ByteBuffer buffer) {
        // is the buffer cacheable? There are some blessed sizes we use over and over, for the
        // rest buffer only powers of two
        final int capacity = buffer.capacity();
        if (capacity != 100 && capacity != 13 && capacity != 16000) {
            int size = (int) Math.pow(2, Math.ceil(Math.log(capacity) / Math.log(2)));
            if (size != capacity) {
                return false;
            }
        }

        // clean up the buffer -> we need to zero out its contents as if it was just
        // created or some shapefile tests will start failing
        buffer.clear();
        buffer.order(ByteOrder.BIG_ENDIAN);

        // set the buffer back in the cache, either as a soft reference or as
        // a hard one depending on whether we're past the hard cache or not
        Queue<Object> buffers = cache.get(capacity);
        if (hardCacheSize.get() > maxCacheSize) {
            buffers.add(new BufferSoftReference(buffer));
        } else {
            hardCacheSize.addAndGet(capacity);
            buffers.add(buffer);
        }
        return true;
    }

    /** Checks if a warning message should be logged. */
    private static synchronized boolean isLoggable() {
        try {
            return !warned
                    && (Boolean.getBoolean("org.geotools.io.debugBuffer")
                            || System.getProperty("os.name").indexOf("Windows") >= 0);
        } catch (SecurityException exception) {
            // The utilities may be running in an Applet, in which case we
            // can't read properties. Assumes we are not in debugging mode.
            return false;
        }
    }

    /** Logs a warning message. */
    private static synchronized void log(final Exception e, final ByteBuffer buffer) {
        warned = true;
        String message = "Error attempting to close a mapped byte buffer : "
                + buffer.getClass().getName()
                + "\n JVM : "
                + System.getProperty("java.version")
                + ' '
                + System.getProperty("java.vendor");
        Logging.getLogger(NIOUtilities.class).log(Level.SEVERE, message, e);
    }
}
