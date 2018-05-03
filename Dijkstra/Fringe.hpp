//
//  Fringe.hpp
//  UninformedSearch
//
//  Created by Phuoc Do on 1/28/18.
//  Copyright © 2018 Phuoc Do. All rights reserved.
//
//  ASCENDING QUEUE IN RESPECT TO PATH COST
//

#ifndef Fringe_hpp
#define Fringe_hpp

#include <stdio.h>
#include "Graph.hpp"
#include <list>

using std::list;

struct path_node
{
    path_node* parent;
    node* current;
    int path_cost;
};

class Fringe
{
private:
    list<path_node*> queue;
public:
    void freeMem();
    void push(path_node* node);
    path_node* pop();
    bool isEmpty();
};

#endif /* Fringe_hpp */
