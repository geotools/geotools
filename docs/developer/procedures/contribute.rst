Contribute
==========

When submitting a patch or :doc:`pull request </procedures/pull_requests>`:

* Small: Fix to one existing file, reviewer has your permission to apply the change
* Large: Greater than a file, :doc:`contribution_license` is required

For details check the :doc:`contributor </roles/contribute>` role and responsibility page.

.. admonition:: Talk first policy

   Unless you intend to provide a trivial change (fixing typos in the documentation, easy bugfix
   with test) the very first thing you should do is to subscribe to the :doc:`GeoTools developer
   list </communication>` and explain what you're about to do.

   This is a very important step:

   * It lets core developers assess your suggestions and propose alternate/better ways of getting
     the desired results
   * It makes it easier to review the pull request/patch as its content are already known and
     agreed upon
   
.. admonition:: Jira

   Jira tracks features as well as bugs. By creating an issue the change will be listed in the GeoTools release notes.

.. admonition:: Master First
   
   Changes cannot be accepted directly onto the stable branch, and should be applied to master first.
   
   We are able to accept fixes and changes that do not break compatibility onto the stable branch after they have been tested on master.

Breaking published API or performing major changes on existing modules
----------------------------------------------------------------------

Any change involving a break in existing API (e.g., changing an interface, adding abstract methods
to an abstract class) and any significant change affecting more than one module should go through a
formal proposal, that will be discussed and voted on the developer mailing list.

Please consult the :doc:`proposal procedure page </procedures/proposal>` procedure for more details.

.. admonition:: Don't break the Build
   
   We do have this nice rule about breaking the build (don't).
   
   This means that if you are working on core interfaces you will be running all over the place
   cleaning up modules. One very good reason to talk to the list first is to give other module
   maintainers a chance to get out of the way, or offer to help you clean up the mess.

Adding a New Module
-------------------

You may be reaching out to GeoTools in order to add a new module to the library. That is fine and appreciated, in this case you should follow two extra steps:

* You need to ask for a new "unsupported" module on the developer mailing list. This is a request for :doc:`/roles/commit` access.
* Since you're certainly adding new files, you'll have to sign a contributor agreement

See the :doc:`create` procedure for more information.

.. admonition:: Supported Modules
   
   When ready your module can be included in the normal build for everyone; and you can go through the quality assurance procedures checks to make the module a :doc:`supported` part of GeoTools.

Work on existing Module
-----------------------

Be sure to discuss any change with the module maintainer on the developer list before starting work. Module maintainers have volunteered to look after the module and may be aware of other development teams working in this area or know of plans that can effect your work.

You can check the module :file:`pom.xml` to determine the module maintainer.