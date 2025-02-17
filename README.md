### Connecting to DBs in Docker
Before running the app make sure all dbs are up and running

#### Start storer2db mariaDB database
```docker run -e MARIADB_ROOT_PASSWORD=password -e MARIADB_DATABASE=storer2db -p 3306:3306 --name storer2db -d mariadb```

#### Start storemdb mongoDB database
```docker run -p 27017:27017 --name storemdb -d mongo```

### Useful DB-related commands

##### Enter mariadb shell
```docker exec -it storer2db mariadb -u root -p```
