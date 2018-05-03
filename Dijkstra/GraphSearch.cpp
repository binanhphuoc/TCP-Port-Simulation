//
//  GraphSearch.cpp
//  UninformedSearch
//
//  Created by Phuoc Do on 1/28/18.
//  Copyright © 2018 Phuoc Do. All rights reserved.
//

#include "GraphSearch.hpp"
#include <iostream>
#include <fstream>
#include <list>
#include <iterator>

using std::cerr;
using std::cout;
using std::endl;
using std::list;

// return 1: found, return 0: failure, return -1: error
int GraphSearch::search(string start, string end)
{
    freeMem();
    found = false;

    node* end_node = graph.getNode(end);
    if (end_node == NULL)
    {
        cerr << "Goal does not exist." << endl;
        return -1;
    }

    node* start_node = graph.getNode(start);
    if (start_node == NULL)
    {
        cerr << "Start node does not exist." << endl;
        return -1;
    }

    path_node* start_pn = new path_node;
    start_pn->parent = NULL;
    start_pn->current = start_node;
    start_pn->path_cost = 0;

    fringe.push(start_pn);

    while (!found)
    {
        if (fringe.isEmpty())
            return 0;

        path_node* pn = fringe.pop();
        closed.push_back(pn);

        if (pn->current == end_node)
        {
            found = true;
            optimal_path = pn;
            return 1;
        }

        for (list<edge>::iterator it = pn->current->edges.begin(); it != pn->current->edges.end(); it++)
        {
            node* n = graph.getNode(it->name);
            if (!isVisited(n))
            {
                path_node* successor = new path_node;
                successor->parent = pn;
                successor->current = n;
                successor->path_cost = pn->path_cost + it->cost;
                fringe.push(successor);
            }
        }
    }

    return -1;
}

bool GraphSearch::isVisited(node* n)
{
    for (list<path_node*>::iterator it = closed.begin(); it != closed.end(); it++)
    {
        if ((*it)->current == n)
            return true;
    }
    return false;
}

void GraphSearch::freeMem()
{
    fringe.freeMem();
    for (list<path_node*>::iterator it = closed.begin(); it != closed.end(); it++)
    {
       	delete *it;
    }
    optimal_path = NULL;
    closed.clear();
}

void GraphSearch::printResult()
{
    if (!found)
    {
        cout << "distance: infinity" << endl << "route:" << endl << "none" << endl;
        return;
    }

    cout << "distance: " << optimal_path->path_cost << " km" << endl;
    cout << "route: " << endl;

    list<string> result;
    path_node* p = optimal_path;
    while (p != NULL)
    {
        result.push_front(p->current->name);
        p = p->parent;
    }

    for (list<string>::iterator it = result.begin(); it != result.end(); it++)
    {
      	string n1 = *it;
      	it++;
      	if (it == result.end())
      		break;
      	string n2 = *it;
      	it--;
        int distance = graph.getEdgeCost(n1, n2);
        cout << n1 << " to " << n2 << ", " << distance << " km" << endl;
    }
}

void GraphSearch::writeResult(string n, list<string>& list_node, string fileName)
{
    std::ofstream ofs (fileName, std::ofstream::out);

    for (list<string>::iterator it = list_node.begin(); it != list_node.end(); it++)
    {
        this->search(n, *it);
        if (!found)
        {
            ofs << "distance: infinity" << endl << "route:" << endl << "none" << endl;
            return;
        }

        ofs << *it << " " << optimal_path->path_cost << " ";

        list<string> result;
        path_node* p = optimal_path;
        while (p != NULL)
        {
            result.push_front(p->current->name);
            p = p->parent;
        }

        for (list<string>::iterator it = result.begin(); it != result.end(); it++)
        {
            string n = *it;
            ofs << n << " ";
        }
        ofs << endl;

    }

    ofs.close();
}
