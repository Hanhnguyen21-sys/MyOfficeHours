# Name of application: Faculty's Office Hours Manager

# Version: 0.4

# Who did what

- **Nguyen Nguyen**: Built and connected the SQLite database, and handled inserting user input into it.
- **Meghana Indukuri**: Handled data validation and duplication check, bug fixes
- **Misbah Syed**: Updated UI for office hours and added TableView.
- **Oliver Majano**: Implemented database function, wrote README, bug fixes

# Any other instruction that users need to know:

N/A

## Running the Application

1. Make sure you have Java 23 or higher installed
2. Run `mvn clean compile` to build the project
3. Run `mvn javafx:run` to start the application

## Using the Application

1. Dashboard: Main view showing overview of office hours
2. Office Hours:
   - Click "New" to create new office hours
   - Fill in required fields (Semester, Year, Days)
   - Click "Save" to save the entry
   - Click "List All" to view all office hours
3. Navigation:
   - Use the menu button in top-left to switch between views
   - Click the dashboard label to return to main view
