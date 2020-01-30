:Author: Christian Mueller
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

******************************
 AffineTransformation Tutorial
******************************

.. sectionauthor:: Christian Mueller

Welcome
=======

Welcome to this tutorial about affine transformations which are used to convert coordinates from one
domain to another. As an example we will convert world coordinates to pixel (screen or image)
coordinates. All of the mathematics required will be introduced along the way and you will soon be
making use of the power of these kind of calculations.

.. topic:: About Your Instructor
   
   Christian Mueller is a GeoTools developer and is working for a big customer to build
   up a GIS infrastructure. In the GeoTools project he is best known for his work on
   :doc:`/library/coverage/jdbc/index`

Definitions
-----------

Before we start, some important definitions.

============= ====================
Abbreviation  Meaning
============= ====================
``LLC``       lower left corner 
``ULC``       upper left corner 
``URC``       upper right corner
``LRC``       lower right corner
============= ====================

We have a rectangle in world coordinates, the origin is at the ``LLC, x = 2000, y = 3000,width = 8000``
units an ``height=9000`` units.

The resulting corner points are:

============= =====================
Point         world x,y coordinates
============= =====================
``LLC``            2000,  3000 
``ULC``            2000, 12000 
``URC``           10000, 12000 
``LRC``           10000,  3000 
============= =====================

.. image:: images/world.png

Next, we want to map this rectangle to a screen, using pixel coordinates. The available screen
``size=400x300`` pixels. Unfortunately, pixel coordinates have their origin in the ``ULC`` normally,
not in the ``LLC``. The next table shows the mappings.:

============= ====================== =====================
Point         world x,y coordinates  pixel x,y coordinates 
============= ====================== =====================
``LLC``       2000, 3000             0,300
``ULC``       2000,12000             0,  0 
``URC``       10000,12000            400,  0
``LRC``       10000, 3000            400,300
============= ====================== =====================

.. image:: images/screen.png

The challenge is to find a method how to transform each point within the world rectangle to a
point in the pixel rectangle. (And the other way around)

Mathematical Background
=======================

Since most people I know dislike mathematics I will reduce this section to an absolute minimum.
An affine transformation is based on a matrix.

* Here we need a ``3x3`` matrix like this one::
  
    [ m00 m01 m02 ] 
    [ m10 m11 m12 ] 
    [   0   0   1 ] 

  The numbers are row and column indices, e.g. **m12** is the matrix element in
  row 1, column 2. The values in the bottom row are always as shown.

* A point is represented as a *column vector*::
  
    [ x ] 
    [ y ] 
    [ 1 ] 

  Once again, the bottom element is a constant (always 1).

* Transforming the point coordinates involves multiplying the point's column vector by
  the affine transform matrix::
  
        [ x_new] = [ m00 m01 m02 ] [ x ] = [ m00x + m01y + m02 ]
        [ y_new] = [ m10 m11 m12 ] [ y ] = [ m10x + m11y + m12 ]
        [ 1 ]    = [ 0   0     1 ] [ 1 ] = [ 0 + 0 + 1 ]

.. note::

   It is very important to understand this, but don't worry if you are unfamiliar with 
   matrix arithmetic because the steps are explained in detail below.

* **Identify Matrix**
  
  A quick test. Why is the matrix:: 
  
    [ 1 0 0 ] 
    [ 0 1 0 ] 
    [ 0 0 1 ] 
  
  called the identity matrix ?
  
  Answer::
  
    [ 1 0 0 ] [ x ] = [ x ] 
    [ 0 1 0 ] [ y ] = [ y ]
    [ 0 0 1 ] [ 1 ] = [ 1 ]
  
  The detailed calculation::
  
    1*x + 0*y + 0*1 = x
    0*x + 1*y + 0*1 = y
    0*x + 0*y + 1*1 = 1

* **Swap X and Y**
  
  A second test. What is this matrix responsible for:: 
  
    [ 0 1 0 ] 
    [ 1 0 0 ] 
    [ 0 0 1 ] 
  
  This matrix swaps x and y::
 
    [ 0 1 0 ] [ x ] = [ y ] 
    [ 1 0 0 ] [ y ] = [ x ]
    [ 0 0 1 ] [ 1 ] = [ 1 ]
  
  The detailed calculation::
  
    0*x + 1*y + 0*1 = y
    1*x + 0*y + 0*1 = x
    0*x + 0*y + 1*1 = 1

