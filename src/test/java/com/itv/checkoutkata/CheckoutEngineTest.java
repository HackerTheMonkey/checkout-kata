package com.itv.checkoutkata;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;

import static com.itv.checkoutkata.CheckoutTestData.*;
import static com.itv.checkoutkata.CheckoutTestData.multiPricedQuantity;
import static com.itv.checkoutkata.ErrorMessages.MISSING_CHECKOUT_ITEMS;
import static com.itv.checkoutkata.ErrorMessages.MISSING_PRICING_RULES;
import static com.itv.checkoutkata.ErrorMessages.NO_PRICING_RULES_FOR_CHECKOUT_ITEMS;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckoutEngineTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private CheckoutTestData checkoutTestData;

    private CheckoutEngine checkoutEngine;

    @Before
    public void setup() {
        checkoutEngine = new CheckoutEngine();
    }

    @Test
    public void should_create_system_under_test() {
        assertThat(checkoutEngine, is(notNullValue()));
    }

    @Test
    public void given_itemsToCheckedOutAreNull_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(MISSING_CHECKOUT_ITEMS);

        // When
        checkoutEngine.checkout(null, Maps.newHashMap());
    }

    @Test
    public void given_itemsToCheckedOutAreEmpty_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(MISSING_CHECKOUT_ITEMS);

        // When
        checkoutEngine.checkout(Collections.emptyList(), Maps.newHashMap());
    }

    @Test
    public void given_nullPricingRules_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(MISSING_PRICING_RULES);

        // When
        checkoutEngine.checkout(asList(new Item("Foo")), null);
    }

    @Test
    public void given_emptyPricingRules_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(MISSING_PRICING_RULES);

        // When
        checkoutEngine.checkout(asList(new Item("Foo")), Maps.newHashMap());
    }

    @Test
    public void given_singleItemToCheckout_when_noMatchingPriceRuleFound_then_explode(){
        // Then
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(containsString(NO_PRICING_RULES_FOR_CHECKOUT_ITEMS));

        // Given
        checkoutData()
                .apple(quantity(1), unitPrice(1.0))
                .withoutAssociatedPricingRule();

        // When
        checkoutEngine.checkout(checkoutTestData.getCheckoutItems(), checkoutTestData.getPricingRules());
    }

    @Test
    public void given_singleItemToCheckout_when_matchingPriceRuleFound_withNoMultiPrice_then_applyUnitPrice(){
        // Given
        checkoutData()
                .apple(quantity(1), unitPrice(1.0));

        // When
        double totalPrice = checkoutEngine.checkout(checkoutTestData.getCheckoutItems(), checkoutTestData.getPricingRules());

        // Then
        assertThat(totalPrice, is(1.0));
    }

    @Test
    public void given_multipleSimilarItemsToCheckout_when_matchingPriceRuleFound_withNoMultiPrice_then_applyUnitPrice(){
        // Given
        checkoutData()
                .apple(quantity(2), unitPrice(1.0));

        // When
        double totalPrice = checkoutEngine
                .checkout(checkoutTestData.getCheckoutItems(), checkoutTestData.getPricingRules());

        // Then
        assertThat(totalPrice, is(2.0));
    }

    @Test
    public void given_multipleSimilarItemsToCheckout_when_itemsAreMultiPricedForExactQuantity_then_applyMultiPrice(){
        // Given
        checkoutData()
                .apple(quantity(2), unitPrice(2.0), multiPrice(1.5), multiPricedQuantity(2));

        // When
        double totalPrice = checkoutEngine
                .checkout(checkoutTestData.getCheckoutItems(), checkoutTestData.getPricingRules());

        // Then
        assertThat(totalPrice, is(1.5));
    }

    @Test
    public void given_multipleSimilarItemsToCheckout_when_itemsAreMultiPricedForLessQuantity_then_applyUnitPrice(){
        // Given
        checkoutData()
                .apple(quantity(2), unitPrice(2.0), multiPrice(1.5), multiPricedQuantity(3));

        // When
        double totalPrice = checkoutEngine
                .checkout(checkoutTestData.getCheckoutItems(), checkoutTestData.getPricingRules());

        // Then
        assertThat(totalPrice, is(4.0));
    }

    @Test
    public void given_multipleSimilarItemsToCheckout_when_itemsAreMultiPricedForMoreQuantity_then_applyUnitPriceAndMultiPriceAccordingly(){
        // Given
        checkoutData()
                .apple(quantity(3), unitPrice(2.0), multiPrice(3.0), multiPricedQuantity(2));

        // When
        double totalPrice = checkoutEngine
                .checkout(checkoutTestData.getCheckoutItems(), checkoutTestData.getPricingRules());

        // Then
        assertThat(totalPrice, is(5.0));
    }

    @Test
    public void given_multipleCheckoutItems_then_totalPriceIsCalculatedAsExpected(){
        // Given
        checkoutData()
                .apple(quantity(3), unitPrice(2.0), multiPrice(3.0), multiPricedQuantity(2)) // 5.00
                .banana(quantity(2), unitPrice(1.0), multiPrice(0.0), multiPricedQuantity(0)) // 2.00
                .pear(quantity(10), unitPrice(0.5), multiPrice(1.2), multiPricedQuantity(3)); // 4.1


        // When
        double totalPrice = checkoutEngine
                .checkout(checkoutTestData.getCheckoutItems(), checkoutTestData.getPricingRules());

        // Then
        assertThat(totalPrice, is(11.1));
    }


    private CheckoutTestData checkoutData(){
        checkoutTestData = new CheckoutTestData();
        return checkoutTestData;
    }

}