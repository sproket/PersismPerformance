USE [StackOverflow2010]
GO

SELECT * FROM PerfTests where Category = 'Result' --and TestMethod = 'testPostsQuery'
order by TestClass, TestMethod


SELECT * FROM PerfTests
where TestMethod = 'testQueryAllBadges' and TestText like '%badges size:%'
order by TestClass, TestMethod


SELECT * FROM PerfTests
where TestMethod = 'testQueryAllBadges' and TestText like '%badges%' and TestClass='net.sf.persism.TestPersism'
order by StartTime, TestClass, TestMethod

SELECT * FROM PerfTests
WHERE  Category = 'Result' and TESTMETHOD = 'testExtendedUsers' and TestText like '% testExtendedUsers:%'
order by TestClass, TestMethod

/*
199	net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2380753700 (2380) badges size: 1102019	2380753700	2021-12-13 14:12:04.903	2380
53	net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2557254500 (2557) badges size: 1102019	2557254500	2021-12-13 14:11:43.787	2557
126	net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2416020300 (2416) badges size: 1102019	2416020300	2021-12-13 14:11:54.693	2416


WTF!!!!!!!!!!!!!!!!!!!!!!!!!!!!
net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2367975800 (2367) badges size: 1102019	2367975800	2021-12-13 14:13:24.933	2367
net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2208079000 (2208) badges size: 1102019	2208079000	2021-12-13 14:13:35.680	2208
net.sf.persism.TestPersism	testQueryAllBadges	TIME:  2306186800 (2306) badges size: 1102019	2306186800	2021-12-13 14:13:45.257	2306
*/

