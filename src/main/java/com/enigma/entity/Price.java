package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "m_price")
public class Price {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "price_id")
    @Getter
    @Setter
    private String priceId;

    @Column(name = "product_price")
    @Getter @Setter
    private int productPrice;

//    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(mappedBy = "price", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonBackReference
//    @Setter @Getter
//    private List<Vendor> vendorList;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "vendorPrice")
    @JsonBackReference
    @Setter @Getter
    private List<Product> productList;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "productUnit", fetch = FetchType.EAGER)
    @JsonBackReference
    @Setter @Getter
    private Set<ProductInventory> productInventories;
}
