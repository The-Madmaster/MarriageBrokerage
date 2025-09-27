package com.mahi.marriagebrokerage.controller;

import com.mahi.marriagebrokerage.dto.request.ClientRegistrationRequest;
import com.mahi.marriagebrokerage.dto.response.ClientResponse;
import com.mahi.marriagebrokerage.entity.Client;
import com.mahi.marriagebrokerage.entity.User;
import com.mahi.marriagebrokerage.repository.ClientRepository;
import com.mahi.marriagebrokerage.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/broker")
@RequiredArgsConstructor
public class BrokerController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('BROKER') or hasRole('ADMIN')")
    @PostMapping("/clients")
    public ResponseEntity<?> addClient(@Valid @RequestBody ClientRegistrationRequest request) {
        Optional<User> brokerOpt = userRepository.findById(request.getBrokerId());
        if (brokerOpt.isEmpty() || brokerOpt.get().getRole() != User.Role.BROKER) {
            return ResponseEntity.badRequest().body("Invalid broker ID");
        }
        Client client = new Client();
        // ... set fields from request ...
        client.setFullName(request.getFullName());
        client.setDateOfBirth(request.getDateOfBirth());
        client.setGender(request.getGender());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setEmail(request.getEmail());
        client.setOccupation(request.getOccupation());
        client.setEducation(request.getEducation());
        client.setReligion(request.getReligion());
        client.setCaste(request.getCaste());
        client.setSubcaste(request.getSubcaste());
        client.setAnnualIncome(request.getAnnualIncome());
        client.setCity(request.getCity());
        client.setState(request.getState());
        client.setCountry(request.getCountry());
        client.setHeightCm(request.getHeightCm());
        client.setWeightKg(request.getWeightKg());
        client.setMaritalStatus(request.getMaritalStatus());
        client.setFatherName(request.getFatherName());
        client.setFatherOccupation(request.getFatherOccupation());
        client.setMotherName(request.getMotherName());
        client.setMotherOccupation(request.getMotherOccupation());
        client.setFamilyType(request.getFamilyType());
        client.setFamilyIncome(request.getFamilyIncome());
        client.setSiblingsCount(request.getSiblingsCount());
        client.setPreferredAgeMin(request.getPreferredAgeMin());
        client.setPreferredAgeMax(request.getPreferredAgeMax());
        client.setPreferredHeightMin(request.getPreferredHeightMin());
        client.setPreferredHeightMax(request.getPreferredHeightMax());
        client.setPreferredReligion(request.getPreferredReligion());
        client.setPreferredCaste(request.getPreferredCaste());
        client.setPreferredEducation(request.getPreferredEducation());
        client.setPreferredOccupation(request.getPreferredOccupation());
        client.setPreferredIncomeMin(request.getPreferredIncomeMin());
        client.setPreferredLocation(request.getPreferredLocation());
        client.setProfilePhotoUrl(request.getProfilePhotoUrl());
        client.setHoroscopeUrl(request.getHoroscopeUrl());
        client.setBio(request.getBio());
        client.setBroker(brokerOpt.get());
        clientRepository.save(client);
        return ResponseEntity.ok("Client added successfully!");
    }

    @PreAuthorize("hasRole('BROKER') or hasRole('ADMIN')")
    @GetMapping("/clients")
    public ResponseEntity<?> getClients(@RequestParam Long brokerId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Page<Client> clients = clientRepository.findByBrokerId(brokerId, PageRequest.of(page, size));
        return ResponseEntity.ok(clients.getContent().stream().map(client -> {
            ClientResponse resp = new ClientResponse();
            resp.setId(client.getId());
            resp.setFullName(client.getFullName());
            resp.setDateOfBirth(client.getDateOfBirth());
            resp.setGender(client.getGender());
            resp.setPhoneNumber(client.getPhoneNumber());
            resp.setEmail(client.getEmail());
            resp.setOccupation(client.getOccupation());
            resp.setEducation(client.getEducation());
            resp.setReligion(client.getReligion());
            resp.setCaste(client.getCaste());
            resp.setSubcaste(client.getSubcaste());
            resp.setAnnualIncome(client.getAnnualIncome());
            resp.setCity(client.getCity());
            resp.setState(client.getState());
            resp.setCountry(client.getCountry());
            resp.setHeightCm(client.getHeightCm());
            resp.setWeightKg(client.getWeightKg());
            resp.setMaritalStatus(client.getMaritalStatus());
            resp.setFatherName(client.getFatherName());
            resp.setFatherOccupation(client.getFatherOccupation());
            resp.setMotherName(client.getMotherName());
            resp.setMotherOccupation(client.getMotherOccupation());
            resp.setFamilyType(client.getFamilyType());
            resp.setFamilyIncome(client.getFamilyIncome());
            resp.setSiblingsCount(client.getSiblingsCount());
            resp.setPreferredAgeMin(client.getPreferredAgeMin());
            resp.setPreferredAgeMax(client.getPreferredAgeMax());
            resp.setPreferredHeightMin(client.getPreferredHeightMin());
            resp.setPreferredHeightMax(client.getPreferredHeightMax());
            resp.setPreferredReligion(client.getPreferredReligion());
            resp.setPreferredCaste(client.getPreferredCaste());
            resp.setPreferredEducation(client.getPreferredEducation());
            resp.setPreferredOccupation(client.getPreferredOccupation());
            resp.setPreferredIncomeMin(client.getPreferredIncomeMin());
            resp.setPreferredLocation(client.getPreferredLocation());
            resp.setProfilePhotoUrl(client.getProfilePhotoUrl());
            resp.setHoroscopeUrl(client.getHoroscopeUrl());
            resp.setBio(client.getBio());
            resp.setIsActive(client.getIsActive());
            resp.setBrokerId(client.getBroker().getId());
            resp.setBrokerName(client.getBroker().getFullName());
            resp.setCreatedAt(client.getCreatedAt());
            resp.setUpdatedAt(client.getUpdatedAt());
            // Calculate age
            resp.setAge(client.getDateOfBirth() != null ? java.time.Period.between(client.getDateOfBirth(), java.time.LocalDate.now()).getYears() : null);
            return resp;
        }).collect(Collectors.toList()));
    }
}
