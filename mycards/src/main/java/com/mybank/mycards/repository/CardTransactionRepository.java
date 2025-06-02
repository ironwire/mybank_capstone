package com.mybank.mycards.repository;

import com.mybank.common.entity.Card;
import com.mybank.common.entity.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

    // 根据用户名、开始日期和结束日期查询卡片交易记录
    @Query("SELECT ct FROM CardTransaction ct WHERE ct.transactionDate BETWEEN :startDate AND :endDate")
    List<CardTransaction> findByUsernameAndTransactionDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
            
    // 根据卡片和日期范围查询交易记录
    @Query("SELECT ct FROM CardTransaction ct WHERE ct.card = :card AND ct.transactionDate BETWEEN :startDate AND :endDate")
    List<CardTransaction> findByCardAndTransactionDateBetween(
            @Param("card") Card card,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
