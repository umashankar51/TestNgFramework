# Sample Framework with TestNg

### environment.properties
Test Properties can be set on this file - environment.properties. Default behaviour can be overriden by using below properties
##### included_groups
##### excluded_groups
##### headless
##### env

### qa.properties
Test Environment properties can set using qa.properties or dev.properties
### qaData.json
Test Data for Environment can set using - qaData.json, we can create one for each environment
### resource/templates
Json templates can be placed inside - resource/templates
### image files
Images can set up - resource/images
### testng xml config
Testng XML configurations - testng.xml, set thread count, parallel execution method, add testng test classes etc., 

### Test execution 
Test execution can be triggered using for TestRunner.java
##### Update environment.properties file, navigate to TestRunner.java, right click and click 'Run TestRunner'

or with below maven command

##### mvn clean test -Denv=qa -Dincluded_groups='uiTests' -Dexcluded_groups = 'broken' -Dheadless=true

Default values from environment.properties file will be taken if any of the configurations are not specified on the maven command