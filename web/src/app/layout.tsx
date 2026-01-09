import type { Metadata } from 'next';
import '@/styles/globals.css';

export const metadata: Metadata = {
  title: 'BookMart - Your Gateway to Endless Stories',
  description: 'Discover thousands of books from multiple publishers. AI-powered recommendations, read anywhere.',
  keywords: ['books', 'ebooks', 'reading', 'bookstore', 'digital books', 'audiobooks'],
  authors: [{ name: 'BookMart' }],
  creator: 'BookMart',
  publisher: 'BookMart',
  icons: {
    icon: '/favicon.ico',
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <head>
        <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>📚</text></svg>" />
      </head>
      <body className="min-h-screen bg-primary-beige">
        {children}
      </body>
    </html>
  );
}
