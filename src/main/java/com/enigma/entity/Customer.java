package com.enigma.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@ToString
public class Customer {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "customer_id")
    @Getter @Setter
    private String customerId;

    @Column(name = "first_name", nullable = false , length = 100)
    @Getter @Setter
    private String firstName;

    @Column(name = "last_name", nullable = false , length = 100)
    @Getter @Setter
    private String lastName;

    @Column(name = "phone_number", nullable = false , length = 100)
    @Getter @Setter
    private String phoneNumber;

    @Column(name = "address", nullable = false , length = 100)
    @Getter @Setter
    private String address;
}
