package com.diezel.cryptofeed.model.repository;

import com.diezel.cryptofeed.model.entity.CryptofeedUserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Access cryptofeed user database values.
 *
 * @dzale
 */
public interface CryptofeedUserRepository extends CrudRepository<CryptofeedUserEntity, Long> {

    CryptofeedUserEntity findOneByUsername(String username);

}
