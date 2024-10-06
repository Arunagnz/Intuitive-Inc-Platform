package com.intuitveinc.common.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DynamicPricingRequest {
    double percentage;
    boolean overrideDemand;
}
