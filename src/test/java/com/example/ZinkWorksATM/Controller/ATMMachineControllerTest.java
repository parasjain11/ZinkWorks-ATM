package com.example.ZinkWorksATM.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ZinkWorksATM.Exception.InsufficientBalanceException;
import com.example.ZinkWorksATM.Exception.InsufficientNoteException;
import com.example.ZinkWorksATM.Exception.InvalidAmountException;
import com.example.ZinkWorksATM.Exception.InvalidUserAccountException;
import com.example.ZinkWorksATM.Exception.UnAuthorizedUserException;
import com.example.ZinkWorksATM.Model.ATMWithdrawalDisplay;
import com.example.ZinkWorksATM.Model.Bank;
import com.example.ZinkWorksATM.Model.UserAccount;
import com.example.ZinkWorksATM.Service.ATMMachineService;

@SpringBootTest
public class ATMMachineControllerTest {
	
	@Autowired
	ATMMachineController atmMachineController;
	
	@Mock
	ATMMachineService atmMachineService;
	
	@Test
	public void testInitialiseUserAccounts() {
		UserAccount account =  new UserAccount((long) 123456789, 1234, 800, 200, null);
		when(atmMachineService.initialiseUserAccounts(any(UserAccount.class))).thenReturn(account);
		account =  atmMachineController.initialiseUserAccounts(account);
		assertEquals(account.getAccountNumber(), 123456789);
		assertEquals(account.getPin(), 1234);
		assertEquals(account.getOpeningBalance(), 800);
		assertEquals(account.getOverDraft(), 200);
	}
	
	@Test
	public void testInitialiseATM() {
		List<Bank> banks = new ArrayList<Bank>();
		banks.add(new Bank(50,10));
		banks.add(new Bank(20,30));
		banks.add(new Bank(10,30));
		banks.add(new Bank(5,20));
		when(atmMachineService.initialiseATM(anyList())).thenReturn(banks);
		banks = atmMachineController.initialiseATM(banks);
		assertEquals(banks.size(), 4);
		assertEquals(banks.get(0).getAmount(), 10);
	}
	
	@Test
	public void TestCheckBalance() throws InvalidUserAccountException, UnAuthorizedUserException{
		UserAccount account =  new UserAccount((long) 123456789, 1234, 800, 200, null);
		when(atmMachineService.checkBalance(anyLong(), anyInt())).thenReturn(account);
		account =  atmMachineController.checkBalance((long)123456789, 1234);
		assertEquals(account.getAccountNumber(), 123456789);
		assertEquals(account.getPin(), 1234);
		assertEquals(account.getOpeningBalance(), 800);
		assertEquals(account.getOverDraft(), 200);
	}
	
	@Test
	public void TestCheckBalanceException() throws InvalidUserAccountException, UnAuthorizedUserException{
		UserAccount account =  new UserAccount((long)12345, null, null, null, null);
		when(atmMachineService.checkBalance(anyLong(), anyInt())).thenThrow(new InvalidUserAccountException("Invalid User"));
		account =  atmMachineController.checkBalance((long)123456789, 1234);
		assertNotNull(account.getAccountNumber());
	}
	
	@Test
	public void TestDisperseAmount() throws InvalidUserAccountException, UnAuthorizedUserException, InsufficientBalanceException, InvalidAmountException, InsufficientNoteException{
		List<Bank> banks = new ArrayList<Bank>();
		banks.add(new Bank(50,10));
		banks.add(new Bank(20,30));
		banks.add(new Bank(10,30));
		banks.add(new Bank(5,20));
		ATMWithdrawalDisplay atmWithdrawalDisplay = new ATMWithdrawalDisplay((long) 123456789, 1234, 800, 200,banks, null);
		when(atmMachineService.dispenseAmount(anyLong(), anyInt(),anyInt())).thenReturn(atmWithdrawalDisplay);
		atmWithdrawalDisplay =  atmMachineController.dispenseAmount((long)123456789, 1234,400);
		assertEquals(atmWithdrawalDisplay.getAccountNumber(), 123456789);
		assertEquals(atmWithdrawalDisplay.getPin(), 1234);
		assertEquals(atmWithdrawalDisplay.getOpeningBalance(), 400);
		assertEquals(atmWithdrawalDisplay.getOverDraft(), 200);
	}
	@Test
	public void TestDisperseAmountException() throws InvalidUserAccountException, UnAuthorizedUserException, InsufficientBalanceException, InvalidAmountException, InsufficientNoteException{
		List<Bank> banks = new ArrayList<Bank>();
		banks.add(new Bank(50,10));
		banks.add(new Bank(20,30));
		banks.add(new Bank(10,30));
		banks.add(new Bank(5,20));
		ATMWithdrawalDisplay atmWithdrawalDisplay = new ATMWithdrawalDisplay((long) 123456789, 1234, 800, 200,banks, null);
		when(atmMachineService.dispenseAmount(anyLong(), anyInt(),anyInt())).thenThrow(new InvalidUserAccountException("Invalid User"));
		atmWithdrawalDisplay =  atmMachineController.dispenseAmount((long)1234567890, 1234,100);
		assertNotNull(atmWithdrawalDisplay.getAccountNumber());
	}

}
