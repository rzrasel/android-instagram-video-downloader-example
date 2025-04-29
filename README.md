# Instagram Video Downloader

A modern Android application to download Instagram videos directly to your device. Built with Jetpack Compose, Retrofit, and Hilt for dependency injection.

## Features

- Download Instagram videos from URL
- Extract streams from Instagram videos
- Download videos in MP4 format
- Material Design 3 UI with Jetpack Compose
- Permission handling for storage access

## Technologies Used

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI toolkit
- **Hilt** - Dependency injection framework
- **Retrofit** - Type-safe HTTP client
- **Coroutines** - Asynchronous programming
- **Android DownloadManager** - Background downloads
- **JSoup** - HTML parsing for video info extraction

## 🔐 Permissions

The app requires storage permissions to save downloaded videos:

- For Android 10 and below: WRITE_EXTERNAL_STORAGE
- For Android 11 and above: MANAGE_EXTERNAL_STORAGE

The app handles permission requests automatically when needed.

## 🐛 Known Issues

- Some Instagram URLs might not work due to Instagram's anti-scraping measures
- Private account videos cannot be downloaded
- Reels with special formats might not download properly

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/rzrasel/android-instagram-video-downloader-example.git

## 📱 Instagram Video Downloader APK

[📱 Instagram Profile Downloader](apk/app-instagram-profile-release.apk)
[📱 Instagram Video Downloader](apk/app-instagram-video-release.apk)