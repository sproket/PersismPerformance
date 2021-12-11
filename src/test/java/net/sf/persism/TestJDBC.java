package net.sf.persism;

import net.sf.persism.perf.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static net.sf.persism.Parameters.params;
import static net.sf.persism.SQL.where;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJDBC {

    Connection con;
    static long now;
    Session session;

    @BeforeEach
    public void setup() throws Exception {
        now = System.nanoTime();

        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/datasource.properties"));
        Class.forName(props.getProperty("database.driver"));

        String url = props.getProperty("database.url");
        System.out.println(url);

        con = DriverManager.getConnection(url);
        session = new Session(con);
        out("before session");
        out("setup");

        var d1 = LocalDate.now();
        var d2 = LocalDate.now();

    }

    @RepeatedTest(2)
    public void testJunk() {
        // MOHS47572313 -- for woman add 50 to month
        var ramq = "MOHS47072313";
        System.out.println(ramq);
        var month = ramq.substring(6,8);
        System.out.println(month);
        int x = Integer.parseInt(month);
        x += 50;
        month = ""+x;
        ramq = ramq.substring(0,6) + month + ramq.substring(8);
        System.out.println(ramq);



    }

    @RepeatedTest(2)
    public void testAllFullAutoUsers() throws Exception {

        List<FullAutoUser> fullAutoUsers = session.query(FullAutoUser.class, where("[id] < 1000"));
        System.out.println("full auto users count: " + fullAutoUsers.size());

        assertNotNull(fullAutoUsers.get(0).getPosts().get(0).getUser());

        out("TIME?");
    }


    @RepeatedTest(2)
    public void testPosts() throws Exception {
        out("testPosts PERSISM TIME: 1315 SIZE: 62710 ");
        String sql = """
                    SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], 
                    [CommentCount], [CommunityOwnedDate], [CreationDate], [FavoriteCount], [LastActivityDate], 
                    [LastEditDate], [LastEditorDisplayName], [LastEditorUserId], [OwnerUserId], [ParentId], 
                    [PostTypeId], [Score], [Tags], [Title], [ViewCount] FROM [Posts] 
                    WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)                
                """;

        List<Post> posts = new ArrayList<>();

        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
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
        out("testPosts size: " + posts.size());
    }

    @RepeatedTest(2)
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

    static void out(Object text) {
        long newNan = (System.nanoTime() - now);
        long newMil = newNan / 1000000;

        System.out.println(text + " " + newNan +" (" + newMil + ")");
        now = System.nanoTime();
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

        /*
        SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate],
        [Location], [Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId]
        FROM [Users]
        WHERE [Id] < 1000


        SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], [CommentCount], [CommunityOwnedDate],
        [CreationDate], [FavoriteCount], [LastActivityDate], [LastEditDate], [LastEditorDisplayName], [LastEditorUserId],
        [OwnerUserId], [ParentId], [PostTypeId], [Score], [Tags], [Title], [ViewCount]
        FROM [Posts]
        WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)

        SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate],
        [Location], [Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId]
        FROM [Users]
        WHERE [Id] IN (SELECT OwnerUserId FROM Posts WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000))

        SELECT [Id], [PostId], [UserId], [BountyAmount], [VoteTypeId], [CreationDate]
        FROM [Votes]
        WHERE [UserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)

         */
        public List<FullAutoUser> query(Class<FullAutoUser> fullAutoUserClass, SQL where) throws Exception {

            List<FullAutoUser> users = new ArrayList<>();
            String sql = """
                    SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate],\s
                    [Location], [Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId]\s
                    FROM [Users]
                    """;
            sql += " " + where;
            System.out.println(sql);

            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                FullAutoUser user = new FullAutoUser();
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

                users.add(user);
            }

            sql = """
                    SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], [CommentCount], [CommunityOwnedDate],\s
                    [CreationDate], [FavoriteCount], [LastActivityDate], [LastEditDate], [LastEditorDisplayName], [LastEditorUserId],\s
                    [OwnerUserId], [ParentId], [PostTypeId], [Score], [Tags], [Title], [ViewCount]\s
                    FROM [Posts]\s
                    WHERE [OwnerUserId] IN (SELECT Id FROM Users %s)
                    """;
            sql = String.format(sql, where);

            System.out.println(sql);

            List<Post> posts = new ArrayList<>();
            st = con.prepareStatement(sql);
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
            out("get posts");

            Map<Integer, FullAutoUser> userParentMap;
            userParentMap = users.stream().collect(Collectors.toMap(User::getId, o -> o, (o1, o2) -> o1));
            for (Post post : posts) {
                FullAutoUser parent = userParentMap.get(post.getOwnerUserId());
                parent.getPosts().add(post);
            }
            out("stitch 1");

            // posts queries users again
            sql = """
                    SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate],
                    [Location], [Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId]
                    FROM [Users]
                    WHERE [Id] IN (SELECT OwnerUserId FROM Posts WHERE [OwnerUserId] IN (SELECT Id FROM Users %s))
                    """;

            sql = String.format(sql, where);

            System.out.println(sql);

            List<User> postUsers = new ArrayList<>();
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                User user = new User();
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

                postUsers.add(user);
            }
            out("users from posts");

            Map<Integer, Post> postParentMap;
            postParentMap = posts.stream().collect(Collectors.toMap(Post::getOwnerUserId, o -> o, (o1, o2) -> o1));
            for (User postUser : postUsers) {
                Post post = postParentMap.get(postUser.getId());
                post.setUser(postUser);
            }
            out("stitch 2");

            // votes
            sql = """
                    SELECT [Id], [PostId], [UserId], [BountyAmount], [VoteTypeId], [CreationDate]
                    FROM [Votes]
                    WHERE [UserId] IN (SELECT Id FROM Users %s)
                    """;

            sql = String.format(sql, where);

            System.out.println(sql);

            List<Vote> votes = new ArrayList<>();
            st = con.prepareStatement(sql);
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
            out("get votes");

            userParentMap = users.stream().collect(Collectors.toMap(User::getId, o -> o, (o1, o2) -> o1));
            for (Vote vote : votes) {
                FullAutoUser parent = userParentMap.get(vote.getUserId());
                parent.getVotes().add(vote);
            }
            out("stitch 3");

            return users;
        }
    }

}