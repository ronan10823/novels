package com.example.novels.novel.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.novels.novel.entity.Novel;
import com.example.novels.novel.entity.QGenre;
import com.example.novels.novel.entity.QGrade;
import com.example.novels.novel.entity.QNovel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

public class SearchNovelRepositoryImpl extends QuerydslRepositorySupport implements SearchNovelRepository {

    public SearchNovelRepositoryImpl() {
        super(Novel.class);
    }

    @Override
    public Object[] getNovelById(Long id) {
        // 하나 조회
        // novel_id, author, title, 대여여부, 장르명(+장르아이디), 평점(평균)
        QNovel novel = QNovel.novel;
        QGenre genre = QGenre.genre;
        QGrade grade = QGrade.grade;

        JPQLQuery<Novel> query = from(novel).leftJoin(genre).on(novel.genre.eq(genre)).where(novel.id.eq(id));

        // novel id 별 평점 평균
        JPQLQuery<Double> ratingAvg = JPAExpressions.select(grade.rating.avg()).from(grade).where(grade.novel.eq(novel))
                .groupBy(grade.novel);

        JPQLQuery<Tuple> tuple = query.select(novel, genre, ratingAvg);
        Tuple result = tuple.fetchFirst();

        return result.toArray();
    }

    @Override
    public Page<Object[]> list(Long genreId, String keyword, Pageable pageable) {

        QNovel novel = QNovel.novel;
        QGenre genre = QGenre.genre;
        QGrade grade = QGrade.grade;

        JPQLQuery<Novel> query = from(novel).leftJoin(genre).on(novel.genre.eq(genre));

        // novel id 별 평점 평균
        JPQLQuery<Double> ratingAvg = JPAExpressions.select(grade.rating.avg()).from(grade).where(grade.novel.eq(novel))
                .groupBy(grade.novel);

        JPQLQuery<Tuple> tuple = query.select(novel, genre, ratingAvg);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and((novel.id.gt(0L)));

        // 검색용(장르)
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (genreId != null && genreId != 0) {
            booleanBuilder.and(novel.genre.id.eq(genreId));
        }

        // 검색용(title or author)
        if (keyword != null && !keyword.isEmpty()) {
            booleanBuilder.and(novel.title.contains(keyword));
            booleanBuilder.or(novel.author.contains(keyword));
        }

        builder.and(booleanBuilder);
        tuple.where(builder);

        // 페이지 나누기
        // org.springframework.data.domain.Sort
        Sort sort = pageable.getSort();

        // sort 기준이 여러개 있을 수 있다.
        // Sort.by("bno").descending().and(Sort.by("title").ascending()
        sort.stream().forEach(order -> {
            // com.querydsl.core.types.Order
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            String prop = order.getProperty();
            PathBuilder<Novel> orderByExpression = new PathBuilder<>(Novel.class,
                    "novel");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount();

        List<Object[]> list = result.stream().map(t -> t.toArray()).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, count);
    }

}
