# TinyServer

TinyServer is a lightweight HTTP server written in Java.  
Your feedback is always welcome.

#### Features:

- Multithreaded
- GET paramters
- POST requests
- RESTful routes
- Callback methods
- Fileserver capability
- File, JSON, HTML response classes (easy to write your own)
- High performance

TinyServer is perfect for embedding into your applications.

## Requirements ##

- Java 7+
- Response class dependencies *[optional]*  

## Quick Start ##

First import the server library: `import net.metzweb.tinyserver.*;`  
Now you are good to go and can setup the server:

```
// init server at port 8200
TinyServer server = new TinyServer(8200);

// define a GET route
server.get("/", new Response() {
  @Override
  public void callback(Request request) {
    request.write("Hello world :)");
  }
});

// start server
server.start();
```

and make your first request: `http://localhost:8200`  
If everything works well, you will get a friendly welcome response:

```
Hello world :)
```

> For a working example, take a look into the `/example` folder.

## Details

### Server

Initialize the server by calling: `new TinyServer(<port>)`

After you have defined all routes, simply call `start()` to start the server and receive requests.

### Request routes

#### GET

`get(<route>, <callback>)`

#### POST

`post(<route>, <callback>)`

#### Route patterns

Before you define your request routes, please take a look at the following guidlines:

1. all routes have to start with a **backslash**: `/hello/world`
    - the default route is simply: `/`
2. request parameters are **not allowed** in your **route path**: ~~`/hello?foo=bar`~~
	- access the params instead in the [callback method](#callback).
3. *"fake"* file **extensions** allowed: `/hello.json`
	- useful, if you associate it with the according [response format](#response).

### Request parameters

#### GET parameter

If your request contains request parameters, they will be accessible in your callback method.

To receive a particular parameter, pass its key into the 
`param(<key>)` method. If the key doesn't exists it will return `null`.

Example usage: `/hello?name=Christian`

```
public void callback(Request request) {
  request.write("Howdy " + request.param("name"));
}
```

#### Route parameter *(RESTful)*

In order to receive route parameters, mark these with placeholders: `[parameter]`

`get("/hello/[name]/[age]", <callback>)`

These values are accessible in your callback method, the same way as one accesses GET params:  
`request.param(<parameter>)`

#### POST data

Retrieve posted data, by using the `getData()` method in your callback method.  
This returns the POST data as a `String` (including linebreaks).

### Callback

##### Anonymous class

An anonymous class can be directly passed into the route method:

```
server.get("/", new Response() {
  @Override
  public void callback(Request request) {
	// ...
  }
});
```

##### Standalone class

A callback class has to implement the `Response` interface:

```
class MyCallback implements Response {
  @Override
  public void callback(Request request) {
	// ...
  }
}
```

and then be passed into the route method:

```
server.get("/", new MyCallback());
```

### Response

By default, TinyServer sends `text/plain` responses.  
In order to use a formatted response, it's necessary to set the response class accordingly:

```
server.setResponseFormat(<format object>);
```

Every response class comes with four methods:

- 200 Success: `success(<data>)`
- 403 Forbidden: `forbidden()`
- 404 Not found: `notFound()`
- 500 Server Error: `error()`

These methods are accessible by calling the `write()` method in your callback method:

```
request.write().success("Hello world.");
```

**Shortcut:** Alternatively, you can pass your data directly into the `write()` method, which is an alias for the `success()` method:

```
request.write("Hello world.");
```

#### File

To serve a file, simply pass its path into the `success(String filePath)` method.

Example usage:

```
server.setResponseFormat(new FileResponse());
server.get("/kitten", new Response() {
  @Override
  public void callback(Request request) {
    request.write("kitten.jpg");
  }
});
```

Server response:

```
HTTP/1.1 200 OK
Cache-Control: private, max-age=0
Content-Type: image/jpeg
Server: TinyServer

<kitten.jpg>
```

#### HTML

The `success(String htmlContent)` method accepts an HTML `String`.

Example usage:

```
server.setResponseFormat(new HtmlResponse());
server.get("/halloween", new Response() {
  @Override
  public void callback(Request request) {
    String output = "<!DOCTYPE html>"
	  + "<html>"
	  + "  <head><title>31 October</title></head>"
	  + "  <body><h2>Happy Halloween!</h2></body>"
	  + "</html>";
	request.write(output);
  }
});
```

Server response:

```
HTTP/1.1 200 OK
Cache-Control: private, max-age=0
Content-Type: text/html; charset=utf-8
Server: TinyServer

<!DOCTYPE html>
<html>
  <head><title>31 October</title></head>"
  <body><h2>Happy Halloween!</h2></body>"
</html>
```

#### JSON

---

**Please note:** This requires the [JSON simple](http://code.google.com/p/json-simple/) Java library.

---

The `success()` method requires a `Map` object, that holds the `key => value` pairs.

Example usage:

```
server.setResponseFormat(new JsonResponse());
server.get("/weather", new Response() {
  @Override
  public void callback(Request request) {
    HashMap<String, String> map = new HashMap<>();
    map.put("location", "Munich");
    map.put("condition", "fair");
    map.put("temprature", "32 °C");
	request.write(map);
  }
});
```

Response:

```
HTTP/1.1 200 OK
Cache-Control: no-cache, must-revalidate
Content-Type: application/json; charset=utf-8
Server: TinyServer

{
  "status": "200",
  "message": "OK",
  "data": {
    "condition": "fair",
    "location": "Munich",
    "temprature": "32 °C"
  }
}
```

#### Other formats

It's simple to create your own response class for a missing format.  
You can find a response class template in the example folder.

---

Every response class **must extend the abstract `ResponseFormat`** class  
and its constructor must call `super(<MIME type>)`.

---

#### Response classes

- `File` [response class](#)
- `HTML` [response class](#)
- `JSON` [response class](#)
  - requires [JSON simple](http://code.google.com/p/json-simple/)

> Let me know if you have created a new response class, so it can be add it to the list.

## Misc

POST routes can be tested with cURL via the terminal:

```
curl -X POST -d 'hello world' localhost:8200/data --header "Content-Type:text/plain"
curl -X POST -d @hello.txt localhost:8200/file --header "Content-Type:text/plain"
```

## Config

- Server port: `new TinyServer(<port>)`

## Issues

Please submit issues through the [issue tracker](https://github.com/cosenary/TinyServer/issues) on GitHub.

## History

**TinyServer 1.0 - 13/07/2013**

- `release` First official version
- `update` Major code improvements
- `update` DRY code guidelines
- `update` Rewritten documentation
- `feature` RESTful route parameters
- `feature` anonymous callbacks
- `feature` JSON response class
- `feature` Java 7 improvements
- `feature` POST request support
- `feature` File response class

**Server 0.8 - 28/10/2012**

- `release` Internal testing version
- `feature` Multithreading
- `update` Better documentation

**Server 0.5 - 24/10/2012**

- `release` First internal alpha version

## Credits

Copyright (c) 2013 - Programmed by Christian Metz  
Released under the [BSD License](http://www.opensource.org/licenses/bsd-license.php).