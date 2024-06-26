﻿# MinimalistJavaWebServer
This project is a lightweight Java-based web server utilizing the socket module. While simplistic, it offers the functionality to serve static files. It is intended for educational purposes rather than production use.

![Capture d'écran 2024-03-27 004559](https://github.com/Sharnalk/MinimalistJavaWebServer/assets/98692061/0f548594-3236-4679-a98f-61eb3e846cc3)

## Why ?

The goal of creating this web server was to explore how web servers function and gain a better understanding of the HTTP protocol. By constructing a basic server from scratch, the aim was to grasp fundamental concepts such as handling incoming connections, parsing HTTP requests, and serving responses.

This project serves as a stepping stone to comprehend the workings of sophisticated web servers like Apache or Nginx.

![Capture d'écran 2024-03-27 005303](https://github.com/Sharnalk/MinimalistJavaWebServer/assets/98692061/bfecd03a-b8c3-4b87-805e-9de53d8b2387)

![Capture d'écran 2024-03-27 005342](https://github.com/Sharnalk/MinimalistJavaWebServer/assets/98692061/ae842268-b4a2-47df-a408-319e8b60345f)

## Features

- [x] Serve static files
- [x] Default index file
- [x] Logging
- [x] Error handling
- [x] Documentation
- [x] Unit Testing
- [x] Multi-threaded
- [ ] HTTPS support

## Usage

_You need jre or jdk 17 and git installed on your machine to run this program._


First, clone this repository:
```bash
git clone https://github.com/Sharnalk/MinimalistWebServer
```

```bash
cd MinimalistWebServer
```

Then launch the server:
```bash
java -jar .\MinimalistJavaWebServer.jar
```
The server start on localhost by default and generates a listening port dynamically, but you can specify host / port when creating the
server instance or at runtime. The port number must be of type int, and host number of type string.

At runtime:

```bash
java -jar MinimalistJavaWebServer.jar --port 8000 --host 127.0.0.1
```

Now the server is running, just access the URL displayed in your terminal ( port number + 
host address).<br>You should see the welcome page, unless you replaced default.html
with your own.


This server is intended to serve static files. To do that, simply add file to the ressource folder or change this directory in the ClientHandler class changing this value : DEFAULT_RESOURCE_PATH,
launch the server and access the URL.

All libraries used are built-in inside; no external dependencies are needed.

<p>You can organize your project folder as you want, but the recommended structure is as follows:
</p>
    <pre>
MinimalistWebServer/your_project/
                |-- index.html # or the name you want
                |
                |-- static/
                |         -- css/
                |               style.ss
                |         -- img/
                |               1.png
                |               2.png
                |               3.png
                |         -- js/
                |              index.js
  </pre>

When your project folder is ready, you can acess the URL of your project.

You need to mention your html file in the path, otherwise the server won't find
it. Don't forget to adapt the port number if you changed it.

For example:
```bash
http://127.0.0.1:8080/your_project/index.html
```
That's it, you should be able to see your entry file and all the linked static files will be
served as well.

### Note for Linux users

By default, Linux does not allow access through the web browser to any file apart of those located in <tt>/var/www</tt>, <a href="http://httpd.apache.org/docs/2.4/mod/mod_userdir.html">public_html</a> directories (when enabled) and <tt>/usr/share</tt> (for web applications). If your site is using a web document root located elsewhere you may need to modify your files permissions accordingly.

### Contributing

Any guidance, suggestions, or collaboration is warmly welcomed and greatly appreciated.
