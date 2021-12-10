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


-- Fetch single Post
SELECT [Id], [AcceptedAnswerId], [AnswerCount], [Body], [ClosedDate], [CommentCount], [CommunityOwnedDate], [CreationDate], 
[FavoriteCount], [LastActivityDate], [LastEditDate], [LastEditorDisplayName], [LastEditorUserId], [OwnerUserId], 
[ParentId], [PostTypeId], [Score], [Tags], [Title], [ViewCount] 
FROM 
[Posts] WHERE [Id] = 4

SELECT [Id], [AboutMe], [Age], [CreationDate], [DisplayName], [DownVotes], [EmailHash], [LastAccessDate], [Location], 
[Reputation], [UpVotes], [Views], [WebsiteUrl], [AccountId] 
FROM [Users] 
WHERE [Id] IN (SELECT OwnerUserId FROM Posts WHERE [Id] = 4)



-- Additional TABLE
-- code for UserCommentXref
DROP TABLE UserCommentXref

GO
CREATE TABLE [dbo].[UserCommentXref](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[UserId] [int] NULL,
	[PostId] [int] NOT NULL,
 CONSTRAINT [PK_UserCommentXref] PRIMARY KEY CLUSTERED 
(
[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

insert into UserCommentXref (UserId, PostId) select userId, postId  from Comments -- where UserId is not null
GO

select UserId, PostId, COUNT(PostID) from UserCommentXref group by UserId, PostId order by COUNT(PostID) desc
------------------

-- Added index to Comments

/****** Object:  Index [NonClusteredIndex-20211125-060040]    Script Date: 11/25/2021 6:02:09 AM ******/
CREATE NONCLUSTERED INDEX [NonClusteredIndex-20211125-060040] ON [dbo].[Comments]
(
	[UserId] ASC,
	[PostId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO


Added to XREF 

	CREATE NONCLUSTERED INDEX [_dta_index_UserCommentXref_11_1157579162__K3_K2] ON [dbo].[UserCommentXref]
(
	[PostId] ASC,
	[UserId] ASC
)WITH (SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF) ON [PRIMARY]


ADDED INDEXES


/****** Object:  Index [IX_UserId]    Script Date: 11/25/2021 1:59:39 PM ******/
CREATE NONCLUSTERED INDEX [IX_UserId] ON [dbo].[Votes]
(
	[UserId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO

CREATE NONCLUSTERED INDEX [IX_OwnerUserId] ON [dbo].[Posts]
(
	[OwnerUserId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO




