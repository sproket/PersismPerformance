package net.sf.persism;

import net.sf.persism.perf.models.*;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static net.sf.persism.Parameters.params;
import static net.sf.persism.SQL.where;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJDBC extends BaseTest implements ITests {

    private static final Log log = Log.getLogger(TestJDBC.class);

    Connection con;
    Session session;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            testClassName = description.getClassName();
            testMethodName = description.getMethodName();
        }
    };

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

    @After
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
    public void testAllFullAutoUsers() throws Exception {
        reset();

        List<FullAutoUser> fullAutoUsers = session.query(FullAutoUser.class, where("[id] < 1000"));
        System.out.println("full auto users count: " + fullAutoUsers.size());

        assertNotNull(fullAutoUsers.get(0).getPosts().get(0).getUser());

        out("TIME?");
    }

    @Test
    public void testPostsQuery() throws Exception {
        reset();
        log.info("testPosts PERSISM TIME: 1315 SIZE: 62710 ");
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

    @Test
    public void testUserAndVotes() throws Exception {
        reset();
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

    @Test
    public void testGetMultipleResultSets() throws Exception {
        String sql = """
                SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate],\s
                [Location], [Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId]\s
                FROM [Users]\s
                WHERE [Id] < 1000\s
                                
                                
                SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], [CommentCount], [CommunityOwnedDate],\s
                [CreationDate], [FavoriteCount], [LastActivityDate], [LastEditDate], [LastEditorDisplayName], [LastEditorUserId],\s
                [OwnerUserId], [ParentId], [PostTypeId], [Score], [Tags], [Title], [ViewCount]\s
                FROM [Posts]\s
                WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)
                                
                SELECT [Id], [PostId], [UserId], [BountyAmount], [VoteTypeId], [CreationDate]\s
                FROM [Votes]\s
                WHERE [UserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)
                                
                """;

        try (Statement st = con.createStatement()) {
            int n = 0;
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            System.out.println(rs.getMetaData().getColumnLabel(2));
            n++;
            while(st.getMoreResults()) {
                rs = st.getResultSet();
                System.out.println(rs.getMetaData().getColumnLabel(2));
                System.out.println(++n);
            }
            System.out.println(n);
        }
    }

    @Test
    public void testAllFullUsers() {
        reset();

    }

    @Test
    public void testFetchComments() {
        reset();

    }

    @Test
    public void testFetchPost() {
        reset();

    }

    @Test
    public void testUsersSingleWithFetch() {
        reset();

    }


    // get rid of this
    class Session {
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