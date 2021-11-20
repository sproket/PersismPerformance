package net.sf.persism;


import net.sf.persism.perf.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import static net.sf.persism.NadaPrintStream.out;
import static net.sf.persism.Parameters.params;
import static net.sf.persism.SQL.where;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStackOverflow {

    // https://www.brentozar.com/archive/2015/10/how-to-download-the-stack-overflow-database-via-bittorrent/
    // https://meta.stackexchange.com/questions/2677/database-schema-documentation-for-the-public-data-dump-and-sede/2678#2678

    static {
        InputStream stream = TestStackOverflow.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            //LOGGER= Logger.getLogger(MyClass.class.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Connection con;
    Session session;
    long now;

    @BeforeEach
    public void setup() throws Exception {
        now = System.currentTimeMillis();

        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        out.println(url);

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
        FullUser fullUser = session.fetch(FullUser.class, params(4918));
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

        List<Post> posts;

//        List<User> users = session.query(User.class, where(":id < 1000"));
//        System.out.println("users count: " + users.size());
//        out("time?");
//
//        out("testAllFullAUTOUsers TIME?");
//
//         List<FullUser> fullUsers = session.query(FullUser.class, where(":id < 1000"));
//
//        posts = session.query(Post.class, where(":ownerUserId IN (SELECT Id FROM Users WHERE Id < 1000)"));
//        System.out.println("total posts? FullUser? " + posts.size());
//
//        System.out.println("full users count: " + fullUsers.size());
//
//        out("testAllFullUsers TIME?");
//
//        long result = fullUsers.stream().mapToLong(s -> s.getPosts().size()).sum();
//        System.out.println("total posts?" + result);
//
//        result = fullUsers.stream().mapToLong(s -> s.getVotes().size()).sum();
//        System.out.println("total votes?" + result);

        List<FullAutoUser> fullAutoUsers = session.query(FullAutoUser.class, where(":id < 1000"));
//        posts = session.query(Post.class, where(":ownerUserId IN (SELECT Id FROM Users WHERE Id < 1000)"));
        //System.out.println("total posts? FullAutoUser? " + posts.size());
        System.out.println("full auto users count: " + fullAutoUsers.size());
//        fullAutoUsers.stream().forEach(fullAutoUser -> {
//            var x = fullAutoUser.getPosts().stream().filter(post -> post.getUser() != null).findFirst();
//            if (x.isPresent()) {
//                System.out.println("ASS " + x);
//            }
//        });

        // why is this nulkl?
        System.out.println("HUH? " + fullAutoUsers.get(0).getPosts().get(0).getUser());

        out("TIME?");

        Post post = session.fetch(Post.class, params(4));
        System.out.println(post + " " + post.getUser());
        assertNotNull(post);
        assertNotNull(post.getUser());


    }

    @Test
    public void testQueries() {
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

    void out(Object text) {
        System.out.println(text + " " + (System.currentTimeMillis() - now));
        now = System.currentTimeMillis();
    }

}
