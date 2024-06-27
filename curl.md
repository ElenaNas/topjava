curl -I http://localhost:8080/topjava/
curl -X GET http://localhost:8080/topjava/rest/meals/{100004} -H "Accept: application/json"
curl -X DELETE http://localhost:8080/topjava/rest/meals/{100005}
curl -X GET http://localhost:8080/topjava/rest/meals -H "Accept: application/json"
curl -X GET http://localhost:8080/topjava/rest/admin/users -H "Accept: application/json"
curl -X GET http://localhost:8080/topjava/rest/admin/users/{100000} -H "Accept: application/json"
curl -X DELETE http://localhost:8080/topjava/rest/admin/users/{100000}
curl -X GET http://localhost:8080/topjava/rest/admin/users/{100001}/with-meals -H "Accept: application/json"
curl -X GET http://localhost:8080/topjava/rest/profile -H "Accept: application/json"
curl -X DELETE http://localhost:8080/topjava/rest/profile
curl -X GET http://localhost:8080/topjava/rest/profile/text -H "Accept: application/json"

http://localhost:8080/topjava/
http://localhost:8080/topjava/users
http://localhost:8080/topjava/meals
http://localhost:8080/topjava/meals/update?id=100004