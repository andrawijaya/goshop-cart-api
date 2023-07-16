package com.enigma.entity;

import com.enigma.util.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ToString
@Setter @Getter
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "m_auth")
public class UserCredential {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @ToString.Exclude
    @JsonBackReference
    @OneToOne(mappedBy = "userCredential")
    private Customer customer;

}
