package com.diezel.cryptofeed.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Data structure representing a user of the Cryptofeed API.
 *
 * @author dzale
 */

@Data       // Generates getters, setters, constructor for required fields?
@Builder    // Generates a CryptofeedUserBuilder class and methods alongside
@RequiredArgsConstructor(staticName = "of")   // Generates a static method constructor
@AllArgsConstructor // Generate full constructor
public class CryptofeedUser extends DiezelModel {

    @JsonView( value = { CryptofeedViews.IncludeInResponse.class } )
    @NotBlank(message = "Username must not be blank.")
    // Checks String is not null and length > 0, or request fails validation
    private String username;

    @JsonView( value = { CryptofeedViews.IncludeInResponse.class } )
    @NotBlank(message = "Password must not be blank.")
    private String password;

    @JsonView( value = { CryptofeedViews.IncludeInResponse.class } )
    @NotEmpty(message = "Must assign user at least one authority.")
    // Checks list not null and size > 0, or request fails validation
    private List<String> authorities;

    @JsonIgnore
    @JsonView( value = { CryptofeedViews.IncludeInResponse.class } )
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm z")
    // Will fail JSON syntax validation if not matching given date format
    private Date creationDate;

    @JsonIgnore
    @JsonView( value = { CryptofeedViews.IncludeInResponse.class } )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm z")
    private Date lastLoginDate;

    // TODO: Find a better way to extend for vendor attributes

    @NotNull
    private String krakenPublicApiKey;

    @NotNull
    private String krakenPrivateKey;

}
