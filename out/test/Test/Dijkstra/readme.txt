---------------------------------------------------
STUDENT INFORMATION
---------------------------------------------------

Name: Phuoc Do
UTA ID: 1001239680
Email: phuoc.do@mavs.uta.edu

---------------------------------------------------
PROGRAMMING LANGUAGE
---------------------------------------------------

C++

---------------------------------------------------
CODE STRUCTURE
---------------------------------------------------

The project is divided into small parts as below:
- Graph:
	Related Files: Graph.cpp Graph.hpp
	Purpose: a class for representing
		the input graph using an adjacency list.
- Fringe:
	Related Files: Fringe.cpp Fringe.hpp
	Purpose: this is a class that:
		- Defines path_node structure with 
		related information such as 
		parent node, current state, path cost.
		- Implements a queue structure ordered
		by ascending path costs of nodes.
- GraphSearch:
	Related Files: GraphSearch.cpp GraphSearch.hpp
	Purpose: This is a class that implements 
		Uniform-cost search function,
		print-result function (print path), and
		some other searching-related functions.
- main:
	Related Files: main.cpp
	Purpose:
		- Represent the graph using the input file
		- Create a GraphSearch object to search for
		a path between the two input cities.

---------------------------------------------------
HOW TO RUN CODE
---------------------------------------------------

- The folder contains .cpp, .h, Makefile, and readme.txt
- Open Terminal at this folder. To compile, type:
	
	make

- To run with an input file "input.txt" with two input cities
"CityA" and "CityB", type

	./prog input.txt CityA CityB

