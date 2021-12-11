package net.sf.persism;

import net.sf.persism.jpa.models.User;
import org.hibernate.annotations.QueryHints;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class TestJPA {

    private static final Log log = Log.getLogger(TestJPA.class);

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    long now;


    @BeforeEach
    public void setup() {
        now = System.nanoTime();
        entityManagerFactory = Persistence.createEntityManagerFactory("persism_perf");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @RepeatedTest(2)
    public void testUsers() {

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

    @RepeatedTest(2)
    public void testUsersWithFetch() {
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

    @RepeatedTest(2)
    public void testUsersSingleWithFetch() {

        // ROFL https://stackoverflow.com/questions/30088649/how-to-use-multiple-join-fetch-in-one-jpql-query
        // userid 392

        Query q = entityManager.
                createQuery(
//                        "SELECT u FROM User u JOIN FETCH u.posts JOIN FETCH u.votes JOIN FETCH u.badges WHERE u.id = 392 ");
                        "SELECT u FROM User u WHERE u.id = 392 ");

        User user = (User) q.getSingleResult();

        //User user = users.get(0);
        out("posts: " + user.getPosts().size() + " votes: " + user.getVotes().size() + " badges: " + user.getBadges().size() + " " + user);
    }

    void out(Object text) {
        long newNan = (System.nanoTime() - now);
        long newMil = newNan / 1000000;

        System.out.println("TIME:  " + newNan + " (" + newMil + ") " + text);
        now = System.nanoTime();
    }


}
