import React from 'react';

interface BadgeProps {
  count?: number;
  variant?: 'default' | 'success' | 'error' | 'warning' | 'info';
  dot?: boolean;
  max?: number;
  className?: string;
  children?: React.ReactNode;
}

const Badge: React.FC<BadgeProps> = ({
  count,
  variant = 'default',
  dot = false,
  max = 99,
  className = '',
  children,
}) => {
  const variantClasses = {
    default: 'bg-semantic-error text-neutral-white',
    success: 'bg-semantic-success text-neutral-white',
    error: 'bg-semantic-error text-neutral-white',
    warning: 'bg-semantic-warning text-primary-dark-blue',
    info: 'bg-primary-blue-gray text-neutral-white',
  };

  const displayCount = count !== undefined && count > max ? `${max}+` : count;

  if (dot) {
    return (
      <span
        className={`inline-block w-2 h-2 rounded-full ${variantClasses[variant]} ${className}`}
        aria-label="Notification indicator"
      />
    );
  }

  if (children) {
    return (
      <span
        className={`
          inline-flex items-center justify-center
          px-2 py-0.5 rounded-full
          text-caption font-semibold
          ${variantClasses[variant]}
          ${className}
        `.trim().replace(/\s+/g, ' ')}
      >
        {children}
      </span>
    );
  }

  if (count !== undefined) {
    return (
      <span
        className={`
          inline-flex items-center justify-center
          min-w-[20px] h-5 px-1.5 rounded-full
          text-caption font-bold
          border-2 border-neutral-white
          ${variantClasses[variant]}
          ${className}
        `.trim().replace(/\s+/g, ' ')}
        aria-label={`${count} notifications`}
      >
        {displayCount}
      </span>
    );
  }

  return null;
};

export default Badge;
