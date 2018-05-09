//
//  Graph.hpp
//  UninformedSearch
//
//  Created by Phuoc Do on 1/28/18.
//  Copyright © 2018 Phuoc Do. All rights reserved.
//
//  GRAPH REPRESENTATION
//

#ifndef Graph_hpp
#define Graph_hpp

#include <stdio.h>
#include <string>
#include <list>

using std::string;

struct edge
{
    std::string name;
    int cost;
};

struct node
{
    std::string name;
    std::list<edge> edges;
};

class Graph
{
private:
    std::list<node> node_list;
public:
    Graph(std::string inputFile);
    //~Graph();
    void addEdge(std::string name1, std::string name2, int cost);
    node* getNode(std::string name);
    int getEdgeCost(string n1, string n2);
    node* createNode(std::string name);
    void printGraph();
};


#endif /* Graph_hpp */
