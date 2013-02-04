BatPathFinder v1.12
Copyright Teppo Kankaanpaa (Slobber)
--------------------------------

This is a complex piece of software with little ingame help so please read at least 
sections 1 and 2 of this document before using.

1. Installation

- Copy the jar file into the plugins folder in your batclient directory.
- Copy the BatPathFinderData directory to location 
  c:\Documents and Settings\(your username here)\batclient\BatPathFinderData.
- Copy w.bcs to the scripts folder in your batclient directory.
- Restart the client.

2. Quickstart.

2.1. Super quickstart.

---------------
(Standing at shadowkeep portal)
$w skeep Old_Forest
(wait a bit until the path is found)
$w go
$w go
... until you're at the Old Forest
----------------
(Standing in front of Old Forest)
$w map roth
map
$w beastmaster
$w gogo
-----------------
(Standing at south crossing)
$w map laenor     (no need to type this every time, only when changing continent)
map
$w digga'sdomain
$w gogo
------------------

2.2. Detailed quickstart

You can either use the window, or the command line interface. Open the window with "$w win".

Basically, you set your current location and another location where you want to be, and hit the
Find path button. If the path looks OK, hit Go button which goes 50 steps on the path. Press it as
many times as it takes you to reach the destination. 50 steps limit is to prevent accidental runs to
water or other mistakes, so that you spot the error in time.

If you type 'map', the program tries to find your location on the map. It might not be able to determine
your exact location, but if it does, it automatically sets your current location in the program, so
you only have to type in the destination.

Location can be either "L <continent> <x> <y>" in the bare coordinate system of the map, which you
probably won't have to use manually. It can also be an area name, usually equal to the area names in the realm map
window, lower or upper case, with or without spaces, or spaces converted to _. ("Faerie forest",
"faerieforest" and "fAeRiE_foRest" are all ok.)

It can also be a node outside the map (eg. "church"), or a shorthand name (alias) for an area, 
if such a name is defined.
See chapter 2.2 on defining your own nodes outside the map and shorthand names.

Accepted command line commands are 
"$w help"
"$w get <dest>"
"$w get <source> <dest>"
"$w go"
"$w gogo"                  (turbo go)
"$w win"                   (shows program window if you closed it)
"$w map <continent>"
"$w map"
"$w reload"                (reloads data files into memory)
"$w alias <alias>"
"$w mode <travel mode>"    (parameter is "walk","mount","ship" or "ic")
"$w <source> <dest>"       (only if none of the previous commands match)
"$w <dest>"

Source and dest must always be without spaces, so you have to use the name with spaces removed or
converted to _.

"$w alias <alias>" is a quick way to define shorthand aliases for areas you commonly visit. It looks
at the "from" field in the program window, and defines an alias to this location. The location
must be either "L c x y" or a location name which is on the map, not inside areas ("Oakvale"
is ok, "church" isn't).

This alias is automatically written in the file <continent>.loc.private in the data directory
of this program. You can manually edit this file as well.

3. Details on data files, outside nodes and shorthand names.

You can skip this chapter if you don't want to know the details or customize your installation
just yet.

The program stores its data files in BatPathFinderData folder in the batclient directory. For each
continent there are four files: map, loc, loc.extra, loc.private, nodes, nodes.private.

The program has two kinds of locations, "map nodes" and "external nodes". Map nodes are on the map
and external nodes are outside, like church is.

It has also three levels of "officiality". On the top level are the official map and location
files from Bat and Ggr/Jeskko mapping project. Files *.map and *.loc are on this level.
On the middle level are additional files compiled by the author and distributed with this software.
Files *.loc.extra, *.nodes, tradelane.txt, costs, costs.ship, laenor.shipnodes are on this level.

Finally on the bottom level are user defined locations, aliases and external nodes like your
own start location and your favourite idling spot. These files will not be overwritten with
a new version of the files. The files are *.loc.private and *.nodes.private.

Map files are downloaded ascii maps from the batmud site and loc files are almost directly copied from
Ggr's site, which allows painless updates when new areas come into game. Loc file contains, 
for each area, fields x, y, flags, name. These nodes are all map nodes. It also contains other 
info but it's not used. Flags are only used to check that the area in question is not a pcity or 
another city.

Extraloc file has the same format as loc file, and you can define your own shorthand locations
there, for example "sc" for south crossing. Flags field should just be 0. Also these nodes are
the so called map nodes.

Nodes file contains the so called external nodes and paths which are not on map, but inside 
areas, pcities etc. This file can only reference to other locations with no spaces in the names, 
so you first have to define a shorthand (in extraloc file) for a location whose name contains 
a space, or use its space-stripped name.

In the beginning of the file you have to declare all the external nodes in a format
! external_node closest_map_node
Then you can define paths between these external nodes, map nodes and each other. You could
also define a path between two map nodes if you wanted, but it can lead to suboptimal routes.
All paths defined in this file are considered to be extremely cheap to walk on.

There is also a "costs" file, which contains the costs for each terrain type. There are several
mistakes and guessed values in the file, and you can tune it if you feel like it.

4. Mounted mode and ship modes

You can choose a different travel mode on the program window or with the command "$w mode <mode>"

In mounted mode you will run the whole path in one go, because with a good mount it doesn't matter if
you run into the ocean accidentally. You can force this with "$w gogo" command if you are in 
the walk mode.

In the standard ship mode the program takes into account that ships go faster in the water and
super fast on trade lanes. Also, when you 'walk' when in ship mode, the program sends a cruise
command instead. You can set your navigator level at the program window, drop-down box "N" 
(defaults to 4), in which case the program uses the maximum available commands for that 
navigator, plus a *secure in the end.

You can also set the lift mage power your ship has (the sum of all lift mages on the ship,
minus some penalty for possible damage). This is used to determine what kind of landscape can
you travel across. You can edit costs.ship file if the limit lift levels for different terrain
types seem incorrect. The program doesn't recognize "close shave" situations where the lift is
very close to limit lift and travel is much slower than normally. If you want to avoid this,
set your lift power to a lower number that it actually is.

"Ship IC" is intercontinental ship travel. With this mode the from and to locations can be
on different continents. The ship finds automatically the shortest path to the
tradelane which takes you to the target continent using Jeskko's tradelane checkpoint names,
and then to the desired destination on this continent.

The program reads map normally if you do it in the outworld, but in ships you have to use
"$w map" command to force it to read the map. It can be used on the deck and in the nest.

5. Feedback

Please. Tell me (Slobber) if you use this software and I might continue development sooner than later.

6. Copyright and stuff

Mostly GPL. Source code is available at http://www.helsinki.fi/~tvkankaa/bat-plugins.html

Some data files are copyrighted to their respective owners -- .map files to BatMUD and 
.loc files to GPL.
