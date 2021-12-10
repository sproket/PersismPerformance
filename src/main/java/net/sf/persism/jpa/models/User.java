package net.sf.persism.jpa.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Table(name = "Users", schema = "dbo", catalog = "StackOverflow2010")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String aboutMe;
    private Integer age;
    private Date creationDate;
    private String displayName;
    private Integer downVotes;
    private String emailHash;
    private Date lastAccessDate;
    private String location;
    private Integer reputation;
    private Integer upVotes;
    private Integer views;
    private String websiteUrl;
    private Integer accountId;

    @OneToMany()
    @JoinColumn(name = "OwnerUserId")
    private Set<Post> posts = new HashSet<>();

    @OneToMany()
    @JoinColumn(name = "UserId")
    private Set<Vote> votes = new HashSet<>();

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Integer downVotes) {
        this.downVotes = downVotes;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Integer upVotes) {
        this.upVotes = upVotes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(aboutMe, user.aboutMe) && Objects.equals(age, user.age) && Objects.equals(creationDate, user.creationDate) && Objects.equals(displayName, user.displayName) && Objects.equals(downVotes, user.downVotes) && Objects.equals(emailHash, user.emailHash) && Objects.equals(lastAccessDate, user.lastAccessDate) && Objects.equals(location, user.location) && Objects.equals(reputation, user.reputation) && Objects.equals(upVotes, user.upVotes) && Objects.equals(views, user.views) && Objects.equals(websiteUrl, user.websiteUrl) && Objects.equals(accountId, user.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, aboutMe, age, creationDate, displayName, downVotes, emailHash, lastAccessDate, location, reputation, upVotes, views, websiteUrl, accountId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", location='" + location + '\'' +
                 ", POSTS='" + getPosts().size() + '\'' +
                "}\n";
    }
}
