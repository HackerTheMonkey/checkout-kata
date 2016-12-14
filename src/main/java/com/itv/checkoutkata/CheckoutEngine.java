package com.itv.checkoutkata;

import com.google.common.collect.Sets;
import com.itv.checkoutkata.validation.CheckoutValidator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CheckoutEngine {

    private CheckoutValidator checkoutValidator = new CheckoutValidator();

    public double checkout(List<Item> checkoutItems, Map<Item, PricingRule> pricingRules) {

        checkoutValidator.validate(checkoutItems, pricingRules);

        Map<String, List<Item>> groupedCheckoutItems = checkoutItems
                .stream()
                .collect(Collectors.groupingBy(Item::getName));

        Set<PricingRule> applicablePricingRules = Sets.newHashSet(checkoutItems)
                .stream()
                .map(item -> pricingRules.get(item))
                .collect(Collectors.toSet());

        double unitPricedItemsSubTotal = applicablePricingRules
                .stream()
                .filter(unitPriceIsApplicable(groupedCheckoutItems))
                .map(unitPriceBasedCalculation(groupedCheckoutItems))
                .mapToDouble(Double::new).sum();

        double multiPricedItemsSubTotal = applicablePricingRules
                .stream()
                .filter(multiPriceIsApplicable(groupedCheckoutItems))
                .map(multiPriceBasedCalculation())
                .mapToDouble(Double::new).sum();

        double mixPricedItemsSubTotal = applicablePricingRules
                .stream()
                .filter(mixPricesAreApplicable(groupedCheckoutItems))
                .map(mixPriceBasedCalculation(groupedCheckoutItems))
                .mapToDouble(Double::new).sum();

        return unitPricedItemsSubTotal + multiPricedItemsSubTotal + mixPricedItemsSubTotal;
    }

    private Function<PricingRule, Double> mixPriceBasedCalculation(Map<String, List<Item>> groupedItems) {
        return pricingRule -> {

            double unitPrice = pricingRule.getUnitPrice();
            double multiPrice = pricingRule.getMultiPrice();

            int numberOfItemsToCheckout = groupedItems.get(pricingRule.getItem().getName()).size();
            int multiPricedQuantityLimit = pricingRule.getMultiPricedQuantity();

            int numberOfTimesToApplyMultiPrice = numberOfItemsToCheckout / multiPricedQuantityLimit;
            int numberOfTimesToApplyUnitPrice = numberOfItemsToCheckout - numberOfTimesToApplyMultiPrice * multiPricedQuantityLimit;

            return numberOfTimesToApplyMultiPrice * multiPrice + numberOfTimesToApplyUnitPrice * unitPrice;
        };
    }

    private Function<PricingRule, Double> multiPriceBasedCalculation() {
        return pricingRule -> pricingRule.getMultiPrice();
    }

    private Function<PricingRule, Double> unitPriceBasedCalculation(Map<String, List<Item>> groupedItems) {
        return pricingRule -> {

            double unitPrice = pricingRule.getUnitPrice();
            int numberOfItemsToCheckout = groupedItems.get(pricingRule.getItem().getName()).size();

            return unitPrice * numberOfItemsToCheckout;
        };
    }

    private Predicate<PricingRule> mixPricesAreApplicable(Map<String, List<Item>> groupedItems) {
        return pricingRule -> {

            boolean isMultiPriced = pricingRule.hasMultiPrice();
            int numberOfItemsToCheckout = groupedItems.get(pricingRule.getItem().getName()).size();
            int multiPricedQuantity = pricingRule.getMultiPricedQuantity();

            return isMultiPriced && numberOfItemsToCheckout > multiPricedQuantity;
        };
    }

    private Predicate<PricingRule> multiPriceIsApplicable(Map<String, List<Item>> groupedItems) {
        return pricingRule -> {
            int numberOfItemsToCheckout = groupedItems.get(pricingRule.getItem().getName()).size();
            return numberOfItemsToCheckout == pricingRule.getMultiPricedQuantity();
        };
    }

    private Predicate<PricingRule> unitPriceIsApplicable(Map<String, List<Item>> groupedItems) {
        return pricingRule -> {

            boolean isNotMultiPriced = !pricingRule.hasMultiPrice();
            int numberOfItemsToCheckout = groupedItems.get(pricingRule.getItem().getName()).size();

            boolean numberOfItemsAreNotMultiPriceEligible = !pricingRule.isMultiPriceEligible(numberOfItemsToCheckout);

            return isNotMultiPriced || numberOfItemsAreNotMultiPriceEligible;
        };
    }


}
