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
import java.util.List;

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

    private Connection con;
    private Session session;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        reset();

//        Properties props = new Properties();
//        props.load(getClass().getResourceAsStream("/datasource.properties"));
//        Class.forName(props.getProperty("database.driver"));

//        String url = props.getProperty("database.url");
//        log.info(url);
//
//        con = DriverManager.getConnection(url);
        con = getConnection();
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
    public void testExtendedUser() {
        reset();

        ExtendedUser user = session.fetch(ExtendedUser.class, params(4918));

        out("testAllFullUserSingle: votes: " + user.getVotes().size() + " posts: " + user.getPosts().size() + " badges: " + user.getBadges().size());

        assertNotNull(user);
        assertTrue(user.getVotes().size() > 0);
        assertTrue(user.getPosts().size() > 0);
        assertTrue(user.getBadges().size() > 0);
    }

    @Test
    public void testExtendedUsers() {
        reset();

        List<ExtendedUser> users = session.query(ExtendedUser.class, where("Id < 1000"));

        out("testAllFullUsersMulti: users: " + users.size());
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

//    @Test
//    public void testAllFullAutoUsers() {
//        reset();
//        List<ExtendedUser> fullAutoUsers = session.query(ExtendedUser.class, where(":id < 1000"));
//        out("time to q");
//
//        long posts = fullAutoUsers.stream()
//                .map(ExtendedUser::getPosts)
//                .mapToLong(Collection::size)
//                .sum();
//        out("tiome to stream posts");
//
//        long votes = fullAutoUsers.stream()
//                .map(ExtendedUser::getVotes)
//                .mapToLong(Collection::size)
//                .sum();
//
//        out("tiome to stream votes");
//
//        System.out.println("full auto users count: " + fullAutoUsers.size() + " posts: " + posts + " votes: " + votes);
//
//        //.collect(Collectors.toList());
//
//        assertNotNull(fullAutoUsers.get(0).getPosts().get(0).getUser());
//
//        out("TIME?");
//    }

    @Test
    public void testFetchComments() {
        reset();

        // 297267
        //2677740
        //61
        List<UserCommentXref> userCommentXrefs = session.query(UserCommentXref.class, where(":userId = ? and :postId = ?"), params(297267, 2677740));
        assertNotNull(userCommentXrefs);
        assertEquals(61, userCommentXrefs.size());

        out("testFetchComments");
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
    public void testQueryAllBadges() throws Exception {
        reset();
        List<Badge> badges = session.query(Badge.class, sql("select * from Badges"));
        out("badges size: " + badges.size());
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
