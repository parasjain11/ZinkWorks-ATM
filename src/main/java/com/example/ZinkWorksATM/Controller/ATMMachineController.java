package com.example.ZinkWorksATM.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ZinkWorksATM.Exception.InsufficientBalanceException;
import com.example.ZinkWorksATM.Exception.InsufficientNoteException;
import com.example.ZinkWorksATM.Exception.InvalidAmountException;
import com.example.ZinkWorksATM.Exception.InvalidUserAccountException;
import com.example.ZinkWorksATM.Exception.UnAuthorizedUserException;
import com.example.ZinkWorksATM.Model.ATMWithdrawalDisplay;
import com.example.ZinkWorksATM.Model.Bank;
import com.example.ZinkWorksATM.Model.UserAccount;
import com.example.ZinkWorksATM.Service.ATMMachineService;

@RestController
public class ATMMachineController {
	
	@Autowired
	ATMMachineService atmMachineService;
	
	@CrossOrigin(origins = "*")
	@PostMapping("/initialiseUserAccounts")
	public UserAccount initialiseUserAccounts(@RequestBody UserAccount userAccount) {
		return atmMachineService.initialiseUserAccounts(userAccount);
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("/initialiseATM")
	public List<Bank> initialiseATM(@RequestBody List<Bank> bank) {
		return atmMachineService.initialiseATM(bank);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/checkBalance")
	public UserAccount checkBalance(@RequestParam("accountNumber") Long accountNumber,@RequestParam("pin") int pin) {
		try {
			return atmMachineService.checkBalance(accountNumber,pin);
		} catch (InvalidUserAccountException | UnAuthorizedUserException e) {
			return new UserAccount(null, null, null, null, e.getClass()+ " : " + e.getMessage());
		}
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/dispenseAmount")
	public ATMWithdrawalDisplay dispenseAmount(@RequestParam("accountNumber") Long accountNumber,@RequestParam("pin") int pin,@RequestParam("amount") int amount){
		try {
			return atmMachineService.dispenseAmount(accountNumber,pin,amount);
		} catch (InsufficientBalanceException | InvalidAmountException | InsufficientNoteException | InvalidUserAccountException | UnAuthorizedUserException e) {
			return new ATMWithdrawalDisplay(accountNumber, null, null, null, null, e.getClass()+ " : " + e.getMessage());
		}
	}
}
