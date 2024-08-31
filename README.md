
# MyPeople

MyPeople is an Android application designed to manage user profiles, including adding, updating, and displaying user information. It integrates with a backend API to fetch user data and utilizes Room for local database storage.

## Features

- **User Management:** Add, update, and view user profiles.
- **Paging:** Fetch user data in pages using Retrofit and Room.
- **Image Handling:** Upload and display user profile images.

## Getting Started

### Prerequisites

- Android Studio
- Java Development Kit (JDK) 8 or higher
- An Android device or emulator
### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/NoyChen1/MyPeople.git
   
   
### Challenges Encountered
1. **Image Uploading**
One significant challenge was implementing the image uploading feature in the Android app. Initially, the goal was to allow users to upload profile pictures which would be saved and displayed within the app. Here are the key issues faced:

**Handling Image URIs:** The app needed to correctly handle image URIs from the device’s storage.

**Saving Images Efficiently:** To manage user profile images effectively, it was necessary to save them in a way that persisted across app sessions.

**User Experience:** Ensuring a smooth user experience while uploading images was crucial. 

2. **Integrating the New Room Library**
Learning a New Library

Integrating the new Room library into the project was my first experience with this tool. This required a deep learning as I needed to understand the features and how to implement them effectively in the project.
