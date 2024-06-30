# This command checks the headers returned by the root URL.
curl -I http://localhost:8080/topjava/

# This command gets the meal with ID 100004.
curl -X GET http://localhost:8080/topjava/rest/meals/{100004} -H "Accept: application/json"

# This command deletes the meal with ID 100004.
curl -X DELETE http://localhost:8080/topjava/rest/meals/{100005}

# This command gets all the meals.
curl -X GET http://localhost:8080/topjava/rest/meals -H "Accept: application/json"

# This command gets all the users.
curl -X GET http://localhost:8080/topjava/rest/admin/users -H "Accept: application/json"

# This command gets the user with ID 100000.
curl -X GET http://localhost:8080/topjava/rest/admin/users/{100000} -H "Accept: application/json"

# This command deletes the user with ID 100000.
curl -X DELETE http://localhost:8080/topjava/rest/admin/users/{100000}

# This command gets the current user's profile.
curl -X GET http://localhost:8080/topjava/rest/profile -H "Accept: application/json"

# This command deletes the current user's profile.
curl -X DELETE http://localhost:8080/topjava/rest/profile

# This command gets the user with ID 100001 with all their meals.
curl -X GET http://localhost:8080/topjava/rest/admin/users/{100001}/with-meals -H "Accept: application/json"

# This command retrieves meals between the specified start and end dates and times.
curl -X GET "http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-30&startTime=12:00&endDate=2020-01-31&endTime=19:00" -H "Accept: application/json"

# This command updates the meal with ID 100004 with new date, description, and calories.
curl -s -X PUT -d "{\"id\":100004,\"dateTime\":\"2020-01-30T10:02:00\",\"description\":\"Обновленный завтрак\",\"calories\":200}" -H "Content-Type: application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals/100004

# This command creates a new meal with the specified date, description, and calories.
curl -s -X POST -d "{\"dateTime\":\"2020-02-01T18:00\",\"description\":\"Созданный ужин\",\"calories\":300}" -H "Content-Type: application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals

# This command retrieves filtered meals when start/end data/time are not specified
curl -X GET "http://localhost:8080/topjava/rest/meals/between?startDate=&endTime=" -H "Content-Type: application/json"


