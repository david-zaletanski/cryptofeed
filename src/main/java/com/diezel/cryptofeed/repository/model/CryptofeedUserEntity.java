package com.diezel.cryptofeed.repository.model;

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
@Entity      // Defines it as a JPA DB object.
@Table( name = "CF_USER" )       // Specifies To Create a Table (optionally specify name or let it generate for you)
public class CryptofeedUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;            // Domain has id primary key, model doesn't. Confuses ModelMapper sometimes.

    @Column( name = "username" )
    private String username;

    @Column( name = "passwordHash" )
    private String password;

    @ElementCollection
    @CollectionTable(
            name = "CF_USER_AUTHORITIES",
            joinColumns = @JoinColumn( name = "id", referencedColumnName = "id" )
    )
    @Column( name = "authorities" )
    private List<String> authorities;

    @Column( name = "creationDate" )
    private Date creationDate;

    @Column( name = "lastLoginDate" )
    private Date lastLoginDate;

    /**

     Examples of Adding Entity/Table Relationships

     // First is Child > Parent

     @ManyToOne()
     @JoinColumn( name = "SGN_PKG_ID", referencedColumnName = "PKG_ID", insertable = false, updatable = false )
     private PackageEntity packageEntity;

     // Parent to Child
     @OneToMany( cascade = CascadeType.ALL )
     @JoinColumn( name = "SGN_PKG_ID", nullable = false, foreignKey = @ForeignKey( name = "CMFPSGN1" ) )
     private Set<SignerEntity> signers;

     // Simple (1D?) Collection of String Values

     @ElementCollection
     @CollectionTable( name = "CMT_CONSUMER_ROLES",
     joinColumns = @JoinColumn( name = "CNR_CON_ID", referencedColumnName = "CON_ID", foreignKey = @ForeignKey( name = "CMFCCRL1" ) ) )
     @Column( name = "CNR_CONSUMER_ROLE" )
     private List<String> consumerRoles;

     */

}
