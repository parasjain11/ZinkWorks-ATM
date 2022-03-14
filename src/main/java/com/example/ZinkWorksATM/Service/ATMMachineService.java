package com.example.ZinkWorksATM.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ZinkWorksATM.Exception.InsufficientBalanceException;
import com.example.ZinkWorksATM.Exception.InsufficientNoteException;
import com.example.ZinkWorksATM.Exception.InvalidAmountException;
import com.example.ZinkWorksATM.Exception.InvalidUserAccountException;
import com.example.ZinkWorksATM.Exception.UnAuthorizedUserException;
import com.example.ZinkWorksATM.Model.ATMWithdrawalDisplay;
import com.example.ZinkWorksATM.Model.Bank;
import com.example.ZinkWorksATM.Model.UserAccount;

@Service
public interface ATMMachineService {

	UserAccount initialiseUserAccounts(UserAccount userAccount);

	List<Bank> initialiseATM(List<Bank> bank);

	UserAccount checkBalance(Long accountNumber, Integer pin) throws InvalidUserAccountException, UnAuthorizedUserException;

	ATMWithdrawalDisplay dispenseAmount(Long accountNumber, int pin, int amount) throws InsufficientBalanceException, InvalidAmountException, InsufficientNoteException, InvalidUserAccountException, UnAuthorizedUserException;

}
