Quickstart
==========

Welcome to your first GeoTools project! We are going to set up a project using GeoTools to quickly
display a shapefile on screen.

This tutorial is available for:

.. toctree::
   :maxdepth: 1
   
   eclipse
   netbeans
   intellij
   maven

If you are interested in porting this tutorial to an additional IDE please `contact us <http://geotools.org/getinvolved.html>`_ on the
users list.

Notes and Errata
----------------
* If you're running under OS X you may run into an issue where the file chooser dialog
  never appears and the application hangs. This is a known issue with the native OS X
  Swing look and feel. As a workaround you can use the cross platform look and feel; either 
  use the following static block in your files:
  
   .. code-block:: java
   
      static {
        // Set System L&F
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }  
      }
  
  or run your applications with the following VM argument `-Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel`
  
* At the time of writing the CRSLab application may fail if you try to change the projection a second
  time. There is no workaround or fix for this at the time.