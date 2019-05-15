package com.example.testrediscache.service;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.testrediscache.model.Programmer;
import com.example.testrediscache.repository.ProgrammerRepository;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProgrammerService {

    @Autowired
    private ProgrammerRepository programmerRepository;

    private final long LASTED_TIME_MILLISECONDS = 5000;

    public List<Programmer> findAllProgrammers() {
        return programmerRepository.findAll();
    }

    @Cacheable(value = "programmers")
    public List<Programmer> findProgrammersByMainLanguage(String mainLanguage) {
        log.info("findProgrammersByMainLanguage-" + mainLanguage);
        Callable<List<Programmer>> findProgrammersByMainLanguageCall = () -> {
            Thread.sleep(LASTED_TIME_MILLISECONDS);
            return programmerRepository.findByMainLanguage(mainLanguage);
        };

        return Try.of(() -> findProgrammersByMainLanguageCall.call())
                .getOrElseThrow(throwable -> new RuntimeException("Unexpected error", throwable));
    }

    @CacheEvict(value = "programmers", key = "#programmer.mainLanguage")
    public Programmer saveProgrammer(Programmer programmer) {
        log.info("saveProgrammer" + programmer);
        return programmerRepository.save(programmer);
    }
}
