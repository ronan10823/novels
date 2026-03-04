package com.example.novels.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.novels.novel.dto.NovelDTO;
import com.example.novels.novel.service.NovelService;

@SpringBootTest(properties = "spring.ai.openai.api-key=sk-")
public class NovelServiceTest {

    @Autowired
    private NovelService novelService;

    @Test
    public void aiGenerateTest() {

        NovelDTO dto = NovelDTO.builder()
                .title("된다! 하루 만에 끝내는 제미나이 활용법")
                .author("권서림")
                .available(false)
                .publishedDate(LocalDate.of(2025, 11, 17))
                .summary("""
                        이 책은 AI에 쉽게 적응하고 싶은 초보자를 위한 제미나이 입문서이다.
                        AI 활용에 익숙하지 않은 분들을 위해 제미나이에 회원 가입하고
                        간단한 대화를 나누는 것으로 시작해 상황별로 쓸 수 있는
                        제미나이 활용법 70가지를 수록했다. 실무에서 바로 적용할 수 있는 예제부터
                        일상생활에 접목해서 사용할 수 있는 예제까지 한 권으로 만나 볼 수 있어서
                        효용성이 매우 높다. 아울러 이동하면서도 제미나이를 이용할 수 있도록
                        스마트폰 제미나이 앱 사용법도 담았다. 제미나이 모델을 이용한 구글의
                        AI 서비스, 노트북LM과 구글 AI 스튜디오도 빼놓을 수 없다. 나노 바나나로
                        이미지 생성하고 비오(Veo)로 동영상을 만들다 보면 제미나이의 가치를 200% 활용할 수 있다.
                        """.stripIndent())
                .gid(7L)
                .build();

        Long id = novelService.create(dto);

        // novelService.generateDescription(id);
    }
}
