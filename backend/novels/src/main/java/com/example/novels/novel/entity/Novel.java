package com.example.novels.novel.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "genre")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Novel extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private boolean available;

    private LocalDate publishedDate; // 출판일

    @Column(length = 2000)
    private String summary; // 줄거리

    @Column(length = 2000)
    private String description; // ai 소개글

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public void changeAvailable(boolean available) {
        this.available = available;
    }

    public void changeGenre(Genre genre) {
        this.genre = genre;
    }

    public void changeSummary(String summary) {
        this.summary = summary;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

}
