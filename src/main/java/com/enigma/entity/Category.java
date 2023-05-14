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
import java.util.List;

@ToString
@Entity
@Table(name = "m_category")
public class Category {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "category_id")
    @Getter @Setter
    private String categoryId;

    @Column(name = "category_name", nullable = false, length = 100)
    @Getter @Setter
    private String categoryName;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "category")
    @JsonBackReference
    @Setter @Getter
    private List<Vendor> vendorList;
}

