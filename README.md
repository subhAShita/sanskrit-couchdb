# Introduction
## Goal
We've set out to build a **database of [subhAShita​](https://en.wikipedia.org/wiki/Subhashita)-s (= worthy quotes)** which is:
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
* The clients built for this database could serve as a model for other kAvya readers.
  * Similarly, one can build a collaboratively annotated and rated collection of verses/ sentences within the context of long sequential works (rather than free floating subhAShita-s).

## Motivation
* One of the greatest (and useful) pleasures I've had in tough times is retreat for a while into the world of beautiful subhAShita-s - and then burst back out like the vRtraghna armed with dadhIchi's bones.
* I especially like online collections curated by some friends and myself:
  * since a book is not always available, and
  * I want to collect + easily access choice ones for future enjoyment.
* But it is tedious (atleast for me) to sit in front of a computer to do the following:
    * read them,
    * or scour the internet for new ones
    * or collect favorites in a spreadsheet
    * or just annotate them with comments.
* So, it is desirable to:
    * make the above as simple and easy as possible,
    * and to share our collective labor so that we can benefit more easily from each others' work.


# Database and API
* Periodic update announcements [here](groups.google.com/forum/#!forum/sanskrit-programmers).

## Couchdb
### API-s
* Querying quote entries: use the  database and the index_headwords index
  * Get a particular entry: [link](http://vedavaapi.org:5984/quote_db__sa/astaabhimukhesuuryeuditesampuurmamamDalechamdregamamambudhasyalagmeuditaastamitechaketau).
  * Get 20 some quotes starting with ast: [link](http://vedavaapi.org:5984/quote_db__sa/_all_docs?inclusive_end=false&start_key=%22ast%22&limit=20&inclusive_end=true) .


### Database replicas
* You want to host a repilica and make things faster for folks in your geographical area? Just open an issue in this project and let us know.
* Ahmedabad, IN <http://vedavaapi.org:5984/quote_db__sa/_all_docs>
* Bay area, USA (dev machine, unstable) <http://vvasuki.hopto.org:5984/quote_db__sa/_all_docs>


## Technical choices
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

## Contribution
### A note for subhAShita collectors
* Are you a collector of subhAShita-s? Would you like to contribute your collection to the database? You're very welcome!
* But you should make your collection available in such a way that a machine which does not know human languages can distinguish individual quotes, ratings and various other details.
* सङ्क्षेपेण - मानुषभाषाङ्कामप्य् अनवगच्छता यन्त्रेणापि "अस्यां सुभाषितावल्यां सन्तीमे सुभाषिताः। अमुकस्य सुभाषीतस्यैते विषयाः। अस्मिन् सुभाषीते कस्यचिदियं टिप्पणिः। सुभाषितमिदम् अस्मै ५/५ इति मानेन रोचते। अपरस्मै ३/५ इति मानेन। अस्य सुभाषितस्य लेखकोऽसौ।" इति ग्राह्यं स्यात्। तेन पाठकानुकूलं संलक्ष्य भवितुमर्हति सङ्ग्रहस्य यान्त्रिक-प्रस्तुतिः।


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
