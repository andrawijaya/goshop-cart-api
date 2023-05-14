package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

@ToString
@Entity
@Table(name = "m_vendor")
public class Vendor {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "vendor_id")
    @Getter
    @Setter
    private String vendorId;

    @Column(name = "vendor_name", nullable = false , length = 100)
    @Getter @Setter
    private String vendorName;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "vendor", fetch = FetchType.EAGER)
    @JsonBackReference
    @Setter @Getter
    private Set<Product> productList;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Category category;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "price_id", referencedColumnName = "price_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Price price;

}
