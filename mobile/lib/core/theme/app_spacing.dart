/// App spacing system (8px base unit) matching the design system from Step 1
class AppSpacing {
  AppSpacing._();

  // Base unit: 8px
  static const double xs = 4.0; // 0.5 unit
  static const double sm = 8.0; // 1 unit
  static const double md = 16.0; // 2 units
  static const double lg = 24.0; // 3 units
  static const double xl = 32.0; // 4 units
  static const double xxl = 48.0; // 6 units
  static const double xxxl = 64.0; // 8 units

  // Common padding/margin values
  static const double screenPadding = md; // 16px horizontal padding
  static const double cardPadding = md; // 16px card padding
  static const double sectionSpacing = lg; // 24px between sections

  // Touch targets (minimum 44px for iOS, 48px for Android)
  static const double minTouchTarget = 48.0;
}

/// App border radius
class AppRadius {
  AppRadius._();

  static const double sm = 4.0; // Buttons, small cards
  static const double md = 8.0; // Cards, inputs
  static const double lg = 12.0; // Modals, large cards
  static const double xl = 16.0; // Hero sections
  static const double full = 9999.0; // Pills, avatars
}

/// App shadows
class AppShadows {
  AppShadows._();

  // Shadow elevations matching design system
  static const double sm = 1.0;
  static const double md = 4.0;
  static const double lg = 8.0;
  static const double xl = 12.0;
}

/// App animation durations
class AppDurations {
  AppDurations._();

  static const Duration micro = Duration(milliseconds: 100);
  static const Duration fast = Duration(milliseconds: 150);
  static const Duration normal = Duration(milliseconds: 200);
  static const Duration smooth = Duration(milliseconds: 250);
  static const Duration slow = Duration(milliseconds: 300);
}

/// App animation curves
class AppCurves {
  AppCurves._();

  static const Curve standard = Curves.easeInOut;
  static const Curve enter = Curves.easeOut;
  static const Curve exit = Curves.easeIn;
}
