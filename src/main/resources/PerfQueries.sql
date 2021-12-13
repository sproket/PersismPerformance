USE [StackOverflow2010]
GO

SELECT *, (Timing / 1000 / 1000) as MS FROM PerfTests
--where TestMethod = 'testPostsQuery' and TestText like '%testPosts size:%'
order by TestClass, TestMethod


