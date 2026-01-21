package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerTest {

    private Address getBasicAddress() {
        return Address.builder()
                .street("Bourbon Stree")
                .number("1145")
                .neighborhood("North Ville")
                .city("York")
                .state("South Carolina")
                .zipCode(new ZipCode("12345"))
                .complement("Apt. 114")
                .build();
    }

    private Customer getBasicCustomer() {
        return Customer.brandNew()
                .fullName(new FullName("John", "Doe"))
                .birthDate(new BirthDate(LocalDate.of(1991, 7, 5)))
                .email(new Email("validEmail@email.com"))
                .phone(new Phone("478-256-2504"))
                .document(new Document("255-08-0578"))
                .promotionNotificationsAllowed(false)
                .address(getBasicAddress())
                .build();
    }


    @Test
    void given_invalidEmail_whenTryCreateCustomer_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Customer.brandNew()
                        .fullName(new FullName("John", "Doe"))
                        .birthDate(new BirthDate(LocalDate.of(1991, 7, 5)))
                        .email(new Email("invalid"))
                        .phone(new Phone("478-256-2504"))
                        .document(new Document("255-08-0578"))
                        .promotionNotificationsAllowed(false)
                        .address(getBasicAddress())
                        .build()
                );
    }

    @Test
    void given_invalidEmail_whenTryUpdatedCustomerEmail_shouldGenerateException() {
        Customer customer = getBasicCustomer();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("invalid")));
    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize() {
        Customer customer = getBasicCustomer();

        customer.archive();

        Assertions.assertWith(customer,
                c -> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
                c -> assertThat(c.email()).isNotEqualTo(new Email("john.doe@gmail.com")),
                c -> assertThat(c.phone()).isEqualTo(new Phone("000-000-0000")),
                c -> assertThat(c.document()).isEqualTo(new Document("000-00-0000")),
                c -> assertThat(c.birthDate()).isNull(),
                c -> assertThat(c.promotionNotificationsAllowed()).isFalse(),
                c -> assertThat(c.address()).isEqualTo(Address.builder()
                        .street("Bourbon Stree")
                        .number("Anonymized")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South Carolina")
                        .zipCode(new ZipCode("12345"))
                        .complement(null)
                        .build())
        );

    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException() {
        Customer customer = Customer.existing()
                .id(new CustomerId())
                .fullName(new FullName("Anonymous", "Anonymous"))
                .birthDate(new BirthDate(LocalDate.of(1991, 7, 5)))
                .email(new Email("anonymous@anonymous.com"))
                .phone(new Phone("000-000-0000"))
                .document(new Document("000-00-0000"))
                .promotionNotificationsAllowed(false)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPoints(new LoyaltyPoints(10))
                .address(getBasicAddress())
                .build();


        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("email@gmail.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("123-123-1111")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints() {
        Customer customer = getBasicCustomer();

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException() {
        Customer customer = getBasicCustomer();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));
    }
}