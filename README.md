# Recommendation-Engine-Data-Extractor

This repository contains the Java code that was used to extract user and Movie/TV series data from an anime website: myanimelist.
The user data and anime data was extracted by parsing HTML content from the website and myanimelist API.
(API documentation is at https://myanimelist.net/modules.php?go=api)
Since the API provided user data in XML format, it was parsed to JSON objects to decrease size. Moreover, when the data was
extracted, a 0.6 timeout was added to the code in order to avoid HTTP 428 error (too much requests, bascially myanimelist
website was telling to stop sending hundreds requests per second to fetch their data). Mutlithreads were also used to extract and parse
data faster.

## Collected data
The collected movie data can be found at src/main/outputs folder. Each line is a parsed JSON object containing the information of 
each anime TV series/movies.
1. Status
  Current status of the series.
2. Producers
  String array of producers involved in the production.
3. Staffs
  String array of last name and first name of important staffs who produced the anime.
4. Description
  String of the synopsis or brief plotline of the anime, currently under collection.
5. Popularity
  Popularity ranking based on myanimelist
6. Rating
  PG rating of the media's content.
7. Title
  Title of the series
8. Studios
  String array of all studios that were involved in production
9. Duration
  Number of minutes per episode/movie
10. Source
  Source of the anime if it was adapted from another media.
11. Type
  Type of the anime
12. Score
  Average rating given by myanimelist's users
13. Started_Airing / Ended_Airing / Premeried 
  The date for respective airing dates
14. Episodes
  Number of episodes for the series
15. Genres
  String array of genres of the series
16. Ranked
  Ranking of the rating of the anime
17. Members
  Number of members in myanimelist database who watched the movie
18. Favourites
  Out of the members who watched the anime, number of people who marked it as favourites
  
## Methods
All the methods that were used for the extraction of data are included in Main.java file.

**duplicateRemover**: removes duplicate animes in the JSON file placed at /inputs directory
animeExtractor: takes user data JSON file placed at /inputs directory and uses animeId attached in the data to fetch anime data from the web

**parser**: parsers XML user data to JSON file

**finalizer**: additonally parses any minor errors in XML parsing process

**extractor**: extracts user XML data by using myanimelist API by taking in text file placed at /inputs directory. 
           The text file should contain names of users to collect the data
      
**remover**: removes any dummy data (users who created their accounts on myanimelist but never used them) from XML userdata file.

**formatter**: formats XML userdata file by removing XML declaration.

**run**: helper function to collect usernames in extractor() method.

**toArray**: helper function that converts JSONArray objects to String arrays

**anime**: collects anime data from myanimelist website
