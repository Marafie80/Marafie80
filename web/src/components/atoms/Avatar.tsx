import React from 'react';
import Image from 'next/image';

interface AvatarProps {
  src?: string;
  alt?: string;
  name?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
}

const Avatar: React.FC<AvatarProps> = ({
  src,
  alt,
  name,
  size = 'md',
  className = '',
}) => {
  const sizeClasses = {
    sm: 'w-8 h-8 text-sm',
    md: 'w-10 h-10 text-base',
    lg: 'w-14 h-14 text-lg',
    xl: 'w-20 h-20 text-2xl',
  };

  const getInitials = (fullName: string): string => {
    const parts = fullName.trim().split(' ');
    if (parts.length >= 2) {
      return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase();
    }
    return parts[0].substring(0, 2).toUpperCase();
  };

  const initials = name ? getInitials(name) : '?';

  return (
    <div
      className={`
        ${sizeClasses[size]}
        rounded-full overflow-hidden
        bg-primary-blue-gray text-neutral-white
        flex items-center justify-center
        font-semibold
        border-2 border-neutral-white
        shadow-sm
        ${className}
      `.trim().replace(/\s+/g, ' ')}
      role="img"
      aria-label={alt || name || 'User avatar'}
    >
      {src ? (
        <Image
          src={src}
          alt={alt || name || 'User avatar'}
          fill
          className="object-cover"
          onError={(e) => {
            // Fallback to initials if image fails to load
            const target = e.target as HTMLImageElement;
            target.style.display = 'none';
          }}
        />
      ) : (
        <span>{initials}</span>
      )}
    </div>
  );
};

export default Avatar;
