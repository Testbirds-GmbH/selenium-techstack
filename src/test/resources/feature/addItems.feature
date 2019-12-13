Feature: Add Items to Todo List

  Background:
    Given I am on the TodoMVC page

  Scenario Outline: Add item always possible
    Given I have a todo list with <numberOfItems> items
    Then I will be able to add new items to the todo list

    Examples:
      | numberOfItems |
      | 0             |
      | 1             |
      | 10            |

  Scenario: Add one Item to empty Todo List
    Given I have an empty todo list
    When I add "buy milk" to the todo list
    Then there will be "buy milk" in the todo list
    And there will be exactly 1 item in the todo list

  Scenario Outline: Add items to non-empty Todo List
    Given I have a todo list with <numberOfItems> items
    When I add <numberOfAdditionalItems> more items to the todo list
    Then there will be exactly <finalNumberOfItems> items in the todo list

    Examples:
      | numberOfItems | numberOfAdditionalItems | finalNumberOfItems |
      | 1             | 1                       | 2                  |
      | 1             | 9                       | 10                 |
      | 5             | 5                       | 10                 |

  @smoke
  Scenario: New Items are added at End of List
    Given I have a todo list with 2 items
    And the 1st item in the todo list reads "buy milk"
    And the 2nd item in the todo list reads "clean my bike"
    When I add "automate everything" to the todo list
    Then there will be exactly 3 items in the todo list
    And there will be "automate everything" in the todo list
    And "automate everything" will be the 3rd item in the todo list

  Scenario Outline: All new Items are active
    Given I have an empty todo list
    When I add <numberOfItems> items to the todo list
    Then there will be exactly <numberOfItems> items in the todo list
    And all items in the todo list will be displayed as not completed

    Examples:
      | numberOfItems |
      | 1             |
      | 2             |
      | 5             |
