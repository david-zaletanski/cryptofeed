package com.diezel.cryptofeed.repository;

import com.diezel.cryptofeed.model.CryptofeedUser;
import org.springframework.data.repository.CrudRepository;

/**
 * Access cryptofeed user database values.
 *
 * @dzale
 */
public interface CryptofeedUserRepository extends CrudRepository<CryptofeedUser, Long> {



}
