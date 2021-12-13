package net.sf.persism;

import net.sf.persism.jpa.models.User;
import net.sf.persism.perf.models.Post;
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
    public void testUserAndVotes() {
        reset();
        //var q = entityManager.createNativeQuery("select * from [Users] where ID < 1000", User.class);
        var q = entityManager.createQuery("SELECT u FROM User u WHERE u.id < 1000");

        List<User> users = q.getResultList();

        //log.info(users);
        log.info(users.size());
        User user = users.get(0);
        log.info("USER 1? : " + user);
        log.info(user.getPosts());
        out("USER 1? : " + user);

    }

    @Test
    public void testAllFullUsers() {
        reset();

    }

    @Test
    public void testAllFullAutoUsers() {
        reset();
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
    public void testUsersSingleWithFetch() {
        reset();
        // userid 392

        Query q = entityManager.
                createQuery(
                        //"SELECT u FROM User u JOIN FETCH u.posts JOIN FETCH u.votes JOIN FETCH u.badges WHERE u.id = 392 ");
                        "SELECT u FROM User u WHERE u.id = 392 ");

        User user = (User) q.getSingleResult();

        //  posts: 391 votes: 122 badges: 144 6,869,088 records!
        //User user = users.get(0);
        out("posts: " + user.getPosts().size() + " votes: " + user.getVotes().size() + " badges: " + user.getBadges().size() + " " + user);
    }


}
