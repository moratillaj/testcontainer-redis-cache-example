package com.example.testrediscache.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.testrediscache.model.Programmer;

public interface ProgrammerRepository extends JpaRepository<Programmer, String> {
    List<Programmer> findByMainLanguage(String mainLanguage);
}
