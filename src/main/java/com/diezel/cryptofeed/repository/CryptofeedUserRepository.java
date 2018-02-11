package com.diezel.cryptofeed.repository;

import com.diezel.cryptofeed.model.CryptofeedUser;
import com.diezel.cryptofeed.repository.model.CryptofeedUserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Access cryptofeed user database values.
 *
 * @dzale
 */
public interface CryptofeedUserRepository extends CrudRepository<CryptofeedUserEntity, Long> {

    CryptofeedUserEntity findOneByUsername(String username);

}