Calculation
===========

We need three steps for getting pixel ``x/y`` from world ``x/y``.

1. Translate
2. Scale
3. Mirror

These are discussed below.

Translate Operation
-------------------
   
1. We have to shift the origin of the world rectangle to 0,0. This is easy. The ``LLC`` has values
   2000,3000, we only need to subtract 2000 from ``x``  and 3000 from ``y``. We use the ``URC`` with
   values 10000,12000 to demonstrate the calculation.

2. Java Code
   
   .. code-block:: java
        
        AffineTransform translate= AffineTransform.getTranslateInstance(-2000, -3000);
        System.out.println("Translate:" + translate.toString());
        Point2D p = new Point2D.Double(2000,3000);
        System.out.println(translate.transform(p, null));
   
3. Output::
     
     Translate:AffineTransform[[1.0, 0.0, -2000.0], [0.0, 1.0, -3000.0]]
     Point2D.Double[0.0, 0.0]
  
4. The ``toString()`` method of the ``AffineTransform`` class only shows the first two rows of the
   matrix. The static method ``getTranslateInstance`` is a convenience method, otherwise you have
   to call a constructor with 6 values.

5. The matrix used::
     
     [  1.00  0.00 -2000.00 ] 
     [  0.00  1.00 -3000.00 ] 
     [  0.00  0.00  1.00 ] 
   
6. The detailed calculation::
   
        1 * 2000 + 0 * 3000 + 1 * -2000 = 0
        0 * 2000 + 1 * 3000 + 1 * -3000 = 0
        0 * 2000 + 0 * 3000 + 1 * 1     = 1

7. The result of all four corner points is:
   
    ======= ==================== ====================
    Point           before               after
    ======= ==================== ====================
    ``LLC``          2000, 3000         0,    0
    ``ULC``          2000,12000         0, 9000
    ``URC``         10000,12000      8000, 9000
    ``LRC``         10000, 3000      8000,    0
    ======= ==================== ====================

Scale Operation
---------------

The world rectangle has a width of 8000 units and a height of 9000 units, the pixel dimension has
a width of 400 pixels and a height of 300  pixels. We need to scale with 400/8000.0 and
300 / 9000.0.

1. Let us use the point in the middle of the world rectangle after the translate
   operation, having its ``LLC`` at 0,0.

2. Java Code
   
   .. code-block:: java
   
        AffineTransform scale= AffineTransform.getScaleInstance(400/8000.0, 300 / 9000.0);
        System.out.println("Scale:" + scale.toString());
        p = new Point2D.Double(4000,4500);
        System.out.println(scale.transform(p, null));
   
3. Output::
   
       Scale:AffineTransform[[0.05, 0.0, 0.0], [0.0, 0.033333333333333, 0.0]]
       Point2D.Double[200.0, 150.0]
   
4. The detailed calculation (omitting the last one, the result is always 1) ::
   
        0.05 * 4000 + 0      * 5000 + 1 * 0 = 200
        0    * 4000 + 0.03.. * 5000 + 1 * 0 = 150
   
   
5. The used matrix is::
   
     [  0.05  0.00    0.00 ] 
     [  0.00  0.03..  0.00 ] 
     [  0.00  0.00    1.00 ] 

6. Using the output of the translation operation as the input for the mirror operation, the result
   of all four corner points is:
    
    ======= ==================== ====================
    Point           before               after
    ======= ==================== ====================
    ``LLC``             0,    0             0,    0
    ``ULC``             0, 9000             0,  300
    ``URC``          8000, 9000           400,  300
    ``LRC``          8000,    0           400,    0
    ======= ==================== ====================

Mirror Operation
----------------

Remember: The world rectangle has its origin in the ``LLC`` and  the pixel rectangle has its origin
in the ``ULC``!

There is a need for a mirroring operation. After the scale operation, we have already pixel
values, but we must mirror the y value. The x value should not change. For mirroring,
we must calculate:: 

    y_new = 300 - y

1. Let us create the appropriate affine transform.

