package com.example.ZinkWorksATM.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ZinkWorksATM.Entity.BankEntity;

public interface BankRepository extends JpaRepository<BankEntity, Integer>{

}
