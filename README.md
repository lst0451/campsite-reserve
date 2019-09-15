### How to run:  
 
The Prerequisite is:  
You have already installed java. The minimum version requirement is Java 8.  
You have already installed Maven.  
You have already installed Git.  
Your 8080 port is available ( otherwise you need to modify it in application file ).  

You can pick up one on of the two method to run:      
#####Method 1:  
```
git clone https://github.com/lst0451/campsite-reserve.git
cd campsite
mvn spring-boot:run
```

#####Method 2:

```
mvn package
```

You can find the novel.jar under target directory.  
run:
```
java -jar campsite.jar
```

###Main Requests:
If you have a postman, I suggest you import the "Campsite Reservation.postman_collection.json" file into your postman, which is more convenient to use.  
If you don't want to use postman, you can still use command line to test the API:  
* Get all campsite data and available date for reservation.(A default campsite will be created after system started.)  
```
curl -X GET "http://localhost:8080/campsites" -H "accept: */*"
```
* Get specific campsite data by id.
```
curl -X GET "http://localhost:8080/campsite/1" -H "accept: */*"
```
* Get specific campsite data by id and specified date ( If you do not specify a date, the default is to return the available date within one month).
```
curl -X GET "http://localhost:8080/campsite/1?fromDate=2019-09-20&toDate=2019-09-30" -H "accept: */*"
```
* Create a new reservation.
```
curl -X POST "http://localhost:8080/reservation" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"arrivalDate\": \"2019-10-01\", \"departureDate\": \"2019-10-04\", \"campsite\": { \"id\": 1 }, \"email\": \"shitao.ca@gmail.com\", \"fullName\": \"Shitao Li\"}"
```
* Get all exist reservations.
```
curl -X GET "http://localhost:8080/reservations" -H "accept: */*"
```
* Get reservation by id which returned by create API.
```
curl -X GET "http://localhost:8080/reservation/{reserveId}" -H "accept: */*"
```
* Cancel reservation by id.
```
curl -X DELETE "http://localhost:8080/reservation/{reserveId}" -H "accept: */*"
```
