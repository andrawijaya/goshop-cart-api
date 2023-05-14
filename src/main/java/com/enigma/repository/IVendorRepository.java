package com.enigma.repository;

import com.enigma.entity.Product;
import com.enigma.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVendorRepository extends JpaRepository<Vendor, String> {
    @Query(value = "SELECT * FROM m_vendor WHERE name LIKE %?1%", nativeQuery = true)
    List<Vendor> findByNameContains(String title);
}
