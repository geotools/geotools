/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.text.View;

/**
 * Static utility methods for common dialog and GUI related tasks.
 * 
 * @author Michael Bedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class DialogUtils {

    /**
     * Shows a dialog centred on the screen. May be called safely
     * from any thread.
     * 
     * @param dialog the dialog
     */
    public static void showCentred(final Window dialog) {
        showCentredOnParent(null, dialog);
    }
    
    /**
     * Shows a dialog centred on its parent. May be called safely
     * from any thread. If {@code parent} is {@code null} the dialog
     * is centred on the screen.
     * 
     * @param parent the parent component
     * @param dialog the dialog
     */
    public static void showCentredOnParent(final Window parent, final Window dialog) {
        if (EventQueue.isDispatchThread()) {
            doShowCentred(parent, dialog);
            
        } else {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    doShowCentred(parent, dialog);
                }
            });
        }
    }

    /**
     * Gets all child components that are, or derive from, the given class.
     * This method is adapted from the SwingUtils class written by Darryl Burke.
     * (Accessed from: http://tips4java.wordpress.com/2008/11/13/swing-utils/).
     *
     * @param <T> Swing type derived from JComponent
     * @param clazz the component class
     * @param parent the parent container
     * @param includeNested whether to recursively collect nested components
     *
     * @return list of child components
     */
    public static <T extends JComponent> List<T> getChildComponents(
            Class<T> clazz, Container parent, boolean includeNested) {

        List<T> children = new ArrayList<T>();

        for (Component c : parent.getComponents()) {
            boolean isClazz = clazz.isAssignableFrom(c.getClass());
            if (isClazz) {
                children.add(clazz.cast(c));
            }
            if (includeNested && c instanceof Container) {
                children.addAll(getChildComponents(clazz, (Container) c, includeNested));
            }
        }

        return children;
    }
    
    /**
     * Returns {@code input} if not {@code null} or empty, otherwise returns
     * {@code fallback}. This is handy for setting dialog titles etc. Note that
     * the input string is considered empty if {@code input.trim().length() == 0}.
     * 
     * @param input input string
     * @param fallback fallback string (may be {@code null})
     * 
     * @return {@code input} unless it is {@code null} or empty, in which case
     *     {@code fallback} is returned
     */
    public static String getString(String input, String fallback) {
        if (input == null || input.trim().length() == 0) {
            return fallback;
        }
        
        return input;
    }
    
    private static void doShowCentred(Window parent, Window dialog) {
        if (parent == null) {
            doCentre(dialog, Toolkit.getDefaultToolkit().getScreenSize());
        } else {
            doCentre(dialog, parent.getSize());
        }
        dialog.setVisible(true);
    }
     
    private static void doCentre(Window dialog, Dimension parentDim) {
        Dimension dialogDim = dialog.getSize();
        int x = Math.max(0, parentDim.width / 2 - dialogDim.width / 2);
        int y = Math.max(0, parentDim.height / 2 - dialogDim.height / 2);
        dialog.setLocation(x, y);
    }

    /**
     * Calculates the dimensions that a given text string requires when rendered
     * as HTML text in a label component.
     * <p>
     * The method used is adapted from that described in a blog post by Morten Nobel:
     * <blockquote>
     * http://blog.nobel-joergensen.com/2009/01/18/changing-preferred-size-of-a-html-jlabel/
     * </blockquote>
     * 
     * @param labelText the text to render, optionally enclosed in {@code <html>...</html>} tags
     * @param fixedDimSize the size of the fixed dimension (either width or height
     * @param width {@code true} if the fixed dimension is width; {@code false} for height
     * 
     * @return the rendered label text extent
     */
    public static Dimension getHtmlLabelTextExtent(final String labelText, 
            final int fixedDimSize, 
            final boolean width) {
        
        final Dimension[] result = new Dimension[1];
        
        if (SwingUtilities.isEventDispatchThread()) {
            result[0] = doGetHtmlTextExtent(labelText, fixedDimSize, width);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        result[0] = doGetHtmlTextExtent(labelText, fixedDimSize, width);
                    }
                });
                
            } catch (Exception ex) {
                // Either an InterruptedException or an InvocationTargetException
                // both of which are fatal
                throw new RuntimeException(ex);
            }
        }
        
        return result[0];
    }
    
    /**
     * Helper method for {@linkplain #getHtmlLabelTextExtent(java.lang.String, int, boolean)}.
     * This is required because we are creating and invisibly rendering a {@code JLabel} 
     * object in this method, and being virtuous in our Swing usage we should only do that
     * on the event dispatch thread.
     * 
     * @param labelText the text to render, optionally enclosed in {@code <html>...</html>} tags
     * @param fixedDimSize the size of the fixed dimension (either width or height
     * @param width {@code true} if the fixed dimension is width; {@code false} for height
     * 
     * @return the rendered label text extent
     */
    private static Dimension doGetHtmlTextExtent(String labelText, int fixedDimSize, boolean width) {
        final JLabel label = new JLabel();
        if (labelText.startsWith("<html>")) {
            label.setText(labelText);
        } else {
            label.setText("<html>" + labelText + "</html>");
        }
        
        View view = (View) label.getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
        view.setSize(width ? fixedDimSize : 0, width? 0 : fixedDimSize);

        float w = view.getPreferredSpan(View.X_AXIS);
        float h = view.getPreferredSpan(View.Y_AXIS);
        
        return new java.awt.Dimension((int) Math.ceil(w), (int) Math.ceil(h));
    }

}
