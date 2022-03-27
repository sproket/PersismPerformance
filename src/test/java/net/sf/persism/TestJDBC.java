package net.sf.persism;

import net.sf.persism.perf.Category;
import net.sf.persism.perf.models.*;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.sf.persism.perf.Category.Other;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestJDBC extends BaseTest implements ITests {

    private static final Log log = Log.getLogger(TestJDBC.class);

    Connection con;

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            testClassName = description.getTestClass().getSimpleName();
            testMethodName = description.getMethodName();
        }
    };

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        perfStart();
        con = getConnection();
        perfEnd(Category.Setup, "SETUP: get Connection");
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
    public void testMultiplePosts() throws Exception {
        perfStart();
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
                    rs.getTimestamp("closedDate"),
                    rs.getInt("commentCount"),
                    rs.getTimestamp("communityOwnedDate"),
                    rs.getTimestamp("creationDate"),
                    rs.getInt("favoriteCount"),
                    rs.getTimestamp("lastActivityDate"),
                    rs.getTimestamp("lastEditDate"),
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

            // todo joins....
        }
        perfEnd(Category.Result, "testPosts size: " + posts.size());
    }

    @Test
    public void testSingleUser() throws Exception {
        int userId = 4918;

        perfStart();

        PreparedStatement st = con.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
        st.setObject(1, userId);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            ExtendedUser user = new ExtendedUser();
            user.setId(rs.getInt("id"));
            user.setAboutMe(rs.getString("aboutMe"));
            user.setCreationDate(rs.getTimestamp("creationDate"));
            user.setAccountId(rs.getInt("accountId"));
            user.setAge(rs.getInt("age"));
            user.setDisplayName(rs.getString("displayName"));
            user.setDownVotes(rs.getInt("downVotes"));
            user.setLocation(rs.getString("location"));
            user.setEmailHash(rs.getString("emailHash"));
            user.setLastAccessDate(rs.getTimestamp("lastAccessDate"));
            user.setReputation(rs.getInt("reputation"));
            user.setUpVotes(rs.getInt("upVotes"));
            user.setViews(rs.getInt("views"));
            user.setWebsiteUrl(rs.getString("websiteUrl"));

            List<Vote> votes = user.getVotes();

            st = con.prepareStatement("SELECT * FROM Votes WHERE UserId = ?");
            st.setObject(1, userId);
            rs = st.executeQuery();
            while (rs.next()) {
                Vote vote = new Vote(
                        rs.getInt("id"),
                        rs.getInt("postId"),
                        rs.getInt("userId"),
                        rs.getInt("bountyAmount"),
                        rs.getInt("voteTypeId"),
                        rs.getTimestamp("creationDate"));

                votes.add(vote);
            }

            List<Post> posts = user.getPosts();

            st = con.prepareStatement("SELECT * FROM Posts WHERE OwnerUserId = ?");
            st.setObject(1, userId);
            rs = st.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("id"),
                        rs.getInt("acceptedAnswerId"),
                        rs.getInt("answerCount"),
                        rs.getString("body"),
                        rs.getTimestamp("closedDate"),
                        rs.getInt("commentCount"),
                        rs.getTimestamp("communityOwnedDate"),
                        rs.getTimestamp("creationDate"),
                        rs.getInt("favoriteCount"),
                        rs.getTimestamp("lastActivityDate"),
                        rs.getTimestamp("lastEditDate"),
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

                List<Badge> badges = user.getBadges();

                st = con.prepareStatement("SELECT * FROM Badges WHERE UserId = ?");
                st.setObject(1, userId);
                rs = st.executeQuery();
                while (rs.next()) {
                    Badge badge = new Badge();
                    badge.setUserId(rs.getInt("UserId"));
                    badge.setId(rs.getInt("id"));
                    badge.setName(rs.getString("name"));
                    badge.setDate(rs.getTimestamp("date"));

                    badges.add(badge);
                }
            }

            User userForPosts;
            st = con.prepareStatement("SELECT * FROM USERS WHERE ID = ?");
            st.setObject(1, user.getId());
            rs = st.executeQuery();
            if (rs.next()) {
                userForPosts = new User();
                userForPosts.setId(rs.getInt("id"));
                userForPosts.setAboutMe(rs.getString("aboutMe"));
                userForPosts.setCreationDate(rs.getTimestamp("creationDate"));
                userForPosts.setAccountId(rs.getInt("accountId"));
                userForPosts.setAge(rs.getInt("age"));
                userForPosts.setDisplayName(rs.getString("displayName"));
                userForPosts.setDownVotes(rs.getInt("downVotes"));
                userForPosts.setLocation(rs.getString("location"));
                userForPosts.setEmailHash(rs.getString("emailHash"));
                userForPosts.setLastAccessDate(rs.getTimestamp("lastAccessDate"));
                userForPosts.setReputation(rs.getInt("reputation"));
                userForPosts.setUpVotes(rs.getInt("upVotes"));
                userForPosts.setViews(rs.getInt("views"));
                userForPosts.setWebsiteUrl(rs.getString("websiteUrl"));

                posts.forEach(p -> p.setUser(userForPosts));
            } else {
                fail("no results");
            }

            perfEnd(Category.Result, "votes: " + user.getVotes().size() + " posts: " + user.getPosts().size() + " badges: " + user.getBadges().size());

            assertNotNull(user);
            assertTrue(user.getVotes().size() > 0);
            assertTrue(user.getPosts().size() > 0);
            assertTrue(user.getBadges().size() > 0);

            assertEquals(user.getDisplayName(), user.getPosts().get(0).getUser().getDisplayName());
        } else {
            fail("no results");
        }
    }

    @Test
    public void testMultipleUsers() throws Exception {

        perfStart();
        List<ExtendedUser> users = new ArrayList<>();
        String sql = """
                SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate],\s
                [Location], [Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId]\s
                FROM [Users]
                WHERE [Id] < 1000
                """;
        System.out.println(sql);

        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            ExtendedUser user = new ExtendedUser();
            user.setId(rs.getInt("id"));
            user.setAboutMe(rs.getString("aboutMe"));
            user.setCreationDate(rs.getTimestamp("creationDate"));
            user.setAccountId(rs.getInt("accountId"));
            user.setAge(rs.getInt("age"));
            user.setDisplayName(rs.getString("displayName"));
            user.setDownVotes(rs.getInt("downVotes"));
            user.setLocation(rs.getString("location"));
            user.setEmailHash(rs.getString("emailHash"));
            user.setLastAccessDate(rs.getTimestamp("lastAccessDate"));
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
                WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)
                """;
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
                    rs.getTimestamp("closedDate"),
                    rs.getInt("commentCount"),
                    rs.getTimestamp("communityOwnedDate"),
                    rs.getTimestamp("creationDate"),
                    rs.getInt("favoriteCount"),
                    rs.getTimestamp("lastActivityDate"),
                    rs.getTimestamp("lastEditDate"),
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
        perfEnd(Other, "get posts");

        Map<Integer, ExtendedUser> userParentMap;
        userParentMap = users.stream().collect(Collectors.toMap(User::getId, o -> o, (o1, o2) -> o1));
        for (Post post : posts) {
            ExtendedUser parent = userParentMap.get(post.getOwnerUserId());
            parent.getPosts().add(post);
        }
        perfEnd(Other, "stitch 1");

        // posts queries users again
        sql = """
                SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate],
                [Location], [Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId]
                FROM [Users]
                WHERE [Id] IN (SELECT OwnerUserId FROM Posts WHERE [OwnerUserId] IN (SELECT Id FROM Users WHERE [Id] < 1000))
                """;
        System.out.println(sql);

        List<User> postUsers = new ArrayList<>();
        st = con.prepareStatement(sql);
        rs = st.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setAboutMe(rs.getString("aboutMe"));
            user.setCreationDate(rs.getTimestamp("creationDate"));
            user.setAccountId(rs.getInt("accountId"));
            user.setAge(rs.getInt("age"));
            user.setDisplayName(rs.getString("displayName"));
            user.setDownVotes(rs.getInt("downVotes"));
            user.setLocation(rs.getString("location"));
            user.setEmailHash(rs.getString("emailHash"));
            user.setLastAccessDate(rs.getTimestamp("lastAccessDate"));
            user.setReputation(rs.getInt("reputation"));
            user.setUpVotes(rs.getInt("upVotes"));
            user.setViews(rs.getInt("views"));
            user.setWebsiteUrl(rs.getString("websiteUrl"));

            postUsers.add(user);
        }
        perfEnd(Other, "users from posts");

        Map<Integer, Post> postParentMap;
        postParentMap = posts.stream().collect(Collectors.toMap(Post::getOwnerUserId, o -> o, (o1, o2) -> o1));
        for (User postUser : postUsers) {
            Post post = postParentMap.get(postUser.getId());
            post.setUser(postUser);
        }
        perfEnd(Other, "stitch 2");

        // votes
        sql = """
                SELECT [Id], [PostId], [UserId], [BountyAmount], [VoteTypeId], [CreationDate]
                FROM [Votes]
                WHERE [UserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)
                """;
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
                    rs.getTimestamp("creationDate"));

            votes.add(vote);
        }
        perfEnd(Other, "get votes");

        userParentMap = users.stream().collect(Collectors.toMap(User::getId, o -> o, (o1, o2) -> o1));
        for (Vote vote : votes) {
            ExtendedUser parent = userParentMap.get(vote.getUserId());
            parent.getVotes().add(vote);
        }
        perfEnd(Other, "stitch 3");


        // badges
        sql = """
                SELECT *
                FROM [Badges]
                WHERE [UserId] IN (SELECT Id FROM Users WHERE [Id] < 1000)
                """;
        System.out.println(sql);

        List<Badge> badges = new ArrayList<>();
        st = con.prepareStatement(sql);
        rs = st.executeQuery();
        while (rs.next()) {
            Badge badge = new Badge(
                    rs.getInt("id"),
                    rs.getString("Name"),
                    rs.getInt("userId"),
                    rs.getTimestamp("Date"));

            badges.add(badge);
        }
        perfEnd(Other, "get votes");

        userParentMap = users.stream().collect(Collectors.toMap(User::getId, o -> o, (o1, o2) -> o1));
        for (Badge badge : badges) {
            ExtendedUser parent = userParentMap.get(badge.getUserId());
            parent.getBadges().add(badge);
        }
        perfEnd(Other, "stitch 4");
        perfEnd(Category.Result, "users: " + users.size());

        assertTrue(users.size() > 0);
        ExtendedUser user = users.get(10);
        assertTrue(user.getVotes().size() > 0);
        assertTrue(user.getBadges().size() > 0);
        assertTrue(user.getPosts().size() > 0);
        assertNotNull(user.getPosts().get(0).getUser());
        assertEquals(user.getDisplayName(), user.getPosts().get(0).getUser().getDisplayName());
    }

    @Test
    public void testSinglePost() throws Exception {
        perfStart();
        Post post = null;
        PreparedStatement st = con.prepareStatement("SELECT * FROM Posts WHERE ID=?");
        st.setObject(1, 4435775);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            post = new Post(
                    rs.getInt("id"),
                    rs.getInt("acceptedAnswerId"),
                    rs.getInt("answerCount"),
                    rs.getString("body"),
                    rs.getTimestamp("closedDate"),
                    rs.getInt("commentCount"),
                    rs.getTimestamp("communityOwnedDate"),
                    rs.getTimestamp("creationDate"),
                    rs.getInt("favoriteCount"),
                    rs.getTimestamp("lastActivityDate"),
                    rs.getTimestamp("lastEditDate"),
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

            st = con.prepareStatement("select * from Users where Id = ?");
            st.setObject(1, post.getOwnerUserId());
            rs = st.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("aboutMe"),
                        rs.getInt("age"),
                        rs.getTimestamp("creationDate"),
                        rs.getString("displayName"),
                        rs.getInt("downVotes"),
                        rs.getString("emailHash"),
                        rs.getTimestamp("lastAccessDate"),
                        rs.getString("location"),
                        rs.getInt("reputation"),
                        rs.getInt("upVotes"),
                        rs.getInt("views"),
                        rs.getString("websiteUrl"),
                        rs.getInt("accountId")
                );
                post.setUser(user);
            }

            st = con.prepareStatement("select * from Comments where PostId = ? and UserId = ?");
            st.setObject(1, post.getId());
            st.setObject(2, post.getOwnerUserId());
            rs = st.executeQuery();
            post.setComments(new ArrayList<>());
            while (rs.next()) {
                Comment comment = new Comment(
                        rs.getInt("Id"),
                        rs.getTimestamp("creationDate"),
                        rs.getInt("postId"),
                        rs.getInt("score"),
                        rs.getString("text"),
                        rs.getInt("userId")
                );
                post.getComments().add(comment);
            }

            st = con.prepareStatement("select * from PostTypes where Id = ?");
            st.setObject(1, post.getPostTypeId());
            rs = st.executeQuery();
            if (rs.next()) {
                PostType pt = new PostType();
                pt.setId(rs.getInt("Id"));
                pt.setType(rs.getString("Type"));
                post.setPostType(pt);
            }

        }
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
        PreparedStatement st = con.prepareStatement("SELECT * FROM BADGES");
        ResultSet rs = st.executeQuery();
        List<Badge> badges = new ArrayList<>();
        while (rs.next()) {
            Badge badge = new Badge();
            badge.setId(rs.getInt("Id"));
            badge.setName(rs.getString("Name"));
            badge.setUserId(rs.getInt("UserId"));
            badge.setDate(rs.getTimestamp("Date"));
            badges.add(badge);
        }

        perfEnd(Category.Result, "badges size: " + badges.size());
    }
}
