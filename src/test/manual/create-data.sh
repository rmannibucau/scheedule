#! /bin/bash

#
# Why 101? > 20 to test pagination, N % 20 != 0 to ensure pagination get the overflow correctly
#
for i in {1..101}; do
    curl -X POST \
        -H 'Content-Type:application/json' \
        -H 'Accept:application/json' \
        -d '{"state":false,"cron":"* * * * *","task":"task","name":"cron'$i'"}' \
        http://localhost:8080/scheedule/api/cron
    curl -X POST \
        -H 'Content-Type:application/json' \
        -H 'Accept:application/json' \
        -d '{"key":"key_'$i'","value":"value_'$i'"}' \
        http://localhost:8080/scheedule/api/configuration
done
