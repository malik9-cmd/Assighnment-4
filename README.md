# Android Mini Project - Assignment 4

This is an Android application developed as part of Assignment 4. It demonstrates key Android development concepts including API integration, local persistence with SQLite, dynamic theming, and user state management.

## Features

### 1. Theme Management
- **Multiple Themes**: Supports Light, Dark, and a Custom Orange theme.
- **Dynamic Switching**: Users can switch themes at runtime via the Options Menu.
- **Persistence**: The selected theme is saved using SharedPreferences and applied on app restart.

### 2. User Authentication (Simulation)
- **Login Screen**: Simple login interface.
- **Persistent State**: Stores the login status (`isLoggedIn`) to keep the user logged in across sessions.
- **Auto-Redirect**: Redirects to the Main Activity if the user is already logged in.

### 3. API Integration & Offline Access
- **REST API**: Fetches dummy post data from `https://jsonplaceholder.typicode.com/posts`.
- **Retrofit**: Uses Retrofit for efficient network requests.
- **SQLite Database**: Caches fetched data locally.
- **Offline Mode**: If the network fails, data is retrieved from the local SQLite database.

### 4. WebView Integration
- **In-App Browser**: Opens detailed content in a WebView without leaving the app.
- **Loading Indicators**: Shows a progress bar while the web page is loading.

## Navigation Flow

1.  **Launch**: App starts.
    *   If **Not Logged In** -> `LoginActivity`
    *   If **Logged In** -> `MainActivity`
2.  **Login Activity**: User enters credentials and clicks 'Login' -> Navigates to `MainActivity`.
3.  **Main Activity**:
    *   Displays a list of posts.
    *   **Options Menu** (Top Right):
        *   **Theme**: Select Light, Dark, or Custom. App recreates to apply.
        *   **Logout**: Clears session and navigates back to `LoginActivity`.
    *   **Click Item**: Tapping a post navigates to `WebViewActivity`.
4.  **WebView Activity**: Displays the URL associated with the post. Press 'Back' to return to `MainActivity`.

## Tech Stack
-   **Language**: Kotlin
-   **IDE**: Android Studio
-   **Libraries**:
    -   Retrofit (Networking)
    -   Gson (JSON Parsing)
    -   Material Components (UI)

## Setup
1.  Clone the repository.
2.  Open in Android Studio.
3.  Sync Gradle and Run.
