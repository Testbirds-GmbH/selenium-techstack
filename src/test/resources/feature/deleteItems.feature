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

  Scenario: Items move up when Item is deleted
    Given I have a todo list with 4 items
    And the 1st item in the todo list reads "Buy milk"
    And the 2nd item in the todo list reads "Clean my bike"
    And the 3rd item in the todo list reads "Automate everything"
    And the 4th item in the todo list reads "Call Mum"
    When I delete the 2nd item from the todo list
    Then there will be exactly 3 items in the todo list
    And the 1st item in the todo list will read "Buy milk"
    And the 2nd item in the todo list will read "Automate everything"
    And the 3rd item in the todo list will read "Call Mum"

  Scenario: Delete completed Item
    Given I have a todo list with 3 items
    And all items in the todo list are completed
    When I delete 1 item from the todo list
    Then there will be exactly 2 items in the todo list
