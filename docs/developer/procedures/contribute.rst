Contribute
==========

When submitting a patch or :doc:`pull request </procedures/pull_requests>`:

* Small Contribution / Single Source Code File

  For a very small change (less than one file) a committer can review and apply the change on
  your behalf. This is a quick workaround allowing us to correct spelling mistakes in the
  documentation, clarify a javadoc, or accept a very small fix.

  We understand you may have to update several test cases to verify your change fixes its
  intended problem.

* Large Contributions / Multiple Files / New Files
  
  To  contribute a new file, or if your change effects several files, sign a :doc:`contribution_license`.

Procedure:

* :doc:`contribution_license`

Role and Responsibility:

* :doc:`/roles/contributor`

.. admonition:: Jira

   Jira tracks features as well as bugs. By creating an issue the change will be listed in the GeoTools release notes.

Code Contributions
------------------

Regardless of what you want to achieve, there are some common steps to consider:

#. Talk first policy

   Unless you intend to provide a trivial change (fixing typos in the documentation, easy bugfix
   with test) the very first thing you should do is to subscribe to the :doc:`GeoTools developer
   list </communication>` and explain what you're about to do.

   This is a very important step:

   * It lets core developers assess your suggestions and propose alternate/better ways of getting
     the desired results
   * It makes it easier to review the pull request/patch as its content are already known and
     agreed upon

#. Create a local branch::

     git checkout -b fix_featureLock

#. Work on the fix, using commit as needed.
   
   * **Please remember to always include a test case, most pull requests/patches will be rejected if they don't contain one.**.

   * Please make sure you're following the :doc:`coding conventions </conventions/code/style>`,
     and otherwise avoid any reformats to the existing code, as they make it harder to review your
     changes.
     
     If you find sections not following the coding convetions and you want to amend their
     formatting, that's fine, please do so in a separate commit/patch from the real code changes.

#. Review the work that was done, make sure the changes contain all the files you need, and no other extraneous change.::

     git status
   
   In case you're making a pull request, single commit ones are preferred, you can use ``rebase -i`` to squash multiple commits into one, it's fine to have two commits if one is used to isolate code formatting changes

#. Rebase the branch from master so you get a nice clean set of changes::

      git pull --rebase master

#. Do a full maven build (with tests) to make sure your fix compiles cleanly::

      mvn clean install -Dall

   .. admonition:: Don't break the Build
   
      We do have this nice rule about breaking the build (don't).
   
      This means that if you are working on core interfaces you will be running all over the place
      cleaning up modules. One very good reason to talk to the list first is to give other module
      maintainers a chance to get out of the way, or offer to help you clean up the mess.

   .. admonition:: Master First
   
      Changes cannot be accepted directly onto the stable branch, they need to be tried out on master first.
      
      We are able to accept fixes and changes that do not break compatibility onto the stable branch after they have been tested on master.

#. Submit pull request: for instructions on submitting a pull request see `Using Pull Requests <https://help.github.com/articles/using-pull-requests>`_ on GitHub.
  
  Pull requests are reviewed by module maintainers as outlined in :doc:`/procedures/pull_requests`.
  
   .. admonition:: Patch
      
      Pull requests are generally reviewed faster (as we have build infrastructure in place to test
      them). However attaching a patch to a JIRA issue is an alternative:
      
      #. Create the patch::

           git format-patch master > featureLock.patch

      #. Open a JIRA issues against the subsystem in which the patch was made, the subject should
         describe the contribution and ideally mention that a patch is included. Example: `Patch
         for FeatureLock concurrency failure`

      #. JIRA will automatically notify the maintainer of the module (since that is the best person
         to do the code review). If no one answers or comments in the subsequent few days, then the
         contributor can contact the developers' mailing list to let everyone know about the patch
         and find someone else competent to review the code and integrate the contribution into the
         code base or provide a request for improvements to the patch.

Breaking published API or performing major changes on existing modules
----------------------------------------------------------------------

Any change involving a break in existing API (e.g., changing an interface, adding abstract methods
to an abstract class) and any significant change affecting more than one module should go through a
formal proposal, that will be discussed and voted on the developer mailing list.

Procedure:

* :doc:`/procedures/proposal`

Role and Responsibility:

* :doc:`/roles/committer`

New Module
----------

You may be reaching out to GeoTools in order to add a new module to the library. That is fine and appreciated, in this case you should follow two extra steps:

* You need to ask for a new "unsupported" module on the developer mailing list. This is a request for :doc:`/roles/committer` access.
* Since you're certainly adding new files, you'll have to sign a contributor agreement

Procedure:

* :doc:`create`

Role and Responsibility:

* :doc:`/roles/committer`

Supported Module
----------------
   
When ready your module can be included in the normal build for everyone; and you can go through the quality assurance procedures checks to make the module a :doc:`supported` part of GeoTools.

Procedure:

* :doc:`supported`

Role and Responsibility:

* :doc:`/roles/maintainer`

Existing Module
---------------

Be sure to discuss any change with the module maintainer on the developer list before starting work. Module maintainers have volunteered to look after the module and may be aware of other development teams working in this area or know of plans that can effect your work.

You can check the module :file:`pom.xml` to determine the module maintainer.