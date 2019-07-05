# Selenium Techstack

With this project we've created an exemplary comprehensive technology stack for website testing based on Selenium.
Our goal was to demonstrate how easy it is to construct a proper tech stack that supports all related process steps, from test case creation, over execution, to reporting, just using open source resources.
This tech stack has been first presented at the [German Testing Day 2019](https://www.germantestingday.info/german-testing-day-2019/programm/konferenzprogramm/sessiondetails/action/detail/session/gtd-35-2/title/ein-vollstaendiger-selenium-techstack-von-der-konzeption-bis-zur-auswertung-mit-open-source-softwar.html) and may be used as starting point or inspiration for other (in particular your own) testing projects.

Authors:
* [Robert Bossek](https://github.com/robasek)
* [Felix Kuperjans](https://github.com/Felix-Kuperjans)

## Components of the Selenium Techstack

This Techstack is designed for Selenium Webtests with Java 8 and Selenium 3.

Main tools:
* Test design: [Cucumber (v4.3.1)](https://cucumber.io/)
* Selenium Framework: [Selenide (v5.2.3)](https://selenide.org/)
* Test execution: [Jenkins Pipelines](https://jenkins.io/)
* Test reports: [Reportportal.io](https://reportportal.io/)

Further tools:
* [Apache Maven](https://maven.apache.org/)
* [JUnit 4](https://junit.org/junit4/)
* [SLF4J](https://www.slf4j.org/)
* [Logback Classic](https://logback.qos.ch/)
* [Janino for Logback](https://logback.qos.ch/setup.html#janino)

## Application under Test

We test the [TodoMVC](http://todomvc.com/) application which is a simple todo list written in different MVC Javascript frameworks. This suite has been developed with the [ReactJS variant](http://todomvc.com/examples/react/) of TodoMVC.

To host the application locally, you can follow the steps on <https://github.com/tastejs/todomvc/tree/master/examples/react>.

## How to run

There are three ways to run this test:
* Via Maven's verify goal
* As JUnit tests in an IDE
* Via Jenkins Pipelines (which call Maven verify)

All of them have in common to configure the build using Selenide's and Report Portal's system properties. The Report Portal integration is automatically disabled if the required property for authentication (the Report Portal UUID) is not set, so Report Portal is optional.

### Selenide properties

Selenide is a feature-rich testing framework, it allows you to adjust the conditions of your test run using numerous predefined runtime properties. You can find an extensive list of available properties in the [official framework documentation](https://selenide.org/javadoc/current/com/codeborne/selenide/Configuration.html). If you want to change these properties, you can simply set the respective parameter according to the documentation.

Here is a short list of frequently used properties:
* `selenide.browser` - specifies which browser to use for the test execution; supported values: "chrome", "firefox", "legacy_firefox", "ie", "htmlunit", "phantomjs", "opera", "safari", "edge", "jbrowser"; default value: "chrome"
* `selenide.baseUrl` - defines the base URL for the `open()` function, which will be used for relative URL calls
* `selenide.remote` - can be used to run on a remote WebDriver or in a Selenium Grid

Some systems or browsers require special runtime adjustments, otherwise they are hardly performant. InternetExplorer for example is very slow at entering inputs when using the Selenium built-in `sendKeys` function. Selenide offers therefore an option to pass inputs via JavaScript by setting `selenide.fastSetValue` to true. Also, Edge is currently not really well supported out of the box. This is why we recommend to use Chrome or Firefox for your local test runs and focus on other browsers later.

Please do not confuse the Selenide properties with the Selenium's proprietary desired capabilities. Those can be passed as is usual on WebDriver instantiation or you can put them into the `capabilities.json` located in the root folder of this project. All capabilities specified here, will be automatically passed to the WebDriver instance when created.

#### Using a Selenium Grid

By default, Selenide uses the local browser, but it is also possible to use a Selenium Grid or Cloud provider.
If the `selenide.remote` property is set, Selenide will use a Selenium Grid with this URL.

For example, you could start a Selenium Hub on the local host and then reference it with `selenide.remote=http://localhost:4444/wd/hub`.

Cloud providers can be used, too.
For example, you can use the [Testbirds Device Cloud](https://www.testbirds.com/device-cloud/device-cloud-overview/) with these steps:
1. Register an account for the free trial or use your existing account
2. [Setup the Testbirds Device Cloud Proxy locally](https://www.testbirds.com/device-cloud/use-cases-support/documentation-device-cloud/browser-testing-device-cloud-proxy-java-selenium/)
3. Set `selenide.remote=http://localhost:4444/wd/hub` and optionally extra capabilities in capabilities.json (e.g. `resolution=1920x1080` is provided as an example)

### Setup Report Portal

You can use the [public demo](https://web.demo.reportportal.io/) with the default user for testing.
Alternatively, you can setup your own instance using docker-compose following the [Report Portal installation steps](https://reportportal.io/docs/Installation-steps) and take care of [setting up Elasticsearch properly](https://reportportal.io/docs/Issues-with-Analyzer:).

To setup the Report Portal, you need three things:
* rp.endpoint: the endpoint of the REST service to push data
* rp.uuid: a UUID to authenticate to Report Portal
* rp.project: a project to push the builds to (can be *username*_personal or a project created by an administrator)

Optionally, properties like rp.tags can be used to provide data like which browser was used to the Report Portal.
A full list of properties is available on <https://github.com/reportportal/agent-java-cucumber#parameters>.

For the public demo, these steps are needed:
* login using the prefilled default user credentials
* click on the user profile in the top right corner's user menu
* in the profile, copy the displayed UUID

Note that the public demo server deletes all data and regenerates the UUID every 12 hours.

### Run tests using Maven

Prerequisites:
* Java 8 (OpenJDK or other)
* Maven 3.5

Inside the project's main directory (where the pom.xml and this README is located), a `failsafe.properties` file is used to pass the required configuration properties for Selenide and Report Portal (if used).

Afterwards, just invoke the **verify** goal by running `mvn verify` to execute the integration tests.

#### Example `failsafe.properties`

```
selenide.baseUrl=http://todomvc.com/examples/react/
selenide.browser=chrome
;selenide.remote=http://localhost:4444/wd/hub

rp.endpoint=https://web.demo.reportportal.io/
rp.uuid=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
rp.project=default_personal
rp.tags=chrome;maven
```

Some notes:
* `selenide.remote` is commented; it can be used to utilize a Selenium Grid instead of the local browser
* `rp.uuid` must be set to the UUID which was copied from the Report Portal user profile or commented to not use Report Portal at all
* `rp.project` uses the personal project of the demo instance's default user - you need to adjust it if you use a different user
* `rp.tags` contains some example tags for this build

### Run tests from an IDE

This Howto will use the [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/packages/), since it is fully Open Source, but other IDEs can be used with similar steps.

Setup IDE and project:
1. Download and install the [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/packages/) if you don't have it already
2. Go into your workspace and use `File -> Import...` to import a project
3. Use the option `Check out existing Maven Projects from SCM` in the Maven folder and press Next
4. Enter the URL from GitHub's `Clone or Download` button (should be <https://github.com/Testbirds-GmbH/selenium-techstack.git>) and finish the Wizard
5. Eclipse will now detect the project structure and download the required Maven dependencies, which may take a while

Optionally, the following plugins from the Eclipse Marketplace (`Help -> Eclipse Marketplace...`) can be useful but are not strictly needed:
* **Natural** to support Cucumber files and the Gherkin syntax
* **Eclipse Wild Web Developer** to improve support for Json and HTML

To run the tests, you could either use the IDE's Maven integration to run the verify goal after creating a failsafe.properties (see above, [Run tests using Maven](#run-tests-using-maven)) or use the JUnit integration which will also report the results in your IDE.

To run the tests with JUnit integration, select the `CucumberIT.java` file from `src/test/java` in the package `com.testbirds.selenium.techstack`, right click it and select `Run As -> JUnit Test`.
The first run will use all Selenide defaults, so it will run on Chrome locally using http://localhost:8080/ as base URL, which will likely fail, because TodoMVC is not available on localhost:8080.
To change the properties, go to `Run As -> Run configurations...` in the right click menu of the same file.
It will show the run configuration CucumberIT in the JUnit folder, which can be adjusted on the `Arguments` tab in the text box `VM arguments`:
* Note: the arguments have to be separated by a single space and must not contain spaces. You can append as many options as you need.
* Append `-Dselenide.baseUrl=http://todomvc.com/examples/react/` to the VM arguments. Now the test should run if you have Chrome installed.
* You can add any other Selenide or Report Portal properties (see [Selenide properties](#selenide-properties) and [Setup Report Portal](#setup-report-portal) above) by prefixing it with `-D` and no space after the D.

Below is a complete example of VM arguments using Firefox and the Report Portal public demo.
To do this, Firefox must be installed and the rp.uuid must be replaced with the real one, see [Setup Report Portal](#setup-report-portal).

```
-ea -Dselenide.baseUrl=http://todomvc.com/examples/react/ -Dselenide.browser=firefox -Drp.endpoint=https://web.demo.reportportal.io/ -Drp.uuid=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx -Drp.project=default_personal -Drp.tags=firefox;eclipse
```

You can see the results in the JUnit tab on the left (by default) and if you configured it, also in Report Portal.

### Run tests with Jenkins pipelines

We will soon provide some examples on how to setup the Jenkins and its pipelines for this project, too.

## Architecture and how to use

Similar to the famous model-view-controller pattern for user interfaces, we divided our architecture into three main layers:
* **Features:** the human-readable, stepwise specification of the actual test case written in Gherkin syntax
* **Step Implementation:** the programmatic and executable realization of each test step in code
* **Model:** the conceptual representation of the test object in the form of implemented page objects

Our feature files do not have any direct link to the model as they intend to be an abstract outline of the desired product functionality. The actual implementation of the product could change completely, but the feature file describing its functionality would not be affected at all as long as the specification is satisfied.
The model in turn reflects nothing but the actual implementation of the product. If the implementation changes, the model has to change, too.
The step implementation serves as connecting piece between feature description and implementation, that's why it is also sometimes called the _glue code_.

All features are located under `src > resources > feature`, for now there is no further subdivision since the TodoMVC app has a quite narrow set of functions.
In a more feature-rich product one would certainly creat a more elaborate structure based on cohesiveness and functionality of the system units.

Each feature consists of a feature description, a background that gets executed before every test case in the feature file, and one or more scenarios or scenario outlines.
A scenario (outline) always exhibits a threefold structure induced by the keywords `Given`, `When`, and `Then`.
The `Given` part initializes the test case, it is responsible for establishing the preconditions of the test.
`When` addresses the actual subject matter of the test, it focuses on the actual action that should be tested.
The `Then` part in turn verifies that executing the action has actually brought about the expected outcome.
The `And` keyword is just a simple connector. It doesn't introduce a new logical segment, but only joins several steps that belong to the same one.

```gherkin
Feature: Delete Items from Todo List

  Background:
    Given I am on the TodoMVC page

  Scenario: Delete last item from Todo List
    Given I have a todo list with 1 item
    And the 1st item in the todo list reads "Buy milk"
    When I delete "Buy milk" from the todo list
    Then there will be exactly 0 items in the todo list

  Scenario Outline: Delete several Items from Todo List
    Given I have a todo list with <numberOfItems> items
    When I delete <numberOfDeletedItems> items from the todo list
    Then there will be exactly <numberOfRemainingItems> items in the todo list

    Examples:
      | numberOfItems | numberOfDeletedItems | numberOfRemainingItems |
      | 2             | 1                    | 1                      |
      | 10            | 9                    | 1                      |
      | 10            | 5                    | 5                      |
```

If you like to add a step to the scenario (outline) you can simply add one from the implemented steps in the java package `com.testbirds.selenium.techstack.step` or implement one on your own.
A test step consists of the step annotation and the actual method that gets executed by integrating this test step into an scenario (outline).
The step annotation is human-readable phrase written as regular expression that is matched with the corresponding text in the scenario.
It is parameterizable, i.e. parts of the step annotation can be generic and interpreted by the test method.
This way it is possible to affect the actual execution of the test step on runtime.

```java
package com.testbirds.selenium.techstack.step.fragment;

public class TodoListSteps extends AbstractFragmentSteps<TodoList> {

    @Given("^the (\\d+)(?:st|nd|rd|th) item in the todo list reads \"([^\"]+)\"$")
    public void updateListItemTitle(int position, String text) {
        todoListManipulationService.editItem(position, (item) -> {
            item.setTitle(text);
        });
        getFragment().getItemByText(text).getLabelElement().shouldBe(Condition.visible);
    }
}
```

The arrangement of the step classes is guided by the structure of the corresponding model classes.
The model classes reflect the actual implementation of the product. It has been split into page objects, fragment objects, and DTOs.
Page objects and fragment objects form the outline of the actual user interface, the website under test.
A page object serves as representation of the page. In our context we only use them as containers for the components which it consists of.
Each component is reflected by a fragment object. It is the actual element-wise illustration of the component as code.

```java
package com.testbirds.selenium.techstack.model.fragment;

public class TodoList implements Fragment {

    public Item getItemByText(String text) {
        return new Item(getListOfTodoElements().findBy(Condition.text(text)));
    }
}
```

All immediate interactions with the website under test are implemented in the page and fragment objects.
If the implementation of the website changes, the interactions with the modified or deleted elements have to be adapted in the related page/fragment object.
The step methods in turn assemble these interactions into blocks of functional cohesiveness by putting them into logical sequence.
This allows the test case author, to create scenarios just by putting together abstract descriptions of factual features.
