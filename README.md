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

```
curl -X GET "http://localhost:8080/campsites" -H "accept: */*"
```

```
curl -X GET "http://localhost:8080/campsite/1" -H "accept: */*"
```
```
curl -X GET "http://localhost:8080/campsite/1?fromDate=2019-09-20&toDate=2019-09-30" -H "accept: */*"
```

```
curl -X POST "http://localhost:8080/reservation" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"arrivalDate\": \"2019-10-01\", \"departureDate\": \"2019-10-04\", \"campsite\": { \"id\": 1 }, \"email\": \"shitao.ca@gmail.com\", \"fullName\": \"Shitao Li\"}"
```

```
curl -X GET "http://localhost:8080/reservations" -H "accept: */*"
```

```
curl -X GET "http://localhost:8080/reservation/{reserveId}" -H "accept: */*"
```

```
curl -X DELETE "http://localhost:8080/reservation/{reserveId}" -H "accept: */*"
```
