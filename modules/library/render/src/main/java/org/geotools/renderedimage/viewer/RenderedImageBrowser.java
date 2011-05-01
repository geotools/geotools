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

import java.awt.BorderLayout;
import java.awt.image.RenderedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 * Full rendered image browser, made up of tree for rendered image source
 * hierarchy navigation and a info panel with various information on the
 * selected {@link RenderedImage}
 * 
 * @author Andrea Aime
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 */
public class RenderedImageBrowser extends JPanel {

	ImageTreeModel model;

	JTree imageTree;

	RenderedImageInfoPanel imageInfo;

	JSplitPane split;
	
	boolean showHistogram;

        boolean showRoi;

	public static void showChain(RenderedImage image) {
            showChain(image, true, false);
        }
	
	public static void showChain(RenderedImage image, final boolean showHistogram) {
	    showChain(image, showHistogram, false);
	}
	
	public static void showChain(RenderedImage image, final boolean showHistogram, final boolean showRoi) {
		JFrame frame = new JFrame("Rendered image information tool");
		RenderedImageBrowser info = new RenderedImageBrowser(showHistogram, showRoi);
		info.setImage(image);
		frame.setContentPane(info);
		frame.setSize(1024, 768);
		frame.setVisible(true);
	}

	public RenderedImageBrowser() {
	    this(true, false);
	}
	
	public RenderedImageBrowser(final boolean showHistogram, final boolean showRoi){
		this.showHistogram = showHistogram;
		this.showRoi = showRoi;
	        model = new ImageTreeModel();
		imageTree = new JTree(model);
		imageTree.setCellRenderer(new ImageTreeRenderer());
		imageTree.setShowsRootHandles(true);
		imageTree.putClientProperty("JTree.lineStyle", "Angled");
		imageInfo = new RenderedImageInfoPanel(showHistogram, showRoi);
		split = new JSplitPane();
		split.setLeftComponent(new JScrollPane(imageTree));
		split.setRightComponent(imageInfo);
		split.setResizeWeight(0.2);
		setLayout(new BorderLayout());
		add(split);

		imageTree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				final TreePath selectedpath = imageTree.getSelectionPath();
				if(selectedpath==null)
					imageTree.setSelectionRow(0);
				RenderedImage image = (RenderedImage) imageTree.getSelectionPath().getLastPathComponent();
				imageInfo.setImage(image);
			}

		});
	}

	public void setImage(RenderedImage image) {
	    if(image==null)
	        return;
		model.setRoot(image);
		imageTree.setSelectionPath(new TreePath(image));
		int rc = 0;
		do {
			rc = imageTree.getRowCount();
			for (int x = rc; x >= 0; x--) {
				imageTree.expandRow(x);
			}
		} while (rc != imageTree.getRowCount());
	}
}
