# Component Library Specifications

## Atoms (Basic Building Blocks)

### Button Component

```typescript
interface ButtonProps {
  variant: 'primary' | 'secondary' | 'ghost' | 'danger' | 'success';
  size: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  loading?: boolean;
  icon?: ReactNode;
  iconPosition?: 'left' | 'right';
  fullWidth?: boolean;
  onClick?: () => void;
}
```

#### Variants

**Primary Button (CTA)**
```
┌─────────────────┐
│  Add to Cart    │  Background: #AF924A (Warm Gold)
└─────────────────┘  Text: #FFFFFF
                     Hover: #9A7D3F
                     Active: #85682F
                     Disabled: #E8E5DC (50% opacity)

Height: 48px (desktop), 52px (mobile)
Padding: 12px 24px
Border Radius: 4px
Font: Montserrat SemiBold 16px
Transition: 200ms ease
Shadow: None (default), md (hover)
```

**Secondary Button**
```
┌─────────────────┐
│  Learn More     │  Background: Transparent
└─────────────────┘  Border: 2px solid #1C3F68
                     Text: #1C3F68
                     Hover: Background #1C3F68, Text #FFFFFF
                     Active: Background #152F50

Height: 48px (desktop), 52px (mobile)
Padding: 12px 24px
Border Radius: 4px
```

**Ghost Button**
```
 Cancel             Background: Transparent
                    Text: #647CA4
                    Hover: Background #E8E5DC
                    Active: Background #CAC6B1

Height: 40px
Padding: 8px 16px
No border
```

**Loading State**
```
┌─────────────────┐
│  [●●●] Processing│  Spinner: Left or center
└─────────────────┘  Disabled during loading
                     Text: Optional loading message
                     Spinner color: Matches text
```

---

### Input Field Component

```typescript
interface InputProps {
  type: 'text' | 'email' | 'password' | 'number' | 'tel' | 'url';
  label: string;
  placeholder?: string;
  value: string;
  onChange: (value: string) => void;
  error?: string;
  helperText?: string;
  required?: boolean;
  disabled?: boolean;
  icon?: ReactNode;
  iconPosition?: 'left' | 'right';
  autoComplete?: string;
}
```

#### States

**Default**
```
Label
┌─────────────────────────────┐
│ Enter email address         │
└─────────────────────────────┘

Background: #FFFFFF
Border: 1px solid #E8E5DC
Height: 48px
Padding: 12px 16px
Border Radius: 4px
Font: Lato Regular 16px
Color: #1C3F68
```

**Focus**
```
Label
┌═════════════════════════════┐
│ user@example.com█           │
└═════════════════════════════┘

Border: 2px solid #AF924A
Shadow: 0 0 0 3px rgba(175,146,74,0.1)
Outline: None
```

**Error**
```
Label
┌─────────────────────────────┐
│ invalid@                    │
└─────────────────────────────┘
⚠️ Please enter a valid email

Border: 2px solid #C85C5C
Error Text: #C85C5C, 12px
Icon: Error icon in red
```

**Success**
```
Label
┌─────────────────────────────┐
│ user@example.com         ✓  │
└─────────────────────────────┘

Border: 1px solid #6B8E6B
Icon: Checkmark in green
```

**Disabled**
```
Label
┌─────────────────────────────┐
│ Cannot edit                 │
└─────────────────────────────┘

Background: #F5F4F0
Border: 1px solid #E8E5DC
Color: #9B9B9B
Cursor: not-allowed
```

---

### Icon Button Component

```
Mobile Optimized:
┌────┐
│ 🔍 │  Size: 44x44px minimum (touch target)
└────┘  Background: Transparent or #E8E5DC
        Icon: 24x24px
        Border Radius: 50% (circle) or 4px (square)
        Hover: Background #CAC6B1
        Active: Background #AF924A, Icon #FFFFFF
```

---

### Badge Component

