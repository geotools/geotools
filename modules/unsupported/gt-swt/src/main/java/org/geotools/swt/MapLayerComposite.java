/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swt;

import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.swt.control.MaplayerTableViewer;
import org.geotools.swt.utils.ImageCache;
import org.geotools.swt.utils.Messages;

/**
 * Displays a list of the map layers in an associated {@linkplain JMapPane} and
 * provides controls to set the visibility, selection and style of each layer.
 * <p>
 * Implementation note: DefaultMapContext stores its list of MapLayer objects
 * in rendering order, ie. the layer at index 0 is rendererd first, followed by
 * index 1 etc. MapLayerTable stores its layers in the reverse order since it
 * is more intuitive for the user to think of a layer being 'on top' of other
 * layers.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Michael Bedward
 */
public class MapLayerComposite extends Composite {
    private SwtMapPane pane;
    private MaplayerTableViewer mapLayerTableViewer;

    /**
     * Default constructor. A subsequent call to {@linkplain #setMapPane}
     * will be required.
     */
    public MapLayerComposite( Composite parent, int style ) {
        super(parent, style);
        init();
    }

    /**
     * Set the map pane that this MapLayerTable will service.
     *
     * @param pane the map pane
     */
    public void setMapPane( SwtMapPane pane ) {
        this.pane = pane;
        pane.setMapLayerTable(this);
        mapLayerTableViewer.setPane(pane);

        MapContext mapContext = pane.getMapContext();
        MapLayer[] layers = mapContext.getLayers();
        for( MapLayer mapLayer : layers ) {
            mapLayerTableViewer.addLayer(mapLayer);
        }
    }

    /**
     * Add a new layer to those listed in the table. This method will be called
     * by the associated map pane automatically as part of the event sequence
     * when a new MapLayer is added to the pane's MapContext.
     *
     * @param layer the map layer
     */
    public void onAddLayer( MapLayer layer ) {
        mapLayerTableViewer.addLayer(layer);
    }

    /**
     * Remove a layer from those listed in the table. This method will be called
     * by the associated map pane automatically as part of the event sequence
     * when a new MapLayer is removed from the pane's MapContext.
     *
     * @param layer the map layer
     */
    public void onRemoveLayer( MapLayer layer ) {
        mapLayerTableViewer.removeLayer(layer);
    }

    /**
     * Repaint the list item associated with the specified MapLayer object
     *
     * @param layer the map layer
     */
    public void repaint( MapLayer layer ) {
        mapLayerTableViewer.refresh(layer, true);
    }

    /**
     * Called by the constructor. This method lays out the components that
     * make up the MapLayerTable and registers a mouse listener.
     */
    private void init() {
        setLayout(new GridLayout(1, false));

        Group mapLayersGroup = new Group(this, SWT.NONE);
        mapLayersGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mapLayersGroup.setLayout(new GridLayout(1, false));
        mapLayersGroup.setText(Messages.getString("layers_list_title"));

        mapLayerTableViewer = new MaplayerTableViewer(mapLayersGroup, SWT.BORDER | SWT.FULL_SELECTION);
        GridData listGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        mapLayerTableViewer.getTable().setLayoutData(listGD);

        Composite buttonComposite = new Composite(mapLayersGroup, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        buttonComposite.setLayout(new GridLayout(5, true));

        Button removeLayerButton = new Button(buttonComposite, SWT.PUSH);
        removeLayerButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        removeLayerButton.setToolTipText(Messages.getString("remove_layer"));
        removeLayerButton.setImage(ImageCache.getInstance().getImage(ImageCache.REMOVE_LAYER));
        removeLayerButton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent e ) {
                MapLayer selectedMapLayer = mapLayerTableViewer.getSelectedMapLayer();
                if (selectedMapLayer == null) {
                    return;
                }
                MapContext mapContext = pane.getMapContext();
                mapContext.removeLayer(selectedMapLayer);
                mapLayerTableViewer.selectionChanged(null);
            }
        });

        Button showLayersButton = new Button(buttonComposite, SWT.PUSH);
        showLayersButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        showLayersButton.setToolTipText(Messages.getString("show_all_layers"));
        showLayersButton.setImage(ImageCache.getInstance().getImage(ImageCache.CHECKED));
        showLayersButton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent e ) {
                onShowAllLayers();
            }
        });

        Button hideLayersButton = new Button(buttonComposite, SWT.PUSH);
        hideLayersButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        hideLayersButton.setToolTipText(Messages.getString("hide_all_layers"));
        hideLayersButton.setImage(ImageCache.getInstance().getImage(ImageCache.UNCHECKED));
        hideLayersButton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent e ) {
                onHideAllLayers();
            }
        });

        Button layerUpButton = new Button(buttonComposite, SWT.PUSH);
        layerUpButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        layerUpButton.setToolTipText(Messages.getString("layer_up"));
        layerUpButton.setImage(ImageCache.getInstance().getImage(ImageCache.UP));
        layerUpButton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent e ) {
                moveLayer(-1);
            }
        });

        Button layerDownButton = new Button(buttonComposite, SWT.PUSH);
        layerDownButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        layerDownButton.setToolTipText(Messages.getString("layer_down"));
        layerDownButton.setImage(ImageCache.getInstance().getImage(ImageCache.DOWN));
        layerDownButton.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent e ) {
                moveLayer(1);
            }
        });
    }

    /**
     * Handle a ListDataEvent signallying a drag-reordering of the map layers.
     * The event is published by the list model after the layers have been
     * reordered there.
     *
     * @param ev the event
     */
    private void moveLayer( int delta ) {
        MapLayer selectedMapLayer = mapLayerTableViewer.getSelectedMapLayer();
        if (selectedMapLayer == null)
            return;
        List<MapLayer> layersList = mapLayerTableViewer.getLayersList();
        MapContext mapContext = pane.getMapContext();

        int contextIndex = mapContext.indexOf(selectedMapLayer);

        int viewerIndex = layersList.indexOf(selectedMapLayer);
        int newViewerIndex = viewerIndex + delta;
        if (newViewerIndex < 0 || newViewerIndex > layersList.size() - 1) {
            return;
        }

        /*
        * MapLayerTable stores layers in the reverse order to
        * DefaultMapContext (see comment in javadocs for this class)
        */
        int newContextIndex = contextIndex - delta;
        if (newContextIndex < 0 || newContextIndex > mapContext.getLayerCount() - 1) {
            return;
        }

        if (contextIndex != newContextIndex) {
            mapContext.moveLayer(contextIndex, newContextIndex);
            pane.redraw();
            Collections.swap(layersList, viewerIndex, newViewerIndex);
            mapLayerTableViewer.refresh();
        }

    }

    private void onShowAllLayers() {
        if (pane != null && pane.getMapContext() != null) {
            for( MapLayer layer : pane.getMapContext().getLayers() ) {
                if (!layer.isVisible()) {
                    layer.setVisible(true);
                }
            }
            mapLayerTableViewer.refresh();
            pane.redraw();
        }
    }

    private void onHideAllLayers() {
        if (pane != null && pane.getMapContext() != null) {
            for( MapLayer layer : pane.getMapContext().getLayers() ) {
                if (layer.isVisible()) {
                    layer.setVisible(false);
                }
            }
            mapLayerTableViewer.refresh();
            pane.redraw();
        }
    }
}
