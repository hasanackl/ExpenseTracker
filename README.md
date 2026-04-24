# 💰 Expense Tracker

A simple and intuitive Android application built with Kotlin that helps users track their daily expenses. Users can register and log in securely, and all data is stored in the cloud using Firebase Firestore.

---

## ✨ Features

- 🔐 User registration and login with Firebase Authentication
- ➕ Add expenses with title, amount, category, date, and note
- 📋 View all expenses in a clean list sorted by most recent
- 💵 See total amount spent with transaction count
- 🥧 Pie chart showing spending distribution by category
- 🏷️ Filter expenses by category (Food, Transport, Entertainment, Health, Other)
- 🎨 Color-coded category icons
- 🔍 View detailed information for each expense
- 🗑️ Delete expenses with confirmation dialog
- 📅 Date picker for selecting expense date
- ☁️ Cloud storage — data syncs in real time via Firebase Firestore

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Primary programming language |
| Android Studio | IDE |
| Firebase Firestore | Cloud database |
| Firebase Authentication | User registration and login |
| ViewModel + Flow | MVVM architecture |
| RecyclerView | Expense list |
| Material Design 3 | UI components |
| MPAndroidChart | Pie chart visualization |
| Coroutines | Async database operations |

---

## 🏗️ Architecture

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern:
UI Layer         → LoginActivity, RegisterActivity, MainActivity,
AddExpenseActivity, ExpenseDetailActivity
ViewModel Layer  → ExpenseViewModel
Repository Layer → ExpenseRepository
Data Layer       → Expense (data class), Firebase Firestore
Auth Layer       → Firebase Authentication

---

## 📂 Project Structure
com.example.expensetracker/
├── data/
│   ├── Expense.kt
│   └── ExpenseRepository.kt
├── adapter/
│   └── ExpenseAdapter.kt
├── ExpenseViewModel.kt
├── LoginActivity.kt
├── RegisterActivity.kt
├── MainActivity.kt
├── AddExpenseActivity.kt
└── ExpenseDetailActivity.kt

---

## 🚀 Getting Started

1. Clone the repository:
```bash
git clone https://github.com/hasanackl/ExpenseTracker.git
```

2. Open the project in **Android Studio**
3. Add your own `google-services.json` file to the `app/` directory (from Firebase Console)
4. Let Gradle sync complete
5. Run on an emulator or physical device (minimum API 24)

---

## 📋 Requirements

- Android Studio Ladybug or newer
- Minimum SDK: API 24 (Android 7.0)
- Target SDK: API 35 (Android 15)
- Gradle 9.3.1
- Firebase project with Firestore and Authentication enabled

