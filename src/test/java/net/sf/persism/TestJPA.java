package net.sf.persism;

import net.sf.persism.jpa.models.Badge;
import net.sf.persism.jpa.models.ExtendedUser;
import net.sf.persism.jpa.models.Post;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJPA extends BaseTest implements ITests {

    private static final Log log = Log.getLogger(TestJPA.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            testClassName = description.getClassName();
            testMethodName = description.getMethodName();
        }
    };

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        reset();
        entityManagerFactory = Persistence.createEntityManagerFactory("persism_perf");
        entityManager = entityManagerFactory.createEntityManager();

        out("SETUP: get entityManager");
    }

    @After
    @Override
    public void tearDown() throws Exception {
        entityManager.close();
        entityManagerFactory.close();
        super.tearDown();
    }

    @Test
    public void testExtendedUser() {
        reset();

        var q = entityManager.createQuery("SELECT u FROM ExtendedUser u WHERE u.id = 4918");

        ExtendedUser user = (ExtendedUser) q.getSingleResult();
        out("testExtendedUser: votes: " + user.getVotes().size() + " posts: " + user.getPosts().size() + " badges: " + user.getBadges().size());

        assertNotNull(user);
        assertTrue(user.getVotes().size() > 0);
        assertTrue(user.getPosts().size() > 0);
        assertTrue(user.getBadges().size() > 0);
    }

    @Test
    public void testExtendedUsers() {
        reset();

        var q = entityManager.createQuery("SELECT u FROM ExtendedUser u WHERE u.id < 1000");
        List<ExtendedUser> users = q.getResultList();

        out("testExtendedUsers: users: " + users.size());
    }

    @Test
    public void testFetchComments() {
        reset();

    }

    @Test
    public void testPostsQuery() throws Exception {
        reset();
        String sql = """
                    SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], 
                    [CommentCount], [CommunityOwnedDate], [CreationDate], [FavoriteCount], [LastActivityDate], 
                    [LastEditDate], [LastEditorDisplayName], [LastEditorUserId], [OwnerUserId], [ParentId], 
                    [PostTypeId], [Score], [Tags], [Title], [ViewCount] FROM [Posts] 
                    WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)                
                """;
        var q = entityManager.createNativeQuery(sql);
        List<Post> posts = q.getResultList();
        out("testPosts size: " + posts.size());
    }

    @Test
    public void testFetchPost() {
        reset();

//        var q = entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.posts WHERE u.id < 1000");
//
//        List<User> users = q.getResultList();
//
//        //log.info(users);
//        log.info(users.size());
//
//        User user = users.get(0);
//        log.info("USER 1? : " + user);
//        log.info(user.getPosts());
//        sout("");
    }

    @Test
    public void testQueryAllBadges() throws Exception {
        reset();

        Query q = entityManager.createNativeQuery("SELECT * FROM Badges");
        List<Badge> badges = q.getResultList();

        out("badges size: " + badges.size());
    }


}
