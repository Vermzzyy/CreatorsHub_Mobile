# CreatorsHub_Mobile

<img width="351" height="672" alt="Screenshot 2026-03-28 094906" src="https://github.com/user-attachments/assets/37f95974-a9d5-45f5-9eb6-21e186ce9a0c" />

<img width="351" height="700" alt="Screenshot 2026-03-28 095718" src="https://github.com/user-attachments/assets/0d4c532d-9f79-4638-9b55-662f400a2645" />

<img width="345" height="699" alt="Screenshot 2026-03-28 095744" src="https://github.com/user-attachments/assets/953d1f14-520e-47b6-8062-f9c957661005" />

<img width="359" height="680" alt="Screenshot 2026-03-28 095048" src="https://github.com/user-attachments/assets/72cf12e6-18a7-44c7-82ee-2b86546e2662" />

<img width="348" height="712" alt="Screenshot 2026-03-28 100105" src="https://github.com/user-attachments/assets/b5b70cf8-2e75-42cf-acf0-d9bac289e50d" />

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

