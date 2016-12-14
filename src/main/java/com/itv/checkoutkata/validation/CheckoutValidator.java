package com.itv.checkoutkata.validation;

import com.itv.checkoutkata.Item;
import com.itv.checkoutkata.PricingRule;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.List;
import java.util.Map;

import static com.itv.checkoutkata.ErrorMessages.*;

public class CheckoutValidator {

    public void validate(List<Item> checkoutItems, Map<Item, PricingRule> pricingRules) {
        validateForMissingCheckoutItems(checkoutItems);
        validateForMissingPricingRules(pricingRules);
        validateMatchingRulesForEveryItem(checkoutItems, pricingRules);
    }

    private void validateForMissingPricingRules(Map<Item, PricingRule> pricingRules) {
        if (MapUtils.isEmpty(pricingRules)) {
            throw new IllegalArgumentException(MISSING_PRICING_RULES);
        }
    }

    private void validateForMissingCheckoutItems(List<Item> checkoutItems) {
        if (CollectionUtils.isEmpty(checkoutItems)) {
            throw new IllegalArgumentException(MISSING_CHECKOUT_ITEMS);
        }
    }

    public void validateMatchingRulesForEveryItem(List<Item> checkoutItems, Map<Item, PricingRule> pricingRules) {
        boolean weHaveRulesForAllCheckoutItems = checkoutItems.stream().allMatch(item -> pricingRules.containsKey(item));
        if (!weHaveRulesForAllCheckoutItems) {
            throw new IllegalStateException(NO_PRICING_RULES_FOR_CHECKOUT_ITEMS);
        }
    }
}
