package com.diezel.cryptofeed.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * JPA domain model for CryptofeedUser.
 *
 * @author dzale
 */
@Data
@Entity                         // Defines it as a JPA DB object.
@Table(name = "CF_USERS")     // Specifies To Create a Table (optionally specify name or let it generate for you)
public class CryptofeedUserEntity {

    @Id
    //@SequenceGenerator( name = "userSequence", sequenceName = "USER_SEQ", allocationSize = 1) // Only needed for Oracle? Not sure what 1 is
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Optionally name the sequence generator to use, or let it figure it out :)
    @Column(name = "CF_ID")
    private Long id;            // Domain has id primary key, model doesn't. Confuses modelmapper sometimes.

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private List<String> authorities;

    @Column
    private Date creationDate;

    @Column
    private Date lastLoginDate;

}
