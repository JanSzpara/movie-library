#Movie Library Rest Service


#Running Service

`./gradlew bootRun`

#Testing Service
`./gradlew test`


#Consuming Service

The API returns movie objects with the following attributes

- id
- title
- runtime - Length in minutes of the movie
- releaseYear - Year the movie was released
- blurb - Short summary of the movie

## Get all movies

### `GET /movies/`
 
 Returns an array of all movies in the database and status `200 OK`
 
 Eg.
 ```
 [
   {
     "id": 1,
     "title": "rerum",
     "releaseYear": 2010,
     "runtime": 1650,
     "blurb": "Quae odio omnis a. Aliquam fugit quia minus accusamus ea consequuntur."
   },
   	...
   {
     "id": 21,
     "title": "Too Fast Too Furious 2",
     "releaseYear": 0,
     "runtime": 45022,
     "blurb": "Et maxime nihil consequuntur omnis est."
   }
 ]
 ```
 
 ### `GET /movies/id`
 
 Returns the movie with id `id` and status `200 OK` or a `404 Not Found` if it doesn't exist
 
 Eg.
 ```
{
    "id": 1,
    "title": "rerum",
    "releaseYear": 2010,
    "runtime": 1650,
    "blurb": "Quae odio omnis a. Aliquam fugit quia minus accusamus ea consequuntur."
}
 ```
 
 
 ### `POST /movies/`
 
 Adds a new movie to the database. Accepts a JSON object of the movie of the form
 
 Eg.
 ``` 
 {
   "title": "Too Fast Too Furious 2",
   "releaseYear": 2017,
   "runtime": 45022,
   "blurb": "Et maxime nihil consequuntur omnis est."
 }
 ```

### `DELETE /movies/id`
 
 Delete the movie with id `id` and returns status `204 No Content` or a `404 Not Found` if it doesn't exist
 

### `PUT /movies/id`

 Update the movie with id `id` with new attributes. Accepts data of the form: 
 
```
 {
   "title": "New Title",
   "releaseYear": 2017,
   "runtime": 45022,
   "blurb": "New Blurb"
 }
```
Note the missing id attribute