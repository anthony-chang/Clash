# Clash
 
Map building guide
------------------
Requires:
- .json file for map components (i.e. walls and obstacles)
- add level name (String) to ScrollPane list (located in LevelMenu.java)
- add if statement to call the correct .json file (located in GameScreen.java)
------------------

.json file:
There are four components for walls and obstacles: x, y, width, and height.

x and y values:
The x and y positions of the object.
The map boundaries are in a 16:9 ratio.
(0,0) represents the center of the map.
The coordinate corresponds to the center of the object.
(65,35) is roughly a corner of the map.

width and height:
Note that the object "expands" from the center of the object

Two client local testing
------------------
Running two instances of the same class (intelliJ)
- Go to Edit Configurations, click '+' and create a Compound Run Configuration
- Place two Desktop Configurations (identical) into the Compound Run Configuration
- Click 'Run' with Coverage (two buttons left of standard Run button)

------------------

#### Todo
- deploy node server off of localhost
- p1 generates a lobby code 
- create screen to enter lobby code to join a room
