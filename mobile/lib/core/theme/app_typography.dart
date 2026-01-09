import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

/// App typography system matching the design system from Step 1
class AppTypography {
  AppTypography._();

  // Font Families
  static const String primaryFont = 'Cairo'; // Arabic + Latin
  static const String secondaryFont = 'Montserrat'; // Headings
  static const String bodyFont = 'Lato'; // Body text

  // Text Styles

  // Hero - 48px / 3rem (Bold)
  static TextStyle hero({Color? color}) => TextStyle(
        fontFamily: primaryFont,
        fontSize: 48,
        fontWeight: FontWeight.w700,
        height: 1.2,
        color: color,
      );

  // H1 - 36px / 2.25rem (Bold)
  static TextStyle h1({Color? color}) => TextStyle(
        fontFamily: primaryFont,
        fontSize: 36,
        fontWeight: FontWeight.w700,
        height: 1.2,
        color: color,
      );

  // H2 - 28px / 1.75rem (SemiBold)
  static TextStyle h2({Color? color}) => TextStyle(
        fontFamily: primaryFont,
        fontSize: 28,
        fontWeight: FontWeight.w600,
        height: 1.2,
        color: color,
      );

  // H3 - 24px / 1.5rem (SemiBold)
  static TextStyle h3({Color? color}) => TextStyle(
        fontFamily: primaryFont,
        fontSize: 24,
        fontWeight: FontWeight.w600,
        height: 1.2,
        color: color,
      );

  // H4 - 20px / 1.25rem (Medium)
  static TextStyle h4({Color? color}) => TextStyle(
        fontFamily: primaryFont,
        fontSize: 20,
        fontWeight: FontWeight.w500,
        height: 1.2,
        color: color,
      );

  // Body Large - 18px / 1.125rem (Regular)
  static TextStyle bodyLarge({Color? color}) => TextStyle(
        fontFamily: bodyFont,
        fontSize: 18,
        fontWeight: FontWeight.w400,
        height: 1.6,
        color: color,
      );

  // Body - 16px / 1rem (Regular)
  static TextStyle body({Color? color}) => TextStyle(
        fontFamily: bodyFont,
        fontSize: 16,
        fontWeight: FontWeight.w400,
        height: 1.6,
        color: color,
      );

  // Body Small - 14px / 0.875rem (Regular)
  static TextStyle bodySmall({Color? color}) => TextStyle(
        fontFamily: bodyFont,
        fontSize: 14,
        fontWeight: FontWeight.w400,
        height: 1.6,
        color: color,
      );

  // Caption - 12px / 0.75rem (Regular)
  static TextStyle caption({Color? color}) => TextStyle(
        fontFamily: bodyFont,
        fontSize: 12,
        fontWeight: FontWeight.w400,
        height: 1.4,
        color: color,
      );

  // Button Text - Montserrat SemiBold 16px
  static TextStyle button({Color? color}) => TextStyle(
        fontFamily: secondaryFont,
        fontSize: 16,
        fontWeight: FontWeight.w600,
        height: 1.2,
        color: color,
        letterSpacing: 0.5,
      );

  // Button Text Small - Montserrat SemiBold 14px
  static TextStyle buttonSmall({Color? color}) => TextStyle(
        fontFamily: secondaryFont,
        fontSize: 14,
        fontWeight: FontWeight.w600,
        height: 1.2,
        color: color,
        letterSpacing: 0.5,
      );
}
