Name of application: Faculty's Office Hours Manager
Version: 0.4

Who did what
- Nguyen Nguyen: Set up and connected the SQLite database; handled saving Time Slots to the database with input validation.
- Meghana Indukuri: Handled saving Courses to the database with duplication checks; contributed to input validation and bug fixes.
- Misbah Syed: Designed and implemented the UIs for Time Slots and Courses, along with their controllers; merged all main UI components.
- Oliver Majano: Implemented TableView display with sorting + ReadME.

Any other instruction that users need to know:
N/A

Running the Application

Make sure you have Java 23 or higher installed
Run mvn clean compile to build the project
Run mvn javafx:run to start the application
Using the Application

Dashboard: Main view showing overview of office hours

Office Hours:
   Click "New" to create new office hours
   Fill in required fields (Semester, Year, Days)
   Click "Save" to save the entry
   Click "List All" to view all office hours
Navigation:
   Use the menu button in top-left to switch between views
   Click the dashboard label to return to main view
