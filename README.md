Prerequisites:
* Maven is needed to build this project
* first deploy at least android 4.3 using maven-android-sdk-deployer
 * https://github.com/mosabua/maven-android-sdk-deployer
 * mvn install -P 4.3
* install m2e eclipse plugin
 * I recommend building by console though
 * Did not test building with eclipse thoroughly but it should work

Building:

* go into the folder fsdroid
* there are two possiblities:
 * compile, unit tests, deploy to device: mvn install
 * compile, unit tests, integration tests, deploy to device: mvn -P it install
 * Both options can be run with -o to force offline mode (mvn -P it -o install)

Decisions:

* Cardsui is integrated by mergin the source code into this application
 * Cardsui is not available via maven
 * Todo: write a script to update the Cardsui code in this application
* Refreshactionitem is not used. 
 * It depends on ActionBarSherlock -> Build time doubles
 * Support library v13 brings ActionBar support
 * RAI without ActionBarSherlock is not available via maven
