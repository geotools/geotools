<Include a few sentences describing the overall goals for this Pull Request>

# Checklist

Reviewing is a process done by project maintainers, **mostly on a volunteer basis** (thus limited in time). We need to keep the review overhead as small as possible, and appreciate if you help us to do so by completing the following items. Feel free to ask in a comment if you have troubles with any of them.

For all pull requests:

- [ ] Confirm you have read the [contribution guidelines](https://github.com/geotools/geotools/blob/main/CONTRIBUTING.md)
- [ ] You have sent a Contribution Licence Agreement (CLA) as necessary (not required for small changes, e.g., fixing typos in documentation)
- [ ] Make sure the first PR targets the `main` branch, eventual backports will be managed later. This can be ignored if the PR is fixing an issue that only happens in a specific branch, but not in newer ones.
- [ ] The changes are not causing two modules to share the same Java packages (to avoid [Java 9+ split package](http://tutorials.jenkov.com/java/modules.html#split-packages-not-allowed) issues)
- [ ] The changes are not breaking the build in downstream projects using SNAPSHOT dependencies, GeoWebCache and GeoServer (there is an automatic PR check verifying this, check this when it turns green).

The following are required only for core and extension modules (they are welcomed, but not required, for unsupported modules):

- [ ] There is an issue in [Jira](https://osgeo-org.atlassian.net/projects/GEOT) describing the bug/task/new feature (a notable exemptions is, changes not visible to end users). The ticket is for the GeoTools project, if the issue was found elsewhere it's a good practice to link to the origin ticket/issue.
- [ ] The pull request contains changes related to a single objective. If multiple focuses cannot be avoided, each one is in its own commit and has a separate ticket describing it.
- [ ] PR for bug fixes and small new features are presented as a single commit
- [ ] Commit message(s) must be in the form "[GEOT-XYZW] Title of the Jira ticket"
- [ ] New unit tests have been added covering the changes
- [ ] This PR passes all existing unit tests (test results will be reported by Continuous Integration after opening this PR)
- [ ] This PR passes the [QA checks](https://docs.geotools.org/latest/developer/conventions/code/qa.html) (QA checks results will be reported by Continuous Integration after opening this PR)
- [ ] Documentation has been updated accordingly.

Submitting the PR does not require you to check all items, but by the time it gets merged, they should be either satisfied or not applicable.
