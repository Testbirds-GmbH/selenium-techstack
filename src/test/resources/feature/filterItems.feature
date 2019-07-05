Feature: Filter Items

  Background:
    Given I am on the TodoMVC page

  Scenario: Can't filter items if no items in list
    Given I have an empty todo list
    Then I will not be able to filter for all items in the todo list
    And I will not be able to filter for not completed items in the todo list
    And I will not be able to filter for completed items in the todo list

  Scenario Outline: Can filter items if items in list
    Given I have a todo list with <numberOfItems> items
    Then I will be able to filter for all items in the todo list
    And I will be able to filter for not completed items in the todo list
    And I will be able to filter for completed items in the todo list

    Examples:
      | numberOfItems |
      | 1             |
      | 2             |
      | 10            |

  Scenario: Filter completed when several items completed
    Given I have a todo list with 5 items
    And 3 items in the todo list are completed
    When I filter the todo list for completed items
    Then there will be exactly 3 items in the todo list
    And all items in the todo list will be displayed as completed

  Scenario: Filter completed when no items completed
    Given I have a todo list with 5 items
    And all items in the todo list are not completed
    When I filter the todo list for completed items
    Then there will be exactly 0 items in the todo list

  Scenario: Filter completed when all items completed
    Given I have a todo list with 5 items
    And all items in the todo list are completed
    When I filter the todo list for completed items
    Then there will be exactly 5 items in the todo list
    And all items in the todo list will be displayed as completed

  Scenario: Filter not completed when several items not completed
    Given I have a todo list with 5 items
    And 3 items in the todo list are completed
    When I filter the todo list for not completed items
    Then there will be exactly 2 items in the todo list
    And all items in the todo list will be displayed as not completed

  Scenario: Filter not completed when no items not completed
    Given I have a todo list with 5 items
    And all items in the todo list are completed
    When I filter the todo list for not completed items
    Then there will be exactly 0 items in the todo list

  Scenario: Filter not completed when all items not completed
    Given I have a todo list with 5 items
    And all items in the todo list are not completed
    When I filter the todo list for not completed items
    Then there will be exactly 5 items in the todo list
    And all items in the todo list will be displayed as not completed
