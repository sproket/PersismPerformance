package net.sf.persism;

import net.sf.persism.jpa.models.User;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJPA extends BaseTest {

    private static final Log log = Log.getLogger(TestJPA.class);

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        entityManagerFactory = Persistence.createEntityManagerFactory("persism_perf");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void tearDown() throws Exception {
        entityManager.close();
        entityManagerFactory.close();
        super.tearDown();;
    }

    @Override
    public void testUserAndVotes() {
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

    @Override
    public void testAllFullUsers() {

    }

    @Override
    public void testAllFullAutoUsers() {
    }

    @Override
    public void testFetchComments() {

    }

    @Override
    public void testFetchPost() {
        long now = System.currentTimeMillis();

        var q = entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.posts WHERE u.id < 1000");

        List<User> users = q.getResultList();

        //log.info(users);
        log.info(users.size());
        System.out.println(System.currentTimeMillis() - now);

        User user = users.get(0);
        log.info("USER 1? : " + user);
        log.info(user.getPosts());
    }

    @Override
    public void testUsersSingleWithFetch() {
        // ROFL https://stackoverflow.com/questions/30088649/how-to-use-multiple-join-fetch-in-one-jpql-query
        // userid 392

        Query q = entityManager.
                createQuery(
                        //"SELECT u FROM User u JOIN FETCH u.posts JOIN FETCH u.votes JOIN FETCH u.badges WHERE u.id = 392 ");
                        "SELECT u FROM User u WHERE u.id = 392 ");

        User user = (User) q.getSingleResult();

        //  posts: 391 votes: 122 badges: 144 6869088
        //User user = users.get(0);
        out("posts: " + user.getPosts().size() + " votes: " + user.getVotes().size() + " badges: " + user.getBadges().size() + " " + user);
    }


}
