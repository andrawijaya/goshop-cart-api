package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@ToString
@Entity
@Table(name = "m_order")
public class Order {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "order_id")
    @Getter
    @Setter
    private String orderId;

    @Column(name = "product_name", nullable = false, length = 100)
    @Getter @Setter
    private int quantity;

    @Column(name = "order_date")
    @Getter @Setter
    @JsonFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

//    @ToString.Exclude
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "vendor_id", referencedColumnName = "vendor_id", nullable = false)
//    @JsonManagedReference
//    @Setter @Getter
//    private Vendor vendor;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_inventoryId", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private ProductInventory productInventory;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Customer customer;


}