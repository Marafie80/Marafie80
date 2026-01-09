# BookMart Web Application

React + Next.js 14 web application for the BookMart digital book marketplace.

## 🚀 Tech Stack

- **Framework:** Next.js 14 (App Router)
- **Language:** TypeScript
- **Styling:** Tailwind CSS
- **Animations:** Framer Motion
- **State Management:** Zustand
- **HTTP Client:** Axios

## 📁 Project Structure

```
web/
├── src/
│   ├── app/                 # Next.js app router pages
│   │   ├── layout.tsx       # Root layout
│   │   └── page.tsx         # Home page
│   ├── components/
│   │   ├── atoms/           # Base components (Button, Input, etc.)
│   │   └── molecules/       # Composite components (BookCard, Navbar, etc.)
│   ├── styles/
│   │   └── globals.css      # Global styles + Tailwind
│   ├── types/
│   │   └── index.ts         # TypeScript type definitions
│   ├── lib/                 # Utilities and helpers
│   ├── hooks/               # Custom React hooks
│   └── store/               # Zustand stores
├── public/                  # Static assets
├── tailwind.config.ts       # Tailwind configuration
├── tsconfig.json            # TypeScript configuration
├── next.config.js           # Next.js configuration
└── package.json             # Dependencies
```

## 🎨 Design System

### Colors

- **Primary Beige:** `#CAC6B1` - Background/Cards
- **Dark Blue:** `#1C3F68` - Text Primary
- **Blue Gray:** `#647CA4` - Secondary Background
- **Warm Gold:** `#AF924A` - Accent/CTA Buttons
- **Light Yellow:** `#EEDEAA` - Highlight

### Typography

- **Primary:** Cairo (Arabic + Latin support)
- **Secondary:** Montserrat (Headings)
- **Body:** Lato (Reading optimized)

### Components

#### Atoms
- `Button` - Primary, secondary, ghost variants
- `Input` - Text inputs with validation states
- `Badge` - Notification badges
- `Rating` - Star rating display/input
- `Avatar` - User avatars with initials fallback
- `Skeleton` - Loading placeholders

#### Molecules
- `BookCard` - Book display cards (4 variants)
- `Navbar` - Main navigation with search
- `FilterPanel` - Sidebar filters for search

## 🛠️ Installation

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Type check
npm run type-check

# Lint
npm run lint
```

## 🌐 Development

The development server runs on [http://localhost:3000](http://localhost:3000).

## 📱 Responsive Design

Breakpoints:
- **Mobile:** 0px - 640px
- **Tablet:** 640px - 1024px
- **Desktop:** 1024px - 1440px
- **Large Desktop:** 1440px+

## ♿ Accessibility

- WCAG 2.1 AA compliant
- Full keyboard navigation support
- Screen reader optimized
- Semantic HTML
- ARIA labels where needed

## 🔧 Environment Variables

Create a `.env.local` file:

```env
NEXT_PUBLIC_API_URL=http://localhost:8000
NEXT_PUBLIC_SITE_URL=http://localhost:3000
```

## 📦 Key Features Implemented

✅ Design system with Tailwind CSS
✅ Component library (atoms + molecules)
✅ Responsive navigation bar
✅ Book card components (4 variants)
✅ Filter panel for search
✅ Home page with featured books
✅ TypeScript type safety
✅ Accessibility features

## 🚧 To Be Implemented

- [ ] Search functionality
- [ ] Book details page
- [ ] Shopping cart
- [ ] Checkout flow
- [ ] User authentication
- [ ] Profile pages
- [ ] Order tracking
- [ ] AI chat support
- [ ] Payment integration
- [ ] Backend API integration

## 📝 Notes

- Uses Next.js 14 App Router
- Optimized for performance (< 2s TTI)
- Mobile-first responsive design
- Dark mode support ready
- Internationalization ready (RTL support)

## 🤝 Contributing

This is part of the full-stack BookMart platform. See main README for overall architecture.

---

**Version:** 1.0.0
**Last Updated:** January 2026
**Status:** In Development (Step 2)
