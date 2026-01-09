'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { Avatar, Badge } from '@/components/atoms';
import { User } from '@/types';

interface NavbarProps {
  user?: User;
  cartItemCount?: number;
  onSearch?: (query: string) => void;
  onCartClick?: () => void;
  onProfileClick?: () => void;
  onLogoClick?: () => void;
}

const Navbar: React.FC<NavbarProps> = ({
  user,
  cartItemCount = 0,
  onSearch,
  onCartClick,
  onProfileClick,
  onLogoClick,
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (onSearch && searchQuery.trim()) {
      onSearch(searchQuery);
    }
  };

  const handleProfileMenuToggle = () => {
    setIsProfileMenuOpen(!isProfileMenuOpen);
  };

  return (
    <header className="sticky top-0 z-50 bg-neutral-white border-b border-neutral-light-gray shadow-sm">
      <nav className="container-custom">
        <div className="flex items-center justify-between h-18 md:h-20">
          {/* Mobile Menu Button */}
          <button
            className="lg:hidden p-2 hover:bg-neutral-light-gray rounded transition-colors"
            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
            aria-label="Toggle menu"
          >
            <svg
              className="w-6 h-6 text-primary-dark-blue"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          </button>

          {/* Logo */}
          <Link
            href="/"
            className="flex items-center gap-2 hover:opacity-80 transition-opacity"
            onClick={onLogoClick}
          >
            <span className="text-3xl">📚</span>
            <span className="font-cairo font-bold text-2xl text-primary-dark-blue hidden sm:block">
              BookMart
            </span>
          </Link>

          {/* Desktop Search Bar */}
          <form
            onSubmit={handleSearchSubmit}
            className="hidden lg:flex flex-1 max-w-2xl mx-8"
          >
            <div className="relative w-full">
              <input
                type="search"
                placeholder="Search books, authors, categories..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full h-12 pl-12 pr-4 bg-neutral-white border border-neutral-light-gray
                         rounded-full text-body placeholder:text-neutral-medium-gray
                         focus:outline-none focus:border-primary-gold focus:ring-2 focus:ring-primary-gold/10
                         transition-all"
              />
              <svg
                className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-neutral-medium-gray"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
            </div>
          </form>

          {/* Right Actions */}
          <div className="flex items-center gap-4">
            {/* Mobile Search Button */}
            <button
              className="lg:hidden p-2 hover:bg-neutral-light-gray rounded transition-colors"
              aria-label="Search"
            >
              <svg
                className="w-6 h-6 text-primary-dark-blue"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
            </button>

            {/* Cart */}
            <button
              className="relative p-2 hover:bg-neutral-light-gray rounded transition-colors"
              onClick={onCartClick}
              aria-label="Shopping cart"
            >
              <svg
                className="w-6 h-6 text-primary-dark-blue"
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
              {cartItemCount > 0 && (
                <div className="absolute -top-1 -right-1">
                  <Badge count={cartItemCount} variant="error" />
                </div>
              )}
            </button>

            {/* Profile / Login */}
            {user ? (
              <div className="relative">
                <button
                  onClick={handleProfileMenuToggle}
                  className="focus:outline-none focus-visible-ring rounded-full"
                  aria-label="User menu"
                >
                  <Avatar
                    src={user.avatar}
                    name={user.name}
                    alt={user.name}
                    size="md"
                  />
                </button>

                {/* Profile Dropdown */}
                {isProfileMenuOpen && (
                  <>
                    <div
                      className="fixed inset-0 z-40"
                      onClick={() => setIsProfileMenuOpen(false)}
                    />
                    <div className="absolute right-0 mt-2 w-64 bg-neutral-white rounded-lg shadow-xl border border-neutral-light-gray z-50 animate-scale-in">
                      <div className="p-4 border-b border-neutral-light-gray">
                        <p className="font-semibold text-primary-dark-blue">
                          {user.name}
                        </p>
                        <p className="text-body-sm text-neutral-medium-gray">
                          {user.readerLevel} Reader
                        </p>
                      </div>

                      <div className="py-2">
                        <Link
                          href="/profile"
                          className="block px-4 py-2 text-body hover:bg-neutral-light-gray transition-colors"
                        >
                          My Account
                        </Link>
                        <Link
                          href="/orders"
                          className="block px-4 py-2 text-body hover:bg-neutral-light-gray transition-colors"
                        >
                          Orders
                        </Link>
                        <Link
                          href="/library"
                          className="block px-4 py-2 text-body hover:bg-neutral-light-gray transition-colors"
                        >
                          Library
                        </Link>
                        <Link
                          href="/wishlist"
                          className="block px-4 py-2 text-body hover:bg-neutral-light-gray transition-colors"
                        >
                          Wishlist
                        </Link>
                        <Link
                          href="/settings"
                          className="block px-4 py-2 text-body hover:bg-neutral-light-gray transition-colors"
                        >
                          Settings
                        </Link>
                        <Link
                          href="/support"
                          className="block px-4 py-2 text-body hover:bg-neutral-light-gray transition-colors"
                        >
                          Support
                        </Link>
                      </div>

                      <div className="p-2 border-t border-neutral-light-gray">
                        <button
                          className="w-full text-left px-4 py-2 text-body text-semantic-error hover:bg-neutral-light-gray rounded transition-colors"
                          onClick={() => {
                            // Handle sign out
                            setIsProfileMenuOpen(false);
                          }}
                        >
                          Sign Out
                        </button>
                      </div>
                    </div>
                  </>
                )}
              </div>
            ) : (
              <Link
                href="/login"
                className="hidden md:block px-6 py-2 text-body font-semibold text-primary-dark-blue
                         border-2 border-primary-dark-blue rounded-sm
                         hover:bg-primary-dark-blue hover:text-neutral-white
                         transition-all"
              >
                Sign In
              </Link>
            )}
          </div>
        </div>

        {/* Mobile Search (Expandable) */}
        {isMobileMenuOpen && (
          <div className="lg:hidden py-4 border-t border-neutral-light-gray">
            <form onSubmit={handleSearchSubmit}>
              <div className="relative">
                <input
                  type="search"
                  placeholder="Search books..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full h-12 pl-12 pr-4 bg-neutral-white border border-neutral-light-gray
                           rounded-full text-body placeholder:text-neutral-medium-gray
                           focus:outline-none focus:border-primary-gold focus:ring-2 focus:ring-primary-gold/10"
                  autoFocus
                />
                <svg
                  className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-neutral-medium-gray"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                  />
                </svg>
              </div>
            </form>

            {!user && (
              <div className="mt-4 flex gap-2">
                <Link
                  href="/login"
                  className="flex-1 text-center px-4 py-2 text-body font-semibold
                           border-2 border-primary-dark-blue text-primary-dark-blue rounded-sm"
                >
                  Sign In
                </Link>
                <Link
                  href="/signup"
                  className="flex-1 text-center px-4 py-2 text-body font-semibold
                           bg-primary-gold text-neutral-white rounded-sm"
                >
                  Sign Up
                </Link>
              </div>
            )}
          </div>
        )}
      </nav>
    </header>
  );
};

export default Navbar;
