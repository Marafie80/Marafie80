# Navigation Flow Diagrams & 3-Click Compliance

## Primary User Flows

### Flow 1: Browse → Purchase Digital Book (3 clicks)

```
┌──────────┐
│   HOME   │
│  Screen  │
└─────┬────┘
      │ CLICK 1: Tap on Featured Book
      ▼
┌──────────┐
│   BOOK   │
│ DETAILS  │
└─────┬────┘
      │ CLICK 2: Tap "Buy Now"
      ▼
┌──────────┐
│ CHECKOUT │
│  Screen  │
└─────┬────┘
      │ CLICK 3: Tap "Complete Order"
      ▼
┌──────────┐
│  ORDER   │
│CONFIRMED │
└──────────┘

✓ COMPLIANCE: 3 clicks total
Action: Purchase featured book
Path: Home → Book Details → Checkout → Confirmation
```

---

### Flow 2: Search → Filter → Add to Cart (3 clicks)

```
┌──────────┐
│   HOME   │
└─────┬────┘
      │ CLICK 1: Tap Search Icon
      ▼
┌──────────┐
│  SEARCH  │
│  Screen  │
│ [Type]   │
└─────┬────┘
      │ CLICK 2: Tap Book from Results
      ▼
┌──────────┐
│   BOOK   │
│ DETAILS  │
└─────┬────┘
      │ CLICK 3: Tap "Add to Cart"
      ▼
┌──────────┐
│   CART   │
│ Updated  │
│ (Badge+1)│
└──────────┘

✓ COMPLIANCE: 3 clicks total
Note: Typing doesn't count as clicks
      Filters applied via bottom sheet (optional)
```

---

### Flow 3: Cart → Complete Purchase (3 clicks)

```
┌──────────┐
│   CART   │
│  Screen  │
│ (3 items)│
└─────┬────┘
      │ CLICK 1: Tap "Proceed to Checkout"
      ▼
┌──────────┐
│ CHECKOUT │
│  Fill    │
│  Form    │
└─────┬────┘
      │ CLICK 2: Select Payment Method
      ▼
┌──────────┐
│ CHECKOUT │
│  Ready   │
└─────┬────┘
      │ CLICK 3: Tap "Complete Order"
      ▼
┌──────────┐
│  ORDER   │
│CONFIRMED │
└──────────┘

✓ COMPLIANCE: 3 clicks total
Note: Form filling is typing, not clicking
      Autofill reduces friction
```

---

### Flow 4: Immediate Reading (2 clicks)

```
┌──────────┐
│   HOME   │
│          │
│ Featured │
│  Book    │
│ (owned)  │
└─────┬────┘
      │ CLICK 1: Tap Book Card
      ▼
┌──────────┐
│   BOOK   │
│ DETAILS  │
└─────┬────┘
      │ CLICK 2: Tap "Start Reading"
      ▼
┌──────────┐
│  READER  │
│Interface │
└──────────┘

✓ COMPLIANCE: 2 clicks total
Condition: User already owns the book
```

---

### Flow 5: Order Tracking (3 clicks)

```
┌──────────┐
│   HOME   │
└─────┬────┘
      │ CLICK 1: Tap Profile Icon
      ▼
┌──────────┐
│ PROFILE  │
│          │
│ [Orders] │
└─────┬────┘
      │ CLICK 2: Tap "Orders"
      ▼
┌──────────┐
│  ORDERS  │
│   LIST   │
└─────┬────┘
      │ CLICK 3: Tap Order Card
      ▼
┌──────────┐
│  ORDER   │
│ TRACKING │
└──────────┘

✓ COMPLIANCE: 3 clicks total
Alternative: Direct link from email = 1 click
```

---

### Flow 6: Contact Support (2 clicks)

```
┌──────────┐
│   HOME   │
└─────┬────┘
      │ CLICK 1: Tap Profile Icon
      ▼
┌──────────┐
│ PROFILE  │
│ OR MENU  │
└─────┬────┘
      │ CLICK 2: Tap "Support"
      ▼
┌──────────┐
│ SUPPORT  │
│   CHAT   │
└──────────┘

✓ COMPLIANCE: 2 clicks total
Note: Chat opens with AI assistant immediately ready
      Floating chat button available on most screens
```

