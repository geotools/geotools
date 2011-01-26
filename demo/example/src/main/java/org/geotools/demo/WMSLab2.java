package org.geotools.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.OperationType;
import org.geotools.data.ows.Service;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.ows.WMSRequest;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetLegendGraphicRequest;
import org.geotools.data.wms.request.GetMapRequest;

/**
 * This lab explores the use of the GeoTools WMS client code.
 * <p>
 * The GeoTools WMS client is a little bit more than simply sending away GetMap
 * request, the trick is to send the correct GetMap request based on the version
 * of the web map server specification the server supports, and be able to chose
 * layers and styles supported by the server.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class WMSLab2 extends JFrame {
    private static final long serialVersionUID = -3039255518595806472L;

    /** Original coverage we are working on */
    WebMapServer wms;
    CoveragePanel panel;

    Image image;
    JList layers;
    JButton getMapButton;
    JComboBox styleCombo;
    /**
     * Map<Layer,Style> as controlled by styleCombo box
     */
    Map styles = new HashMap();
    Layer selectedLayer = null;

    DefaultComboBoxModel availableStyles;

    /**
     * Explore the functionality of the provided GridCoverage (think
     * BufferedImage + CRS).
     * <p>
     * A GridCoverage literally a set of features that "covers" an area without
     * gaps; in the case of grid coverage the area is covered by an regular
     * grid.
     * <p>
     * Coverage work by letting you call a "sample" operation in order to
     * retrieve a Record of the data at the location. A grid coverage lets you
     * express the location using row and column.
     * <p>
     * 
     * @param coverage
     */
    public WMSLab2(WebMapServer server) {
        this.wms = server;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String title = getWMSTitle(wms);
        setTitle(title);

        Set good = new HashSet( Arrays.asList( WMSUtils.getNamedLayers(wms.getCapabilities())));
        this.layers = new JList(good.toArray());
        layers.setCellRenderer(new LayerCellRenderer());
        layers.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        setSelectedLayer((Layer) layers.getSelectedValue());
                    }
                });
        JScrollPane scrollPane = new JScrollPane(layers);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        add(scrollPane, BorderLayout.WEST);

        this.panel = new CoveragePanel();
        add(panel, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        getMapButton = new JButton("GetMap");
        getMapButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getMap();
            }
        });
        buttons.add(getMapButton);
        availableStyles = new DefaultComboBoxModel();
        styleCombo = new JComboBox(availableStyles);
        styleCombo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (selectedLayer != null) {
                    styles.put(selectedLayer, styleCombo.getSelectedItem());
                }
            }
        });
        styleCombo.setRenderer(new StyleCellRenderer());
        buttons.add(styleCombo);

        add(buttons, BorderLayout.NORTH);
        pack();
    }

    public String getWMSTitle(WebMapServer wms) {
        WMSCapabilities capabilities = wms.getCapabilities();
        Service service = capabilities.getService();
        String title = service.getTitle();
        return title;
    }


    public void setSelectedLayer(Layer selected) {
        availableStyles.removeAllElements();
        this.selectedLayer = selected;
        if (selected != null) {
            // availableStyles.addElement("default");
            for( StyleImpl style : selected.getStyles() ){
                availableStyles.addElement(style);
            }
        }
        styleCombo.repaint();
    }

    class StyleCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 7421698429520525469L;

        public Component getListCellRendererComponent(JList list, Object value, // value
                // to
                // display
                int index, // cell index
                boolean iss, // is the cell selected
                boolean chf) // the list and the cell have the focus
        {
            super.getListCellRendererComponent(list, value, index, iss, chf);
            StyleImpl style = (StyleImpl) value;
            if (style == null)
                return this;

            String title = style.getTitle() == null ? style.getName() : style
                    .getTitle().toString();
            setText(title);
            return this;
        }

        Icon getIcon(StyleImpl style) {
            List urlList = style.getLegendURLs();
            if (urlList == null || urlList.isEmpty())
                return null;

            URL url = (URL) style.getLegendURLs().get(0);
            ImageIcon icon = new ImageIcon(url);
            return icon;
        }
    }

    class LayerCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1173012107250651733L;
        HashMap icons = new HashMap();

        public Component getListCellRendererComponent(JList list, Object value, // value
                // to
                // display
                int index, // cell index
                boolean iss, // is the cell selected
                boolean chf) // the list and the cell have the focus
        {
            super.getListCellRendererComponent(list, value, index, iss, chf);
            Layer layer = (Layer) value;
            setText(layer.getTitle());
            Icon glyph;
            if (icons.containsKey(layer)) {
                glyph = (Icon) icons.get(layer);
            } else {
                glyph = getLegendGraphics(layer);
                icons.put(layer, glyph);
            }
            setIcon(glyph);
            setToolTipText(layer.getName());
            return this;
        }
    }

    public Icon getLegendGraphics(Layer layer) {
        Icon icon = null;
        WMSCapabilities capabilities = wms.getCapabilities();
        WMSRequest request = capabilities.getRequest();
        OperationType description = request.getGetLegendGraphic();
        if (description == null) return null;
        
        GetLegendGraphicRequest legendGraphicsRequest = wms
                .createGetLegendGraphicRequest();
        legendGraphicsRequest.setLayer(layer.getName());
        legendGraphicsRequest.setStyle(getNamedStyle(layer));
        legendGraphicsRequest.setFormat((String) description.getFormats()
                .iterator().next());
        URL url = legendGraphicsRequest.getFinalURL();
        return new ImageIcon(url);
    }

    private String getNamedStyle(Layer layer) {
        Object layerStyle = styles.get(layer);
        String namedStyle = null;
        if (layerStyle == null) {
            namedStyle = null;
        } else if (layerStyle instanceof String) {
            namedStyle = (String) layerStyle;

        } else if (layerStyle instanceof StyleImpl) {
            StyleImpl style = (StyleImpl) layerStyle;
            namedStyle = style.getName();
        }
        return namedStyle;
    }
    public void getMap() {
        try {
            if (true) {
                Layer selected = (Layer) layers.getSelectedValue();
                getMap(selected);
            }
            Object selection[] = layers.getSelectedValues();
            List layerList = Arrays.asList(selection);
            getMap(layerList);
        } catch (Exception e1) {
            image = null;
        }
    }

    private void getMap(Layer layer) throws Exception {
        GetMapRequest mapRequest = wms.createGetMapRequest();
        String style = getNamedStyle( layer );
        mapRequest.addLayer(layer, style);
        
        mapRequest.setFormat(getImageFormat(wms));
        CRSEnvelope box = getCRSEnvelope(layer, null);
        if (box == null) {
            box = layer.getLatLonBoundingBox();
            box.setEPSGCode("EPSG:4326");
        }
        mapRequest.setSRS(box.getEPSGCode());
        mapRequest.setBBox(box);
        mapRequest.setDimensions(panel.getWidth(), panel.getHeight());
        
        URL url = mapRequest.getFinalURL();
        ImageIcon load = new ImageIcon(url);
        image = load.getImage();
        panel.repaint();
    }
    private void getMap(List layers) throws Exception {
        if (layers == null || layers.isEmpty())
            return;

        GetMapRequest mapRequest = wms.createGetMapRequest();
        CRSEnvelope bounds = null;
        for (Iterator i = layers.iterator(); i.hasNext();) {
            Layer layer = (Layer) i.next();
            String namedStyle = getNamedStyle(layer);
            mapRequest.addLayer(layer, namedStyle);
            CRSEnvelope box = layer.getLatLonBoundingBox();
            box.setEPSGCode("EPSG:4326");
            if (box != null) {
                if (bounds != null) {
                    bounds.setMinX(Math.min(bounds.getMinX(), box.getMinX()));
                    bounds.setMaxX(Math.max(bounds.getMaxX(), box.getMaxX()));
                    bounds.setMinY(Math.min(bounds.getMinY(), box.getMinY()));
                    bounds.setMaxY(Math.max(bounds.getMaxY(), box.getMaxY()));
                } else {
                    bounds = box;
                }
            }
        }
        if (bounds == null) {
            bounds = new CRSEnvelope();
            bounds.setEPSGCode("EPSG:4326");
        }
        mapRequest.setFormat(getImageFormat(wms));

        mapRequest.setSRS(bounds.getEPSGCode());
        mapRequest.setBBox(bounds);
        mapRequest.setDimensions(panel.getWidth(), panel.getHeight());

        URL url = mapRequest.getFinalURL();
        ImageIcon load = new ImageIcon(url);
        image = load.getImage();
        panel.repaint();
    }


    /**
     * Some WMS Servers like GeoServer support strange formats like KML and PDF
     * 
     * @return
     */
    String getImageFormat(WebMapServer wms) {
        OperationType description = wms.getCapabilities().getRequest()
                .getGetMap();

        List formats = description.getFormats();
        for (Iterator i = formats.iterator(); i.hasNext();) {
            String format = (String) i.next();
            if (format.indexOf("pdf") != -1)
                continue;

            return format;
        }
        return "image/jpeg";
    }

    /**
     * Search through the layer data structure for bounds for the provided srs
     * (if null we will return the first bounds).
     * 
     * @param srs
     * @return CRSEnvelope
     */
    CRSEnvelope getCRSEnvelope(Layer layer, String srs) {

        if (layer == null)
            return null;
        Map boundingBoxes = layer.getBoundingBoxes();
        if (boundingBoxes != null && !boundingBoxes.isEmpty()) {
            for (Iterator i = boundingBoxes.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Entry) i.next();
                String layerSrs = (String) entry.getKey();
                CRSEnvelope box = (CRSEnvelope) entry.getValue();
                if (srs == null || srs.equals(layerSrs)) {
                    return box;
                }
            }
        }
        return getCRSEnvelope(layer.getParent(), srs);
    }

    class CoveragePanel extends JPanel {
        private static final long serialVersionUID = -4755270758709990530L;

        CoveragePanel() {
            setBackground(Color.WHITE);
        }

        public Dimension getPreferredSize() {
            return new Dimension(640, 400);
        }

        public void paintComponent(Graphics graphics) {
            super.paintComponents(graphics);
            if (image != null) {
                Graphics2D g = (Graphics2D) graphics;
                g.drawImage(image, 0, 0, null);
            }
        }
    }

    /**
     * Prompt the user for a file and open up ImageLab.
     * 
     * @param args
     *                filename of image
     */
    public static void main(String[] args) throws Exception {
        URL server = getServerURL(args);
        WebMapServer wms;
    
        System.out.println("Connecting to " + server);
        wms = new WebMapServer(server);
    
        System.out.println("Welcome");
        WMSLab2 wmsLab = new WMSLab2(wms);
        wmsLab.setVisible(true);
    }

    public static String[] getLayerNames(WebMapServer wms) {
        Layer[] namedLayers = WMSUtils.getNamedLayers(wms.getCapabilities());
        String[] names = new String[namedLayers.length];
        for (int i = 0; i < namedLayers.length; i++) {
            Layer layer = namedLayers[i];
            names[i] = layer.getName();
        }
        return names;
    }

    public static URL getServerURL(String[] args) throws MalformedURLException {
        if (args.length == 0) {
            Object[] servers = new Object[] {
                    "http://localhost:8080/geoserver/wms?service=WMS&request=GetCapabilities",
                    "http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?Service=WMS&VERSION=1.1.0&REQUEST=GetCapabilities",
                    "http://wms.jpl.nasa.gov/wms.cgi?Service=WMS&Version=1.1.1&Request=GetCapabilities",
                    "http://giswebservices.massgis.state.ma.us/geoserver/wms?service=WMS&request=GetCapabilities",
                    "http://wms.cits.rncan.gc.ca/cgi-bin/cubeserv.cgi?VERSION=1.1.0&REQUEST=GetCapabilities",
                    "http://atlas.gc.ca/cgi-bin/atlaswms_en?VERSION=1.1.1&Request=GetCapabilities&Service=WMS", };
            Object selected = JOptionPane.showInputDialog(null,
                    "WMS GetCapabilities URL", "Choose a WMS Server",
                    JOptionPane.QUESTION_MESSAGE, null, servers, null);

            if (selected == null)
                System.exit(0);

            return new URL((String) selected);
        } else {
            return new URL(args[0]);
        }
    }
}
