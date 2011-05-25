package org.geotools.swing.wms;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;

/**
 * Dialog providing a chance to chose a WMSLayer.
 * <p>
 * Initially we are going to work with a JList, we will switch
 * to a JTree when we get a chance.
 *
 *
 * @source $URL$
 */
public class WMSLayerChooser extends JDialog implements ActionListener {
    private static final long serialVersionUID = -409825958139086013L;

    WebMapServer wms;

    WMSCapabilities caps;

    JList list;

    private DefaultListModel model;

    public WMSLayerChooser() throws HeadlessException {
        super();
        init();
    }

    public WMSLayerChooser(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
        init();
    }

    public WMSLayerChooser(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        init();
    }

    public WMSLayerChooser(Frame owner, String title, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        init();
    }

    public WMSLayerChooser(Frame owner, String title) throws HeadlessException {
        super(owner, title);
        init();
    }

    public WMSLayerChooser(Frame owner) throws HeadlessException {
        super(owner);
        init();
    }

    private void init() {
            this.setSize(400, 200);
            
            // Create and initialize the buttons.
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(this);
            
            final JButton setButton = new JButton("Select");
            setButton.setActionCommand("Select");
            setButton.addActionListener(this);
            getRootPane().setDefaultButton(setButton);
            
            model = new DefaultListModel();
            list = new JList( model );
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setVisibleRowCount(-1);
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        setButton.doClick(); // emulate button click
                    }
                }
            });
            
            JScrollPane listScroller = new JScrollPane(list);
            listScroller.setPreferredSize(new Dimension(400, 280));
            JPanel listPane = new JPanel();
            listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
            JLabel label = new JLabel("Layers");
            label.setLabelFor(list);
            listPane.add(label);
            listPane.add(Box.createRigidArea(new Dimension(0, 5)));
            listPane.add(listScroller);
            listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Lay out the buttons from left to right.
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
            buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            buttonPane.add(Box.createHorizontalGlue());
            buttonPane.add(cancelButton);
            buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
            buttonPane.add(setButton);

            // Put everything together, using the content pane's BorderLayout.
            Container contentPane = getContentPane();
            contentPane.add(listPane, BorderLayout.CENTER);
            contentPane.add(buttonPane, BorderLayout.PAGE_END);
    }

    private void setupLayersList() {
        caps = wms.getCapabilities();
        model.clear();
        for (Layer layer :WMSUtils.getNamedLayers( caps )) {
            String title = layer.getTitle();
            if (title == null) {
                title = layer.getName();
            }
            model.addElement( layer );
        }
    }

    public int getLayer() {
        return list.getSelectedIndex();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("cancel")) {
            list.clearSelection();
        }
        this.setVisible(false);
    }

    public ArrayList<Layer> getLayers() {
        ArrayList<Layer> layers = new ArrayList<Layer>();
        for( Object selected : list.getSelectedValues() ){
            layers.add( (Layer) selected );
        }
        return layers;
    }

    public WebMapServer getWms() {
        return wms;
    }

    public void setWMS(WebMapServer wms) {
        this.wms = wms;
        setupLayersList();
    }
    
    public static List<Layer> showSelectLayer( WebMapServer wms) {
        if( wms == null ){
            return null; // run along nothing to see here
        }
        WMSLayerChooser prompt = new WMSLayerChooser();        
        prompt.setWMS( wms ); // this will populate the layers list        
        prompt.setModal(true);
        prompt.pack();
        prompt.setVisible(true);
 
        return prompt.getLayers();
    }
}
