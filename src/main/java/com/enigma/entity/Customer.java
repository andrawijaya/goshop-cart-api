package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

@ToString
@Setter @Getter
@Entity
@Table(name = "m_customer")
public class Customer {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "first_name", nullable = false , length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false , length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 100)
    private String phoneNumber;

    @Column(name = "address", length = 100)
    private String address;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Order> orders;

//    @OneToOne(mappedBy = "customer")
//    private UserCredential userCredential;
//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "user_id")
//    private UserCredential userCredential;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_id", referencedColumnName = "email")
    @JsonBackReference
    private UserCredential userCredential;

}
