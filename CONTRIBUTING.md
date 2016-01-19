# Contribute to GeoTools

When submitting a patch or pull request:

* **Small Contribution / Single Source Code File:** For a very small change (less than one file) a committer can review and apply the change on your behalf. This is a quick workaround allowing us to correct spelling mistakes in the documentation, clarify a javadoc, or accept a very small fix.
  We understand you may have to update several test cases to verify your change fixes its intended problem.

* **Large Contributions / Multiple Files / New Files:** To  contribute a new file, or if your change effects several files, sign a [Code Contribution License](http://docs.geotools.org/latest/developer/procedures/contribution_license.html). It does not take long and you can send it via email.

For details check the developers guide page on [contributing](http://docs.geotools.org/latest/developer/procedures/contribute.html).

## Code Contributions

Regardless of what you want to achieve, there are some common steps to consider:

1. Talk first policy

   Unless you intend to provide a trivial change (fixing typos in the documentation, easy bugfix with test) the very first thing you should do is to subscribe to the [GeoTools developer list](http://docs.geotools.org/latest/developer/communication.html) and explain what you're about to do.

   This is a very important step:
   * It lets core developers assess your suggestions and propose alternate/better ways of getting
     the desired results
   * It makes it easier to review the pull request/patch as its content are already known and
     agreed upon

2. Create a local branch:
   ````
   git checkout -b fix_featureLock
   ````
3. Work on the fix, using commit as needed.
   * **Please remember to always include a test case, most pull requests/patches will be rejected if they don't contain one.**.
   * Remember to update the copyright header with the year of the modification, e.g., if you are modifying a file whose copyright header states ``(C) 2002-2008, Open Source Geospatial Foundation (OSGeo)`` in January 2016 then update it to say ``(C) 2002-2016, Open Source Geospatial Foundation (OSGeo).``
   * The commit message should refer an existing ticket in Jira, if there is none, create one. The commit message should look like ``[GEOT-XYWZ] Title of the ticket``
   * Please make sure you're following the [coding conventions](http://docs.geotools.org/latest/developer/conventions/code/style.html), and otherwise avoid any reformats to the existing code, as they make it harder to review your changes.
     If you find sections not following the coding convetions and you want to amend their formatting, that's fine, please do so in a separate commit/patch from the real code changes.

4. Review the work that was done, make sure the changes contain all the files you need, and no other extraneous change:
   ````
   git status
   ````
   In case you're making a pull request, single commit ones are preferred, you can use `rebase -i` to squash multiple commits into one, it's fine to have two commits if one is used to isolate code formatting changes

5. Rebase the branch from master so you get a nice clean set of changes:
   ````
   git pull --rebase master
   ````
6. Do a full maven build (with tests) to make sure your fix compiles cleanly:
   ````
   mvn clean install -Dall
   ````
   * **Don't break the Build:** We do have this nice rule about breaking the build (don't). This means that if you are working on core interfaces you will be running all over the place cleaning up modules. One very good reason to talk to the list first is to give other module maintainers a chance to get out of the way, or offer to help you clean up the mess.
   * **Master First:** Changes cannot be accepted directly onto the stable branch, they need to be tried out on master first. We are able to accept fixes and changes that do not break compatibility onto the stable branch after they have been tested on master.

7. Submit pull request: for instructions on submitting a pull request see [Using Pull Requests](https://help.github.com/articles/using-pull-requests) on GitHub.
  
   Pull requests are reviewed by module maintainers as outlined in our developers guide page on [pull requests](http://docs.geotools.org/latest/developer/procedures/pull_requests.html).
