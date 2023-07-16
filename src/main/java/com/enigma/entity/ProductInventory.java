package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ToString
@Entity
@Table(name = "product_inventory")
public class ProductInventory {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    @Getter
    @Setter
    private String id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id", referencedColumnName = "price_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Price productUnit;

    @Column(name = "stock", nullable = false , length = 100)
    @Getter @Setter
    private int stock;


//    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(mappedBy = "vendor", fetch = FetchType.EAGER)
//    @JsonBackReference
//    @Setter @Getter
//    private Set<Product> productList;
//
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_code", referencedColumnName = "product_code", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Product product;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", referencedColumnName = "vendor_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Vendor vendor;

}