2. Java Code
   
   .. code-block:: java
   
        AffineTransform mirror_y = new AffineTransform(1, 0, 0, -1, 0, 300);
        System.out.println("Mirror:" + mirror_y.toString());
        p = new Point2D.Double(100,50);
        System.out.println(mirror_y.transform(p, null));
   
3. Output::
   
        Mirror:AffineTransform[[1.0, 0.0, 0.0], [0.0, -1.0, 300.0]]
        Point2D.Double[100.0, 250.0]

4. The x value is unchanged, but the y value is mirrored.

5. The matrix used is::
    
     [  1.00  0.00   0.00 ] 
     [  0.00 -1.00 300.00 ] 
     [  0.00  0.00   1.00 ] 

6. The detailed calculation::
   
       1 * 100 +  0 * 50  + 1 *   0 = 100
       0 * 100 + -1 * 50 +  1 * 300 = 250

7. Using the output of the scale operation as the input for the scale operation, the result of all
   four corner points is:
   
   ======= ==================== ====================
   Point           before               after
   ======= ==================== ====================
   ``LLC``             0,    0             0,  300
   ``ULC``             0,  300             0,    0
   ``URC``           400,  300           400,    0
   ``LRC``           400,    0           400,  300
   ======= ==================== ====================

Matrix Magic
============

Concatenation
-------------

.. sidebar:: Magic Part 1
   
   The ability to concatenate transforms
   into a single matrix is vital to the
   performance of computer graphics and GIS.

Until now, most of you will say that it is easier to write this calculations without the use of the ``AffineTransform`` class, be patient.
We have created 3 ``AffineTransform`` objects, now we combine them. There is a method 

* ``AffineTransform.concatenate(AffineTransform other)``

Which we will be introducing in this section. The only important thing to know is that you have
to START with the LAST ``AffineTransform`` object, NOT with the first.

1. Java Code
   
   .. code-block:: java
   
        AffineTransform world2pixel = new AffineTransform(mirror_y);        
        world2pixel.concatenate(scale);
        world2pixel.concatenate(translate);
        System.out.println("World2Pixel:" + world2pixel.toString());

        p = new Point2D.Double(2000,3000);
        System.out.println("LLC: " + world2pixel.transform(p,null));
        p = new Point2D.Double(2000,12000);
        System.out.println("ULC: " + world2pixel.transform(p,null));        
        p = new Point2D.Double(10000,12000);
        System.out.println("URC: " + world2pixel.transform(p,null));
        p = new Point2D.Double(10000,3000);
        System.out.println("LRC: " + world2pixel.transform(p,null));
   
2. Output::

     LLC: Point2D.Double[  0.0, 300.0]
     ULC: Point2D.Double[  0.0,   0.0]
     URC: Point2D.Double[400.0,   0.0]
     LRC: Point2D.Double[400.0, 300.0]
   
3. The combined matrix is::
  
     [  0.05  0.00 -100.00 ] 
     [  0.00 -0.03  400.00 ] 
     [  0.00  0.00    1.00 ] 
   
5. Lets use ``LRC`` (10000,3000) to show a detailed calculation::

      0.05 * 10000 +     0 * 3000 + 1 * -100  = 400
      0    * 10000 + -0.03.. 3000 + 1 *  400  = 300 

6. At the end of the day, you have exactly one ``AffineTransform`` object doing the job.

Graphics2D
^^^^^^^^^^

As an example of the power of ``Affinetransformation``, the
``java.awt.Graphics2D`` class has a method:

* ``Graphic2d.setTransform(AffineTransform tx)``

If you set our transform object in your ``Graphics2D`` object, you can draw and paint with world
coordinates.

Inversion
---------

.. sidebar:: Magic Part II
   
   The ability to invert a matrix and go
   the other way allows you to determine
   what a user clicked on.

Create an inverse transformation
--------------------------------

What about calculating world coordinates from pixel coordinates? This is a commonly asked in terms
of "what did the user click on?".

This is easy, get the **inverse** transform as shown here:

