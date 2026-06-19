package com.example.colocmeal.domain.model

enum class Aisle(val displayName: String, val emoji: String) {
    FRUITS_VEGETABLES("Fruits & Vegetables", "🥬"),
    MEAT("Meat & Deli", "🥩"),
    FISH("Fish & Seafood", "🐟"),
    DAIRY("Dairy", "🥛"),
    GROCERY("Pantry", "🥫"),
    BAKERY("Bakery", "🥖"),
    DRINKS("Drinks", "🥤"),
    HYGIENE("Hygiene & Cleaning", "🧼"),
    FROZEN("Frozen", "🧊"),
    OTHER("Other", "📦")
}