package com.enigma.repository;

import com.enigma.entity.Product;
import com.enigma.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IUserCredentialRepository extends JpaRepository<UserCredential, String> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO m_product(name, available_quantity, price) VALUES (:#{#product.name}, :#{#product.availableQuantity}, :#{#product.price})", nativeQuery = true)
    public abstract void addProduct(@Param("product") Product product);

//    List<ProductInventory> findAll(Specification specification);

    @Query(value = "SELECT * FROM product_inventory WHERE name LIKE %?1%", nativeQuery = true)
    List<UserCredential> findByNameContains(String title);

//    @Query(value = "SELECT * FROM m_product WHERE CAST(available_quantity AS text) LIKE %?1%", nativeQuery = true)
//    List<Product> findByQuantityContains(int qty);

//    @Query(value = "SELECT * FROM m_product WHERE CAST(price AS text) LIKE %?1%", nativeQuery = true)
//    List<Product> findByPriceContains(int price);
}
