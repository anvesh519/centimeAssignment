Feature: New User Registration


  @Registration
  Scenario Outline: New Registration to e-Cart
    Given scenario as <scenario>
    And user is on Home page
    And an Email Id to create Account
    And select the gender <gender>
    And enter firstName <firstName>, lastName <lastName>, password <password>
    And select the DOB based on number of years <years>
    And signup for newsletter and specialoffers
    And enter address fields address1, address2, city, state <state>, postalCode and country <country>
    And enter either home or mobile number <isMobile>
    And click on register
    Then verify account created successfully

  @postive_scenarios
    Examples:
      | scenario                                  | firstName | lastName  | password  | gender | years | country       | state   | isMobile |
      | Registration_With_All_Mandatory_Fileds    | mandatory | mandatory | mandatory | male   |       | United States | Alabama | true     |
      | Registration_With_Optional_Fileds_gender1 | mandatory | mandatory | mandatory | male   | 10    | United States | Alabama | true     |
      | Registration_With_Optional_Fileds_gender2 | mandatory | mandatory | mandatory | female | 10    | United States | Alabama | true     |

  @negative_scenarios
    Examples:
      | scenario                                | firstName | lastName  | password  | gender | years | country       | state   | isMobile |
      | Registration_Without_firstname_Negative |           | mandatory | mandatory | male   |       | United States | Alabama | true     |

