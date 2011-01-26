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

package org.geotools.swing.control;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JList;

/**
 * A sub-class of JList that supports drag and drop to reorder items within a single list
 * and to copy or move items between lists. It is used by the
 * {@linkplain org.geotools.swing.MapLayerTable} widget but has been written
 * in a general fashion so that it may be used for other pusposes.
 *
 * @see DnDListModel
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class DnDList<T> extends JList implements DragGestureListener, DragSourceListener, DropTargetListener {
    private static final long serialVersionUID = 3310751294076288683L;

    private static final ResourceBundle stringRes = ResourceBundle.getBundle("org/geotools/swing/Text");

    private DragSource src;
    
    @SuppressWarnings("unused")
    private DropTarget tgt; // this is not used? what is it for
    
    private boolean movingItems;
    private int overIndex;
    private int[] dragIndices;

    /**
     * Default constructor. An DnDListModel object will be created
     * for the list.
     */
    public DnDList() {
        this(new DnDListModel<T>());
    }

    /**
     * Constructor allowing the list model to be specified
     * @param model an instance of DnDListModel
     * @throws IllegalArgumentException if model is null
     */
    public DnDList(DnDListModel<T> model) {
        super( model );
        if (model == null) {
            throw new IllegalArgumentException(stringRes.getString("arg_null_error"));
        }
        //this.setModel(model);

        src = new DragSource();
        src.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);

        tgt = new DropTarget(this, this);

        movingItems = false;
        overIndex = -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public DnDListModel<T> getModel() {
        return (DnDListModel<T>) super.getModel();
    }
    /**
     * Called by the system when a drag gesture starts
     */
    public void dragGestureRecognized(DragGestureEvent dge) {
        dragIndices = getSelectedIndices();
        if (dragIndices.length > 0) {
            List<T> items = getModel().getElementsAt(dragIndices);
            Transferable stuff = new DnDListItemsTransferable<T>(items);
            movingItems = true;
            src.startDrag(dge, DragSource.DefaultMoveDrop, stuff, this);
        }
    }


    /**
     * DragSourceListener method - presently ignored
     * <p>
     * Description copied from interface:<br>
     * Called as the cursor's hotspot enters a platform-dependent drop site.
     * This method is invoked when all the following conditions are true:
     * <ul>
     * <li> The cursor's hotspot enters the operable part of a platform- dependent drop site.
     * <li> The drop site is active.
     * <li> The drop site accepts the drag.
     * </ul>
     */
    public void dragEnter(DragSourceDragEvent dsde) {
        // do nothing
    }

    /**
     * DragSourceListener method - presently ignored
     * <p>
     * Description copied from interface:<br>
     * Called as the cursor's hotspot moves over a platform-dependent drop site.
     * This method is invoked when all the following conditions are true:
     * <ul>
     * <li> The cursor's hotspot has moved, but still intersects the operable part
     * of the drop site associated with the previous dragEnter() invocation.
     * <li> The drop site is still active.
     * <li> The drop site accepts the drag.
     * </ul>
     */
    public void dragOver(DragSourceDragEvent dsde) {
        // do nothing
    }

    /**
     * DragSourceListener method - presently ignored
     * <p>
     * Description copied from interface:<br>
     * Called when the user has modified the drop gesture. This method is invoked when
     * the state of the input device(s) that the user is interacting with changes.
     * Such devices are typically the mouse buttons or keyboard modifiers that the
     * user is interacting with.
     */
    public void dropActionChanged(DragSourceDragEvent dsde) {
        // do nothing
    }

    /**
     * DragSourceListener method - presently ignored
     * <p>
     * Description copied from interface:<br>
     * Called as the cursor's hotspot exits a platform-dependent drop site.
     * This method is invoked when <b>any</b> of the following conditions are true:
     * <ul>
     * <li> The cursor's hotspot no longer intersects the operable part of the
     * drop site associated with the previous dragEnter() invocation.
     * <li> The drop site associated with the previous dragEnter() invocation is
     * no longer active.
     * <li> The drop site associated with the previous dragEnter() invocation
     * has rejected the drag.
     * </ul>
     */
    public void dragExit(DragSourceEvent dse) {
        // do nothing
    }

    /**
     * Description copied from interface:<br>
     * This method is invoked to signify that the Drag and Drop operation is
     * complete. The getDropSuccess() method of the DragSourceDropEvent can be
     * used to determine the termination state. The getDropAction() method returns
     * the operation that the drop site selected to apply to the Drop operation.
     * Once this method is complete, the current DragSourceContext and associated
     * resources become invalid.
     */
    public void dragDropEnd(DragSourceDropEvent dsde) {
        movingItems = false;
        overIndex = -1;
    }

    /**
     * Records the index of the list item (if any) pointed to by the mouse cursor
     * <p>
     * Description copied from interface:<br>
     * Called while a drag operation is ongoing, when the mouse pointer enters
     * the operable part of the drop site for the DropTarget registered with
     * this listener.
     */
    public void dragEnter(DropTargetDragEvent dtde) {
        overIndex = this.locationToIndex(dtde.getLocation());
        setSelectedIndex(overIndex);
    }


    /**
     * Records the index of the list item (if any) pointed to by the mouse cursor
     * <p>
     * Description copied from interface:<br>
     * Called when a drag operation is ongoing, while the mouse pointer is still
     * over the operable part of the drop site for the DropTarget registered with
     * this listener.
     */
    public void dragOver(DropTargetDragEvent dtde) {
        int index = this.locationToIndex(dtde.getLocation());
        if (index >= 0 && index != overIndex) {
            overIndex = index;
            setSelectedIndex(overIndex);
        }
    }

    /**
     * DropTargetListener method - presently ignored
     * <p>
     * Description copied from interface:<br>
     * Called if the user has modified the current drop gesture.
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // do nothing
    }

    /**
     * Description copied from interface:<br>
     * Called while a drag operation is ongoing, when the mouse pointer has exited
     * the operable part of the drop site for the DropTarget registered with this
     * listener.
     */
    public void dragExit(DropTargetEvent dte) {
        overIndex = -1;
    }

    /**
     * Handles the moving (for drag and drop actions within this list) or transfer
     * (for actions between lists) of list items.
     * Description copied from interface:<br>
     * Called when the drag operation has terminated with a drop on the operable part
     * of the drop site for the DropTarget  registered with this listener.
     * <p>
     * This method is responsible for undertaking the transfer of the data associated
     * with the gesture. The DropTargetDropEvent provides a means to obtain a Transferable
     * object that represents the data object(s) to be transfered.
     * <p>
     * From this method, the DropTargetListener shall accept or reject the drop via
     * the acceptDrop(int dropAction) or rejectDrop() methods of the DropTargetDropEvent
     * parameter.
     * <p>
     * Subsequent to acceptDrop(), but not before, DropTargetDropEvent's getTransferable()
     * method may be invoked, and data transfer may be performed via the returned
     * Transferable's getTransferData() method.
     * <p>
     * At the completion of a drop, an implementation of this method is required to
     * signal the success/failure of the drop by passing an appropriate boolean to the
     * DropTargetDropEvent's dropComplete(boolean success) method.
     * <p>
     * Note: The data transfer should be completed before the call to the
     * DropTargetDropEvent's dropComplete(boolean success) method. After that, a call
     * to the getTransferData() method of the Transferable returned by
     * DropTargetDropEvent.getTransferable() is guaranteed to succeed only if the data
     * transfer is local; that is, only if DropTargetDropEvent.isLocalTransfer()
     * returns true. Otherwise, the behavior of the call is implementation-dependent.
     */
    public void drop(DropTargetDropEvent dtde) {
        //Transferable stuff = dtde.getTransferable();
        
        /*
         * @todo check DataFlavor of stuff
         */
        dtde.acceptDrop(DnDConstants.ACTION_MOVE);

        if (movingItems) {
            /*
             * This is a local drag and drop to reorder items
             */
            DnDListModel<T> model = getModel();
            model.moveItems(overIndex, dragIndices);

        } else {

            // @todo stuff dragged from other list

        }

        overIndex = -1;
        movingItems = false;
        dtde.getDropTargetContext().dropComplete(true);
    }

}
