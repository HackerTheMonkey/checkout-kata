package com.itv.checkoutkata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PricingRuleTest {

    @Test
    public void given_zeroMultiPrice_nonZeroMultiPricedQuantity_then_pricingRuleHasNoMultiPrice(){
        // Given
        PricingRule pricingRule = new PricingRule(new Item("A"), 1.0, 0, 1);

        // Then
        assertThat(pricingRule.hasMultiPrice(), is(false));
    }

    @Test
    public void given_nonZeroMultiPrice_zeroMultiPricedQuantity_then_pricingRuleHasNoMultiPrice(){
        // Given
        PricingRule pricingRule = new PricingRule(new Item("A"), 1.0, 1.0, 0);

        // Then
        assertThat(pricingRule.hasMultiPrice(), is(false));
    }

    @Test
    public void given_nonZeroMultiPrice_nonZeroMultiPricedQuantity_then_pricingRuleHasMultiPrice(){
        // Given
        PricingRule pricingRule = new PricingRule(new Item("A"), 1.0, 1.0, 2);

        // Then
        assertThat(pricingRule.hasMultiPrice(), is(true));
    }

    @Test
    public void given_numberOfItemsLessThanMultiPricedQuantity_then_itemsAreNotEligibleForMultiPricing(){
        // Given
        int multiPricedQuantity = 2;
        int numberOfItems = 1;

        PricingRule pricingRule = new PricingRule(new Item("A"), 1.0, 1.0, multiPricedQuantity);

        // Then
        assertThat(pricingRule.isMultiPriceEligible(numberOfItems), is(false));
    }

    @Test
    public void given_numberOfItemsGreaterThanMultiPricedQuantity_then_itemsAreEligibleForMultiPricing(){
        // Given
        int multiPricedQuantity = 2;
        int numberOfItems = 3;

        PricingRule pricingRule = new PricingRule(new Item("A"), 1.0, 1.0, multiPricedQuantity);

        // Then
        assertThat(pricingRule.isMultiPriceEligible(numberOfItems), is(true));
    }

    @Test
    public void given_numberOfItemsEqualToMultiPricedQuantity_then_itemsAreEligibleForMultiPricing(){
        // Given
        int multiPricedQuantity = 2;
        int numberOfItems = 2;

        PricingRule pricingRule = new PricingRule(new Item("A"), 1.0, 1.0, multiPricedQuantity);

        // Then
        assertThat(pricingRule.isMultiPriceEligible(numberOfItems), is(true));
    }

}