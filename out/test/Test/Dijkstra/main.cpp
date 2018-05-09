//
//  main.cpp
//  UninformedSearch
//
//  Created by Phuoc Do on 1/28/18.
//  Copyright © 2018 Phuoc Do. All rights reserved.
//

#include <iostream>
#include "Graph.hpp"
#include "GraphSearch.hpp"

int main(int argc, const char * argv[]) {
    // insert code here...
    //Graph g("/Users/phuocdo/Documents/UTA/School/Spring 2018/AI/Assignments/UninformedSearch/UninformedSearch/input.txt");
    Graph g(argv[1]);
    GraphSearch gs(g);
    gs.search(argv[2], argv[3]);
    gs.printResult();
	//gs.~GraphSearch();
    return 0;
}
