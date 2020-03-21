/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.files;

import static org.geotools.data.shapefile.files.ShpFileType.DBF;
import static org.geotools.data.shapefile.files.ShpFileType.SHP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;

/**
 * The collection of all the files that are the shapefile and its metadata and indices.
 *
 * <p>This class has methods for performing actions on the files. Currently mainly for obtaining
 * read and write channels and streams. But in the future a move method may be introduced.
 *
 * <p>Note: The method that require locks (such as getInputStream()) will automatically acquire
 * locks and the javadocs should document how to release the lock. Therefore the methods {@link
 * #acquireRead(ShpFileType, FileReader)} and {@link #acquireWrite(ShpFileType, FileWriter)} svn
 *
 * @author jesse
 */
public class ShpFiles {

    static final Logger LOGGER = Logging.getLogger(ShpFiles.class);

    /**
     * The urls for each type of file that is associated with the shapefile. The key is the type of
     * file
     */
    private final Map<ShpFileType, URL> urls = new ConcurrentHashMap<ShpFileType, URL>();

    /** A read/write lock, so that we can have concurrent readers */
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * The set of locker sources per thread. Used as a debugging aid and to upgrade/downgrade the
     * locks
     */
    private final Map<Thread, Collection<ShpFilesLocker>> lockers =
            new ConcurrentHashMap<Thread, Collection<ShpFilesLocker>>();

    /** A cache for read only memory mapped buffers */
    private final MemoryMapCache mapCache = new MemoryMapCache();

    private boolean memoryMapCacheEnabled;

    /**
     * Searches for all the files and adds then to the map of files.
     *
     * @param fileName the filename or url of any one of the shapefile files
     * @throws MalformedURLException if it isn't possible to create a URL from string. It will be
     *     used to create a file and create a URL from that if both fail this exception is thrown
     */
    public ShpFiles(String fileName) throws MalformedURLException {
        try {
            URL url = new URL(fileName);
            init(url);
        } catch (MalformedURLException e) {
            init(new File(fileName).toURI().toURL());
        }
    }

    /**
     * Searches for all the files and adds then to the map of files.
     *
     * @param file any one of the shapefile files
     * @throws FileNotFoundException if the shapefile associated with file is not found
     */
    public ShpFiles(File file) throws MalformedURLException {
        init(file.toURI().toURL());
    }

    /**
     * Searches for all the files and adds then to the map of files.
     *
     * @param url any one of the shapefile files
     */
    public ShpFiles(URL url) throws IllegalArgumentException {
        init(url);
    }

    private void init(URL url) {
        String base = baseName(url);
        if (base == null) {
            throw new IllegalArgumentException(
                    url.getPath()
                            + " is not one of the files types that is known to be associated with a shapefile");
        }

        String urlString = url.toExternalForm();
        char lastChar = urlString.charAt(urlString.length() - 1);
        boolean upperCase = Character.isUpperCase(lastChar);

        for (ShpFileType type : ShpFileType.values()) {

            String extensionWithPeriod = type.extensionWithPeriod;
            if (upperCase) {
                extensionWithPeriod = extensionWithPeriod.toUpperCase();
            } else {
                extensionWithPeriod = extensionWithPeriod.toLowerCase();
            }

            URL newURL;
            String string = base + extensionWithPeriod;
            try {
                newURL = new URL(url, string);
            } catch (MalformedURLException e) {
                // shouldn't happen because the starting url was constructable
                throw new RuntimeException(e);
            }
            urls.put(type, newURL);
        }

        // if the files are local check each file to see if it exists
        // if not then search for a file of the same name but try all combinations of the
        // different cases that the extension can be made up of.
        // IE Shp, SHP, Shp, ShP etc...
        if (isLocal()) {
            Set<Entry<ShpFileType, URL>> entries = urls.entrySet();
            Map<ShpFileType, URL> toUpdate = new HashMap<ShpFileType, URL>();
            for (Entry<ShpFileType, URL> entry : entries) {
                if (!exists(entry.getKey())) {
                    url = findExistingFile(entry.getKey(), entry.getValue());
                    if (url != null) {
                        toUpdate.put(entry.getKey(), url);
                    }
                }
            }

            urls.putAll(toUpdate);
        }
    }