---

### Flow 7: Browse by Category (2 clicks)

```
┌──────────┐
│   HOME   │
│          │
│Categories│
│ [Chips]  │
└─────┬────┘
      │ CLICK 1: Tap Category (e.g., "Mystery")
      ▼
┌──────────┐
│ CATEGORY │
│   LIST   │
│ (Mystery)│
└─────┬────┘
      │ CLICK 2: Tap Book Card
      ▼
┌──────────┐
│   BOOK   │
│ DETAILS  │
└──────────┘

✓ COMPLIANCE: 2 clicks total
Categories always visible on home screen
```

---

### Flow 8: Wishlist → Purchase (2 clicks)

```
┌──────────┐
│ PROFILE  │
└─────┬────┘
      │ CLICK 1: Tap "Wishlist"
      ▼
┌──────────┐
│ WISHLIST │
│   LIST   │
└─────┬────┘
      │ CLICK 2: Tap "Add to Cart" (on book card)
      ▼
┌──────────┐
│   CART   │
│ Updated  │
└──────────┘

✓ COMPLIANCE: 2 clicks total
Note: Quick action buttons on wishlist cards
```

---

### Flow 9: Free Book → Start Reading (2 clicks)

```
┌──────────┐
│   HOME   │
│          │
│   Free   │
│  Books   │
└─────┬────┘
      │ CLICK 1: Tap Free Book Card
      ▼
┌──────────┐
│   BOOK   │
│ DETAILS  │
│ (FREE)   │
└─────┬────┘
      │ CLICK 2: Tap "Get Free" or "Read Now"
      ▼
┌──────────┐
│  READER  │
│Interface │
└──────────┘

✓ COMPLIANCE: 2 clicks total
Free books auto-added to library
No checkout required
```

---

### Flow 10: Recommendation → Purchase (3 clicks)

```
┌──────────┐
│   HOME   │
│          │
│Recommend-│
│  ations  │
└─────┬────┘
      │ CLICK 1: Tap Recommended Book
      ▼
┌──────────┐
│   BOOK   │
│ DETAILS  │
└─────┬────┘
      │ CLICK 2: Tap "Add to Cart"
      ▼
┌──────────┐
│   CART   │
│ Updated  │
└─────┬────┘
      │ CLICK 3: Tap "Checkout"
      ▼
┌──────────┐
│ CHECKOUT │
└──────────┘

✓ COMPLIANCE: 3 clicks total
AI-powered personalized recommendations
```

---

## Complete Site Map

```
                        ┌──────────────┐
                        │  SPLASH      │
                        │  (2-3 sec)   │
                        └──────┬───────┘
                               │
                        ┌──────▼───────┐
                    ┌───│  WELCOME     │◄───┐
                    │   │  (First time)│    │
                    │   └──────┬───────┘    │
                    │          │            │
            ┌───────▼──┐   ┌──▼────────┐   │
            │  LOGIN   │   │  SIGNUP   │   │
            └───────┬──┘   └──┬────────┘   │
                    │         │            │
                    └────┬────┘            │
                         │                 │
                    ┌────▼────────┐        │
                    │    HOME     │◄───────┘
                    │   (Feed)    │
                    └──┬──┬──┬───┘
                       │  │  │
        ┌──────────────┘  │  └──────────────┐
        │                 │                 │
   ┌────▼─────┐     ┌────▼────┐      ┌────▼────┐
   │  SEARCH  │     │  CART   │      │ PROFILE │
   │ & FILTER │     │         │      │         │
   └────┬─────┘     └────┬────┘      └────┬────┘
        │                │                 │
   ┌────▼─────┐     ┌────▼────┐      ┌────▼────┐
   │   BOOK   │     │CHECKOUT │      │ ORDERS  │
   │ DETAILS  │     │         │      │  LIST   │
   └────┬─────┘     └────┬────┘      └────┬────┘
        │                │                 │
   ┌────▼─────┐     ┌────▼────┐      ┌────▼────┐
   │  READER  │     │ ORDER   │      │ ORDER   │
   │(Digital) │     │CONFIRM  │      │TRACKING │
   └──────────┘     └────┬────┘      └─────────┘
                         │
                    ┌────▼────┐
                    │  ORDER  │
                    │TRACKING │
                    └─────────┘

Side Panels (Accessible from multiple screens):
┌────────────┐
│  SUPPORT   │ ← Available via: Floating button, Profile menu, Order screens
│   CHAT     │
└────────────┘

┌────────────┐
│  WISHLIST  │ ← Available via: Profile, Book Details (Add), Bottom Nav
└────────────┘

┌────────────┐
│  LIBRARY   │ ← Available via: Profile, Confirmation (Direct access)
└────────────┘
```

