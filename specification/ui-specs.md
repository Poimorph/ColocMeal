---
title: "Composable functions/objects headers"
author:
  - Suhayb Saleh
date: "28/04/2026"
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
highlight-style: tango
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

# Content

This file contains the headers to be used for the Composable functions in Kotlin in order to create the essential components for the app

> Uses Material Design 3 v. 1.4.0

# Buttons

## ButtonStyle enum

```kotlin
    enum class ButtonStyle { FILLED, TONAL, OUTLINED, TEXT, ELEVATED }
```

## Button composable

```Kotlin
    @Composable
    fun AppButton(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        style: ButtonStyle = ButtonStyle.FILLED,
        leadingIcon: ImageVector? = null,
        enabled: Boolean = true
    )
```

# IconButton

```Kotlin
    @Composable
    fun AppIconButton(
        icon: ImageVector,
        contentDescription: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    )
```

# FloatingActionButton

```Kotlin
    @Composable
    fun AppFab(
        icon: ImageVector,
        contentDescription: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    )
```

# OutlinedTextField

```Kotlin
    @Composable
    fun AppTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        modifier: Modifier = Modifier,
        supportingText: String? = null,
        isError: Boolean = false,
        trailingIcon: ImageVector? = null,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardType: KeyboardType = KeyboardType.Text,
        maxLines: Int = Int.MAX_VALUE
    )
```

# OTP / Code Input

```Kotlin
    @Composable
    fun OtpCodeInput(
        value: String,
        onValueChange: (String) -> Unit,
        length: Int = 6,
        modifier: Modifier = Modifier
    )

    // Sous-composant interne
    @Composable
    private fun CodeBox(
        char: Char?,
        state: CodeBoxState // EMPTY, CURSOR, FILLED
    )
```

# SearchBar

```Kotlin
    @Composable
    fun AppSearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        placeholder: String,
        modifier: Modifier = Modifier,
        leadingIcon: ImageVector = Icons.Default.Search,
        trailingIcon: ImageVector? = null
    )
```

# Switch et Checkbox

```Kotlin
    @Composable
    fun AppSwitch(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    )

    @Composable
    fun AppCheckbox(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    )
```

# Chips

```Kotlin
    enum class ChipKind { ASSIST, FILTER, INPUT, CATEGORY}

    @Composable
    fun AppChip(
        label: String,
        kind: ChipKind = ChipKind.FILTER,
        selected: Boolean = false,
        onClick: () -> Unit = {},
        leadingIcon: ImageVector? = null,
        onDismiss: (() -> Unit)? = null // only INPUT
    )
```

# SegmentedButton

```Kotlin
    @Composable
    fun <T> AppSegmentedButton(
        options: List<T>,
        selected: T,
        onSelect: (T) -> Unit,
        labelOf: (T) -> String,
        modifier: Modifier = Modifier
    )
```

# TabBar

```Kotlin
    data class TabItem(
        val label: String, 
        val badge: Int? = null
    )

    @Composable
    fun AppTabBar(
        tabs: List<TabItem>,
        selectedIndex: Int,
        onSelect: (Int) -> Unit,
        modifier: Modifier = Modifier
    )
```

# WeekStrip

```Kotlin
    data class DayState(
        val date: LocalDate,
        val isToday: Boolean,
        val hasPlan: Boolean
    )

    @Composable
    fun WeekStrip(
        days: List<DayState>,
        selectedDate: LocalDate,
        onSelect: (LocalDate) -> Unit,
        modifier: Modifier = Modifier
    )
```

# Cards

## AppOutlinedCard

```Kotlin
    @Composable
    fun AppOutlinedCard(
        selected: Boolean = false,
        onClick: () -> Unit = {},
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    )
```

## RecipeCard

```Kotlin
    @Composable
    fun RecipeCard(
        recipe: Recipe,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    )
```

## PlanCard

