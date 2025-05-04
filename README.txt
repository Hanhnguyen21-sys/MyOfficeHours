Name of application: Faculty's Office Hours Manager
Version: 0.8

Who did what
- Nguyen Nguyen: Design UI dashboard when adding edit schedule
- Meghana Indukuri: Project integration + Populate requirements  + MVC patterns + Debugging
- Misbah Syed: ReadMe +  Polymorphism implementation
- Oliver Majano: Implement edit schedule method


Polymorphism Usage:
Parent interface - SceneSwitcher
Concrete child classes (12 total) - CourseListSwitcher, CoursesSwitcher, DashboardSwitcher, EditScheduleSwitcher, OfficeHoursListSwitcher, OfficeHoursSwitcher, ScheduleListSwitcher, ScheduleSwitcher, SearchSwitcher, SwitchScene, TimeSlotsListSwitcher, TimeSlotsSwitcher
    - Polymorphism is implemented through the use of the SceneSwitcher interface.
    - Multiple scene-switching classes implement the same interface but define their own specific scene transitions.
    - The Controller classes use the SceneSwitcher interface as a common type to invoke scene changes without needing to know which specific subclass is being used.
    - Polymorphism allows for flexible and extensible scene navigation logic in the project.


Any other instruction that users need to know:
N/A

Running the Application

Make sure you have Java 23 or higher installed
Run mvn clean compile to build the project
Run mvn javafx:run to start the application
Using the Application


Dashboard: Main view showing overview of office hours

Navigation:
   Use the menu button in the top-left to switch between views
   Click the Dashboard label to return to the main view

   To Add Office Hours:
      - Click the "Office Hours" tab
      Fill in required fields:
         - Semester
         - Year
         - Days
      Click "Save" to save the entry
      Click "List All" to view all office hours
   
   To Add Time Slots:
      - Click the "Time Slots" tab
      Enter the following:
         - From Hour (e.g., 10:30)
         - To Hour (e.g., 11:30)
   Click "Save" or confirm the entry
   Click "List All" to view all time slots

   To Add Courses:
       - Click the "Courses" tab
      Enter course details:
         - Course Name (e.g., CS 146)
         - Course Code
         - Section Number
   Click "Save" to add the course
   Click "List All" to view all courses

    To Add Office Hour Schedule:
        - Click the "Schedule" tab
        Enter schedule details:
            - Student Name (e.g., Doug)
            - Schedule Date
            - Course
            - Reason
            - Comment
    Click "Save" to add the schedule
    Click "List All" to view the schedule

    To Search Schedule:
        - Click the "Search" tab
        - Search Student Name
    Click "Enter" to search
    View the schedule

    To Delete from Schedule:
        - Click the "Search" tab
        - Click Entry
    Click on Delete Button

    To Edit Schedule:
        - Click on the "Edit Schedule" tab
        - Click on Entry
        - Click "Edit"
        - Click on "Ok" after changes are made