```
Cart Badge:
[🛒 3]

Background: #C85C5C (Red)
Text: #FFFFFF
Size: 20x20px
Border Radius: 50%
Font: 12px Bold
Position: Absolute top-right of icon
Border: 2px solid background color
```

---

### Chip Component

```
Category Chips:
┌──────────┐ ┌──────────┐
│ Fiction  │ │ Mystery×│
└──────────┘ └──────────┘

Default State:
- Background: #E8E5DC
- Text: #1C3F68
- Border Radius: 16px
- Padding: 6px 12px
- Font: 14px Medium

Selected State:
- Background: #AF924A
- Text: #FFFFFF

Removable:
- × icon on right
- Hover: Background #C85C5C
```

---

### Rating Component

```
Star Rating (Read-only):
⭐⭐⭐⭐⭐ 4.8

Implementation:
- Full stars: #FFB800
- Half stars: Gradient
- Empty stars: #E8E5DC
- Size: 16px (standard), 20px (large)
- Spacing: 2px between stars

Interactive (Reviews):
☆☆☆☆☆ → Tap to rate
⭐⭐⭐☆☆ → 3 stars selected

Hover: Fill stars on hover
Click: Set rating
Keyboard: Arrow keys to adjust
```

---

### Avatar Component

```
User Avatar:
┌──────┐
│  JD  │  Size: 40px (default), 32px (sm), 56px (lg)
└──────┘  Border Radius: 50%
          Background: #647CA4
          Text: Initials, #FFFFFF, 16px

With Image:
┌──────┐
│[IMG] │  Border: 2px solid #FFFFFF
└──────┘  Shadow: sm
          Fallback: Initials if image fails
```

---

### Skeleton Loader Component

```
Loading Placeholder:
▓▓▓▓▓▓▓▓▓░░░░░░

Color: #E8E5DC
Animation: Shimmer effect (1.5s loop)
Gradient: Linear from #E8E5DC to #F5F4F0
Border Radius: Matches content shape

Book Card Skeleton:
┌─────────────┐
│             │
│   ▓▓▓▓▓▓    │ ← Cover
│   ▓▓▓▓▓▓    │
│             │
├─────────────┤
│ ▓▓▓▓▓▓▓     │ ← Title
│ ▓▓▓▓        │ ← Author
│ ▓▓▓ ▓▓▓     │ ← Rating + Price
└─────────────┘
```

---

## Molecules (Composite Components)

### BookCard Component

```typescript
interface BookCardProps {
  book: {
    id: string;
    title: string;
    author: string;
    coverUrl: string;
    rating: number;
    reviewCount: number;
    price: number;
    currency: string;
    format: 'digital' | 'paperback' | 'hardcover' | 'audiobook';
    isFree?: boolean;
    isOwned?: boolean;
    badge?: 'new' | 'bestseller' | 'featured';
  };
  variant: 'standard' | 'featured' | 'compact' | 'list';
  onAddToCart?: () => void;
  onWishlist?: () => void;
  onClick?: () => void;
}
```

#### Standard Book Card (220px × 380px)

```
┌──────────────────────┐
│                      │
│                      │
│     Book Cover       │  Image: 3:4 ratio
│     [Image]          │  Lazy load
│     220x293px        │  Placeholder: Beige
│                      │
│                      │
├──────────────────────┤
│ The Midnight Garden  │  Title: H4, 2 lines max
│ by Sarah Williams    │  Author: Body Small, 1 line
│ ⭐ 4.9 (3,421)       │  Rating: Inline, Caption
│ $24.99               │  Price: H4, Accent color
│                      │
│ [+ Cart]    [♡]      │  Actions: Icon buttons
└──────────────────────┘

Hover Effect (Desktop):
- Elevation: Shadow lg
- Transform: translateY(-4px)
- Transition: 200ms ease
- Show quick action buttons

Badge Overlay (if applicable):
┌──────────────────────┐
│ [NEW]                │ ← Top-right corner
│                      │   Background: #AF924A
│     Book Cover       │   Text: #FFFFFF
│                      │   Padding: 4px 8px
```

