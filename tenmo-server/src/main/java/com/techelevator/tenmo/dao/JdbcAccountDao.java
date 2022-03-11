package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
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
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            accounts = mapRowAccount(results);
        } else {
            System.out.println("Account Not Found");


        }
        return accounts.getBalance();
    }

    @Override
    public Accounts getAccountBy(int userId) {
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
    public void updateBalance(Long accountId) {
        Accounts accounts = new Accounts();
        String sql = "UPDATE account SET balance = ? WHERE account_id = ? ";
        jdbcTemplate.update(sql, accounts.getBalance(),accountId);

    }

    private Accounts mapRowAccount(SqlRowSet rs){
        Accounts accounts = new Accounts();
        accounts.setAccountId(rs.getLong("account_id"));
        accounts.setUserId(rs.getInt("user_id"));
        accounts.setBalance(rs.getBigDecimal("balance"));
        return accounts;
    }
}
