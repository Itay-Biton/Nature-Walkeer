
# Nature Walkeer

**Nature Walkeer** is an Android application designed to encourage users to stay active by walking. The app tracks daily steps, converts them into coins, and allows users to use these coins to purchase in-app upgrades and plants. Users can activate boosts, care for plants, and participate in challenges. The app also includes features for user authentication and profile management.

## Features

### 1. Step Tracking
- **Real-Time Step Tracking**: The app tracks the user's steps throughout the day using Android's built-in step sensor. It updates the step count regularly when the app is in the foreground or background, ensuring that users receive coins for each step taken.
- **Daily and Weekly Step Goals**: Users can set daily step goals, and the app keeps track of weekly step counts, helping users to stay motivated and active.

### 2. In-App Currency
- **Coins for Steps**: Every step taken by the user earns them coins. These coins can be used to purchase virtual plants and upgrades within the app.
- **Boosts**: Users can activate boosts that multiply the number of coins earned per step for a limited time.

### 3. Virtual Plant Care
- **Plant Store**: Users can buy plants from a store using the coins they’ve earned.
- **Plant Care**: Users can plant their plants and grow them until they are ready to be harvested for additional rewards.

### 4. Firebase Integration
- **Firestore Database**: The app uses Firestore to store user data, including step counts, coins, and plant information. This allows for real-time data syncing across devices.
- **Firebase Authentication**: The app includes Firebase Authentication to manage user sign-up, login, and profile management securely. Users can sign in using email and password.
- **Firebase Storage**: The app utilizes Firebase Storage for handling most of the content, such as pictures or icons. These images are stored securely, and their URLs are saved in Firestore.

### 5. User Management
- **Profile Management**: Users can view and edit their profile information, including changing their password and profile picture.
- **Leaderboard**: The app includes a leaderboard feature that ranks users based on their current amount of coins.


## Screenshots

<p align="center">Dashboard</p>
<p align="center">
   <img src="./Screenshots/Dashboard1.png" alt="Dashboard 1" width="200"/> 
   <img src="./Screenshots/Dashboard2.png" alt="Dashboard 2" width="200"/> 
</p>

<p align="center">Sport Data</p>
<p align="center">
   <img src="./Screenshots/Sport1.png" alt="Sport 1" width="200"/> 
   <img src="./Screenshots/Sport2.png" alt="Sport 2" width="200"/> 
</p>

<p align="center">Store</p>
<p align="center">
   <img src="./Screenshots/Store1.png" alt="Store 1" width="200"/> 
   <img src="./Screenshots/Store2.png" alt="Store 2" width="200"/> 
   <img src="./Screenshots/Store3.png" alt="Store 3" width="200"/> 
</p>

<p align="center">Plant Care</p>
<p align="center">
   <img src="./Screenshots/Plant1.png" alt="Plant 1" width="200"/> 
   <img src="./Screenshots/Plant2.png" alt="Plant 2" width="200"/> 
</p>

<p align="center">Challenge Yourself</p>
<p align="center">
   <img src="./Screenshots/Challenge.png" alt="Challenge" width="200"/> 
</p>

<p align="center">Compete Againts Others</p>
<p align="center">
   <img src="./Screenshots/Leaderboard.png" alt="Leaderboard" width="200"/> 
</p>

## Demo Video

<p align="center">
  <a href="https://youtu.be/bsFHrgH8_00">
    <img src="https://i9.ytimg.com/vi/bsFHrgH8_00/mq2.jpg?sqp=CKS6k7YG-oaymwEmCMACELQB8quKqQMa8AEB-AHwBIACyAmKAgwIABABGB8gZSgcMA8=&rs=AOn4CLCMTIH0E8GeMFdur3kWIVk9jeKakg" alt="Nature Walker Demo" style="width:60%; height:auto;">
  </a>
</p>


## Installation

**To install this repository:**
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

  
## Download and Install Nature Walker

1. **Download the App:**
   - Click [here](https://github.com/Itay-Biton/Nature-Walkeer/releases/download/v1.0/Nature-Walker-Installation.apk) to download the latest version of the `Nature-Walker-Installation.apk`.

2. **Bypass Google Virus Scan:**
   - If prompted by Google, you can safely bypass the virus scan warning. The APK is secure and has been tested thoroughly.

3. **Install the App:**
   - Once downloaded, open the APK file and follow the on-screen instructions to install Nature Walker on your Android device.

## Getting Started

After installing the app, you can sign up with your email or use Google Authentication to log in. Start walking, earn coins, and enjoy the interactive features Nature Walker offers.



