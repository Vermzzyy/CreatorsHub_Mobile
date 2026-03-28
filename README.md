# CreatorsHub

<img width="351" height="672" alt="Screenshot 2026-03-28 094906" src="https://github.com/user-attachments/assets/c68223aa-3974-4d87-80b2-eba71ccc2f5f" />

<img width="351" height="700" alt="Screenshot 2026-03-28 095718" src="https://github.com/user-attachments/assets/cea39fd5-25bb-42a6-bef8-306bb6621a9f" />

<img width="345" height="699" alt="Screenshot 2026-03-28 095744" src="https://github.com/user-attachments/assets/93ebb585-0758-4079-813c-c45bf0778ed6" />

<img width="351" height="710" alt="Screenshot 2026-03-28 100052" src="https://github.com/user-attachments/assets/5ee2ca0a-7b0e-4735-9075-4030e193cd0c" />

<img width="348" height="712" alt="Screenshot 2026-03-28 100105" src="https://github.com/user-attachments/assets/8296f134-f841-4d3a-b92a-751eab641411" />


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
