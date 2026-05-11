# 🌦️ WeatherBoy

WeatherBoy is a modern Android weather application built in Java using Retrofit and the OpenWeather API. The app provides real-time weather information for any city and includes advanced features such as GPS-based current location weather, MVVM architecture, and Room Database for saving favorite cities.

---

## 📱 Features

### 🌤️ Core Features
- Search weather by city name
- Display current temperature
- Weather description
- Humidity
- Wind speed
- Atmospheric pressure
- Dynamic weather icons
- Default city weather on app launch

### 📍 GPS-Based Current Location Weather
- Automatically fetches weather based on the user's current location
- Uses Google Play Services Location API
- Runtime permission handling for location access

### 🏗️ MVVM Architecture
- Clean and scalable architecture
- Separation of concerns using:
  - View (Activity)
  - ViewModel
  - Repository
  - Network Layer

### 💾 Room Database for Saved Cities
- Save favorite cities locally
- Retrieve saved cities even after restarting the app
- Offline persistence

---

## 🛠️ Tech Stack

| Technology | Purpose |
|----------|----------|
| Java | Main programming language |
| Android Studio | Development environment |
| Retrofit | REST API networking |
| Gson Converter | JSON parsing |
| Glide | Weather icon loading |
| OpenWeather API | Weather data provider |
| MVVM | Application architecture |
| LiveData & ViewModel | UI state management |
| Room Database | Local data persistence |
| Google Play Services Location | GPS weather |

---

## 🏗️ Architecture

UI (Activity + XML)
        ↓
ViewModel
        ↓
Repository
        ↓
Retrofit API Service
        ↓
OpenWeather API
        ↓
Room Database
