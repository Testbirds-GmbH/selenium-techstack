Feature: Active Items Count

  Background:
    Given I am on the TodoMVC page

  Scenario: Don't see count of items if no items in list
    Given I have an empty todo list
    Then I will not see a count of all not completed items in the todo list

  Scenario Outline: See count of items if items in list
    Given I have a todo list with <numberOfItems> items
    Then I will see a count of all not completed items in the todo list

    Examples:
      | numberOfItems |
      | 1             |
      | 2             |
      | 10            |

  Scenario: Some items are active
    Given I have a todo list with 5 items
    And 2 items in the todo list are completed
    Then the number of not completed items in the todo list will be 3

  Scenario: All items are active
    Given I have a todo list with 5 items
    And 0 items in the todo list are completed
    Then the number of not completed items in the todo list will be 5

  Scenario: No items are active
    Given I have a todo list with 5 items
    And 5 items in the todo list are completed
    Then the number of not completed items in the todo list will be 0
