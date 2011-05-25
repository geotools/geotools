/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.Color;
import java.awt.image.IndexColorModel;  // For javadoc
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import javax.imageio.IIOException;
import javax.imageio.spi.ServiceRegistry;
import java.util.*;

import org.geotools.io.DefaultFileFilter;
import org.geotools.io.LineFormat;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.IndexedResourceBundle;
import org.geotools.util.logging.Logging;
import org.geotools.util.CanonicalSet;


/**
 * A factory for {@linkplain IndexColorModel index color models} created from RGB values listed
 * in files. The palette definition files are text files containing an arbitrary number of lines,
 * each line containing RGB components ranging from 0 to 255 inclusive. An optional fourth column
 * may be provided for alpha components. Empty lines and lines starting with the {@code '#'}
 * character are ignored. Example:
 *
 * <blockquote><pre>
 * # RGB codes for SeaWiFs images
 * # (chlorophylle-a concentration)
 *
 *   033   000   096
 *   032   000   097
 *   031   000   099
 *   030   000   101
 *   029   000   102
 *   028   000   104
 *   026   000   106
 *   025   000   107
 * <cite>etc...</cite>
 * </pre></blockquote>
 *
 * The number of RGB codes doesn't have to match the target {@linkplain IndexColorModel#getMapSize
 * color map size}. RGB codes will be automatically interpolated as needed.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class PaletteFactory {
    /**
     * The file which contains a list of available color palettes. This file is optional
     * and used only in last resort, since scanning a directory content is more reliable.
     * If such file exists in the same directory than the one that contains the palettes,
     * this file will be used by {@link #getAvailableNames}.
     */
    private static final String LIST_FILE = "list.txt";

    /**
     * The default sub-directory, relative to the {@code PaletteFactory} class directory.
     */
    private static final File DEFAULT_DIRECTORY = new File("colors");

    /**
     * The default palette factory.
     */
    private static PaletteFactory defaultFactory;

    /**
     * The fallback factory, or {@code null} if there is none. The fallback factory
     * will be queried if a palette was not found in current factory.
     * <p>
     * This field should be considered as final. It is modified by {@link #scanForPlugins} only.
     */
    private PaletteFactory fallback;

    /**
     * The class loader from which to load the palette definition files. If {@code null} and
     * {@link #loader} is null as well, then loading will occurs from the system current
     * working directory.
     */
    private final ClassLoader classloader;

    /**
     * An alternative to {@link #classloader} for loading resources. At most one of
     * {@code classloader} and {@code loader} can be non-null. If both are {@code null},
     * then loading will occurs from the system current working directory.
     */
    private final Class<?> loader;

    /**
     * The base directory from which to search for palette definition files.
     * If {@code null}, then the working directory ({@code "."}) is assumed.
     */
    private final File directory;

    /**
     * The file extension.
     */
    private final String extension;

    /**
     * The charset to use for parsing files, or {@code null} for the current default.
     */
    private final Charset charset;

    /**
     * The locale to use for parsing files, or {@code null} for the current default.
     */
    private final Locale locale;

    /**
     * The locale to use for formatting error messages, or {@code null} for the current default.
     * This locale is informative only; there is no garantee that this locale will be really used.
     */
    private transient ThreadLocal<Locale> warningLocales;

    /**
     * The set of palettes already created.
     */
    private final CanonicalSet<Palette> palettes = CanonicalSet.newInstance(Palette.class);

    /**
     * The set of palettes protected from garbage collection. We protect a palette as long as it
     * holds a reference to a color model - this is necessary in order to prevent multiple creation
     * of the same {@link IndexColorModel}. The references are cleaned by {@link PaletteDisposer}.
     */
    final Set<Palette> protectedPalettes = new HashSet<Palette>();

    /**
     * Gets the default palette factory. This method creates a default instance looking for
     * {@code org/geotools/image/io/colors/*.pal} files where {@code '*'} is a palette name.
     * Next, this method {@linkplain #scanForPlugins scan for plugins} using the default
     * class loader. The result is cached for subsequent calls to this {@code getDefault()}
     * method.
     */
    public synchronized static PaletteFactory getDefault() {
        if (defaultFactory == null) {
            defaultFactory = new PaletteFactory();
            scanForPlugins(null);
        }
        return defaultFactory;
    }

    /**
     * Lookups for additional palette factories on the classpath. The palette factories shall
     * be declared in {@code META-INF/services/org.geotools.image.io.PaletteFactory} files.
     * <p>
     * Palette factories found are added to the chain of default factories. The next time that
     * a <code>{@linkplain #getDefault()}.getPalette(...)</code> method will be invoked, the
     * scanned factories will be tried first. If they can't create a given palette, then the
     * Geotools default factory will be tried last.
     * <p>
     * It is usually not needed to invoke this method directly since it is invoked automatically
     * by {@link #getDefault()} when first needed. This method may be useful when a specific class
     * loader need to be used, or when the classpath content changed.
     *
     * @param loader The class loader to use, or {@code null} for the default one.
     *
     * @since 2.4
     */
    public synchronized static void scanForPlugins(final ClassLoader loader) {
        final Set<Class<? extends PaletteFactory>> existings = new HashSet<Class<? extends PaletteFactory>>();
        for (PaletteFactory p=getDefault(); p!=null; p=p.fallback) {
            existings.add(p.getClass());
        }
        final Iterator<? extends PaletteFactory> it = (loader == null) ?
                ServiceRegistry.lookupProviders(PaletteFactory.class) :
                ServiceRegistry.lookupProviders(PaletteFactory.class, loader);
        while (it.hasNext()) {
            /*
             * Adds the scanned factory to the chain. There is no public method for doing that
             * because PaletteFactory is quasi-immutable except for this method which modifies
             * the fallback field. It is okay in this context since we just created the factory
             * instance.
             */
            final PaletteFactory factory = it.next();
            if (existings.add(factory.getClass())) {
                PaletteFactory tail = factory;
                while (tail.fallback != null) {
                    tail = tail.fallback;
                }
                tail.fallback = defaultFactory;
                defaultFactory = factory;
            }
        }
    }

    /**
     * Constructs a default palette factory using this {@linkplain #getClass object class} for
     * loading palette definition files. The default directory is {@code "colors"} relative to
     * the directory of the subclass extending this class. The character encoding is ISO-8859-1
     * and the locale is {@linkplain Locale#US US}.
     * <p>
     * This constructor is protected because is it merely a convenience for subclasses registering
     * themself as a service in the {@code META-INF/services/org.geotools.image.io.PaletteFactory}
     * file. Users should invoke {@link #getDefault} instead, which will returns a shared instance
     * of this class together with any custom factories found on the class path.
     *
     * @since 2.5
     */
    protected PaletteFactory() {
        this.classloader = null;
        this.loader      = getClass();
        this.directory   = DEFAULT_DIRECTORY;
        this.extension   = ".pal";
        this.charset     = Charset.forName("ISO-8859-1");
        this.locale      = Locale.US;
    }

    /**
     * Constructs a palette factory using loading palette definition files in a specific directory.
     * No {@linkplain ClassLoader class loader} is used for loading the files.
     *
     * @param directory The base directory for palette definition files relative to current
     *                  directory, or {@code null} for {@code "."}.
     * @param extension File name extension, or {@code null} if there is no extension
     *                  to add to filename. If non-null, this extension will be automatically
     *                  appended to filename. It should starts with the {@code '.'} character.
     * @param charset   The charset to use for parsing files, or {@code null} for the default.
     * @param locale    The locale to use for parsing files, or {@code null} for the default.
     *
     * @since 2.5
     */
    public PaletteFactory(final File    directory,
                          final String  extension,
                          final Charset charset,
                          final Locale  locale)
    {
        this.classloader = null;
        this.loader      = null;
        this.directory   = directory;
        this.extension   = startWithDot(extension);
        this.charset     = charset;
        this.locale      = locale;
    }

    /**
     * Constructs a palette factory using an optional {@linkplain ClassLoader class loader}
     * for loading palette definition files.
     *
     * @param fallback  An optional fallback factory, or {@code null} if there is none. The fallback
     *                  factory will be queried if a palette was not found in the current factory.
     * @param loader    An optional class loader to use for loading the palette definition files.
     *                  If {@code null}, loading will occurs from the system current working
     *                  directory.
     * @param directory The base directory for palette definition files. It may be a Java package
     *                  if a {@code loader} were specified. If {@code null}, then {@code "."} is
     *                  assumed.
     * @param extension File name extension, or {@code null} if there is no extension
     *                  to add to filename. If non-null, this extension will be automatically
     *                  appended to filename. It should starts with the {@code '.'} character.
     * @param charset   The charset to use for parsing files, or {@code null} for the default.
     * @param locale    The locale to use for parsing files, or {@code null} for the default.
     */
    public PaletteFactory(final PaletteFactory fallback,
                          final ClassLoader    loader,
                          final File           directory,
                          final String         extension,
                          final Charset        charset,
                          final Locale         locale)
    {
        this.fallback    = fallback;
        this.classloader = loader;
        this.loader      = null;
        this.directory   = directory;
        this.extension   = startWithDot(extension);
        this.charset     = charset;
        this.locale      = locale;
    }

    /**
     * Constructs a palette factory using an optional {@linkplain Class class} for loading
     * palette definition files. Using a {@linkplain Class class} instead of a {@linkplain
     * ClassLoader class loader} can avoid security issue on some platforms (some platforms
     * do not allow to load resources from a {@code ClassLoader} because it can load from the
     * root package).
     *
     * @param fallback  An optional fallback factory, or {@code null} if there is none. The fallback
     *                  factory will be queried if a palette was not found in the current factory.
     * @param loader    An optional class to use for loading the palette definition files.
     *                  If {@code null}, loading will occurs from the system current working
     *                  directory.
     * @param directory The base directory for palette definition files. It may be a Java package
     *                  if a {@code loader} were specified. If {@code null}, then {@code "."} is
     *                  assumed.
     * @param extension File name extension, or {@code null} if there is no extension
     *                  to add to filename. If non-null, this extension will be automatically
     *                  appended to filename. It should starts with the {@code '.'} character.
     * @param charset   The charset to use for parsing files, or {@code null} for the default.
     * @param locale    The locale to use for parsing files. or {@code null} for the default.
     *
     * @since 2.2
     */
    public PaletteFactory(final PaletteFactory fallback,
                          final Class<?>       loader,
                          final File           directory,
                          final String         extension,
                          final Charset        charset,
                          final Locale         locale)
    {
        this.fallback    = fallback;
        this.classloader = null;
        this.loader      = loader;
        this.directory   = directory;
        this.extension   = startWithDot(extension);
        this.charset     = charset;
        this.locale      = locale;
    }

    /**
     * Ensures that the given string starts with a dot.
     */
    private static String startWithDot(String extension) {
        if (extension != null && !extension.startsWith(".")) {
            extension = '.' + extension;
        }
        return extension;
    }

    /**
     * Sets the locale to use for formatting warning or error messages. This is typically the
     * {@linkplain javax.imageio.ImageReader#getLocale image reader locale}. This locale is
     * informative only; there is no garantee that this locale will be really used.
     * <p>
     * This method sets the locale for the current thread only. It is safe to use this palette
     * factory concurrently in many threads, each with their own locale.
     *
     * @param warningLocale The locale for warning or error messages, or {@code null} for the
     *        default locale
     *
     * @since 2.4
     */
    public synchronized void setWarningLocale(final Locale warningLocale) {
        if (warningLocales == null) {
            if (warningLocale == null) {
                return;
            }
            warningLocales = warningLocales();
        }
        // TODO: use 'remove' on warningLocale==null when we will be allowed to compile for J2SE 1.5.
        warningLocales.set(warningLocale);
    }

    /**
     * Gets the {@linkplain #warningLocales} from the fallback or create a new one. This
     * method invokes itself recursively in order to assign the same {@link ThreadLocal}
     * to every factories in the chain.
     */
    private synchronized ThreadLocal<Locale> warningLocales() {
        if (warningLocales == null) {
            warningLocales = (fallback != null) ? fallback.warningLocales() : new ThreadLocal<Locale>();
        }
        return warningLocales;
    }

    /**
     * Returns the locale set by the last invocation to {@link #setWarningLocale} in the
     * current thread.
     *
     * @since 2.4
     */
    public Locale getWarningLocale() {
        final ThreadLocal<Locale> warningLocales = this.warningLocales;
        // Protected 'warningLocales' from changes so there is no need to synchronize.
        if (warningLocales != null) {
            return warningLocales.get();
        }
        return null;
    }

    /**
     * Returns the resources for formatting error messages.
     */
    final IndexedResourceBundle getErrorResources() {
        return Errors.getResources(getWarningLocale());
    }

    /**
     * Returns an input stream for reading the specified resource. The default
     * implementation delegates to the {@link Class#getResourceAsStream(String) Class} or
     * {@link ClassLoader#getResourceAsStream(String) ClassLoader} method of the same name,
     * according the {@code loader} argument type given to the constructor. Subclasses may
     * override this method if a more elaborated mechanism is wanted for fetching resources.
     * This is sometime required in the context of applications using particular class loaders.
     *
     * @param name The name of the resource to load, constructed as {@code directory} + {@code name}
     *             + {@code extension} where <var>directory</var> and <var>extension</var> were
     *             specified to the constructor, while {@code name} was given to the
     *             {@link #getPalette} method.
     * @return The input stream, or {@code null} if the resources was not found.
     *
     * @since 2.3
     */
    protected InputStream getResourceAsStream(final String name) {
        if (loader != null) {
            return loader.getResourceAsStream(name);
        }
        if (classloader != null) {
            return classloader.getResourceAsStream(name);
        }
        return null;
    }

    /**
     * Returns the list of available palette names. Any item in this list can be specified as
     * argument to {@link #getPalette}.
     *
     * @return The list of available palette name, or {@code null} if this method
     *         is unable to fetch this information.
     */
    public String[] getAvailableNames() {
        final Set<String> names = new TreeSet<String>();
        PaletteFactory factory = this;
        do {
            factory.getAvailableNames(names);
            factory = factory.fallback;
        } while (factory != null);
        return names.toArray(new String[names.size()]);
    }

    /**
     * Adds available palette names to the specified collection.
     */
    private void getAvailableNames(final Collection<String> names) {
        /*
         * First, parses the content of every "list.txt" files found on the classpath. Those files
         * are optional. But if they are present, we assume that their content are accurate.
         */
        String filename = new File(directory, LIST_FILE).getPath();
        BufferedReader in = getReader(LIST_FILE, "getAvailableNames");
        try {
            if (in != null) {
                readNames(in, names);
            }
            if (classloader != null) {
                for (final Enumeration<URL> it=classloader.getResources(filename); it.hasMoreElements();) {
                    final URL url = it.nextElement();
                    in = getReader(url.openStream());
                    readNames(in, names);
                }
            }
        } catch (IOException e) {
            /*
             * Logs a warning but do not stop. The only consequence is that the names list
             * will be incomplete. We log the message as if came from getAvailableNames(),
             * which is the public method that invoked this one.
             */
            Logging.unexpectedException(PaletteFactory.class, "getAvailableNames", e);
        }
        /*
         * After the "list.txt" files, check if the resources can be read as a directory.
         * It may happen if the classpath point toward a directory of .class files rather
         * than a JAR file.
         */
        File dir = (directory != null) ? directory : new File(".");
        if (classloader != null) {
            dir = toFile(classloader.getResource(dir.getPath()));
            if (dir == null) {
                // Directory not found.
                return;
            }
        } else if (loader != null) {
            dir = toFile(loader.getResource(dir.getPath()));
            if (dir == null) {
                // Directory not found.
                return;
            }
        }
        if (!dir.isDirectory()) {
            return;
        }
        final String[] list = dir.list(new DefaultFileFilter('*' + extension));
        final int extLg = extension.length();
        for (int i=0; i<list.length; i++) {
            filename = list[i];
            final int lg = filename.length();
            if (lg>extLg && filename.regionMatches(true, lg-extLg, extension, 0, extLg)) {
                names.add(filename.substring(0, lg-extLg));
            }
        }
    }

    /**
     * Copies the content of the specified reader to the specified collection.
     * The reader is closed after this operation.
     */
    private static void readNames(final BufferedReader in, final Collection<String> names)
            throws IOException
    {
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.length() != 0 && line.charAt(0) != '#') {
                names.add(line);
            }
        }
        in.close();
    }

    /**
     * Transforms an {@link URL} into a {@link File}. If the URL can't be
     * interpreted as a file, then this method returns {@code null}.
     */
    private static File toFile(final URL url) {
        if (url!=null && url.getProtocol().equalsIgnoreCase("file")) {
            return new File(url.getPath());
        }
        return null;
    }

    /**
     * Returns a buffered reader for the specified palette.
     *
     * @param  The palette's name to load. This name doesn't need to contains a path
     *         or an extension. Path and extension are set according value specified
     *         at construction time.
     * @return A buffered reader to read {@code name}, or {@code null} if the resource is not found.
     */
    private LineNumberReader getPaletteReader(String name) {
        if (extension!=null && !name.endsWith(extension)) {
            name += extension;
        }
        return getReader(name, "getPalette");
    }

    /**
     * Returns a buffered reader for the specified filename.
     *
     * @param  The filename. Path and extension are set according value specified
     *         at construction time.
     * @return A buffered reader to read {@code name}, or {@code null} if the resource is not found.
     */
    private LineNumberReader getReader(final String name, final String caller) {
        final File   file = new File(directory, name);
        final String path = file.getPath().replace(File.separatorChar, '/');
        InputStream stream;
        try {
            stream = getResourceAsStream(path);
            if (stream == null) {
                if (file.canRead()) try {
                    stream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    /*
                     * Should not occurs, since we checked for file existence. This is not a fatal
                     * error however, since this method is allowed to returns null if the resource
                     * is not available.
                     */
                    Logging.unexpectedException(PaletteFactory.class, caller, e);
                    return null;
                } else {
                    return null;
                }
            }
        } catch (SecurityException e) {
            Logging.recoverableException(PaletteFactory.class, caller, e);
            return null;
        }
        return getReader(stream);
    }

    /**
     * Wraps the specified input stream into a reader.
     */
    private LineNumberReader getReader(final InputStream stream) {
        return new LineNumberReader((charset != null) ?
            new InputStreamReader(stream, charset) : new InputStreamReader(stream));
    }

    /**
     * Reads the colors declared in the specified input stream. Colors must be encoded on 3 or 4
     * columns. If 3 columns, it is assumed RGB values. If 4 columns, it is assumed RGBA values.
     * Values must be in the 0-255 ranges. Empty lines and lines starting by {@code '#'} are
     * ignored.
     *
     * @param  input The stream to read.
     * @param  name  The palette name to read. Used for formatting error message only.
     * @return The colors.
     * @throws IOException if an I/O error occured.
     * @throws IIOException if a syntax error occured.
     */
    @SuppressWarnings("fallthrough")
    private Color[] getColors(final LineNumberReader input, final String name) throws IOException {
        int values[] = null;
        final LineFormat reader = (locale!=null) ? new LineFormat(locale) : new LineFormat();
        final List<Color> colors = new ArrayList<Color>();
        String line; while ((line=input.readLine()) != null) try {
            line = line.trim();
            if (line.length() == 0)        continue;
            if (line.charAt(0) == '#')     continue;
            if (reader.setLine(line) == 0) continue;
            values = reader.getValues(values);
            int A=255,R,G,B;
            switch (values.length) {
                case 4: A = byteValue(values[3]); // fall through
                case 3: B = byteValue(values[2]);
                        G = byteValue(values[1]);
                        R = byteValue(values[0]);
                        break;
                default: {
                    throw syntaxError(input, name, null);
                }
            }
            final Color color;
            try {
                color = new Color(R, G, B, A);
            } catch (IllegalArgumentException exception) {
                /*
                 * Color constructor checks the RGBA value and throws an IllegalArgumentException
                 * if they are not in the 0-255 range. Intercept this exception and rethrows as a
                 * checked IIOException, since we want to notify the user that the palette file is
                 * badly formatted. (additional note: it is somewhat redundant with byteValue(int)
                 * work. Lets keep it as a safety).
                 */
                throw syntaxError(input, name, exception);
            }
            colors.add(color);
        } catch (ParseException exception) {
            throw syntaxError(input, name, exception);
        }
        return colors.toArray(new Color[colors.size()]);
    }

    /**
     * Prepares an exception for the specified cause, which may be {@code null}.
     */
    private IIOException syntaxError(final LineNumberReader input, final String name, final Exception cause) {
        String message = getErrorResources().getString(
                ErrorKeys.BAD_LINE_IN_FILE_$2, name, input.getLineNumber());
        if (cause != null) {
            message += cause.getLocalizedMessage();
        }
        return new IIOException(message, cause);
    }

    /**
     * Loads colors from a definition file. If no colors were found in the current palette
     * factory and a fallback was specified at construction time, then the fallback will
     * be queried.
     *
     * @param  name The palette's name to load. This name doesn't need to contains a path
     *              or an extension. Path and extension are set according value specified
     *              at construction time.
     * @return The set of colors, or {@code null} if the set was not found.
     * @throws IOException if an error occurs during reading.
     * @throws IIOException if an error occurs during parsing.
     */
    public Color[] getColors(final String name) throws IOException {
        final LineNumberReader reader = getPaletteReader(name);
        if (reader == null) {
            return (fallback != null) ? fallback.getColors(name) : null;
        }
        final Color[] colors = getColors(reader, name);
        reader.close();
        return colors;
    }

    /**
     * Ensures that the specified valus is inside the {@code [0..255]} range.
     * If the value is outside that range, a {@link ParseException} is thrown.
     */
    private int byteValue(final int value) throws ParseException {
        if (value>=0 && value<256) {
            return value;
        }
        throw new ParseException(getErrorResources().getString(
                ErrorKeys.RGB_OUT_OF_RANGE_$1, value), 0);
    }

    /**
     * Returns the palette of the specified name and size. The palette's name doesn't need
     * to contains a directory path or an extension. Path and extension are set according
     * values specified at construction time.
     *
     * @param  name The palette's name to load.
     * @param  size The {@linkplain IndexColorModel index color model} size.
     * @return The palette.
     *
     * @since 2.4
     */
    public Palette getPalette(final String name, final int size) {
        return getPalette(name, 0, size, size, 1, 0);
    }

    /**
     * Returns a palette with a <cite>pad value</cite> at index 0.
     *
     * @param  name The palette's name to load.
     * @param  size The {@linkplain IndexColorModel index color model} size.
     * @return The palette.
     *
     * @since 2.4
     */
    public Palette getPalettePadValueFirst(final String name, final int size) {
        return getPalette(name, 1, size, size, 1, 0);
    }

    /**
     * Returns a palette with <cite>pad value</cite> at the last index.
     *
     * @param  name The palette's name to load.
     * @param  size The {@linkplain IndexColorModel index color model} size.
     * @return The palette.
     *
     * @since 2.4
     */
    public Palette getPalettePadValueLast(final String name, final int size) {
        return getPalette(name, 0, size-1, size, 1, 0);
    }

    /**
     * Returns the palette of the specified name and size. The RGB colors will be distributed
     * in the range {@code lower} inclusive to {@code upper} exclusive. Remaining pixel values
     * (if any) will be left to a black or transparent color by default.
     * <p>
     * The palette's name doesn't need to contains a directory path or an extension.
     * Path and extension are set according values specified at construction time.
     *
     * @param name  The palette's name to load.
     * @param lower Index of the first valid element (inclusive) in the
     *              {@linkplain IndexColorModel index color model} to be created.
     * @param upper Index of the last valid element (exclusive) in the
     *              {@linkplain IndexColorModel index color model} to be created.
     * @param size  The size of the {@linkplain IndexColorModel index color model} to be created.
     *              This is the value to be returned by {@link IndexColorModel#getMapSize}.
     * @param numBands    The number of bands (usually 1).
     * @param visibleBand The band to use for color computations (usually 0).
     * @return The palette.
     *
     * @since 2.4
     */
    public Palette getPalette(final String name, final int lower, final int upper, final int size,
                              final int numBands, final int visibleBand)
    {
        Palette palette = new IndexedPalette(this, name, lower, upper, size, numBands, visibleBand);
        palette = palettes.unique(palette);
        return palette;
    }

    /**
     * Creates a palette suitable for floating point values.
     *
     * @param name        The palette name.
     * @param minimum     The minimal sample value expected.
     * @param maximum     The maximal sample value expected.
     * @param dataType    The data type as a {@link java.awt.image.DataBuffer#TYPE_FLOAT}
     *                    or {@link java.awt.image.DataBuffer#TYPE_DOUBLE} constant.
     * @param numBands    The number of bands (usually 1).
     * @param visibleBand The band to use for color computations (usually 0).
     *
     * @since 2.4
     *
     * @todo Current implementation ignores the name and builds a gray scale in all cases.
     *       Future version may improve on that.
     */
    public Palette getContinuousPalette(final String name, final float minimum, final float maximum,
                                        final int dataType, final int numBands, final int visibleBand)
    {
        Palette palette = new ContinuousPalette(this, name, minimum, maximum, dataType, numBands, visibleBand);
        palette = palettes.unique(palette);
        return palette;
    }
}
