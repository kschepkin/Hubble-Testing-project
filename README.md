# testing-project <img src="http://icons.iconarchive.com/icons/carlosjj/google-jfk/128/hubble-icon.png" width="75px;"/> <br>
         


## Getting Started

These instructions will get you a copy of the project up and running on your local machine. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

* install jdk8
* install lombok plugin
* customize lombok plugin (Build, Execution, Deployment --> Compiler --> Annotation Processors, Enable annotation processing)
* install allure 
```
Give examples
```
### Requirements

* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
 `java` and `jar` on the PATH (make sure you use `java` executable from JDK but not JRE)

```
Give an example
```

### Installing

Appium run: <br/>
* first install Xcode
* then install other 
```
> brew install node      # get node.js
> npm install -g appium  # get appium
> npm install wd         # get appium client
> brew install carthage  # get carthage (necessary to install webdriver for ios)
```
Simulator will start automatically after start mobile tests


```
until finished
```
End with an example of getting some data out of the system or using it for a little demo


## Running the tests

Parameters of environment <br /> 

Using gradle add this parameter in gradle arguments:

```
-Penv=dev
```
Using junit add this JVM option:

```
-Dsettings.env=dev
```
## Reporting
Hubble use Allure reports     <img src="https://avatars.githubusercontent.com/u/5879127?v=3" width="30px;"/>

To generate report into  build/reports  dir use Gradle tasks:
```
allureAggregatedReport copyConfig
```
To open allure reports in console type:
```
allure open /YOU_PATH/build/reports/allure-report/
```

copyConfig - gradle task copy allure.properties which contain environment from resources/ENV_DIR <br /> 
to build/reports/allure-results

### Сoding style tests

We used java code conventions <br /> 
https://www.oracle.com/technetwork/java/codeconventions-150003.pdf
https://www.magnumblog.space/java/131-translating-java-code-conventions#file_organization


## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Gradle](https://gradle.org) - Dependency Management
* [Spring Boot](https://spring.io) - application framework to get you Api tests up and running as quickly as possible
* [Junit](https://junit.org/junit5) - test runner
* [Selenide](https://selenide.org) - framework for test automation powered by Selenium WebDriver
* [Appium](https://http://appium.io) - Appium is an open source test automation framework for use with native, hybrid and mobile web apps. 
                                       It drives iOS, Android, and Windows apps using the WebDriver protocol.

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore -->
| [<img src="https://i.ibb.co/6wdHTH7/2018-11-18-21-56-15.png" width="100px;"/><br /><sub><b>Smetanin Kirill</b></sub>](https://github.com/Rocksod)<br />         | [<img src="https://i.ibb.co/nQdjjx8/photo-2017-04-04-18-45-16.jpg" width="100px;"/><br /><sub><b>Schepkin Konstantin</b></sub>](https://gitlab.com/konstantin.schepkin)<br /> | 
| :-----------------------------------------------------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------: |
<!-- ALL-CONTRIBUTORS-LIST:END -->
* **Schepkin Konstantin** - *Liga QA Expert* 
* **Smetanin Kirill** - *Lead QA GJ* 



## Acknowledgments


