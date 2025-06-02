package com.mybank.mycards.repository;

import com.mybank.common.entity.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository1 extends JpaRepository<CardTransaction, Long> {
}
