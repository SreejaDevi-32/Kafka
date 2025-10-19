package com.kafka.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kafka.springboot.WikimediaData;

public interface WikimediaDataRepository extends JpaRepository<WikimediaData,Long> {

}
