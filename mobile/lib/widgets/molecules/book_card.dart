import 'package:flutter/material.dart';
import 'package:cached_network_image/cached_network_image.dart';
import '../../core/models/book.dart';
import '../../core/theme/app_colors.dart';
import '../../core/theme/app_typography.dart';
import '../../core/theme/app_spacing.dart';
import '../atoms/rating_stars.dart';
import '../atoms/app_badge.dart';
import '../atoms/app_button.dart';

enum BookCardVariant {
  standard, // 160px width for mobile grid
  featured, // Full width hero card
  compact, // Small card for carousels
  list, // Horizontal list layout
}

class BookCard extends StatelessWidget {
  final Book book;
  final BookCardVariant variant;
  final VoidCallback? onTap;
  final VoidCallback? onAddToCart;
  final VoidCallback? onWishlist;

  const BookCard({
    Key? key,
    required this.book,
    this.variant = BookCardVariant.standard,
    this.onTap,
    this.onAddToCart,
    this.onWishlist,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    switch (variant) {
      case BookCardVariant.standard:
        return _buildStandardCard(context);
      case BookCardVariant.featured:
        return _buildFeaturedCard(context);
      case BookCardVariant.compact:
        return _buildCompactCard(context);
      case BookCardVariant.list:
        return _buildListCard(context);
    }
  }

  // Standard Card (160px width)
  Widget _buildStandardCard(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 160,
        decoration: BoxDecoration(
          color: AppColors.neutralWhite,
          borderRadius: BorderRadius.circular(AppRadius.md),
          boxShadow: const [
            BoxShadow(
              color: Color(0x14000000),
              blurRadius: 8,
              offset: Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            // Cover Image
            Stack(
              children: [
                ClipRRect(
                  borderRadius: const BorderRadius.vertical(
                    top: Radius.circular(AppRadius.md),
                  ),
                  child: AspectRatio(
                    aspectRatio: 3 / 4,
                    child: CachedNetworkImage(
                      imageUrl: book.coverUrl,
                      fit: BoxFit.cover,
                      placeholder: (context, url) => Container(
                        color: AppColors.neutralLightGray,
                        child: const Center(
                          child: CircularProgressIndicator(
                            strokeWidth: 2,
                            valueColor: AlwaysStoppedAnimation(AppColors.primaryGold),
                          ),
                        ),
                      ),
                      errorWidget: (context, url, error) => Container(
                        color: AppColors.neutralLightGray,
                        child: const Icon(Icons.book, size: 48, color: AppColors.neutralMediumGray),
                      ),
                    ),
                  ),
                ),
                if (book.badge != null) _buildBadgeOverlay(book.badge!),
              ],
            ),

            // Book Info
            Padding(
              padding: const EdgeInsets.all(AppSpacing.sm),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  // Title
                  Text(
                    book.title,
                    style: AppTypography.bodySmall(color: AppColors.primaryDarkBlue).copyWith(
                      fontWeight: FontWeight.w600,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: AppSpacing.xs),

                  // Author
                  Text(
                    'by ${book.author}',
                    style: AppTypography.caption(color: AppColors.neutralMediumGray),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: AppSpacing.xs),

                  // Rating
                  RatingStars(
                    rating: book.rating,
                    size: RatingSize.small,
                    showValue: true,
                  ),
                  const SizedBox(height: AppSpacing.sm),

                  // Price
                  Text(
                    book.formatPrice,
                    style: AppTypography.body(color: AppColors.primaryGold).copyWith(
                      fontWeight: FontWeight.w700,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  // Featured Card (Hero)
  Widget _buildFeaturedCard(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: AppColors.primaryYellow,
          borderRadius: BorderRadius.circular(AppRadius.xl),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Cover Image
            Stack(
              children: [
                ClipRRect(
                  borderRadius: const BorderRadius.vertical(
                    top: Radius.circular(AppRadius.xl),
                  ),
                  child: AspectRatio(
                    aspectRatio: 16 / 9,
                    child: CachedNetworkImage(
                      imageUrl: book.coverUrl,
                      fit: BoxFit.cover,
                      placeholder: (context, url) => Container(
                        color: AppColors.neutralLightGray,
                      ),
                      errorWidget: (context, url, error) => Container(
                        color: AppColors.neutralLightGray,
                        child: const Icon(Icons.book, size: 64),
                      ),
                    ),
                  ),
                ),
                if (book.badge != null) _buildBadgeOverlay(book.badge!),
              ],
            ),

            // Book Info
            Padding(
              padding: const EdgeInsets.all(AppSpacing.lg),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Badge
                  const AppBadge.text(
                    text: 'Book of the Month',
                    variant: BadgeVariant.warning,
                  ),
                  const SizedBox(height: AppSpacing.md),

                  // Title
                  Text(
                    book.title,
                    style: AppTypography.h2(color: AppColors.primaryDarkBlue),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: AppSpacing.sm),

                  // Author
                  Text(
                    'by ${book.author}',
                    style: AppTypography.bodyLarge(color: AppColors.neutralMediumGray),
                  ),
                  const SizedBox(height: AppSpacing.sm),

                  // Rating
                  RatingStars(
                    rating: book.rating,
                    size: RatingSize.medium,
                    showValue: true,
                    reviewCount: book.reviewCount,
                  ),
                  const SizedBox(height: AppSpacing.md),

                  // Description
                  Text(
                    book.description,
                    style: AppTypography.body(color: AppColors.neutralDarkGray),
                    maxLines: 3,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: AppSpacing.lg),

                  // Action Buttons
                  Row(
                    children: [
                      Expanded(
                        child: AppButton(
                          text: 'Add to Cart',
                          onPressed: onAddToCart,
                          size: AppButtonSize.large,
                        ),
                      ),
                      const SizedBox(width: AppSpacing.md),
                      Expanded(
                        child: AppButton(
                          text: 'Buy ${book.formatPrice}',
                          onPressed: onTap,
                          variant: AppButtonVariant.secondary,
                          size: AppButtonSize.large,
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  // Compact Card (Carousel)
  Widget _buildCompactCard(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 120,
        decoration: BoxDecoration(
          color: AppColors.neutralWhite,
          borderRadius: BorderRadius.circular(AppRadius.md),
          boxShadow: const [
            BoxShadow(
              color: Color(0x14000000),
              blurRadius: 4,
              offset: Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            // Cover Image
            Stack(
              children: [
                ClipRRect(
                  borderRadius: const BorderRadius.vertical(
                    top: Radius.circular(AppRadius.md),
                  ),
                  child: AspectRatio(
                    aspectRatio: 3 / 4,
                    child: CachedNetworkImage(
                      imageUrl: book.coverUrl,
                      fit: BoxFit.cover,
                      placeholder: (context, url) => Container(
                        color: AppColors.neutralLightGray,
                      ),
                      errorWidget: (context, url, error) => Container(
                        color: AppColors.neutralLightGray,
                        child: const Icon(Icons.book, size: 32),
                      ),
                    ),
                  ),
                ),
                if (book.badge != null) _buildBadgeOverlay(book.badge!),
              ],
            ),

            // Book Info
            Padding(
              padding: const EdgeInsets.all(AppSpacing.xs),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    book.title,
                    style: AppTypography.caption(color: AppColors.primaryDarkBlue).copyWith(
                      fontWeight: FontWeight.w600,
                    ),
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 2),
                  Text(
                    book.formatPrice,
                    style: AppTypography.caption(color: AppColors.primaryGold).copyWith(
                      fontWeight: FontWeight.w700,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  // List Card (Horizontal)
  Widget _buildListCard(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(AppSpacing.md),
        decoration: BoxDecoration(
          color: AppColors.neutralWhite,
          borderRadius: BorderRadius.circular(AppRadius.md),
          boxShadow: const [
            BoxShadow(
              color: Color(0x14000000),
              blurRadius: 8,
              offset: Offset(0, 2),
            ),
          ],
        ),
        child: Row(
          children: [
            // Cover Image
            Stack(
              children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(AppRadius.sm),
                  child: SizedBox(
                    width: 80,
                    height: 106,
                    child: CachedNetworkImage(
                      imageUrl: book.coverUrl,
                      fit: BoxFit.cover,
                      placeholder: (context, url) => Container(
                        color: AppColors.neutralLightGray,
                      ),
                      errorWidget: (context, url, error) => Container(
                        color: AppColors.neutralLightGray,
                        child: const Icon(Icons.book, size: 32),
                      ),
                    ),
                  ),
                ),
                if (book.badge != null)
                  Positioned(
                    top: 4,
                    right: 4,
                    child: _buildBadgeOverlay(book.badge!),
                  ),
              ],
            ),
            const SizedBox(width: AppSpacing.md),

            // Book Info
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    book.title,
                    style: AppTypography.h4(color: AppColors.primaryDarkBlue),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: AppSpacing.xs),
                  Text(
                    'by ${book.author}',
                    style: AppTypography.bodySmall(color: AppColors.neutralMediumGray),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: AppSpacing.sm),
                  RatingStars(
                    rating: book.rating,
                    size: RatingSize.small,
                    showValue: true,
                    reviewCount: book.reviewCount,
                  ),
                  const SizedBox(height: AppSpacing.xs),
                  Text(
                    '${book.formatDisplay} • ${book.pages} pages',
                    style: AppTypography.caption(color: AppColors.neutralMediumGray),
                  ),
                ],
              ),
            ),

            // Price & Actions
            Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  book.formatPrice,
                  style: AppTypography.h4(color: AppColors.primaryGold),
                ),
                const SizedBox(height: AppSpacing.sm),
                Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    if (onWishlist != null)
                      IconButton(
                        icon: const Icon(Icons.favorite_border, size: 20),
                        color: AppColors.primaryDarkBlue,
                        onPressed: onWishlist,
                        padding: EdgeInsets.zero,
                        constraints: const BoxConstraints(),
                      ),
                    if (onWishlist != null) const SizedBox(width: AppSpacing.sm),
                    if (onAddToCart != null)
                      IconButton(
                        icon: const Icon(Icons.add_shopping_cart, size: 20),
                        color: AppColors.primaryGold,
                        onPressed: onAddToCart,
                        padding: EdgeInsets.zero,
                        constraints: const BoxConstraints(),
                      ),
                  ],
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildBadgeOverlay(BookBadge badge) {
    String text;
    BadgeVariant variant;

    switch (badge) {
      case BookBadge.newRelease:
        text = 'NEW';
        variant = BadgeVariant.info;
        break;
      case BookBadge.bestseller:
        text = 'BESTSELLER';
        variant = BadgeVariant.warning;
        break;
      case BookBadge.featured:
        text = 'FEATURED';
        variant = BadgeVariant.success;
        break;
      case BookBadge.winner:
        text = 'WINNER';
        variant = BadgeVariant.success;
        break;
      case BookBadge.nominee:
        text = 'NOMINEE';
        variant = BadgeVariant.info;
        break;
    }

    return Positioned(
      top: AppSpacing.sm,
      right: AppSpacing.sm,
      child: AppBadge.text(text: text, variant: variant),
    );
  }
}