#### Featured Book Card (320px × 480px)

```
┌──────────────────────────┐
│                          │
│                          │
│     Book Cover           │
│     [Large Image]        │
│     320x427px            │
│                          │
│                          │
├──────────────────────────┤
│ Book of the Month        │ ← Badge/Label
│ The Midnight Garden      │ ← Title: H2
│ by Sarah Williams        │ ← Author: Body Large
│ ⭐⭐⭐⭐⭐ 4.9          │ ← Rating: Visual stars
│ (3,421 reviews)          │
│                          │
│ A mesmerizing tale of... │ ← Excerpt: Body, 2 lines
│                          │
│ [Add to Cart] [Buy Now]  │ ← Prominent CTAs
└──────────────────────────┘

Desktop Layout:
- Horizontal split option
- Cover left (40%), Details right (60%)
- More space for description
```

#### Compact Book Card (160px × 280px Mobile)

```
┌──────────────┐
│              │
│ Book Cover   │
│ [Image]      │
│ 160x213px    │
│              │
├──────────────┤
│ Title        │ ← 1-2 lines
│ Author       │ ← 1 line
│ ⭐ 4.9       │ ← No review count
│ $24.99       │
└──────────────┘

Minimal design for mobile grids
Quick tap to details
```

#### List View Card (Full Width × 120px)

```
┌─────┬────────────────────────────────────────┐
│     │ The Midnight Garden                    │
│Covr │ by Sarah Williams                      │
│80px │ ⭐ 4.9 (3,421) · Fiction > Mystery     │
│     │ Digital · 384 pages                    │
│     │ $24.99            [+ Cart] [♡] [View] │
└─────┴────────────────────────────────────────┘

Horizontal layout for list views
More metadata visible
Quick actions on right
```

---

### SearchBar Component

```typescript
interface SearchBarProps {
  placeholder?: string;
  onSearch: (query: string) => void;
  onClear?: () => void;
  autoFocus?: boolean;
  suggestions?: string[];
  recentSearches?: string[];
}
```

#### Desktop Search Bar

```
┌────────────────────────────────────────────┐
│ 🔍 Search books, authors, categories...    │
└────────────────────────────────────────────┘

Width: 100% (max 600px)
Height: 48px
Background: #FFFFFF
Border: 1px solid #E8E5DC
Border Radius: 24px (pill)
Padding: 12px 20px 12px 48px
Icon: Absolute left, 16px from edge

Focus State:
┌════════════════════════════════════════════┐
│ 🔍 mystery█                             [×]│
└════════════════════════════════════════════┘
  │
  └─▼
   ┌────────────────────────────────────────┐
   │ Suggestions:                           │
   │ → Mystery Books                        │
   │ → Mystery & Thriller                   │
   │ → The Mysterious Garden                │
   │                                        │
   │ Recent Searches:                       │
   │ → science fiction                      │
   │ → best sellers 2025                    │
   └────────────────────────────────────────┘

Dropdown:
- Position: Absolute, below input
- Width: Match input width
- Max height: 400px, scrollable
- Background: #FFFFFF
- Shadow: lg
- Border Radius: 8px
- Keyboard navigation: Arrow keys
```

#### Mobile Search Bar

```
┌─────────────────────────────┐
│ [←] 🔍 Search books...   [×]│
└─────────────────────────────┘

Full width
Height: 56px (larger touch target)
Back button: Navigate back
Clear button: Only when text present
Auto-focus on mount
Keyboard: Search button triggers search
```

---

### FilterPanel Component

```typescript
interface FilterPanelProps {
  filters: FilterGroup[];
  activeFilters: Record<string, any>;
  onChange: (filters: Record<string, any>) => void;
  onReset: () => void;
  onApply: () => void;
}

interface FilterGroup {
  id: string;
  label: string;
  type: 'checkbox' | 'radio' | 'range' | 'toggle';
  options?: FilterOption[];
  min?: number;
  max?: number;
}
```

