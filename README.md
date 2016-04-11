# Android-Spacecraft-Game

About
-----

In this app, the user controls a spacecraft and attempts to navigate
through all of the levels by dodging enemies and rocks.

I programmed this from December of 2015 (during my winter break)
to March of 2016 (during the winter quarter of my first year in college).

I programmed it in Java with the help of Android Studio.

Features
--------

-Handling of two threads (the main game thread and the input thread).

-User's spacecraft reacts according to which half (if any) of the screen the
user is touching, even if he/she is touching both halves.

-Five kinds of enemies:
1. Dummies and Asteroids
2. Hunters
3. Zig-Zaggers (has slight problems)
4. Big Ones (i.e. big versions of dummies, asteroids, and hunters)
5. Ambushers (unfinished)

-Sprites designed with Autodesk Inventor.

-Sound effects.

-Background music that changes according to level data.

-Each level has total goal distance according to level data.

-Five levels.

-A few unit tests. (Because of the nature of this game and my relatively low
experience with test-driven development, I missed a few
opportunities to add unit tests and functional tests.
I hope to not make this mistake again.)

-No significant frame rate issues (at least on my device). I tested with
100 hunters.


Releases
--------

Latest release: v0.3

Please see the releases section under this repository on GitHub
to download the latest release.

How to Run App
--------------

In Android Studio, run 'app' in 'enterprises.wayne.spacecraftgame'.

How to Run Tests
----------------

In Android Studio, run 'Tests' in 'enterprises.wayne.spacecraftgame'.
The Android device that you use to run the tests must be unlocked (even
if the device is an emulator); otherwise, at least one of the functional
tests will always fail.

Acknowledgments
---------------

"Android Game Programming by Example" by John Horton helped me decide
which classes to use. Many of this game's classes were inspired
by the classes of the Tappy Defender game demonstrated in his book.
Furthermore, the sound effects in this game came from Tappy Defender's
source code. However, this game still differs greatly from Tappy Defender.

"Testing and Securing Android Studio Applications" by Belén Cruz Zapata
and Antonio Hernández Niñirola helped me set up the unit tests.

I made (and exported images of) sprites with Autodesk Inventor and edited
images with www.online-image-editor.com.

Sources of Background Music
---------------------------

(Note that I make no money from this app.)

The track "Hurry" is from the video game Super Mario Galaxy 2.
The track "King Galaxian" is from the video game Pac-Man World.
The "Saber Wing" tracks were produced by TAG.
The track named "pac-man-champ.ogg" is from the video game
Pac-Man Championship Edition DX.

Author
------

That is me, Aaron Kaloti.

Contact Information
-------------------

My email address: aarons.7007@gmail.com

My YouTube channel (in which I demonstrate my finished applications):
https://www.youtube.com/channel/UCHhcIcXErjijtAI9TWy7wNw/videos