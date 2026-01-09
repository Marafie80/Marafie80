import 'package:flutter/material.dart';

/// App color palette matching the design system from Step 1
class AppColors {
  AppColors._();

  // Primary Colors
  static const Color primaryBeige = Color(0xFFCAC6B1);
  static const Color primaryDarkBlue = Color(0xFF1C3F68);
  static const Color primaryBlueGray = Color(0xFF647CA4);
  static const Color primaryGold = Color(0xFFAF924A);
  static const Color primaryYellow = Color(0xFFEEDEAA);

  // Semantic Colors
  static const Color semanticSuccess = Color(0xFF6B8E6B);
  static const Color semanticError = Color(0xFFC85C5C);
  static const Color semanticInfo = Color(0xFF647CA4);
  static const Color semanticWarning = Color(0xFFD4A574);

  // Neutral Scale
  static const Color neutralWhite = Color(0xFFFFFFFF);
  static const Color neutralLightGray = Color(0xFFE8E5DC);
  static const Color neutralMediumGray = Color(0xFF9B9B9B);
  static const Color neutralDarkGray = Color(0xFF4A4A4A);
  static const Color neutralBlack = Color(0xFF1A1A1A);

  // Star Rating
  static const Color starFilled = Color(0xFFFFB800);

  // Gradients
  static const LinearGradient goldGradient = LinearGradient(
    colors: [Color(0xFFAF924A), Color(0xFF9A7D3F)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );

  static const LinearGradient blueGradient = LinearGradient(
    colors: [Color(0xFF647CA4), Color(0xFF1C3F68)],
    begin: Alignment.topLeft,
    end: Alignment.bottomRight,
  );
}
