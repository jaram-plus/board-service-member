package jaram.jaramplus.mopp_service.controller;

import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.dto.MemberStatusResponse;
import jaram.jaramplus.mopp_service.dto.PendingMemberResponse;
import jaram.jaramplus.mopp_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/members/pending")
    public ResponseEntity<List<PendingMemberResponse>> getPendingMembers() {
        List<PendingMemberResponse> pendingMembers = adminService.getPendingMembers();
        return ResponseEntity.ok(pendingMembers);
    }

    @PostMapping("/members/{memberId}/approve")
    public ResponseEntity<MemberStatusResponse> approveMember(@PathVariable Long memberId) {
        Member member = adminService.approveMember(memberId);
        return ResponseEntity.ok(MemberStatusResponse.from(member));
    }

    @PostMapping("/members/{memberId}/reject")
    public ResponseEntity<MemberStatusResponse> rejectMember(@PathVariable Long memberId) {
        Member member = adminService.rejectMember(memberId);
        return ResponseEntity.ok(MemberStatusResponse.from(member));
    }
}
