package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ToString
@Entity
@Table(name = "t_DetailTransaction")
public class TransactionDetail {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "transactionDetail_id")
    @Getter
    @Setter
    private String transactionDetailId;

    @Column(name = "qty")
    @Getter @Setter
    private Integer qty;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id", nullable = false)
    @JsonManagedReference
    @Setter @Getter
    private Transaction transaction;


}
