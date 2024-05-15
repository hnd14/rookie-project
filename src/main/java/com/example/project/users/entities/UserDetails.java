package com.example.project.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class UserDetails {
    @Column(name = "phone_number", table = "user_details")
    String phoneNumber;

    @Column(name = "billing_address", table = "user_details")
    String billingAddress;

    @Column(name = "shipping_address", table = "user_details")
    String shippingAddress;
}