    private URL findExistingFile(ShpFileType shpFileType, URL value) {
        final File file = URLs.urlToFile(value);
        File directory = file.getParentFile();
        if (directory == null || !directory.exists()) {
            // doesn't exist
            return null;
        }
        File[] files =
                directory.listFiles(
                        new FilenameFilter() {

                            public boolean accept(File dir, String name) {
                                return file.getName().equalsIgnoreCase(name);
                            }
                        });
        if (files != null && files.length > 0) {
            try {
                return files[0].toURI().toURL();
            } catch (MalformedURLException e) {
                LOGGER.log(Level.SEVERE, "", e);
            }
        }
        return null;
    }

    /** This verifies that this class has been closed correctly (nothing locking) */
    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    public void dispose() {
        if (numberOfLocks() != 0) {
            logCurrentLockers(Level.SEVERE);
            lockers.clear(); // so as not to get this log again.
        }
        mapCache.clean();
    }

    /**
     * Writes to the log all the lockers and when they were constructed.
     *
     * @param logLevel the level at which to log.
     */
    public void logCurrentLockers(Level logLevel) {
        for (Collection<ShpFilesLocker> lockerList : lockers.values()) {
            for (ShpFilesLocker locker : lockerList) {
                StringBuilder sb = new StringBuilder("The following locker still has a lock: ");
                sb.append(locker);
                LOGGER.log(logLevel, sb.toString(), locker.getTrace());
            }
        }
    }

    private String baseName(Object obj) {
        for (ShpFileType type : ShpFileType.values()) {
            String base = null;
            if (obj instanceof File) {
                File file = (File) obj;
                base = type.toBase(file);
            }
            if (obj instanceof URL) {
                URL file = (URL) obj;
                base = type.toBase(file);
            }
            if (base != null) {
                return base;
            }
        }
        return null;
    }

    /**
     * Returns the URLs (in string form) of all the files for the shapefile datastore.
     *
     * @return the URLs (in string form) of all the files for the shapefile datastore.
     */
    public Map<ShpFileType, String> getFileNames() {
        Map<ShpFileType, String> result = new HashMap<ShpFileType, String>();
        Set<Entry<ShpFileType, URL>> entries = urls.entrySet();

        for (Entry<ShpFileType, URL> entry : entries) {
            result.put(entry.getKey(), entry.getValue().toExternalForm());
        }

        return result;
    }

    /**
     * Returns the string form of the url that identifies the file indicated by the type parameter
     * or null if it is known that the file does not exist.
     *
     * <p>Note: a URL should NOT be constructed from the string instead the URL should be obtained
     * through calling one of the aquireLock methods.
     *
     * @param type indicates the type of file the caller is interested in.
     * @return the string form of the url that identifies the file indicated by the type parameter
     *     or null if it is known that the file does not exist.
     */
    public String get(ShpFileType type) {
        return urls.get(type).toExternalForm();
    }

    /**
     * Returns the number of locks on the current set of shapefile files. This is not thread safe so
     * do not count on it to have a completely accurate picture but it can be useful debugging
     *
     * @return the number of locks on the current set of shapefile files.
     */
    public int numberOfLocks() {
        int count = 0;
        for (Collection<ShpFilesLocker> lockerList : lockers.values()) {
            count += lockerList.size();
        }
        return count;
    }

    /**
     * Acquire a File for read only purposes. It is recommended that get*Stream or get*Channel
     * methods are used when reading or writing to the file is desired.
     *
     * @see #getInputStream(ShpFileType, FileReader)
     * @see #getReadChannel(ShpFileType, FileReader)
     * @see #getWriteChannel(ShpFileType, FileReader)
     * @param type the type of the file desired.
     * @param requestor the object that is requesting the File. The same object must release the
     *     lock and is also used for debugging.
     * @return the File type requested
     */
    public File acquireReadFile(ShpFileType type, FileReader requestor) {
        if (!isLocal()) {
            throw new IllegalStateException("This method only applies if the files are local");
        }
        URL url = acquireRead(type, requestor);
        return URLs.urlToFile(url);
    }

