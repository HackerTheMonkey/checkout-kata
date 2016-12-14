package com.itv.checkoutkata;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PricingRule {


    private final Item item;

    private final double unitPrice;
    private final double multiPrice;

    private final int multiPricedQuantity;


    public boolean hasMultiPrice(){
        return multiPrice > 0.0 && multiPricedQuantity > 1;
    }


    public boolean isMultiPriceEligible(int numberOfItems){
        return numberOfItems >= multiPricedQuantity;
    }

}
