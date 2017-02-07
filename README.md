# weather-3

generated using Luminus version "2.9.11.30"

## Running

To start a web server for the application, run:

    lein run

Testing

   lein midje

## heroku

Create herulo environments

```
$ heroku new
```

Add database URI to environments

To push to heroku

```
$ git push heroku master
```

To test locally first

```
$ heroku open
```

# to move from generated app to my one (copying in code)

## project.clj

Add midje and proto-repl to dev dependencies
Add midje to plugins
Add clj-http to dependencies

# to set up the database from new

Use db_init.clj with repl
Setup url to point to the correct database
Create the database & schema using supplied functions
No need to create initial locations

# run the logger

To run the logger to log one set of readings per location at the current time in the development environment.

```
$ lein run -m weather-3.log-data with-profile dev
```

For the scheduled job in Heroku, try the following 
```
lein trampoline run -m weather-3.log-data with-profile production
```
