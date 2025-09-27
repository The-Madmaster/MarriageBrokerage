package com.mahi.marriagebrokerage.repository;

import com.mahi.marriagebrokerage.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByBrokerId(Long brokerId);
    Page<Client> findByBrokerId(Long brokerId, Pageable pageable);
    
    List<Client> findByIsActive(Boolean isActive);
    
    Optional<Client> findByIdAndBrokerId(Long id, Long brokerId);
    
    @Query("SELECT c FROM Client c WHERE c.broker.id = :brokerId AND c.isActive = true")
    List<Client> findActiveClientsByBrokerId(@Param("brokerId") Long brokerId);
    
    @Query("SELECT c FROM Client c WHERE c.gender = :gender AND c.isActive = true " +
           "AND (:ageMin IS NULL OR YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) >= :ageMin) " +
           "AND (:ageMax IS NULL OR YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) <= :ageMax) " +
           "AND (:religion IS NULL OR c.religion = :religion) " +
           "AND (:caste IS NULL OR c.caste = :caste) " +
           "AND (:city IS NULL OR c.city = :city) " +
           "AND (:education IS NULL OR c.education = :education) " +
           "AND (:occupation IS NULL OR c.occupation = :occupation)")
    Page<Client> findMatchingClients(
            @Param("gender") Client.Gender gender,
            @Param("ageMin") Integer ageMin,
            @Param("ageMax") Integer ageMax,
            @Param("religion") String religion,
            @Param("caste") String caste,
            @Param("city") String city,
            @Param("education") String education,
            @Param("occupation") String occupation,
            Pageable pageable);
    
    @Query("SELECT c FROM Client c WHERE c.isActive = true " +
           "AND (:fullName IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) " +
           "AND (:gender IS NULL OR c.gender = :gender) " +
           "AND (:religion IS NULL OR c.religion = :religion) " +
           "AND (:caste IS NULL OR c.caste = :caste) " +
           "AND (:city IS NULL OR c.city = :city) " +
           "AND (:occupation IS NULL OR c.occupation = :occupation) " +
           "AND (:education IS NULL OR c.education = :education)")
    Page<Client> searchClients(
            @Param("fullName") String fullName,
            @Param("gender") Client.Gender gender,
            @Param("religion") String religion,
            @Param("caste") String caste,
            @Param("city") String city,
            @Param("occupation") String occupation,
            @Param("education") String education,
            Pageable pageable);
}