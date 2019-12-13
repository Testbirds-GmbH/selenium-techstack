Feature: Complete Items

  Background:
    Given I am on the TodoMVC page

  Scenario: Can't mark all items completed if no items in list
    Given I have an empty todo list
    Then I will not be able to complete all items in the todo list at once
    And I will not be able to complete any item in the todo list individually

  Scenario Outline: Can mark all items completed if items in list
    Given I have a todo list with <numberOfItems> items
    Then I will be able to complete all items in the todo list at once
    And I will be able to complete all <numberOfItems> items in the todo list individually

    Examples:
      | numberOfItems |
      | 1             |
      | 2             |
      | 10            |

  @smoke
  Scenario: Complete one item
    Given I have a todo list with 5 items
    And all items in the todo list are not completed
    When I complete the 3rd item in the todo list
    Then the checkbox of the 3rd item in the todo list will be checked
    And the text of the 3rd item in the todo list will be crossed out

  Scenario: Complete several items
    Given I have a todo list with 5 items
    And all items in the todo list are not completed
    When I complete the 2nd item in the todo list
    And I complete the 5th item in the todo list
    Then the 2nd item in the todo list will be displayed as completed
    And the 5th item in the todo list will be displayed as completed


  Scenario: Complete all items
    Given I have a todo list with 5 items
    And all items in the todo list are not completed
    When I complete all items in the todo list
    Then all items in the todo list will be displayed as completed
