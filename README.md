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
