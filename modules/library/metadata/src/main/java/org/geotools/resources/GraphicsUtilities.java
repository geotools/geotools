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
package org.geotools.resources;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.geotools.io.ExpandedTabWriter;


/**
 * A set of utilities methods for painting in a {@link Graphics2D} handle.
 * Method in this class was used to be in {@link org.geotools.gui.swing.ExceptionMonitor}.
 * We had to extract them in a separated class in order to avoid dependencies of renderer
 * module toward the GUI one, especially since the extracted methods are not Swing specific.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class GraphicsUtilities {
    /**
     * Number of spaces to leave between each tab.
     */
    private static final int TAB_WIDTH = 4;

    /**
     * The creation of {@code GraphicsUtilities} class objects is forbidden.
     */
    private GraphicsUtilities() {
    }

    /**
     * Writes the specified exception trace in the specified graphics
     * context.  This method is useful when an exception has occurred
     * inside a {@link java.awt.Component#paint} method and we want to
     * write it rather than leaving an empty window.
     *
     * @param exception Exception whose trace we want to write.
     * @param graphics Graphics context in which to write exception.  The
     *        graphics context should be in its initial state (default affine
     *        transform, default colour, etc...)
     * @param widgetBounds Size of the trace which was being drawn.
     */
    public static void paintStackTrace(final Graphics2D graphics,
                                       final Rectangle  widgetBounds,
                                       final Throwable  exception)
    {
        /*
         * Obtains the exception trace in the form of a character chain.
         * The carriage returns in this chain can be "\r", "\n" or "r\n".
         */
        final String message = printStackTrace(exception);
        /*
         * Examines the character chain line by line.
         * "Glyphs" will be created as we go along and we will take advantage
         * of this to calculate the necessary space.
         */
        double width = 0, height = 0;
        final List<GlyphVector> glyphs = new ArrayList<GlyphVector>();
        final List<Rectangle2D> bounds = new ArrayList<Rectangle2D>();
        final int length = message.length();
        final Font font = graphics.getFont();
        final FontRenderContext context = graphics.getFontRenderContext();
        for (int i = 0; i < length;) {
            int ir = message.indexOf('\r', i);
            int in = message.indexOf('\n', i);
            if (ir < 0) ir = length;
            if (in < 0) in = length;
            final int irn = Math.min(ir, in);
            final GlyphVector line = font.createGlyphVector(context, message.substring(i, irn));
            final Rectangle2D rect = line.getVisualBounds();
            final double w = rect.getWidth();
            if (w > width) width = w;
            height += rect.getHeight();
            glyphs.add(line);
            bounds.add(rect);
            i = (Math.abs(ir - in) <= 1 ? Math.max(ir, in) : irn) + 1;
        }
        /*
         * Proceeds to draw all the previously calculated glyphs.
         */
        float xpos = (float) (0.5 * (widgetBounds.width - width));
        float ypos = (float) (0.5 * (widgetBounds.height - height));
        final int size = glyphs.size();
        for (int i = 0; i < size; i++) {
            final GlyphVector line = glyphs.get(i);
            final Rectangle2D rect = bounds.get(i);
            ypos += rect.getHeight();
            graphics.drawGlyphVector(line, xpos, ypos);
        }
    }

    /**
     * Returns an exception trace. All tabs will have been replaced by 4 white spaces.
     * This method was used to be a private one in {@link org.geotools.gui.swing.ExceptionMonitor}.
     * Do not rely on it.
     */
    public static String printStackTrace(final Throwable exception) {
        final StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(new ExpandedTabWriter(writer, TAB_WIDTH)));
        return writer.toString();
    }
}
