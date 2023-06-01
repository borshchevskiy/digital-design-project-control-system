package ru.borshchevskiy.pcs.service.services.account.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.borshchevskiy.pcs.common.exceptions.NotFoundException;
import ru.borshchevskiy.pcs.common.exceptions.UserAlreadyExistsException;
import ru.borshchevskiy.pcs.dto.account.AccountDto;
import ru.borshchevskiy.pcs.entities.account.Account;
import ru.borshchevskiy.pcs.repository.account.AccountRepository;
import ru.borshchevskiy.pcs.service.mappers.account.AccountMapper;
import ru.borshchevskiy.pcs.service.services.account.AccountService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final AccountRepository repository;
    private final AccountMapper accountMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
    }

    @Override
    @Transactional
    public AccountDto save(AccountDto request) {
        return request.getId() == null
                ? create(request)
                : update(request);
    }

    private AccountDto create(AccountDto request) {
        Optional<Account> optionalAccount = repository.findByUsername(request.getUsername());

        if (optionalAccount.isPresent()) {
            throw new UserAlreadyExistsException("Username already exist!");
        }

        Account account = repository.save(accountMapper.createAccount(request));
        return accountMapper.mapToDto(account);
    }

    private AccountDto update(AccountDto request) {

        Account account = repository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Account not found!"));

        account = accountMapper.mergeAccount(account, request);

        return accountMapper.mapToDto(repository.save(account));
    }


}