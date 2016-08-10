# Android-Spacecraft-Game

About
-----

In this portrait-oriented app,
the user controls a spacecraft and attempts to navigate
through all of the levels by dodging enemies and rocks.

I programmed this from December of 2015 (during my winter break)
to April of 2016 (through the winter and spring quarters of my first year
in college) in Java with the help of Android Studio.

Here is a demonstrative YouTube video:
https://www.youtube.com/watch?v=8kL93vDL6ms

Here is the app (named "Outer Space Legion") on the Google Play store:
https://play.google.com/store/apps/details?id=enterprises.wayne.spacecraftgame&hl=en

Readme Contents
---------------

1) Features

2) Note on Device Resolution

3) Releases

4) How to Run App

5) How to Run Tests

6) Acknowledgements

7) Sources of Background Music

8) Author

9) Contact Information

1) Features
-----------

-Handling of two threads (the main game thread and the input thread).

-User's spacecraft reacts according to which half (if any) of the screen the
user is touching, even if he/she is touching both halves (causing no movement).

-Can choose between three spacecrafts that differ in speed and shape:
1) Default,
2) Cube (vertically faster but horizontally slower), and
3) Long (horizontally faster but dangerously long).

-Six kinds of enemies:
1) Dummies (includes Asteroids),
2) Hunters,
3) Zig-Zaggers,
4) Big Ones (i.e. big versions of dummies, asteroids, and hunters),
5) Fast Ones (i.e. fast versions of dummies and hunters),
6) Ambushers (unfinished).

-Sprites designed with Autodesk Inventor.

-Sound effects.

-Background music that changes according to level data.

-Each level has total goal distance according to level data.

-15 levels.

-A few unit tests. (Because of the nature of this game and my relatively low
experience with test-driven development, I missed a few
opportunities to add unit tests and functional tests.
I hope to not make this mistake again.)

-Enemy, player, and asteroid bitmaps are scaled based on device resolution.

-No significant frame rate issues (at least on my device). I tested with
100 hunters.

2) Note on Device Resolution
----------------------------

Because this was a no-budget game, I couldn't test it on devices
of different resolutions than my smartphone's.
The AVD Manager of Android Studio lets me create an emulator for a
high-resolution tablet, but any emulator with a high enough resolution
always freezes when I switch its
orientation (which is unfortunately landscape by default).
Enemy, player, and asteroid bitmaps are scaled based on device resolution,
but I could only test on my smartphone and on the BlueStacks emulator.
Thus, the game should work for all resolutions, but I have no
way of knowing for sure. Furthermore, the scale still seems easier
on the BlueStacks emulator (which has a higher resolution than my
smartphone does).


3) Releases
-----------

Latest release: v1.1

Please see the releases section under this repository on GitHub
to download the latest release.

4) How to Run App
-----------------

In Android Studio, run 'app' in 'enterprises.wayne.spacecraftgame'.

5) How to Run Tests
-------------------

In Android Studio, run 'Tests' in 'enterprises.wayne.spacecraftgame'.
The Android device that you use to run the tests must be unlocked (even
if the device is an emulator); otherwise, at least one of the functional
tests will always fail.

6) Acknowledgments
------------------

"Android Game Programming by Example" by John Horton helped me decide
which classes to use. Many of this game's classes were inspired
by the classes of the Tappy Defender game demonstrated in his book.
Furthermore, the sound effects in this game came from Tappy Defender's
source code. However, this game still differs greatly from Tappy Defender.

"Testing and Securing Android Studio Applications" by Belén Cruz Zapata
and Antonio Hernández Niñirola helped me set up the unit tests.

I made (and exported images of) sprites with Autodesk Inventor and edited
images with www.online-image-editor.com.

7) Sources of Background Music
------------------------------

(Note that I make no money from this app.)

The track "Hurry" is from the video game Super Mario Galaxy 2.
The track "King Galaxian" is from the video game Pac-Man World.
The "Saber Wing" tracks were produced by TAG.
The track named "pac-man-champ.ogg" is from the video game
Pac-Man Championship Edition DX.

8) Author
---------

That is me, Aaron Kaloti.

9) Contact Information
----------------------

My email address: aarons.7007@gmail.com

My YouTube channel (in which I demonstrate my finished applications):
https://www.youtube.com/channel/UCHhcIcXErjijtAI9TWy7wNw/videos