# LamportClocks

> Authors: Gabriel Castro, Gustavo Possebon e Henrique Kops

_Java implementation over Lamport's clocks._

### How to run

At project's root:

1. Package code into jar with:

    ```
    $ mvn clean package
    ```
1. Execute with java:

    ```
    $ ./run.sh <confFilePath: str> <currentNodeId: int>
    ```

### Configuration

To run this project, you will need to input a configuration file formatted as
below:

```
# node_id node_host node_port node_chance
1 127.0.0.1 3001 0.3
2 127.0.0.1 3002 0.4
3 127.0.0.1 3003 0.6
4 127.0.0.1 3004 0.8
```
> OBS.: The field 'node_chance' refers to the chance of the referring node to
>generate a distributed event.

### Architecture overview

@TODO

### Code Observations

1. The first node at configuration file will always be master, and needs to run 
first.

1. All nodes will connect initially to a multicast group at **224.0.0.1:5000**,
and all will wait until master node enable all communications.

1. All communication buffers will be set with a space of 1024 bytes.