---

## Navigation Depth Analysis

### Maximum Click Depths from Home

```
┌─────────────────────────────────────────┬───────────────┐
│ Destination                             │ Clicks        │
├─────────────────────────────────────────┼───────────────┤
│ Book Details (Featured)                 │ 1 click       │
│ Book Details (Category)                 │ 2 clicks      │
│ Book Details (Search)                   │ 2 clicks      │
│ Start Reading (Owned)                   │ 2 clicks      │
│ Start Reading (Free)                    │ 2 clicks      │
│ Add to Cart                             │ 2 clicks      │
│ Cart View                               │ 1 click       │
│ Checkout                                │ 2 clicks      │
│ Complete Purchase                       │ 3 clicks      │
│ Profile                                 │ 1 click       │
│ Order History                           │ 2 clicks      │
│ Order Tracking                          │ 3 clicks      │
│ Support Chat                            │ 2 clicks      │
│ Wishlist                                │ 2 clicks      │
│ Library                                 │ 2 clicks      │
│ Settings                                │ 2 clicks      │
│ Search Results                          │ 1 click       │
│ Filtered Results                        │ 2 clicks      │
└─────────────────────────────────────────┴───────────────┘

✓ MAXIMUM DEPTH: 3 clicks for any critical action
✓ AVERAGE DEPTH: 1.8 clicks
✓ MEDIAN DEPTH: 2 clicks
```

---

## Click Reduction Strategies

### 1. Smart Defaults
```
- Auto-detect location for shipping
- Remember payment method (saved cards)
- Pre-fill user information
- Default to preferred format (Digital/Print)
- Auto-apply eligible promotions
```

### 2. Quick Actions
```
Book Cards Include:
- Direct "Add to Cart" button
- Quick "Buy Now" (1-click purchase for returning users)
- Instant "Wishlist" toggle
- Format selector (without opening details)

Cart Items Include:
- Inline quantity adjustment
- Quick delete (swipe gesture)
- Move to wishlist
```

### 3. Contextual Navigation
```
- "Buy Again" on past orders (1 click to cart)
- "Continue Reading" prominent on home (1 click to reader)
- Recent searches saved (1 click to re-run)
- Suggested next purchase based on current cart
```

### 4. Persistent Access
```
Floating Elements:
- Cart badge (always visible)
- Support chat button (most screens)
- Quick search (sticky header)
- Back navigation (iOS swipe, Android back)

Bottom Nav (Mobile):
- Home
- Search
- Cart
- Profile
```

### 5. Deep Linking
```
Email/Push Notifications Direct Links:
- Order confirmation → Track order (1 click)
- Recommendation → Book details (1 click)
- Price drop alert → Book details (1 click)
- Delivery update → Tracking page (1 click)
- Support response → Chat thread (1 click)
```

---

## Information Architecture

### Primary Navigation (Desktop)

```
┌────────────────────────────────────────────────────────┐
│ 📚 BookMart    [Search Bar]         [Cart] [Profile]  │
└────────────────────────────────────────────────────────┘
                                            │
                                            └──→ Profile Menu:
                                                  - My Account
                                                  - Orders
                                                  - Library
                                                  - Wishlist
                                                  - Settings
                                                  - Support
                                                  - Sign Out

Home Page Structure:
├─ Hero (Featured Book of Month)
├─ Recommended For You (AI)
├─ Free Books
├─ Book of the Year
├─ New Releases
├─ Browse by Category
└─ Publishers Spotlight
```

