'use client';

import React from 'react';
import { Navbar } from '@/components/molecules';
import { BookCard } from '@/components/molecules';
import { Button } from '@/components/atoms';
import { Book } from '@/types';

// Mock data
const mockBooks: Book[] = [
  {
    id: '1',
    isbn: '978-1234567890',
    title: 'The Midnight Garden',
    author: 'Sarah Williams',
    coverUrl: '/images/books/midnight-garden.jpg',
    rating: 4.9,
    reviewCount: 3421,
    price: 24.99,
    currency: 'USD',
    format: 'digital',
    category: 'Fiction',
    subcategory: 'Mystery',
    publisher: 'HarperCollins',
    publishedDate: '2025-03-01',
    pages: 384,
    language: 'English',
    description: 'A mesmerizing tale of love, loss, and mystery set in a forgotten English garden.',
    badge: 'featured',
  },
  {
    id: '2',
    isbn: '978-1234567891',
    title: 'Ocean\'s Whisper',
    author: 'James Peterson',
    coverUrl: '/images/books/oceans-whisper.jpg',
    rating: 4.7,
    reviewCount: 1289,
    price: 19.99,
    currency: 'USD',
    format: 'paperback',
    category: 'Fiction',
    subcategory: 'Adventure',
    publisher: 'Penguin Random House',
    publishedDate: '2024-11-15',
    pages: 312,
    language: 'English',
    description: 'An epic adventure across the seven seas.',
  },
  {
    id: '3',
    isbn: '978-1234567892',
    title: 'Digital Dreams',
    author: 'Lisa Chen',
    coverUrl: '/images/books/digital-dreams.jpg',
    rating: 4.8,
    reviewCount: 2156,
    price: 19.99,
    currency: 'USD',
    format: 'audiobook',
    category: 'Science Fiction',
    publisher: 'Tor Books',
    publishedDate: '2025-01-10',
    pages: 456,
    language: 'English',
    description: 'A journey through virtual reality and the future of humanity.',
    badge: 'bestseller',
  },
  {
    id: '4',
    isbn: '978-1234567893',
    title: 'The Last Symphony',
    author: 'Michael Brown',
    coverUrl: '/images/books/last-symphony.jpg',
    rating: 4.6,
    reviewCount: 987,
    price: 22.99,
    currency: 'USD',
    format: 'digital',
    category: 'Fiction',
    subcategory: 'Historical',
    publisher: 'Simon & Schuster',
    publishedDate: '2024-09-20',
    pages: 428,
    language: 'English',
    description: 'A historical fiction set in Vienna during the golden age of classical music.',
  },
  {
    id: '5',
    isbn: '978-1234567894',
    title: 'Starlight Chronicles',
    author: 'Emma Davis',
    coverUrl: '/images/books/starlight-chronicles.jpg',
    rating: 4.9,
    reviewCount: 4521,
    price: 0,
    currency: 'USD',
    format: 'digital',
    category: 'Fantasy',
    publisher: 'Orbit Books',
    publishedDate: '2024-06-01',
    pages: 512,
    language: 'English',
    description: 'An epic fantasy saga spanning multiple worlds.',
    isFree: true,
    badge: 'new',
  },
];

