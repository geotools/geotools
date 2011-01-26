/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;

import org.geotools.coverage.grid.ImageGeometry;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.util.logging.Logging;


/**
 * Creates a collection of {@link Tile tiles} from their <cite>grid to CRS</cite> affine transform.
 * When the {@linkplain Rectangle rectangle} that describe the destination region is known for every
 * tiles, {@linkplain Tile#Tile(ImageReader,Object,int,Rectangle,Dimension) tile constructor} can be
 * invoked directly. But in some cases the destination region is not known directly. Instead we have
 * a set of {@linkplain java.awt.image.BufferedImage buffered images} with a (0,0) location for each
 * of them, and different <cite>grid to CRS</cite> affine transforms. This {@code RegionCalculator}
 * class infers the destination regions automatically from the set of affine transforms.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class RegionCalculator {
    /**
     * Small number for floating point comparisons.
     */
    private static final double EPS = 1E-10;

    /**
     * The location of the final bounding box (the one including every tiles).
     * Tiles will be translated as needed in order to fit this location.
     */
    private final int xLocation, yLocation;

    /**
     * Tiles for which we should compute the bounding box only when we have them all.
     * Their bounding box (region) will need to be adjusted for the affine transform.
     */
    private final Map<AffineTransform,Tile> tiles;

    /**
     * Creates an initially empty tile collection with the location set to (0,0).
     */
    public RegionCalculator() {
        this(null);
    }

    /**
     * Creates an initially empty tile collection with the given location.
     *
     * @param location The location, or {@code null} for (0,0).
     */
    public RegionCalculator(final Point location) {
        if (location != null) {
            xLocation = location.x;
            yLocation = location.y;
        } else {
            xLocation = yLocation = 0;
        }
        // We really need an IdentityHashMap, not an ordinary HashMap, because we will
        // put many AffineTransforms that are equal in the sense of Object.equals  but
        // we still want to associate them to different Tile instances.
        tiles = new IdentityHashMap<AffineTransform,Tile>();
    }

    /**
     * Returns the location of the tile collections to be created. The location is usually (0,0)
     * which match the {@linkplain java.awt.image.BufferedImage buffered image} location, but
     * it doesn't have to.
     */
    public Point getLocation() {
        return new Point(xLocation, yLocation);
    }

    /**
     * Adds a tile to the collection of tiles to process.
     *
     * @param  tile The tile to add.
     * @return {@code true} if the tile has been successfully added, or {@code false}
     *         if the tile doesn't need to be processed by this class.
     */
    public boolean add(final Tile tile) {
        final AffineTransform gridToCRS;
        synchronized (tile) {
            gridToCRS = tile.getPendingGridToCRS(true);
        }
        if (gridToCRS == null) {
            return false;
        }
        if (tiles.put(gridToCRS, tile) != null) {
            throw new AssertionError(); // Should never happen.
        }
        return true;
    }

    /**
     * Returns the tiles. Keys are grid geometry (containing image bounds and <cite>grid to
     * coordinate reference system</cite> transforms) and values are the tiles. This method
     * usually returns a singleton map, but more entries may be present if this method was
     * not able to build a single pyramid using all provided tiles.
     * <p>
     * <strong>Invoking this method flush the collection</strong>. On return, this instance
     * is in the same state as if {@link #clear} has been invoked. This is because current
     * implementation modify its workspace directly for efficiency.
     */
    public Map<ImageGeometry,Tile[]> tiles() {
        final Map<ImageGeometry,Tile[]> results = new HashMap<ImageGeometry,Tile[]>(4);
        for (final Map<AffineTransform,Dimension> tilesAT : computePyramidLevels(tiles.keySet())) {
            /*
             * Picks an affine transform to be used as the reference one. We need the finest one.
             * If more than one have the finest resolution, the exact choice does not matter much.
             * But we will save a little bit of CPU if we pickup the one that will lead to a (0,0)
             * translation at the end of this method.
             */
            AffineTransform reference = null;
            double xMin  = Double.POSITIVE_INFINITY;
            double xLead = Double.POSITIVE_INFINITY; // Minimum on the first row only.
            double yMin  = Double.POSITIVE_INFINITY;
            double scale = Double.POSITIVE_INFINITY;
            for (final AffineTransform tr : tilesAT.keySet()) {
                final double s = XAffineTransform.getScale(tr);
                double y = tr.getTranslateY(); if (tr.getScaleY() < 0 || tr.getShearY() < 0) y = -y;
                double x = tr.getTranslateX(); if (tr.getScaleX() < 0 || tr.getShearX() < 0) x = -x;
                if (!(Math.abs(s - scale) <= EPS)) {
                    if (!(s < scale)) continue;  // '!' is for catching NaN.
                    scale = s; // Found a smaller scale.
                    yMin = y;
                    xMin = x;
                } else { // Found a transform with the same scale.
                    if (x < xMin) xMin = x;
                    if (!(Math.abs(y - yMin) <= EPS)) {
                        if (!(y < yMin)) continue;
                        yMin = y; // Found a smaller y.
                    } else if (!(x < xLead)) continue;
                }
                xLead = x;
                reference = tr;
            }
            /*
             * If there is missing tiles at the begining of the first row, then the x location
             * of the first tile is greater than the "true" minimum. We will need to adjust.
             */
            if (reference == null) {
                continue;
            }
            xLead -= xMin;
            if (xLead > EPS) {
                final double[] matrix = new double[6];
                reference.getMatrix(matrix);
                matrix[4] -= xLead;
                reference = new AffineTransform(matrix);
            } else {
                reference = new AffineTransform(reference); // Protects from upcomming changes.
            }
            /*
             * Transforms the image bounding box from its own space to the reference space. If
             * 'computePyramidLevels' did its job correctly, the transform should contains only
             * a scale and translation - no shear (we don't put assertions because of rounding
             * errors). In such particular case, transforming a Rectangle2D is accurate. We
             * round (we do not clip as in the default Rectangle implementation) because we
             * really expect integer results.
             */
            final AffineTransform toGrid;
            try {
                toGrid = reference.createInverse();
            } catch (NoninvertibleTransformException e) {
                throw new IllegalStateException(e);
            }
            int index = 0;
            Rectangle groupBounds = null;
            final Rectangle2D.Double envelope = new Rectangle2D.Double();
            final Tile[] tilesArray = new Tile[tilesAT.size()];
            for (final Map.Entry<AffineTransform,Dimension> entry : tilesAT.entrySet()) {
                final AffineTransform tr = entry.getKey();
                Tile tile = tiles.remove(tr); // Should never be null.
                tr.preConcatenate(toGrid);
                /*
                 * Computes the transformed bounds. If we fail to obtain it, there is probably
                 * something wrong with the tile (typically a wrong filename) but this is not
                 * fatal to this method. In such case, we will transform only the location instead
                 * of the full box, which sometime imply a lost of accuracy but not always. Note
                 * that the user is likely to obtains the same exception if the MosaicImageReader
                 * attempts to read the same tile (but as long as it doesn't, it may work).
                 */
                Rectangle bounds;
                synchronized (tile) {
                    tile.setSubsampling(entry.getValue());
                    try {
                        bounds = tile.getRegion();
                    } catch (IOException exception) {
                        bounds = null;
                        Logging.unexpectedException(RegionCalculator.class, "tiles", exception);
                    }
                    if (bounds != null) {
                        XAffineTransform.transform(tr, bounds, envelope);
                        bounds.x      = (int) Math.round(envelope.x);
                        bounds.y      = (int) Math.round(envelope.y);
                        bounds.width  = (int) Math.round(envelope.width);
                        bounds.height = (int) Math.round(envelope.height);
                    } else {
                        final Point location = tile.getLocation();
                        tr.transform(location, location);
                        bounds = new Rectangle(location.x, location.y, 0, 0);
                    }
                    tile.setAbsoluteRegion(bounds);
                }
                if (groupBounds == null) {
                    groupBounds = bounds;
                } else {
                    groupBounds.add(bounds);
                }
                tilesArray[index++] = tile;
            }
            tilesAT.clear(); // Lets GC do its work.
            /*
             * Translates the tiles in such a way that the upper-left corner has the coordinates
             * specified by (xLocation, yLocation). Adjusts the tile affine transform concequently.
             * After this block, tiles having the same subsampling will share the same immutable
             * affine transform instance.
             */
            if (groupBounds != null) {
                final int dx = xLocation - groupBounds.x;
                final int dy = yLocation - groupBounds.y;
                if (dx != 0 || dy != 0) {
                    reference.translate(-dx, -dy);
                    groupBounds.translate(dx, dy);
                }
                final ImageGeometry geometry = new ImageGeometry(groupBounds, reference);
                reference = geometry.getGridToCRS(); // Fetchs the immutable instance.
                final Map<Dimension,TranslatedTransform> pool =
                        new HashMap<Dimension,TranslatedTransform>();
                for (final Tile tile : tilesArray) {
                    final Dimension subsampling = tile.getSubsampling();
                    TranslatedTransform translated = pool.get(subsampling);
                    if (translated == null) {
                        translated = new TranslatedTransform(subsampling, reference, dx, dy);
                        pool.put(subsampling, translated);
                    }
                    translated.applyTo(tile);
                }
                results.put(geometry, tilesArray);
            }
        }
        return results;
    }

    /**
     * Sorts affine transform by increasing X scales in absolute value.
     * For {@link #computePyramidLevels} internal working only.
     */
    private static final Comparator<AffineTransform> X_COMPARATOR = new Comparator<AffineTransform>() {
        public int compare(final AffineTransform tr1, final AffineTransform tr2) {
            return Double.compare(XAffineTransform.getScaleX0(tr1), XAffineTransform.getScaleX0(tr2));
        }
    };

    /**
     * Sorts affine transform by increasing Y scales in absolute value.
     * For {@link #computePyramidLevels} internal working only.
     */
    private static final Comparator<AffineTransform> Y_COMPARATOR = new Comparator<AffineTransform>() {
        public int compare(final AffineTransform tr1, final AffineTransform tr2) {
            return Double.compare(XAffineTransform.getScaleY0(tr1), XAffineTransform.getScaleY0(tr2));
        }
    };

    /**
     * From a set of arbitrary affine transforms, computes pyramid levels that can be given to
     * {@link Tile} constructors. This method tries to locate the affine transform with finest
     * resolution. This is typically (but not always, depending on rotation or axis flip) the
     * transform with smallest {@linkplain AffineTransform#getScaleX scale X} and {@linkplain
     * AffineTransform#getScaleY scale Y} coefficients in absolute value. This transform is
     * given a dimension of (1,1) and stored in an {@linkplain IdentityHashMap identity hash
     * map}. Other transforms are stored in the same map with their dimension relative to the
     * first one, or discarded if the scale ratio is not an integer. In the later case, the
     * transforms that were discarded from the first pass will be put in a new map to be added
     * as the second element in the returned list. A new pass is run, discarded transforms from
     * the second pass are put in the third element of the list, <cite>etc</cite>.
     *
     * @param  gridToCRS The <cite>grid to CRS</cite> affine transforms computed from the
     *         image to use in a pyramid. The collection and the transform elements are not
     *         modified by this method (they may be modified by the caller however).
     * @return A subset of the given transforms with their relative resolution. This method
     *         typically returns one map, but more could be returned if the scale ratio is
     *         not an integer for every transforms.
     */
    private static List<Map<AffineTransform,Dimension>> computePyramidLevels(
            final Collection<AffineTransform> gridToCRS)
    {
        final List<Map<AffineTransform,Dimension>> results =
                new ArrayList<Map<AffineTransform,Dimension>>(2);
        /*
         * First, computes the pyramid levels along the X axis. Transforms that we were unable
         * to classify will be discarded from the first run and put in a subsequent run.
         */
        AffineTransform[] transforms = gridToCRS.toArray(new AffineTransform[gridToCRS.size()]);
        Arrays.sort(transforms, X_COMPARATOR);
        int length = transforms.length;
        while (length != 0) {
            final Map<AffineTransform,Dimension> result =
                    new IdentityHashMap<AffineTransform,Dimension>();
            if (length <= (length = computePyramidLevels(transforms, length, result, false))) {
                throw new AssertionError(length); // Should always be decreasing.
            }
            results.add(result);
        }
        /*
         * Next, computes the pyramid levels along the Y axis. If we fail to compute the
         * pyramid level for some AffineTransform, they will be removed from the map. If
         * a map became empty because of that, the whole map will be removed.
         */
        final Iterator<Map<AffineTransform,Dimension>> iterator = results.iterator();
        while (iterator.hasNext()) {
            final Map<AffineTransform,Dimension> result = iterator.next();
            length = result.size();
            transforms = result.keySet().toArray(transforms);
            Arrays.sort(transforms, 0, length, Y_COMPARATOR);
            length = computePyramidLevels(transforms, length, result, true);
            while (--length >= 0) {
                if (result.remove(transforms[length]) == null) {
                    throw new AssertionError(length);
                }
            }
            if (result.isEmpty()) {
                iterator.remove();
            }
        }
        return results;
    }

    /**
     * Computes the pyramid level for the given affine transforms along the X or Y axis, and
     * stores the result in the given map.
     *
     * @param  gridToCRS The AffineTransform to analyse. This array <strong>must</strong> be
     *                   sorted along the dimension specified by {@code term}.
     * @param  length    The number of valid entries in the {@code gridToCRS} array.
     * @param  result    An initially empty map in which to store the results.
     * @param  isY       {@code false} for analyzing the X axis, or {@code true} for the Y axis.
     * @return The number of entries remaining in {@code gridToCRS}.
     */
    private static int computePyramidLevels(final AffineTransform[] gridToCRS, final int length,
            final Map<AffineTransform,Dimension> result, final boolean isY)
    {
        int processing = 0;  // Index of the AffineTransform under process.
        int remaining  = 0;  // Count of AffineTransforms that this method did not processed.
        AffineTransform base;
        double scale, shear;
        boolean scaleIsNull, shearIsNull;
        do {
            if (processing >= length) {
                return remaining;
            }
            base = gridToCRS[processing++];
            if (isY) {
                scale = base.getScaleY();
                shear = base.getShearY();
            } else {
                scale = base.getScaleX();
                shear = base.getShearX();
            }
            scaleIsNull = Math.abs(scale) < EPS;
            shearIsNull = Math.abs(shear) < EPS;
        } while (scaleIsNull && shearIsNull && redo(result.remove(base)));
        if (isY) {
            // If we get a NullPointerException here, it would be a bug in the algorithm.
            result.get(base).height = 1;
        } else {
            assert result.isEmpty() : result;
            result.put(base, new Dimension(1,0));
        }
        /*
         * From this point, consider 'base', 'scale', 'shear', 'scaleIsNull', 'shearIsNull'
         * as final. They describe the AffineTransform with finest resolution along one axis
         * (X or Y), not necessarly both.
         */
        while (processing < length) {
            final AffineTransform candidate = gridToCRS[processing++];
            final double scale2, shear2;
            if (isY) {
                scale2 = candidate.getScaleY();
                shear2 = candidate.getShearY();
            } else {
                scale2 = candidate.getScaleX();
                shear2 = candidate.getShearX();
            }
            final int level;
            if (scaleIsNull) {
                if (!(Math.abs(scale2) < EPS)) {
                    // Expected a null scale but was not.
                    gridToCRS[remaining++] = candidate;
                    continue;
                }
                level = level(shear2 / shear);
            } else {
                level = level(scale2 / scale);
                if (shearIsNull ? !(Math.abs(shear2) < EPS) : (level(shear2 / shear) != level)) {
                    // Expected (a null shear) : (the same pyramid level), but was not.
                    gridToCRS[remaining++] = candidate;
                    continue;
                }
            }
            if (level == 0) {
                // Not a pyramid level (the ratio is not an integer).
                gridToCRS[remaining++] = candidate;
                continue;
            }
            /*
             * Stores the pyramid level either as the width or as the height, depending on the
             * 'isY' value. The map is assumed initially empty for the X values, and containing
             * every required entries for the Y values.
             */
            if (isY) {
                // If we get a NullPointerException here, it would be a bug in the algorithm.
                result.get(candidate).height = level;
            } else {
                if (result.put(candidate, new Dimension(level,0)) != null) {
                    throw new AssertionError(candidate); // Should never happen.
                }
            }
        }
        Arrays.fill(gridToCRS, remaining, length, null);
        return remaining;
    }

    /**
     * Computes the pyramid level from the ratio between two affine transform coefficients.
     * If the ratio has been computed from {@code entry2.scaleX / entry1.scaleX}, then a
     * return value of:
     * <p>
     * <ul>
     *   <li>1 means that both entries are at the same level.</li>
     *   <li>2 means that the second entry has pixels twice as large as first entry.</li>
     *   <li>3 means that the second entry has pixels three time larger than first entry.</li>
     *   <li><cite>etc...</cite></li>
     *   <li>A negative number means that the second entry has pixels smaller than first entry.</li>
     *   <li>0 means that the ratio between entries is not an integer number.</li>
     * </ul>
     *
     * @param  ratio The ratio between affine transform coefficients.
     * @return The pixel size (actually subsampling) relative to the smallest pixel, or 0 if it
     *         can't be computed. If the ratio is between 0 and 1, then this method returns a
     *         negative number.
     */
    private static int level(double ratio) {
        if (ratio > 0 && ratio < Double.POSITIVE_INFINITY) {
            // The 0.75 threshold could be anything between 0.5 and 1. We
            // take a middle value for being safe regarding rounding errors.
            final boolean inverse = (ratio < 0.75);
            if (inverse) {
                ratio = 1 / ratio;
            }
            final double integer = Math.rint(ratio);
            if (integer < Integer.MAX_VALUE && Math.abs(ratio - integer) < EPS) {
                // Found an integer ratio. Inverse the sign (just
                // as a matter of convention) if smaller than 1.
                int level = (int) integer;
                if (inverse) {
                    level = -level;
                }
                return level;
            }
        }
        return 0;
    }

    /**
     * A hack for a {@code while} loop.
     */
    private static boolean redo(final Dimension size) {
        return true;
    }

    /**
     * Returns a string representation of the tiles contained in this object. Since this method is
     * for debugging purpose, only the first tiles may be formatted in order to avoid consumming to
     * much space in the debugger.
     */
    @Override
    public String toString() {
        final List<Tile> tiles = new ArrayList<Tile>(this.tiles.values());
        Collections.sort(tiles);
        return Tile.toString(tiles, 400);
    }
}
