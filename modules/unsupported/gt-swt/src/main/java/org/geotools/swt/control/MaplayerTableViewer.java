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

package org.geotools.swt.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.geotools.data.FeatureSource;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.styling.SimpleStyleConfigurator;
import org.geotools.swt.utils.ImageCache;
import org.geotools.swt.utils.Utils;

/**
 * A {@link TableViewer table viewer} for {@link MapLayer map layers}.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class MaplayerTableViewer extends TableViewer implements ISelectionChangedListener {
    private List<MapLayer> layersList = new ArrayList<MapLayer>();
    private MapLayer selectedMapLayer;

    private String[] titles = {"Layer name", "Visible", "Style"};
    private SwtMapPane pane;

    /**
     * Constructor.
     * 
     * <p>
     * <b>Note</b> that after the object is built and before actually using it, the
     * {@link SwtMapPane map pane} has to be set through the {@link #setPane(SwtMapPane)}
     * method. 
     * </p>
     * 
     * @param parent the parent {@link Composite}. 
     * @param style the style for the {@link Composite}.
     */
    public MaplayerTableViewer( Composite parent, int style ) {
        super(parent, style);

        this.setContentProvider(new ArrayContentProvider());
        this.addSelectionChangedListener(this);

        createColumns(parent, this);
        final Table table = this.getTable();
        table.setHeaderVisible(true);
        // table.setLinesVisible(true);

        this.setInput(layersList);
    }

    /**
     * Setter for the {@link SwtMapPane map pane}.
     * 
     * @param pane the map pane to use.
     */
    public void setPane( SwtMapPane pane ) {
        this.pane = pane;
    }

    /**
     * Getter for the loaded {@link MapLayer}s list.
     * 
     * @return the list of map layers.
     */
    public List<MapLayer> getLayersList() {
        return layersList;
    }

    /**
     * Getter for the selected {@link MapLayer}.
     * 
     * @return the selected layer or <code>null</code>.
     */
    public MapLayer getSelectedMapLayer() {
        return selectedMapLayer;
    }

    private void createColumns( final Composite parent, final TableViewer viewer ) {

        int[] bounds = {120, 50, 50};

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider(){
            @Override
            public Image getImage( Object element ) {
                if (element instanceof MapLayer) {
                    MapLayer p = (MapLayer) element;
                    if (Utils.isGridLayer(p)) {
                        return ImageCache.getInstance().getImage(ImageCache.GRID);
                    }
                    return ImageCache.getInstance().getImage(ImageCache.FEATURE);
                }
                return null;
            }

            @Override
            public String getText( Object element ) {
                if (element instanceof MapLayer) {
                    MapLayer p = (MapLayer) element;
                    String title = p.getTitle();
                    if (title == null || title.length() == 0) {
                        @SuppressWarnings("rawtypes")
                        FeatureSource featureSource = p.getFeatureSource();
                        if (featureSource != null) {
                            title = featureSource.getName().getLocalPart().toString();
                        }
                    }
                    return title;
                }
                return null;
            }
        });

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider(){
            @Override
            public Image getImage( Object element ) {
                if (element instanceof MapLayer) {
                    MapLayer p = (MapLayer) element;
                    if (p.isVisible()) {
                        return ImageCache.getInstance().getImage(ImageCache.CHECKED);
                    }
                    return ImageCache.getInstance().getImage(ImageCache.UNCHECKED);
                }
                return null;
            }
            @Override
            public String getText( Object element ) {
                return null;
            }
        });

        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider(){
            public Image getImage( Object element ) {
                return ImageCache.getInstance().getImage(ImageCache.STYLE);
            }
            @Override
            public String getText( Object element ) {
                return null;
            }
        });

    }

    private TableViewerColumn createTableViewerColumn( String title, int bound, final int colNumber ) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(this, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    public void selectionChanged( SelectionChangedEvent arg0 ) {
        if (arg0 == null) {
            selectedMapLayer = null;
            return;
        }
        IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
        Object firstElement = selection.getFirstElement();
        if (firstElement instanceof MapLayer) {
            selectedMapLayer = (MapLayer) firstElement;
        }
    }

    @Override
    protected void triggerEditorActivationEvent( ColumnViewerEditorActivationEvent event ) {
        super.triggerEditorActivationEvent(event);
        ViewerCell source = (ViewerCell) event.getSource();
        int columnIndex = source.getColumnIndex();
        if (columnIndex == 1) {
            MapLayer element = (MapLayer) source.getElement();
            element.setVisible(!element.isVisible());
            refresh();
            pane.redraw();
        } else if (columnIndex == 2) {
            MapLayer element = (MapLayer) source.getElement();
            try {
                doSetStyle(element);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pane.redraw();
        }
    }

    /**
     * Show a style dialog to create a new Style for the layer
     *
     * @param layer the layer to be styled
     * @throws IOException 
     */
    private void doSetStyle( MapLayer layer ) throws IOException {
        Style style = SimpleStyleConfigurator.showDialog(this.getTable().getShell(), layer);
        if (style != null) {
            layer.setStyle(style);
        }
    }

    /**
     * Adds a {@link MapLayer} to the viewer and updates.
     * 
     * @param layer the layer to add.
     */
    public void addLayer( MapLayer layer ) {
        layersList.add(0, layer);
        refresh();
    }
    
    /**
     * Removes a {@link MapLayer} from the viewer and updates.
     * 
     * @param layer the layer to remove.
     */
    public void removeLayer( MapLayer layer ) {
        layersList.remove(layer);
        refresh();
    }

}
