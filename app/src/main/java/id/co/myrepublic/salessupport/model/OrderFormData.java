package id.co.myrepublic.salessupport.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Contain all order field when creating order
 *
 * @author Fahreza Tamara
 */

public class OrderFormData implements Serializable {

    private static final long serialVersionUID = 543543456443554225L;

    private HashMap<String,Object> salesData;
    private HashMap<String,Object> billingAddressData;


    public HashMap<String, Object> getSalesData() {
        return salesData;
    }

    public void setSalesData(HashMap<String, Object> salesData) {
        this.salesData = salesData;
    }

    public HashMap<String, Object> getBillingAddressData() {
        return billingAddressData;
    }

    public void setBillingAddressData(HashMap<String, Object> billingAddressData) {
        this.billingAddressData = billingAddressData;
    }
}
