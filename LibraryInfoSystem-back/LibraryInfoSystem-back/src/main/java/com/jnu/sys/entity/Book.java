package com.jnu.sys.entity;

import java.io.Serializable;
import java.sql.Blob;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiav
 * @since 2025-01-26
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private String isbn;

    private String title;

    private String author;

    private String introduction;

    private String comments;

    private Blob image;

    private Integer stock;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Book{" +
            "isbn=" + isbn +
            ", title=" + title +
            ", author=" + author +
            ", introduction=" + introduction +
            ", comments=" + comments +
            ", image=" + image +
            ", stock=" + stock +
        "}";
    }
}
