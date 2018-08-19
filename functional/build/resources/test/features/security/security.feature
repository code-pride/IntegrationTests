# language: en
Feature: Security
  Security test

  @important
  Scenario: Login
    Given I have csrf cookie and populate is done
    And The base path is "login"
    And The login request payload is set
    When The post request is passed
    Then Response code is 200