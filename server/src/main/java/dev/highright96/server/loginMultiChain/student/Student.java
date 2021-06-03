package dev.highright96.server.loginMultiChain.student;

import dev.highright96.server.loginMultiChain.teacher.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    private String id;
    private String username;
    private Set<GrantedAuthority> role;
    private String teacherId;
}
