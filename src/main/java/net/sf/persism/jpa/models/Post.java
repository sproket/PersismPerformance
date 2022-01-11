package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Posts", schema = "dbo", catalog = "StackOverflow2010")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer acceptedAnswerId;

    private int answerCount;

    private String body;

    private Date closedDate;

    private int commentCount;

    private Date communityOwnedDate;

    private Date creationDate;

    private int favoriteCount;

    private Date lastActivityDate;

    private Date lastEditDate;

    private String lastEditorDisplayName;

    private Integer lastEditorUserId;

    //private Integer ownerUserId;
    private Integer parentId;
    private Integer postTypeId;

    private int score;
    private String tags;
    private String title;
    private int viewCount;

    @OneToOne
    @JoinColumn(name = "ownerUserId", referencedColumnName = "Id")
    private User user;

    @OneToMany
    @JoinColumn(name = "postid", referencedColumnName = "id")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "posttypeid", referencedColumnName = "id")
    private PostType postType;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAcceptedAnswerId() {
        return acceptedAnswerId;
    }

    public void setAcceptedAnswerId(Integer acceptedAnswerId) {
        this.acceptedAnswerId = acceptedAnswerId;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }


    public Date getCommunityOwnedDate() {
        return communityOwnedDate;
    }

    public void setCommunityOwnedDate(Date communityOwnedDate) {
        this.communityOwnedDate = communityOwnedDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getLastEditorDisplayName() {
        return lastEditorDisplayName;
    }

    public void setLastEditorDisplayName(String lastEditorDisplayName) {
        this.lastEditorDisplayName = lastEditorDisplayName;
    }

    public Integer getLastEditorUserId() {
        return lastEditorUserId;
    }

    public void setLastEditorUserId(Integer lastEditorUserId) {
        this.lastEditorUserId = lastEditorUserId;
    }

//    public Integer getOwnerUserId() {
//        return ownerUserId;
//    }
//
//    public void setOwnerUserId(Integer ownerUserId) {
//        this.ownerUserId = ownerUserId;
//    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(Integer postTypeId) {
        this.postTypeId = postTypeId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(acceptedAnswerId, post.acceptedAnswerId) && Objects.equals(answerCount, post.answerCount) && Objects.equals(body, post.body) && Objects.equals(closedDate, post.closedDate) && Objects.equals(commentCount, post.commentCount) && Objects.equals(communityOwnedDate, post.communityOwnedDate) && Objects.equals(creationDate, post.creationDate) && Objects.equals(favoriteCount, post.favoriteCount) && Objects.equals(lastActivityDate, post.lastActivityDate) && Objects.equals(lastEditDate, post.lastEditDate) && Objects.equals(lastEditorDisplayName, post.lastEditorDisplayName) && Objects.equals(lastEditorUserId, post.lastEditorUserId) && Objects.equals(parentId, post.parentId) && Objects.equals(postTypeId, post.postTypeId) && Objects.equals(score, post.score) && Objects.equals(tags, post.tags) && Objects.equals(title, post.title) && Objects.equals(viewCount, post.viewCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, acceptedAnswerId, answerCount, body, closedDate, commentCount, communityOwnedDate, creationDate, favoriteCount, lastActivityDate, lastEditDate, lastEditorDisplayName, lastEditorUserId, parentId, postTypeId, score, tags, title, viewCount);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", creationDate=" + creationDate +
                ", ownerUser=" + user +
                ", parentId=" + parentId +
                ", postTypeId=" + postTypeId +
                '}';
    }
}
