# Introduction
## Goal
We've set out to build a **database of [subhAShita​](https://en.wikipedia.org/wiki/Subhashita) -s** which is:
* **Universal**
    * Its goal is to contain within it **every worthy subhAShita** ever composed.
    * In fact, the ambition encompasses all *languages*, *verse* and *prose* forms.
* **Freely and easily available**. Anyone should be able to
    * Access it
    * Export it in other formats
    * Present it in any way users will find convenient.
      * Eg. the upcoming [subhAShita-pratimAlA] app project.
* **Growing constantly in number**
    * sarasvatI still suckles some amongst us at her bosom!
* **Growing constantly in annotations/ ratings**
    * add annotations (rating, description, translations, metre, flaws, sources ...)


## Expected extensions
* We hope that this will motivate other such long-sought-after universal databases for sanskrit connoiseurs, like: one for metres.
* Similarly, one can build a collaboratively annotated and rated collection of verses/ sentences within the context of long sequential works (rather than free floating subhAShita-s).
* The clients built for this database could serve as a model for other kAvya readers.

## Motivation
* One of the greatest (and useful) pleasures I've had in tough times is retreat for a while into the world of beautiful subhAShita-s - and then burst back out like the vRtraghna armed with dadhIchi's bones.
* I especially like online collections curated by some friends and myself:
  * since a book is not always available, and
  * I want to collect + easily access choice ones for future enjoyment.
* But it is tedious (atleast for me) to sit in front of a computer to either:
 * read them,
 * or scour the internet for new ones
 * or collect favorites in a spreadsheet
 * or just annotate them with comments.

# Technical choices
* Use Nosql rather than a relational database as the primary canonical database.
  * Reasons (from [here](https://www.couchbase.com/nosql-resources/why-nosql) ):
    * The need to develop with agility
      * Simplicity (less need to join n tables)
      * Flexibility (schema can easily be changed)
    * distributed nature: ability to operate at any scale.
      * Database copies can sync with each other easily - no centralization is good.
  * Comparison [here](http://db-engines.com/en/system/Cassandra%3BCouchbase%3BMongoDB%3BRedis) .
  * Alternatives:
    * Couchbase database, since it can be [used in many](https://developer.couchbase.com/documentation/mobile/1.4/training/develop/using-the-database/index.html) mobile OS-s.
      * Current problems: Reading what was written with scala - [stackoverflow](http://stackoverflow.com/questions/43315540/couchbase-lite-retrieving-document-properties-after-reopening-database-yields) .
    * Realm.io - good for storing plain Java objects via XML.
    * Google Firebase for key-value storage - 1GB free.
* Scala for the ingestion libraries (same justification as expressed [here](https://github.com/sanskrit-coders/sanskritnlpjava/blob/master/README.md#scala) ).
