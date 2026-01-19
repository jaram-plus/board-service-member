package jaram.jaramplus.mopp_service.config;

import jaram.jaramplus.mopp_service.domain.Member;
import jaram.jaramplus.mopp_service.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    private static final String ADMIN_EMAIL = "specture258@hanyang.ac.kr";
    private static final String ADMIN_NAME = "관리자";

    @Override
    public void run(String... args) {
        if (memberRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
            Member admin = Member.createAdmin(ADMIN_EMAIL, ADMIN_NAME);
            memberRepository.save(admin);
            log.info("관리자 계정이 생성되었습니다: {}", ADMIN_EMAIL);
        } else {
            log.info("관리자 계정이 이미 존재합니다: {}", ADMIN_EMAIL);
        }
    }
}
