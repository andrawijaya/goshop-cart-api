package com.enigma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@ToString
@Entity
@Table(name = "t_transaction")
public class Transaction {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "transaction_id")
    @Getter
    @Setter
    private String transactionId;

    @Column(name = "trans_date")
    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date transDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", referencedColumnName = "vendor_id", nullable = false)
    @JsonManagedReference
    @Setter
    @Getter
    private Vendor vendor;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
    @JsonBackReference
    @Setter
    @Getter
    private List<TransactionDetail> transactionDetailList;

}
