package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Votes", schema = "dbo", catalog = "StackOverflow2010")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer postId;
    private Integer userId;
    private Integer bountyAmount;
    private Integer voteTypeId;
    private Date creationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBountyAmount() {
        return bountyAmount;
    }

    public void setBountyAmount(Integer bountyAmount) {
        this.bountyAmount = bountyAmount;
    }

    public Integer getVoteTypeId() {
        return voteTypeId;
    }

    public void setVoteTypeId(Integer voteTypeId) {
        this.voteTypeId = voteTypeId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id) && Objects.equals(postId, vote.postId) && Objects.equals(userId, vote.userId) && Objects.equals(bountyAmount, vote.bountyAmount) && Objects.equals(voteTypeId, vote.voteTypeId) && Objects.equals(creationDate, vote.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postId, userId, bountyAmount, voteTypeId, creationDate);
    }
}