### Primary Navigation (Mobile)

```
Bottom Navigation Bar:
┌────────────────────────────────────────┐
│  [🏠]     [🔍]     [🛒]      [👤]      │
│  Home    Search   Cart      Profile   │
└────────────────────────────────────────┘

Hamburger Menu (Profile):
├─ Profile
├─ Orders
├─ Library
├─ Wishlist
├─ Addresses
├─ Payment Methods
├─ Settings
├─ Support
└─ Sign Out
```

---

## State Management & Navigation

### Persistent State

```
Maintained Across Sessions:
- Cart contents
- Wishlist items
- Search history (last 10)
- Reading position (per book)
- Filter preferences
- Sort preferences
- Notification settings
- Payment methods (tokenized)
- Shipping addresses

Cleared on Sign Out:
- Authentication token
- Personal data cache
- Current session cart (optional: save server-side)
```

### Navigation History

```
Browser Back Button Behavior:
- Respects standard navigation
- Search filters preserved
- Scroll position maintained
- Form data retained (where appropriate)

Deep Links:
- /book/:isbn
- /order/:orderId
- /category/:categorySlug
- /search?q=:query
- /profile/orders
- /profile/library
- /cart
- /checkout
```

---

## Modal & Overlay Patterns

### Modals (Interrupting)
```
Used For:
- Login/Signup (when not signed in)
- Payment processing
- Confirmation dialogs
- Error messages (critical)

Design:
- Center of screen
- Backdrop dimmed (70% opacity)
- Close button (always accessible)
- ESC key closes (desktop)
- Click outside closes (non-critical only)
```

### Bottom Sheets (Mobile)
```
Used For:
- Filters
- Sort options
- Format selection
- Quick actions
- Share sheet

Design:
- Slide from bottom
- Handle to drag
- Swipe down to dismiss
- Partial height (70% max)
- Content scrollable
```

### Drawer (Side Panel)
```
Used For:
- Support chat
- Profile menu (mobile)
- Filter panel (desktop)

Design:
- Slide from side (left or right)
- Overlay content
- Backdrop dismissible
- Can be persistent (desktop filters)
```

### Toast Notifications
```
Used For:
- Add to cart confirmation
- Wishlist updates
- Success messages
- Non-critical errors
- Undo actions

Design:
- Bottom of screen (mobile)
- Top-right (desktop)
- Auto-dismiss (3-5 seconds)
- Action button optional (Undo, View)
- Stack multiple (max 3)
```

---

## Error States & Recovery

### Network Errors
```
┌─────────────────────┐
│   😞 Connection     │
│      Lost           │
│                     │
│ Please check your   │
│ internet connection │
│                     │
│ [Try Again]         │
│                     │
│ Cached data still   │
│ available below     │
└─────────────────────┘

Recovery:
- Auto-retry (exponential backoff)
- Show cached content when available
- Queue actions for when online
- Clear indicator of offline state
```

### Empty States
```
Cart Empty:
┌─────────────────────┐
│   🛒                │
│   Your cart is      │
│   empty             │
│                     │
│   [Browse Books]    │
│                     │
│   Recommended:      │
│   [Book][Book][Book]│
└─────────────────────┘

Search No Results:
┌─────────────────────┐
│   🔍                │
│   No results for    │
│   "xyz"             │
│                     │
│   Suggestions:      │
│   - Check spelling  │
│   - Try different   │
│     keywords        │
│   - Browse category │
│                     │
│   [Clear Search]    │
│   [Browse All]      │
└─────────────────────┘
```

### Payment Failures
```
┌─────────────────────┐
│   ⚠️ Payment Failed  │
│                     │
│   Your card was     │
│   declined          │
│                     │
│   [Try Another Card]│
│   [Contact Support] │
│                     │
│   Order saved:      │
│   #BM-2025-DRAFT    │
└─────────────────────┘

Recovery:
- Save order as draft
- Email draft link
- 24-hour hold on cart
- Support chat pre-populated
```

