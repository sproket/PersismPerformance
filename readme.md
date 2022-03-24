This project is for unit testing for performance between JDBC, Persism and JPA/hibernate.

I'm using the database from StackOverflow (2010 version) which should large enough to get good numbers.

https://www.brentozar.com/archive/2015/10/how-to-download-the-stack-overflow-database-via-bittorrent/

If anyone wants to give this a run:

* attach the StackOverflow database
* modify the```datasource.properties``` file
* check BaseTest and make sure the field```dropAndRecreate``` is set to true
* Run AllTests
* Run DashboardApplication to view results

All data is stored in a table called ```[PerfTests]```

Stay tuned!