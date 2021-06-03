package dev.highright96.server.loginMultiChain.teacher;

import dev.highright96.server.loginMultiChain.student.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher {
    private String id;
    private String username;
    private Set<GrantedAuthority> role;
    private List<Student> studentList;
}
