import React from 'react';
import Image from 'next/image';
import { BookCardProps } from '@/types';
import { Button, Badge, Rating } from '@/components/atoms';

const BookCard: React.FC<BookCardProps> = ({
  book,
  variant = 'standard',
  onAddToCart,
  onWishlist,
  onClick,
  className = '',
}) => {
  const formatPrice = (price: number, currency: string = 'USD') => {
    if (book.isFree) return 'FREE';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency,
    }).format(price);
  };

  const renderBadge = () => {
    if (!book.badge) return null;

    const badgeConfig = {
      new: { text: 'NEW', variant: 'info' as const },
      bestseller: { text: 'BESTSELLER', variant: 'warning' as const },
      featured: { text: 'FEATURED', variant: 'success' as const },
      winner: { text: 'WINNER', variant: 'success' as const },
      nominee: { text: 'NOMINEE', variant: 'info' as const },
    };

    const config = badgeConfig[book.badge];

    return (
      <div className="absolute top-2 right-2 z-10">
        <Badge variant={config.variant}>{config.text}</Badge>
      </div>
    );
  };

  // Standard Card (220px × 380px)
  if (variant === 'standard') {
    return (
      <div
        className={`
          book-card w-[220px] group
          ${className}
        `.trim().replace(/\s+/g, ' ')}
        onClick={onClick}
      >
        <div className="relative aspect-[3/4] rounded-t overflow-hidden bg-neutral-light-gray">
          {renderBadge()}
          <Image
            src={book.coverUrl}
            alt={`${book.title} by ${book.author}`}
            fill
            className="object-cover"
            sizes="220px"
          />
        </div>

        <div className="p-4 space-y-2">
          <h4 className="text-h4 font-semibold truncate-2 min-h-[2.5rem]">
            {book.title}
          </h4>
          <p className="text-body-sm text-neutral-medium-gray truncate">
            by {book.author}
          </p>

          <Rating
            value={book.rating}
            readonly
            showValue
            count={book.reviewCount}
            size="sm"
          />

          <div className="flex items-center justify-between pt-2">
            <p className="text-h4 font-bold text-primary-gold">
              {formatPrice(book.price, book.currency)}
            </p>

            <div className="opacity-0 group-hover:opacity-100 transition-opacity duration-normal flex items-center gap-2">
              {onWishlist && (
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    onWishlist();
                  }}
                  className="p-2 rounded-full hover:bg-neutral-light-gray transition-colors"
                  aria-label="Add to wishlist"
                >
                  <svg
                    className="w-5 h-5 text-primary-dark-blue"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
                    />
                  </svg>
                </button>
              )}

              {onAddToCart && !book.isOwned && (
                <button
                  onClick={(e) => {
                    e.stopPropagation();
                    onAddToCart();
                  }}
                  className="p-2 rounded-full bg-primary-gold hover:bg-primary-gold/90 text-neutral-white transition-colors"
                  aria-label="Add to cart"
                >
                  <svg
                    className="w-5 h-5"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"
                    />
                  </svg>
                </button>
              )}
            </div>
          </div>

          {book.isOwned && (
            <Button variant="primary" size="sm" fullWidth>
              Read Now
            </Button>
          )}
        </div>
      </div>
    );
  }

  // Featured Card (320px × 480px)
  if (variant === 'featured') {
    return (
      <div
        className={`
          card w-full max-w-[320px] group cursor-pointer
          ${className}
        `.trim().replace(/\s+/g, ' ')}
        onClick={onClick}
      >
        <div className="relative aspect-[16/9] rounded-t overflow-hidden bg-neutral-light-gray">
          {renderBadge()}
          <Image
            src={book.coverUrl}
            alt={`${book.title} by ${book.author}`}
            fill
            className="object-cover"
            sizes="320px"
          />
        </div>

        <div className="p-6 space-y-3">
          <div className="inline-block px-3 py-1 bg-primary-yellow rounded-full">
            <span className="text-body-sm font-semibold text-primary-dark-blue">
              Book of the Month
            </span>
          </div>

          <h2 className="text-h2 font-bold truncate-2">{book.title}</h2>
          <p className="text-body-lg text-neutral-medium-gray">
            by {book.author}
          </p>

          <Rating
            value={book.rating}
            readonly
            showValue
            count={book.reviewCount}
            size="md"
          />

          <p className="text-body text-neutral-dark-gray truncate-3">
            {book.description}
          </p>

          <div className="flex items-center gap-3 pt-2">
            <Button variant="primary" size="lg" onClick={onAddToCart} fullWidth>
              Add to Cart
            </Button>
            <Button variant="secondary" size="lg" fullWidth>
              Buy Now - {formatPrice(book.price, book.currency)}
            </Button>
          </div>
        </div>
      </div>
    );
  }

  // Compact Card (160px × 280px - Mobile)
  if (variant === 'compact') {
    return (
      <div
        className={`
          book-card w-[160px] group
          ${className}
        `.trim().replace(/\s+/g, ' ')}
        onClick={onClick}
      >
        <div className="relative aspect-[3/4] rounded-t overflow-hidden bg-neutral-light-gray">
          {renderBadge()}
          <Image
            src={book.coverUrl}
            alt={`${book.title} by ${book.author}`}
            fill
            className="object-cover"
            sizes="160px"
          />
        </div>

        <div className="p-2 space-y-1">
          <h4 className="text-body-sm font-semibold truncate-2 min-h-[2.5rem]">
            {book.title}
          </h4>
          <p className="text-caption text-neutral-medium-gray truncate">
            {book.author}
          </p>

          <div className="flex items-center gap-1">
            <Rating value={book.rating} readonly size="sm" showValue />
          </div>

          <p className="text-body font-bold text-primary-gold">
            {formatPrice(book.price, book.currency)}
          </p>
        </div>
      </div>
    );
  }

  // List View Card
  if (variant === 'list') {
    return (
      <div
        className={`
          card flex gap-4 p-4 cursor-pointer group
          ${className}
        `.trim().replace(/\s+/g, ' ')}
        onClick={onClick}
      >
        <div className="relative w-20 h-[106px] flex-shrink-0 rounded overflow-hidden bg-neutral-light-gray">
          {renderBadge()}
          <Image
            src={book.coverUrl}
            alt={`${book.title} by ${book.author}`}
            fill
            className="object-cover"
            sizes="80px"
          />
        </div>

        <div className="flex-1 min-w-0 space-y-2">
          <div>
            <h4 className="text-h4 font-semibold truncate">{book.title}</h4>
            <p className="text-body-sm text-neutral-medium-gray">
              by {book.author}
            </p>
          </div>

          <div className="flex items-center gap-3 text-caption text-neutral-medium-gray">
            <Rating value={book.rating} readonly size="sm" showValue count={book.reviewCount} />
            <span>•</span>
            <span>{book.category}</span>
          </div>

          <div className="flex items-center gap-3 text-caption text-neutral-medium-gray">
            <span>{book.format}</span>
            <span>•</span>
            <span>{book.pages} pages</span>
          </div>
        </div>

        <div className="flex flex-col items-end justify-between">
          <p className="text-h4 font-bold text-primary-gold">
            {formatPrice(book.price, book.currency)}
          </p>

          <div className="flex items-center gap-2">
            {onWishlist && (
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  onWishlist();
                }}
                className="p-2 rounded-full hover:bg-neutral-light-gray transition-colors"
                aria-label="Add to wishlist"
              >
                <svg
                  className="w-5 h-5 text-primary-dark-blue"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"
                  />
                </svg>
              </button>
            )}

            {onAddToCart && (
              <Button size="sm" onClick={onAddToCart}>
                Add to Cart
              </Button>
            )}

            <Button variant="secondary" size="sm">
              View
            </Button>
          </div>
        </div>
      </div>
    );
  }

  return null;
};

export default BookCard;
