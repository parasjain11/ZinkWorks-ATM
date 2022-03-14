package com.example.ZinkWorksATM.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ZinkWorksATM.Entity.BankEntity;
import com.example.ZinkWorksATM.Entity.UserAccountEntity;
import com.example.ZinkWorksATM.Exception.InsufficientBalanceException;
import com.example.ZinkWorksATM.Exception.InsufficientNoteException;
import com.example.ZinkWorksATM.Exception.InvalidAmountException;
import com.example.ZinkWorksATM.Exception.InvalidUserAccountException;
import com.example.ZinkWorksATM.Exception.UnAuthorizedUserException;
import com.example.ZinkWorksATM.Model.ATMWithdrawalDisplay;
import com.example.ZinkWorksATM.Model.Bank;
import com.example.ZinkWorksATM.Model.UserAccount;
import com.example.ZinkWorksATM.Repository.BankRepository;
import com.example.ZinkWorksATM.Repository.UserAccountRepository;

@SpringBootTest
public class ATMMachineServiceImplTest {
	
	@Mock
	UserAccountRepository accountRepository;
	
	@Mock
	BankRepository bankRepository;
	
	@Autowired
	ATMMachineServiceImpl atmMachineServiceImpl;
	
	@Test
	public void testInitialiseUserAccounts() {
		UserAccountEntity accountEntity = new UserAccountEntity((long)123456789, 1234, 800, 200);
		when(accountRepository.save(accountEntity)).thenReturn(accountEntity);
		UserAccount account =  atmMachineServiceImpl.initialiseUserAccounts(new UserAccount(accountEntity.getAccountNumber(),accountEntity.getPin(),accountEntity.getOpeningBalance(),accountEntity.getOverDraft(),null));
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
		banks = atmMachineServiceImpl.initialiseATM(banks);
		assertEquals(banks.size(), 4);
		assertEquals(banks.get(0).getAmount(), 10);
	}
	
	@Test
	public void TestCheckBalance() throws InvalidUserAccountException, UnAuthorizedUserException{
		Optional<UserAccountEntity> accountEntityOptional = Optional.of((UserAccountEntity) new UserAccountEntity((long)123456789, 1234, 800, 200));
		when(accountRepository.findById((long)123456789)).thenReturn(accountEntityOptional);
		UserAccount account =  atmMachineServiceImpl.checkBalance((long)123456789, 1234);
		assertEquals(account.getAccountNumber(), 123456789);
		assertEquals(account.getPin(), 1234);
		assertEquals(account.getOpeningBalance(), 800);
		assertEquals(account.getOverDraft(), 200);
	}
	
	@Test
	public void TestCheckBalanceUnAuthorized() throws InvalidUserAccountException, UnAuthorizedUserException{
		Optional<UserAccountEntity> accountEntityOptional = Optional.of((UserAccountEntity) new UserAccountEntity((long)123456789, 234, 800, 200));
		when(accountRepository.findById((long)123456789)).thenReturn(accountEntityOptional);
		UnAuthorizedUserException accountException = assertThrows(UnAuthorizedUserException.class, () -> atmMachineServiceImpl.checkBalance((long)123456789, 234),"User is NOT authorized to check balance :: INCORRECT PIN");
		assertTrue(accountException.getMessage().contains("User is NOT authorized to check balance :: INCORRECT PIN"));
	}
	
	@Test
	public void TestCheckBalanceInvalidUserAccountException() throws InvalidUserAccountException, UnAuthorizedUserException{
		Optional<UserAccountEntity> accountEntityOptional = Optional.empty();
		Long accountNumber = (long) 123456789;
		when(accountRepository.findById(accountNumber)).thenReturn(accountEntityOptional);
		InvalidUserAccountException accountException = assertThrows(InvalidUserAccountException.class, () -> atmMachineServiceImpl.checkBalance( (long) 12345678, 1234),"User not found ::: Invalid UserAccount entered");
		assertTrue(accountException.getMessage().contains("User not found ::: Invalid UserAccount entered"));
	}

	@Test
	public void testDispenseAmount() throws InsufficientBalanceException, InvalidAmountException, InsufficientNoteException, InvalidUserAccountException, UnAuthorizedUserException {
		ATMWithdrawalDisplay atmWithdrawalDisplay = new ATMWithdrawalDisplay();
		Long accountNumber = (long) 123456789;
		List<Bank> bankNotes = new ArrayList<Bank>();
		bankNotes.add(new Bank(50,2));
		bankNotes.add(new Bank(20,2));
		List<BankEntity> banks = new ArrayList<BankEntity>();
		banks.add(new BankEntity(50,10));
		banks.add(new BankEntity(20,30));
		banks.add(new BankEntity(10,30));
		BankEntity bankEntity = new BankEntity();
		bankEntity.setAmount(5);
		bankEntity.setValue(20);
		UserAccountEntity accountEntity = Mockito.mock(UserAccountEntity.class);
		accountEntity.setAccountNumber(accountNumber);
		accountEntity.setPin(123);
		accountEntity.setOpeningBalance(800);
		accountEntity.setOverDraft(200);
		when(accountRepository.findByAccountNumber(any(Long.class))).thenReturn(accountEntity);
		when(bankRepository.findAll()).thenReturn(banks);
		atmWithdrawalDisplay = atmMachineServiceImpl.dispenseAmount(accountNumber, 1234,140);
		assertEquals(atmWithdrawalDisplay.getNotes().size(), 2);
	}
	
	@Test
	public void testDispenseAmountInvalidUserAccountException() throws InvalidUserAccountException, UnAuthorizedUserException{
		Long accountNumber = (long) -123456789;
		UserAccountEntity accountEntity = Mockito.mock(UserAccountEntity.class);
		accountEntity.setOpeningBalance(800);
		accountEntity.setOverDraft(200);
		accountEntity.setPin(1234);
		accountEntity.setAccountNumber(accountNumber);
		when(accountRepository.findByAccountNumber(any(Long.class))).thenReturn(accountEntity);
		assertThrows(NullPointerException.class, () -> atmMachineServiceImpl.dispenseAmount(accountNumber, 1234,140),"User not found ::: Invalid UserAccount entered");
	}
	

}
