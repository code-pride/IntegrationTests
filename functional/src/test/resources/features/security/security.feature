# language: en

@Security
Feature: Security
  Security test

  @UserService
  Scenario: Login
    Given I have csrf cookie and populate is done
    And The base path is "login"
    And The login request payload is set
    When The post request is passed
    Then Response code is 200
    Then Response cooke is "_secu"
  
  @LoggedAsUser @UserService
  Scenario: Logout
    Given The base path is "logout"
    When The post request is passed
    Then Response code is 200
    Given The absolute path is "api/getUserProfile"
    And The payload is clean
    When The get request is passed
    Then Response code is 403

  @Api
  Scenario: UnsecuredRequestApi
    Given I have csrf cookie and populate is done
    And The base path is "getUserProfile"
    When The post request is passed
    Then Response code is 403

  @UserService
  Scenario: UnsecuredRequestLogout
    Given I have csrf cookie and populate is done
    And The base path is "logout"
    When The post request is passed
    Then Response code is 403

  @UserService
  Scenario: CsrfValidationLogin
    Given I have empty request
    And The base path is "login"
    When The get request is passed
    Then Response code is 403

  @UserService
  Scenario: CsrfValidationLogout
    Given I have empty request
    And The base path is "logout"
    When The get request is passed
    Then Response code is 403

  @Api
  Scenario: CsrfValidationApi
    Given I have empty request
    And The base path is "getUserProfile"
    When The get request is passed
    Then Response code is 403