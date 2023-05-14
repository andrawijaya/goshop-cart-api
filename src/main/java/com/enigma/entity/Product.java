package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@ToString
@Entity
@Table(name = "m_product")
public class Product {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "product_code")
    @Getter
    @Setter
    private String productCode;

    @Column(name = "product_name", nullable = false, length = 100)
    @Getter @Setter
    private String productName;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", referencedColumnName = "vendor_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Vendor vendor;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id", referencedColumnName = "price_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Price price;



}
