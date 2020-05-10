$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("Login.feature");
formatter.feature({
  "line": 1,
  "name": "Application Login",
  "description": "",
  "id": "application-login",
  "keyword": "Feature"
});
formatter.scenarioOutline({
  "line": 5,
  "name": "Home page default Login",
  "description": "",
  "id": "application-login;home-page-default-login",
  "type": "scenario_outline",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 4,
      "name": "@regression"
    }
  ]
});
formatter.step({
  "line": 6,
  "name": "User is on Home page",
  "keyword": "Given "
});
formatter.examples({
  "line": 9,
  "name": "",
  "description": "",
  "id": "application-login;home-page-default-login;",
  "rows": [
    {
      "cells": [
        "scenario",
        "Username",
        "Password"
      ],
      "line": 10,
      "id": "application-login;home-page-default-login;;1"
    },
    {
      "cells": [
        "",
        "User1",
        "pass1"
      ],
      "line": 11,
      "id": "application-login;home-page-default-login;;2"
    },
    {
      "cells": [
        "",
        "User3",
        "pass3"
      ],
      "line": 12,
      "id": "application-login;home-page-default-login;;3"
    }
  ],
  "keyword": "Examples"
});
formatter.scenario({
  "line": 11,
  "name": "Home page default Login",
  "description": "",
  "id": "application-login;home-page-default-login;;2",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 4,
      "name": "@regression"
    }
  ]
});
formatter.step({
  "line": 6,
  "name": "User is on Home page",
  "keyword": "Given "
});
formatter.match({
  "location": "RegistrationStepDefinition.user_is_on_home_landing_page()"
});
formatter.result({
  "duration": 60188162500,
  "status": "passed"
});
formatter.scenario({
  "line": 12,
  "name": "Home page default Login",
  "description": "",
  "id": "application-login;home-page-default-login;;3",
  "type": "scenario",
  "keyword": "Scenario Outline",
  "tags": [
    {
      "line": 4,
      "name": "@regression"
    }
  ]
});
formatter.step({
  "line": 6,
  "name": "User is on Home page",
  "keyword": "Given "
});
formatter.match({
  "location": "RegistrationStepDefinition.user_is_on_home_landing_page()"
});
formatter.result({
  "duration": 13025466400,
  "status": "passed"
});
});