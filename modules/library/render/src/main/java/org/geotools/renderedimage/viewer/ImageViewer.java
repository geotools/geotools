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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;

import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Simple rendered image browser, allows to zoom in, out, display tile grid and
 * view pixel values on mouse over
 * 
 * @author Andrea Aime
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 */
public class ImageViewer extends JPanel {
        private ZoomableImageDisplay display;
        private ImageViewer relatedViewer;
        private JLabel status;
        private RandomIter pixelIter;
        private int[] ipixel;
        private double[] dpixel;
        private int dataType;
        private StringBuffer sb = new StringBuffer();
        private RenderedImage image;

        public ImageViewer(ImageViewer relatedViewer) {
            this();
            this.relatedViewer = relatedViewer;
        }

	public ImageViewer() {
		setLayout(new BorderLayout());

		// build the button bar
		JButton zoomIn = new JButton("Zoom in");
		JButton zoomOut = new JButton("Zoom out");
		final JToggleButton tileGrid = new JToggleButton("Tile grid");
		JPanel buttonBar = new JPanel();
		buttonBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonBar.add(zoomIn);
		buttonBar.add(zoomOut);
		buttonBar.add(tileGrid);

		// actual image viewer
		display = new ZoomableImageDisplay();
//		display.setBackground(Color.BLACK);
		tileGrid.setSelected(display.isTileGridVisible());

		// the "status bar"
		status = new JLabel("Move on the image to display pixel values... ");

		// compose
		add(buttonBar, BorderLayout.NORTH);
		add(new JScrollPane(display), BorderLayout.CENTER);
		add(status, BorderLayout.SOUTH);

		// events
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.setScale(display.getScale() * 2.0);
				if (relatedViewer != null){
				    relatedViewer.display.setScale(relatedViewer.display.getScale() * 2.0);
				}
			}

		});
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display.setScale(display.getScale() / 2.0);
			        if (relatedViewer != null){
			            relatedViewer.display.setScale(relatedViewer.display.getScale() / 2.0);
    	                        }
			}

		});
		tileGrid.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				display.setTileGridVisible(tileGrid.isSelected());
				if (relatedViewer != null){
                                    relatedViewer.display.setTileGridVisible(tileGrid.isSelected());
                                }
			}

		});
		display.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if(pixelIter != null) {
				    int x = (int) Math.round(e.getX() / display.getScale());
				    int y = (int) Math.round(e.getY() / display.getScale());
				    sb.setLength(0);
				    
				    if(x < image.getMinX() || x >= image.getMinX() + image.getWidth()
				            || y < image.getMinY() || y >= image.getMinY() + image.getHeight()) {
				        sb.append("Outside of image bounds");
				    } else {
    				    sb.append("Value at ");
    				    sb.append(x).append(", ").append(y).append(": [");
    				    if(dataType == DataBuffer.TYPE_DOUBLE || dataType == DataBuffer.TYPE_FLOAT) {
    				        pixelIter.getPixel(x, y, dpixel);
    				        for (int i = 0; i < dpixel.length; i++) {
                                sb.append(dpixel[i]);
                                if(i < dpixel.length - 1)
                                    sb.append(", ");
                            }
    				    } else { // integer samples
    				        pixelIter.getPixel(x, y, ipixel);
                            for (int i = 0; i < ipixel.length; i++) {
                                sb.append(ipixel[i]);
                                if(i < ipixel.length - 1)
                                    sb.append(", ");
                            }
    				    }
    				    sb.append("]");
				    }
	                status.setText(sb.toString());
				}
			}

		});
	}

	public void setImage(RenderedImage image) {
	    this.image = image;
		display.setImage(image);
		pixelIter = RandomIterFactory.create(image, null);
            ipixel = new int[image.getSampleModel().getNumBands()];
            dpixel = new double[image.getSampleModel().getNumBands()];
	}
	
    public ImageViewer getRelatedViewer() {
        return relatedViewer;
    }

    public void setRelatedViewer(ImageViewer relatedViewer) {
        this.relatedViewer = relatedViewer;
    }

}
