# Spring Boot Caching Proxy

![Current Version](https://img.shields.io/badge/version-1.0.0-green)
[![Framework](https://img.shields.io/badge/framework-Spring_Boot-6DB33F?logo=springboot&logoColor=white)](https://spring.io/)
[![Redis](https://img.shields.io/badge/cache-Redis-DC382D?logo=redis&logoColor=white)](https://redis.io/)

> Caching Proxy Server with interactive shell built using Spring Boot and Redis.

## Table of Contents

- [General Info](#general-information)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [Project Status](#project-status)
- [Acknowledgements](#acknowledgements)

## General Information

This project is a caching proxy server built using [Spring Boot](https://spring.io) and [Redis](https://redis.io/).
It provides an interactive shell for users to clear the cache. The proxy server will forward requests to other servers
and
cache their responses to improve performance and reduce latency.

## Technologies Used

- Java 21.0.11
- Spring Boot 4.0.6
- Spring Shell 4.0.2
- Redis 8.6.2

## Features

- Request forwarding to target servers.
- Support for GET requests.
- Caching of HTTP responses using Redis.
- Interactive shell for cache clearing.

## Setup

To run this project locally, you'll need:

- Java 21 or higher
- Redis server running on `localhost:6379`

How to install and run the project:

1. Clone the repository:

   ```bash
   git clone https://github.com/krisnaajiep/springboot-caching-proxy.git
   ```

2. Navigate to the project directory:

   ```bash
   cd springboot-caching-proxy
   ```

3. Build the project using Maven:

   ```bash
   mvn clean install
   ```

4. Run the application:

   ```bash
   java -jar target/springboot-caching-proxy-1.0.0.jar
   ```
   
## Usage

If you run the application without any server options, the server will be accessible at http://localhost:8080. 
The server will forward requests to the default target server https://dummyjson.com.

```terminaloutput
Server started on http://localhost:8080
Server origin set to: https://dummyjson.com
```

If you want to specify a different port and target server, you can use the `--server.port` and `--server.origin` option 
when starting the application. For example, to run the server on port 8081 and forward requests to https://jsonplaceholder.typicode.com, 
you can use the following command:

```bash
java -jar target/springboot-caching-proxy-1.0.0.jar --server.port=8081 --server.origin=https://jsonplaceholder.typicode.com
```

When you enter the interactive shell, you can type `help` command to see available commands or `clear-cache` to clear the Redis cache.

```terminaloutput
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.0.6)

Welcome to the Spring Boot Caching Proxy Server with Interactive Shell

Server started on http://localhost:8081
Server origin set to: https://jsonplaceholder.typicode.com

Type 'help' to see available commands.

$>help
AVAILABLE COMMANDS


        clear-cache: Clear the Redis cache
Built-In Commands
        clear: Clear the terminal screen
        help: Show help about available commands
        quit, exit: Exit the shell
        script: Execute commands from a script file
        version: Show version info

$>clear-cache
Clearing all cache data...
Cache cleared successfully.
$>
```

## Project Status

Project is: _complete_.

[![CI](https://github.com/krisnaajiep/springboot-caching-proxy/actions/workflows/maven.yml/badge.svg)](https://github.com/krisnaajiep/springboot-caching-proxy/actions/workflows/maven.yml)

## Acknowledgements

This project was inspired by [roadmap.sh](https://roadmap.sh/projects/caching-server).