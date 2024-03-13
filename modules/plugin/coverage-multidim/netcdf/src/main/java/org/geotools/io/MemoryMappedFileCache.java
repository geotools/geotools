/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
/*
 * BSD 3-Clause License
 *
 * Copyright (c) 1998-2023, University Corporation for Atmospheric Research/Unidata
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geotools.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.nc2.dataset.DatasetUrl;
import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateFormatter;
import ucar.nc2.util.CancelTask;
import ucar.nc2.util.cache.FileCacheIF;
import ucar.nc2.util.cache.FileCacheable;
import ucar.nc2.util.cache.FileFactory;
import ucar.unidata.util.StringUtil2;

@SuppressWarnings("unchecked")
@ThreadSafe
/**
 * {@link FileCacheIF} implementation based on {@link ucar.nc2.util.cache.FileCache} from Unidata,
 * using MemoryMappedRandomAccessFile instead of standard RandomAccessFile. FileCache cannot be
 * subclassed since some fields are private and are needed by implementation.
 */
public class MemoryMappedFileCache implements FileCacheIF {
    protected static final Logger log = LoggerFactory.getLogger(MemoryMappedFileCache.class);
    protected static final Logger cacheLog = LoggerFactory.getLogger("MemoryMappedFileCacheLogger");
    private static Timer timer;
    private static Object lock = new Object();
    protected String name;
    protected final int softLimit;
    protected final int minElements;
    protected final int hardLimit;
    protected final long period;
    private final AtomicBoolean disabled;
    protected final AtomicBoolean hasScheduled;
    protected final ConcurrentHashMap<Object, CacheElement> cache;
    protected final ConcurrentHashMap<FileCacheable, CacheElement.CacheFile> files;
    protected final AtomicInteger cleanups;
    protected final AtomicInteger hits;
    protected final AtomicInteger miss;
    protected ConcurrentHashMap<Object, CacheTracking> track;
    protected boolean trackAll = false;

    public static void shutdown() {
        synchronized (lock) {
            if (timer != null) {
                timer.cancel();
                cacheLog.info("MemoryMappedFileCache.shutdown called%n");
            }

            timer = null;
        }
    }

