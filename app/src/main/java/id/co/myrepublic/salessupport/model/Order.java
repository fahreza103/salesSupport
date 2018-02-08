package id.co.myrepublic.salessupport.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Model from create order response
 */

public class Order implements Serializable {

    private static final long serialVersionUID = 1114134321877914553L;

    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("subscription_id")
    private String subscriptionId;
    @JsonProperty("order_id")
    private String orderId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
