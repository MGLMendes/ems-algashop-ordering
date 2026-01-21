package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Customer(
                            IdGenerator.generatedTimeBasedUUID(),
                            "Jhon Due",
                            LocalDate.of(2000, 3, 21),
                            "invalid",
                            "458-254-5122",
                            "135-04-2042",
                            false,
                            OffsetDateTime.now());
                });
    }

    @Test
    void given_invalidEmail_whenTryUpdateCustomer_shouldGenerateException() {
        Customer customer = new Customer(
                IdGenerator.generatedTimeBasedUUID(),
                "Jhon Due",
                LocalDate.of(2000, 3, 21),
                "valid@email.com",
                "458-254-5122",
                "135-04-2042",
                false,
                OffsetDateTime.now());
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    customer.changeEmail("invalid");
                });
    }
}