# language: en

@Basic @LoggedAsUser
Feature: Basic
  Basic API request

  Scenario: UserDetails
    Given The base path is "getUserProfile"
    When The get request is passed
    Then Response code is 200