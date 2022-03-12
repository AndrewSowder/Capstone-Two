package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalanceByAccountId(Long accountId) {
        Accounts accounts = null;
        String sql = "SELECT balance FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            accounts = mapRowAccount(results);
        } else {
            System.out.println("Account Not Found");


        }
        return accounts.getBalance();
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        Accounts accounts = null;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            accounts = mapRowAccount(results);
        } else {
            System.out.println("Account Not Found");


        }
        return accounts.getBalance();
    }



    @Override
    public Accounts getAccountById(int userId) {
        Accounts accounts = null;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            accounts = mapRowAccount(results);
        } else {
            System.out.println("Account Not Found");

        }
            return accounts;
        }

    @Override
    public Accounts getAccountByUsername(String userName) {
        Accounts accounts = null;
        String sql = "SELECT * FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userName);
        if (results.next()) {
            accounts = mapRowAccount(results);
        } else {
            System.out.println("Account Not Found");

        }
        return accounts;
    }




    @Override
    public void updateBalances(Transfers transfers) {
        String fromSql = "UPDATE account SET balance = (balance - ?) WHERE account_id = ? ";
        jdbcTemplate.update(fromSql,transfers.getAmount(), transfers.getAccountFrom());
        String toSql = "UPDATE account SET balance = (balance + ?) WHERE account_id = ? ";
        jdbcTemplate.update(toSql,transfers.getAmount(), transfers.getAccountTo());

    }

    private Accounts mapRowAccount(SqlRowSet rs){
        Accounts accounts = new Accounts();
        accounts.setAccountId(rs.getLong("account_id"));
        accounts.setUserId(rs.getInt("user_id"));
        accounts.setBalance(rs.getBigDecimal("balance"));
        return accounts;
    }
}
