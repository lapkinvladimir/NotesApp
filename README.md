# NotesApp

**NotesApp** is a web application that simplifies the process of creating, viewing, and managing notes (reminders). It offers users an intuitive web interface for interacting with their notes. Below is a concise overview of **NotesApp**'s features:

## Features

1. **Registration and Login:** Users can easily register with their username and password or log in using existing credentials.

2. **Creating Notes:** Create new notes with titles and textual content.

3. **Viewing List of Notes:** After logging in, users can access a list of their notes, displaying brief details such as titles.

4. **Viewing Full Note:** Select any note from the list to view its complete content, including the title and text.

5. **Editing Notes:** Registered users have the ability to edit existing notes, allowing changes to titles and text.

6. **Deleting Notes:** Users can remove notes they no longer need.

7. **Sorting and Filtering:** Additional functionality can be added for sorting and filtering the list of notes, streamlining the search process.

8. **Authentication and Authorization:** **NotesApp** ensures security through user authentication (verifying credentials) and authorization (defining access rights).

9. **User-Friendly UI/UX:** The user interface is designed to be simple and intuitive, ensuring a seamless user experience.

## Technologies Used

### Frontend

- **HTML and CSS:** **NotesApp**'s frontend is constructed using HTML for content structure and CSS for styling.

- **JavaScript:** JavaScript enhances the frontend, enabling dynamic updates, user interactions, and form handling.

### Backend

- **Spring Boot:** **NotesApp** leverages Spring Boot, a Java framework for rapid application development. Spring Boot simplifies the setup and configuration of various Spring modules.

- **Spring Web:** The Spring Web module facilitates the creation of RESTful APIs, handling HTTP requests and responses to build the backend of the web application.

- **Spring Security:** Integrated Spring Security manages authentication and authorization, ensuring secure access to the application and its resources.

- **H2 Database:** **NotesApp** uses the H2 in-memory database to store user account details and notes, simplifying data management without requiring an external database setup.

## Development Tools

- **Gradle:** Gradle is employed for project management, dependency resolution, and application building.

- **Integrated Development Environment (IDE):** IDEs such as IntelliJ IDEA or Eclipse are often used for coding, debugging, and project management.

## Deployment and Hosting

- **Heroku, AWS, or Similar:** The application can be deployed on platforms like Heroku or AWS for hosting, making it accessible on the internet.

## Workflow

- The frontend and backend communicate through RESTful API endpoints.

- Users interact with the frontend UI to perform actions such as registration, login, note creation, and editing.

- The frontend UI makes API calls to the backend, which processes the requests and communicates with the H2 database for data storage and retrieval.

- Spring Security ensures proper authentication and authorization for accessing different parts of the application.

- The application can be run locally during development and deployed to a hosting platform for public access.

In summary, **NotesApp** combines Spring Boot, Spring Security, an H2 database, frontend technologies like HTML, CSS, and JavaScript, along with development tools and deployment platforms, to create a user-friendly web application for efficient note management.
