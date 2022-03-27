package net.sf.persism;


import net.sf.persism.perf.Category;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
            testClassName = description.getTestClass().getSimpleName();
            testMethodName = description.getMethodName();
        }
    };

    private Connection con;
    private Session session;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        perfStart();
        con = getConnection();
        session = new Session(con);
        //session.fetch(ExtendedUser.class, params(-1));
        //session.fetch(ExtendedUser.class, params(4918));
        perfEnd(Category.Setup, "SETUP: get Connection and Session");
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
    public void testSingleUser() {
        perfStart();
        ExtendedUser user = session.fetch(ExtendedUser.class, params(4918));
        perfEnd(Category.Result, "votes: " + user.getVotes().size() + " posts: " + user.getPosts().size() + " badges: " + user.getBadges().size());

        assertNotNull(user);
        assertTrue(user.getVotes().size() > 0);
        assertTrue(user.getPosts().size() > 0);
        assertTrue(user.getBadges().size() > 0);
    }

    @Test
    public void testMultipleUsers() {
        perfStart();
        List<ExtendedUser> users = session.query(ExtendedUser.class, where("Id < 1000"));
        perfEnd(Category.Result, "users: " + users.size());

        assertTrue(users.size() > 0);
        ExtendedUser user = users.get(4);
        assertTrue(user.getVotes().size() > 0);
        assertTrue(user.getBadges().size() > 0);
        assertTrue(user.getPosts().size() > 0);
        assertNotNull(user.getPosts().get(0).getUser());
        assertEquals(user.getDisplayName(), user.getPosts().get(0).getUser().getDisplayName());


        AtomicInteger votes = new AtomicInteger();
        AtomicInteger badges = new AtomicInteger();
        AtomicInteger posts = new AtomicInteger();
        AtomicInteger comments = new AtomicInteger();
        users.forEach(u -> {
            votes.addAndGet(u.getVotes().size());
            badges.addAndGet(u.getBadges().size());
            posts.addAndGet(u.getPosts().size());
            u.getPosts().forEach( p -> {
                comments.addAndGet(p.getComments().size());
            });
        });

        log.info("USERS: " + users.size() + " VOTES: " + votes + " BADGES: " + badges + " POSTS: " + posts + " COMMENTS: " + comments);
    }


    @Test
    public void testMultiplePosts() throws Exception {
        perfStart();
        String sql = """
                    SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], 
                    [CommentCount], [CommunityOwnedDate], [CreationDate], [FavoriteCount], [LastActivityDate], 
                    [LastEditDate], [LastEditorDisplayName], [LastEditorUserId], [OwnerUserId], [ParentId], 
                    [PostTypeId], [Score], [Tags], [Title], [ViewCount] FROM [Posts] 
                    WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)                
                """;
        List<Post> posts = session.query(Post.class, sql(sql));
        perfEnd(Category.Result, "testPosts size: " + posts.size());
    }

    @Test
    public void testSinglePost() {

        perfStart();
        Post post = session.fetch(Post.class, params(4435775));
        perfEnd(Category.Result, getCurrentMethodName());

        assertNotNull(post);
        assertNotNull(post.getUser());
        assertNotNull(post.getPostType());
        assertEquals("comment count in db? 92?", 92, post.getCommentCount());
        assertEquals("comment count?", 42, post.getComments().size());
    }


    @Test
    public void testQueryAllBadges() throws Exception {
        perfStart();
        List<Badge> badges = session.query(Badge.class, sql("select * from Badges"));
        perfEnd(Category.Result, "badges size: " + badges.size());
    }

    public void testQueries() {
        // this loads ALL data....
        if (true) {
            return;
        }
        List<Badge> badges = session.query(Badge.class);
        perfEnd(Category.Result, "badges: " + badges.size());

        List<Comment> comments = session.query(Comment.class);
        perfEnd(Category.Result, "comments: " + comments.size());

        List<LinkType> linkTypes = session.query(LinkType.class);
        perfEnd(Category.Result, "linkTypes: " + linkTypes.size());

        List<Post> posts = session.query(Post.class);
        perfEnd(Category.Result, "posts: " + posts.size());

        List<PostType> postTypes = session.query(PostType.class);
        perfEnd(Category.Result, "postTypes: " + postTypes.size());

        List<User> users = session.query(User.class);
        perfEnd(Category.Result, "users: " + users.size());

        List<Vote> votes = session.query(Vote.class);
        perfEnd(Category.Result, "votes: " + votes.size());

        List<VoteType> voteTypes = session.query(VoteType.class);
        perfEnd(Category.Result, "voteTypes: " + voteTypes.size());
    }

}
