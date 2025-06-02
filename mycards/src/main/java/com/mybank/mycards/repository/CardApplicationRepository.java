package com.mybank.mycards.repository;

import com.mybank.common.entity.CardApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardApplicationRepository extends JpaRepository<CardApplication, Long> {


}