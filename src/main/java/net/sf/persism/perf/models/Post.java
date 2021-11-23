package net.sf.persism.perf.models;

import net.sf.persism.annotations.Join;
import net.sf.persism.annotations.NotColumn;

import java.sql.Date;
import java.util.List;

public final class Post {
    private Integer id;
    private Integer acceptedAnswerId;
    private Integer answerCount;
    private String body;
    private Date closedDate;
    private Integer commentCount;
    private Date communityOwnedDate;
    private Date creationDate;
    private Integer favoriteCount;
    private Date lastActivityDate;
    private Date lastEditDate;
    private String lastEditorDisplayName;
    private Integer lastEditorUserId;
    private Integer ownerUserId;
    private Integer parentId;
    private Integer postTypeId;
    private Integer score;
    private String tags;
    private String title;
    private Integer viewCount;

    // infinite loop if you do FullAutoUser
    @Join(to=User.class, onProperties = "ownerUserId", toProperties = "id")
    //@NotColumn
    private User user;

    //@Join(to=Comment.class, onProperties = "id, ownerUserId", toProperties = "postId, userId")
    @NotColumn
    private Comment comment;

    //@Join(to=Comment.class, onProperties = "id, ownerUserId", toProperties = "postId, userId")
    @NotColumn
    private List<Comment> comments;

    public Post() {
    }

    public Post(Integer id, Integer acceptedAnswerId, Integer answerCount, String body, Date closedDate, Integer commentCount, Date communityOwnedDate, Date creationDate, Integer favoriteCount, Date lastActivityDate, Date lastEditDate, String lastEditorDisplayName, Integer lastEditorUserId, Integer ownerUserId, Integer parentId, Integer postTypeId, Integer score, String tags, String title, Integer viewCount) {
        this.id = id;
        this.acceptedAnswerId = acceptedAnswerId;
        this.answerCount = answerCount;
        this.body = body;
        this.closedDate = closedDate;
        this.commentCount = commentCount;
        this.communityOwnedDate = communityOwnedDate;
        this.creationDate = creationDate;
        this.favoriteCount = favoriteCount;
        this.lastActivityDate = lastActivityDate;
        this.lastEditDate = lastEditDate;
        this.lastEditorDisplayName = lastEditorDisplayName;
        this.lastEditorUserId = lastEditorUserId;
        this.ownerUserId = ownerUserId;
        this.parentId = parentId;
        this.postTypeId = postTypeId;
        this.score = score;
        this.tags = tags;
        this.title = title;
        this.viewCount = viewCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
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

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Post posts = (Post) o;

        if (id != null ? !id.equals(posts.id) : posts.id != null) {
            return false;
        }
        if (acceptedAnswerId != null ? !acceptedAnswerId.equals(posts.acceptedAnswerId) : posts.acceptedAnswerId != null) {
            return false;
        }
        if (answerCount != null ? !answerCount.equals(posts.answerCount) : posts.answerCount != null) {
            return false;
        }
        if (body != null ? !body.equals(posts.body) : posts.body != null) {
            return false;
        }
        if (closedDate != null ? !closedDate.equals(posts.closedDate) : posts.closedDate != null) {
            return false;
        }
        if (commentCount != null ? !commentCount.equals(posts.commentCount) : posts.commentCount != null) {
            return false;
        }
        if (communityOwnedDate != null ? !communityOwnedDate.equals(posts.communityOwnedDate) : posts.communityOwnedDate != null) {
            return false;
        }
        if (creationDate != null ? !creationDate.equals(posts.creationDate) : posts.creationDate != null) {
            return false;
        }
        if (favoriteCount != null ? !favoriteCount.equals(posts.favoriteCount) : posts.favoriteCount != null) {
            return false;
        }
        if (lastActivityDate != null ? !lastActivityDate.equals(posts.lastActivityDate) : posts.lastActivityDate != null) {
            return false;
        }
        if (lastEditDate != null ? !lastEditDate.equals(posts.lastEditDate) : posts.lastEditDate != null) {
            return false;
        }
        if (lastEditorDisplayName != null ? !lastEditorDisplayName.equals(posts.lastEditorDisplayName) : posts.lastEditorDisplayName != null) {
            return false;
        }
        if (lastEditorUserId != null ? !lastEditorUserId.equals(posts.lastEditorUserId) : posts.lastEditorUserId != null) {
            return false;
        }
        if (ownerUserId != null ? !ownerUserId.equals(posts.ownerUserId) : posts.ownerUserId != null) {
            return false;
        }
        if (parentId != null ? !parentId.equals(posts.parentId) : posts.parentId != null) {
            return false;
        }
        if (postTypeId != null ? !postTypeId.equals(posts.postTypeId) : posts.postTypeId != null) {
            return false;
        }
        if (score != null ? !score.equals(posts.score) : posts.score != null) {
            return false;
        }
        if (tags != null ? !tags.equals(posts.tags) : posts.tags != null) {
            return false;
        }
        if (title != null ? !title.equals(posts.title) : posts.title != null) {
            return false;
        }
        if (viewCount != null ? !viewCount.equals(posts.viewCount) : posts.viewCount != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (acceptedAnswerId != null ? acceptedAnswerId.hashCode() : 0);
        result = 31 * result + (answerCount != null ? answerCount.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (closedDate != null ? closedDate.hashCode() : 0);
        result = 31 * result + (commentCount != null ? commentCount.hashCode() : 0);
        result = 31 * result + (communityOwnedDate != null ? communityOwnedDate.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (favoriteCount != null ? favoriteCount.hashCode() : 0);
        result = 31 * result + (lastActivityDate != null ? lastActivityDate.hashCode() : 0);
        result = 31 * result + (lastEditDate != null ? lastEditDate.hashCode() : 0);
        result = 31 * result + (lastEditorDisplayName != null ? lastEditorDisplayName.hashCode() : 0);
        result = 31 * result + (lastEditorUserId != null ? lastEditorUserId.hashCode() : 0);
        result = 31 * result + (ownerUserId != null ? ownerUserId.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (postTypeId != null ? postTypeId.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (viewCount != null ? viewCount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
