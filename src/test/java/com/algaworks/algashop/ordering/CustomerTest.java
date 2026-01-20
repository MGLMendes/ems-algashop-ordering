package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CustomerTest {

    @Test
    public void testingCustomer() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setFullName("Miguel Mendes");
        customer.setDocument("1234");
        customer.setLoyaltyPoints(10);
    }

}
