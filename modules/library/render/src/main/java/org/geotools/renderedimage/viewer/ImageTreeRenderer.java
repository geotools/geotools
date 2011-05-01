/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2007, GeoTools Project Managment Committee (PMC)
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
package org.geotools.renderedimage.viewer;

import java.awt.Component;
import java.awt.image.RenderedImage;

import javax.media.jai.RenderedOp;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Extends DefaultTreeCellRenderer to provide a good looking label for rendered
 * images and RenderedOp instances in particular
 * 
 * @author Andrea Aime
 * 
 */
class ImageTreeRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded,
				leaf, row, hasFocus);
		RenderedImage ri = (RenderedImage) value;
		if (ri instanceof RenderedOp) {
			RenderedOp op = (RenderedOp) ri;
			setText("<html><body><b>RenderedOp:</b> <i>"
					+ op.getOperationName() + "</i></body></html>");
		} else {
			setText(ri.getClass().getSimpleName());
		}
		return this;
	}

}
