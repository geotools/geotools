Contributors
============

Anyone can contribute to the GeoTools project by editing the web site, by writing documentation, by answering questions on the email list, or by contributing code directly into the project.

Initially, newcomers to the project generally participate in an informal role. These types of contributors have no long term responsibility to the project.

Easy informal code contributions
--------------------------------

Informal participants can contribute small modifications to the project code by submitting a contributon (as a pull request or attachment to a JIRA task).

* pull-request

  For instructions on submitting a pull request see `Using Pull
  Requests <https://help.github.com/articles/using-pull-requests>`_ on GitHub.

  **Please remember to include a test case**.

* patch

  #. Create a local branch::

       git checkout -b fix_featureLock

  #. Work on the fix, using commit as needed.

     **Please remember to include a test case**.

  #. Review the work that was done::

       git log --pretty=oneline -3

  #. Rebase the branch from master so you get a nice clean set of changes::

       git pull --rebase master

  #. Do a full maven build (with tests) to make sure your fix compiles cleanly::

       mvn clean install -Dall

  #. Create the patch::

       git format-patch master > featureLock.patch

  #. Open a JIRA issues against the subsystem in which the patch was made, the subject should
     describe the contribution and ideally mention that a patch is included. Example: `Patch
     for FeatureLock concurrency failure`

  #. JIRA will automatically notify the maintainer of the module (since that is the best person to
     do the code review). If no one answers or comments in the subsequent few days, then the
     contributor can contact the developers' mailing list to let everyone know about the patch and
     find someone else competent to review the code and integrate the contribution into the code
     base or provide a request for improvements to the patch.

Large contributions
-------------------

Informal participants can also submit larger contributions following essentially the same process as that just described for small code contributions but also including the formal transfer of the copyright over the contribution to the Open Source Geospatial Foundation (OSGeo).

Patches submitted to JIRA for large contributions should include the contributor name in the list of authors of the class documentation for any file in which the contributor has made significant changes. That is the contributor's name should be added using the @author javadoc tag.

Code Contribution License
---------------------------

GeoTools has adopted a formal policy as part of the process of joining the Open Source Geospatial Foundation (OSGeo).

All new contributors will be required to grant copyright to the foundation using a `Contributor Licenses <http://www.osgeo.org/content/foundation/legal/licenses.html>`_:

* :download:`individual_contributor.txt </artifacts/individual_contributor.txt>`
* :download:`corporate_contributor.txt </artifacts/corporate_contributor.txt>`

These licenses are directly derived from the Apache code contribution licenses (CLA V2.0 and CCLA v r190612).

#. Contributors must print out a copy of the Contribution License document(s) and either sign it themselves or have their employer sign the document, depending on the circumstances governing when and where the Contributor develops the code. It is up to the Contributor to understand the legal status of the code which the Contributor produces.
#. Scan the document and email to **info@osgeo.org**. Once we have confirmation the document has
   been recieved we are in position to review and accept your work.
#. Any questions should be addressed to the developers' mailing list.

Signing a Contributor License is intended to serve several purposes such as shielding the contributor from a direct legal attack by users of the code, enabling the Foundation to represent the interests of the GeoTools project in a legal forum, and enabling the GeoTools project to switch licenses when necessary.