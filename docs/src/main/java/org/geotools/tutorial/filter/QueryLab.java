/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
// docs start source
package org.geotools.tutorial.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JDataStoreWizard;
import org.geotools.swing.table.FeatureCollectionTableModel;
import org.geotools.swing.wizard.JWizard;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

/**
 * The Query Lab is an excuse to try out Filters and Expressions on your own data with a table to show the results.
 *
 * <p>Remember when programming that you have other options then the CQL parser, you can directly make a Filter using
 * CommonFactoryFinder.getFilterFactory().
 */
@SuppressWarnings("serial")
public class QueryLab extends JFrame {
    private DataStore dataStore;
    private JComboBox<String> featureTypeCBox;
    private JTable table;
    private JTextField text;

    public static void main(String[] args) throws Exception {
        JFrame frame = new QueryLab();
        frame.setVisible(true);
    }
    // docs end main

    // docs start constructor
    public QueryLab() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        text = new JTextField(80);
        text.setText("include"); // include selects everything!
        getContentPane().add(text, BorderLayout.NORTH);

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setModel(new DefaultTableModel(5, 5));
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);

        featureTypeCBox = new JComboBox<>();
        menubar.add(featureTypeCBox);

        JMenu dataMenu = new JMenu("Data");
        menubar.add(dataMenu);
        pack();

        // docs start file menu
        fileMenu.add(new SafeAction("Open shapefile...") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                connect(new ShapefileDataStoreFactory());
            }
        });
        fileMenu.add(new SafeAction("Connect to PostGIS database...") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                connect(new PostgisNGDataStoreFactory());
            }
        });
        fileMenu.add(new SafeAction("Connect to DataStore...") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                connect(null);
            }
        });
        fileMenu.addSeparator();
        fileMenu.add(new SafeAction("Exit") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                System.exit(0);
            }
        });
        // docs end file menu

        // docs start data menu
        dataMenu.add(new SafeAction("Get features") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                filterFeatures();
            }
        });
        dataMenu.add(new SafeAction("Count") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                countFeatures();
            }
        });
        dataMenu.add(new SafeAction("Geometry") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                queryFeatures();
            }
        });
        // docs end data menu
        dataMenu.add(new SafeAction("Center") {
            @Override
            public void action(ActionEvent e) throws Throwable {
                centerFeatures();
            }
        });
    }
    // docs end constructor

    // docs start connect
    private void connect(DataStoreFactorySpi format) throws Exception {
        JDataStoreWizard wizard = new JDataStoreWizard(format);
        int result = wizard.showModalDialog();
        if (result == JWizard.FINISH) {
            Map<String, Object> connectionParameters = wizard.getConnectionParameters();
            dataStore = DataStoreFinder.getDataStore(connectionParameters);
            if (dataStore == null) {
                JOptionPane.showMessageDialog(null, "Could not connect - check parameters");
            }
            updateUI();
        }
    }
    // docs end connect

    // docs start update
    private void updateUI() throws Exception {
        ComboBoxModel<String> cbm = new DefaultComboBoxModel<>(dataStore.getTypeNames());
        featureTypeCBox.setModel(cbm);

        table.setModel(new DefaultTableModel(5, 5));
    }
    // docs end update

    // docs start filterFeatures
    private void filterFeatures() throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        SimpleFeatureSource source = dataStore.getFeatureSource(typeName);

        Filter filter = CQL.toFilter(text.getText());
        SimpleFeatureCollection features = source.getFeatures(filter);
        FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
        table.setModel(model);
    }
    // docs end filterFeatures

    // docs start countFeatures
    private void countFeatures() throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        SimpleFeatureSource source = dataStore.getFeatureSource(typeName);

        Filter filter = CQL.toFilter(text.getText());
        SimpleFeatureCollection features = source.getFeatures(filter);

        int count = features.size();
        JOptionPane.showMessageDialog(text, "Number of selected features:" + count);
    }
    // docs end countFeatures

    // docs start queryFeatures
    private void queryFeatures() throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        SimpleFeatureSource source = dataStore.getFeatureSource(typeName);

        FeatureType schema = source.getSchema();
        String name = schema.getGeometryDescriptor().getLocalName();

        Filter filter = CQL.toFilter(text.getText());

        Query query = new Query(typeName, filter, new String[] {name});

        SimpleFeatureCollection features = source.getFeatures(query);

        FeatureCollectionTableModel model = new FeatureCollectionTableModel(features);
        table.setModel(model);
    }
    // docs end queryFeatures

    // docs start centerFeatures
    private void centerFeatures() throws Exception {
        String typeName = (String) featureTypeCBox.getSelectedItem();
        SimpleFeatureSource source = dataStore.getFeatureSource(typeName);

        Filter filter = CQL.toFilter(text.getText());

        FeatureType schema = source.getSchema();
        String name = schema.getGeometryDescriptor().getLocalName();
        Query query = new Query(typeName, filter, new String[] {name});

        SimpleFeatureCollection features = source.getFeatures(query);

        double totalX = 0.0;
        double totalY = 0.0;
        long count = 0;
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                Point centroid = geom.getCentroid();
                totalX += centroid.getX();
                totalY += centroid.getY();
                count++;
            }
        }
        double averageX = totalX / (double) count;
        double averageY = totalY / (double) count;
        Coordinate center = new Coordinate(averageX, averageY);

        JOptionPane.showMessageDialog(text, "Center of selected features:" + center);
    }
    // docs end centerFeatures

}
// docs end source
