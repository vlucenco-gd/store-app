### Connecting to DBs in Docker
Before running the app make sure all dbs are up and running

#### Start storer2db mariaDB database
```docker run -e MARIADB_ROOT_PASSWORD=password -e MARIADB_DATABASE=storer2db -p 3306:3306 --name storer2db -d mariadb```

#### Start storemdb mongoDB database
```docker run -p 27017:27017 --name storemdb -d mongo```

### Useful DB-related commands

##### Enter mariadb shell
```docker exec -it storer2db mariadb -u root -p```

### Postman Collection & Environment for store-app

#### How to Import in Postman
1. Open Postman.
2. Click **Import** → Select `postman/store-app.postman_collection.json`.
3. Click **Import** → Select `postman/store-app.postman_environment.json`.
4. Select the imported environment in the **top-right dropdown**.
