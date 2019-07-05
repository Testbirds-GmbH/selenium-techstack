Feature: Reactivate Completed Items

  Background:
    Given I am on the TodoMVC page

  Scenario: Reactivate one item
    Given I have a todo list with 5 items
    And all items in the todo list are completed
    When I reactivate the 3rd item in the todo list
    Then the checkbox of the 3rd item in the todo list will not be checked
    And the text of the 3rd item in the todo list will not be crossed out

  Scenario: Reactivate several items
    Given I have a todo list with 5 items
    And all items in the todo list are completed
    When I reactivate the 2nd item in the todo list
    And I reactivate the 5th item in the todo list
    Then the 2nd item in the todo list will be displayed as not completed
    And the 5th item in the todo list will be displayed as not completed


  Scenario: Complete all items
    Given I have a todo list with 5 items
    And all items in the todo list are completed
    When I reactivate all items in the todo list
    Then all items in the todo list will be displayed as not completed
