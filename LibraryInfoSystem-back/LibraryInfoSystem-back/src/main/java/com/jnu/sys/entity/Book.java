package com.jnu.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jiav
 * @since 2025-02-16
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "isbn", type = IdType.INPUT)
    private String isbn;

    private String title;

    private String author;

    private String introduction;

    private String comments;

    private String image;

    private Integer stock;

    private Integer sale;

    private Integer deleted;

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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
            ", sale=" + sale +
            ", deleted=" + deleted +
        "}";
    }
}
