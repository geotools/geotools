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
package org.geotools.image.io.mosaic;

import java.util.*;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import org.geotools.util.Utilities;
import org.geotools.util.IntegerList;
import org.geotools.util.logging.Logging;
import org.geotools.util.FrequencySortedSet;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.UnmodifiableArrayList;


/**
 * A level of overview in a {@linkplain GridTileManager gridded tile manager}. Instances of this
 * class can not be created or modified by public methods.
 * <p>
 * <b>Note:</b> This class as a {@link #compareTo} method which is inconsistent with
 * {@link #equals}.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class OverviewLevel implements Comparable<OverviewLevel>, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -1441934881339348L;

    /**
     * The input types for which we will try to find a pattern.
     */
    private static final Set<Class<?>> INPUT_TYPES = new HashSet<Class<?>>(8);
    static {
        INPUT_TYPES.add(String.class);
        INPUT_TYPES.add(File  .class);
        INPUT_TYPES.add(URL   .class);
        INPUT_TYPES.add(URI   .class);
    }

    /**
     * A level with finer (smaller) subsampling value than this level, or {@code null} if none.
     * Will be set by {@link #createLinkedList}.
     */
    private OverviewLevel finer;

    /**
     * The overview level of this {@code OverviewLevel}. 0 is finest subsampling. Must match the
     * element index in the sorted {@code GridTileManager.levels} array.
     */
    private int ordinal;

    /**
     * The number of tiles along <var>x</var> and <var>y</var> axis.
     * Will be computed by {@link #createLinkedList}.
     */
    private int nx, ny;

    /**
     * Subsampling of every tiles at this level.
     */
    private final int xSubsampling, ySubsampling;

    /**
     * The location of the tile closest to origin, positive. They are <cite>relative</cite>
     * coordinates as used in public {@link Tile} API (i.e. those coordinates are <em>not</em>
     * pre-multiplied by {@link #xSubsampling} and {@link #ySubsampling}).
     */
    private final int xOffset, yOffset;

    /**
     * Size of every tiles at this level. They are <cite>relative</cite> size as used in
     * public {@link Tile} API (i.e. those coordinates are <em>not</em> pre-multiplied by
     * {@link #xSubsampling} and {@link #ySubsampling}).
     */
    private final int dx, dy;

    /**
     * The region of every tiles in this level. The {@linkplain Rectangle#x x} and
     * {@linkplain Rectangle#y y} coordinates are the upper-left corner of the (0,0)
     * tile. The {@linkplain Rectangle#width width} and {@linkplain Rectangle#height height}
     * are big enough for including every tiles.
     * <p>
     * They are <cite>relative</cite> coordinates as used in public {@link Tile} API
     * (i.e. those coordinates are <em>not</em> pre-multiplied by {@link #xSubsampling}
     * and {@link #ySubsampling}).
     */
    private final Rectangle mosaic;

    /**
     * On construction, the list of tiles {@linkplain #add added} in this level in no particular
     * order. After {@linkplain #createLinkedList processing}, the tiles that need to be retained
     * because they can not be created on the fly from the {@linkplain #patterns}, or {@code null}
     * if none.
     */
    private List<Tile> tiles;

    /**
     * The tiles to use as a pattern for creating tiles on the fly, or {@code null} if none.
     * If non-null, then the array length is typically 1. If greater than one, then the
     * {@linkplain #usePattern} field needs to be non-null in order to specify which pattern
     * is used.
     */
    private Tile[] patterns;

    /**
     * If there is more than one pattern, the index of pattern to use. Also used for signaling
     * holes in the mosaic if there is any.
     */
    private IntegerList patternUsed;

    /**
     * A sample tile which can be used as a pattern. This is just one amont many possible tiles.
     */
    private transient Tile sample;

    /**
     * Index of last pattern used. Used in order to avoid reinitializing
     * the {@linkplain #formatter} more often than needed.
     */
    private transient int lastPattern;

    /**
     * The formatter used for parsing and creating filename.
     */
    private transient FilenameFormatter formatter;

    /**
     * Creates a new level using the given pattern. The {@link #createLinkedList} method should
     * be invoked directly after this constructor, without prior calls to {@link #add}.
     *
     * @param  pattern The tile to use as a pattern.
     * @param  region  The region encompassing every tiles at this level, in relative coordinates.
     * @throws IOException if an error occured while reading tile information.
     */
    OverviewLevel(final Tile pattern, Rectangle region) throws IOException {
        final Dimension subsampling = pattern.getSubsampling();
        final Rectangle tile = pattern.getRegion();
        mosaic = region = new Rectangle(region);
        int x = tile.x % (dx = tile.width);
        int y = tile.y % (dy = tile.height);
        if (x < 0) x += dx;
        if (y < 0) y += dy;
        xOffset = x;
        yOffset = y;
        xSubsampling = subsampling.width;
        ySubsampling = subsampling.height;
        patterns = new Tile[] {
            pattern
        };
    }

    /**
     * Creates a new level with only one initial tile. More tiles will need to be added by
     * invoking {@link #add}, and {@link #createLinkedList} must be invoked when every tiles
     * are there.
     * <p>
     * The tile given to this constructor is particular in that it will defines the origin
     * and size of grid cells. It must be a typical tile, not a tile in the last column or
     * last row which may be smaller than typical tiles.
     *
     * @param tile The tile to wrap.
     * @param subsampling The tile subsampling, provided as an explicit argument only
     *        in order to avoid creating a temporary {@link Dimension} object again.
     * @throws IOException if an error occured while reading tile information.
     */
    OverviewLevel(final Tile tile, final Dimension subsampling) throws IOException {
        mosaic = tile.getRegion();
        int x = mosaic.x % (dx = mosaic.width);
        int y = mosaic.y % (dy = mosaic.height);
        if (x < 0) x += dx;
        if (y < 0) y += dy;
        xOffset = x;
        yOffset = y;
        assert subsampling.equals(tile.getSubsampling()) : subsampling;
        xSubsampling = subsampling.width;
        ySubsampling = subsampling.height;
        tiles = new ArrayList<Tile>();
        tiles.add(tile);
    }

    /**
     * Adds a tile to the list of tiles in this level, provided that they are aligned on the same
     * grid.
     *
     * @param  tile The tile to add.
     * @param  subsampling The tile subsampling, provided as an explicit argument only
     *         in order to avoid creating a temporary {@link Dimension} object again.
     * @throws IOException if an I/O operation was required and failed.
     * @throws IllegalArgumentException if the tiles are not aligned on the same grid.
     */
    final void add(final Tile tile, final Dimension subsampling)
            throws IOException, IllegalArgumentException
    {
        assert subsampling.equals(tile.getSubsampling()) : subsampling;
        assert subsampling.width == xSubsampling && subsampling.height == ySubsampling : subsampling;
        final Rectangle toAdd = tile.getRegion();
        if (toAdd.width > dx || toAdd.height > dy) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.UNEXPECTED_IMAGE_SIZE));
        }
        int ox = toAdd.x % dx;
        int oy = toAdd.y % dy;
        if (ox < 0) ox += dx;
        if (oy < 0) oy += dy;
        if ((ox -= xOffset) < 0 || (ox + toAdd.width)  > dx ||
            (oy -= yOffset) < 0 || (oy + toAdd.height) > dy)
        {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NOT_A_GRID));
        }
        mosaic.add(toAdd);
        tiles.add(tile);
    }

    /**
     * Once every tiles have been {@linkplain #add added} to this grid level, search for a pattern.
     *
     * @param ordinal The overview level of this {@code OverviewLevel}. 0 is finest subsampling.
     * @param finer A level with finer (smaller) subsampling value than this level, or {@code null}.
     * @throws MalformedURLException if an error occured while creating the URL for the tile.
     */
    final void createLinkedList(final int ordinal, final OverviewLevel finer)
            throws MalformedURLException
    {
        this.ordinal = ordinal;
        this.finer   = finer;
        assert getFinerLevel() == finer; // For running the assertions inside getFinerLevel().
        nx = (mosaic.width  + (dx - 1)) / dx;  // Round toward positive infinity.
        ny = (mosaic.height + (dy - 1)) / dy;
        assert (tiles == null) != (patterns == null); // Exactly one of those should be non-null.
        if (patterns != null) {
            /*
             * If this overview level has been created from a pattern, then we are done.
             */
            return;
        }
        /*
         * Searchs for the most common tuple of ImageReaderSpi, imageIndex, input pattern. The
         * rectangle below is named "size" because the (x,y) location is not representative.
         * The tiles that we failed to modelize by a pattern will be stored under the null key.
         */
        formatter = new FilenameFormatter();
        final Rectangle size = new Rectangle(xOffset, yOffset, dx, dy);
        final Map<Tile,List<Tile>> models = new HashMap<Tile,List<Tile>>();
        for (final Tile tile : tiles) {
            final String input = inputPattern(tile);
            final Tile model = (input != null) ? new Tile(tile, input, size) : null;
            List<Tile> similar = models.get(model);
            if (similar == null) {
                similar = new ArrayList<Tile>();
                models.put(model, similar);
            }
            similar.add(tile);
        }
        /*
         * If there is at least one tile that can not be processed, keep them in an array.
         * The array length is exactly (nx*ny) but contains only the elements that should
         * not be computed on the fly (other elements are null). Note that if the number
         * of elements to be computed on the fly is less than some arbitrary threshold,
         * it is not worth to compute them on the fly so we move them to the tiles list.
         */
        tiles = models.remove(null);
        for (final Iterator<List<Tile>> it = models.values().iterator(); it.hasNext();) {
            final List<Tile> similar = it.next();
            if (similar.size() < 4) {
                if (tiles == null) {
                    tiles = similar;
                } else {
                    tiles.addAll(similar);
                }
                it.remove();
            }
        }
        if (tiles != null) {
            tiles = UnmodifiableArrayList.wrap(toArray(tiles));
        }
        /*
         * If there is no recognized pattern, clears the unused fields and finish immediately
         * this method, so we skip the construction of "pattern used" list (which may be large).
         * Note that we clears the formatter unconditionnaly because the last pattern guessed
         * in the 'inputPattern' method may be wrong.
         */
        formatter = null;
        if (models.isEmpty()) {
            return;
        }
        /*
         * Sets the pattern index. Index in the 'tile' array are numbered from 0 (like usual),
         * but values in the 'patternUsed' list are numbered from 1 because we reserve the 0
         * value for non-existant tiles.
         */
        patterns = new Tile[models.size()];
        patternUsed = new IntegerList(nx*ny, patterns.length, true);
        int index = 0;
        for (final Map.Entry<Tile,List<Tile>> entry : models.entrySet()) {
            patterns[index++] = entry.getKey();
            for (final Tile tile : entry.getValue()) {
                final Point pt = getIndex2D(tile);
                final int i = getIndex(pt.x, pt.y);
                final int p = patternUsed.getInteger(i);
                if ((p != 0 && p != index) || (tiles != null && tiles.get(i) != null)) {
                    throw duplicatedTile(pt);
                }
                patternUsed.setInteger(i, index);
            }
        }
        /*
         * In the common case where there is only one pattern and no missing tiles,
         * clears the 'patternUsed' construct since we don't need it.
         */
        if (patterns.length == 1) {
            for (int i=patternUsed.size(); --i >= 0;) {
                if (patternUsed.getInteger(i) == 0) {
                    if (tiles == null || tiles.get(i) == null) {
                        // We have at least one hole, so we need to keep the list of them.
                        return;
                    }
                }
            }
            patternUsed = null;
        }
    }

    /**
     * Returns a pattern for the given tile. If no pattern can be found, returns {@code null}.
     * This method accepts only tile and input of specific types in order to be able to rebuild
     * later an exactly equivalent object from the pattern.
     *
     * @param  tile The tile to inspect for a pattern in the input object.
     * @return The pattern, or {@code null} if none.
     */
    private String inputPattern(final Tile tile) {
        /*
         * Accepts only instance of Tile (not a subclass), otherwise we will not know how to create
         * the instance on the fly. Once we have verified that the class is Tile, we are allowed to
         * check the tile size using the 'isSizeEquals' shortcut. We accept only tiles that fill
         * completly the cell size, otherwise we can not recreate the tile from a pattern.
         */
        if (!Tile.class.equals(tile.getClass()) || !tile.isSizeEquals(dx, dy)) {
            return null;
        }
        final Object input = tile.getInput();
        final Class<?> type = input.getClass();
        if (!INPUT_TYPES.contains(type)) {
            return null;
        }
        final Point index = getIndex2D(tile);
        String pattern = input.toString();
        pattern = formatter.guessPattern(ordinal, index.x, index.y, pattern);
        if (pattern != null) {
            pattern = type.getSimpleName() + ':' + pattern;
        }
        return pattern;
    }

    /**
     * Formats an exception for a duplicated tile.
     *
     * @param  pt The upper-left corner coordinate.
     * @return An exception formatted for a duplicated tile at the given coordinate.
     */
    private static IllegalArgumentException duplicatedTile(final Point pt) {
        return new IllegalArgumentException(Errors.format(
                ErrorKeys.DUPLICATED_VALUES_$1, "location=" + pt.x + ',' + pt.y));
    }

    /**
     * Removes the tile at the given index. Current implementation can remove only tiles
     * created from a pattern.
     */
    final void removeTile(final int x, final int y) {
        final int i = getIndex(x, y);
        assert tiles == null || tiles.get(i) == null;
        if (patternUsed == null) {
            patternUsed = new IntegerList(nx*ny, patterns.length, true);
            patternUsed.fill(1);
        }
        patternUsed.setInteger(i, 0);
    }

    /**
     * Expands the given tiles in a flat array. Tiles are stored by their index, with
     * <var>x</var> index varying faster.
     */
    private Tile[] toArray(final Collection<Tile> tiles) {
        final Tile[] array = new Tile[nx * ny];
        for (final Tile tile : tiles) {
            final Point pt = getIndex2D(tile);
            final int index = getIndex(pt.x, pt.y);
            if (array[index] != null && !tile.equals(array[index])) {
                throw duplicatedTile(pt);
            }
            array[index] = tile;
        }
        return array;
    }


    /////////////////////////////////////////////////////////////////////////////////
    ////                                                                         ////
    ////    End of construction methods. The remainding is for querying only.    ////
    ////    None of the methods below should modify the OverviewLevel state.     ////
    ////                                                                         ////
    /////////////////////////////////////////////////////////////////////////////////


    /**
     * Converts the search rectangle from <cite>absolute space</cite> to
     * <cite>tile index space</cite>. Index can not be negative neither
     * greater than ({@linkplain #nx},@linkplain #ny}). The (xmin,ymin)
     * index are inclusive while the (xmax,ymax) index are exclusive.
     *
     * @param search The search region in absolute coordinate. This rectangle will not be modified.
     * @return The search region as tile index.
     */
    private Rectangle toTileIndex(final Rectangle search) {
        final Rectangle index = new Rectangle(dx * xSubsampling, dy * ySubsampling);

        // Computes min values.
        int x = search.x - mosaic.x * xSubsampling;
        int y = search.y - mosaic.y * ySubsampling;
        if (x >= 0) index.x = x / index.width;  // Otherwise lets (x,y) to its default value (0).
        if (y >= 0) index.y = y / index.height;

        // Computes max values. We round (width,height) toward higher integer.
        x += search.width;
        y += search.height;
        index.width  = Math.min(nx, (x + (index.width  - 1)) / index.width)  - index.x;
        index.height = Math.min(ny, (y + (index.height - 1)) / index.height) - index.y;
        return index;
    }

    /**
     * Returns the index of the given tile. The tile in the upper-left corner has index (0,0).
     *
     * @param  tile The tile for which to get the index.
     * @return The index in a two-dimensional grid.
     */
    private Point getIndex2D(final Tile tile) {
        final Point location = tile.getLocation();
        location.x -= mosaic.x;
        location.y -= mosaic.y;
        assert (location.x % dx == 0) && (location.y % dy == 0) : location;
        location.x /= dx;
        location.y /= dy;
        return location;
    }

    /**
     * Returns the flat index for the given 2D index.
     *
     * @param  x,y The tile location, with (0,0) as the upper-left tile.
     * @return The corresponding index in a flat array.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     */
    private int getIndex(final int x, final int y) throws IndexOutOfBoundsException {
        if (x < 0 || x >= nx || y < 0 || y >= ny) {
            throw new IndexOutOfBoundsException(Errors.format(
                    ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, "(" + x + ',' + y + ')'));
        }
        return y * nx + x;
    }

    /**
     * Returns a level finer than this level, or {@code null} if this level is already the finest
     * one.
     *
     * @return The next level toward finer ones, or {@code null} if none.
     */
    public OverviewLevel getFinerLevel() {
        assert ((finer != null) ? (ordinal > 0) : (ordinal == 0)) : ordinal;
        assert (finer == null) || (compareTo(finer) >= 0 && finer.ordinal == ordinal-1) : finer;
        return finer;
    }

    /**
     * Returns the number of tiles at this level.
     *
     * @return The number of tiles.
     */
    public int getNumTiles() {
        int count = 0;
        if (patterns != null) {
            count = nx * ny;
            if (patternUsed != null) {
                count -= patternUsed.occurence(0);
            }
        } else if (tiles != null) {
            for (final Tile tile : tiles) {
                if (tile != null) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the number of tiles along the <var>x</var> axis.
     *
     * @return The number of tiles in a row.
     */
    public final int getNumXTiles() {
        return nx;
    }

    /**
     * Returns the number of tiles along the <var>y</var> axis.
     *
     * @return The number of tiles in a column.
     */
    public final int getNumYTiles() {
        return ny;
    }

    /**
     * If there is more than one tile, returns the tile size. Otherwise returns {@code null}.
     * This special condition on the number of tile exists for {@link GridTileManager}
     * implementation convenience.
     *
     * @return The tile size, or {@code null} if there is only one tile.
     */
    public Dimension getTileSize() {
        if (mosaic.width > dx || mosaic.height > dy) {
            return new Dimension(dx, dy);
        }
        return null;
    }

    /**
     * Returns the tiles bounding box in <cite>absolute</cite> coordinates. This is
     * the bounding box that this level would have if its subsampling was 1.
     *
     * @return The region in absolute coordinates.
     */
    public Rectangle getAbsoluteRegion() {
        return new Rectangle(xSubsampling * mosaic.x,
                             ySubsampling * mosaic.y,
                             xSubsampling * mosaic.width,
                             ySubsampling * mosaic.height);
    }

    /**
     * Returns {@code true} if the given bounds (in absolute coordinates) matches exactly the
     * region of a tile or a group of tiles at this level. This method do not checks if the
     * tiles actually exist.
     *
     * @param bounds The bounds to test.
     * @return {@code true} if the given bounds matches tiles bounds.
     */
    private boolean isAbsoluteTilesRegion(final Rectangle bounds) {
        final int width = dx * xSubsampling; // Tile width in "absolute" units.
        if (bounds.width % width == 0) {
            final int height = dy * ySubsampling; // Tile height in "absolute" units.
            if (bounds.height % height == 0) {
                return (bounds.x - xOffset*xSubsampling) % width  == 0 &&
                       (bounds.y - yOffset*ySubsampling) % height == 0;
            }
        }
        return false;
    }

    /**
     * Returns a sample tile. The tile may be {@linkplain Tile#getLocation located} anywhere,
     * and the {@linkplain Tile#getInput tile input} may not be usuable (it may be only a
     * pattern for creating input on the fly).
     */
    public Tile getSampleTile() {
        if (sample == null) {
            if (patterns != null) {
                /*
                 * Should never be empty and should never contains null elements (but we still do
                 * a loop for paranoia). If we get an IndexOutOfBoundsException, then it would be
                 * a bug in the createLinkedList(...) method.
                 */
                int i = 0;
                do {
                    sample = patterns[i++];
                } while (sample == null);
            } else {
                /*
                 * Should never be null when patterns == null and never empty. It may contains
                 * some null elements, but at least one element should be nun-null. If we get a
                 * NullPointerException or an IndexOutOfBoundsException, then it would be a bug
                 * in the createLinkedList(...) method.
                 */
                int i = 0;
                do {
                    sample = tiles.get(i++);
                } while (sample == null);
            }
        }
        return sample;
    }

    /**
     * Returns the tile at the given index.
     *
     * @param  x,y The tile location, with (0,0) as the upper-left tile.
     * @return The tile at the given location, or {@code null} if none.
     * @throws IndexOutOfBoundsException if the given index is out of bounds.
     * @throws MalformedURLException if an error occured while creating the URL for the tile.
     */
    final Tile getTile(int x, int y) throws IndexOutOfBoundsException, MalformedURLException {
        final int index = getIndex(x, y);
        /*
         * Checks for fully-created instance. Those instances are expected to exist if
         * some tile do not comply to a general pattern that this class can recognize.
         */
        if (tiles != null) {
            final Tile tile = tiles.get(index);
            if (tile != null) {
                // If a tile is explicitly defined, it should not have a pattern.
                assert patternUsed == null || patternUsed.getInteger(index) == 0 : index;
                return tile;
            }
            // Tests here because it would be an error to have null patterns when tiles == null,
            // so we are better to lets NullPointerException been thrown in such case so we can
            // debug.
            if (patterns == null) {
                return null;
            }
        }
        /*
         * The requested tile does not need to be handled in a special way, so now get the
         * pattern for this tile and generate the filename of the fly. Doing so avoid the
         * consumption of memory for the thousands of tiles we may have.
         */
        int p = 0;
        if (patternUsed != null) {
            p = patternUsed.get(index);
            if (p == 0) {
                return null;
            }
            p--;
        }
        final Tile tile = patterns[p];
        final String pattern = tile.getInput().toString();
        if (formatter == null) {
            formatter = new FilenameFormatter();
            lastPattern = -1;
        }
        if (p != lastPattern) {
            formatter.applyPattern(pattern.substring(pattern.indexOf(':') + 1));
            lastPattern = p;
        }
        final String filename = formatter.generateFilename(ordinal, x, y);
        /*
         * We now have the filename to be given to the tile. Creates the appropriate object
         * (File, URL, URI or String) from it.
         */
        final Object input;
        if (pattern.startsWith("File")) {
            input = new File(filename);
        } else if (pattern.startsWith("URL")) {
            input = new URL(filename);
        } else if (pattern.startsWith("URI")) try {
            input = new URI(filename);
        } catch (URISyntaxException cause) { // Rethrown as an IOException subclass.
            MalformedURLException e = new MalformedURLException(cause.getLocalizedMessage());
            e.initCause(cause);
            throw e;
        } else {
            input = filename;
        }
        assert INPUT_TYPES.contains(input.getClass()) : input;
        /*
         * Now creates the definitive tile. The tiles in the last
         * row or last column may be smaller than other tiles.
         */
        final Rectangle bounds = new Rectangle(
                mosaic.x + (x *= dx),
                mosaic.y + (y *= dy),
                Math.min(dx, mosaic.width  - x),
                Math.min(dy, mosaic.height - y));
        return new Tile(tile, input, bounds);
    }

    /**
     * Adds all internal tiles to the given set, together with their frequency.
     *
     * @param addTo The collection where to add the internal tiles.
     */
    final void getInternalTiles(final FrequencySortedSet<? super Tile> addTo) {
        int count = 0;
        if (tiles != null) {
            for (final Tile tile : tiles) {
                if (tile != null) {
                    addTo.add(tile);
                    count++;
                }
            }
        }
        if (patterns != null) {
            for (int p=0; p<patterns.length;) {
                final Tile tile = patterns[p++];
                final int n = (patternUsed != null) ? patternUsed.occurence(p) : nx*ny - count;
                addTo.add(tile, n);
            }
        }
    }

    /**
     * Adds to the given list every tiles that intersect the given region. This is
     * caller responsability to ensure that this level uses the subsampling of interest.
     *
     * @param  addTo The list where to add the tiles.
     * @param  search The region of interest in absolute coordinates.
     * @param  subsampling The subsampling to apply on the tiles to be read.
     *         Used for cost calculation.
     * @param  costLimit If reading the returned tiles would have a cost equals or higher
     *         than the given number, stop the search and returns {@code null}.
     * @return The cost of reading the tiles, or {@code -1} if the cost limit has been reached
     *         (in which case no tiles has been added to the list).
     * @throws IOException if an error occured while creating the URL for the tiles.
     */
    final long getTiles(final ArrayList<Tile> addTo, final Rectangle search,
            final Dimension subsampling, final long costLimit) throws IOException
    {
        final Rectangle atr = toTileIndex(search);
        final int xmin = atr.x;
        final int ymin = atr.y;
        final int xmax = atr.width  + xmin;
        final int ymax = atr.height + ymin;
        /*
         * Recycles the rectangle created by toTileIndex. The "atr" name stands for "Absolute
         * Tile Region". Width and height will not change anymore. X and y will be set later.
         */
        atr.width  = dx * xSubsampling;
        atr.height = dy * ySubsampling;
        final int size = addTo.size();
        if (size == 0) {
            final int n = (xmax - xmin) * (ymax - ymin);
            addTo.ensureCapacity(n);
        }
        /*
         * Creates the destination array with a capacity equals to the maximal number of tiles
         * expected at this level. The array may not be filled completly if the iteration gets
         * some null tiles. The array way also expand belong the expected "maximal" size if we
         * put tiles from finer levels into the mix (as the loop below may do).
         */
        long totalCost = 0;
        for (int y=ymin; y<ymax; y++) {
nextTile:   for (int x=xmin; x<xmax; x++) {
                final Tile tile = getTile(x, y);
                if (tile == null) {
                    continue;
                }
                /*
                 * We have found a tile to add to the list. Before doing so, computes the cost
                 * of reading this tile and checks if reading a tile at a finer level would be
                 * cheaper.
                 */
                final long cost = tile.countUnwantedPixelsFromAbsolute(search, subsampling);
                if (cost != 0) {
                    totalCost += cost;
                    if (totalCost >= costLimit) {
                        /*
                         * The new tile increases the cost above the limit. Forget the tiles found
                         * so far and cancel the search. Note that it is theorically possible that
                         * the search in finer levels (code below) finds cheaper tiles which would
                         * have allowed us to stay below the cost limit. We could have enabled this
                         * case by performing this check at the end of the loop rather than now.
                         * However doing so implies that every levels are tested recursively down
                         * to the finest level. We have more to gain by stopping this method early
                         * instead.
                         */
                        addTo.subList(size, addTo.size()).clear();
                        return -1;
                    }
                    atr.x = atr.width  * x;
                    atr.y = atr.height * y;
                    assert atr.equals(tile.getAbsoluteRegion()) ||
                            !tile.getClass().equals(Tile.class) : atr;
                    OverviewLevel previous = this;
                    while ((previous = previous.getFinerLevel()) != null) {
                        if (!previous.isAbsoluteTilesRegion(atr)) {
                            continue;
                        }
                        final Rectangle clipped = atr.intersection(search);
                        final long c = previous.getTiles(addTo, clipped, subsampling, cost);
                        if (c >= 0) {
                            // Tiles at the finer level are cheaper than the current tiles. So keep
                            // them (they have been added to the 'addTo' array) and discart 'tile'.
                            totalCost += (c - cost);
                            continue nextTile;
                        }
                        break;
                    }
                }
                addTo.add(tile);
            }
        }
        assert (addTo.size() > size) == intersects(search);
        return totalCost;
    }

    /**
     * Returns {@code true} if at least one tile intersects the given region.
     * This method does not search recursively into finer levels.
     *
     * @param  search The region (in absolute coordinates) where to search for tiles.
     * @return {@code true} if at least one tile intersects the given region.
     * @throws IOException if an error occured while fetching a tile size.
     */
    final boolean intersects(final Rectangle search) throws IOException {
        final Rectangle index = toTileIndex(search);
        final int xmin = index.x;
        final int ymin = index.y;
        final int xmax = index.width  + xmin;
        final int ymax = index.height + ymin;
        for (int y=ymin; y<ymax; y++) {
            for (int x=xmin; x<xmax; x++) {
                final int i = getIndex(x, y);
                if (tiles != null) {
                    final Tile tile = tiles.get(i);
                    if (tile != null) {
                        if (search.intersects(tile.getAbsoluteRegion())) {
                            return true;
                        } else {
                            continue;
                        }
                    }
                    // If there is an explicit list of tiles, we may have no pattern. In this
                    // case we don't want to return 'true' on the 'patternUsed' check below.
                    if (patterns == null) {
                        continue;
                    }
                }
                if (patternUsed == null || patternUsed.get(i) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns {@code true} if this level or any finer level contains the given tile. This method
     * is static in order to prevent accidental usage of implicit {@code this}, which would be a
     * bug. At the different of other (non-static) methods, this one is recursive.
     *
     * @param  tile The tile to check for inclusion.
     * @reutrn {@code true} if this manager contains the given tile.
     */
    static final boolean contains(OverviewLevel level, final Tile tile) {
        final Dimension subsampling = tile.getSubsampling();
        while (level != null) {
            if (level.xSubsampling == subsampling.width && level.ySubsampling == subsampling.height) {
                final Point index = level.getIndex2D(tile);
                if (index.x >= 0 && index.x < level.nx && index.y >= 0 && index.y < level.ny) try {
                    // Reminder: level.getTile(x,y) may returns null.
                    return tile.equals(level.getTile(index.x, index.y));
                } catch (MalformedURLException e) {
                    // If we can't format the name, then it is different than the given tile
                    // input otherwise the user wouldn't have been able to create that tile.
                    Logging.recoverableException(OverviewLevel.class, "contains", e);
                }
                break;
            }
            level = level.getFinerLevel();
        }
        return false;
    }

    /**
     * Compares subsamplings, sorting smallest areas first. If two subsamplings have the
     * same area, sorts by <var>xSubsampling</var> first then by <var>ySubsampling</var>.
     * <p>
     * The algorithm applied in this method must be identical to {@link #compareTo(OverviewLevel)}.
     */
    public int compareTo(final Dimension subsampling) {
        int c = (xSubsampling * ySubsampling) - (subsampling.width * subsampling.height);
        if (c == 0) {
            c = xSubsampling - subsampling.width;
            if (c == 0) {
                c = ySubsampling - subsampling.height;
            }
        }
        return c;
    }

    /**
     * Compares subsamplings, sorting smallest areas first. If two subsamplings have the
     * same area, sorts by <var>xSubsampling</var> first then by <var>ySubsampling</var>.
     * <p>
     * The algorithm applied in this method must be identical to {@link #compareTo(Dimension)}.
     */
    public int compareTo(final OverviewLevel other) {
        int c = (xSubsampling * ySubsampling) - (other.xSubsampling * other.ySubsampling);
        if (c == 0) {
            c = xSubsampling - other.xSubsampling;
            if (c == 0) {
                c = ySubsampling - other.ySubsampling;
            }
        }
        return c;
    }

    /**
     * Compares this overview level with the given object for equality.
     *
     * @param  other The other object to compare for equality.
     * @return {@code true} if the given object is equals to this overview level.
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof OverviewLevel) {
            final OverviewLevel that = (OverviewLevel) other;
            return ordinal == that.ordinal &&
                   dx == that.dx && dy == that.dy && nx == that.nx && ny == that.ny &&
                   xSubsampling == that.xSubsampling && ySubsampling == that.ySubsampling &&
                   xOffset == that.xOffset && yOffset == that.yOffset &&
                   Utilities.equals(this.mosaic,      that.mosaic)   &&
                   Utilities.equals(this.tiles,       that.tiles)    &&
                   Arrays   .equals(this.patterns,    that.patterns) &&
                   Utilities.equals(this.patternUsed, that.patternUsed) &&
                   Utilities.equals(this.finer,       that.finer);
        }
        return false;
    }

    /**
     * Returns a hash code value for this overview level.
     */
    @Override
    public int hashCode() {
        int code = ordinal + 37 * (xSubsampling + 37 * (ySubsampling + Arrays.hashCode(patterns)));
        if (finer != null) {
            code += 31 * finer.hashCode();
        }
        return code;
    }

    /**
     * Returns a string representation for debugging purpose.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + ordinal + ", subsampling=(" +
                xSubsampling + ',' + ySubsampling + "), " + getNumTiles() + " tiles]";
    }
}
