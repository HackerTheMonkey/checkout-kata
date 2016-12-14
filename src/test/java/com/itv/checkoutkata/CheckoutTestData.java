package com.itv.checkoutkata;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// So that we can keep our tests cleaner, less cluttered and more expressive.
@Data
public class CheckoutTestData {

    /**
     * TODO integrate_AMTRAK_encoder
     * tidy up
     */

    private List<Item> checkoutItems = new ArrayList<>();
    private Map<Item, PricingRule> pricingRules = Maps.newHashMap();

    public CheckoutTestData apple(int quantity, double unitPrice, double multiPrice, int multiPricedQuantity) {
        return getCheckoutTestData(quantity, unitPrice, multiPrice, multiPricedQuantity, "Apple");
    }

    public CheckoutTestData apple(int quantity, double unitPrice){
        return getCheckoutTestData(quantity, unitPrice, 0, 0, "Apple");
    }

    public CheckoutTestData banana(int quantity, double unitPrice, double multiPrice, int multiPricedQuantity){
        return getCheckoutTestData(quantity, unitPrice, multiPrice, multiPricedQuantity, "Banana");
    }

    public CheckoutTestData pear(int quantity, double unitPrice, double multiPrice, int multiPricedQuantity){
        return getCheckoutTestData(quantity, unitPrice, multiPrice, multiPricedQuantity, "Pears");
    }

    private CheckoutTestData getCheckoutTestData(int quantity, double unitPrice, double multiPrice, int multiPriceQuantity, String name) {
        Item apple = new Item(name);

        for(int i = 0 ; i < quantity ; i++){
            checkoutItems.add(apple);
        }

        pricingRules.put(apple, PricingRule.builder().item(apple).unitPrice(unitPrice).multiPrice(multiPrice).multiPricedQuantity(multiPriceQuantity).build());

        return this;
    }


    // some syntactic sugar
    public static double unitPrice(double price){
        return price;
    }

    public static double multiPrice(double price){
        return price;
    }

    public static int quantity(int quantity){
        return quantity;
    }

    public static int multiPricedQuantity(int quantity){
        return quantity;
    }

    public void withoutAssociatedPricingRule() {
        pricingRules.clear();
        Item whatever = new Item("Whatever");
        pricingRules.put(whatever, PricingRule.builder().item(whatever).build());
    }
}