```Kotlin
    sealed class PlanCardState {
        data class Filled(val recipe: Recipe, val cook: Member) : PlanCardState()
        object Empty : PlanCardState()
    }

    @Composable
    fun PlanCard(
        date: LocalDate,
        isToday: Boolean,
        state: PlanCardState,
        onClick: () -> Unit
    )
```

# BottomSheet

```Kotlin
    @Composable
    fun AppBottomSheet(
        visible: Boolean,
        onDismiss: () -> Unit,
        title: String? = null,
        subtitle: String? = null,
        content: @Composable ColumnScope.() -> Unit
    )
```

# TopAppBar

```Kotlin
    enum class TopBarSize { SMALL, MEDIUM }

    @Composable
    fun AppTopAppBar(
        title: String,
        size: TopBarSize = TopBarSize.SMALL,
        subtitle: String? = null,
        onNavigationClick: (() -> Unit)? = null,
        actions: @Composable RowScope.() -> Unit = {}
    )
```

# NavigationBar

```Kotlin
    data class NavDestination(
        val route: String,
        val label: String,
        val icon: ImageVector,
        val iconSelected: ImageVector = icon
    )

    @Composable
    fun AppNavigationBar(
        destinations: List<NavDestination>,
        currentRoute: String?,
        onSelect: (NavDestination) -> Unit
    )
```

# Tag

```Kotlin
    enum class TagRole { MINE, SHARED, INGREDIENT_COUNT, AUTO, MANUAL, COOK }

    @Composable
    fun AppTag(
        label: String,
        role: TagRole,
        leadingIcon: ImageVector? = null,
        modifier: Modifier = Modifier
    )
```

# Avatar

```Kotlin
    enum class AvatarSize(val dp: Dp) { 
        XS(24.dp), 
        MD(40.dp), 
        LG(56.dp) 
        }

    @Composable
    fun AppAvatar(
        name: String,
        userId: String,
        size: AvatarSize = AvatarSize.MD,
        modifier: Modifier = Modifier
    )
```

# Avatar

```Kotlin
    enum class AvatarSize(val dp: Dp) { 
        XS(24.dp), 
        MD(40.dp), 
        LG(56.dp) 
        }

    @Composable
    fun AppAvatar(
        name: String,
        userId: String,
        size: AvatarSize = AvatarSize.MD,
        modifier: Modifier = Modifier
    )
```

# LinearProgressIndicator

```Kotlin
    @Composable
    fun AppLinearProgress(
        progress: Float? = null, // null = indeterminate
        modifier: Modifier = Modifier
    )
```

# StepDots

```Kotlin
    @Composable
    fun StepDots(
        total: Int,
        current: Int,
        modifier: Modifier = Modifier
    )
```

# Divider

```Kotlin
    @Composable
    fun AppDivider(
        modifier: Modifier = Modifier,
        indent: Dp = 0.dp
    )
```

# Snackbar

```Kotlin
    @Composable
    fun AppDivider(
        modifier: Modifier = Modifier,
        indent: Dp = 0.dp
    )
```

# OrDivider

```Kotlin
    @Composable
    fun OrDivider(
        label: String = "OR",
        modifier: Modifier = Modifier
    )
```

# CookOption

```Kotlin
    @Composable
    fun CookOption(
        member: Member,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    )
```

# AisleHeader

```Kotlin
    @Composable
    fun AisleHeader(
        aisle: Aisle,
        checkedCount: Int,
        totalCount: Int,
        modifier: Modifier = Modifier
    )
```

# GroceryItem

```Kotlin
    @Composable
    fun GroceryItem(
        item: GroceryItem,
        onCheckChange: (Boolean) -> Unit,
        modifier: Modifier = Modifier
    )
```

# CameraViewFinder

```Kotlin
    @Composable
    fun CameraViewFinder(
        isScanning: Boolean,
        size: DpSize = DpSize(260.dp, 144.dp),
        modifier: Modifier = Modifier
    )
```