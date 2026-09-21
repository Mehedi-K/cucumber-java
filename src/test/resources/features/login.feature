Feature: Login
  As a saucedemo customer
  I want to log in with my credentials
  So that I can reach the products page and start shopping

  Background:
    Given I am on the saucedemo login page

  Scenario: Successful login with valid credentials
    When I log in as "standard_user" with password "secret_sauce"
    Then I should be redirected to the products page

  Scenario: Login fails for a locked out user
    When I log in as "locked_out_user" with password "secret_sauce"
    Then I should see the error message "Epic sadface: Sorry, this user has been locked out."

  Scenario: Submitting the login form with empty fields shows a validation error
    When I attempt to log in without entering a username or password
    Then I should see the error message "Epic sadface: Username is required"

  Scenario Outline: Login attempts with multiple credential combinations
    When I log in as "<username>" with password "<password>"
    Then the login outcome should be "<outcome>"

    Examples:
      | username         | password       | outcome    |
      | standard_user     | secret_sauce   | success    |
      | problem_user      | secret_sauce   | success    |
      | locked_out_user   | secret_sauce   | locked_out |
      | standard_user     | wrong_password | invalid    |
      | invalid_user      | secret_sauce   | invalid    |
