
# change this as per instruction to avoid conflicts.
PORT=12345

COOKIEJAR=cookies.txt

# clear cookies
/bin/rm ${COOKIEJAR}

# test authentication
curl -v -H "Content-Type: application/json" \
     -c ${COOKIEJAR} \
     -X POST \
     -d '{"username":"user0","password":"thepassword"}' \
    http://localhost:${PORT}/api/login

# this should succeed if the password is correct
curl -v \
    -b ${COOKIEJAR} \
    http://localhost:${PORT}/api/login

# create a 'private' folder first.
# this should fail since credentials were not presented
curl -v \
    http://localhost:${PORT}/private/secret.txt

# this should succeed since credentials were presented
curl -v \
    -b ${COOKIEJAR} \
    http://localhost:${PORT}/private/secret.txt



# test authentication
curl -v  -H "Content-Type: application/json" -c cookies.txt --no-keepalive -X POST -d '{"username":"user0","password":"thepassword"}' http://localhost:12345/api/login

# this should succeed if the password is correct
curl -v 
    -b cookies.txt  --no-keepalive
    http://localhost:12345/api/login

# create a 'private' folder first.
# this should fail since credentials were not presented
curl -v \
    http://localhost:12345/private/secret.txt

# this should succeed since credentials were presented
curl -v \
    -b cookies.txt \
    http://localhost:12345/private/secret.txt

