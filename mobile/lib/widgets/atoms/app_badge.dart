import 'package:flutter/material.dart';
import '../../core/theme/app_colors.dart';
import '../../core/theme/app_typography.dart';
import '../../core/theme/app_spacing.dart';

enum BadgeVariant {
  primary,
  success,
  error,
  warning,
  info,
}

class AppBadge extends StatelessWidget {
  final String? text;
  final int? count;
  final int maxCount;
  final BadgeVariant variant;
  final bool dot;

  const AppBadge({
    Key? key,
    this.text,
    this.count,
    this.maxCount = 99,
    this.variant = BadgeVariant.error,
    this.dot = false,
  }) : super(key: key);

  const AppBadge.dot({
    Key? key,
    this.variant = BadgeVariant.error,
  })  : text = null,
        count = null,
        maxCount = 99,
        dot = true,
        super(key: key);

  const AppBadge.count({
    Key? key,
    required int count,
    this.maxCount = 99,
    this.variant = BadgeVariant.error,
  })  : count = count,
        text = null,
        dot = false,
        super(key: key);

  const AppBadge.text({
    Key? key,
    required String text,
    this.variant = BadgeVariant.info,
  })  : text = text,
        count = null,
        maxCount = 99,
        dot = false,
        super(key: key);

  @override
  Widget build(BuildContext context) {
    if (dot) {
      return Container(
        width: 8,
        height: 8,
        decoration: BoxDecoration(
          color: _getBackgroundColor(),
          shape: BoxShape.circle,
        ),
      );
    }

    if (count != null) {
      final displayCount = count! > maxCount ? '$maxCount+' : count.toString();

      return Container(
        constraints: const BoxConstraints(minWidth: 20, minHeight: 20),
        padding: const EdgeInsets.symmetric(
          horizontal: AppSpacing.xs,
          vertical: 2,
        ),
        decoration: BoxDecoration(
          color: _getBackgroundColor(),
          borderRadius: BorderRadius.circular(AppRadius.full),
          border: Border.all(color: AppColors.neutralWhite, width: 2),
        ),
        child: Center(
          child: Text(
            displayCount,
            style: AppTypography.caption(color: AppColors.neutralWhite).copyWith(
              fontWeight: FontWeight.w700,
              height: 1.0,
            ),
          ),
        ),
      );
    }

    if (text != null) {
      return Container(
        padding: const EdgeInsets.symmetric(
          horizontal: AppSpacing.sm,
          vertical: AppSpacing.xs,
        ),
        decoration: BoxDecoration(
          color: _getBackgroundColor(),
          borderRadius: BorderRadius.circular(AppRadius.full),
        ),
        child: Center(
          child: Text(
            text!,
            style: AppTypography.caption(
              color: variant == BadgeVariant.warning
                  ? AppColors.primaryDarkBlue
                  : AppColors.neutralWhite,
            ).copyWith(
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
      );
    }

    return const SizedBox.shrink();
  }

  Color _getBackgroundColor() {
    switch (variant) {
      case BadgeVariant.primary:
        return AppColors.primaryGold;
      case BadgeVariant.success:
        return AppColors.semanticSuccess;
      case BadgeVariant.error:
        return AppColors.semanticError;
      case BadgeVariant.warning:
        return AppColors.semanticWarning;
      case BadgeVariant.info:
        return AppColors.primaryBlueGray;
    }
  }
}

/// Badge that can wrap another widget (e.g., icon with notification badge)
class BadgedWidget extends StatelessWidget {
  final Widget child;
  final AppBadge badge;
  final Alignment alignment;

  const BadgedWidget({
    Key? key,
    required this.child,
    required this.badge,
    this.alignment = Alignment.topRight,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(
      clipBehavior: Clip.none,
      children: [
        child,
        Positioned(
          top: alignment.y < 0 ? -4 : null,
          bottom: alignment.y > 0 ? -4 : null,
          right: alignment.x > 0 ? -4 : null,
          left: alignment.x < 0 ? -4 : null,
          child: badge,
        ),
      ],
    );
  }
}
