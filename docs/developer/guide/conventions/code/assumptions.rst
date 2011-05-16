Avoid Assumptions
------------------

A new code has been added or an existing code has been modified, and everything seems to work fine? Then why some reviewers push for what seem useless details? Why not just wait to see if somebody complains?

Because approximative code often work only for some specific cases. It is more difficult to figure out six months later what is going wrong, than to think hard about corners at the time the code is first written. Real examples from geotools code:

Attention to equals and hashCode contract
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The **org.geotools.geometry.GeneralEnvelope** class implements the equals method with a strict comparison: equals returns true only if two envelopes are of the same class and have identical coordinates (e.g. e1.x == e2.x). It may sound like a good idea to relax this rule and compare the coordinates only up to some tolerance error (e.g. abs(e1.x - e2.x) < EPS), since rounding errors in various calculation algorithms may produce slightly different floating point numbers for conceptually identical envelopes.

Actually, it is not a good idea to define the standard equals(Object) method in this relaxed way. First of all, the EPS tolerance level is arbitrary and should be provided explicitly by the user. Second and most important, such relaxation violates the equals and hashCode contract in the Object class. The contract stipulate that if e1.equals(e2) == true, than e1.hashCode() == e2.hashCode(). If the hash code value is computed from the envelope coordinates, then a relaxed equals implementation will wrongly return true even if the hash codes are actually different.

The consequence is a GeneralEnvelope that seems to work conveniently, but a **java.util.HashMap** that work randomly when such envelopes are used as keys. The user may have the feeling that there is a bug in Sun's implementation of HashMap, while actually it is caused by oversight in GeneralEnvelope.equals(Object) implementation.

Attention to String used as keys
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Strings are often used as keys, either in **java.util.Map** or some kind of named object. They are dangerous when used in more than one place without the help of compile time constants.

Examples::
   
   // String Literal's as Keys
   map.put("No data", value);
   
   // Will not work because of the upper-case 'D'
   value = map.get("No Data");
   
   // Will work only in English or unsupported locales!
   value = map.get(Vocabulary.format(VocabularyKeys.NO_DATA));

Consider using a static constants instead::
   
   // Static Constants as Keys
   private static final String NO_DATA = "No data";
   
   map.put(NO_DATA, value);
   value = map.get(NO_DATA);

Note this static constant can still be used for generating human readable messages; it is simply used as a key to look up the appropriate translation.

Use AffineTransform mathematic
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Incorrect Use of Transform::
   
   public void foo(CoordinateReferenceSystem crs, AffineTransform transform) {
       boolean isLongitudeFirst = crs.getCoordinateSystem().getAxis(0)
            .getDirection().absolute().equals(AxisDirection.EAST);
       double scaleX = (isLongitudeFirst) ? transform.getScaleX() : transform.getShearY();
       double scaleX = (isLongitudeFirst) ? transform.getScaleY() : transform.getShearX();
   }

The above code is wrong for two reasons described below:

* Do not make assumption on AffineTransform based on the coordinate reference system
  
  What the isLongitudeFirst line above really try to do is to determine if the affine transform interchanges the x and y axis. But as the name suggests, isLongitudeFirst just said if the first axis is longitude. It implicitly assumes that the axis are exclusively (longitude, latitude) or (latitude, longitude), which is a wrong assumption. Oceanographers will want to display (time, latitude) images for example, in which case the above code will wrongly believes that axis are interchanged.
  
  To be strict, we need at least the sourceCRS and targetCRS in order to determine if some axis were interchanged. In addition, if isLongitudeFirst is used only for selecting affine transform coefficients, it would be much safer to determine the "axis interchange" state from the affine transform rather than from the CRS. Keep in mind that this is a user-supplied affine transform and we have no guarantee that the user built it in the same way that we would. Maybe the user wants an arbitrary rotation? Maybe he uses a (latitude, depth) coordinate system? So if you really want to determine if axis were interchanged, consider using something like transform.getShearX() != 0, or yet better XAffineTransform.getSwapXY(transform) == -1 (a static convenience method that perform more elaborated mathematic than the naive former test).

