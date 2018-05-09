//
//  GraphSearch.hpp
//  UninformedSearch
//
//  Created by Phuoc Do on 1/28/18.
//  Copyright © 2018 Phuoc Do. All rights reserved.
//
//  PATH FINDING FOR OPTIMAL SOLUTION
//

#ifndef GraphSearch_hpp
#define GraphSearch_hpp

#include <stdio.h>
#include <list>
#include "Graph.hpp"
#include "Fringe.hpp"

using std::list;

class GraphSearch
{
private:
    Graph& graph;
    Fringe fringe;
    list<path_node*> closed;
    path_node* optimal_path;
    bool found;


public:
    GraphSearch(Graph& g) : graph(g) {found = false;}
    void freeMem(); // remove all memories of path_nodes (in visited and optimal path and fringe);
    int search(string start, string end);
    void printResult();
    void writeResult(string n, list<string>& list_node, string fileName);
    bool isVisited(node* n);
};

#endif /* GraphSearch_hpp */