    /**
     * Acquire a URL for read only purposes. It is recommended that get*Stream or get*Channel
     * methods are used when reading or writing to the file is desired.
     *
     * @see #getInputStream(ShpFileType, FileReader)
     * @see #getReadChannel(ShpFileType, FileReader)
     * @see #getWriteChannel(ShpFileType, FileReader)
     * @param type the type of the file desired.
     * @param requestor the object that is requesting the URL. The same object must release the lock
     *     and is also used for debugging.
     * @return the URL to the file of the type requested
     */
    public URL acquireRead(ShpFileType type, FileReader requestor) {
        URL url = urls.get(type);
        if (url == null) return null;

        readWriteLock.readLock().lock();
        Collection<ShpFilesLocker> threadLockers = getCurrentThreadLockers();
        threadLockers.add(new ShpFilesLocker(url, requestor));
        return url;
    }

    /**
     * Tries to acquire a URL for read only purposes. Returns null if the acquire failed or if the
     * file does not.
     *
     * <p>It is recommended that get*Stream or get*Channel methods are used when reading or writing
     * to the file is desired.
     *
     * @see #getInputStream(ShpFileType, FileReader)
     * @see #getReadChannel(ShpFileType, FileReader)
     * @see #getWriteChannel(ShpFileType, FileReader)
     * @param type the type of the file desired.
     * @param requestor the object that is requesting the URL. The same object must release the lock
     *     and is also used for debugging.
     * @return A result object containing the URL or the reason for the failure.
     */
    public Result<URL, State> tryAcquireRead(ShpFileType type, FileReader requestor) {
        URL url = urls.get(type);
        if (url == null) {
            return new Result<URL, State>(null, State.NOT_EXIST);
        }

        boolean locked = readWriteLock.readLock().tryLock();
        if (!locked) {
            return new Result<URL, State>(null, State.LOCKED);
        }

        getCurrentThreadLockers().add(new ShpFilesLocker(url, requestor));

        return new Result<URL, State>(url, State.GOOD);
    }

    /**
     * Unlocks a read lock. The file and requestor must be the the same as the one of the lockers.
     *
     * @param file file that was locked
     * @param requestor the class that requested the file
     */
    public void unlockRead(File file, FileReader requestor) {
        Collection<URL> allURLS = urls.values();
        for (URL url : allURLS) {
            if (URLs.urlToFile(url).equals(file)) {
                unlockRead(url, requestor);
            }
        }
    }

    /**
     * Unlocks a read lock. The url and requestor must be the the same as the one of the lockers.
     *
     * @param url url that was locked
     * @param requestor the class that requested the url
     */
    public void unlockRead(URL url, FileReader requestor) {
        if (url == null) {
            throw new NullPointerException("url cannot be null");
        }
        if (requestor == null) {
            throw new NullPointerException("requestor cannot be null");
        }

        Collection threadLockers = getCurrentThreadLockers();
        boolean removed = threadLockers.remove(new ShpFilesLocker(url, requestor));
        if (!removed) {
            throw new IllegalArgumentException(
                    "Expected requestor "
                            + requestor
                            + " to have locked the url but it does not hold the lock for the URL");
        }
        if (threadLockers.size() == 0) lockers.remove(Thread.currentThread());
        readWriteLock.readLock().unlock();
    }

    /**
     * Acquire a File for read and write purposes.
     *
     * <p>It is recommended that get*Stream or get*Channel methods are used when reading or writing
     * to the file is desired.
     *
     * @see #getInputStream(ShpFileType, FileReader)
     * @see #getReadChannel(ShpFileType, FileReader)
     * @see #getWriteChannel(ShpFileType, FileReader)
     * @param type the type of the file desired.
     * @param requestor the object that is requesting the File. The same object must release the
     *     lock and is also used for debugging.
     * @return the File to the file of the type requested
     */
    public File acquireWriteFile(ShpFileType type, FileWriter requestor) {
        if (!isLocal()) {
            throw new IllegalStateException("This method only applies if the files are local");
        }
        URL url = acquireWrite(type, requestor);
        return URLs.urlToFile(url);
    }

    /**
     * Acquire a URL for read and write purposes.
     *
     * <p>It is recommended that get*Stream or get*Channel methods are used when reading or writing
     * to the file is desired.
     *
     * @see #getInputStream(ShpFileType, FileReader)
     * @see #getReadChannel(ShpFileType, FileReader)
     * @see #getWriteChannel(ShpFileType, FileReader)
     * @param type the type of the file desired.
     * @param requestor the object that is requesting the URL. The same object must release the lock
     *     and is also used for debugging.
     * @return the URL to the file of the type requested
     */
    public URL acquireWrite(ShpFileType type, FileWriter requestor) {
        URL url = urls.get(type);
        if (url == null) {
            return null;
        }

        // we need to give up all read locks before getting the write one
        Collection<ShpFilesLocker> threadLockers = getCurrentThreadLockers();
        relinquishReadLocks(threadLockers);
        readWriteLock.writeLock().lock();
        threadLockers.add(new ShpFilesLocker(url, requestor));
        mapCache.cleanFileCache(url);
        return url;
    }