---

## Loading States

### Initial Page Load
```
┌─────────────────────┐
│                     │
│  [Skeleton Screen]  │
│  ▓▓▓▓▓▓▓░░░░░       │
│  ▓▓▓▓░░░            │
│  ▓▓▓▓▓▓░░░░░        │
│  ▓▓▓░░░             │
│                     │
└─────────────────────┘

- Content layout visible
- Shimmer animation
- Progressive enhancement
- Perceived performance boost
```

### Search Loading
```
┌─────────────────────┐
│ [Search: "mystery"] │
│                     │
│  Searching...       │
│  [Spinner]          │
│                     │
│  Or showing cached: │
│  [Previous Results] │
└─────────────────────┘

- Instant feedback
- Show cached if available
- Debounced search (300ms)
- Cancel previous requests
```

### Checkout Processing
```
┌─────────────────────┐
│                     │
│  Processing Payment │
│  [Progress Circle]  │
│                     │
│  Please wait...     │
│  Do not close       │
│                     │
│  🔒 Secure          │
└─────────────────────┘

- Full-screen blocking
- Progress indicator
- Security badges
- Prevent duplicate submission
- Timeout after 30s → support
```

---

## Keyboard Navigation (Desktop)

```
Global Shortcuts:
- / or Ctrl+K : Focus search
- Ctrl+B : View cart
- Ctrl+P : Profile
- Escape : Close modal/drawer
- ? : Show keyboard shortcuts

Navigation:
- Tab : Next interactive element
- Shift+Tab : Previous element
- Enter : Activate button/link
- Space : Toggle checkbox/radio
- Arrow Keys : Navigate lists/carousels

Book Details:
- W : Add to wishlist
- C : Add to cart
- B : Buy now
- R : Start reading (if owned)
- S : Share

Reader:
- Left/Right Arrows : Previous/Next page
- Space : Next page
- Shift+Space : Previous page
- Home/End : First/Last page
- Ctrl+Plus/Minus : Font size
```

---

## Accessibility Navigation

### Screen Reader Landmarks
```
<header role="banner">
  <nav role="navigation" aria-label="Main">
  <form role="search">
</header>

<main role="main">
  <section aria-labelledby="featured">
  <section aria-labelledby="recommendations">
</main>

<footer role="contentinfo">

<aside role="complementary"> (Support Chat)
```

### Skip Links
```
[Skip to main content]
[Skip to navigation]
[Skip to search]
[Skip to footer]

- Visible on focus
- First tab stop
- Jump to main content
- Bypass repetitive elements
```

### ARIA Live Regions
```
<div role="status" aria-live="polite">
  Item added to cart
</div>

<div role="alert" aria-live="assertive">
  Payment failed
</div>

- Announce state changes
- Cart updates
- Form validation
- Search results count
```

---

## Performance Optimization

### Code Splitting
```
Routes:
- Home (initial bundle)
- Search (lazy)
- BookDetails (lazy)
- Cart (lazy)
- Checkout (lazy)
- Profile (lazy)
- Reader (lazy, separate chunk)

Components:
- Modal (lazy)
- Chat (lazy)
- VideoPlayer (lazy)
- PDFViewer (lazy)
```

### Prefetching
```
On Hover (Desktop):
- Book details page
- Next page in list
- Related books

On Viewport (Mobile):
- Next section content
- Images below fold
- Carousel items

On Intent:
- Checkout page (when viewing cart)
- Payment scripts (when on checkout)
- Reader (when owned book is viewed)
```

### Caching Strategy
```
Service Worker:
- Cache-first: Static assets
- Network-first: API calls
- Stale-while-revalidate: Book covers
- Cache-only: Offline page

CDN:
- Book covers: Aggressive caching (1 year)
- Static JS/CSS: Hash-based versioning
- API: Short TTL (5 minutes)
```

---
