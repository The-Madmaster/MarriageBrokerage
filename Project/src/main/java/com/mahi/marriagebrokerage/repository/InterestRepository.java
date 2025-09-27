package com.mahi.marriagebrokerage.repository;

import com.mahi.marriagebrokerage.entity.Interest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByBrokerId(Long brokerId);
    Page<Interest> findByBrokerId(Long brokerId, Pageable pageable);
    
    List<Interest> findByFromClientId(Long fromClientId);
    List<Interest> findByToClientId(Long toClientId);
    
    List<Interest> findByStatus(Interest.Status status);
    
    @Query("SELECT i FROM Interest i WHERE i.broker.id = :brokerId AND i.status = :status")
    List<Interest> findByBrokerIdAndStatus(@Param("brokerId") Long brokerId, @Param("status") Interest.Status status);
    
    @Query("SELECT i FROM Interest i WHERE (i.fromClient.id = :clientId OR i.toClient.id = :clientId)")
    List<Interest> findByClientId(@Param("clientId") Long clientId);
    
    @Query("SELECT i FROM Interest i WHERE i.fromClient.broker.id = :brokerId OR i.toClient.broker.id = :brokerId")
    List<Interest> findByBrokerClients(@Param("brokerId") Long brokerId);
    
    Optional<Interest> findByFromClientIdAndToClientId(Long fromClientId, Long toClientId);
    
    @Query("SELECT COUNT(i) FROM Interest i WHERE i.broker.id = :brokerId AND i.status = :status")
    Long countByBrokerIdAndStatus(@Param("brokerId") Long brokerId, @Param("status") Interest.Status status);
    
    @Query("SELECT COUNT(i) FROM Interest i WHERE i.fromClient.id = :clientId")
    Long countSentInterestsByClientId(@Param("clientId") Long clientId);
    
    @Query("SELECT COUNT(i) FROM Interest i WHERE i.toClient.id = :clientId")
    Long countReceivedInterestsByClientId(@Param("clientId") Long clientId);
}