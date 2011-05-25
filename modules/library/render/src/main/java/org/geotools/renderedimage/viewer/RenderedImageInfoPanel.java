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
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Tabbed {@link RenderedImage} information display
 * 
 * @author Andrea Aime
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools/renderedimage/viewer/RenderedImageInfoPanel.java $
 */
public class RenderedImageInfoPanel extends JPanel {
	// Data buffer type to name map
	private static final Map<Integer, String> TYPE_MAP = new HashMap<Integer, String>();
	// Rendering key to name map
	private static final Map<Object, String> KEY_MAP = new HashMap<Object, String>();
	JTabbedPane tabs;
	JEditorPane generalPanel;
	JEditorPane propertiesPanel;
	private JEditorPane operationPanel;
	private ImageViewer viewer;
	private ImageViewer roiViewer;
	private boolean showHistogram = true;
	private DisplayHistogram histogramPanel= new DisplayHistogram("");
        private boolean showRoi = false;

	static {
		TYPE_MAP.put(DataBuffer.TYPE_BYTE, "Byte");
		TYPE_MAP.put(DataBuffer.TYPE_DOUBLE, "Double");
		TYPE_MAP.put(DataBuffer.TYPE_FLOAT, "Float");
		TYPE_MAP.put(DataBuffer.TYPE_INT, "Int");
		TYPE_MAP.put(DataBuffer.TYPE_SHORT, "Short");
		TYPE_MAP.put(DataBuffer.TYPE_UNDEFINED, "Undefined");
		TYPE_MAP.put(DataBuffer.TYPE_USHORT, "Short");

		try {
			Field[] fields = JAI.class.getFields();
			for (Field field : fields) {
				final int modifiers = field.getModifiers();
				// funny enough most KEY in JAI are not final...
				if (Modifier.isStatic(modifiers)
						&& Modifier.isPublic(modifiers)
						&& field.getName().startsWith("KEY_")) {
					KEY_MAP.put(field.get(null), field.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RenderedImageInfoPanel() {
	    this(false, false);
	}
	
	public RenderedImageInfoPanel(final boolean showHistogram){
	    this(showHistogram,false);
	}
	
	public RenderedImageInfoPanel(final boolean showHistogram, final boolean showRoi) {
		setLayout(new BorderLayout());
		tabs = new JTabbedPane();
		add(tabs, BorderLayout.CENTER);
		this.showHistogram = showHistogram;
		this.showRoi = showRoi;
		generalPanel = buildHtmlPane();
		propertiesPanel = buildHtmlPane();
		operationPanel = buildHtmlPane();
		
		if (showRoi){
		    roiViewer = new ImageViewer();
		    viewer = new ImageViewer(roiViewer);
		    roiViewer.setRelatedViewer(viewer);
		} else {
		    viewer = new ImageViewer();
		}
		tabs.addTab("General information", scroll(generalPanel));
		tabs.addTab("Viewer", viewer);
		tabs.addTab("Properties", scroll(propertiesPanel));
		tabs.addTab("Operation", scroll(operationPanel));
		if (showHistogram)
		    tabs.addTab("Histogram", scroll(histogramPanel));
		if (showRoi)
		    tabs.addTab("ROIViewer", roiViewer);
	}

	private JScrollPane scroll(JComponent pane) {
		JScrollPane scroll = new JScrollPane(pane);
		return scroll;
	}

	private JEditorPane buildHtmlPane() {
		JEditorPane pane = new ScrollTopEditorPane("text/html", "");
		pane.setEditable(false);
		return pane;
	}

	public void setImage(RenderedImage image) {
		buildGeneralInfoPane(image);
		buildPropertiesPane(image);
		if (image instanceof RenderedOp) {
			operationPanel.setEnabled(true);
			buildOpPane((RenderedOp) image);
		} else {
			operationPanel.setEnabled(false);
		}
		if (showHistogram)
		    histogramPanel.setImage(PlanarImage.wrapRenderedImage(image));
		viewer.setImage(image);
		if (showRoi){
		    if (image instanceof RenderedOp){
		        final Object object = image.getProperty("ROI");
                        if (object != null && object instanceof ROI){
                                ROI roi = (ROI) object;
                                roiViewer.setImage(roi.getAsImage());
                        }
		    }
		}

	}

	void buildOpPane(RenderedOp image) {
		final HTMLBuilder hb = new HTMLBuilder();
		hb.title("Operation");
		hb.dataLine("Name", image.getOperationName());

		hb.title("Parameters");
		final ParameterBlock block = image.getParameterBlock();
		Vector<Object> paramValues = block.getParameters();
		for (int i = 0; i < paramValues.size(); i++) {
			String name = "Parameter " + (i + 1);
			if (block instanceof ParameterBlockJAI) {
				name = ((ParameterBlockJAI) block).getParameterListDescriptor()
						.getParamNames()[i];
			}
			hb.dataLine(name, paramValues.get(i));
		}

		hb.title("Hints");
		RenderingHints hints = image.getRenderingHints();
		List keys = new ArrayList(hints.keySet());
		for (Object key : keys) {
			String label = KEY_MAP.get(key);
			if (label == null)
				label = key.toString();
			hb.dataLine(label, hints.get(key));
		}
		operationPanel.setText(hb.getHtml());
	}

	void buildPropertiesPane(RenderedImage source) {
		HTMLBuilder hb = new HTMLBuilder();
		hb.title("Properties");
		String[] properties = source.getPropertyNames();
		if(properties==null)
			return;
		Arrays.sort(properties);
		for (String propName : properties) {
			hb.dataLine(propName, source.getProperty(propName));
		}

		propertiesPanel.setText(hb.getHtml());
		operationPanel.setCaretPosition(0);
	}

	void buildGeneralInfoPane(RenderedImage image) {
		HTMLBuilder hb = new HTMLBuilder();
		hb.title("Abstract");
		hb.dataLine("Name", getImageName(image));
		hb.dataLine("Image class", image.getClass());
		hb.dataLine("Image size", image.getWidth() + " x " + image.getHeight());

		hb.title("Tiles organisation");
		int tw = image.getTileWidth();
		int th = image.getTileHeight();
		int ytc = image.getNumYTiles();
		int xtc = image.getNumXTiles();
		hb.dataLine("Tile size", tw + " x " + th);
		hb.dataLine("Tile grid", xtc + " x " + ytc + // 
				" (" + tw * xtc + " x " + th * ytc + ")");
		hb.dataLine("Tile offsets x,y", image.getTileGridXOffset() + ", "
				+ image.getTileGridYOffset());
		hb.dataLine("Min tile x,y", image.getMinTileX() + ", "
				+ image.getMinTileY());

		hb.title("Sample model");
		SampleModel sm = image.getSampleModel();
		hb.dataLine("Sample model", sm.getClass());
		hb.dataLine("Size", sm.getWidth() + " x " + sm.getHeight());
		hb.dataLine("Bands", sm.getNumBands());
		hb.dataLine("Bands", TYPE_MAP.get(sm.getDataType()));

		hb.title("Color model");
		hb.dataLine("Color model", image.getColorModel().getClass());
		switch(image.getColorModel().getTransparency()){
			case Transparency.OPAQUE:
				hb.dataLine("Transparency", "Opaque");
				break;
			case Transparency.TRANSLUCENT:
				hb.dataLine("Transparency", "Translucent");
				break;			
			case Transparency.BITMASK:
				hb.dataLine("Transparency", "Bitmask");
				break;					
		}
		if(image.getColorModel() instanceof IndexColorModel)
		{
			final IndexColorModel icm = (IndexColorModel) image.getColorModel();
			//transparent pixel
			hb.dataLine("Transparent Pixel",Integer.toString(icm.getTransparentPixel()));
			
			// lut
			hb.dataLine("ColorMap Size",Integer.toString(icm.getMapSize()));
			final int numbands=icm.hasAlpha()?4:3;
			final byte[][] cmap= new byte[numbands][icm.getMapSize()];
			icm.getReds(cmap[0]);
			icm.getGreens(cmap[1]);
			icm.getBlues(cmap[2]);
			if(numbands==4)
				icm.getAlphas(cmap[3]);
			final StringBuilder b= new StringBuilder();
			for(int i=0;i<icm.getMapSize();i++){
				b.append("(").append(i).append(")-").append("[").append(cmap[0][i]).append(",").append(cmap[1][i]).append(",").append(cmap[2][i]);
				if(numbands==4)
					b.append(",").append(cmap[3][i]);
				b.append("]").append("\n");
			}
			hb.dataLine("ColorMap",b.toString());
		}
		hb.dataLine("Color space", image.getColorModel().getColorSpace().getClass());

		hb.title("Sources");
		List<RenderedImage> sources = image.getSources();
		if(sources==null)
		    return;
		final int size= sources.size();
		for (int i = 0; i < size; i++) {
			hb.dataLine("Source " + (i + 1), getImageName(sources.get(i)));
		}

		generalPanel.setText(hb.getHtml());
	}

	private String getImageName(RenderedImage source) {
		if (source instanceof RenderedOp) {
			return "RenderedOp, <i>" + ((RenderedOp) source).getOperationName()
					+ "</i>";
		} else {
			return "Generic <i>" + source.getClass() + "</i>";
		}
	}

	/**
	 * Simple variation over JEditorPane making the caret be at top each time
	 * the text is set into the pane
	 * 
	 * @author Andrea Aime
	 * 
	 */
	private static class ScrollTopEditorPane extends JEditorPane {

		public ScrollTopEditorPane(String type, String text) {
			super(type, text);
		}

		@Override
		public void setText(String t) {
			super.setText(t);
			setCaretPosition(0);
		}
	}
}
