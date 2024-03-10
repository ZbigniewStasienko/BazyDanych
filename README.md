# Java CRUD Application with SQLite Database

Project made in collaboration with [Dominik Serwatka](https://github.com/DominikSerwatka)

This Java application is a simple CRUD (Create, Read, Update, Delete) application that connects to an SQLite database using JDBC (Java Database Connectivity) driver. Users can perform all CRUD operations on the database.
The application operation scheme is as follows:

![KOMUNIKACJA](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/3208c89e-8c84-40fe-96b4-b54ca802657d)

## Database

We used SQLite - an embedded SQL database engine that operates directly with the application. Unlike traditional client-server databases, SQLite doesn't require a separate server process to be running.
Instead, it operates directly on the disk, making it "serverless". Below is the database design used in our application:

![MODEL_BAZY](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/2efc0cb8-a008-4198-aca5-fcfc1301d693)


## Usage

With our application, users can seamlessly communicate with the database through intuitive graphical user interfaces (GUI).
Our GUI provides a user-friendly environment where you can effortlessly perform all CRUD operations - Create, Read, Update, and Delete - with just a few clicks.
Navigate through your data effortlessly, add new data with ease, update existing records, and remove unwanted data  - all within the comfort of a visually appealing interface. 
Our GUI abstracts the complexities of database management, allowing users of all skill levels to interact with their data efficiently and effectively. 
Below is the welcome menu that appears when the application starts successfully:


![uno](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/d702be16-9e05-47f1-95ed-2da512365a82)


### 1. Create (C)

To add new player to database just press *Add Player* button. A window for entering data will appear. Enter data and press *Submit* button to add your player to database:

![dos](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/46e4cdb3-e5fc-44a1-a2a1-5242daa2826f)

After pressing submit button you have to refresh view. Button *Refresh* will turn green signalizing that some changes have occured:

![tres](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/9c059422-5323-4c41-b228-1ba533445bd1)

After refreshing view, your newly created player will be visible in GUI window.

### 2. Read (R)

The user can view data sorted according to various player parameters or according to the clubs to which the players belong. 
After pressing *View* menu bar we will see possible display options:

![quatro](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/0143c01d-372c-42a2-92db-8ff042ff85ed)

First three options sort data according to different parameters. Last option will show us all teams that are included in database:

![funf](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/6379574b-539b-4885-8618-56ecb8d2f4f2)

Now when we select for example *Atlanta Hawks* and we will press *Show Roster* button, GUI window will display for us list of all players that play for selected team:

![image](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/afc41915-ba89-47bd-9ede-9c0bbe0d442e)


### 3. Update (U)

If you want to update players data, you have to firstly list players and then select player you want to update and press *Update Player* button. After this new window will pop-up:

![sevenn](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/db1f15ea-d206-4dfa-9dc7-81bc0813b494)

Some of the fields are locked to maintain the originality of the player. After commiting changes refresh view similarly to adding a player operation.
### 4. Delete (D)

To delete player select player you want to remove and press *Delete* button. Following warning will pop-up:

![ocho](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/d23c07a5-0d3d-4624-987f-3f40987e8f81)

Since deleting is destructive operation, user have to confirm his choice. After commiting changes refresh view similarly to adding/updating player operation.

## Features

Project contains some basic protection against prohibited operations. For example:


-In database we are chcecking if stats are greater than or equal to zero.


-In Java application, we implemented function that accepts numers in two formats, for example number 12,3 and number 12.3 will be both correct.


-In the Java application, we implemented the adding player operation to be performed in one transaction.

We also implemented some basic JUnit tests to see if our application works properly. All unit tests were successful:

![nineee](https://github.com/ZbigniewStasienko/BazyDanych/assets/140521815/1beb5be3-6eda-4fc8-be84-f03e8780b96e)


## How to Run:

### Prerequisites:

Make sure you have Java installed on your system. You can check by opening a terminal and typing:

    java -version

If no version is shown, you can download it using the following [link](https://www.java.com/en/download/help/download_options.html)
### Running the Application:

1) Clone the repository.


```
git clone https://github.com/ZbigniewStasienko/BazyDanych
```


2) Download the jdbc driver (sqlite-jdbc-3.43.0.0.jar file) from [here](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.43.0.0/) and put it in the project folder (*BazyDanych* folder)

3) Open the project in the [Intellij](https://www.jetbrains.com/idea/) environment.

4) If you keep *BazyDanych* folder on driver that is **not the C driver** you will have to make some small changes in code before compiling project.

   In *Datasource.java* file in line 15 is defined connection string:

       public static final String CONNECTION_STRING = "jdbc:sqlite:C:..\\BazyDanych\\" + DB_NAME;
   
   If you keep *BazyDannych* folder for example on driver D, your connection string will look like this:

       public static final String CONNECTION_STRING = "jdbc:sqlite:D:..\\BazyDanych\\" + DB_NAME;

    Change the letter according to the drive where you have the folder.

5) Run the *MainApplication* class.

   
When you run the project, follow the instructions displayed in the GUI windows.
