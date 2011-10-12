GeoTools user list posting guide
================================

Regardless of whether you are just starting out with GeoTools or have been using it for years, we
encourage you to subscribe to the `user list
<https://lists.sourceforge.net/lists/listinfo/geotools-gt2-users>`_ and take an active part in the
community. To help make the user list as useful and friendly as possible we ask you to observe the
following code of behaviour:

Be polite 
---------

Messages to the user list are not moderated so whatever is in your post when you hit the send button
will be seen by all.
   
Be clear and informative
------------------------

Avoid posting messages such as *My app doesn't work !!!! HELP ME !!!!*. Messages like this are
likely to be ignored (especially if typed in capitals).
   
Be patient
----------

Remember that everyone who answers questions on the list does so as a volunteer, usually in their
own time. Depending on the work and life schedules of the GeoTools developers and other list
members, it might not be possible to respond to your question immediately or even at all. It could
also be that no one on the list knows the answer. If your project requires immediate, guaranteed
assistance you should investigate the :ref:`commercial support <commercial-support>` options
available for GeoTools.

Be realistic
------------

Remember that the list is here to help but not to write your code for you. Sometimes list members
will post code snippets or even extended examples in response to questions, but you should not
expect that routinely. If you need someone to help you write GeoTools-related code, consider
:ref:`commercial support <commercial-support>`.

Check first
-----------

Before posting a question, check to see if it is already answered in the following:

   - `User Guide <http://docs.geotools.org/latest/userguide/>`_.

   - `FAQ <http://docs.geotools.org/latest/userguide/faq.html>`_.

   - `Tutorials <http://docs.geotools.org/latest/userguide/tutorial/>`_.

   - `User list archives <http://osgeo-org.1803224.n2.nabble.com/geotools-gt2-users-f1936685.html>`_.

Post enough (but not too much)
------------------------------

When posting about a problem with your application or a possible bug in GeoTools, be sure to include:

   - The version of GeoTools that you are using.

   - Your operating system.

   - Whether you are using Maven to build your application, or are using some other method (e.g. Ant
     with manual download of the GeoTools jars).

   - The full error trace if applicable.

   - A **small, self-contained** code sample that either reproduces the problem or illustrates the
     issue that you are posting about. The code sample should be suitable for other list members to
     copy and paste into their IDEs without editing. Please don't post huge chunks of code, or code
     samples that can't be compiled in isolation.

.. Tip::
   If you using a SNAPSHOT version of GeoTools you can call the static
   **GeoTools.getAboutInfo()** method to get summary information about your system and GeoTools
   installation which you can include in your posting to the list. You can also use the
   **AboutDialog** class in the gt-swing module to get this information.

Share your solutions
--------------------

If you have posted to the list seeking an answer to some GeoTools programming problem, and then
discover the answer yourself, please take the time to share it with the list. This makes your answer
available to others searching the list arhives later. For bonus points, share the answers to
questions you didn't post as well, as in: "I've just worked out a much better way of doing XYZ with
GeoTools".

Take a little, give a little
----------------------------

The user list is there to help you but it works best if you help it too. Give back to the GeoTools
project and community by answering questions or pointing people towards other useful sources of
information when you can. We will like you a lot.

Further information
-------------------

We recommend `How to ask questions the smart way <http://catb.org/~esr/faqs/smart-questions.html>`_
for more tips on how to get the most out of your user list membership.

