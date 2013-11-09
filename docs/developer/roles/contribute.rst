Contributors
============

Anyone can contribute to the GeoTools project by editing the web site, by writing documentation, by answering questions on the email list, or by contributing code directly into the project.

Initially, newcomers to the project generally participate in an informal role. These types of contributors have no long term responsibility to the project.

Talk first policy
-----------------

Unless you intend to provide a trivial change (fixing typos in the documentation, easy bugfix with test) the very first thing you
should do is to subscribe to the `GeoTools developer list <http://docs.geotools.org/latest/developer/communication.html#geotools-devel>`_ and
explain what you're about to do. This is a very important step:
  
  * It lets core developers assess your suggestions and propose alternate/better ways of getting the desired results
  * It makes it easier to review the pull request/patch as its content are already known and agreed upon

Easy informal code contributions
--------------------------------

We are happy to accept quick informal patches to existing files. To contribute a new file, please sign a code contribution agreement.

Informal participants can contribute small modifications to the project as a GitHub pull request (preferred) or a patch attached to a JIRA task (or sent to the mailing list as a last resort).
Regardless of what you want to achieve, there are some common steps to consider:

* Create a local branch::

   git checkout -b fix_featureLock

* Work on the fix, using commit as needed. **Please remember to always include a test case, most pull requests/patches will be rejected if they don't contain one.**.

* Please make sure you're following the `coding conventions <http://docs.geotools.org/latest/developer/conventions/code/style.html>`_, and otherwise avoid any reformats to the existing code, as they make it harder to review your changes. If you find sections not following the coding convetions and you want to amend their formatting, that's fine, please do so in a separate commit/patch from the real code changes.

* Review the work that was done, make sure the changes contain all the files you need, and no other extraneous change (in case you're making a pull request, single commit ones are preferred, you can use ``rebase -i`` to squash multiple commits into one, it's fine to have two commits if one is used to isolate code formatting changes)::

   git status

* Rebase the branch from master so you get a nice clean set of changes::

   git pull --rebase master

* Do a full maven build (with tests) to make sure your fix compiles cleanly::

   mvn clean install -Dall

The next step will depend on your preference between pull request and patch. Mind, pull requests are normally 
processed faster as the review is easier and we have discussion tools, but a patch is ok as well.

* Pull-request: for instructions on submitting a pull request see `Using Pull Requests <https://help.github.com/articles/using-pull-requests>`_ on GitHub.

* Patch:


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

Adding a new module
-------------------

You may be reaching out to GeoTools in order to add a new module to the library. That is fine and appreciated, in this case you should follow two extra steps:

* You need to ask for a new community module on the developer mailing list
* Since you're certainly adding new files, you'll have to sign a contributor agreement (see the "Large contributions" section below).

See the `Creating your own module page <http://docs.geotools.org/latest/developer/procedures/create.html>`_ for more information.

Breaking published API or performing major changes on existing modules
----------------------------------------------------------------------

Any change involving a break in existing API (e.g., changing an interface, adding abstract methods to an abstract class) and
any significant change affecting more than one module should go through a formal proposal, that will be discussed and voted
on the developer mailing list.

Please consult the "proposal procedure page <http://docs.geotools.org/latest/developer/procedures/proposal.html>`_ for more details.


Large contributions (new files)
-------------------------------

Informal participants can also submit larger contributions following essentially the same process as that just described for small code contributions but also including the formal transfer of the copyright over the contribution to the Open Source Geospatial Foundation (OSGeo). A contribution is considered to be "large" if it adds at least one new file to the repository.

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
   been received we are in position to review and accept your work.
#. Any questions should be addressed to the developers' mailing list.

Signing a Contributor License is intended to serve several purposes such as shielding the contributor from a direct legal attack by users of the code, enabling the Foundation to represent the interests of the GeoTools project in a legal forum, and enabling the GeoTools project to switch licenses when necessary.
