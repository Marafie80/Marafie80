import React, { useState } from 'react';

interface RatingProps {
  value: number;
  max?: number;
  size?: 'sm' | 'md' | 'lg';
  readonly?: boolean;
  onChange?: (value: number) => void;
  showValue?: boolean;
  count?: number;
  className?: string;
}

const Rating: React.FC<RatingProps> = ({
  value,
  max = 5,
  size = 'md',
  readonly = true,
  onChange,
  showValue = false,
  count,
  className = '',
}) => {
  const [hoverValue, setHoverValue] = useState<number | null>(null);

  const sizeClasses = {
    sm: 'w-4 h-4',
    md: 'w-5 h-5',
    lg: 'w-6 h-6',
  };

  const handleClick = (rating: number) => {
    if (!readonly && onChange) {
      onChange(rating);
    }
  };

  const handleMouseEnter = (rating: number) => {
    if (!readonly) {
      setHoverValue(rating);
    }
  };

  const handleMouseLeave = () => {
    setHoverValue(null);
  };

  const renderStar = (index: number) => {
    const rating = index + 1;
    const currentValue = hoverValue !== null ? hoverValue : value;
    const fillPercentage = Math.min(Math.max(currentValue - index, 0), 1) * 100;

    return (
      <button
        key={index}
        type="button"
        className={`
          relative inline-block ${sizeClasses[size]}
          ${readonly ? 'cursor-default' : 'cursor-pointer'}
          focus:outline-none focus-visible-ring rounded-sm
        `.trim().replace(/\s+/g, ' ')}
        onClick={() => handleClick(rating)}
        onMouseEnter={() => handleMouseEnter(rating)}
        onMouseLeave={handleMouseLeave}
        disabled={readonly}
        aria-label={`${rating} star${rating !== 1 ? 's' : ''}`}
      >
        {/* Empty star (background) */}
        <svg
          className="absolute inset-0 text-neutral-light-gray"
          fill="currentColor"
          viewBox="0 0 20 20"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
        </svg>

        {/* Filled star (gradient overlay) */}
        <svg
          className="absolute inset-0 text-yellow-500"
          fill="currentColor"
          viewBox="0 0 20 20"
          xmlns="http://www.w3.org/2000/svg"
          style={{ clipPath: `inset(0 ${100 - fillPercentage}% 0 0)` }}
        >
          <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
        </svg>
      </button>
    );
  };

  return (
    <div className={`inline-flex items-center gap-2 ${className}`}>
      <div
        className="inline-flex items-center gap-0.5"
        role="img"
        aria-label={`Rating: ${value} out of ${max} stars`}
      >
        {Array.from({ length: max }, (_, i) => renderStar(i))}
      </div>

      {(showValue || count !== undefined) && (
        <span className="text-body-sm text-primary-dark-blue">
          {showValue && <span className="font-semibold">{value.toFixed(1)}</span>}
          {count !== undefined && (
            <span className="text-neutral-medium-gray">
              {showValue && ' '}({count.toLocaleString()})
            </span>
          )}
        </span>
      )}
    </div>
  );
};

export default Rating;
