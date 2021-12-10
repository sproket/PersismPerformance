package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "PostLinks", schema = "dbo", catalog = "StackOverflow2010")
public class PostLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Date creationDate;
    private Integer postId;
    private Integer relatedPostId;
    private Integer linkTypeId;

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

    public Integer getRelatedPostId() {
        return relatedPostId;
    }

    public void setRelatedPostId(Integer relatedPostId) {
        this.relatedPostId = relatedPostId;
    }

    public Integer getLinkTypeId() {
        return linkTypeId;
    }

    public void setLinkTypeId(Integer linkTypeId) {
        this.linkTypeId = linkTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostLink postLink = (PostLink) o;
        return Objects.equals(id, postLink.id) && Objects.equals(creationDate, postLink.creationDate) && Objects.equals(postId, postLink.postId) && Objects.equals(relatedPostId, postLink.relatedPostId) && Objects.equals(linkTypeId, postLink.linkTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, postId, relatedPostId, linkTypeId);
    }
}
