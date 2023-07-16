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

    @Column(name = "address", nullable = false , length = 100)
    @Getter @Setter
    private String address;

    @Column(name = "state", nullable = false , length = 100)
    @Getter @Setter
    private String state;

    @Column(name = "city", nullable = false , length = 100)
    @Getter @Setter
    private String city;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "vendor", fetch = FetchType.EAGER)
    @JsonBackReference
    @Setter @Getter
    private Set<ProductInventory> productInventories;


}