* Use mathematic invariants when possible (avoid special cases)
  
  In the vast majority of cases, code don't need to determine if axis were interchanged. If a code tries to fetch different affine transform coefficients according some "axis interchanged" state, chances are that the code is just not using proper affine transform mathematic.

  Incorrect Axis Interchange with Modal Code::
     
     public void foo(AffineTransform gridToWorld) {
         double scaleX = Math.abs(swapXY ? gridToWorld.getShearY() : gridToWorld.getScaleX());
         double scaleX = Math.abs(swapXY ? gridToWorld.getShearX() : gridToWorld.getScaleY());
         double xUpperLeft = (swapXY) ? gridToWorld.getTranslateY() : gridToWorld.getTranslateX();
         double yUpperLeft = (swapXY) ? gridToWorld.getTranslateX() : gridToWorld.getTranslateY();
     }
  
  Correct Axis Interchange with Model Code::
     
     public void foo(AffineTransform gridToWorld) {
         AffineTransform worldToGrid = gridToWorld.createInverse();
         double scaleX = 1 / XAffineTransform.getScaleX0(worldToGrid);
         double scaleX = 1 / XAffineTransform.getScaleY0(worldToGrid);
         double xUpperLeft = -worldToGrid.getTranslateX() * scaleX;
         double yUpperLeft = -worldToGrid.getTranslateY() * scaleY;
         // TODO: in case of flipping, there is a sign issue.
         // See XAffineTransform.getFlip(...) javadoc.
     }
  
  Note that swapXY vanished completely in the later code, providing that we work on the right affine transform (worldToGrid rather than gridToWorld in the above example). The XAffineTransform.getScaleX0 method uses an identity that work for any rotation, not just axis swapping (which is a 90° rotation + flip) like the former code.
  
  If you are tempted to fetch different coefficients in an affine transform according some conditions, it is worth to take a paper and a pencil, write down the matrix and see if the equations can be written in some form invariant to rotation, flipping or axis swapping. This is often possible and leads to more robust and generic code.
  
  It may sound like paranoiac, but it is not. Old Geotools code was assuming (longitude,latitude) axis order in all cases, for example through unconditional calls to AffineTransform.getScaleX(). It required a great amount of energy from nice volunter in order to handle the (latitude,longitude) axis order as well. Unfortunatly the initial fix for this axis order issue, based on the "Axis Interchange with Modal Code" approach, has just pushed the problem a little bit further away. The code will fails for the next great Geotools step: 3D-Coverage. Users will want to see 2D slices using a wide range axis that are not longitude or latitude. It is better to make the best possible use of affine transform mathematic early than revisiting again the whole Geotools code base as in the "axis order issue" case.

Prefer MathTransform over GridRange - Envelope pair
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

In some place of GeoTools API, a MathTransform is inferred automatically from a grid range and an envelope. For example the GeneralGridGeometry class provides the following constructors::
   
   public GeneralGridGeometry(GridRange gridRange, Envelope userRange);
   public GeneralGridGeometry(GridRange gridRange, MathTransform gridToCRS, CoordinateReferenceSystem crs);

While the GridRange - Envelope pair seems easier and more intuitive, it is also ambiguous. There is no way to infer a MathTransform from this pair without making some assumptions on axis order and axis reversal. For example GeneralGridGeometry assumes that the y axis must be reversed in order to match the direction used in most screen devices (y values increasing down). Only the constructor with MathTransform argument is unambiguous.

GridRange - Envelope pairs are provided as a convenience for helping users to get their first math transform right in a majority (but not all) cases. From that point, Geotools code should perform all their internal work on MathTransform, never on Envelope. Need to expand an envelope? Compute a scale affine transform and concatenate it with the user math transform. Need to translate, flip or swap axis? Same approach: express your change as an other transform, then concatenate.