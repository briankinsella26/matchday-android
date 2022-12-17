# matchday-android

Student Number: 16728365
Student Name: Brian Kinsella

An android application that can record match details throughout the season.

Functionality
- Create users and matches.
- Record results.
- Provide match information such as location, start date/time
- Sleek UI with google and basic login
- Text input animations
- Display user information
- Access google account information
- upload new avatar image
- Swipe right (edit) and left (delete) functionality on cards
- Latest android SDK, swipe left for back
- Switch to filter by league games only, or all games
- Persistent database for users & matches via Firebase Realtime DB
- Storage for avatar images via Firebase Storage
- Clean MVVM Design pattern
- Uses navigation component
- Nav drawer
- Day/Night/Default mode options
- Best practice git history, feature branches, commits and PRs
- Develop branch employed, along with a Release (main) branch

Tech 
- IDE- Android Studio
- Platform - Android
- Language - Kotlin & xml 
- Database - Firebase Realtime Database https://firebase.google.com/
- Storage - Firebase Storage https://firebase.google.com/

Assets
- Icons - https://fonts.google.com/icons
- Splashpage image
  <a href="https://www.freepik.com/free-vector/gradient-match-day-label-set_28899251.htm#query=matchday&position=1&from_view=keyword">Image by pikisuperstar</a> on Freepik

References
- Circular spinner xml obtained from <a href="https://www.geeksforgeeks.org/material-design-components-progress-indicator-in-android/>progress indicator</a> on geeksforgeeks.org
- Dark Mode code obtained from <a href="https://proandroiddev.com/dark-mode-on-android-app-with-kotlin-dc759fc5f0e1"/>Dark Mode</a> on proandroiddev.com
- Code obtained from wit.ie

UK / DX
The UX is minimal, sleek and fast. I chose a green theme to play on the colour of a pitch, however, the application itself can be used for any sport
The login page uses textlayoutinput animations and rounded text input corners, and gives the user a premium feel on their very first interaction with the application. Information 
is retrieved from the DB without any long queries, making the application very responsive. 

Users interacting with the Matchday app will feel connected, and trust that they can store and retrieve their match data without fuss. The application focuses on the DX of 
convenience through simplicity, while providing customisation options when required.

Notes
The application was challenging. As the initial version employed activities for every view, and with no specific design pattern, the change to MVVVM was a significant refactor.
Also, I updated the min SDK to the latest and greatest midway through the project which came with its list of dependency related problems. 
There was some dependency/versioning issues that took some time to resolve also, such as the safeargs plugin was preventing initial connection to firebase realtime db. Disabling
the plugin, connecting to firebase realtime DB and then re-enabling the plugin allowed for this. The application is robust, and does what it is supposed to do. If I can get time, I would
like to extend the functionality of the application to include teams, leagues and players.

This application is intended for education purposes only