import 'package:flutter/material.dart';
import '../../core/theme/app_colors.dart';
import '../../core/theme/app_typography.dart';
import '../../core/theme/app_spacing.dart';

enum RatingSize {
  small,
  medium,
  large,
}

class RatingStars extends StatelessWidget {
  final double rating;
  final int maxStars;
  final RatingSize size;
  final bool showValue;
  final int? reviewCount;
  final bool interactive;
  final ValueChanged<double>? onRatingChanged;

  const RatingStars({
    Key? key,
    required this.rating,
    this.maxStars = 5,
    this.size = RatingSize.medium,
    this.showValue = false,
    this.reviewCount,
    this.interactive = false,
    this.onRatingChanged,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        // Stars
        Row(
          mainAxisSize: MainAxisSize.min,
          children: List.generate(
            maxStars,
            (index) => _buildStar(index + 1),
          ),
        ),

        // Rating value and count
        if (showValue || reviewCount != null) ...[
          const SizedBox(width: AppSpacing.sm),
          Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              if (showValue)
                Text(
                  rating.toStringAsFixed(1),
                  style: _getTextStyle().copyWith(fontWeight: FontWeight.w600),
                ),
              if (reviewCount != null)
                Text(
                  showValue ? ' (${_formatCount(reviewCount!)})' : '(${_formatCount(reviewCount!)})',
                  style: _getTextStyle().copyWith(color: AppColors.neutralMediumGray),
                ),
            ],
          ),
        ],
      ],
    );
  }

  Widget _buildStar(int starNumber) {
    final double starSize = _getStarSize();
    final double fillPercentage = (rating - (starNumber - 1)).clamp(0.0, 1.0);

    return GestureDetector(
      onTap: interactive && onRatingChanged != null ? () => onRatingChanged!(starNumber.toDouble()) : null,
      child: Padding(
        padding: const EdgeInsets.only(right: 2),
        child: SizedBox(
          width: starSize,
          height: starSize,
          child: Stack(
            children: [
              // Empty star (background)
              Icon(
                Icons.star,
                size: starSize,
                color: AppColors.neutralLightGray,
              ),
              // Filled star (foreground with clip)
              ClipRect(
                clipper: _StarClipper(fillPercentage),
                child: Icon(
                  Icons.star,
                  size: starSize,
                  color: AppColors.starFilled,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  double _getStarSize() {
    switch (size) {
      case RatingSize.small:
        return 16;
      case RatingSize.medium:
        return 20;
      case RatingSize.large:
        return 24;
    }
  }

  TextStyle _getTextStyle() {
    switch (size) {
      case RatingSize.small:
        return AppTypography.caption();
      case RatingSize.medium:
        return AppTypography.bodySmall();
      case RatingSize.large:
        return AppTypography.body();
    }
  }

  String _formatCount(int count) {
    if (count >= 1000000) {
      return '${(count / 1000000).toStringAsFixed(1)}M';
    } else if (count >= 1000) {
      return '${(count / 1000).toStringAsFixed(1)}K';
    }
    return count.toString();
  }
}

class _StarClipper extends CustomClipper<Rect> {
  final double fillPercentage;

  _StarClipper(this.fillPercentage);

  @override
  Rect getClip(Size size) {
    return Rect.fromLTWH(0, 0, size.width * fillPercentage, size.height);
  }

  @override
  bool shouldReclip(covariant _StarClipper oldClipper) {
    return oldClipper.fillPercentage != fillPercentage;
  }
}
