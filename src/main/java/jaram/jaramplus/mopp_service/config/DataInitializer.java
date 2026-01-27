package jaram.jaramplus.mopp_service.config;

import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.name:관리자}")
    private String adminName;

    @Override
    public void run(String... args) {
        if (adminEmail == null || adminEmail.isBlank()) {
            log.warn("관리자 이메일이 설정되지 않았습니다. (app.admin.email)");
            return;
        }

        if (memberRepository.findByEmail(adminEmail).isEmpty()) {
            Member admin = Member.createAdmin(adminEmail, adminName);
            memberRepository.save(admin);
            log.info("관리자 계정이 생성되었습니다: {}", adminEmail);
        } else {
            log.info("관리자 계정이 이미 존재합니다: {}", adminEmail);
        }
    }
}
