## Database and API
* Periodic update announcements [here](groups.google.com/forum/#!forum/sanskrit-programmers).

### Couchdb
### API-s
* Querying quote entries: use the  database and the index_headwords index
  * Get a particular entry: [link](http://vedavaapi.org:5984/quote_db__sa/astaabhimukhesuuryeuditesampuurmamamDalechamdregamamambudhasyalagmeuditaastamitechaketau).
  * Get 20 some quotes starting with ast: [link](http://vedavaapi.org:5984/quote_db__sa/_all_docs?inclusive_end=false&start_key=%22ast%22&limit=20&inclusive_end=true) .


#### Database replicas
* You want to host a repilica and make things faster for folks in your geographical area? Just open an issue in this project and let us know.
* Ahmedabad, IN <http://vedavaapi.org:5984/quote_db__sa/_all_docs>
* Bay area, USA (dev machine, unstable) <http://vvasuki.hopto.org:5984/quote_db__sa/_all_docs>


### Technical choices
* Use Nosql rather than a relational database as the primary canonical database.
  * Reasons (from [here](https://www.couchbase.com/nosql-resources/why-nosql) ):
    * The need to develop with agility
      * Simplicity (less need to join n tables)
      * Flexibility (schema can easily be changed)
    * distributed nature: ability to operate at any scale.
      * Database copies can sync with each other easily - no centralization is good.
  * Comparison [here](http://db-engines.com/en/system/Cassandra%3BCouchbase%3BMongoDB%3BRedis) .
  * Alternatives for mobile offline use:
    * Couchbase database, since it can be [used in many](https://developer.couchbase.com/documentation/mobile/1.4/training/develop/using-the-database/index.html) mobile OS-s.
      * Comparison with mongodb: [on cb site](https://www.couchbase.com/comparing-couchbase-vs-mongodb).
    * Realm.io - good for storing plain Java objects via JSON.
    * Google Firebase for key-value storage - 1GB free.
* Scala for the ingestion libraries (same justification as expressed [here](https://github.com/sanskrit-coders/sanskritnlpjava/blob/master/README.md#scala) ).


## Code Contribution
### Links to general comments
See [indic-transliteration/README](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md) for the following info:

  - [Setup](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md#setup)
  - [Deployment](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md#deployment)
    - [Regarding **maven targets** in intellij](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md#regarding-**maven-targets**-in-intellij)
    - [Releasing to maven.](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md#releasing-to-maven.)
    - [Building a jar.](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md#building-a-jar.)
  - [Technical choices](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md#technical-choices)
    - [Scala](https://github.com/sanskrit-coders/indic-transliteration/blob/master/README.md#scala
