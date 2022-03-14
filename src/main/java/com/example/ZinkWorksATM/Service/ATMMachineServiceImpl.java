package com.example.ZinkWorksATM.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class ATMMachineServiceImpl implements ATMMachineService{

	@Autowired
	UserAccountRepository accountRepository;
	
	@Autowired
	BankRepository bankRepository;
	
	@Override
	public UserAccount initialiseUserAccounts(UserAccount userAccount) {
		UserAccountEntity accountEntity = new UserAccountEntity(userAccount.getAccountNumber(),userAccount.getPin(),userAccount.getOpeningBalance(),userAccount.getOverDraft());
		accountEntity = accountRepository.save(accountEntity);
		return new UserAccount(accountEntity.getAccountNumber(),accountEntity.getPin(),accountEntity.getOpeningBalance(),accountEntity.getOverDraft(),null);
	}

	@Override
	public List<Bank> initialiseATM(List<Bank> bank) {
		final List<BankEntity> bankEntities = new ArrayList<BankEntity>();
		List<Bank> banks = new ArrayList<Bank>();
		bank.forEach(b -> bankEntities.add(new BankEntity(b.getValue(), b.getAmount())));
		bankRepository.saveAll(bankEntities);
		bankEntities.forEach(b -> banks.add(new Bank(b.getValue(), b.getAmount())));
		return banks;
	}

	@Override
	public UserAccount checkBalance(Long accountNumber, Integer pin) throws InvalidUserAccountException, UnAuthorizedUserException {
		Optional<UserAccountEntity> accountEntity = accountRepository.findById(accountNumber);
		if (accountEntity.isPresent()) {
			if (pin.equals(accountEntity.get().getPin())) {
				System.out.println("User is authorized to check balance");
				return new UserAccount(accountEntity.get().getAccountNumber(),accountEntity.get().getPin(),accountEntity.get().getOpeningBalance(),accountEntity.get().getOverDraft(),null);
			}else {
				System.out.println("User is NOT authorized to check balance :: INCORRECT PIN");
				throw new UnAuthorizedUserException("User is NOT authorized to check balance :: INCORRECT PIN");
			}
		}else {
			System.out.println("User not found");
			throw new InvalidUserAccountException("User not found ::: Invalid UserAccount entered");
		}
	}

	@Override
	public ATMWithdrawalDisplay dispenseAmount(Long accountNumber, int pin, int amount) throws InsufficientBalanceException, InvalidAmountException, InsufficientNoteException, InvalidUserAccountException, UnAuthorizedUserException {
		ATMWithdrawalDisplay atmWithdrawalDisplay;
		UserAccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
		if (accountEntity.getAccountNumber() > 0) {
			if (pin == accountEntity.getPin()) {
				List<BankEntity> bankEntities = bankRepository.findAll();
				List<Bank> banks = new ArrayList<Bank>();
				bankEntities.forEach(b -> banks.add(new Bank(b.getValue(), b.getAmount())));
				atmWithdrawalDisplay = new ATMWithdrawalDisplay(accountNumber, pin, accountEntity.getOpeningBalance(), accountEntity.getOverDraft(), countCurrency(amount, banks),null);
				if (amount <= atmWithdrawalDisplay.getOpeningBalance() + atmWithdrawalDisplay.getOverDraft()) {
					if (atmWithdrawalDisplay.getOpeningBalance() - amount < 0) {
						//needs OD
						System.out.println("needs OD");
						if (Math.abs(atmWithdrawalDisplay.getOpeningBalance()-amount) <= atmWithdrawalDisplay.getOverDraft()) {
							//Uses OD
							atmWithdrawalDisplay.setOpeningBalance(atmWithdrawalDisplay.getOpeningBalance()-amount);
							accountEntity.setOpeningBalance(atmWithdrawalDisplay.getOpeningBalance());
						}else {
							//Overflow OD Limit // insuffient balance
							System.out.println("Overflow OD Limit");
							throw new InsufficientBalanceException("Overflow OD Limit");
						}
					}else {
						//does not need OD have sufficient opening balance
						System.out.println("Does not need OD have sufficient opening balance");
						atmWithdrawalDisplay.setOpeningBalance(atmWithdrawalDisplay.getOpeningBalance()-amount);
						accountEntity.setOpeningBalance(atmWithdrawalDisplay.getOpeningBalance());
					}
				}else {
					System.out.println("insuffient Balance");
					throw new InsufficientBalanceException("Insuffient Balance");
				}
				accountEntity = accountRepository.save(accountEntity);
				return atmWithdrawalDisplay;
			}else {
				//incorrect pin 
				throw new UnAuthorizedUserException("Incorrect PIN");
			}
		}else {
			//user not found
			throw new InvalidUserAccountException("Usernot found");
		}
	}
	private static List<Bank> countCurrency(int amount, List<Bank> banks) throws InvalidAmountException, InsufficientBalanceException, InsufficientNoteException
	{
		Integer amountInAtmMachine = 0;
		List<Bank> notesOutput = new ArrayList<Bank>();
		banks.sort((Bank b1, Bank b2)->b2.getValue()-b1.getValue());
		amountInAtmMachine = banks.stream()
				  .map(bank -> bank.getAmount()*bank.getValue())
				  .reduce(0, Integer::sum);
		if (amount <= amountInAtmMachine) {
			for (int i = 0; i < banks.size(); i++) {
				if (amount >= banks.get(i).getValue()) {
					Bank bank = new Bank();
					if(amount / banks.get(i).getValue() >= banks.get(i).getAmount()) {
						bank.setAmount(banks.get(i).getAmount());
						bank.setValue(banks.get(i).getValue());
						notesOutput.add(bank);
						amount = amount - bank.getAmount() * banks.get(i).getValue();
					}else {
						bank.setAmount(amount / banks.get(i).getValue());
						bank.setValue(banks.get(i).getValue());
						notesOutput.add(bank);
						amount = amount - bank.getAmount() * banks.get(i).getValue();
					}
				}
			}
			if (amount == 0) {
				notesOutput.forEach(i -> System.out.println("Collect "+i.getAmount()+" notes of "+ i.getValue()));
				return notesOutput;	
			}else {
				System.out.println("Invalid amount in entered by user");
				throw new InvalidAmountException("Invalid amount in entered by user");
			}
		}else {
			System.out.println("Insufficient amount in ATM Machine");
			throw new InsufficientNoteException("Insufficient amount in ATM Machine");
		}
	}

}
