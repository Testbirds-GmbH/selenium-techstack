Feature: Edit Items in Todo List

  Background:
    Given I am on the TodoMVC page

  Scenario: Edit active Item
    Given I have a todo list with 3 items
    And the 1st item in the todo list reads "Buy milk"
    And the 2nd item in the todo list reads "Clean my bike"
    And the 3rd item in the todo list reads "Automate everything"
    And all items in the todo list are not completed
    When I change the 1st item in the todo list to read "Call Mum"
    Then there will be exactly 3 items in the todo list
    And the 1st item in the todo list will read "Call Mum"
    And the 2nd item in the todo list will read "Clean my bike"
    And the 3rd item in the todo list will read "Automate everything"

  Scenario: Edit completed Item
    Given I have a todo list with 3 items
    And the 1st item in the todo list reads "Buy milk"
    And the 2nd item in the todo list reads "Clean my bike"
    And the 3rd item in the todo list reads "Automate everything"
    And all items in the todo list are completed
    When I change the 1st item in the todo list to read "Call Mum"
    Then there will be exactly 3 items in the todo list
    And the 1st item in the todo list will read "Call Mum"
    And the 2nd item in the todo list will read "Clean my bike"
    And the 3rd item in the todo list will read "Automate everything"
