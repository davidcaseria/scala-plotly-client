
# Plotly client

## Authentication

To create a graph on Plotly, start by opening an account with the web UI. Then create an API key by clicking on your username in the top right hand corner of the screen and selecting *SETTINGS > API KEYS*. Create the file `~/.plotly/.credentials` in your home directory. The file should look like:

```
{
  "username": "pbugnion",
  "api_key": "l233fgfdsjk"
}
```

Note that if you have already used another Plotly client, you probably do not need to do this.

## Your first graph

To create a graph on the Plotly servers, start by importing the client:

```
import co.theasi.plotly._
```

Then, just pass the *x*, *y* series that you want to plot:

```
scala> val x = Vector(1.0, 2.0, 3.0)

scala> val y = Vector(1.0, 4.0, 9.0)

scala> plot(x, y, "hello-plotly")
```

This will create a graph called `hello-plotly` in your account!
