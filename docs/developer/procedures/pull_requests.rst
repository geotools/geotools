Pull Requests
=============

Pull Requests via GitHub are the preferred method of contributing code changes to GeoTools.

Pull requests, like patches, are reviewed as a contribution to the codebase. See :doc:`contribute` for more information.

.. admonition:: Talk first policy

   Unless you intend to provide a trivial change (fixing typos in the documentation, easy bugfix
   with test) the very first thing you should do is to subscribe to the :doc:`GeoTools developer
   list </communication>` and explain what you're about to do.

   This is a very important step:

   * It lets core developers assess your suggestions and propose alternate/better ways of getting
     the desired results
   * It makes it easier to review the pull request/patch as its content are already known and
     agreed upon
     
Create Pull Request
-------------------

A pull request notifies the GeoTools community of some code changes focused around an issue. It allows the relevant Module Maintainer and others to review the set of changes, discuss issues and suggest fixes before the pull request can be merged into the official repository.

.. image:: /images/create-pull-request.png
   :align: center
   :width: 80%

When submitting your pull request:

* Note any associated JIRA number
* Link to any change proposal related to the pull request
* Include a test case
* If the change less than a file in size the reviewer can commit it on your behalf, over a file in size you will need to sign a :doc:`contribution license <contribution_license>`

.. note:: Jira is used as the project issue tracker. You can include a link to a pull request
   in JIRA as an alternative to a patch.

   A pull request is evaluated using the same standards as a patch, code formatting, test case, and
   example documentation for any API change.
   
Pull Request Discussion
-----------------------

There maybe reasons why a pull request may not be merged quickly; requiring a unit test is just one example.

To progress the pull request, a conversation with the contributor concerning the issues around the pull request is required. Any discussion
regarding the pull request should be confined to its thread established by GitHub. There may be issues raised that are larger than
the pull request; take these to mailing lists as appropriate.

.. note:: Discuss Feedback

   * You can also update a pull request in response to discussion, by pushing a commit to the same
     feature branch you made the pull request from.

   * You may also be asked to rebase your pull request (and resolve merge conflicts) if the
     GeoTools library has changed since the pull request was first issued.

If the feedback concerning the pull request have been addressed it can be merged.

.. image:: /images/discuss-pull-request.png
   :align: center
   :width: 80%

If the contributor or other individuals are not willing to address the feedback, then a
decision needs to be made regarding the pull request's future. The module maintainer is in the best position to make this assessment.

Avoiding Merge Problems
^^^^^^^^^^^^^^^^^^^^^^^

While any issues with the pull request may be resolved given the time; it exposes another problem
that becomes more likely with time: merge conflicts. The code in the repository changes overtime
which may conflict with the new code in the pull request.

While possible; trying to reconcile differences between files is likely to introduce errors. GitHub 
assists with merging pull request by flagging merge conflicts and disabling the merge option. It is
always possible to use Git via the command line to merge the pull request. Avoiding merge conflicts is always preferable.

.. note:: Avoid Merge Conflicts

.. image:: /images/merge-conflict.png
   :align: center
   :width: 80%
   
Accepting a Pull Request 
------------------------

The merging of pull requests is ultimately a responsibility of module maintainers, and to a lesser
extent members of the PMC. Do not merge pull requests if you are not responsible for the code. For
other, act responsibility and collaboratively.

.. image:: /images/automatically-merged.png
   :align: center
   :width: 80%

If the pull request's contributor has followed the guidelines for GeoTools and has written good quality code, then it is likely that the pull request will be merged quickly by the module maintainer.

Keeping a Pull Request Open
---------------------------

If there are legitimate reasons for the pull request to remain open they need to be recorded. An
example would be a pending feature freeze and a delay will allow GeoTools and upstream
projects to deal with ramifications of the change post the freeze.

If no firm decision can be made then leave the pull request open; time may be our friend.

.. note:: Record Decisions

Closing a Pull Request
----------------------

Even if there are no merge conflicts not all pull requests can be accepted by the community.

The simple solution to avoid long standing pull requests, and avoiding merge conflicts; is to simply close the pull request - unmerged. If issues have been discussed and decisions recorded; and there is no impending resolution to the pull request then it is better to close the pull request.

Record the reason why the pull request is being closed unmerged. It is possible to reopen an unmerged pull request at a latter date, resolve merge conflicts and associated issues, and then merge the pull request.

.. image:: /images/close-pull-request.png
   :align: center
   :width: 80%
   
While this is a simple solution there are number of checks before closing an unmerged pull request that will help everyone involved:

* Check with the module maintainer, if your not it, or a member of the PMC; that it is fine to
  close the pull request. The module maintainer has the responsibility for maintaining the code and
  needs to kept informed of changes. The module maintainer has every right to say no.
* Ensure that an associated JIRA issue has been created, create one if not so. Document the pull
  request in the JIRA issue and visa versa.
* Be clear with the reason for closing the pull request. We don't want to create animosity with
  contributors. Provide links to documentation and guidelines where appropriate. Be helpful.
* Document the reason in the associated JIRA issue. Leave the JIRA issue open with a resolution of
  incomplete or unresolved.
* Document the reason in the pull request and close it. It is possible to reopen a pull request,
  resolve issues and merge it latter.



Long Standing Pull Requests
^^^^^^^^^^^^^^^^^^^^^^^^^^^

There are some questions raised for a long standing pull request.

* What is the status for an old pull request on the master branch when a new stable branch is created? Is it left as it is, moved to the new stable branch, duplicated to the new stable branch or closed unmerged?
* Despite being no problems with the merging of a pull request; the issue maybe resolved by the GeoTools community in other ways. Merging the pull request would only add cruft to the code base.

Long standing pull requests need to be handled appropriately so that everyone benefits. Contributors may learn how to better
collaborate with GeoTools. Module maintainers may have to incorporate new features of a problem. Ultimaltely, pull requests are tied to the code
base; and, if not handled with care they may lead to problems creating unnecessary work. While the risk of harm is
small, the reputation of GeoTools relies upon the integrity of it's code base and the good work of contributors.

The closing of long standing pull requests provides a barrier; protecting the code base. Also, GitHub lacks the comprehensive
search facilities of the like of JIRA. The creation of an associated JIRA issue allows the search facilities of JIRA to
be leveraged when looking for information concerning an issue. JIRA has a long history of changes made to the GeoTools
project, an invaluable resource. Give it as much information possible for it to be used to the best of its abilities.

In the history of a long standing pull request, there are ample opportunities to talk about issues, document decisions and improve
the pull request. These are important facets of working collaboratively in GeoTools. Try to be helpful and keep others informed
when handling a long standing pull request.

(Images on this page are Copyrighted |copy| 2013, GitHub, Inc. Used with permission)

.. |copy| unicode:: 0xA9