logtime 0 
// in the simulation. Be sure to include the full class name for the 
// The timeout statement indicates when the simulation will terminate in 

//
// seed number
// -1
// class.  That way when the simulated sensors of robots look for things
time 2.0 
-.5 0 0 x3399ff x444444 1 
//======

// from getting jumpy on slow machines, or when/if your process gets 
//======
// maxtimestep milliseconds 
	x000000 x000000 0

// 

object EDU.gatech.cc.is.simulation.ObstacleInvisibleSim 2.047 -1.4396 0 1.0
//
	x000000 x000000 0
object EDU.gatech.cc.is.simulation.ObstacleInvisibleSim -2.047 1.4396 0 1.0
//====== 
eastname EastTeam 
// You can used different colors to tell your team or individual 
// termination. 
//
// To simulate the corner panels, we use invisible 1 meter diameter 
// A background statement sets the background color for the simulation.
//======
//====== 
robot EDU.gatech.cc.is.abstractrobot.SocSmallSim t131417.SalamiTeam 
1.2 0 0 xff3333 xffff66 2 
.5 0 0 xff3333 xffff66 2 
// in the simulation.  Be sure to include the full class name for the
//------------your control system name goes here ^^^^^^^^ 
//====== 
// 
//====== 
xffffff x000000 3 
// The ball 
timeout 120000 // ten seconds 
// LOGTIME 
// 
// parameter is used to put each kind of object into it's own perceptual

// LOGFILENAME 
// run at half speed, "time 1.0" will cause it to run at real time, 
// 
// milliseconds. The program automatically terminates when this time 
// the same as the graphical area set aside by the simulation, then 
//
// 
// bounds left right bottom top
bounds -1.47 1.47 -.8625 .8625
// bounds statements set the bounds of the visible "playing field" in
//====== 

// 
// the robots may wander off the screen.  This will be fixed (eventually!)
// with respect to real time. "time 0.5" will cause the simulation to 
westname WestTeam 


// soccer game simulation in the JavaBotSim simulator.
robot EDU.gatech.cc.is.abstractrobot.SocSmallSim t070807.Clasicos 
maxtimestep 20 

// while "time 4.0" will run at 4 times normal speed. Be careful 
// obstacles whose centers are outside the playing field
//======
// SEED
robot EDU.gatech.cc.is.abstractrobot.SocSmallSim t070807.Clasicos 
robot EDU.gatech.cc.is.abstractrobot.SocSmallSim t070807.Clasicos 
//------------your control system name goes here ^^^^^^^^ 
// and BB is the blue.  For soccer, we use dark green.
object EDU.gatech.cc.is.simulation.ObstacleInvisibleSim 2.047 1.4396 0 1.0 
// the red component (00 for none, FF for full), GG is the green component,
// 
// numbers according to the order in which they are listed here. 
-.15 -.5 0 x3399ff x444444 1 
//
// 
//======
// object.  The x y and theta parameters set the initial position of 
// parameters are actually ignored for soccer robots because they 
// they can be sorted by this identifier.

// SIMULATION BOUNDARY
.15 .5 0 xff3333 xffff66 2 
background x009000
// swapped out. 
// These are the bounds of a RoboCup field "base."  The actual field walls
