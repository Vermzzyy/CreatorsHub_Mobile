# TestApplication

An Android mobile application showcasing secure authentication and profile management using a Supabase backend integration.

## Features

- **User Authentication**: Secure Login and Registration screens integrated with Supabase Auth.
- **Profile Management**: View and modify user profile details such as First Name and Last Name.
- **Change Password**: Update user passwords securely.
- **Avatar Uploads**: Pick an image from your device and upload it to Supabase Storage as your profile picture.
- **Auto Login Flow**: The app securely stores authentication tokens locally (SharedPreferences) to automatically authenticate the user on subsequent app launches.

## Tech Stack

- **Android / Kotlin**: Core application language and framework.
- **Retrofit & Gson**: Handling REST API requests to the Supabase backend.
- **Glide**: Efficiently loading and caching the avatar images from Supabase Storage.
- **Supabase**: 
  - Authentication (JWT based login/registration)
  - PostgREST API (Database CRUD operations for User Profiles)
  - Storage (User Avatar uploads)

## Setup Instructions

1. Clone or download the repository to your local machine.
2. Open the project in **Android Studio**.
3. Let Gradle complete its sync so `Retrofit` and `Glide` dependencies are resolved.
4. Ensure you have an active emulator or physical Android device connected.
5. Click **Run** (`Shift + F10`) to build and launch the application.

## Testing Profiles

By default, executing a successful login or app launch with a stored token will currently direct the user to the `SettingsActivity`. This helps bypass any unneeded views so you can test the Profile, password changes, and Avatar uploads directly.
