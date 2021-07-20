<!--Include a few sentences describing the overall goals for this Pull Request-->
  
<!-- Please help our volunteers reviewing this PR by completing the following items. 
Ask in a comment if you have troubles with any of them. -->

# Checklist

- [ ] I have read the [contribution guidelines](https://github.com/geotools/geotools/blob/main/CONTRIBUTING.md).
- [ ] I have sent a [Contribution Licence Agreement](https://docs.geotools.org/latest/developer/procedures/contribution_license.html) (not required for small changes, e.g., fixing typos in documentation).
- [ ] First PR targets the `main` branch (backports managed later; ignore for branch specific issues).
- [ ] Avoid [Java 9+ split packages](http://tutorials.jenkov.com/java/modules.html#split-packages-not-allowed).
- [ ] All the build checks are green ([see automated QA checks](https://docs.geotools.org/latest/developer/conventions/code/qa.html)).

For core and extension modules:

- [ ] New unit tests have been added covering the changes.
- [ ] [Documentation](https://github.com/geotools/geotools/tree/main/docs) has been updated (if change is visible to end users).
- [ ] There is an issue in [GeoTools Jira](https://osgeo-org.atlassian.net/projects/GEOT) (except for changes not visible to end users). 
- [ ] Commit message(s) must be in the form ``[GEOT-XYZW] Title of the Jira ticket``.
- [ ] Bug fixes and small new features are presented as a single commit.
- [ ] The commit targets a single objective (if multiple focuses cannot be avoided, each one is in its own commit, and has a separate ticket describing it).

<!--Submitting the PR does not require you to check all items, but by the time it gets merged, they should be either satisfied or not applicable.-->
