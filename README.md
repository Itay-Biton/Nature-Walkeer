
# Nature Walkeer

**Nature Walkeer** is an Android application designed to encourage users to stay active by walking. The app tracks daily steps, converts them into coins, and allows users to use these coins to purchase in-app upgrades and plants. Users can activate boosts, care for plants, and participate in challenges. The app also includes features for user authentication and profile management.

## Features

- **Step Tracking**: Tracks steps taken during the day. Users earn coins based on the number of steps.
- **Boosts and Upgrades**: Activate boosts that enhance step counting for a period. Purchase upgrades using earned coins.
- **Plant Management**: Buy, care for, and harvest plants to earn additional rewards.
- **Challenges**: Set and achieve daily step goals.
- **User Profile**: Manage user profiles, including email, username, and profile picture.
- **Firebase Integration**: Utilizes Firebase Authentication for user management and Firestore for storing user data.

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Itay-Biton/Nature-Walkeer.git
   ```
2. **Open in Android Studio**:
   - Open Android Studio and select "Open an existing project."
   - Navigate to the cloned repository and open it.

3. **Set up Firebase**:
   - Add your Firebase project configuration files (e.g., `google-services.json`) to the `app` directory.
   - Ensure Firebase Authentication and Firestore are set up in your Firebase Console.

4. **Run the App**:
   - Build and run the project on an Android device or emulator.

## Usage

- **Login/Register**: Users can create an account or log in with an existing account using email and password.
- **Track Steps**: The app tracks steps while it is in the foreground or background.
- **Manage Plants**: Use coins earned from walking to buy plants, care for them, and harvest rewards.
- **Profile Management**: Users can update their profile picture, email, and password.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, feel free to create a pull request or open an issue.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
