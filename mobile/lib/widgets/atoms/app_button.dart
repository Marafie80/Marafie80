import 'package:flutter/material.dart';
import '../../core/theme/app_colors.dart';
import '../../core/theme/app_typography.dart';
import '../../core/theme/app_spacing.dart';

enum AppButtonVariant {
  primary,
  secondary,
  ghost,
  danger,
  success,
}

enum AppButtonSize {
  small,
  medium,
  large,
}

class AppButton extends StatelessWidget {
  final String text;
  final VoidCallback? onPressed;
  final AppButtonVariant variant;
  final AppButtonSize size;
  final bool isLoading;
  final bool fullWidth;
  final Widget? icon;
  final bool iconRight;

  const AppButton({
    Key? key,
    required this.text,
    this.onPressed,
    this.variant = AppButtonVariant.primary,
    this.size = AppButtonSize.medium,
    this.isLoading = false,
    this.fullWidth = false,
    this.icon,
    this.iconRight = false,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final bool isDisabled = onPressed == null || isLoading;

    return SizedBox(
      width: fullWidth ? double.infinity : null,
      height: _getHeight(),
      child: variant == AppButtonVariant.primary
          ? ElevatedButton(
              onPressed: isDisabled ? null : onPressed,
              style: _getPrimaryStyle(isDisabled),
              child: _buildContent(),
            )
          : variant == AppButtonVariant.secondary
              ? OutlinedButton(
                  onPressed: isDisabled ? null : onPressed,
                  style: _getSecondaryStyle(isDisabled),
                  child: _buildContent(),
                )
              : TextButton(
                  onPressed: isDisabled ? null : onPressed,
                  style: _getGhostStyle(isDisabled),
                  child: _buildContent(),
                ),
    );
  }

  Widget _buildContent() {
    if (isLoading) {
      return Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          SizedBox(
            width: _getLoadingSize(),
            height: _getLoadingSize(),
            child: CircularProgressIndicator(
              strokeWidth: 2,
              valueColor: AlwaysStoppedAnimation<Color>(
                variant == AppButtonVariant.primary
                    ? AppColors.neutralWhite
                    : AppColors.primaryDarkBlue,
              ),
            ),
          ),
          const SizedBox(width: AppSpacing.sm),
          Text('Processing...', style: _getTextStyle()),
        ],
      );
    }

    if (icon != null && !iconRight) {
      return Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          icon!,
          const SizedBox(width: AppSpacing.sm),
          Text(text, style: _getTextStyle()),
        ],
      );
    }

    if (icon != null && iconRight) {
      return Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Text(text, style: _getTextStyle()),
          const SizedBox(width: AppSpacing.sm),
          icon!,
        ],
      );
    }

    return Text(text, style: _getTextStyle());
  }

  double _getHeight() {
    switch (size) {
      case AppButtonSize.small:
        return 40;
      case AppButtonSize.medium:
        return AppSpacing.minTouchTarget;
      case AppButtonSize.large:
        return 56;
    }
  }

  double _getLoadingSize() {
    switch (size) {
      case AppButtonSize.small:
        return 16;
      case AppButtonSize.medium:
        return 20;
      case AppButtonSize.large:
        return 24;
    }
  }

  TextStyle _getTextStyle() {
    final baseStyle =
        size == AppButtonSize.small ? AppTypography.buttonSmall() : AppTypography.button();

    switch (variant) {
      case AppButtonVariant.primary:
        return baseStyle.copyWith(color: AppColors.neutralWhite);
      case AppButtonVariant.secondary:
        return baseStyle.copyWith(color: AppColors.primaryDarkBlue);
      case AppButtonVariant.ghost:
        return baseStyle.copyWith(color: AppColors.primaryBlueGray);
      case AppButtonVariant.danger:
        return baseStyle.copyWith(color: AppColors.neutralWhite);
      case AppButtonVariant.success:
        return baseStyle.copyWith(color: AppColors.neutralWhite);
    }
  }

  ButtonStyle _getPrimaryStyle(bool isDisabled) {
    return ElevatedButton.styleFrom(
      backgroundColor: _getPrimaryColor(),
      foregroundColor: AppColors.neutralWhite,
      disabledBackgroundColor: AppColors.neutralLightGray,
      disabledForegroundColor: AppColors.neutralMediumGray,
      elevation: 0,
      padding: EdgeInsets.symmetric(
        horizontal: _getHorizontalPadding(),
        vertical: AppSpacing.md,
      ),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(AppRadius.sm),
      ),
    );
  }

  ButtonStyle _getSecondaryStyle(bool isDisabled) {
    return OutlinedButton.styleFrom(
      foregroundColor: AppColors.primaryDarkBlue,
      disabledForegroundColor: AppColors.neutralMediumGray,
      side: BorderSide(
        color: isDisabled ? AppColors.neutralLightGray : AppColors.primaryDarkBlue,
        width: 2,
      ),
      padding: EdgeInsets.symmetric(
        horizontal: _getHorizontalPadding(),
        vertical: AppSpacing.md,
      ),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(AppRadius.sm),
      ),
    );
  }

  ButtonStyle _getGhostStyle(bool isDisabled) {
    return TextButton.styleFrom(
      foregroundColor: AppColors.primaryBlueGray,
      disabledForegroundColor: AppColors.neutralMediumGray,
      padding: EdgeInsets.symmetric(
        horizontal: _getHorizontalPadding(),
        vertical: AppSpacing.sm,
      ),
    );
  }

  Color _getPrimaryColor() {
    switch (variant) {
      case AppButtonVariant.primary:
        return AppColors.primaryGold;
      case AppButtonVariant.danger:
        return AppColors.semanticError;
      case AppButtonVariant.success:
        return AppColors.semanticSuccess;
      default:
        return AppColors.primaryGold;
    }
  }

  double _getHorizontalPadding() {
    switch (size) {
      case AppButtonSize.small:
        return AppSpacing.md;
      case AppButtonSize.medium:
        return AppSpacing.lg;
      case AppButtonSize.large:
        return AppSpacing.xl;
    }
  }
}
