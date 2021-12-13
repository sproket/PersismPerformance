package net.sf.persism;


import net.sf.persism.perf.models.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static net.sf.persism.Parameters.params;
import static net.sf.persism.SQL.sql;
import static net.sf.persism.SQL.where;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPersism extends BaseTest implements ITests {

    private static final Log log = Log.getLogger(TestPersism.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            testClassName = description.getClassName();
            testMethodName = description.getMethodName();
        }
    };

    Connection con;
    Session session;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        reset();

        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        log.info(url);

        con = DriverManager.getConnection(url);
        out("SETUP: get Connection");

        session = new Session(con);
        out("SETUP: get Session");
    }

    @Override
    public void tearDown() throws Exception {
        try {
            if (con != null) {
                con.close();
            }
        } finally {
            super.tearDown();
        }
    }

    @Test
    public void testUserAndVotes() {
        reset();
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
        reset();
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
    public void testPostsQuery() throws Exception {
        String sql = """
                    SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], 
                    [CommentCount], [CommunityOwnedDate], [CreationDate], [FavoriteCount], [LastActivityDate], 
                    [LastEditDate], [LastEditorDisplayName], [LastEditorUserId], [OwnerUserId], [ParentId], 
                    [PostTypeId], [Score], [Tags], [Title], [ViewCount] FROM [Posts] 
                    WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)                
                """;
        List<Post> posts = session.query(Post.class, sql(sql));
        out("testPosts size: " + posts.size());
    }

    @Test
    public void testAllFullAutoUsers() {
        reset();
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
        reset();
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

        reset();
        Post post = session.fetch(Post.class, params(4));
        System.out.println(post + " " + post.getUser());
        assertNotNull(post);
        assertNotNull(post.getUser());

        out("testFetchPost");
    }

    @Test
    public void testUsersSingleWithFetch() {
        reset();

        FullAutoUser user = session.fetch(FullAutoUser.class, params(392));
        out("posts: " + user.getPosts().size() + " votes: " + user.getVotes().size() + " badges: " + user.getBadges().size() + " USERID: " + user.getId());
    }

    public void testQueries() {
        // this loads ALL data....
        if (true) {
            return;
        }
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

}
