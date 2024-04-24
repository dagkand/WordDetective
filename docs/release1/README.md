# WordDetective: Substring Edition - release 1

This is the initial release of WordDetective: Substring Edition.
WordDetective: Substring Edition is designed to be a brainteaser, which intrigues and promotes learning.
This release is both buildable and testable.
Primarily, we have focused on implementing the core functions of the application.
This release lets the user access the application as a guest, and thereby access the default categories.
So far, the only default category is a wordlist containing all the countries in the world.
The application randomly chooses a word from the wordlist, and serves the user with a randomly generated
substring from this word.
If the user guesses a word which contains the given substring, in addition to the wordlist containing
the guessed word, the score of the user increments. The new score is then written to persistent memory.
Upon opening the application once again, the user will be able to continue with the score they left with.

The requirements for the first release have been fulfilled in the following manner:

- 1: The project is situated in the given gitlab repository.
- 2: The root folder of the project folder contains a "docs" folder, which contains folders to the various releases.
- 3: We have created and continuously used a gitlab milestone named "Minimal JavaFx application".
- 4: The projects is rigged with maven. The various mvn commands for usage can be found in the root level readme.
- 5: The root folder contains a readme file explaining the content of the folder structure. The code reposity is clearly stated.
- 6: The code repository contains a README with a description of the intended functionality of the finished project, and a simple sketch which illustrates what the finished project might look like. This README also contains one user story intended to unravel the demands of the application.
- 7: We have connected issues for every performed task to the milestone in gitlab.
- 8: We have implemented the various architechture layers in the following manner:
  - Domain logic: Initialization triggers a method to randomly pull a word out of the wordlist, and provide a substring from this word. Furthermore, another method is used to check whether or not the guessed word is valid by comparing the guess with the substring, in addition to querying the wordlist to make sure the word is present.
  - User interface: The user first meets a log in page, which currently only allows the user to press "log in", signaling that the user is a guest user. Furthermore, the user interacts with the gamepage, for guessing of words.
  - Persistent storage: The application reads the current highscore before starting the game, and stores the updated score in a persistent file after the game is over. Thus the application reads and writes to persistent memory. Furtermore, the wordlist is stored in a persistent file, where the application reads this file.
- 9 : The application supports various maven commands, including "mvn javafx:run". See root level readme for list of supported maven commands.
- 10: Currently, there are 8 tests divided into two testfiles. All the tests get performed by "mvn test". In addition to this, we have implemented jacoco. See root level readme for jacoco run command and retrieval of results.
- 11: The root level readme does indeed contain information about required versions and dependencies.
