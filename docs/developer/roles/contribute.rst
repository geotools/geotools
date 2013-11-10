Contributors
============

Initially, newcomers to the project generally participate in an informal role as a contributor. These types of contributors have no long term responsibility to the project.

Contributions take several forms:

* Small Contribution / Single File
  
  We are happy to accept quick informal patches to an existing file as a GitHub pull request (preferred) or a patch attached to a JIRA task (or sent to the mailing list as a last resort).

* Large Contributions / New Files
   
  To  contribute a new file, or if your change effects several files, sign a :doc:`/procedures/contribution_license`.
   
  Patches submitted to JIRA for large contributions should include the contributor name in the list
  of authors of the class documentation for any file in which the contributor has made significant
  changes. That is the contributor's name should be added using the @author javadoc tag.

Code Contributions
------------------

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
  
  Pull requests are reviewed by module maintainers as outlined in :doc:`/procedures/pull_requests`.

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
