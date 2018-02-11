package com.diezel.cryptofeed.service;

import com.diezel.cryptofeed.model.CryptofeedUser;
import com.diezel.cryptofeed.repository.CryptofeedUserRepository;
import com.diezel.cryptofeed.repository.model.CryptofeedUserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The service supporting the main Spring Controller.
 *
 * @author dzale
 */
@Service
public class CryptofeedService {
    private static final Logger log = LoggerFactory.getLogger(CryptofeedService.class);

    @Autowired
    CryptofeedUserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    public boolean addUser(CryptofeedUser user) {

        // Hash User's Password for DB Insertion
        String userPassword = user.getPassword();
        String hashedUserPassword = passwordEncoder.encode(userPassword);
        user.setPassword(hashedUserPassword);

        CryptofeedUserEntity userEntity = objectMapper.convertValue(user, CryptofeedUserEntity.class);
        userRepository.save(userEntity);

        return userEntity.getId() != null;
    }

    public List<CryptofeedUser> getUsers() {
        List<CryptofeedUser> users = new ArrayList<>();
        for (CryptofeedUserEntity user : userRepository.findAll()) {
            users.add(objectMapper.convertValue(user, CryptofeedUser.class));
        }
        return users;
    }

    public CryptofeedUser getUserByUsername(String username) {
        CryptofeedUserEntity userEntity = userRepository.findOneByUsername(username);
        if (userEntity == null || userEntity.getId() == null)
            return null;

        CryptofeedUser user = objectMapper.convertValue(userEntity, CryptofeedUser.class);
        return user;
    }

    public CryptofeedUser getUser(Long userId) {
        CryptofeedUserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null || userEntity.getId() == null)
            return null;

        CryptofeedUser user = objectMapper.convertValue(userEntity, CryptofeedUser.class);
        return user;
    }
}
