---
title: "ColocMeal"
subtitle: "Functional & Technical Specification"
date: "27/04/2026"
lang: fr-FR
geometry: margin=1.5cm
papersize: a4
fontsize: 11pt
documentclass: article
toc: true
toc-title: Table des matières
toc-depth: 3
numbersections: true
colorlinks: true
linkcolor: blue
highlight-style: pygments
header-includes: |
  \usepackage{tcolorbox}
  \tcbuselibrary{breakable}
  \usepackage{etoolbox}
  \usepackage{fvextra}
  \definecolor{codebg}{RGB}{246,248,250}
  \definecolor{codeborder}{RGB}{230,230,230}
  \DefineVerbatimEnvironment{Highlighting}{Verbatim}{breaklines,commandchars=\\\{\}}
  \BeforeBeginEnvironment{Highlighting}{
    \begin{tcolorbox}[
      breakable,
      colback=codebg,
      colframe=codeborder,
      arc=3pt,
      boxrule=0.5pt,
      left=5pt,
      right=5pt,
      top=5pt,
      bottom=5pt
    ]
  }
  \AfterEndEnvironment{Highlighting}{\end{tcolorbox}}
  \usepackage{xcolor}
  \definecolor{quotebar}{RGB}{99,102,241}
  \definecolor{quotebg}{RGB}{238,239,255}
  \renewenvironment{quote}{%
    \begin{tcolorbox}[
      breakable,
      colback=quotebg,
      colframe=quotebar,
      leftrule=4pt,
      rightrule=0pt,
      toprule=0pt,
      bottomrule=0pt,
      arc=0pt,
      left=10pt,
      right=8pt,
      top=6pt,
      bottom=6pt
    ]
    \itshape
  }{%
    \end{tcolorbox}
  }
---


\newpage

---

# Product description

---

ColocMeal is an Android app for flatmates/housemates who want to organize their shared dinners in a simple and fair way. The core idea is the virtual house: a shared space where members plan the incoming meals, designate a cook for each evening, and manage a common shopping list that updates automatically based on the planning.

## Problem solved

| Daily problem | ColocMeal solution |
|---|---|
| "Who's cooking tonight?" | Weekly planning with a designated cook |
| "What are we eating?" | Shared recipe library, fed by all members |
| "What do we need to buy?" | Shopping list automatically generated from the planning, organized by aisle |
| "Who already bought what?" | Real-time synced checkboxes across all devices |

## What the app isn't

- Not a nutrition tracking app (calories, macros)
- Not a shared expense management app (Tricount-style)
- Not a cooking app with external recipes 

---

# Glossary


| Term | Definition |
|:---|:---|
| **House** | Group of flatmates/housemates sharing planning, recipes, and shopping list |
| **Invite code** | 6-character alphanumeric code generated when a house is created |
| **Member** | Authenticated user belonging to a house |
| **Creator** | Member who created the house. they retain the right to delete it |
| **Recipe** | Name + description + list of ingredients (no quantities) |
| **Planned meal** | Recipe assigned to a specific evening with a designated cook |
| **(Grocery) item** | Element of the shopping list, attached to an aisle |
| **Aisle** | Product category used to group the list (e.g. Fruits & Vegetables) |
| **Cook** | Member designated as responsible for preparing a planned meal |

\newpage

# Scope & prioritization


## Must Have

- [ ] Authentication: sign-up / sign-in (Firebase Auth)
- [ ] Virtual house: create / join via invite code
- [ ] Recipe library (create, share with the house)
- [ ] Weekly planning: recipe + designated cook per evening
- [ ] Automatic shopping list generation from the planning
- [ ] List organized by aisles
- [ ] Automatic deduplication of merged ingredients
- [ ] Manual item addition (text search)
- [ ] Mark item as bought (real-time sync)
- [ ] Shake the phone (on the Shopping tab) to mark all items bought & clear them
- [ ] Compose animations (transitions, check anim, progress)
- [ ] A weekly report on what and who bought groceries

## Nice to Have

- [ ] History of past menus
- [ ] Push notifications (new planned meal, new item added)
- [ ] Distribution stats (who cooked how many times)
- [ ] Dark mode
- [ ] Animated onboarding on first launch
- [ ] Recipe photos
- [ ] Nutrition tracking / calories / macros

# Main use cases

## UC-01 — Create a house

| Field | Value |
|---|---|
| **Actor** | Logged-in user without a house |
| **Precondition** | Active Firebase account |
| **Trigger** | Tap "Create a house" on HouseSetupScreen |
| **Nominal flow** | 1. Enter a name -> 2. Tap "Create" -> 3. System generates an invite code -> 4. Redirect to HomeScreen with code displayed |
| **Postcondition** | User is creator and sole member of the house |

## UC-02 — Join a house

| Field | Value |
|---|---|
| **Actor** | Logged-in user without a house |
| **Precondition** | Has a valid invite code |
| **Trigger** | Tap "Join a house" |
| **Nominal flow** | 1. Enter the code -> 2. Tap "Join" -> 3. System validates the code -> 4. Added to members -> 5. Redirect HomeScreen |
| **Error case** | Code does not exist: "Invalid code" message; already in a house: blocking message |

