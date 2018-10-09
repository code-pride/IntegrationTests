# language: en

  @Http
  Feature: Http
    Basic http feature

  @Cors
  Scenario: CorsUserService
    Given The absolute path is "/users/login"
    When The options request is passed
    Then Response code is 200
    Then Response header is "Access-Control-Allow-Headers" and has value "content-type"
    Then Response header is "Access-Control-Allow-Methods" and has value "POST"