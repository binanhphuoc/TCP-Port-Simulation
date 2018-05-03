# Routers Forwarding Table

This folder contains the routing table for each router in our network.

## Getting Started

Instead of real IP address, we will use PortNo to specify the address of each node in our network. Each router has its own table which has the shortest path to each of the other routers and agents.

The shortest paths for each router have already been calculated using Dijkstra Algorithm. Each router has its own file containing its own shortest paths.

### Format

Table for Router X will be stored in X.txt

```
Router X --> X.txt
```

Each line in X.txt shows the shortest path from X to another node Y.

An example line looks like:

```
Y 25 X A B C Y
```

Which can be understood as:

```
Dest Node | Shortest Distance | Full Path
----------|-------------------|-----------------------
    Y     |       25          | X -> A -> B -> C -> Y
```