## UC-03 — Plan a meal

| Field | Value |
|---|---|
| **Actor** | Any member |
| **Precondition** | At least one recipe exists in the library |
| **Nominal flow** | 1. Tap an empty slot in the planning -> 2. BottomSheet opens -> 3. Select a recipe -> 4. Designate a cook -> 5. Confirm -> 6. Meal appears, ingredients are added to the shopping list |
| **Postcondition** | MealPlan created in Firestore + GroceryItems created/updated |

## UC-04 — Do the shopping

| Field | Value |
|---|---|
| **Actor** | Any member |
| **Nominal flow** | 1. Open Shopping tab -> 2. See items grouped by aisle -> 3. Check items as you go -> 4. (Optional) Tap "Clear bought items" at the end |
| **Sync** | Each check is propagated in < 1 s to other members |

## UC-05 — Shake to clear the shopping list

| Field | Value |
|---|---|
| **Actor** | Any member |
| **Precondition** | On the Shopping tab with at least one item |
| **Trigger** | Physically shake the phone |
| **Nominal flow** | 1. Shake detected (accelerometer) -> 2. All items animate to "bought" -> 3. All items are removed from the list -> 4. Undo snackbar shown briefly |
| **Postcondition** | The house's shopping list is emptied; change syncs to other members |
| **Error case** | Empty list: shake ignored (no-op) |

# Data model

## Main entities

```kotlin
data class User(
    val uid: String,
    val displayName: String,
    val email: String,
    val houseId: String? = null
)

data class House(
    val id: String,
    val name: String,
    val inviteCode: String,    // 6 alphanumeric chars
    val creatorId: String,     // creator id
    val memberIds: List<String>
)

data class Recipe(
    val id: String,
    val name: String,
    val description: String? = null,
    val ingredients: List<String>,   // names only, no quantities
    val authorId: String,
    val authorName: String,          // denormalized
    val houseId: String? = null,     // null if private
    val isShared: Boolean = false    // set at creation, never changes
)

data class MealPlan(
    val id: String,
    val houseId: String,
    val weekStart: LocalDate,        // always Monday
    val dayOfWeek: Int,              // 1 = Mon ... 7 = Sun
    val recipeId: String,
    val recipeName: String,          // denormalized
    val cookId: String,
    val cookName: String             // denormalized
)

data class GroceryItem(
    val id: String,
    val houseId: String,
    val name: String,
    val nameNormalized: String,      // toLowerCase().trim().removeAccents()
    val aisle: Aisle,
    val isChecked: Boolean = false,
    val source: Source,
    val addedBy: String
)

enum class Source { AUTO, MANUAL }

enum class Aisle(val displayName: String, val emoji: String) {
    FRUITS_VEGETABLES("Fruits & Vegetables", [emoji]),  // emojis are in Unicode
    MEAT("Meat & Deli", [emoji]),
    FISH("Fish & Seafood", [emoji]),
    DAIRY("Dairy", [emoji]),
    GROCERY("Pantry", [emoji]),
    BAKERY("Bakery", [emoji]),
    DRINKS("Drinks", [emoji]),
    HYGIENE("Hygiene & Cleaning", [emoji]),
    FROZEN("Frozen", [emoji]),
    OTHER("Other", [emoji])
}
```
## Firebase structure

Flat top-level collections, mirroring the data model above. Each document carries the `houseId` it belongs to (when relevant), so house-scoped data is fetched with a `where("houseId", "==", ...)` query rather than nested subcollections.

```
/users/{userId}
    -> User (id = Firebase Auth uid)

/houses/{houseId}
    -> House (id, name, inviteCode, creatorId, memberIds, createdAt)

/recipes/{recipeId}
    -> Recipe (houseId = null for private recipes, authorId for ownership)

/mealPlans/{mealPlanId}
    -> MealPlan (houseId, weekStart, dayOfWeek, recipeId, cookId)

/groceryItems/{groceryItemId}
    -> GroceryItem (houseId, nameNormalized, isChecked, sourceRecipeIds)
```

### Notes
- `houseId` is the main partition key: queries for recipes, meal plans and grocery items always filter on it (`recipes` additionally filter on `authorId` for private recipes).
- `inviteCode` lookup (UC-02) requires a single-field index on `houses.inviteCode`.
- Real-time sync (planning, shopping list) uses Firestore snapshot listeners (`addSnapshotListener`) on the `mealPlans` and `groceryItems` collections, scoped by `houseId`.
- The Firestore document ID *is* the entity's `id` (e.g. `users/{uid}` doc ID = Firebase Auth uid), so `id` is not stored as a field inside the document — avoids keeping it in sync.
- House members (avatars, cook picker): no extra collection or denormalized member list needed. Fetch `users` docs directly with `whereIn(FieldPath.documentId(), house.memberIds)` (Firestore supports up to 30 IDs per query — plenty for a flatshare).
- Denormalized fields (`authorName`, `cookName`, `checkedByName`) avoid extra reads when displaying lists.
- Room stays as a local cache/offline layer: Firestore listeners write through to Room, and the UI reads from Room (single source of truth on-device).
