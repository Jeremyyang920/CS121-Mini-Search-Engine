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

### Index Construction
- Key Statistics
  - Number of Documents: **37,497**
  - Number of Unique Tokens: **800,888**
  - Total Size of Index: **155 MB (155,000 KB)**
- Additional Information
  - We stored our index in **75** files. Each *.ser* file corresponds to a folder (i.e. *0.ser* represents all the documents inside the *0* directory).
  - To reduce the time it takes to index the dataset/corpus, we used multiple threads. To efficiently store all of the document information, we built and inserted the data into a HashMap.
  
### Example Input/Output
```
Enter Query: anuj
www.ics.uci.edu/community/news/view_news?id=1066: 1 hit(s)

Enter Query: anuj shah
www.ics.uci.edu/community/news/view_news?id=1066: 18.74462230450985
www.ics.uci.edu/community/alumni/mentor//../../../../about/search/search_graduate_all.php: 15.136463785866656
www.ics.uci.edu/community/news/spotlight/spotlight_shah.php: 15.136463785866656
www.ics.uci.edu/~thornton/ics45c/CourseReference.html: 15.136463785866656
vision.ics.uci.edu/papers/Sangmin_CVPR_2011: 15.136463785866656

Enter Query: ACM
www.ics.uci.edu/~eppstein/bibs/eppstein.bib: 114 hit(s)
www.ics.uci.edu/~eppstein/bibs/eppstein.html: 114 hit(s)
www.ics.uci.edu/~eppstein/pubs/pubs.ff: 112 hit(s)
ibook.ics.uci.edu/references.html: 74 hit(s)
www.ics.uci.edu/~eppstein/pubs/geom-all.html: 71 hit(s)

Enter Query: crista lopes
www.ics.uci.edu/community/news/features/view_feature?id=67: 29.662925430391827
www.ics.uci.edu/community/news/notes/notes_2014.php: 28.065603217866613
www.ics.uci.edu/community/news/notes/notes_2010.php: 25.739774893858453
www.ics.uci.edu/community/news/features/view_feature?id=89: 25.044785040208602
www.ics.uci.edu/community/news/notes/notes_2013.php: 24.457985203054612

Enter Query: machine learning
cml.ics.uci.edu/category/aiml: 19.604839464977665
www.ics.uci.edu/~pazzani/Publications/OldPublications.html: 18.45114860547814
www.ics.uci.edu/~pazzani/Publications/APubs.html: 18.406583355915828
ibook.ics.uci.edu/references.html: 17.354481746498987
cml.ics.uci.edu/category/aiml/page/2: 16.7763577826483

Enter Query: richard pattis
www.ics.uci.edu/~kay/courses/i42/wildride/data/1000customers.txt: 47.582820035330236
www.ics.uci.edu/~pattis/quotations.html: 32.44993130955071
archive.ics.uci.edu/ml/machine-learning-databases/movies-mld/data/actors.html: 26.881468802416773
www.ics.uci.edu/~taylor/Publications.htm: 25.624559621596024
www.ics.uci.edu/~pattis: 20.60639992247456

Enter Query: alpha beta pruning
mondego.ics.uci.edu/datasets/maven-contents.txt: 55.80326172451126
www.ics.uci.edu/~eppstein/180a/970422.html: 50.649418023586406
www.ics.uci.edu/~eppstein/180a/990204.html: 44.14978579312477
gonet.genomics.ics.uci.edu/pgo/p2_293_.txt: 42.17387801975056
gonet.genomics.ics.uci.edu/fgo/f1_6_.txt: 38.43691169227589

Enter Query: recursion
www.ics.uci.edu/~kay/courses/i42/wildride/data/1000customers.txt: 477 hit(s)
www.ics.uci.edu/~pattis/ICS-46/lectures/notes/recursion.txt: 33 hit(s)
www.ics.uci.edu/~pattis/ICS-33/lectures/recursion.txt: 30 hit(s)
www.ics.uci.edu/~kay/courses/i42/wildride/data/customers2.txt: 27 hit(s)
www.ics.uci.edu/~pattis/ICS-33/lectures/functionalprogramming.txt: 13 hit(s)

Enter Query: alexander thomas ihler
www.ics.uci.edu/~ihler/papers/bib.html: 48.99304239330009
archive.ics.uci.edu/ml/machine-learning-databases/movies-mld/data/actors.html: 43.87848728706223
sli.ics.uci.edu/Pubs/Bibliography: 36.59255254291315
fano.ics.uci.edu/cites/Author: 35.41550506502074
www.ics.uci.edu/~ihler/pubs.html: 33.747109817416145

Enter Query: boo thornton
www.ics.uci.edu/~thornton/ics45c/ProjectGuide/Project2: 46.09251690516345
www.ics.uci.edu/~thornton/ics45c/ProjectGuide/Project3: 29.576749040331922
www.ics.uci.edu/~thornton/ics32/ProjectGuide/Project2: 28.619835216975822
www.ics.uci.edu/~thornton/ics32/ProjectGuide/Project1: 28.203195155198856
www.ics.uci.edu/~thornton/inf43/CourseProject/Testing: 25.102001527212003

Enter Query: !quit
Program terminated.
```
