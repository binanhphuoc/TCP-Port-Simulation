//
//  Fringe.cpp
//  UninformedSearch
//
//  Created by Phuoc Do on 1/28/18.
//  Copyright © 2018 Phuoc Do. All rights reserved.
//

#include "Fringe.hpp"
#include <list>
#include "Graph.hpp"

using std::list;

void Fringe::push(path_node* n)
{
    for (list<path_node*>::iterator it = queue.begin(); it != queue.end(); it++)
    {
        if (n->current == (*it)->current)
        {
            if (n->path_cost > (*it)->path_cost)
            {
                delete n;
                return;
            }
            else
            {
                queue.remove(*it);
                delete *it;
                it = queue.end();
            }
        }
    }

    for (list<path_node*>::iterator it = queue.begin(); it != queue.end(); it++)
    {
        if (n->path_cost < (*it)->path_cost)
        {
            queue.insert(it, n);
            return;
        }
    }
    queue.push_back(n);
}

path_node* Fringe::pop()
{
    path_node* n = *queue.begin();
    queue.pop_front();
    return n;
}

bool Fringe::isEmpty()
{
    return (queue.size() == 0) ? true : false;
}

void Fringe::freeMem()
{
    for (list<path_node*>::iterator it = queue.begin(); it != queue.end(); it++)
    {
	     delete *it;
    }
    queue.clear();
}
