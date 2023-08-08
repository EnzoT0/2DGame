# 2D Style Game

## Project Design

I propose to design a simple 2D game for the personal project this term. 

### **What will the application do?**

It will be a simple 2D game. The player-controlled character will be able to explore the world and interact with objects such as treasures, and it will include several functions and features such as an inventory system and a coin based currency system that I hopefully plan to use for a shop feature. It will also include a player-controlled and there will be a combat system as well where the player can engage in battles against enemies. In conclusion, this application will therefore simulate a simple 2D game, including all the typical features such types of games has.  

### **Who will use it?**

The game that I am making will have a wide target audience, such as:

- Gamers who generally enjoy 2D games.
- Potential developers and employers during an interview or internship who may want to find out what kind of game I created (eg. its style and gameplay for example).
- Students who are studying in this field as they may find value in going through the code used to make the game. It can thus be used for educational purposes if done correctly. 
- Indie users who look through unique and creative projects created individually.

### **Why is this project of interest to you?**

Constructing a 2D game in Java is of interest to myself due to several reasons.

- First of all, developing a game allows myself to unleash my creativity and express my own ideas through the game's design, story and gameplay. I have always wanted to make my own game with my own thoughts, propelling me to continue on with **my** own story and ending and this project gives me exactly that as it allows me to create a unique world that was brought to life according to my vision.


- Creating a game requires heavy programming skills, which is why working on such a project can help me enhance on such skills. Getting practical experience in areas like gameplay mechanics, collision detection, and user interfaces will help me continue to develop my talents in these areas, which may or may not be helpful in the future if I do decide to pursue my passion in this industry as well. I will also be able to develop project and time management skills, as well as the ability to work independently through this which are areas that are good to learn ahead of time in general.


- Lastly, completing or even having a rough copy of a game is an incredible achievement in my opinion and this as a result can serve as a valuable addition to my portfolio. It will demonstrate my programming abilities and dedication towards this project and this can help me by giving myself something to talk about to any potential employers in the future once I decide to do co-op.

## User Stories

### First User Stories Needed to be Added:

- As a user, I want to have an inventory system that allows me to collect items.
- As a user, I want to encounter enemies with unique behavior patterns.
- As a user, I want to add a HP bar that slowly decreases if hit by enemies.
- As a user, I want to add a coin-based currency system that the player can collect.
- As a user, I want to have the option to save my game.
- As a user, I want to be able to load my saved game and continue where I left off.

### User Stories that I would like to implement:

- As a user, I want to add a weapon to my character's inventory, allowing me to equip it.
- As a user, I want to add a health potion to my character's inventory, enabling me to heal my character's HP (health points).
- As a user, I want to add a new location to the list of locations available for the player-controlled character to travel to.
- As a user, I want a shop or tavern system that can be used to trade in the currency that the player currently has for items.

### Instructions for Grader:

- You can generate the first required action related to adding Xs to Y by clicking on the button, Inventory, on the right side of the game screen. This shows all the items that you currently have collected.
- You can generate the second required action related to adding Xs to Y by clicking first clicking on the Inventory button. There, you can click more buttons to use the items that you currently have. It disappears once you have used all the remaining quantity.
- Another action related to adding Xs to Y, just not on the treasure and inventory section of the code this time, is by clicking the add an enemy button where it will add a new enemy class to the list of enemy class.
- You can locate my visual component by clicking on the inventory button, where each item you collect displays a picture related to said item. There are more just from the drawing portion of the game as well such as enemy and user projectiles.
- You can save the state of my application by pressing on the save button on the right hand of the game screen.
- You can reload the state of my application to a saved game file by pressing the load button on the right hand of the game screen. 

### Phase 4: Task 2

Representative sample of the events that occur:

Mon Aug 07 23:02:29 PDT 2023 Added treasure: Sword

Mon Aug 07 23:02:35 PDT 2023 Added treasure: Health Pot

Mon Aug 07 23:02:43 PDT 2023 Added treasure: Meat

Mon Aug 07 23:02:46 PDT 2023 Added treasure: Bomb

Mon Aug 07 23:02:49 PDT 2023 Added a coin.

Mon Aug 07 23:03:00 PDT 2023 Added treasure: Twig

Mon Aug 07 23:03:05 PDT 2023 Character has increased their attack to 7

Mon Aug 07 23:03:07 PDT 2023 Character's hp has changed to 100

Mon Aug 07 23:03:08 PDT 2023 Character's hp has changed to 90

Mon Aug 07 23:03:12 PDT 2023 Character has increased their attack to 8

Mon Aug 07 23:03:17 PDT 2023 Added an enemy

### Phase 4: Task 3

If I had more time to work on the project, I would have refactored my game class and split the methods to more classes that have only one role because as of right now, my game class consists of the bulk of the code, consisting of different roles like adding an enemy, removing one, shooting, etc. I would split the code to its perspective class (or made a new one entirely) if I had more time. Another thing I would refactor if I had more time is abstracting some of my classes as some of them (eg. game and projectile for instance) have basically similar methods and I think that abstracting some parts of the code and using extending these classes to it is a great way to reduce the amount of code I have to keep copying. 