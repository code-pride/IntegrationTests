# language: en

  @Security
Feature: Security
  Security test

  Scenario: Login
    Given I have csrf cookie and populate is done
    And The base path is "login"
    And The login request payload is set
    When The post request is passed
    Then Response code is 200
    Then Response cooke is "_secu"
  
  @LoggedAsUser
  Scenario: Logout
    Given The base path is "logout"
    When The post request is passed
    Then Response code is 200
    Given The base path is "getUserProfile"
    And The payload is clean
    When The get request is passed
    Then Response code is 403
    
  Scenario: UnsecuredRequestApi
    Given I have csrf cookie and populate is done
    And The base path is "getUserProfile"
    When The post request is passed
    Then Response code is 403

  Scenario: UnsecuredRequestLogout
    Given I have csrf cookie and populate is done
    And The base path is "logout"
    When The post request is passed
    Then Response code is 403

  Scenario: CsrfValidationLogin
    Given I have empty request
    And The base path is "login"
    When The get request is passed
    Then Response code is 403

  Scenario: CsrfValidationLogout
    Given I have empty request
    And The base path is "logout"
    When The get request is passed
    Then Response code is 403

  Scenario: CsrfValidationApi
    Given I have empty request
    And The base path is "getUserProfile"
    When The get request is passed
    Then Response code is 403