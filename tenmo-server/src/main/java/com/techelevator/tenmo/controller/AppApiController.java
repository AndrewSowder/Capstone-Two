package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@PreAuthorize("isAuthenticated()")
@RestController

public class AppApiController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransfersDao transfersDao;

    public AppApiController(UserDao userDao, AccountDao accountDao, TransfersDao transfersDao){
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transfersDao = transfersDao;
    }

    @GetMapping("accounts/balance")
    public BigDecimal getBalanceByUserId(Principal principal){
       int userId =  userDao.findIdByUsername(principal.getName());
       return this.accountDao.getBalanceByUserId(userId);
    }

    @GetMapping("accounts/users")
    public List<User> listAllUsers(){
        List<User> allUsers;
        allUsers = userDao.findAll();
        return allUsers;

    }

    @GetMapping("accounts/users/{id}")
    public User getUserById(@PathVariable Long id){
       User user = userDao.getUserById(id);
        return user;

    }

    @GetMapping("accounts/users/username/{name}")
    public Accounts getAccountByUserName(@PathVariable String name){
        return accountDao.getAccountByUsername(name);
    }

    @GetMapping("accounts/username/{id}")
    public String getUsernameByAccountId(@PathVariable Long id) {
        return this.userDao.findUsernameByAccount(id);
    }


    @PostMapping("accounts/balance/update/{transferId}")
    public void updateBalance(@RequestBody Transfers transfers, @PathVariable long transferId) throws AccountNotFoundException {
        this.accountDao.updateBalances(transfers);
    }

    @GetMapping("accounts/{id}")
    public Accounts getAccountByUserId(@PathVariable int id) throws AccountNotFoundException{
        return this.accountDao.getAccountById(id);
    }



    @GetMapping("transfers")
    public List<Transfers> getAllTransfers(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return this.transfersDao.getTransfersByUserId(userId);
    }

    @PostMapping("transfers/{transferId}")
    public void sendTransfer(@RequestBody Transfers newTransfer, @PathVariable Long transferId){
        this.transfersDao.sendTransfer(newTransfer);
    }

    /*@PostMapping("transfers/update/{transferId}")
    public void updateTransferStatusAndType(@RequestBody Transfers transfers,@PathVariable Long transferId){
        this.transfersDao.updateTransferStatusAndType();
    }*/

    @GetMapping("transfers/{id}")
    public Transfers getTransfersByTransferId(@PathVariable long id) throws AccountNotFoundException {
        return this.transfersDao.getTransfersByTransferId(id);
    }

    @GetMapping("accounts/{accountId}/transfers")
    public List<Transfers> getTransfersByAccount(@PathVariable Long accountId) {
        return this.transfersDao.getTransfersByAccount(accountId);
    }

    @GetMapping("transfers/user/{id}")
    public List<Transfers> getTransferByUserId(@PathVariable int id, Principal principal) {
        return this.transfersDao.getTransfersByUserId(id);
    }

    @GetMapping("transfers/next")
    public Long getNewTransferId(){
        return transfersDao.getNewTransferId();
    }

    @GetMapping("transfers/type/{id}")
    public String getTransferTypeDescription(@PathVariable int id) {
        return this.transfersDao.getTransferTypeDesc(id);
    }

    @GetMapping("transfers/status/{id}")
    public String getTransferStatusDescription(@PathVariable int id) {
        return this.transfersDao.getTransferStatusDesc(id);
    }






}