    private static void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        synchronized (lock) {
            if (timer == null) {
                timer = new Timer("MemoryMappedFileCache");
            }

            timer.scheduleAtFixedRate(task, delay, period);
        }
    }

    private static void schedule(TimerTask task, long delay) {
        synchronized (lock) {
            if (timer == null) {
                timer = new Timer("MemoryMappedFileCache");
            }

            timer.schedule(task, delay);
        }
    }

    public MemoryMappedFileCache(int minElementsInMemory, int maxElementsInMemory, int period) {
        this("", minElementsInMemory, maxElementsInMemory, -1, period);
    }

    public MemoryMappedFileCache(
            int minElementsInMemory, int softLimit, int hardLimit, int period) {
        this("", minElementsInMemory, softLimit, hardLimit, period);
    }

    public MemoryMappedFileCache(
            String name, int minElementsInMemory, int softLimit, int hardLimit, int period) {
        this.disabled = new AtomicBoolean(false);
        this.hasScheduled = new AtomicBoolean(false);
        this.cleanups = new AtomicInteger();
        this.hits = new AtomicInteger();
        this.miss = new AtomicInteger();
        this.name = name;
        this.minElements = minElementsInMemory;
        this.softLimit = softLimit;
        this.hardLimit = hardLimit;
        this.period = 1000L * (long) period;
        this.cache = new ConcurrentHashMap(2 * softLimit, 0.75F, 8);
        this.files = new ConcurrentHashMap(4 * softLimit, 0.75F, 8);
        boolean wantsCleanup = period > 0;
        if (wantsCleanup) {
            scheduleAtFixedRate(new MemoryMappedFileCache.CleanupTask(), this.period, this.period);
            if (cacheLog.isDebugEnabled()) {
                cacheLog.debug(
                        "MemoryMappedFileCache " + name + " cleanup every " + period + " secs");
            }
        }

        if (this.trackAll) {
            this.track = new ConcurrentHashMap(5000);
        }
    }

    protected void addRaf(String uriString, MemoryMappedRandomAccessFile mmraf) {
        MemoryMappedFileCache.CacheElement elem =
                cache.putIfAbsent(uriString, new CacheElement(mmraf, uriString));

        if (elem != null) {
            synchronized (elem) {
                elem.addFile(mmraf);
            }
        }
    }

    @Override
    public void disable() {
        this.disabled.set(true);
        this.clearCache(true);
    }

    @Override
    public void enable() {
        this.disabled.set(false);
    }

    @Override
    public FileCacheable acquire(FileFactory factory, DatasetUrl durl) throws IOException {
        return this.acquire(factory, durl.getTrueurl(), durl, -1, null, null);
    }

    @Override
    public FileCacheable acquire(
            FileFactory factory,
            Object hashKey,
            DatasetUrl location,
            int buffer_size,
            CancelTask cancelTask,
            Object spiObject)
            throws IOException {
        if (null == hashKey) {
            hashKey = location.getTrueurl();
        }

        if (null == hashKey) {
            throw new IllegalArgumentException();
        } else {
            CacheTracking t = null;
            if (this.trackAll) {
                t = new CacheTracking(hashKey);
                CacheTracking prev = this.track.putIfAbsent(hashKey, t);
                if (prev != null) {
                    t = prev;
                }
            }

            FileCacheable ncfile = this.acquireCacheOnly(hashKey);
            if (ncfile != null) {
                this.hits.incrementAndGet();
                if (t != null) {
                    ++t.hit;
                }

                return ncfile;
            } else {
                this.miss.incrementAndGet();
                if (t != null) {
                    ++t.miss;
                }

                String uriString = StringUtil2.replace(location.getTrueurl(), '\\', "/");
                if (uriString.startsWith("file:")) {
                    uriString = StringUtil2.unescape(uriString.substring(5));
                }
                ncfile = new MemoryMappedRandomAccessFile(uriString, "r");
                if (cacheLog.isDebugEnabled()) {
                    cacheLog.debug(
                            "MemoryMappedFileCache "
                                    + this.name
                                    + " acquire "
                                    + hashKey
                                    + " "
                                    + ncfile.getLocation());
                }

                if (cancelTask != null && cancelTask.isCancel()) {
                    if (ncfile != null) {
                        ncfile.close();
                    }

                    return null;
                } else if (this.disabled.get()) {
                    return ncfile;
                } else {
                    MemoryMappedFileCache.CacheElement elem =
                            this.cache.putIfAbsent(hashKey, new CacheElement(ncfile, hashKey));

                    if (elem != null) {
                        synchronized (elem) {
                            elem.addFile(ncfile);
                        }
                    }

                    boolean needHard = false;
                    boolean needSoft = false;

                    if (!this.hasScheduled.get()) {
                        int count = this.files.size();
                        if (count > this.hardLimit && this.hardLimit > 0) {
                            needHard = true;
                            this.hasScheduled.getAndSet(true);
                        } else if (count > this.softLimit && this.softLimit > 0) {
                            this.hasScheduled.getAndSet(true);
                            needSoft = true;
                        }
                    }

                    if (needHard) {
                        this.cleanup(this.hardLimit);
                    } else if (needSoft) {
                        schedule(new CleanupTask(), 100L);
                    }

                    return ncfile;
                }
            }
        }
    }

    private FileCacheable acquireCacheOnly(Object hashKey) {
        if (this.disabled.get()) {
            return null;
        } else {
            CacheElement wantCacheElem = this.cache.get(hashKey);
            if (wantCacheElem == null) {
                return null;
            } else {
                CacheElement.CacheFile want = null;
                Iterator listIt = wantCacheElem.list.iterator();

                while (listIt.hasNext()) {
                    CacheElement.CacheFile file = (CacheElement.CacheFile) listIt.next();
                    if (file.isLocked.compareAndSet(false, true)) {
                        want = file;
                        break;
                    }
                }

                if (want == null) {
                    return null;
                } else {
                    if (want.ncfile != null) {
                        long lastModified = want.ncfile.getLastModified();
                        boolean changed = lastModified != want.lastModified;
                        if (cacheLog.isDebugEnabled() && changed) {
                            cacheLog.debug(
                                    "MemoryMappedFileCache "
                                            + this.name
                                            + ": acquire from cache "
                                            + hashKey
                                            + " "
                                            + want.ncfile.getLocation()
                                            + " was changed; discard");
                        }

                        if (changed) {
                            this.remove(want);
                        }
                    }

                    if (want.ncfile != null) {
                        try {
                            want.ncfile.reacquire();
                        } catch (IOException ioe) {
                            if (cacheLog.isDebugEnabled()) {
                                cacheLog.debug(
                                        "MemoryMappedFileCache "
                                                + this.name
                                                + " acquire from cache "
                                                + hashKey
                                                + " "
                                                + want.ncfile.getLocation()
                                                + " failed: "
                                                + ioe.getMessage());
                            }

                            this.remove(want);
                        }
                    }

                    return want.ncfile;
                }
            }
        }
    }

    private void remove(CacheElement.CacheFile want) {
        want.remove();
        this.files.remove(want.ncfile);

        try {
            want.ncfile.setFileCache(null);
            want.ncfile.close();
        } catch (IOException ioe) {
            log.error("close failed on " + want.ncfile.getLocation(), ioe);
        }

        want.ncfile = null;
    }

    @Override
    public void eject(Object hashKey) {
        if (!this.disabled.get()) {
            CacheElement wantCacheElem = this.cache.get(hashKey);
            if (wantCacheElem != null) {
                Iterator listIt = wantCacheElem.list.iterator();

                while (true) {
                    if (!listIt.hasNext()) {
                        wantCacheElem.list.clear();
                        break;
                    }

                    CacheElement.CacheFile want = (CacheElement.CacheFile) listIt.next();
                    this.files.remove(want.ncfile);

                    try {
                        want.ncfile.setFileCache(null);
                        want.ncfile.close();
                        log.debug("close " + want.ncfile.getLocation());
                    } catch (IOException ioe) {
                        log.error("close failed on " + want.ncfile.getLocation(), ioe);
                    }

                    want.ncfile = null;
                }

                this.cache.remove(hashKey);
            }
        }
    }

    @Override
    public boolean release(FileCacheable ncfile) throws IOException {
        if (ncfile == null) {
            return false;
        } else if (this.disabled.get()) {
            ncfile.setFileCache(null);
            ncfile.close();
            return false;
        } else {
            CacheElement.CacheFile file = this.files.get(ncfile);
            if (file != null) {
                if (!file.isLocked.get()) {
                    cacheLog.warn(
                            "MemoryMappedFileCache "
                                    + this.name
                                    + " release "
                                    + ncfile.getLocation()
                                    + " not locked; hash= "
                                    + ncfile.hashCode());
                }

                file.lastAccessed = System.currentTimeMillis();
                ++file.countAccessed;

                try {
                    file.ncfile.release();
                    file.isLocked.set(false);
                } catch (IOException ioe) {
                    cacheLog.error(
                            "MemoryMappedFileCache {} release failed on {} - will try to remove from cache. Failure due to:",
                            new Object[] {this.name, file.getCacheName(), ioe});
                    this.remove(file);
                }

                if (cacheLog.isDebugEnabled()) {
                    cacheLog.debug(
                            "MemoryMappedFileCache "
                                    + this.name
                                    + " release "
                                    + ncfile.getLocation()
                                    + "; hash= "
                                    + ncfile.hashCode());
                }

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public synchronized void clearCache(boolean force) {
        List<CacheElement.CacheFile> deleteList = new ArrayList(2 * this.cache.size());
        Iterator iter;
        CacheElement.CacheFile file;
        if (force) {
            this.cache.clear();
            deleteList.addAll(this.files.values());
            this.files.clear();
        } else {
            iter = this.files.values().iterator();

            while (iter.hasNext()) {
                file = (CacheElement.CacheFile) iter.next();
                if (file.isLocked.compareAndSet(false, true)) {
                    file.remove();
                    deleteList.add(file);
                    iter.remove();
                }
            }

            Iterator<CacheElement> iterator = this.cache.values().iterator();
            while (iterator.hasNext()) {
                CacheElement elem = iterator.next();
                synchronized (elem) {
                    if (elem.list.isEmpty()) {
                        iterator.remove();
                    }
                }
            }
        }

        iter = deleteList.iterator();

        while (iter.hasNext()) {
            file = (CacheElement.CacheFile) iter.next();
            if (force && file.isLocked.get()) {
                cacheLog.warn(
                        "MemoryMappedFileCache " + this.name + " force close locked file= " + file);
            }

            try {
                if (file != null && file.ncfile != null) {
                    file.ncfile.setFileCache(null);
                    file.ncfile.close();
                } else {
                    log.error(
                            String.format(
                                    "MemoryMappedFileCache %s: null file or null ncfile",
                                    this.name));
                }

                if (file != null) {
                    file.ncfile = null;
                }
            } catch (IOException ioe) {
                log.error("MemoryMappedFileCache " + this.name + " close failed on " + file);
            }
        }

        if (cacheLog.isDebugEnabled()) {
            cacheLog.debug(
                    "*MemoryMappedFileCache "
                            + this.name
                            + " clearCache force= "
                            + force
                            + " deleted= "
                            + deleteList.size()
                            + " left="
                            + this.files.size());
        }
    }

    @Override
    public void showCache(Formatter format) {
        ArrayList<CacheElement.CacheFile> allFiles = new ArrayList(this.files.size());
        Iterator it = this.cache.values().iterator();

        while (it.hasNext()) {
            CacheElement elem = (CacheElement) it.next();
            synchronized (elem) {
                allFiles.addAll(elem.list);
            }
        }

        Collections.sort(allFiles);
        format.format(
                "%nFileCache %s (min=%d softLimit=%d hardLimit=%d scour=%d secs):%n",
                this.name, this.minElements, this.softLimit, this.hardLimit, this.period / 1000L);
        format.format(" isLocked  accesses lastAccess                   location %n");
        it = allFiles.iterator();

        while (it.hasNext()) {
            CacheElement.CacheFile file = (CacheElement.CacheFile) it.next();
            String loc = file.ncfile != null ? file.ncfile.getLocation() : "null";
            format.format(
                    "%8s %9d %s == %s %n",
                    file.isLocked,
                    file.countAccessed,
                    CalendarDateFormatter.toDateTimeStringISO(file.lastAccessed),
                    loc);
        }

        this.showStats(format);
    }

    @Override
    public List<String> showCache() {
        List<CacheElement.CacheFile> allFiles = new ArrayList(this.files.size());
        Iterator valIt = this.cache.values().iterator();

        while (valIt.hasNext()) {
            CacheElement elem = (CacheElement) valIt.next();
            synchronized (elem) {
                allFiles.addAll(elem.list);
            }
        }

        Collections.sort(allFiles);
        List<String> result = new ArrayList(allFiles.size());
        Iterator fileIt = allFiles.iterator();

        while (fileIt.hasNext()) {
            CacheElement.CacheFile file = (CacheElement.CacheFile) fileIt.next();
            result.add(file.toString());
        }

        return result;
    }

    @Override
    public void showStats(Formatter format) {
        format.format(
                "  hits= %d miss= %d nfiles= %d elems= %d%n",
                this.hits.get(), this.miss.get(), this.files.size(), this.cache.values().size());
    }

    @Override
    public void showTracking(Formatter format) {
        if (this.track != null) {
            List<CacheTracking> all = new ArrayList(this.track.size());
            all.addAll(this.track.values());
            Collections.sort(all);
            int seq = 0;
            int countAll = 0;
            int countHits = 0;
            int countMiss = 0;
            format.format("%nTracking All files in cache %s%n", this.name);
            format.format("    #    accum       hit    miss  file%n");
            Iterator allIt = all.iterator();

            while (allIt.hasNext()) {
                CacheTracking t = (CacheTracking) allIt.next();
                ++seq;
                countAll += t.hit + t.miss;
                countHits += t.hit;
                countMiss += t.miss;
                format.format("%6d  %7d : %6d %6d %s%n", seq, countAll, t.hit, t.miss, t.key);
            }

            float r = countAll == 0 ? 0.0F : (float) countHits / (float) countAll;
            format.format(
                    "  total=%7d : %6d %6d hit ratio=%f%n", countAll, countHits, countMiss, r);
        }
    }

    @Override
    public void resetTracking() {
        this.track = new ConcurrentHashMap(5000);
        this.trackAll = true;
    }

    synchronized void cleanup(int maxElements) {
        try {
            int size = this.files.size();
            if (size > this.minElements) {
                if (cacheLog.isDebugEnabled()) {
                    cacheLog.debug(
                            "MemoryMappedFileCache {} cleanup started at {} for maxElements={}",
                            new Object[] {this.name, CalendarDate.present(), maxElements});
                }

                this.cleanups.incrementAndGet();
                ArrayList<CacheFileSorter> unlockedFiles = new ArrayList();
                Iterator filesIt = this.files.values().iterator();

                while (filesIt.hasNext()) {
                    CacheElement.CacheFile file = (CacheElement.CacheFile) filesIt.next();
                    if (!file.isLocked.get()) {
                        unlockedFiles.add(new CacheFileSorter(file));
                    }
                }

                Collections.sort(unlockedFiles);
                int need2delete = size - this.minElements;
                int minDelete = size - maxElements;
                List<CacheElement.CacheFile> deleteList = new ArrayList(need2delete);
                int count = 0;
                Iterator iter = unlockedFiles.iterator();

                while (iter.hasNext() && count < need2delete) {
                    CacheElement.CacheFile file = ((CacheFileSorter) iter.next()).cacheFile;
                    if (file.isLocked.compareAndSet(false, true)) {
                        file.remove();
                        deleteList.add(file);
                        ++count;
                    }
                }

                if (count < minDelete) {
                    cacheLog.warn(
                            "MemoryMappedFileCache "
                                    + this.name
                                    + " cleanup couldnt remove enough to keep under the maximum= "
                                    + maxElements
                                    + " due to locked files; currently at = "
                                    + (size - count));
                }

                Iterator<CacheElement> iterator = this.cache.values().iterator();
                while (iterator.hasNext()) {
                    CacheElement elem = iterator.next();
                    synchronized (elem) {
                        if (elem.list.isEmpty()) {
                            iterator.remove();
                        }
                    }
                }
                long start = System.currentTimeMillis();
                Iterator deleteIt = deleteList.iterator();

                while (deleteIt.hasNext()) {
                    CacheElement.CacheFile file = (CacheElement.CacheFile) deleteIt.next();
                    if (null == this.files.remove(file.ncfile) && cacheLog.isDebugEnabled()) {
                        cacheLog.debug(
                                " MemoryMappedFileCache {} cleanup failed to remove {}%n",
                                this.name, file.ncfile.getLocation());
                    }

                    try {
                        file.ncfile.setFileCache(null);
                        file.ncfile.close();
                        file.ncfile = null;
                    } catch (IOException ioe) {
                        log.error(
                                "MemoryMappedFileCache "
                                        + this.name
                                        + " close failed on "
                                        + file.getCacheName());
                    }
                }

                long took = System.currentTimeMillis() - start;
                if (cacheLog.isDebugEnabled()) {
                    cacheLog.debug(
                            " MemoryMappedFileCache {} cleanup had={} removed={} took={} msecs%n",
                            new Object[] {this.name, size, deleteList.size(), took});
                }

                return;
            }
        } finally {
            this.hasScheduled.set(false);
        }
    }

    private class CleanupTask extends TimerTask {
        private CleanupTask() {}

        @Override
        public void run() {
            if (!MemoryMappedFileCache.this.disabled.get()) {
                MemoryMappedFileCache.this.cleanup(MemoryMappedFileCache.this.softLimit);
            }
        }
    }

    private class CacheFileSorter implements Comparable<CacheFileSorter> {
        private final CacheElement.CacheFile cacheFile;
        private final long lastAccessed;

        CacheFileSorter(CacheElement.CacheFile cacheFile) {
            this.cacheFile = cacheFile;
            this.lastAccessed = cacheFile.lastAccessed;
        }

        @Override
        public int compareTo(CacheFileSorter o) {
            return Long.compare(this.lastAccessed, o.lastAccessed);
        }
    }

    class CacheElement {
        final ConcurrentLinkedDeque<CacheFile> list = new ConcurrentLinkedDeque<>();

        final Object key;

        CacheElement(FileCacheable ncfile, Object key) {
            this.key = key;
            CacheElement.CacheFile file = new CacheElement.CacheFile(ncfile);
            this.list.add(file);
            MemoryMappedFileCache.this.files.put(ncfile, file);
            if (cacheLog.isDebugEnabled()) {
                cacheLog.debug(
                        "CacheElement add to cache " + key + " " + MemoryMappedFileCache.this.name);
            }
        }

        CacheElement.CacheFile addFile(FileCacheable ncfile) {
            CacheElement.CacheFile file = new CacheElement.CacheFile(ncfile);
            this.list.add(file);

            MemoryMappedFileCache.this.files.put(ncfile, file);
            return file;
        }

        @Override
        public String toString() {
            return this.key + " count=" + this.list.size();
        }

        class CacheFile implements Comparable<CacheElement.CacheFile> {
            FileCacheable ncfile;
            final AtomicBoolean isLocked;
            int countAccessed;
            long lastModified;
            long lastAccessed;

            private CacheFile(FileCacheable ncfile) {
                this.isLocked = new AtomicBoolean(true);
                this.ncfile = ncfile;
                this.lastModified = ncfile.getLastModified();
                this.lastAccessed = System.currentTimeMillis();
                ncfile.setFileCache(MemoryMappedFileCache.this);
                if (cacheLog.isDebugEnabled()) {
                    cacheLog.debug(
                            "MemoryMappedFileCache "
                                    + MemoryMappedFileCache.this.name
                                    + " add to cache "
                                    + CacheElement.this.key);
                }
            }

            String getCacheName() {
                return this.ncfile.getLocation();
            }

            void remove() {
                if (!CacheElement.this.list.remove(this)) {
                    cacheLog.warn(
                            "MemoryMappedFileCache "
                                    + MemoryMappedFileCache.this.name
                                    + " could not remove "
                                    + this.ncfile.getLocation());
                }

                if (cacheLog.isDebugEnabled()) {
                    cacheLog.debug(
                            "MemoryMappedFileCache "
                                    + MemoryMappedFileCache.this.name
                                    + " remove "
                                    + this.ncfile.getLocation());
                }
            }

            @Override
            public String toString() {
                String name = this.ncfile == null ? "ncfile is null" : this.ncfile.getLocation();
                return this.isLocked
                        + " "
                        + this.countAccessed
                        + " "
                        + CalendarDateFormatter.toDateTimeStringISO(this.lastAccessed)
                        + "   "
                        + name;
            }

            @Override
            public int compareTo(CacheElement.CacheFile o) {
                return Long.compare(this.lastAccessed, o.lastAccessed);
            }
        }
    }

    private static class CacheTracking implements Comparable<CacheTracking> {
        Object key;
        int hit;
        int miss;

        private CacheTracking(Object key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                CacheTracking tracker = (CacheTracking) o;
                return this.key.equals(tracker.key);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return this.key.hashCode();
        }

        @Override
        public int compareTo(CacheTracking o) {
            return Integer.compare(this.hit + this.miss, o.hit + o.miss);
        }
    }
}
