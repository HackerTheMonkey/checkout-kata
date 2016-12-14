package com.itv.checkoutkata.validation;

import com.itv.checkoutkata.Item;
import com.itv.checkoutkata.PricingRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.itv.checkoutkata.ErrorMessages.MISSING_CHECKOUT_ITEMS;
import static com.itv.checkoutkata.ErrorMessages.MISSING_PRICING_RULES;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CheckoutValidatorTest {

    public static final Map<Item, PricingRule> PRICING_RULES = Collections.singletonMap(new Item("A"), PricingRule.builder().build());
    public static final List<Item> CHECKOUT_ITEMS = Arrays.asList(new Item("A"));

    public static final Map<Item, PricingRule> NON_MATCHING_PRICING_RULES = Collections.singletonMap(new Item("B"), PricingRule.builder().build());

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private CheckoutValidator checkoutValidator;

    @Before
    public void setup() {
        checkoutValidator = new CheckoutValidator();
    }

    @Test
    public void should_create_system_under_test() {
        assertThat(checkoutValidator, is(notNullValue()));
    }

    @Test
    public void given_emptyCheckoutItems_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(containsString(MISSING_CHECKOUT_ITEMS));

        // When
        checkoutValidator.validate(Collections.emptyList(), PRICING_RULES);
    }

    @Test
    public void given_nullCheckoutItems_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(containsString(MISSING_CHECKOUT_ITEMS));

        // When
        checkoutValidator.validate(null, PRICING_RULES);
    }

    @Test
    public void given_emptyPricingRules_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(containsString(MISSING_PRICING_RULES));

        // When
        checkoutValidator.validate(CHECKOUT_ITEMS, Collections.emptyMap());
    }

    @Test
    public void given_nullPricingRules_then_explode(){
        // Then
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(containsString(MISSING_PRICING_RULES));

        // When
        checkoutValidator.validate(CHECKOUT_ITEMS, null);
    }

    @Test
    public void given_validInput_then_noExceptionsAreThrown(){
        checkoutValidator.validate(CHECKOUT_ITEMS, PRICING_RULES);
    }

    @Test
    public void given_pricingRulesNotMatchingEveryCheckoutItems_then_explode(){
        // Then
        expectedException.expect(IllegalStateException.class);

        // When
        checkoutValidator.validateMatchingRulesForEveryItem(CHECKOUT_ITEMS, NON_MATCHING_PRICING_RULES);
    }

    @Test
    public void given_pricingRulesForEveryCheckoutItem_then_noExceptionsAreThrown(){
        checkoutValidator.validateMatchingRulesForEveryItem(CHECKOUT_ITEMS, PRICING_RULES);
    }
}