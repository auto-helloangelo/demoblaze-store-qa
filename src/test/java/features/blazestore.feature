@web
  Feature: DEMOBLAZE STORE ONLINE SHOP

    Background:
      Given URL is loaded in to browser
      And Customer registers himself

      Scenario: Verify customer is able to place order
        When Customer logins to online store
        And Customer navigates to Laptops
        And Customer select product Sony vaio i5
        Then Customer adds to cart
        And Customer navigates to Home
        And Customer navigates to Laptops
        And Customer select product Dell i7 8gb
        Then Customer adds to cart
        And Customer navigates to Cart
        And Customer removes product Sony vaio i5 from cart
        Then Customer place order and fill form details
        And Customer Verify the amount on order confirmation
        Then Customer clicks on OK button