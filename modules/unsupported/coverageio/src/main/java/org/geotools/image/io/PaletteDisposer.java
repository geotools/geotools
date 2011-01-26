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
package org.geotools.image.io;

import java.awt.image.ColorModel;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Set;


/**
 * Allows garbage-collection of {@link Palette} after their index color model has been
 * garbage collected.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
final class PaletteDisposer extends Thread {
    /**
     * The reference queue.
     */
    private static final ReferenceQueue<ColorModel> queue = new ReferenceQueue<ColorModel>();

    /**
     * A weak reference to a color model created by a palette.
     */
    static final class Reference extends WeakReference<ColorModel> {
        /**
         * Starts the disposer thread when the {@link Reference} are about to be created.
         */
        static {
            new PaletteDisposer().start();
        }

        /**
         * The palette that created the color model.
         */
        final Palette palette;

        /**
         * Creates the weak reference for the specified color model.
         */
        public Reference(final Palette palette, final ColorModel colors) {
            super(colors, queue);
            this.palette = palette;
            final Set<Palette> protectedPalettes = palette.factory.protectedPalettes;
            synchronized (protectedPalettes) {
                protectedPalettes.add(palette);
            }
        }
    }

    /**
     * Creates a new disposer thread.
     */
    private PaletteDisposer() {
        super("PaletteDisposer");
        setDaemon(true);
    }

    /**
     * Removes the palette from the set of protected ones.
     */
    @Override
    public void run() {
        while (true) {
            final Reference ref;
            try {
                ref = (Reference) queue.remove();
            } catch (InterruptedException e) {
                continue;
            }
            final Set<Palette> protectedPalettes = ref.palette.factory.protectedPalettes;
            synchronized (protectedPalettes) {
                protectedPalettes.remove(ref.palette);
            }
        }
    }
}