1. Look at this code segment.
   
    Java Code
   
   .. code-block:: java

        AffineTransform pixel2World=null;
        try {
            pixel2World = world2pixel.createInverse();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        System.out.println("Pixel2World:" + pixel2World.toString());
        
        p = new Point2D.Double(200,150);
        System.out.println("World : " + pixel2World.transform(p,null));
   
2. Output::
   
      Pixel2World:AffineTransform[[20.0, 0.0, 2000.0], [0.0, -30.0, 12000.0]]
      World : Point2D.Double[6000.0, 7500.0]

3. The inverse matrix is::

     [ 20.00  0.00 2000.00 ] 
     [  0.00 -30.00 12000.00 ] 
     [  0.00  0.00  1.00 ] 

4. Let us use the pixel values  200,150 (representing the center of the pixel rectangle) to show
   a detailed calculation::

     20 * 200 +   0 * 150 + 1 * 2000 =  6000
      0 * 200 + -30 * 150 + 1* 12000 =  7500

5. The point 6000,7500 is indeed the center of our world rectangle.

6. The inversion result of our pixel corner points is:
   
   ======= ==================== ====================
   Point           before               after
   ======= ==================== ====================
   ``LLC``          0,  300          2000, 3000
   ``ULC``          0,    0          2000,12000
   ``URC``        400,    0         10000,12000
   ``LRC``        400,  300         10000, 3000
   ======= ==================== ====================

.. hint::

 As an example, if you want to show the world coordinates while a user moves the mouse over a map,
 this transform is what you need.

NoninvertibleTransformException
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

It can happen that a matrix is not invertible. This chapter is for the interested reader, if you
dislike mathematics, you can skip it. The only import thing you should now is that for this kind
of matrices the exception can never occur.

A matrix has a determinant. For creating the inverse matrix, divisions by the determinant are
needed. As we know from school, it is not  allowed to divide by zero. As a consequence, the
determinant with value 0 prevents the creation of an inverse matrix.

* For a ``2x2`` matrix::

     [ a b ]
     [ c d ]
 
 the determinant is::
 
      a*d - c*b

* For a ``3x3`` matrix::

     [ a b c]
     [ d e f]
     [ g h i]

  the determinant is::

     a * ( e*i-h*f ) - d * (b*i -h *c) + g * ( b*f -e *c)

Fortunately, our matrices always have g = 0,  h = 0 and i = 1.

* Setting 0 for g results in::

     a * ( e*i-h*f ) - d * (b*i -h *c) 

* Setting i to 1 results in::

      a * ( e-h*f ) - d * (b -h *c) 

* Finally, we set h to 0::

      a * e - d * b

This is in fact the same calculation as for the ``2x2`` matrix.

1. Let as construct such a matrix 
   
   .. code-block:: java

            AffineTransform noInvert = new AffineTransform(5,3,5,3,0,0);
            System.out.println("NoInvert : "+noInvert.toString());
            System.out.println("Determinant : "+noInvert.getDeterminant());
            try {
            noInvert.createInverse();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }

2. Output::

        NoInvert : AffineTransform[[5.0, 5.0, 0.0], [3.0, 3.0, 0.0]]
        Determinant : 0.0
        java.awt.geom.NoninvertibleTransformException: Determinant is 0.0
            at java.awt.geom.AffineTransform.createInverse(AffineTransform.java:2666)
            at at.linux4all.affine.TestAffineTransform.test(TestAffineTransform.java:164)
            at at.linux4all.affine.TestAffineTransform.main(TestAffineTransform.java:84)

3. Remember, our matrix for world to pixel transformation was::

      [  0.05  0.00 -100.00 ] 
      [  0.00 -0.03  400.00 ] 
      [  0.00  0.00    1.00 ] 

4. The determinant is::

      0.05 *  (-0.03..) - 0 * 0 

5. which is not equal 0 and we can create the inverse matrix.

Conclusion
==========

I hope this tutorial helps to demystify affine transforms, once you are used to working with them
you will never return to doing coordinate calculations "by hand".

Take a look at the Java API of the ``java.awt.geom.AffineTransform`` class to see further
possibilities (rotate, shear,...).

References
==========

* Java ``AffineTransform`` `class javadocs`_. 

.. _class javadocs: http://download.oracle.com/javase/6/docs/api/java/awt/geom/AffineTransform.html

* Wikipedia article on `affine transformation`_.

.. _affine transformation: http://en.wikipedia.org/wiki/Affine_transformation