    /**
     * Tries to acquire a URL for read/write purposes. Returns null if the acquire failed or if the
     * file does not exist
     *
     * <p>It is recommended that get*Stream or get*Channel methods are used when reading or writing
     * to the file is desired.
     *
     * @see #getInputStream(ShpFileType, FileReader)
     * @see #getReadChannel(ShpFileType, FileReader)
     * @see #getWriteChannel(ShpFileType, FileReader)
     * @param type the type of the file desired.
     * @param requestor the object that is requesting the URL. The same object must release the lock
     *     and is also used for debugging.
     * @return A result object containing the URL or the reason for the failure.
     */
    public Result<URL, State> tryAcquireWrite(ShpFileType type, FileWriter requestor) {

        URL url = urls.get(type);
        if (url == null) {
            return new Result<URL, State>(null, State.NOT_EXIST);
        }

        Collection<ShpFilesLocker> threadLockers = getCurrentThreadLockers();
        boolean locked = readWriteLock.writeLock().tryLock();
        if (!locked && threadLockers.size() > 1) {
            // hum, it may be be because we are holding a read lock
            relinquishReadLocks(threadLockers);
            locked = readWriteLock.writeLock().tryLock();
            if (locked == false) {
                regainReadLocks(threadLockers);
                return new Result<URL, State>(null, State.LOCKED);
            }
        }

        threadLockers.add(new ShpFilesLocker(url, requestor));
        return new Result<URL, State>(url, State.GOOD);
    }

    /**
     * Unlocks a read lock. The file and requestor must be the the same as the one of the lockers.
     *
     * @param file file that was locked
     * @param requestor the class that requested the file
     */
    public void unlockWrite(File file, FileWriter requestor) {
        Collection<URL> allURLS = urls.values();
        for (URL url : allURLS) {
            if (URLs.urlToFile(url).equals(file)) {
                unlockWrite(url, requestor);
            }
        }
    }

    /**
     * Unlocks a read lock. The requestor must be have previously obtained a lock for the url.
     *
     * @param url url that was locked
     * @param requestor the class that requested the url
     */
    public void unlockWrite(URL url, FileWriter requestor) {
        if (url == null) {
            throw new NullPointerException("url cannot be null");
        }
        if (requestor == null) {
            throw new NullPointerException("requestor cannot be null");
        }
        Collection<ShpFilesLocker> threadLockers = getCurrentThreadLockers();
        boolean removed = threadLockers.remove(new ShpFilesLocker(url, requestor));
        if (!removed) {
            throw new IllegalArgumentException(
                    "Expected requestor "
                            + requestor
                            + " to have locked the url but it does not hold the lock for the URL");
        }

        if (threadLockers.size() == 0) {
            lockers.remove(Thread.currentThread());
        } else {
            // get back read locks before giving up the write one
            regainReadLocks(threadLockers);
        }
        readWriteLock.writeLock().unlock();
    }

    /** Returns the list of lockers attached to a given thread, or creates it if missing */
    private Collection<ShpFilesLocker> getCurrentThreadLockers() {
        Collection<ShpFilesLocker> threadLockers = lockers.get(Thread.currentThread());
        if (threadLockers == null) {
            threadLockers = new ArrayList<ShpFilesLocker>();
            lockers.put(Thread.currentThread(), threadLockers);
        }
        return threadLockers;
    }

    /** Gives up all read locks in preparation for lock upgade */
    private void relinquishReadLocks(Collection<ShpFilesLocker> threadLockers) {
        for (ShpFilesLocker shpFilesLocker : threadLockers) {
            if (shpFilesLocker.reader != null && !shpFilesLocker.upgraded) {
                readWriteLock.readLock().unlock();
                shpFilesLocker.upgraded = true;
            }
        }
    }