#### Desktop Filter Panel (Sidebar)

```
┌─────────────────────┐
│ Filters             │
│                     │
│ Categories ▼        │
│ ┌─────────────────┐ │
│ │ ☑ Science Fiction│ │
│ │ ☐ Fantasy        │ │
│ │ ☐ Mystery        │ │
│ │ ☐ Romance        │ │
│ │ ☐ Biography      │ │
│ │ [Show More ▼]    │ │
│ └─────────────────┘ │
│                     │
│ Price Range ▼       │
│ ┌─────────────────┐ │
│ │  ◄═══○═══════►  │ │
│ │  $0        $50  │ │
│ │                 │ │
│ │  Min   Max      │ │
│ │  ┌──┐  ┌──┐    │ │
│ │  │ 0│  │50│    │ │
│ │  └──┘  └──┘    │ │
│ └─────────────────┘ │
│                     │
│ Rating ▼            │
│ ┌─────────────────┐ │
│ │ ☑ 4★ & up       │ │
│ │ ☐ 3★ & up       │ │
│ │ ☐ 2★ & up       │ │
│ └─────────────────┘ │
│                     │
│ Format ▼            │
│ ┌─────────────────┐ │
│ │ ☑ Digital       │ │
│ │ ☐ Paperback     │ │
│ │ ☐ Hardcover     │ │
│ │ ☐ Audiobook     │ │
│ └─────────────────┘ │
│                     │
│ Publisher ▼         │
│ ┌─────────────────┐ │
│ │ ☐ Penguin       │ │
│ │ ☐ HarperCollins │ │
│ │ ☐ Simon & Schust│ │
│ │ [Show More ▼]   │ │
│ └─────────────────┘ │
│                     │
│ Language ▼          │
│ ┌─────────────────┐ │
│ │ ☑ English       │ │
│ │ ☐ Arabic        │ │
│ │ ☐ Spanish       │ │
│ └─────────────────┘ │
│                     │
│ [Clear All Filters] │
└─────────────────────┘

Width: 280px
Position: Sticky (scrolls with content)
Background: #FFFFFF
Border: 1px solid #E8E5DC
Border Radius: 8px
Padding: 24px
Shadow: sm

Collapsible sections
Checkbox groups: Multi-select
Radio groups: Single-select
Range slider: Dual thumb
Instant update (no apply button needed)
Active filters: Show count badge
```

#### Mobile Filter Panel (Bottom Sheet)

```
┌─────────────────────┐
│                     │ ← Backdrop
│                     │
│ ╔═══════════════════╗
│ ║ ─── Filters       ║
│ ║                   ║
│ ║ [Same content as  ║
│ ║  desktop but      ║
│ ║  scrollable]      ║
│ ║                   ║
│ ║ ───────────────── ║
│ ║ [Clear]  [Apply]  ║
│ ╚═══════════════════╝

Height: 70% of viewport
Border Radius: 16px 16px 0 0
Handle: Drag indicator at top
Swipe: Down to dismiss
Buttons: Sticky at bottom
Apply: Updates results & closes
```

---

### Navbar Component

```typescript
interface NavbarProps {
  user?: User;
  cartItemCount: number;
  onSearch?: (query: string) => void;
  onCartClick?: () => void;
  onProfileClick?: () => void;
  onLogoClick?: () => void;
}
```

#### Desktop Navbar

