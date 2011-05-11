/*
 * uDig - User Friendly Desktop Internet GIS client http://udig.refractions.net (C) 2004,
 * Refractions Research Inc. This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; version 2.1 of the License. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package org.geotools.swt.styling.simple;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * A "button" of a certain color determined by the color picker.
 */
public class StolenColorEditor {

    private Point fExtent;
    private Image fImage;
    private RGB fColorValue;
    private Color fColor;
    private Button fButton;
    private SelectionListener listener;

    public StolenColorEditor( Composite parent ) {
        this( parent, null );
    }
    public StolenColorEditor( Composite parent, SelectionListener parentListener ) {
        this.listener = parentListener;
        fButton = new Button(parent, SWT.PUSH);
        fExtent = computeImageSize(parent);
        fImage = new Image(parent.getDisplay(), fExtent.x, fExtent.y);

        GC gc = new GC(fImage);
        gc.setBackground(fButton.getBackground());
        gc.fillRectangle(0, 0, fExtent.x, fExtent.y);
        gc.dispose();

        fButton.setImage(fImage);
        fButton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent event ) {
                ColorDialog colorDialog = new ColorDialog(fButton.getShell());
                colorDialog.setRGB(fColorValue);
                RGB newColor = colorDialog.open();
                if (newColor != null) {
                    fColorValue = newColor;
                    updateColorImage();
                }
                notifyParent(event);
            }
        });

        fButton.addDisposeListener(new DisposeListener(){
            public void widgetDisposed( DisposeEvent event ) {
                if (fImage != null) {
                    fImage.dispose();
                    fImage = null;
                }
                if (fColor != null) {
                    fColor.dispose();
                    fColor = null;
                }
            }
        });
    }

    public void setListener( SelectionListener newListener ) {
        listener = newListener;
    }
    
    private void notifyParent( SelectionEvent event ) {
        if( listener != null )
            listener.widgetSelected(event);
    }

    public java.awt.Color getColor(){
        RGB rgb = getColorValue();
        return new java.awt.Color( rgb.red, rgb.green, rgb.blue);
    }
    public void setColor( java.awt.Color color ){
        if( color == null ){
            setColorValue( null );
        }
        else {
            RGB rgb = new RGB(color.getRed(), color.getGreen(), color.getBlue() );
            setColorValue( rgb );    
        }                
    }
    public void setEnabled( boolean isEnabled ){
        getButton().setEnabled( isEnabled );
    }
    public RGB getColorValue() {
        return fColorValue;
    }

    public void setColorValue( RGB rgb ) {
        if( rgb == null ){
            rgb = new RGB(0,0,0);
        }
        fColorValue = rgb;
        updateColorImage();
    }

    public Button getButton() {
        return fButton;
    }

    protected void updateColorImage() {

        Display display = fButton.getDisplay();

        GC gc = new GC(fImage);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.drawRectangle(0, 2, fExtent.x - 1, fExtent.y - 4);

        if (fColor != null)
            fColor.dispose();

        fColor = new Color(display, fColorValue);
        gc.setBackground(fColor);
        gc.fillRectangle(1, 3, fExtent.x - 2, fExtent.y - 5);
        gc.dispose();

        fButton.setImage(fImage);
    }

    protected Point computeImageSize( Control window ) {
        GC gc = new GC(window);
        Font f = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
        gc.setFont(f);
        int height = gc.getFontMetrics().getHeight();
        gc.dispose();
        Point p = new Point(height * 3 - 6, height);
        return p;
    }
}