package com.example.novels.novel.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.novels.ai.domain.response.AiDescriptionDto;
import com.example.novels.novel.dto.NovelDTO;
import com.example.novels.novel.dto.PageRequestDTO;
import com.example.novels.novel.dto.PageResultDTO;
import com.example.novels.novel.entity.Genre;
import com.example.novels.novel.entity.Novel;
import com.example.novels.novel.repository.GradeRepository;
import com.example.novels.novel.repository.NovelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;
    private final GradeRepository gradeRepository;
    private final ChatClient chatClient;

    // CRUD
    public Long create(NovelDTO dto) {

        Novel novel = Novel.builder()
                .author(dto.getAuthor())
                .title(dto.getTitle())
                .publishedDate(dto.getPublishedDate())
                .summary(dto.getSummary())
                .genre(Genre.builder().id(dto.getGid()).build())
                .available(dto.isAvailable())
                .build();

        Long id = novelRepository.save(novel).getId();

        return id;
    }

    public AiDescriptionDto generateDescription(Long id) {
        // description 이 없는 경우에 ai 소개글 작성
        Novel novel = novelRepository.findById(id).get();
        if (novel.getDescription() != null && !novel.getDescription().isBlank()) {
            return new AiDescriptionDto(id, novel.getDescription());
        }

        // ai 요청 들어가기
        // System Prompt 작성
        String system = """
                당신은 출판사 마케터 겸 카피라이터입니다.
                입력으로 제공된 정보(도서명, 작가, 장르, 줄거리)만 활용.
                줄거리/등장인물/세계관/수상경력 등 제공되지 않은 사실을 사용하지 말 것.
                대신 장르의 매력, 독서 경험, 기대 포인트, 추천 독자를 중심으로 소개문을 작성
                """.stripIndent();
        String user = """
                도서명 : %s
                작가: %s
                장르: %s
                줄거리: %s

                출력규칙:
                - 한국어 3~5 문장
                - 첫 문장을 훅(흥미 유발)으로 시작
                - 마지막 문장은 '이런 독자에게 추천:' 으로 시작하는 한 문장
                - 과장 광고처럼 느껴지지 않고 자연스럽게
                """.formatted(novel.getTitle(), novel.getAuthor(), novel.getGenre().getName(), novel.getSummary());

        String aiText = chatClient.prompt()
                .system(system)
                .user(user)
                .call()
                .content();
        // dirty checking
        novel.changeDescription(aiText);
        return new AiDescriptionDto(id, aiText);
    }

    @Transactional(readOnly = true)
    public NovelDTO getRow(Long id) {
        Object[] row = novelRepository.getNovelById(id);
        return entityToDto((Novel) row[0], (Genre) row[1], (Double) row[2]);
    }

    @Transactional(readOnly = true)
    public PageResultDTO<NovelDTO> getList(PageRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by("id").descending());
        Page<Object[]> result = novelRepository.list(dto.getGenre(), dto.getKeyword(), pageable);

        Function<Object[], NovelDTO> function = en -> entityToDto((Novel) en[0], (Genre) en[1], (Double) en[2]);

        List<NovelDTO> dtoList = result.get().map(function).collect(Collectors.toList());
        long totalCount = result.getTotalElements();

        return PageResultDTO.<NovelDTO>withAll().dtoList(dtoList).pageRequestDTO(dto).totalCount(totalCount).build();
    }

    // available 변경
    public Long updateAvailable(NovelDTO dto) {
        Novel novel = novelRepository.findById(dto.getId()).get();
        novel.changeAvailable(dto.isAvailable());
        return novel.getId();
    }

    // available + 장르
    public Long update(NovelDTO dto) {
        Novel novel = novelRepository.findById(dto.getId()).get();

        novel.changeAvailable(dto.isAvailable());
        novel.changeGenre(Genre.builder().id(dto.getGid()).build());
        return novel.getId();
    }

    public void delete(Long id) {
        // 평점 삭제
        gradeRepository.deleteByNovel(id);

        // 도서 삭제
        novelRepository.deleteById(id);
    }

    private NovelDTO entityToDto(Novel novel, Genre genre, Double rating) {
        NovelDTO novelDTO = NovelDTO.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .author(novel.getAuthor())
                .available(novel.isAvailable())
                .publishedDate(novel.getPublishedDate())
                .summary(novel.getSummary())
                .description(novel.getDescription())
                .gid(genre.getId())
                .genreName(genre.getName())
                .rating(rating != null ? rating.intValue() : 0)
                .build();
        return novelDTO;
    }

}
