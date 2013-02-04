
Batmud Path Finder
Copyright Teppo Kankaanp‰‰


This utility is a plugin for a Java based client Batclient for a game
Batmud (www.bat.org).
The game consists of five basically euclidean maps with square tiles,
and the player can move on ground one tile at a time to any of the
8 directions. Each tile has a movement cost assigned to it.

The most basic functionality is: Given two points on a same map, this
utility will find the path with the lowest cost between the two points. 
This is solved by an implementation of A* algorithm suited for the
problem at hand.

There are two other movement modes: on mount and on ship. The movement
will be slightly different in these modes, for example there are only
three movement costs on nodes when using the ship movement (on water,
flying on land or completely impassable). 

The UI functionality will also help with sending these directions to
the game client in a nice fashion, and of course parsing commands 
from the client either by command line or graphical UI input.

The classes are divided in three packages: Program logic, user interface
and API.

To build this software you need bat_interfaces_1.99i.jar or similar from
somewhere under http://www.bat.org/batclient/.


Program logic:
-------------------------------------
Visible (public) classes:
  AreaContainer: Main interfacing class.
  BPFException: Main program logic error reporting exception
  Location: Interface of a class representing a location somewhere.
  SolvedListener: Interface for a listener that will be called when
    a solver thread finishes.

Internal (package) classes:
  AHeap: A custom implementation of an array heap. Used from BatPathFinder.
  Area: Represents one map of the five maps in the game. Contained in
    AreaContainer.
  BatPathFinderThread: The class that implements the actual A* algorithm
    and runs in a thread. Called from AreaContainer.  
  Link: Represents a link to a destination, with some cost and description.
    Stored in NameLocation.
  NameLocation: A Location that is just a network node with some neighbors.
  PlaneLocation: A Location at some coordinates on a map. Used to store coordinates
    during A* search. At other times almost all PlaneLocation objects are destroyed.
    NameLocation objects and PlaneLocations that are adjacent to those NameLocations,
    however, remain.
  RoutePart: A part of a route constructed in BatPathFinderThread.
  Tradelanes: Represents the Trade lanes on the map.
  TrueNode: A wrapper for Location (actually, a sublass for both NameLocation and
    PlaneLocation), that stores extra data for pathfinding algorithm. 
  

  
User interface:
-------------------------------------
BatPathFinderUI: the main user interface class which creates windows
  and does the main UI program logic.
GridLayout2: A nice grid layout class.
InfoPanel, ListPanel: Not used at the moment.

API Classes:
-------------------------------------
The purpose of the API classes is to streamline debugging separating BatClient
routines and coding functions to handle input and output similar to BatClient.

BPFApi: the interface class for APIs. There are two interfaces, BatClientAPI
  and ShellAPI which work a little bit differently.
BatClientAPI: Used when invoking the software from BatClient. The commands
  are redirected to BatClient etc.
ShellAPI: Used for invoking debugging commands.


Logic flow:
-------------------------------------
Flow goes approximately like this.
BatClient starts and invokes loadPlugin at BatClientAPI, which loads 
BatPathFinderUI into memory, and this in turn loads Area object into memory
and initializes windows etc.

User of BatClient invokes a command. This is sent to BatClientAPI which then
sends this command to BatPathFinderUI, which processes this command. If it's
a pathfinding request from a to b, it starts a new thread or two of the class
BatPathFinder, which processes this request.

BatPathFinder calculates the route using A* and when it finishes, it calls
a method in BatPathFinderUI, so that it now knows the search result and
informs the user. It also saves this result.

If the user command is a move request ("w go"), then BatPathFinderUI looks
at this saved path and sends appropriate commands to the BatClient to
perform this walk.

These command requests can also be invoked from the program window,
and the program also updates the window whenever the state changes.


Notable features:
-----------------------------------
The program supports 5 modes of travel:
Walking: Outputs the commands 50 at a time.
Riding (mounted): Outputs all the commands at once, because riders
  can't get exhausted.
Ship travel: Instead of regular movement costs, there are only movement
  costs for flying travel, sea travel, impassable terrain and tradelane
  travel. Lift level determines the terrain types that are impassable.
Intercontinental ship travel: Invokes two searches: one from the current
  location to the tradelane exit node for the destination map, and one
  from the tradelane entry node on the destination map to the destination.
  These are run on separate threads and when both are finished, the result
  is concatenated (with the appropriate movement from the exit to entry
  nodes)
Intercontinental esiris travel: Uses the special esiris portals for
  intercontinental walking. Use this only if you don't have any cooldown
  on the portals and can use them freely.
  
Navigator level determines how many commands can be performed on one
  invokation of "w go" when in ship travel mode.
  
  