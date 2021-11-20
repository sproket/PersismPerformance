package net.sf.persism;

import net.sf.persism.perf.models.FullUser;
import net.sf.persism.perf.models.Post;
import net.sf.persism.perf.models.User;
import net.sf.persism.perf.models.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import static net.sf.persism.NadaPrintStream.out;
import static net.sf.persism.Parameters.params;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJDBC {

    Connection con;
    long now;
    Session session;

    @BeforeEach
    public void setup() throws Exception {
        now = System.currentTimeMillis();

        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        out.println(url);

        con = DriverManager.getConnection(url);
        session = new Session(con);
        out("before session");
        out("setup");
    }

    @Test
    public void testUserAndVotes() throws Exception {
        out("testUserAndVotes");
        User user = session.fetch(User.class, params(9));
        assertNotNull(user);
        out("testUserAndVotes " + user);

        // select count(*) from votes  where UserId = 4918
        FullUser fullUser = (FullUser) session.fetch(FullUser.class, params(4918));
        out("time?");

        assertNotNull(fullUser);
        System.out.println(fullUser.getVotes().size());
        System.out.println(fullUser.getPosts().size());

        assertTrue(fullUser.getVotes().size() > 0);
        assertTrue(fullUser.getPosts().size() > 0);

    }

    void out(Object text) {
        System.out.println(text + " " + (System.currentTimeMillis() - now));
        now = System.currentTimeMillis();
    }

    static class Session {
        Connection con;

        public Session(Connection con) {
            this.con = con;
        }

        public <T extends User> User fetch(Class<T> objectClass, Parameters params) throws Exception {

            boolean fullUser = objectClass.isAssignableFrom(FullUser.class);

            PreparedStatement st = con.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
            st.setObject(1, params.get(0));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User user;
                if (fullUser) {
                    user = new FullUser();
                } else {
                    user = new User();
                }
                user.setId(rs.getInt("id"));
                user.setAboutMe(rs.getString("aboutMe"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setAccountId(rs.getInt("accountId"));
                user.setAge(rs.getInt("age"));
                user.setDisplayName(rs.getString("displayName"));
                user.setDownVotes(rs.getInt("downVotes"));
                user.setLocation(rs.getString("location"));
                user.setEmailHash(rs.getString("emailHash"));
                user.setLastAccessDate(rs.getDate("lastAccessDate"));
                user.setReputation(rs.getInt("reputation"));
                user.setUpVotes(rs.getInt("upVotes"));
                user.setViews(rs.getInt("views"));
                user.setWebsiteUrl(rs.getString("websiteUrl"));

                if (fullUser) {
                    FullUser fuser = (FullUser) user;
                    List<Vote> votes = fuser.getVotes();

                    st = con.prepareStatement("SELECT * FROM Votes WHERE UserId = ?");
                    st.setObject(1, params.get(0));
                    rs = st.executeQuery();
                    while (rs.next()) {
                        Vote vote = new Vote(
                                rs.getInt("id"),
                                rs.getInt("postId"),
                                rs.getInt("userId"),
                                rs.getInt("bountyAmount"),
                                rs.getInt("voteTypeId"),
                                rs.getDate("creationDate"));

                        votes.add(vote);
                    }

                    List<Post> posts = fuser.getPosts();

                    st = con.prepareStatement("SELECT * FROM Posts WHERE OwnerUserId = ?");
                    st.setObject(1, params.get(0));
                    rs = st.executeQuery();
                    while (rs.next()) {
                        Post post = new Post(
                                rs.getInt("id"),
                                rs.getInt("acceptedAnswerId"),
                                rs.getInt("answerCount"),
                                rs.getString("body"),
                                rs.getDate("closedDate"),
                                rs.getInt("commentCount"),
                                rs.getDate("communityOwnedDate"),
                                rs.getDate("creationDate"),
                                rs.getInt("favoriteCount"),
                                rs.getDate("lastActivityDate"),
                                rs.getDate("lastEditDate"),
                                rs.getString("lastEditorDisplayName"),
                                rs.getInt("lastEditorUserId"),
                                rs.getInt("ownerUserId"),
                                rs.getInt("parentId"),
                                rs.getInt("postTypeId"),
                                rs.getInt("score"),
                                rs.getString("tags"),
                                rs.getString("title"),
                                rs.getInt("viewCount")
                        );

                        posts.add(post);
                    }


                }

                return user;
            }


            return null;
        }

    }
}
