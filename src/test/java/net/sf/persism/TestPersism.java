package net.sf.persism;


import net.sf.persism.perf.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static net.sf.persism.Parameters.params;
import static net.sf.persism.SQL.where;
import static org.junit.jupiter.api.Assertions.*;

public class TestPersism {

    // https://www.brentozar.com/archive/2015/10/how-to-download-the-stack-overflow-database-via-bittorrent/
    // https://meta.stackexchange.com/questions/2677/database-schema-documentation-for-the-public-data-dump-and-sede/2678#2678

    private static final Log log = Log.getLogger(TestPersism.class);

    Connection con;
    Session session;
    long now;

    @BeforeEach
    public void setup() throws Exception {
        now = System.nanoTime();

        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        out(url);

        con = DriverManager.getConnection(url);
        out("before session");
        session = new Session(con);
        out("setup");
    }

    @AfterEach
    public void tearDown() throws SQLException {
        con.close();
    }

    @Test
    public void testUserAndVotes() {
        out("testUserAndVotes");
        User user = session.fetch(User.class, params(9));
        assertNotNull(user);
        out("testUserAndVotes " + user);

        // select count(*) from votes  where UserId = 4918
        var fullUser = session.fetch(FullAutoUser.class, params(4918));
        out("time?");

        assertNotNull(fullUser);
        System.out.println(fullUser.getVotes().size());
        System.out.println(fullUser.getPosts().size());

        assertTrue(fullUser.getVotes().size() > 0);
        assertTrue(fullUser.getPosts().size() > 0);
    }

    @Test
    public void testAllFullUsers() {
// todo test with JDBC
        List<FullUser> fullUsers = session.query(FullUser.class, where("Id < 1000"));
        out("full users " + fullUsers.size());

        List<Post> posts = session.query(Post.class, where(":ownerUserId IN (SELECT Id FROM Users WHERE Id < 1000)"));
        out("total posts? COW? " + posts.size());

        Map<Integer, FullUser> userMap = fullUsers.stream().collect(Collectors.toMap(fullUser1 -> fullUser1.getId(), fullUser -> fullUser));
        out("mapping time");

        for (Post post : posts) {
            userMap.get(post.getOwnerUserId()).getPosts().add(post);
        }

        out("assigning time");

        System.out.println("MOIIO");
    }

    @Test
    public void testAllFullAutoUsers() {

        List<FullAutoUser> fullAutoUsers = session.query(FullAutoUser.class, where(":id < 1000"));
        out("time to q");

        long posts = fullAutoUsers.stream()
                .map(FullAutoUser::getPosts)
                .mapToLong(Collection::size)
                .sum();
        out("tiome to stream posts");

        long votes = fullAutoUsers.stream()
                .map(FullAutoUser::getVotes)
                .mapToLong(Collection::size)
                .sum();

        out("tiome to stream votes");

        System.out.println("full auto users count: " + fullAutoUsers.size() + " posts: " + posts + " votes: " + votes);

        //.collect(Collectors.toList());

        assertNotNull(fullAutoUsers.get(0).getPosts().get(0).getUser());

        out("TIME?");
    }

    @Test
    public void testFetchComments() {
        System.out.println("testFetchComments?");
        long now = System.currentTimeMillis();

        // 297267
        //2677740
        //61
        List<UserCommentXref> userCommentXrefs = session.query(UserCommentXref.class, where(":userId = ? and :postId = ?"), params(297267, 2677740));
        assertNotNull(userCommentXrefs);
        assertEquals(61, userCommentXrefs.size());
        System.out.println("TIME? " + (System.currentTimeMillis() - now));
    }

    @Test
    public void testFetchPost() {

        Post post = session.fetch(Post.class, params(4));
        System.out.println(post + " " + post.getUser());
        assertNotNull(post);
        assertNotNull(post.getUser());

        out("TIME?");
    }

    @Test
    public void testQueries() {
        // this loads ALL data....
        if (true) {
            return;
        }
        now = System.currentTimeMillis();

        List<Badge> badges = session.query(Badge.class);
        out("badges: " + badges.size());

        List<Comment> comments = session.query(Comment.class);
        out("comments: " + comments.size());

        List<LinkType> linkTypes = session.query(LinkType.class);
        out("linkTypes: " + linkTypes.size());

        List<Post> posts = session.query(Post.class);
        out("posts: " + posts.size());

        List<PostType> postTypes = session.query(PostType.class);
        out("postTypes: " + postTypes.size());

        List<User> users = session.query(User.class);
        out("users: " + users.size());

        List<Vote> votes = session.query(Vote.class);
        out("votes: " + votes.size());

        List<VoteType> voteTypes = session.query(VoteType.class);
        out("voteTypes: " + voteTypes.size());
    }

    @Test
    public void testUsersSingleWithFetch() {

        FullAutoUser user = session.fetch(FullAutoUser.class, params(392));
        out("posts: " + user.getPosts().size() + " votes: " + user.getVotes().size() + " " + user);
    }


    void out(Object text) {
        long newNan = (System.nanoTime() - now);
        long newMil = newNan / 1000000;

        System.out.println("TIME:  " + newNan + " (" + newMil + ") " + text);
        now = System.nanoTime();
    }

}