export default function HomePage() {
  const handleAddToCart = (book: Book) => {
    console.log('Add to cart:', book.title);
    // TODO: Implement cart functionality
  };

  const handleWishlist = (book: Book) => {
    console.log('Add to wishlist:', book.title);
    // TODO: Implement wishlist functionality
  };

  const handleBookClick = (book: Book) => {
    console.log('View book:', book.title);
    // TODO: Navigate to book details
  };

  return (
    <div className="min-h-screen">
      <Navbar
        cartItemCount={3}
        user={{
          id: '1',
          name: 'John Doe',
          email: 'john@example.com',
          readerLevel: 'avid',
          memberSince: '2024-03-01',
          booksRead: 24,
          reviewCount: 12,
          wishlistCount: 8,
        }}
      />

      <main className="container-custom py-8 space-y-12">
        {/* Hero Section - Featured Book */}
        <section className="bg-primary-yellow rounded-xl p-8 md:p-12">
          <div className="grid md:grid-cols-2 gap-8 items-center">
            <div className="order-2 md:order-1 space-y-4">
              <div className="inline-block px-4 py-1 bg-primary-gold text-neutral-white rounded-full">
                <span className="text-body-sm font-semibold">Book of the Month</span>
              </div>
              <h1 className="text-hero font-bold text-primary-dark-blue">
                {mockBooks[0].title}
              </h1>
              <p className="text-body-lg text-neutral-dark-gray">
                by {mockBooks[0].author}
              </p>
              <p className="text-body text-neutral-dark-gray">
                ⭐ {mockBooks[0].rating} ({mockBooks[0].reviewCount.toLocaleString()} reviews)
              </p>
              <p className="text-body text-neutral-dark-gray">
                {mockBooks[0].description}
              </p>
              <div className="flex gap-4 pt-4">
                <Button
                  variant="primary"
                  size="lg"
                  onClick={() => handleAddToCart(mockBooks[0])}
                >
                  Add to Cart
                </Button>
                <Button variant="secondary" size="lg">
                  Buy Now - ${mockBooks[0].price}
                </Button>
              </div>
            </div>

            <div className="order-1 md:order-2">
              <div className="relative aspect-[3/4] max-w-sm mx-auto">
                <div className="absolute inset-0 bg-primary-beige rounded-lg transform rotate-6"></div>
                <div className="relative bg-neutral-white rounded-lg shadow-xl p-4">
                  <div className="aspect-[3/4] bg-gradient-to-br from-primary-blue-gray to-primary-dark-blue rounded"></div>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Recommended For You */}
        <section>
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-h2 font-bold text-primary-dark-blue">
              📖 Recommended For You
            </h2>
            <Button variant="ghost">View All →</Button>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {mockBooks.slice(0, 5).map((book) => (
              <BookCard
                key={book.id}
                book={book}
                variant="standard"
                onAddToCart={() => handleAddToCart(book)}
                onWishlist={() => handleWishlist(book)}
                onClick={() => handleBookClick(book)}
              />
            ))}
          </div>
        </section>

        {/* Free Books */}
        <section>
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-h2 font-bold text-primary-dark-blue">
              🎁 Free Books
            </h2>
            <Button variant="ghost">View All →</Button>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {[mockBooks[4], ...mockBooks.slice(0, 4)].map((book) => (
              <BookCard
                key={book.id}
                book={book}
                variant="standard"
                onAddToCart={() => handleAddToCart(book)}
                onWishlist={() => handleWishlist(book)}
                onClick={() => handleBookClick(book)}
              />
            ))}
          </div>
        </section>

        {/* Book of the Year */}
        <section>
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-h2 font-bold text-primary-dark-blue">
              🏆 Book of the Year 2025
            </h2>
            <Button variant="ghost">View All →</Button>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {mockBooks.map((book) => (
              <BookCard
                key={book.id}
                book={book}
                variant="standard"
                onAddToCart={() => handleAddToCart(book)}
                onWishlist={() => handleWishlist(book)}
                onClick={() => handleBookClick(book)}
              />
            ))}
          </div>
        </section>

        {/* Browse by Category */}
        <section>
          <h2 className="text-h2 font-bold text-primary-dark-blue mb-6">
            📚 Browse by Category
          </h2>

          <div className="flex flex-wrap gap-3">
            {['Fiction', 'Non-Fiction', 'Mystery', 'Romance', 'Sci-Fi', 'Fantasy', 'Biography', 'Self-Help'].map(
              (category) => (
                <button
                  key={category}
                  className="px-6 py-3 bg-neutral-white border-2 border-neutral-light-gray
                           text-primary-dark-blue font-semibold rounded-lg
                           hover:border-primary-gold hover:text-primary-gold
                           transition-all"
                >
                  {category}
                </button>
              )
            )}
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="bg-primary-dark-blue text-neutral-white mt-16">
        <div className="container-custom py-12">
          <div className="grid md:grid-cols-4 gap-8">
            <div>
              <h3 className="text-h3 font-bold mb-4">📚 BookMart</h3>
              <p className="text-body-sm opacity-80">
                Your gateway to endless stories. Discover thousands of books from multiple publishers.
              </p>
            </div>

            <div>
              <h4 className="text-h4 font-semibold mb-4">Shop</h4>
              <ul className="space-y-2 text-body-sm opacity-80">
                <li><a href="#" className="hover:opacity-100">All Books</a></li>
                <li><a href="#" className="hover:opacity-100">New Releases</a></li>
                <li><a href="#" className="hover:opacity-100">Bestsellers</a></li>
                <li><a href="#" className="hover:opacity-100">Free Books</a></li>
              </ul>
            </div>

            <div>
              <h4 className="text-h4 font-semibold mb-4">Account</h4>
              <ul className="space-y-2 text-body-sm opacity-80">
                <li><a href="#" className="hover:opacity-100">My Library</a></li>
                <li><a href="#" className="hover:opacity-100">Orders</a></li>
                <li><a href="#" className="hover:opacity-100">Wishlist</a></li>
                <li><a href="#" className="hover:opacity-100">Settings</a></li>
              </ul>
            </div>

            <div>
              <h4 className="text-h4 font-semibold mb-4">Support</h4>
              <ul className="space-y-2 text-body-sm opacity-80">
                <li><a href="#" className="hover:opacity-100">Help Center</a></li>
                <li><a href="#" className="hover:opacity-100">Contact Us</a></li>
                <li><a href="#" className="hover:opacity-100">Shipping Info</a></li>
                <li><a href="#" className="hover:opacity-100">Returns</a></li>
              </ul>
            </div>
          </div>

          <div className="border-t border-neutral-white/20 mt-8 pt-8 text-center text-body-sm opacity-80">
            <p>&copy; 2026 BookMart. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
