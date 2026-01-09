import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'app_colors.dart';
import 'app_typography.dart';
import 'app_spacing.dart';

/// App theme configuration
class AppTheme {
  AppTheme._();

  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.light,

      // Color Scheme
      colorScheme: ColorScheme.light(
        primary: AppColors.primaryGold,
        onPrimary: AppColors.neutralWhite,
        secondary: AppColors.primaryBlueGray,
        onSecondary: AppColors.neutralWhite,
        error: AppColors.semanticError,
        onError: AppColors.neutralWhite,
        surface: AppColors.neutralWhite,
        onSurface: AppColors.primaryDarkBlue,
        background: AppColors.primaryBeige,
        onBackground: AppColors.primaryDarkBlue,
      ),

      // Scaffold
      scaffoldBackgroundColor: AppColors.primaryBeige,

      // AppBar
      appBarTheme: AppBarTheme(
        elevation: 1,
        backgroundColor: AppColors.neutralWhite,
        foregroundColor: AppColors.primaryDarkBlue,
        systemOverlayStyle: SystemUiOverlayStyle.dark,
        centerTitle: false,
        titleTextStyle: AppTypography.h3(color: AppColors.primaryDarkBlue),
      ),

      // Bottom Navigation Bar
      bottomNavigationBarTheme: BottomNavigationBarThemeData(
        backgroundColor: AppColors.neutralWhite,
        selectedItemColor: AppColors.primaryGold,
        unselectedItemColor: AppColors.neutralMediumGray,
        selectedLabelStyle: AppTypography.caption(color: AppColors.primaryGold),
        unselectedLabelStyle: AppTypography.caption(color: AppColors.neutralMediumGray),
        type: BottomNavigationBarType.fixed,
        elevation: 8,
      ),

      // Card
      cardTheme: CardTheme(
        color: AppColors.neutralWhite,
        elevation: AppShadows.sm,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(AppRadius.md),
        ),
        margin: const EdgeInsets.all(0),
      ),

      // Input Decoration
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: AppColors.neutralWhite,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(AppRadius.sm),
          borderSide: const BorderSide(color: AppColors.neutralLightGray),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(AppRadius.sm),
          borderSide: const BorderSide(color: AppColors.neutralLightGray),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(AppRadius.sm),
          borderSide: const BorderSide(
            color: AppColors.primaryGold,
            width: 2,
          ),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(AppRadius.sm),
          borderSide: const BorderSide(color: AppColors.semanticError),
        ),
        contentPadding: const EdgeInsets.symmetric(
          horizontal: AppSpacing.md,
          vertical: AppSpacing.md,
        ),
        hintStyle: AppTypography.body(color: AppColors.neutralMediumGray),
      ),

      // Button
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: AppColors.primaryGold,
          foregroundColor: AppColors.neutralWhite,
          elevation: 0,
          padding: const EdgeInsets.symmetric(
            horizontal: AppSpacing.lg,
            vertical: AppSpacing.md,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(AppRadius.sm),
          ),
          textStyle: AppTypography.button(color: AppColors.neutralWhite),
          minimumSize: const Size(0, AppSpacing.minTouchTarget),
        ),
      ),

      outlinedButtonTheme: OutlinedButtonThemeData(
        style: OutlinedButton.styleFrom(
          foregroundColor: AppColors.primaryDarkBlue,
          side: const BorderSide(
            color: AppColors.primaryDarkBlue,
            width: 2,
          ),
          padding: const EdgeInsets.symmetric(
            horizontal: AppSpacing.lg,
            vertical: AppSpacing.md,
          ),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(AppRadius.sm),
          ),
          textStyle: AppTypography.button(color: AppColors.primaryDarkBlue),
          minimumSize: const Size(0, AppSpacing.minTouchTarget),
        ),
      ),

      textButtonTheme: TextButtonThemeData(
        style: TextButton.styleFrom(
          foregroundColor: AppColors.primaryBlueGray,
          padding: const EdgeInsets.symmetric(
            horizontal: AppSpacing.md,
            vertical: AppSpacing.sm,
          ),
          textStyle: AppTypography.button(color: AppColors.primaryBlueGray),
          minimumSize: const Size(0, 40),
        ),
      ),

      // Icon
      iconTheme: const IconThemeData(
        color: AppColors.primaryDarkBlue,
        size: 24,
      ),

      // Divider
      dividerTheme: const DividerThemeData(
        color: AppColors.neutralLightGray,
        thickness: 1,
        space: 1,
      ),

      // Chip
      chipTheme: ChipThemeData(
        backgroundColor: AppColors.neutralLightGray,
        selectedColor: AppColors.primaryGold,
        labelStyle: AppTypography.bodySmall(color: AppColors.primaryDarkBlue),
        padding: const EdgeInsets.symmetric(
          horizontal: AppSpacing.sm,
          vertical: AppSpacing.xs,
        ),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(AppRadius.xl),
        ),
      ),

      // Dialog
      dialogTheme: DialogTheme(
        backgroundColor: AppColors.neutralWhite,
        elevation: AppShadows.xl,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(AppRadius.lg),
        ),
        titleTextStyle: AppTypography.h3(color: AppColors.primaryDarkBlue),
        contentTextStyle: AppTypography.body(color: AppColors.primaryDarkBlue),
      ),

      // BottomSheet
      bottomSheetTheme: BottomSheetThemeData(
        backgroundColor: AppColors.neutralWhite,
        elevation: AppShadows.xl,
        shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(
            top: Radius.circular(AppRadius.xl),
          ),
        ),
      ),

      // SnackBar
      snackBarTheme: SnackBarThemeData(
        backgroundColor: AppColors.primaryDarkBlue,
        contentTextStyle: AppTypography.body(color: AppColors.neutralWhite),
        behavior: SnackBarBehavior.floating,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(AppRadius.md),
        ),
      ),

      // Text Theme (fallback)
      textTheme: TextTheme(
        displayLarge: AppTypography.hero(color: AppColors.primaryDarkBlue),
        displayMedium: AppTypography.h1(color: AppColors.primaryDarkBlue),
        displaySmall: AppTypography.h2(color: AppColors.primaryDarkBlue),
        headlineMedium: AppTypography.h3(color: AppColors.primaryDarkBlue),
        headlineSmall: AppTypography.h4(color: AppColors.primaryDarkBlue),
        bodyLarge: AppTypography.bodyLarge(color: AppColors.primaryDarkBlue),
        bodyMedium: AppTypography.body(color: AppColors.primaryDarkBlue),
        bodySmall: AppTypography.bodySmall(color: AppColors.primaryDarkBlue),
        labelLarge: AppTypography.button(color: AppColors.primaryDarkBlue),
        labelSmall: AppTypography.caption(color: AppColors.primaryDarkBlue),
      ),
    );
  }

  static ThemeData get darkTheme {
    // Dark mode support (future enhancement)
    return lightTheme.copyWith(
      brightness: Brightness.dark,
      scaffoldBackgroundColor: AppColors.neutralBlack,
      colorScheme: ColorScheme.dark(
        primary: AppColors.primaryGold,
        onPrimary: AppColors.neutralBlack,
        secondary: AppColors.primaryBlueGray,
        onSecondary: AppColors.neutralWhite,
        error: AppColors.semanticError,
        onError: AppColors.neutralWhite,
        surface: const Color(0xFF2A2A2A),
        onSurface: AppColors.neutralLightGray,
        background: AppColors.neutralBlack,
        onBackground: AppColors.neutralLightGray,
      ),
    );
  }
}
