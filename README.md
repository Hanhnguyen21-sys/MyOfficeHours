# Name of application: Faculty's Office Hours Manager

# Version: 0.2

# who did what:

1. Meghana Indukuri - Dashboard FXML UI design and implementation
2. Nguyen Nguyen - Office Hours UI design and implementation
3. Oliver Majano - Office Hours Controller implementation
4. Misbah Syed - Dashboard Controller implementation

# Any other instruction that users need to know:
## Running the Application

1. Make sure you have Java 21 or higher installed
2. Run `mvn clean compile` to build the project
3. Run `mvn javafx:run` to start the application

## Using the Application

1. Dashboard: Main view showing overview of office hours
2. Office Hours:
   - Click "New" to create new office hours
   - Fill in required fields (Semester, Year, Days, Time, Course Code)
   - Click "Save" to save the entry
   - Click "List All" to view all office hours
3. Navigation:
   - Use the menu button in top-left to switch between views
   - Click the dashboard label to return to main view

## Note

- Data persistence is currently not implemented (saving/loading data)
- All form data is temporary and will be cleared when closing the application

