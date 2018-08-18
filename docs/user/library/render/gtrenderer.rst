GTRenderer to draw maps
-----------------------

GTRenderer renderer is the reason why you signed up for this whole GeoTools experience; you want to see a Map.

GTRenderer is actually an interface; currently there are two implementations:

* StreamingRenderer - a great implementation that does not cache anything. This decision makes it easier to understand and allows it to tackle that large datasets without running out of memory.
* ShapefileRenderer - (unsupported) restricted to shapefiles as a playground for trying out speed improvements. However all the good optimisations have been picked up by StreamingRenderer.


.. image:: /images/GTRenderer.PNG

Here is how to drawn an outputArea rectangle::
  
  GTRenderer draw = new StreamingRenderer();
  draw.setMapContent(map);
  
  draw.paint(g2d, outputArea, map.getLayerBounds() );

If you have completed the Quickstart, this is the approach used by the JMapPane class we use in a
lot of our tutorials.

The important part of the above example is that GTRenderer works on the Java2D class
**Graphics2D**. You can find many implementations of Graphics2D allowing GeoTools to work with a
range of graphics systems beyond the screen.

Swing
^^^^^


You can create a swing control to render the image interactively; we
provide an example JMapPane for use in our tutorials.

GTRenderer is just a rendering engine - in your own application you may
consider the following ideas:

* Experiment with different Java 2D graphics settings such as
  anti-aliasing
* Use background threads to draw tiles, and allowing your swing control
  to pull the tiles onto the screen for a smooth "slippy" map

3D
^^

A Google summer of code student put together and example of rendering
into a texture buffers and using OpenGL to handle panning and zooming.

Personally I would look for an Graphics2D implementation that was
backed by OpenGL commands and use GeoTools to render out into the scene
graph.

If you are interested in the students code it is currently in the
"spike" directory of GeoTools where we keep experiments.

Printing
^^^^^^^^

The Java2D library is also used for Java printing. You can print using StreamingRenderer, the code works like normal just
use the Graphics2D object from your Printer.

uDig uses this facility to allow for printing maps directly to the
printer.

PDF
^^^

We have also had success using GTRenderer and Batik for the generation
of PDF output (they provide a Graphics2D object).

You can see this functionality in uDig and GeoServer.

The following example is taken from uDig::

    Rectangle suggestedPageSize = getITextPageSize(page1.getPageSize());                
    Rectangle pageSize = rotatePageIfNecessary(suggestedPageSize);
    //rotate if we need landscape
    Document document = new Document(pageSize);  
    ...
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFile));
    document.open();            
    Graphics2D graphics = cb.createGraphics(pageSize.getWidth(), pageSize.getHeight());
    
    // call your GTRenderer here
    GTRenderer draw = new StreamingRenderer();
    draw.setMapContent(map);
    
    draw.paint(graphics, outputArea, map.getLayerBounds() );
    
    // cleanup
    graphics.dispose();
    
    //cleanup
    document.close();
    writer.close();
  
  PDF Tips:
  
  You may wish to increase the page size by 2 and then scale the result by 50% in order to produce high resolution raster layers

SVG
^^^

 The GeoVista team have used the Batik project to generate SVG output.

 You will need to manage the `Batik dependencies
 <https://xmlgraphics.apache.org/batik/>`_ yourself, make sure to include
 `batik-codec` if your map contains any image based graphics.

Thanks to James Macgill for the following code example:

.. literalinclude:: /../src/main/java/org/geotools/render/GenerateSVG.java
   :language: java
   :start-after: // exportSVG start
   :end-before: // exportSVG end

Image
^^^^^

You can also ask Java to make you a Graphics2D for a BufferedImage in memory. After drawing into this
image you can write it out to disk.

Here is an example from Oliver on the email list (modified slightly to use current GeoTools classes)::
  
    public void saveImage(final MapContent map, final String file, final int imageWidth) {

        GTRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);

        Rectangle imageBounds = null;
        ReferencedEnvelope mapBounds = null;
        try {
            mapBounds = map.getMaxBounds();
            double heightToWidth = mapBounds.getSpan(1) / mapBounds.getSpan(0);
            imageBounds = new Rectangle(
                    0, 0, imageWidth, (int) Math.round(imageWidth * heightToWidth));

        } catch (Exception e) {
            // failed to access map layers
            throw new RuntimeException(e);
        }

        BufferedImage image = new BufferedImage(imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB); 

        Graphics2D gr = image.createGraphics();
        gr.setPaint(Color.WHITE);
        gr.fill(imageBounds);

        try {
            renderer.paint(gr, imageBounds, mapBounds);
            File fileToSave = new File(file);
            ImageIO.write(image, "jpeg", fileToSave);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

