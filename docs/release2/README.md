# WordDetective: Substring Edition - release 2

This is the second release of WordDetective: Substring edition.
In release 2, our focus has been on modularization and a clean architectural layer. As an improvement from release 1, methods processing logic has been moved into the core folder in order to improve and enhance the modularization. Furthermore, the persistence layer has been expanded with more functionality. Formerly, the user could only persistently store their high score to a shared high score list. In this release, users can create their own profile, which is done by creating a new folder structure for each user. From there, every user is able to interact with their respective file, all done through persistent storage in JSON format. We chose to go with Gson for handling the JSON files.

The requirements for the second release have been fulfilled in the following manner:

- **Modularization**: In this release, the program is divided into the "core" and "ui" modules. The "core" module contains both the Java files handling the main logic, as well as the persistence layer, which handles the JSON files. The "ui" module contains the FXML files and their respective controllers.

- **Architecture**: We have implemented a full three-layer architecture by separating the different layers into separate modules and files. The UI layer has its own module, while the files controlling the persistent storage are separated into separate files. We have used JSON for the persistent storage, utilized with Gson. For the document metaphor, we have chosen to store the data locally on the user's computer, since this gives the user a feeling of security over their own data. At the same time, we have implemented implicit storage since data is automatically saved to persistent storage once it is updated.

- **Code Quality**: We have written tests for all modules and have achieved 80% test coverage in the core module and 71% test coverage in the UI module. As 80% is considered the benchmark for good test coverage, we are satisfied with this number. Since the UI module contains several void animation methods that are impractical to test, we have concluded that 71% test coverage in this module is reasonable. The JaCoCo report for the core module is available in `core/target/site`, while the JaCoCo report for the UI module is available in `UI/target/site`. Furthermore, we have implemented Checkstyle and SpotBugs tests in addition to using mock objects from Mockito during testing.

- **Documentation**: We have updated the ([WordDetective README](../../WordDetective/README.md)) to be up-to-date with the current release.
  In addition to the explanation of folder structure in the root level readme, the architecture is displayed in [this PlantUML diagram](PlantUML_release2.png).
  For information regarding work routines, please see the section named "Work routines".
  For information regaring code quality, please see the section named "Code quality".
  Finally, we have documentation for release 2 (this readme).

- **Work routines**: We have continiously used a gitlab milestone named "Moderate JavaFX application - Release 2", and assigned issues for every task to complete. During this release, we also implemented "Co-working", by splitting the group into pairs, where one part processsed the information, while the other did the coding.
  This has been documented in the commit messages by "Co-authored by:".

- **Extra**: We have expanded the section of userstories, in order to develop a program better suited to various audiences. The userstories are situated [here](../../userstories.md).
