# Black Swan Data Assessment - User Tasks

## Instructions
[Detailed Instructions] (./ASSESSMENT.md)

## Design
Current assumptions, notes and other useful insights is documented in `NOTES.md`.

## Running the project

### Prerequisite

* Java 8
* Maven

To make sure Maven is installed
```
$ mvn --version
```
you should see a detailed output of Maven info.

To install all project dependencies do
```
$ mvn install
```

### Test
To run unit tests do

```
$ mvn test
```

To run integration tests do

```
$ mvn integration-test -P integration
```
### Running project
To run the project, from the base of the project directory do
```
$ ./mvnw spring-boot:run
```

### Test deliverable
Once the project is running, you can test the REST endpoints with `curl` or your favorite client API.

To test the **Bonus feature** you can't use a `curl` because the application won't allow to insert task with date in the past.

To test it, some tasks to be expired are inserted in the database at each applications.
You can add, delete or modify these task by updating the file `/tasks/src/main/resources/db/seed/V1_2018092313461537703199__task_table_add_insert_sample_records.sql`  

**NOTE** The job expiring task is scheduled to re-run 1 min after the end of the previous run.

### Monitoring
You can check the status of the application at `http://localhost:8080/monitoring/health`.

### API documentation

We are using [Swagger] (http://swagger.io/) for API documentation.

When the application is running, all API a presented in a user friendly way at `http://localhost:8080/swagger-ui.html`.

The Swagger documentation file is available `http://localhost:8080/v2/api-docs`.

### Linting

#### CheckStyle:
[Website] (http://checkstyle.sourceforge.net/)

CheckStyle is our default linter. It's set to run automatically. To run it manually do
```
$ mvn checkstyle::check
```

### Seed and Migration

Migration files are located in `tasks/src/main/resources/db/migration/`.
They change the database schema.


Seed files are located in `tasks/src/main/resources/db/seed/`.
They insert record in the database schema, or update existing records.

#### Creating a migration/seed file
[Website] (https://flywaydb.org/)

We use Flyway for database migration.

To avoid number clashes, we use timestamp in the name of the migration file

```
$ cd src/main/resources/db/migration/
$ touch V1_$(date +%Y%m%d%H%M%s)__user_friendly_name.sql
```
Where `V1` is the current running version prefix

### Profile

We currently have a development (`development`) and a production (`production`) profile that can be used to run the project.
The development profile prints significantly more information in the console for debugging purposes.

To run the project in `production` mode, do
```
$ mvn spring-boot:run -Dspring.profiles.active=production
```

The command 
```
$ ./mvnw spring-boot:run
```
runs the production mode by default. To run the project in development mode, do
```
$ mvn spring-boot:run -Dspring.profiles.active=development
```
