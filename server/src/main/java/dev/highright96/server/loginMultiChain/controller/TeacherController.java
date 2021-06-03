package dev.highright96.server.loginMultiChain.controller;

import dev.highright96.server.loginMultiChain.student.StudentManager;
import dev.highright96.server.loginMultiChain.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    StudentManager studentManager;

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    public String main(@AuthenticationPrincipal Teacher teacher, Model model) {
        model.addAttribute("studentList", studentManager.myStudentList(teacher.getId()));
        return "login-custom-filter/TeacherMain";
    }
}
