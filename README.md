# Mini Search Engine
## Written in Java using Eclipse

### Description
This search engine allows the user to search for webpages on the ICS domain (http://www.ics.uci.edu).
- Each webpage is tokenized and stored in an inverted index (words are mapped to documents). 

### How to Run
- Run *Tokens.sql* to create the database tables.
- Run *Indexer.java* to build the index.
- Run *HeaderIndexer.java* to keep track of titles and other important HTML content.
- Run *Deserialize.java* to put the index in one serialized file.
- Run *DBConnectionTest.java* to store the index in a MySQL database.
- Run *Searcher.java* or *SearcherDB.java* to start the search engine.
  - The user can input a query and the search engine will return the best results.
    - Webpages are ranked/scored using TF-IDF.
      - The top 5 documents with the highest TF-IDF scores are shown.
  - The user can input *!quit* to terminate the program.
