package dev.highright96.server.loginUserDetails.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id"))
    private Set<Authority> authorities;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