```
┌────────────────────────────────────────────────────────┐
│ 📚 BookMart    [🔍 Search books...]    [🛒 3] [👤 JD] │
└────────────────────────────────────────────────────────┘

Height: 72px
Background: #FFFFFF
Border Bottom: 1px solid #E8E5DC
Shadow: sm
Position: Sticky top
Z-index: 100

Layout:
- Logo: Left (160px width)
- Search: Center (flex-grow, max 600px)
- Actions: Right (fixed width)

Logo:
- Clickable → Home
- Font: Cairo Bold 24px
- Color: #1C3F68
- Icon: 📚 or custom SVG

Search:
- Expandable on smaller screens
- Margin: 0 32px

Cart:
- Icon + Badge
- Badge: Shows count (max "9+")
- Hover: Preview cart items (optional)

Profile:
- Avatar or Icon
- Dropdown on click:
  ┌──────────────┐
  │ John Doe     │
  │ Avid Reader  │
  ├──────────────┤
  │ My Account   │
  │ Orders       │
  │ Library      │
  │ Wishlist     │
  │ Settings     │
  │ Support      │
  ├──────────────┤
  │ Sign Out     │
  └──────────────┘
```

#### Mobile Navbar

```
┌─────────────────────────────┐
│ [☰] 📚 BookMart    [🛒3] [🔍]│
└─────────────────────────────┘

Height: 56px
Simplified layout
Hamburger: Opens side drawer
Search: Icon → Full screen search
Cart: Icon + Badge
No profile icon (in hamburger menu)
```

---

### CartItem Component

```typescript
interface CartItemProps {
  item: {
    bookId: string;
    title: string;
    author: string;
    coverUrl: string;
    format: string;
    price: number;
    quantity: number;
  };
  onQuantityChange: (quantity: number) => void;
  onRemove: () => void;
  onWishlist: () => void;
}
```

#### Cart Item Layout

```
Desktop:
┌──────┬────────────────────────────────────────────────┐
│      │ The Midnight Garden                            │
│Cover │ by Sarah Williams                              │
│80x   │ Digital Edition · 384 pages                    │
│106px │ ⭐ 4.9                                          │
│      │                                                 │
│      │ [− 1 +]  $24.99  [♡ Wishlist] [🗑️ Remove]    │
└──────┴────────────────────────────────────────────────┘

Mobile:
┌────┬──────────────────────────────┐
│Cvr │ The Midnight Garden          │
│60x │ by Sarah Williams            │
│80px│ Digital · ⭐ 4.9             │
│    │                              │
│    │ $24.99      [− 1 +]          │
│    │ [♡]  [🗑️]                   │
└────┴──────────────────────────────┘

Quantity Selector:
┌────────────┐
│ [−] 1 [+]  │
└────────────┘
- Buttons: 32x32px
- Disabled if quantity = 1 (for minus)
- Max quantity: 10 (for digital), 99 (for physical)
- Update debounced (500ms)

Swipe to Delete (Mobile):
  Swipe Left
┌────────────────┐        ┌──────────┐
│ [Cart Item]    │ ═════► │   [🗑️]  │
└────────────────┘        └──────────┘
```

---

### OrderCard Component

```typescript
interface OrderCardProps {
  order: {
    id: string;
    orderNumber: string;
    date: string;
    items: OrderItem[];
    total: number;
    status: 'processing' | 'shipped' | 'delivered' | 'cancelled';
    trackingUrl?: string;
  };
  onTrack?: () => void;
  onViewDetails?: () => void;
  onBuyAgain?: () => void;
}
```

#### Order Card Layout

```
┌────────────────────────────────────────────────────────┐
│ Order #BM-2025-00142                   January 9, 2026 │
│ 3 items                                   Total $70.17 │
│                                                        │
│ ┌──┐ ┌──┐ ┌──┐                                        │
│ │  │ │  │ │  │  [Book thumbnails]                     │
│ └──┘ └──┘ └──┘                                        │
│                                                        │
│ Status: ● In Transit                                   │
│ Estimated Delivery: January 16-18, 2026               │
│                                                        │
│ [Track Order]  [View Details]                         │
└────────────────────────────────────────────────────────┘

Status Badge Colors:
- Processing: #AF924A (Gold)
- Shipped: #647CA4 (Blue)
- In Transit: #647CA4 (Blue)
- Delivered: #6B8E6B (Green)
- Cancelled: #9B9B9B (Gray)

Status Icons:
- Processing: ⏳
- Shipped: 📦
- In Transit: 🚚
- Delivered: ✓
- Cancelled: ✕
```

