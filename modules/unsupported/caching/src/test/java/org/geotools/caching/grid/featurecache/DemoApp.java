/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.grid.featurecache;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.geotools.caching.featurecache.FeatureCacheException;
import org.geotools.caching.grid.spatialindex.store.MemoryStorage;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.FeatureCollection;

import com.vividsolutions.jts.geom.Envelope;


public class DemoApp extends JFrame {
    MemoryDataStore ds;
    GridFeatureCache cache;
    volatile boolean task_wait = true;
    JPanel jContentPane = null;
    JPanel statsPanel = null;
    JPanel graphPanel = null;
    CacheDisplayPanel panel = null;
    JButton runQueryButton = null;
    JButton pauseButton = null;
    JLabel lblNumData = null;
    JLabel lblNumReads = null;
    JLabel lblNumWrites = null;
    JLabel lblNumEvictions = null;
    int threads = 0;

    DemoApp(long seed) {
        initDataStore(seed);
        initDataCache();
        panel = new CacheDisplayPanel(cache);
        this.setContentPane(getJContentPane());
    }

    void initDataStore(long seed) {
        ds = new MemoryDataStore();

        FeatureCollection fc = DataUtilities.createUnitsquareDataSet(500, seed);
        ds.addFeatures(fc);
    }

    void initDataCache() {
        try {
            cache = new GridFeatureCache(ds.getFeatureSource(ds.getTypeNames()[0]), 100, 100,
                    MemoryStorage.createInstance());
        } catch (FeatureCacheException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();

            GridBagLayout bag = new GridBagLayout();
            jContentPane.setLayout(bag);

            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            bag.setConstraints(getStatsPanel(), c);
            jContentPane.add(getStatsPanel());
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            bag.setConstraints(panel, c);
            jContentPane.add(panel, c);
            c.weightx = 0;
            c.weighty = 0;
            bag.setConstraints(getGraphPanel(), c);
            jContentPane.add(getGraphPanel(), c);
        }

        return jContentPane;
    }

    JPanel getStatsPanel() {
        if (statsPanel == null) {
            statsPanel = new JPanel();
            statsPanel.setLayout(new GridLayout(5, 2, 1, 1));
            statsPanel.setSize(100, 400);
            statsPanel.add(new JLabel("Stats"));
            statsPanel.add(new JLabel("Panel"));
            statsPanel.add(new JLabel("Data = "));
            lblNumData = new JLabel("0");
            statsPanel.add(lblNumData);
            statsPanel.add(new JLabel("Reads = "));
            lblNumReads = new JLabel("0");
            statsPanel.add(lblNumReads);
            statsPanel.add(new JLabel("Writes = "));
            lblNumWrites = new JLabel("0");
            statsPanel.add(lblNumWrites);
            lblNumEvictions = new JLabel("0");
            statsPanel.add(new JLabel("Evictions = "));
            statsPanel.add(lblNumEvictions);
        }

        return statsPanel;
    }

    JButton getRunQueryButton() {
        if (runQueryButton == null) {
            runQueryButton = new JButton("New thread");
            runQueryButton.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent ev) {
                        Runnable task = new Runnable() {
                                public void run() {
                                    runQueries();
                                }
                            };

                        new Thread(task, new Integer(++threads).toString()).start();
                    }

                    public void mouseEntered(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }

                    public void mouseExited(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }

                    public void mousePressed(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }

                    public void mouseReleased(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }
                });
        }

        return runQueryButton;
    }

    JButton getPauseButton() {
        if (pauseButton == null) {
            pauseButton = new JButton("Start");
            pauseButton.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        if (task_wait) {
                            pauseButton.setText("Pause");
                        } else {
                            pauseButton.setText("Resume");
                        }

                        task_wait = !task_wait;
                    }

                    public void mouseEntered(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }

                    public void mouseExited(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }

                    public void mousePressed(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }

                    public void mouseReleased(MouseEvent arg0) {
                        // TODO Auto-generated method stub
                    }
                });
        }

        return pauseButton;
    }

    JPanel getGraphPanel() {
        if (graphPanel == null) {
            graphPanel = new JPanel();
            graphPanel.setSize(300, 100);
            graphPanel.add(new JLabel("GraphPanel"));
            graphPanel.add(getRunQueryButton());
            graphPanel.add(getPauseButton());
        }

        return graphPanel;
    }

    void runQueries() {
        JFrame frame = new JFrame("Thread " + Thread.currentThread().getName());
        QueryDisplayPanel display = new QueryDisplayPanel();
        frame.setContentPane(display);
        frame.setSize(200, 200);
        frame.setVisible(true);

        //		cache.clear();
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                try {
                    while (task_wait) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //System.out.print("i = " + i + ", j = " + j);
                    Envelope query = new Envelope(i * .1, (i + 1) * .1, j * .1, (j + 1) * .1);
                    display.setResult(cache.get(query));
                    lblNumData.setText(new Long(cache.tracker.getStatistics().getNumberOfData())
                        .toString());
                    lblNumReads.setText(new Long(cache.tracker.getStatistics().getReads()).toString());
                    lblNumWrites.setText(new Long(cache.tracker.getStatistics().getWrites())
                        .toString());
                    lblNumEvictions.setText(new Integer(cache.tracker.getEvictions()).toString());
                    panel.setCurrentQuery(Thread.currentThread().getName(), query);
                    panel.repaint();

                    Object waiter = new Object();

                    synchronized (waiter) {
                        try {
                            waiter.wait(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        frame.setVisible(false);
        frame.dispose();
        panel.removeWorker(Thread.currentThread().getName());
        panel.repaint();
    }

    public static void main(String[] args) {
        long seed = 1025;

        if (args.length < 1) {
            System.out.println("Usage: DemoApp seed");
            System.out.println("Using default for seed value : " + seed);
        } else {
            try {
                seed = Long.parseLong(args[0]);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                    "Error : seed argument must be numeric ; input was : " + args[0]);
            }
        }

        DemoApp thisClass = new DemoApp(seed);
        thisClass.setSize(600, 400);
        thisClass.setTitle("Google SoC : Feature Cache Demo Application");
        thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisClass.setVisible(true);

        thisClass.runQueries();
    }
}
