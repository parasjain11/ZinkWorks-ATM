package com.example.ZinkWorksATM.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ZinkWorksATM.Entity.UserAccountEntity;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long>{

	UserAccountEntity findByAccountNumber(Long accountNumber);

}
