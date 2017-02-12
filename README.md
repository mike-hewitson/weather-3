# weather-3

generated using Luminus version "2.9.11.30"

## Running

To start a web server for the application, run:

    lein run

Testing

   lein midje

## heroku

Create heruko environments

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
# to set up the database from new

Use db_init.clj with repl
Setup url to point to the correct database
Create the database & schema using supplied functions
No need to create initial locations
Don't forget to execute the functions in the repl

# run the logger

To run the logger to log one set of readings per location at the current time in the development environment.

```
$ lein with-profile dev trampoline run -m weather-3.log-data
```

For the scheduled job in Heroku, try the following
```
$ lein with-profile production trampoline run -m weather-3.log-data
```

# to create a new version of this from a template

## project.clj

Add midje and proto-repl to dev dependencies
Add midje to plugins
Add clj-http to dependencies

Copy weather icons directory into public
Add into base.html
Copy contents from home.html, and any other html pages

Copy code from db.core
Copy routes from home.clj and any others
For clj files, don't forget to adjust the ns
