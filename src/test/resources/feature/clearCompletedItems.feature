Feature: Clear Completed Items

  Background:
    Given I am on the TodoMVC page

  Scenario Outline: Can't clear completed items if no completed items in list
    Given I have a todo list with <numberOfItems> items
    And all items in the todo list are not completed
    Then I will not be able to clear all completed items in the todo list

    Examples:
      | numberOfItems |
      | 0             |
      | 1             |
      | 5             |

  Scenario Outline: Can clear completed items if completed items in list
    Given I have a todo list with <numberOfItems> items
    And <numberOfCompletedItems> items in the todo list are completed
    Then I will be able to clear all completed items in the todo list

    Examples:
      | numberOfItems | numberOfCompletedItems |
      | 1             | 1                      |
      | 5             | 1                      |
      | 5             | 3                      |
      | 10            | 10                     |

  Scenario: Clear some items in list
    Given I have a todo list with 5 items
    And 2 items in the todo list are completed
    When I clear all completed items in the todo list
    Then there will be exactly 3 items in the todo list

  Scenario: Clear all items in list
    Given I have a todo list with 5 items
    And 5 items in the todo list are completed
    When I clear all completed items in the todo list
    Then there will be exactly 0 items in the todo list

  Scenario: Clear no items in list
    Given I have a todo list with 5 items
    And 0 items in the todo list are completed
    Then I will not be able to clear all completed items in the todo list
