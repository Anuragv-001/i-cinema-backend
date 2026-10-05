package com.icinema.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity 
@Table(name = "movies")
public class Movie {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String title;

    @Column (nullable = false, length=1000)
    private String description;

    @Column (nullable = false)
    private String genre;

    @Column (nullable = false)
    private String language;

    @Column (nullable = false)
    private Integer duration;

    @Column (nullable=false)
    private String certificate;

    @Column (nullable = false)
    private LocalDate releaseDate;

    @Column (nullable = false, length=1000)
    private String posterUrl;

    @Column (nullable = false)
    private Double rating;

    public Movie(){}

    public Long getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getCertificate() {
        return certificate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
