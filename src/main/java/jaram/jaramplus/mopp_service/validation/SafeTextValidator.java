package jaram.jaramplus.mopp_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SafeTextValidator implements ConstraintValidator<SafeText, String> {

    private static final Pattern[] DANGEROUS_PATTERNS = {
        // XSS 공격 패턴
        Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
        Pattern.compile("<iframe[^>]*>.*?</iframe>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE),  // onclick, onerror 등
        Pattern.compile("<embed[^>]*>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<object[^>]*>", Pattern.CASE_INSENSITIVE),

        // SQL Injection 팬턴
        Pattern.compile("(--|\\/\\*|\\*\\/|;\\s*drop|;\\s*delete|;\\s*update|;\\s*insert)",
                       Pattern.CASE_INSENSITIVE),
        Pattern.compile("(union.*select|select.*from.*where)", Pattern.CASE_INSENSITIVE),
        Pattern.compile("(exec\\s*\\(|execute\\s*\\()", Pattern.CASE_INSENSITIVE),

        // 기타 특수문자 조합 거르기
        Pattern.compile("[<>]{2,}"),  // <<, >>, <> 등 연속된 괄호
    };

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;  // null/빈 값은 @NotBlank가 처리
        }

        // 모든 위험 패턴 검사
        for (Pattern pattern : DANGEROUS_PATTERNS) {
            if (pattern.matcher(value).find()) {
                return false;
            }
        }

        return true;
    }
}
