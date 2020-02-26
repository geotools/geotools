/* uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.swt.styling.simple;

import java.awt.Color;
import java.text.MessageFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.SLD;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.geotools.swt.utils.Messages;
import org.opengis.style.GraphicalSymbol;

/**
 * Allows editing/viewing of a Style Layer Descriptor "Graphic".
 *
 * <p>Here is the pretty picture:
 *
 * <pre><code>
 *          +-+ +-------+ +------+
 *   Point: |x| | star\/| |size\/|
 *          +-+ +-------+ +------+
 * </code></pre>
 *
 * <p>Workflow:
 *
 * <ol>
 *   <li>createControl( parent ) - set up controls
 *   <li>setGraphic( graphic, mode ) - provide content from SimpleStyleConfigurator
 *       <ol>
 *         <li>Symbolizer values copied into fields based on mode
 *         <li>fields copied into controls
 *         <li>controls enabled based on mode & fields
 *       </ol>
 *   <li>Listener.widgetSelected/modifyText - User performs an "edit"
 *   <li>Listener.sync( SelectionEvent ) - update fields with values of controls
 *   <li>fire( SelectionSevent ) - notify SimpleStyleConfigurator of change
 *   <li>getGraphic( Fill, Stroke, StyleBuilder ) - construct a Graphic based on fields
 * </ul>
 *
 * @author Jody Garnett
 * @since 1.0.0
 */
public class GraphicViewer {
    boolean enabled;
    String type;
    double width;

    Button on;
    Combo name;
    Combo size;

    private class Listener implements SelectionListener, ModifyListener {
        public void widgetSelected(SelectionEvent e) {
            sync(e);
        };

        public void widgetDefaultSelected(SelectionEvent e) {
            sync(e);
        };

        public void modifyText(final ModifyEvent e) {
            sync(AbstractSimpleConfigurator.selectionEvent(e));
        };

        private void sync(SelectionEvent selectionEvent) {
            try {
                GraphicViewer.this.enabled = GraphicViewer.this.on.getSelection();
                GraphicViewer.this.type = GraphicViewer.this.name.getText();
                try {
                    GraphicViewer.this.width = Integer.parseInt(GraphicViewer.this.size.getText());
                } catch (NumberFormatException nan) {
                    // well lets just leave width alone
                }
                fire(selectionEvent); // everything worked
            } catch (Throwable t) {
                // meh
            } finally {
                GraphicViewer.this.name.setEnabled(GraphicViewer.this.enabled);
                GraphicViewer.this.size.setEnabled(GraphicViewer.this.enabled);
            }
        }
    };

    Listener sync = new Listener();
    private SelectionListener listener;

    /**
     * Accepts a listener that will be notified when content changes.
     *
     */
    public void addListener(SelectionListener listener1) {
        this.listener = listener1;
    }

    /**
     * Remove listener.
     *
     */
    public void removeListener(SelectionListener listener1) {
        if (this.listener == listener1) this.listener = null;
    }

    /**
     * TODO summary sentence for fire ...
     *
     */
    protected void fire(SelectionEvent event) {
        if (this.listener == null) return;
        this.listener.widgetSelected(event);
    }

    /**
     * TODO summary sentence for createControl ...
     *
     * @return Generated composite
     */
    public Composite createControl(Composite parent, KeyListener klisten, StyleBuilder build) {
        Composite part =
                AbstractSimpleConfigurator.subpart(
                        parent, Messages.getString("SimpleStyleConfigurator_point_label"));

        this.on = new Button(part, SWT.CHECK);
        // this.on.addSelectionListener( this.sync );

        this.size = new Combo(part, SWT.DROP_DOWN);
        this.size.setItems(
                new String[] {
                    "1", "2", "3", "5", "10", "15"
                }); // $NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        // //$NON-NLS-6$
        this.size.setTextLimit(2);
        this.size.addKeyListener(klisten);
        this.size.setToolTipText(Messages.getString("GraphicViewer_size_tooltip"));

        this.name = new Combo(part, SWT.DROP_DOWN);
        this.name.setItems(build.getWellKnownMarkNames());
        this.name.setTextLimit(9);
        this.name.addKeyListener(klisten);
        this.name.setToolTipText(Messages.getString("GraphicViewer_name_tooltip"));
        return part;
    }

    /**
     * TODO summary sentence for getGraphic ...
     *
     * @return Graphic defined by this model
     */
    public Graphic getGraphic(Fill filll, Stroke stroke, StyleBuilder build) {
        if (!this.enabled) {
            Mark mark = null;
            if (this.type == null) {
                build.createMark("square", null, null); // $NON-NLS-1$
            } else {
                mark = build.createMark(this.type, (Fill) null, (Stroke) null);
            }
            return build.createGraphic(null, mark, null);
        }
        Mark mark = build.createMark(this.type, filll, stroke);
        Graphic graphic = build.createGraphic(null, mark, null);
        graphic.setSize(build.literalExpression(this.width));
        return graphic;
    }

    /**
     * TODO summary sentence for setGraphic ...
     *
     */
    public void setGraphic(Graphic graphic, Mode mode, Color defaultColor) {
        boolean enabled = true;
        if (graphic == null) {
            StyleBuilder builder = new StyleBuilder();
            graphic =
                    builder.createGraphic(
                            null, builder.createMark(StyleBuilder.MARK_SQUARE, defaultColor), null);
            enabled = true;
        }
        this.width = SLDs.size(graphic);
        String text = MessageFormat.format("{0,number,#0}", this.width); // $NON-NLS-1$
        if (text != null) {
            this.size.setText(text);
            this.size.select(this.size.indexOf(text));
        }

        boolean marked = false;
        if (graphic != null
                && graphic.graphicalSymbols() != null
                && !graphic.graphicalSymbols().isEmpty()) {

            for (GraphicalSymbol symbol : graphic.graphicalSymbols()) {
                if (symbol instanceof Mark) {
                    Mark mark = (Mark) symbol;
                    setMark(mark, mode);
                    marked = true;
                    break;
                }
            }
        }
        if (!marked) {
            setMark(null, mode);
        }
        this.enabled = this.enabled && enabled;
    }

    private void setMark(Mark mark, Mode mode) {
        listen(false);
        try {
            this.enabled = (mode == Mode.POINT && mark != null);
            this.type = SLD.wellKnownName(mark);

            // Stroke is used in line, point and polygon
            this.on.setEnabled(mode == Mode.POINT || mode == Mode.ALL);

            if (this.type != null) {
                this.name.setText(this.type);
                this.name.select(this.name.indexOf(this.type));
            }

            this.on.setSelection(this.enabled);
            this.size.setEnabled(this.enabled);
            this.name.setEnabled(this.enabled);
        } finally {
            listen(true); // listen to user now
        }
    }

    void listen(boolean listen) {
        if (listen) {
            this.on.addSelectionListener(this.sync);
            this.size.addSelectionListener(this.sync);
            this.size.addModifyListener(this.sync);
            this.name.addSelectionListener(this.sync);
            this.name.addModifyListener(this.sync);
        } else {
            this.on.removeSelectionListener(this.sync);
            this.size.removeSelectionListener(this.sync);
            this.size.removeModifyListener(this.sync);
            this.name.removeSelectionListener(this.sync);
            this.name.removeModifyListener(this.sync);
        }
    }
}
