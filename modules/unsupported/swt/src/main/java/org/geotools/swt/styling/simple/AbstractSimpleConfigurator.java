/*
 *    uDig - User Friendly Desktop Internet GIS client
 *    http://udig.refractions.net
 *    (C) 2004, Refractions Research Inc.
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
 *
 */
package org.geotools.swt.styling.simple;

import java.awt.Color;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * This is here to save me some typing and ensure that the simple raster and feature configurators
 * look similar.
 *
 * @author mleslie
 * @since 0.6.0
 */
public class AbstractSimpleConfigurator extends Dialog {
    /** <code>build</code> field */
    protected StyleBuilder build = new StyleBuilder();

    protected SimpleFeatureCollection featureCollection;
    protected Style style;

    /** Construct <code>AbstractSimpleConfigurator</code>. */
    public AbstractSimpleConfigurator(
            Shell parent, SimpleFeatureCollection featureCollection, Style style) {
        super(parent);
        this.featureCollection = featureCollection;
        this.style = style;
    }

    protected void setLayout(Composite parent) {
        RowLayout layout = new RowLayout();
        layout.pack = false;
        layout.wrap = true;
        layout.type = SWT.HORIZONTAL;
        layout.fill = true;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.spacing = 0;
        parent.setLayout(layout);
    }

    /**
     * Retrieves the style object from the style blackboard.
     *
     * @return Style
     */
    protected Style getStyle() {
        assert (featureCollection != null);
        Style style = this.style;
        if (style == null) {
            SimpleFeatureType schema = featureCollection.getSchema();
            if (SLDs.isLine(schema)) {
                style = SLD.createLineStyle(Color.red, 1);
            } else if (SLDs.isPoint(schema)) {
                style = SLD.createPointStyle("Circle", Color.red, Color.green, 1f, 3f);
            } else if (SLDs.isPolygon(schema)) {
                style = SLD.createPolygonStyle(Color.red, Color.green, 1f);
            }
        }
        this.style = style;
        return style;
    }

    /**
     * Construct a subpart labeled with the provided tag.
     *
     * <p>Creates a composite with a grid layout of the specifed columns, and a label with text from
     * label.
     *
     * @return Composite with one label
     */
    public static Composite subpart(Composite parent, String label) {
        Composite subpart = new Composite(parent, SWT.NONE);
        RowLayout across = new RowLayout();
        across.type = SWT.HORIZONTAL;
        across.wrap = true;
        across.pack = true;
        across.fill = true;
        across.marginBottom = 1;
        across.marginRight = 2;

        subpart.setLayout(across);

        Label labell = new Label(subpart, SWT.LEFT);
        labell.setText(label);

        RowData data = new RowData();
        data.width = 40;
        // check to see if width is not enough space
        GC gc = new GC(parent.getParent());
        gc.setFont(parent.getParent().getFont());
        FontMetrics fontMetrics = gc.getFontMetrics();
        gc.dispose();
        int labelWidth =
                Dialog.convertWidthInCharsToPixels(fontMetrics, labell.getText().length() + 1);
        if (labelWidth > data.width) {
            data.width = labelWidth;
        }
        // TODO: adjust the methods that call this one to keep a consistent
        // width (otherwise they're misaligned)
        data.height = 10;
        labell.setLayoutData(data);

        return subpart;
    }
    /**
     * Morph a text ModifyEvent into a SelectionEvent as best we can.
     *
     * <p>This may be a bad abuse of the event system, it appears to be in use because we are too
     * lazy to specify a new event type for style modification.
     *
     * <p>However this does seem to be in keeping with the purpose of SelectionEvent it already
     * isolates out code from TypedEvents by providing a summary of what changed in which widet.
     *
     * @return A SelectionEvent based on the provided modify event
     */
    public static SelectionEvent selectionEvent(final ModifyEvent e) {
        Event event = new Event();
        event.widget = e.widget;
        event.data = e.data;
        event.display = e.display;
        event.time = e.time;
        return new SelectionEvent(event) {
            /** <code>serialVersionUID</code> field */
            private static final long serialVersionUID = 6544345585295778029L;

            @Override
            public Object getSource() {
                return e.getSource();
            }
        };
    };
}
