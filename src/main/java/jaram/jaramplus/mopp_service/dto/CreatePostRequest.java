package jaram.jaramplus.mopp_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jaram.jaramplus.mopp_service.validation.SafeText;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 1, max = 200, message = "제목은 1자 이상 200자 이하여야 합니다")
    @SafeText
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Size(min = 1, max = 5000, message = "내용은 1자 이상 5000자 이하여야 합니다")
    @SafeText
    private String content;

    @Size(max = 50, message = "작성자명은 50자 이하여야 합니다")
    @SafeText
    private String author;
}
