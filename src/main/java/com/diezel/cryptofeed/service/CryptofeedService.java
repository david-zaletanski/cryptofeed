package com.diezel.cryptofeed.service;

import com.diezel.cryptofeed.model.CryptofeedUser;
import com.diezel.cryptofeed.repository.CryptofeedUserRepository;
import com.diezel.cryptofeed.repository.model.CryptofeedUserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The service supporting the main Spring Controller.
 *
 * @author dzale
 */
@Service
public class CryptofeedService {

    @Autowired
    CryptofeedUserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Returns a hello world message.
     *
     * @param world who to say hello to. if null or empty, its the world
     * @return a hello world message
     */
    public String hello(String world) {
        return "Hello " + (world == null || world.isEmpty() ? "world!" : world);
    }

    public boolean addUser(CryptofeedUser user) {

        CryptofeedUserEntity userEntity = objectMapper.convertValue(user, CryptofeedUserEntity.class);
        userRepository.save(userEntity);

        return userEntity.getId() != null;
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
