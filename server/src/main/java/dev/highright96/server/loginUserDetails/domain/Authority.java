package dev.highright96.server.loginUserDetails.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(Authority.class)
public class Authority implements GrantedAuthority {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    private String authority;

}

