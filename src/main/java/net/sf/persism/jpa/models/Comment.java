package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Comments", schema = "dbo", catalog = "StackOverflow2010")
public class Comment {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Basic
    @Column(name = "CreationDate")
    private Date creationDate;

    @Basic
    @Column(name = "PostId")
    private Integer postId;

    @Basic
    @Column(name = "Score")
    private Integer score;

    @Basic
    @Column(name = "Text")
    private String text;

    @Basic
    @Column(name = "UserId")
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(creationDate, comment.creationDate) && Objects.equals(postId, comment.postId) && Objects.equals(score, comment.score) && Objects.equals(text, comment.text) && Objects.equals(userId, comment.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, postId, score, text, userId);
    }
}
