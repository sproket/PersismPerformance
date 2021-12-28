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

        perfStart();
        con = getConnection();
        session = new Session(con);
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
    public void testExtendedUser() {
        perfStart();

        ExtendedUser user = session.fetch(ExtendedUser.class, params(4918));

        perfEnd(Category.Result, "testExtendedUser: votes: " + user.getVotes().size() + " posts: " + user.getPosts().size() + " badges: " + user.getBadges().size());

        assertNotNull(user);
        assertTrue(user.getVotes().size() > 0);
        assertTrue(user.getPosts().size() > 0);
        assertTrue(user.getBadges().size() > 0);
    }

    @Test
    public void testExtendedUsers() {
        perfStart();

        List<ExtendedUser> users = session.query(ExtendedUser.class, where("Id < 1000"));

        perfEnd(Category.Result, "testExtendedUsers: users: " + users.size());

        assertTrue(users.size() > 0);
        ExtendedUser user = users.get(10);
        assertTrue(user.getVotes().size() > 0);
        assertTrue(user.getBadges().size() > 0);
        assertTrue(user.getPosts().size() > 0);
        assertNotNull(user.getPosts().get(0).getUser());
        assertEquals(user.getDisplayName(), user.getPosts().get(0).getUser().getDisplayName());


        AtomicInteger votes = new AtomicInteger();
        AtomicInteger badges = new AtomicInteger();
        AtomicInteger posts = new AtomicInteger();
        users.forEach(u -> {
            votes.addAndGet(u.getVotes().size());
            badges.addAndGet(u.getBadges().size());
            posts.addAndGet(u.getPosts().size());
        });

        log.info("USERS: " + users.size() + " VOTES: " + votes + " BADGES: " + badges + " POSTS: " + posts);
    }


    @Test
    public void testPostsQuery() throws Exception {
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
        perfStart();

        // 297267
        //2677740
        //61
        List<UserCommentXref> userCommentXrefs = session.query(UserCommentXref.class, where(":userId = ? and :postId = ?"), params(297267, 2677740));
        assertNotNull(userCommentXrefs);
        assertEquals(61, userCommentXrefs.size());

        perfEnd(Category.Result, "testFetchComments");
    }

    @Test
    public void testFetchPost() {

        perfStart();
        Post post = session.fetch(Post.class, params(4));
        System.out.println(post + " " + post.getUser());
        assertNotNull(post);
        assertNotNull(post.getUser());

        perfEnd(Category.Result, "testFetchPost");
    }


    @Test
    public void testQueryAllBadges() throws Exception {
        perfStart();
        List<Badge> badges = session.query(Badge.class, sql("select * from Badges"));
        perfEnd(Category.Result, "badges size: " + badges.size());

        perfStart();
        List<BadgeRec> BadgeRec = session.query(BadgeRec.class, sql("select * from Badges"));
        perfEnd(Category.Result, "badges rec size: " + BadgeRec.size());
    }

    //@Test
    public void testRecordInstanceTiming() throws Exception {
        Badge badge;
        BadgeRec badgeRec;

        long now = System.nanoTime();
        badge = Badge.class.getDeclaredConstructor().newInstance();
        System.out.println("object " + (System.nanoTime() - now));
        System.out.println(badge);

        now = System.nanoTime();
        Class<?>[] ctypes = {Integer.class, String.class, Integer.class, Timestamp.class};
        badgeRec = BadgeRec.class.getConstructor(ctypes).newInstance(1, "t1", 2, new Timestamp(System.currentTimeMillis()));
        System.out.println("record " + (System.nanoTime() - now));
        System.out.println(badgeRec);

        now = System.nanoTime();
        badge = Badge.class.getDeclaredConstructor().newInstance();
        System.out.println("object " + (System.nanoTime() - now));
        System.out.println(badge);

        now = System.nanoTime();
        badgeRec = BadgeRec.class.getConstructor(ctypes).newInstance(1, "t2", 2, new Timestamp(System.currentTimeMillis()));
        System.out.println("record " + (System.nanoTime() - now));
        System.out.println(badgeRec);
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
