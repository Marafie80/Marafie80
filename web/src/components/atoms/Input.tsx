import React from 'react';
import { InputProps } from '@/types';

const Input: React.FC<InputProps> = ({
  type = 'text',
  label,
  placeholder,
  value,
  onChange,
  error,
  helperText,
  required = false,
  disabled = false,
  icon,
  iconPosition = 'left',
  autoComplete,
  className = '',
}) => {
  const inputClasses = `
    input
    ${error ? 'input-error' : ''}
    ${icon && iconPosition === 'left' ? 'pl-12' : ''}
    ${icon && iconPosition === 'right' ? 'pr-12' : ''}
    ${className}
  `.trim().replace(/\s+/g, ' ');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange(e.target.value);
  };

  return (
    <div className="w-full">
      {label && (
        <label className="block text-body-sm font-medium text-primary-dark-blue mb-2">
          {label}
          {required && <span className="text-semantic-error ml-1">*</span>}
        </label>
      )}

      <div className="relative">
        {icon && iconPosition === 'left' && (
          <div className="absolute left-4 top-1/2 -translate-y-1/2 text-neutral-medium-gray">
            {icon}
          </div>
        )}

        <input
          type={type}
          value={value}
          onChange={handleChange}
          placeholder={placeholder}
          disabled={disabled}
          required={required}
          autoComplete={autoComplete}
          className={inputClasses}
          aria-invalid={error ? 'true' : 'false'}
          aria-describedby={
            error ? 'error-message' : helperText ? 'helper-text' : undefined
          }
        />

        {icon && iconPosition === 'right' && (
          <div className="absolute right-4 top-1/2 -translate-y-1/2 text-neutral-medium-gray">
            {icon}
          </div>
        )}

        {error && !icon && iconPosition === 'right' && (
          <div className="absolute right-4 top-1/2 -translate-y-1/2 text-semantic-error">
            <svg
              className="w-5 h-5"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                fillRule="evenodd"
                d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
                clipRule="evenodd"
              />
            </svg>
          </div>
        )}

        {!error && value && type !== 'password' && (
          <div className="absolute right-4 top-1/2 -translate-y-1/2 text-semantic-success">
            <svg
              className="w-5 h-5"
              fill="currentColor"
              viewBox="0 0 20 20"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                fillRule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                clipRule="evenodd"
              />
            </svg>
          </div>
        )}
      </div>

      {error && (
        <p id="error-message" className="mt-2 text-body-sm text-semantic-error">
          {error}
        </p>
      )}

      {helperText && !error && (
        <p id="helper-text" className="mt-2 text-body-sm text-neutral-medium-gray">
          {helperText}
        </p>
      )}
    </div>
  );
};

export default Input;
