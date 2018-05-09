#include <iostream>
#include <string>
#include <list>
#include "../Dijkstra/Graph.hpp"
#include "../Dijkstra/GraphSearch.hpp"

using namespace std;

int main(int args, char* argv[])
{
  string nodes[] = {"A","B","C","D","E","F","G","H","L","Ann","Chan","Jan"};
  std::list<string> routers;
  for (char a = 'A'; a <= 'L'; a++)
    routers.push_back(nodes[a-'A']);

  Graph g(argv[1]);
  GraphSearch gs(g);

  for (list<string>::iterator it = routers.begin(); it != routers.end(); it++)
  {
    string temp = *it + ".txt";
    gs.writeResult(*it, routers, temp);
  }

  return 0;
}
