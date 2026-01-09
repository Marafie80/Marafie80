import React from 'react';

interface SkeletonProps {
  variant?: 'text' | 'rectangular' | 'circular';
  width?: string | number;
  height?: string | number;
  className?: string;
  shimmer?: boolean;
}

const Skeleton: React.FC<SkeletonProps> = ({
  variant = 'rectangular',
  width,
  height,
  className = '',
  shimmer = true,
}) => {
  const variantClasses = {
    text: 'rounded',
    rectangular: 'rounded',
    circular: 'rounded-full',
  };

  const baseClass = shimmer ? 'skeleton-shimmer' : 'skeleton';

  const style: React.CSSProperties = {
    width: typeof width === 'number' ? `${width}px` : width,
    height: typeof height === 'number' ? `${height}px` : height,
  };

  return (
    <div
      className={`
        ${baseClass}
        ${variantClasses[variant]}
        ${className}
      `.trim().replace(/\s+/g, ' ')}
      style={style}
      aria-label="Loading..."
      role="status"
    />
  );
};

export default Skeleton;
