package jaram.jaramplus.mopp_service.controller;

import jaram.jaramplus.mopp_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendingMembers", adminService.getPendingMembers());
        return "admin/dashboard";
    }

    @PostMapping("/members/{id}/approve")
    public String approveMember(@PathVariable Long id) {
        adminService.approveMember(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/members/{id}/reject")
    public String rejectMember(@PathVariable Long id) {
        adminService.rejectMember(id);
        return "redirect:/admin/dashboard";
    }
}