---

## Organisms (Complex Components)

### Hero Section Component

```
Desktop:
┌──────────────────────────────────────────────────────────┐
│  ┌────────────┐                                          │
│  │            │  Book of the Month                       │
│  │   Cover    │  "The Midnight Garden"                   │
│  │   Image    │  by Sarah Williams                       │
│  │   Large    │  ⭐⭐⭐⭐⭐ 4.9 (3,421 reviews)         │
│  │            │                                          │
│  │            │  A mesmerizing tale of love, loss,       │
│  │            │  and mystery set in a forgotten          │
│  │            │  English garden...                       │
│  │            │                                          │
│  └────────────┘  [Add to Cart] [Buy Now $24.99]         │
└──────────────────────────────────────────────────────────┘

Layout: Flex row
Cover: 30% width
Content: 70% width
Background: Linear gradient or solid #EEDEAA
Padding: 48px
Border Radius: 12px
Min Height: 400px

Mobile: Stacks vertically
```

---

### Product Grid Component

```typescript
interface ProductGridProps {
  books: Book[];
  columns: 2 | 3 | 4 | 5;
  gap: number;
  loading?: boolean;
  onLoadMore?: () => void;
}
```

```
Desktop (4 columns):
┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐
│Book 1│  │Book 2│  │Book 3│  │Book 4│
└──────┘  └──────┘  └──────┘  └──────┘

┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐
│Book 5│  │Book 6│  │Book 7│  │Book 8│
└──────┘  └──────┘  └──────┘  └──────┘

Gap: 24px
Responsive: 5 cols (>1440px), 4 cols (1024-1440px), 3 cols (768-1024px), 2 cols (<768px)

Infinite Scroll:
- Load more trigger: 200px before end
- Loading indicator: Skeleton cards
- No "Load More" button (auto-load)
```

---

### Carousel Component

```typescript
interface CarouselProps {
  items: ReactNode[];
  autoplay?: boolean;
  interval?: number;
  showArrows?: boolean;
  showDots?: boolean;
  slidesToShow?: number;
  responsive?: ResponsiveSettings[];
}
```

```
Desktop:
  ←  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐  →
     │Item 1│  │Item 2│  │Item 3│  │Item 4│  │Item 5│
     └──────┘  └──────┘  └──────┘  └──────┘  └──────┘

        ●       ●       ●       ○       ○

Features:
- Arrows: Outside on desktop, inside on mobile
- Dots: Below carousel, clickable
- Swipe: Touch gesture support
- Keyboard: Arrow keys navigation
- Snap: Smooth scroll snap
- Peek: Show partial next/prev items
- Auto-play: Optional, pauses on hover

Mobile:
- Full width items
- Swipe gesture primary
- Smaller/hidden arrows
- Dots remain visible
```

---

### Modal Component

```typescript
interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl' | 'fullscreen';
  closeOnBackdrop?: boolean;
  closeOnEsc?: boolean;
  children: ReactNode;
}
```

```
┌────────────────────────────────────────┐
│                                        │ ← Backdrop
│    ╔════════════════════════════╗     │   70% opacity
│    ║ Modal Title           [×]  ║     │   #1C3F68
│    ║────────────────────────────║     │
│    ║                            ║     │
│    ║   Modal Content            ║     │
│    ║                            ║     │
│    ║                            ║     │
│    ║────────────────────────────║     │
│    ║        [Cancel] [Confirm]  ║     │
│    ╚════════════════════════════╝     │
│                                        │
└────────────────────────────────────────┘

Sizes:
- sm: 400px
- md: 600px
- lg: 800px
- xl: 1000px
- fullscreen: 100vw/vh (mobile)

Animation:
- Backdrop: Fade in (200ms)
- Modal: Scale + Fade (250ms)
- Exit: Reverse animation (150ms)

Features:
- Lock body scroll
- Focus trap (tab navigation)
- ESC key to close
- Click outside to close
- Accessible (ARIA attributes)
```

