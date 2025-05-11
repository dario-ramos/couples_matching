# Context

This repository provides a command-line Java application that solves
the couples matching problem using two different approaches:

- Backtracking: Try every combination until a stable solution is found.
- Gale-Shapley algorithm: Uses a greedy approach that allows reaching
a stable solution faster. However, this doesn't guarantee optimality.

For more details and a deeper analysis, see the `doc` directory.

## Build and run

### Locally

This is a Maven project, so it should work even without an IDE.
To generate the .jar binary:

```
mvn package 
```

Then, just run the .jar passing the input file path as an argument:

```
java -jar target/couples-matching-1.0.0.jar src/main/resources/sample_input.txt 
```

### Via Docker

Generate the Docker image:

```
docker build -t couples-matching .
```

Run the Docker image, mounting the input file as a Docker volume and
passing its bound path in the container as an argument:

```
docker run --rm -v $PWD/couples_matching/src/main/resources/sample_input.txt:/input.txt couples-matching /input.txt
```

Notice that this approach allows us to change the input file
without rebuilding the Docker image: just mount a different file,
or modify its contents.