    /** Re-takes the read locks in preparation for lock downgrade */
    private void regainReadLocks(Collection<ShpFilesLocker> threadLockers) {
        for (ShpFilesLocker shpFilesLocker : threadLockers) {
            if (shpFilesLocker.reader != null && shpFilesLocker.upgraded) {
                readWriteLock.readLock().lock();
                shpFilesLocker.upgraded = false;
            }
        }
    }

    /**
     * Determine if the location of this shapefile is local or remote.
     *
     * @return true if local, false if remote
     */
    public boolean isLocal() {
        return urls.get(ShpFileType.SHP).toExternalForm().toLowerCase().startsWith("file:");
    }

    /** Returns true if the files are writable */
    public boolean isWritable() {
        if (!isLocal()) {
            return false;
        }
        return URLs.urlToFile(urls.get(SHP)).canWrite() && URLs.urlToFile(urls.get(DBF)).canWrite();
    }

    /**
     * Delete all the shapefile files. If the files are not local or the one files cannot be deleted
     * return false.
     */
    public boolean delete() {
        BasicShpFileWriter requestor = new BasicShpFileWriter("ShpFiles for deleting all files");
        URL writeLockURL = acquireWrite(SHP, requestor);
        boolean retVal = true;
        try {
            if (isLocal()) {
                Collection<URL> values = urls.values();
                for (URL url : values) {
                    File f = URLs.urlToFile(url);
                    if (!f.delete()) {
                        retVal = false;
                    }
                }
            } else {
                retVal = false;
            }
        } finally {
            unlockWrite(writeLockURL, requestor);
        }
        return retVal;
    }

