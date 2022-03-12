package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();


    User findByUsername(String username);

    int findIdByUsername(String username);

    String findUsernameByAccount(Long accountNumber);

    boolean create(String username, String password);

    User getUserById(Long userId);
}
