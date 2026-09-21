Feature: Product Sorting
  As a logged in shopper
  I want to sort the products listing
  So that I can find items by name or price in the order I prefer

  Background:
    Given I am logged in as a standard user

  Scenario: Sort products by name, A to Z
    When I sort products by "Name (A to Z)"
    Then the products should be listed in ascending alphabetical order

  Scenario: Sort products by name, Z to A
    When I sort products by "Name (Z to A)"
    Then the products should be listed in descending alphabetical order

  Scenario: Sort products by price, low to high
    When I sort products by "Price (low to high)"
    Then the product prices should be listed in ascending order

  Scenario: Sort products by price, high to low
    When I sort products by "Price (high to low)"
    Then the product prices should be listed in descending order