    /**
     * Opens a input stream for the indicated file. A read lock is requested at the method call and
     * released on close.
     *
     * @param type the type of file to open the stream to.
     * @param requestor the object requesting the stream
     * @return an input stream
     * @throws IOException if a problem occurred opening the stream.
     */
    public InputStream getInputStream(ShpFileType type, final FileReader requestor)
            throws IOException {
        final URL url = acquireRead(type, requestor);

        try {
            FilterInputStream input =
                    new FilterInputStream(url.openStream()) {

                        private volatile boolean closed = false;

                        @Override
                        public void close() throws IOException {
                            try {
                                super.close();
                            } finally {
                                if (!closed) {
                                    closed = true;
                                    unlockRead(url, requestor);
                                }
                            }
                        }
                    };
            return input;
        } catch (Throwable e) {
            unlockRead(url, requestor);
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Opens a output stream for the indicated file. A write lock is requested at the method call
     * and released on close.
     *
     * @param type the type of file to open the stream to.
     * @param requestor the object requesting the stream
     * @return an output stream
     * @throws IOException if a problem occurred opening the stream.
     */
    @SuppressWarnings("PMD.CloseResource") // resource is returned
    public OutputStream getOutputStream(ShpFileType type, final FileWriter requestor)
            throws IOException {
        final URL url = acquireWrite(type, requestor);
        OutputStream out = null;
        try {

            if (isLocal()) {
                File file = URLs.urlToFile(url);
                out = new FileOutputStream(file);
            } else {
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                out = connection.getOutputStream();
            }

            FilterOutputStream output =
                    new FilterOutputStream(out) {

                        private volatile boolean closed = false;

                        @Override
                        public void close() throws IOException {
                            try {
                                super.close();
                            } finally {
                                if (!closed) {
                                    closed = true;
                                    unlockWrite(url, requestor);
                                }
                            }
                        }
                    };

            return output;
        } catch (Throwable e) {
            unlockWrite(url, requestor);
            if (out != null) {
                out.close();
            }
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Obtain a ReadableByteChannel from the given URL. If the url protocol is file, a FileChannel
     * will be returned. Otherwise a generic channel will be obtained from the urls input stream.
     *
     * <p>A read lock is obtained when this method is called and released when the channel is
     * closed.
     *
     * @param type the type of file to open the channel to.
     * @param requestor the object requesting the channel
     */
    @SuppressWarnings("PMD.CloseResource") // cannot close RAF/IS locally, the channel refers to it
    public ReadableByteChannel getReadChannel(ShpFileType type, FileReader requestor)
            throws IOException {
        URL url = acquireRead(type, requestor);
        ReadableByteChannel channel = null;
        try {
            if (isLocal()) {
                File file = URLs.urlToFile(url);

                RandomAccessFile raf = new RandomAccessFile(file, "r");
                channel = new FileChannelDecorator(raf.getChannel(), this, url, requestor);
            } else {
                InputStream in = url.openConnection().getInputStream();
                channel =
                        new ReadableByteChannelDecorator(
                                Channels.newChannel(in), this, url, requestor);
            }
        } catch (Throwable e) {
            unlockRead(url, requestor);
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        return channel;
    }

    /**
     * Obtain a WritableByteChannel from the given URL. If the url protocol is file, a FileChannel
     * will be returned. Currently, this method will return a generic channel for remote urls,
     * however both shape and dbf writing can only occur with a local FileChannel channel.
     *
     * <p>A write lock is obtained when this method is called and released when the channel is
     * closed.
     *
     * @param type the type of file to open the stream to.
     * @param requestor the object requesting the stream
     * @return a WritableByteChannel for the provided file type
     * @throws IOException if there is an error opening the stream
     */
    @SuppressWarnings("PMD.CloseResource") // closeable resource are returned
    public WritableByteChannel getWriteChannel(ShpFileType type, FileWriter requestor)
            throws IOException {

        URL url = acquireWrite(type, requestor);

        try {
            WritableByteChannel channel;
            if (isLocal()) {

                File file = URLs.urlToFile(url);

                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                channel = new FileChannelDecorator(raf.getChannel(), this, url, requestor);

                ((FileChannel) channel).lock();

            } else {
                OutputStream out = url.openConnection().getOutputStream();
                channel =
                        new WritableByteChannelDecorator(
                                Channels.newChannel(out), this, url, requestor);
            }

            return channel;
        } catch (Throwable e) {
            unlockWrite(url, requestor);
            if (e instanceof IOException) {
                throw (IOException) e;
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public enum State {
        /** Indicates the files does not exist for this shapefile */
        NOT_EXIST,
        /** Indicates that the files are locked by another thread. */
        LOCKED,
        /** Indicates that the url and lock were successfully obtained */
        GOOD
    }

    /**
     * Obtains a Storage file for the type indicated. An id is provided so that the same file can be
     * obtained at a later time with just the id
     *
     * @param type the type of file to create and return
     * @return StorageFile
     * @throws IOException if temporary files cannot be created
     */
    public StorageFile getStorageFile(ShpFileType type) throws IOException {
        String baseName = getTypeName();
        if (baseName.length() < 3) { // min prefix length for createTempFile
            baseName = baseName + "___".substring(0, 3 - baseName.length());
        }
        File tmp = File.createTempFile(baseName, type.extensionWithPeriod);
        return new StorageFile(this, tmp, type);
    }

    public String getTypeName() {
        String path = SHP.toBase(urls.get(SHP));
        int slash = Math.max(0, path.lastIndexOf('/') + 1);
        return path.substring(slash);
    }

    /**
     * Internal method that the file channel decorators will call to allow reuse of the memory
     * mapped buffers
     */
    MappedByteBuffer map(FileChannel wrapped, URL url, MapMode mode, long position, long size)
            throws IOException {
        if (memoryMapCacheEnabled) {
            return mapCache.map(wrapped, url, mode, position, size);
        } else {
            return wrapped.map(mode, position, size);
        }
    }

    /**
     * Returns the status of the memory map cache. When enabled the memory mapped portions of the
     * files are cached and shared (giving each thread a clone of it)
     */
    public boolean isMemoryMapCacheEnabled() {
        return memoryMapCacheEnabled;
    }

    /**
     * Enables the memory map cache. When enabled the memory mapped portions of the files are cached
     * and shared (giving each thread a clone of it)
     */
    public void setMemoryMapCacheEnabled(boolean memoryMapCacheEnabled) {
        this.memoryMapCacheEnabled = memoryMapCacheEnabled;
        if (!memoryMapCacheEnabled) {
            mapCache.clean();
        }
    }

    /**
     * Returns true if the file exists. Throws an exception if the file is not local.
     *
     * @param fileType the type of file to check existance for.
     * @return true if the file exists.
     * @throws IllegalArgumentException if the files are not local.
     */
    public boolean exists(ShpFileType fileType) throws IllegalArgumentException {
        if (!isLocal()) {
            throw new IllegalArgumentException(
                    "This method only makes sense if the files are local");
        }
        URL url = urls.get(fileType);
        if (url == null) {
            return false;
        }

        File file = URLs.urlToFile(url);
        return file.exists();
    }
}
