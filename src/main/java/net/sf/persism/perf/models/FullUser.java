package net.sf.persism.perf.models;

import net.sf.persism.annotations.Join;
import net.sf.persism.annotations.NotColumn;
import net.sf.persism.annotations.Table;

import java.util.ArrayList;
import java.util.List;

@Table("Users")
public final class FullUser extends User {

    @NotColumn
    List<Vote> votes = new ArrayList<>();

    @NotColumn
    List<Post> posts = new ArrayList<>();

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