---

### Toast Notification Component

```typescript
interface ToastProps {
  message: string;
  type: 'success' | 'error' | 'warning' | 'info';
  duration?: number;
  action?: {
    label: string;
    onClick: () => void;
  };
  onClose?: () => void;
}
```

```
Desktop (Top Right):
                ┌──────────────────────────────┐
                │ ✓ Added to cart              │
                │ [Undo]                   [×] │
                └──────────────────────────────┘

Mobile (Bottom):
┌─────────────────────────────────────┐
│ ✓ Added to cart                     │
│ [Undo]                          [×] │
└─────────────────────────────────────┘

Types:
- Success: #6B8E6B background, ✓ icon
- Error: #C85C5C background, ⚠️ icon
- Warning: #D4A574 background, ⚠️ icon
- Info: #647CA4 background, ℹ️ icon

All have white text
Auto-dismiss: 5 seconds (default)
Hover: Pause auto-dismiss
Stack: Max 3 toasts
Position: Fixed
Z-index: 9999
Animation: Slide in + Fade
```

---

### Breadcrumb Component

```
Home > Books > Fiction > Mystery > Book Details

- Separator: > or /
- Last item: Not clickable (current page)
- Color: #647CA4
- Hover: #1C3F68
- Mobile: Show only last 2 items with ellipsis
  Home > ... > Mystery > Details
```

---

### Pagination Component

```
[←Previous] [1] [2] [3] ... [10] [11] [12] [Next→]
               ↑ Active page

Active: #AF924A background, #FFFFFF text
Inactive: Transparent, #1C3F68 text
Hover: #E8E5DC background
Disabled: #9B9B9B color, not clickable
Mobile: Show [←] [Page X of Y] [→]
```

---

## Interaction Patterns

### Hover States (Desktop Only)
- Cards: Elevate (shadow + translate)
- Buttons: Darken background
- Links: Underline or color change
- Icons: Scale up slightly (1.05x)
- Thumbnails: Zoom in

### Loading States
- Skeleton screens for initial load
- Spinners for actions (buttons, forms)
- Progress bars for uploads
- Shimmer effect for placeholders

### Empty States
- Friendly illustration or icon
- Clear message
- Call-to-action button
- Suggestions when applicable

### Success States
- Checkmark animation
- Green color scheme
- Brief message
- Auto-proceed option

### Error States
- Red color scheme
- Clear error message
- Suggested fix
- Retry option

---

## Responsive Breakpoints

```
// Mobile First Approach

Mobile (Portrait):
  0px - 640px
  1 column grids
  Touch-optimized (48px+ targets)
  Bottom navigation
  Full-width components

Tablet (Portrait):
  640px - 768px
  2 column grids
  Mix of mobile/desktop patterns

Tablet (Landscape):
  768px - 1024px
  3 column grids
  Desktop-like with adaptations

Desktop (Small):
  1024px - 1280px
  4 column grids
  Full desktop features

Desktop (Medium):
  1280px - 1440px
  Standard layout
  Optimal viewing

Desktop (Large):
  1440px+
  5 column grids
  Max content width: 1600px
  Centered with margins
```

---

## Dark Mode Support

```
Color Mapping:

Light Mode → Dark Mode

Background:
#CAC6B1 → #1A1A1A
#FFFFFF → #2A2A2A
#E8E5DC → #3A3A3A

Text:
#1C3F68 → #E8E5DC
#4A4A4A → #C0C0C0
#9B9B9B → #808080

Accent (Unchanged):
#AF924A → #D4A574 (slightly lighter)

Cards:
Add 1px border in dark mode for definition
Increase shadow opacity

Images:
Apply subtle overlay (5% black) to reduce brightness

Implementation:
- Respect system preference
- Manual toggle in settings
- Persist preference
- Smooth transition (200ms)
```

---

