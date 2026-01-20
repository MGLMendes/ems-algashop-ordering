package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

class CustomerTest {

    @Test
    public void testingCustomer() {
        Customer customer = new Customer(
                UUID.randomUUID(),
                "Jhon Due",
                LocalDate.of(1991, 7, 5),
                "jhon.due@email.com",
                "592-259-2503",
                "255-05-0259",
                true,
                OffsetDateTime.now()
        );

        customer.addLoyaltyPoints(10);
        customer.changeEmail("jhon.due@gmail.com");

    }

}
