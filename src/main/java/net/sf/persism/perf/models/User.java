package net.sf.persism.perf.models;

import java.sql.Date;

public class User {
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

        User users = (User) o;

        if (id != null ? !id.equals(users.id) : users.id != null) {
            return false;
        }
        if (aboutMe != null ? !aboutMe.equals(users.aboutMe) : users.aboutMe != null) {
            return false;
        }
        if (age != null ? !age.equals(users.age) : users.age != null) {
            return false;
        }
        if (creationDate != null ? !creationDate.equals(users.creationDate) : users.creationDate != null) {
            return false;
        }
        if (displayName != null ? !displayName.equals(users.displayName) : users.displayName != null) {
            return false;
        }
        if (downVotes != null ? !downVotes.equals(users.downVotes) : users.downVotes != null) {
            return false;
        }
        if (emailHash != null ? !emailHash.equals(users.emailHash) : users.emailHash != null) {
            return false;
        }
        if (lastAccessDate != null ? !lastAccessDate.equals(users.lastAccessDate) : users.lastAccessDate != null) {
            return false;
        }
        if (location != null ? !location.equals(users.location) : users.location != null) {
            return false;
        }
        if (reputation != null ? !reputation.equals(users.reputation) : users.reputation != null) {
            return false;
        }
        if (upVotes != null ? !upVotes.equals(users.upVotes) : users.upVotes != null) {
            return false;
        }
        if (views != null ? !views.equals(users.views) : users.views != null) {
            return false;
        }
        if (websiteUrl != null ? !websiteUrl.equals(users.websiteUrl) : users.websiteUrl != null) {
            return false;
        }
        if (accountId != null ? !accountId.equals(users.accountId) : users.accountId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (aboutMe != null ? aboutMe.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (downVotes != null ? downVotes.hashCode() : 0);
        result = 31 * result + (emailHash != null ? emailHash.hashCode() : 0);
        result = 31 * result + (lastAccessDate != null ? lastAccessDate.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (reputation != null ? reputation.hashCode() : 0);
        result = 31 * result + (upVotes != null ? upVotes.hashCode() : 0);
        result = 31 * result + (views != null ? views.hashCode() : 0);
        result = 31 * result + (websiteUrl != null ? websiteUrl.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
