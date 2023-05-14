package com.enigma.repository;

import com.enigma.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO m_product(name, available_quantity, price) VALUES (:#{#product.name}, :#{#product.availableQuantity}, :#{#product.price})", nativeQuery = true)
    public abstract void addProduct(@Param("product") Product product);

    List<Product> findAll(Specification specification);

    @Query(value = "SELECT * FROM m_product WHERE name LIKE %?1%", nativeQuery = true)
    List<Product> findByNameContains(String title);

//    @Query(value = "SELECT * FROM m_product WHERE CAST(available_quantity AS text) LIKE %?1%", nativeQuery = true)
//    List<Product> findByQuantityContains(int qty);

//    @Query(value = "SELECT * FROM m_product WHERE CAST(price AS text) LIKE %?1%", nativeQuery = true)
//    List<Product> findByPriceContains(int price);

}
