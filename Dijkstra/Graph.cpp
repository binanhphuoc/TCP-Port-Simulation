//
//  Graph.cpp
//  UninformedSearch
//
//  Created by Phuoc Do on 1/28/18.
//  Copyright © 2018 Phuoc Do. All rights reserved.
//

#include "Graph.hpp"
#include <string>
#include <fstream>
#include <sstream>
#include <list>
#include <iostream>

using std::string;
using std::ifstream;
using std::stringstream;
using std::cout;

Graph::Graph(string inputFile)
{
    ifstream ifs(inputFile.c_str(), ifstream::in);
    string line;
    getline(ifs, line);
    while (line != "END OF INPUT")
    {
        stringstream ss;
        ss.str(line);
        string name1;
        string name2;
        int cost;
        
        getline(ss, name1, ' ');
        getline(ss, name2, ' ');
        ss >> cost;
        
        addEdge(name1, name2, cost);
        
        getline(ifs, line);
    }
}

void Graph::addEdge(string name1, string name2, int cost)
{
    node* node1 = getNode(name1);
    if (node1 == NULL)
        node1 = createNode(name1);
    edge e1;
    e1.name = name2;
    e1.cost = cost;
    node1->edges.push_back(e1);
    
    node* node2 = getNode(name2);
    if (node2 == NULL)
        node2 = createNode(name2);
    edge e2;
    e2.name = name1;
    e2.cost = cost;
    node2->edges.push_back(e2);
}

node* Graph::getNode(string name)
{
    for (std::list<node>::iterator it = node_list.begin(); it != node_list.end(); it++)
    {
        if (it->name == name)
            return &*it;
    }
    return NULL;
}

node* Graph::createNode(string name)
{
    node n;
    n.name = name;
    node_list.push_back(n);
    return &node_list.back();
}

void Graph::printGraph()
{
    for (std::list<node>::iterator it = node_list.begin(); it != node_list.end(); it++)
    {
        cout << it->name;
        for (std::list<edge>::iterator it2 = it->edges.begin(); it2 != it->edges.end(); it2++)
            cout << "->" << it2->name << "(" << it2->cost << ")";
        cout << std::endl;
    }
}

int Graph::getEdgeCost(string n1, string n2)
{
    std::list<edge>& l = getNode(n1)->edges;
    for (std::list<edge>::iterator it = l.begin(); it != l.end(); it++)
    {
        if (it->name == n2)
            return it->cost;
    }
    return -1;
}
