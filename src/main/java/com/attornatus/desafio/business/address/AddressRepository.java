package com.attornatus.desafio.business.address;

import com.attornatus.desafio.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("""
            SELECT COUNT(a) > 0
            FROM Person p, Address a
            WHERE p.id = :personId AND a MEMBER OF p.address AND a.mainAddress
            """)
    boolean existsMainAddress(Long personId);

    @Query("""
            SELECT a
            FROM Address a, Person p
            WHERE p.id = :personId AND a MEMBER OF p.address
            """)
    List<Address> findAllByPersonId(@Param("personId") Long personId);

    @Query("""
                SELECT a
                FROM Address a, Person p
                WHERE p.id = :personId AND a MEMBER OF p.address AND a.mainAddress
            """)
    Address findMainAddressByPersonId(@Param("personId") Long personId);

    @Query("""
                SELECT a
                FROM Person p, Address a
                WHERE p.id = :personId AND a.id = :addressId AND a MEMBER OF p.address
            """)
    Address findByPersonIdAndAddressId(@Param("personId") Long personId,
                                       @Param("addressId") Long addressId);

}
