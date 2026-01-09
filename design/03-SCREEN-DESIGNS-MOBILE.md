# Mobile Screen Designs (375px width - iPhone SE/12/13 Mini)

## 1. Splash / Loading Screen (Mobile)

```
┌─────────────────────┐
│                     │
│                     │
│                     │
│                     │
│       ┌─────┐       │
│       │     │       │
│       │ 📚  │       │
│       │Book │       │
│       │Mart │       │
│       └─────┘       │
│                     │
│    [Loading ●●●]    │
│    ═══════          │
│                     │
│  "Discover Your     │
│   Next Read"        │
│                     │
│                     │
│                     │
│                     │
└─────────────────────┘

Portrait mode only
Background: #CAC6B1
Duration: 2-3 seconds
Smooth fade to welcome
```

---

## 2. Welcome Screen (Mobile)

```
┌─────────────────────┐
│                     │
│                     │
│    [Hero Image]     │
│   Stack of Books    │
│                     │
│                     │
├─────────────────────┤
│                     │
│    📚 BookMart      │
│                     │
│  Your Gateway to    │
│  Endless Stories    │
│                     │
│  • 1000s of books   │
│  • AI powered       │
│  • Read anywhere    │
│                     │
│  ┌───────────────┐  │
│  │  Get Started  │  │
│  └───────────────┘  │
│                     │
│  ┌───────────────┐  │
│  │   Sign In     │  │
│  └───────────────┘  │
│                     │
│                     │
└─────────────────────┘

Layout: Full screen, stacked
Hero: 50% height
Content: Centered
Buttons: Full width - 16px margin
```

---

## 3. Login Screen (Mobile)

```
┌─────────────────────┐
│ [← Back]            │
├─────────────────────┤
│                     │
│                     │
│   Welcome Back      │
│                     │
│   Sign in to your   │
│   account           │
│                     │
│   Email             │
│   ┌───────────────┐ │
│   │               │ │
│   └───────────────┘ │
│                     │
│   Password          │
│   ┌───────────────┐ │
│   │               │ │
│   └───────────────┘ │
│                     │
│   [ ] Remember me   │
│   Forgot password?  │
│                     │
│   ┌───────────────┐ │
│   │   Sign In     │ │
│   └───────────────┘ │
│                     │
│   ───── or ─────    │
│                     │
│   [G] Google        │
│   [A] Apple         │
│                     │
│   Don't have an     │
│   account? Sign Up  │
│                     │
│                     │
└─────────────────────┘

Full screen form
Inputs: 48px height (touch-friendly)
Margins: 16px horizontal
Skip button: Top right
```

---

## 4. Signup Screen (Mobile)

```
┌─────────────────────┐
│ [← Back]            │
├─────────────────────┤
│                     │
│   Create Account    │
│                     │
│   Join thousands    │
│   of readers        │
│                     │
│   Full Name         │
│   ┌───────────────┐ │
│   │               │ │
│   └───────────────┘ │
│                     │
│   Email             │
│   ┌───────────────┐ │
│   │               │ │
│   └───────────────┘ │
│                     │
│   Password          │
│   ┌───────────────┐ │
│   │               │ │
│   └───────────────┘ │
│   • 8+ chars        │
│   • 1 uppercase     │
│                     │
│   [✓] I agree to    │
│       Terms         │
│                     │
│   ┌───────────────┐ │
│   │   Sign Up     │ │
│   └───────────────┘ │
│                     │
│   ───── or ─────    │
│                     │
│   [G] Google        │
│   [A] Apple         │
│                     │
│   Have account?     │
│   Sign In           │
│                     │
└─────────────────────┘

Scrollable content
Form validation: Inline
Keyboard avoidance: Auto-scroll
```

---

## 5. Home / Feed Screen (Mobile)

```
┌─────────────────────┐
│ 📚  [🔍]   [🛒3][👤]│
├─────────────────────┤
│                     │
│ ┌─────────────────┐ │
│ │  ┌────┐         │ │
│ │  │Cvr │ Book of │ │
│ │  │    │ Month   │ │
│ │  └────┘         │ │
│ │  "The Midnight  │ │
│ │   Garden"       │ │
│ │  by S. Williams │ │
│ │  ⭐ 4.9 (3,421) │ │
│ │                 │ │
│ │  [Add] [Buy Now]│ │
│ └─────────────────┘ │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📖 Recommended      │
│                     │
│ ← ┌───┐ ┌───┐ ┌───┐→│
│   │Cvr│ │Cvr│ │Cvr││
│   │   │ │   │ │   ││
│   └───┘ └───┘ └───┘│
│   Title Title Title │
│   $19   $22   $18   │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 🎁 Free Books       │
│                     │
│ ← ┌───┐ ┌───┐ ┌───┐→│
│   │   │ │   │ │   ││
│   │FR │ │FR │ │FR ││
│   │EE │ │EE │ │EE ││
│   └───┘ └───┘ └───┘│
│   Title Title Title │
│   FREE  FREE  FREE  │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 🏆 Book of Year     │
│                     │
│ ← ┌───┐ ┌───┐ ┌───┐→│
│   │WIN│ │NOM│ │NOM││
│   │NER│ │   │ │   ││
│   └───┘ └───┘ └───┘│
│   Title Title Title │
│   $29   $27   $26   │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📚 Categories       │
│ [Fiction][Mystery]  │
│ [Romance][Sci-Fi]   │
│                     │
└─────────────────────┘

Header: Fixed at top
Sections: Stack vertically
Carousels: Horizontal scroll (swipe)
Cards: 140px width mobile
Hero Card: Full width - 32px margin
```

---

## 6. Search Screen (Mobile)

```
┌─────────────────────┐
│ [← ]  [🔍 science...│
│                  [×]│
├─────────────────────┤
│                     │
│ Search Results      │
│ 142 books  [⋮ Sort] │
│                     │
│ [Filters ▼]         │
│                     │
│ ┌────┐ ┌────┐      │
│ │Cvr │ │Cvr │      │
│ │    │ │    │      │
│ └────┘ └────┘      │
│ Title   Title       │
│ Author  Author      │
│ ⭐4.5   ⭐4.8       │
│ $19.99  $22.99      │
│ [+Cart] [+Cart]     │
│                     │
│ ┌────┐ ┌────┐      │
│ │Cvr │ │Cvr │      │
│ │    │ │    │      │
│ └────┘ └────┘      │
│ Title   Title       │
│ Author  Author      │
│ ⭐4.3   ⭐4.7       │
│ $18.99  $25.99      │
│ [+Cart] [+Cart]     │
│                     │
│ ┌────┐ ┌────┐      │
│ │Cvr │ │Cvr │      │
│ │    │ │    │      │
│ └────┘ └────┘      │
│ Title   Title       │
│ Author  Author      │
│ ⭐4.6   ⭐4.4       │
│ $21.99  $19.99      │
│ [+Cart] [+Cart]     │
│                     │
│ [Load More]         │
│                     │
└─────────────────────┘

Grid: 2 columns, 8px gap
Cards: Compact, 160px width
Filters: Bottom sheet modal
Sort: Dropdown from top
```

---

## 7. Filters Bottom Sheet (Mobile)

```
┌─────────────────────┐
│                     │
│                     │ ← Dimmed background
│                     │
│ ╔═══════════════════╗
│ ║  ─────  Filters  ║
│ ║                   ║
│ ║ Categories ▼      ║
│ ║ ☑ Sci-Fi          ║
│ ║ ☐ Fantasy         ║
│ ║ ☐ Mystery         ║
│ ║                   ║
│ ║ Price Range       ║
│ ║ ◄═══○═══════►     ║
│ ║ $0        $50     ║
│ ║                   ║
│ ║ Rating ▼          ║
│ ║ ☑ 4★ & up         ║
│ ║ ☐ 3★ & up         ║
│ ║                   ║
│ ║ Format ▼          ║
│ ║ ☑ Digital         ║
│ ║ ☐ Print           ║
│ ║ ☐ Audio           ║
│ ║                   ║
│ ║ Language ▼        ║
│ ║ ☑ English         ║
│ ║ ☐ Arabic          ║
│ ║                   ║
│ ║ [Clear]  [Apply]  ║
│ ╚═══════════════════╝
└─────────────────────┘

Appearance: Slide up from bottom
Backdrop: Semi-transparent dark
Height: 70% screen max
Scrollable: If content overflows
Gestures: Swipe down to dismiss
```

---

## 8. Book Details Screen (Mobile)

```
┌─────────────────────┐
│ [←]     [♡] [⋮ Share│
├─────────────────────┤
│                     │
│   ┌───────────┐     │
│   │           │     │
│   │   Book    │     │
│   │   Cover   │     │
│   │   Large   │     │
│   │           │     │
│   └───────────┘     │
│                     │
│ The Midnight Garden │
│ by Sarah Williams   │
│                     │
│ ⭐⭐⭐⭐⭐ 4.9      │
│ (3,421 reviews)     │
│                     │
│ Fiction > Mystery   │
│ HarperCollins 2025  │
│ 384 pages           │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ Description:        │
│ A mesmerizing tale  │
│ of love, loss, and  │
│ mystery set in a    │
│ forgotten English   │
│ garden. When Emma   │
│ inherits her...     │
│ [Read More ▼]       │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ Format:             │
│ ⦿ Digital    $24.99 │
│ ○ Paperback  $29.99 │
│ ○ Hardcover  $34.99 │
│ ○ Audiobook  $19.99 │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ Reviews (3,421) →   │
│                     │
│ ┌─────────────────┐ │
│ │ ⭐⭐⭐⭐⭐       │ │
│ │ "Captivating!"  │ │
│ │ by BookLover... │ │
│ │ I couldn't put  │ │
│ │ this book...    │ │
│ │ [Read More]     │ │
│ └─────────────────┘ │
│                     │
│ [View All Reviews]  │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ You May Also Like   │
│                     │
│← ┌───┐ ┌───┐ ┌───┐→│
│  │   │ │   │ │   ││
│  └───┘ └───┘ └───┘│
│                     │
│                     │
└─────────────────────┘
┌─────────────────────┐
│ $24.99              │
│ [Add to Cart] [Buy] │
└─────────────────────┘
      ↑ Sticky footer

Scroll: Smooth vertical
Footer: Fixed at bottom
Images: Lazy load
Share: Native share sheet
Preview: Tap cover to preview
```

---

## 9. Shopping Cart (Mobile)

```
┌─────────────────────┐
│ [←] Cart (3)        │
├─────────────────────┤
│                     │
│ ┌─────────────────┐ │
│ │ ┌──┐            │ │
│ │ │  │ Midnight   │ │
│ │ │Cv│ Garden     │ │
│ │ │  │ S. Williams│ │
│ │ └──┘ Digital    │ │
│ │      ⭐ 4.9     │ │
│ │                 │ │
│ │ $24.99          │ │
│ │ [- 1 +] [♡][🗑]│ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ ┌──┐            │ │
│ │ │  │ Ocean's    │ │
│ │ │Cv│ Whisper    │ │
│ │ │  │ J. Peterson│ │
│ │ └──┘ Paperback  │ │
│ │      ⭐ 4.7     │ │
│ │                 │ │
│ │ $19.99          │ │
│ │ [- 1 +] [♡][🗑]│ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ ┌──┐            │ │
│ │ │  │ Digital    │ │
│ │ │Cv│ Dreams     │ │
│ │ │  │ L. Chen    │ │
│ │ └──┘ Audiobook  │ │
│ │      ⭐ 4.8     │ │
│ │                 │ │
│ │ $19.99          │ │
│ │ [- 1 +] [♡][🗑]│ │
│ └─────────────────┘ │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 💡 Recommended      │
│← ┌───┐ ┌───┐ ┌───┐→│
│  │   │ │   │ │   ││
│  └───┘ └───┘ └───┘│
│                     │
└─────────────────────┘
┌─────────────────────┐
│ Subtotal:   $64.97  │
│ Tax:        $5.20   │
│ Total:      $70.17  │
│ ━━━━━━━━━━━━━━━━━  │
│ [Checkout]          │
└─────────────────────┘
      ↑ Sticky footer

Cards: Full width - 16px margin
Swipe: Left to delete (iOS style)
Footer: Sticky with summary
Empty: Show recommendations
```

---

## 10. Checkout Screen (Mobile)

```
┌─────────────────────┐
│ [←] Checkout        │
├─────────────────────┤
│ [Cart]→[Pay]→[Done] │
│         ▲           │
│                     │
│ 1️⃣ Contact         │
│                     │
│ Email               │
│ ┌─────────────────┐ │
│ │user@example.com │ │
│ └─────────────────┘ │
│ [✓] Email updates   │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 2️⃣ Payment         │
│                     │
│ ⦿ Card              │
│ ○ PayPal            │
│ ○ Apple Pay         │
│ ○ Google Pay        │
│                     │
│ Card Number         │
│ ┌─────────────────┐ │
│ │                 │ │
│ └─────────────────┘ │
│                     │
│ Expiry      CVV     │
│ ┌────────┐ ┌─────┐ │
│ │ MM/YY  │ │     │ │
│ └────────┘ └─────┘ │
│                     │
│ Cardholder          │
│ ┌─────────────────┐ │
│ │                 │ │
│ └─────────────────┘ │
│                     │
│ [✓] Terms agreed    │
│ [✓] Save card       │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ Order Summary       │
│ 3 items             │
│                     │
│ ┌──┐ Midnight      │
│ │  │ Garden $24.99 │
│ └──┘               │
│ ┌──┐ Ocean's       │
│ │  │ Whisper $19.99│
│ └──┘               │
│ ┌──┐ Digital       │
│ │  │ Dreams $19.99 │
│ └──┘               │
│                     │
│ Subtotal:   $64.97  │
│ Tax:        $5.20   │
│ Total:      $70.17  │
│                     │
└─────────────────────┘
┌─────────────────────┐
│ 🔒 [Complete Order] │
└─────────────────────┘
      ↑ Sticky CTA

Sections: Collapsible accordions
Inputs: Large touch targets
Payment: Native integrations priority
Validation: Real-time inline
Autofill: Support system autofill
```

---

## 11. Order Confirmation (Mobile)

```
┌─────────────────────┐
│                     │
│                     │
│       ✓             │
│    Success!         │
│                     │
│ Order Confirmed     │
│                     │
│ Order #BM-2025-00142│
│                     │
│ Email sent to:      │
│ user@example.com    │
│                     │
├─────────────────────┤
│                     │
│ Order Details       │
│                     │
│ ┌──┐ Midnight      │
│ │  │ Garden        │
│ └──┘ Digital $24.99│
│                     │
│ ┌──┐ Ocean's       │
│ │  │ Whisper       │
│ └──┘ Paper $19.99  │
│                     │
│ ┌──┐ Digital       │
│ │  │ Dreams        │
│ └──┘ Audio $19.99  │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ Total: $70.17       │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📚 Digital Ready    │
│                     │
│ Midnight Garden     │
│ [Start Reading]     │
│                     │
│ Digital Dreams      │
│ [Start Listening]   │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📦 Shipping         │
│                     │
│ Ocean's Whisper     │
│ Status: Processing  │
│ Arrives: Jan 16-18  │
│                     │
│ John Doe            │
│ 123 Main St, 4B     │
│ New York, NY 10001  │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ [View Order]        │
│ [Shop More]         │
│ [Download Invoice]  │
│                     │
│ Need help?          │
│ [Contact Support]   │
│                     │
└─────────────────────┘

Animation: Checkmark animation
Sections: Clearly divided
CTAs: Prominent for digital items
Auto-redirect: Optional countdown
```

---

## 12. Order Tracking (Mobile)

```
┌─────────────────────┐
│ [←] Order Tracking  │
├─────────────────────┤
│                     │
│ Order #BM-2025-00142│
│ Jan 9, 2026         │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📦 Ocean's Whisper  │
│    Paperback        │
│                     │
│ ●━━●━━●━━○━━○       │
│ │  │  │  │  │       │
│ ✓  ✓  ✓  ◌  ◌       │
│ │  │  │  │  │       │
│ Order                │
│ Jan 9                │
│ 10:30                │
│                     │
│ Packed               │
│ Jan 10               │
│ 09:15                │
│                     │
│ Shipped              │
│ Jan 11               │
│ 14:20                │
│                     │
│ In                   │
│ Transit              │
│ Jan 16               │
│ (est)                │
│                     │
│ Delivered            │
│ Jan 18               │
│ (latest)             │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ Current Status      │
│ 📍 In Transit       │
│                     │
│ Newark, NJ          │
│ Distribution Center │
│                     │
│ Updated:            │
│ Jan 11, 2:20 PM     │
│                     │
│ [Track on UPS]      │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📜 History          │
│                     │
│ ● Jan 11, 2:20 PM   │
│   In transit        │
│   Newark, NJ        │
│                     │
│ ● Jan 11, 6:45 AM   │
│   Departed facility │
│   Philadelphia, PA  │
│                     │
│ ● Jan 10, 9:15 PM   │
│   Arrived facility  │
│   Philadelphia, PA  │
│                     │
│ ● Jan 10, 9:15 AM   │
│   Picked up         │
│   New York, NY      │
│                     │
│ ● Jan 9, 10:30 AM   │
│   Order confirmed   │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📍 Delivery To      │
│                     │
│ John Doe            │
│ 123 Main St, 4B     │
│ New York, NY 10001  │
│ (555) 123-4567      │
│                     │
│ "Leave at desk"     │
│                     │
│ [Update Prefs]      │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ Need Help?          │
│                     │
│ [Report Problem]    │
│ [Change Address]    │
│ [Contact Support]   │
│                     │
└─────────────────────┘

Progress: Vertical timeline (mobile-friendly)
Map: Optional inline map
Updates: Push notifications enabled
Scroll: Long page, smooth
```

---

## 13. Profile Screen (Mobile)

```
┌─────────────────────┐
│ [☰] Profile    [⚙️] │
├─────────────────────┤
│                     │
│    [Avatar]         │
│                     │
│    John Doe         │
│    user@example.com │
│                     │
│    Reader Level:    │
│    ⭐⭐⭐⭐ Avid    │
│                     │
│    Member Since     │
│    March 2024       │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📊 Stats            │
│ 24 Books Read       │
│ 12 Reviews          │
│ 8 In Wishlist       │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ 📦 My Orders    [>] │
│                     │
│ 📚 My Library   [>] │
│                     │
│ ♡ Wishlist      [>] │
│                     │
│ 📍 Addresses    [>] │
│                     │
│ 💳 Payment      [>] │
│                     │
│ 💬 Support      [>] │
│                     │
│ ⚙️ Settings     [>] │
│                     │
│ ━━━━━━━━━━━━━━━━━  │
│                     │
│ [Sign Out]          │
│                     │
└─────────────────────┘

Layout: List style navigation
Avatar: Large, centered
Stats: Quick overview
Menu: Touch-friendly spacing (56px)
```

---

## 14. Orders List (Mobile)

```
┌─────────────────────┐
│ [←] My Orders       │
├─────────────────────┤
│                     │
│ [All][Active][Past] │
│                     │
│ ┌─────────────────┐ │
│ │ Order #BM-..142 │ │
│ │ Jan 9, 2026     │ │
│ │                 │ │
│ │ ┌─┐ ┌─┐ ┌─┐    │ │
│ │ │ │ │ │ │ │    │ │
│ │ └─┘ └─┘ └─┘    │ │
│ │ 3 items         │ │
│ │                 │ │
│ │ Total: $70.17   │ │
│ │ In Transit      │ │
│ │                 │ │
│ │ [Track Order]   │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Order #BM-..128 │ │
│ │ Jan 2, 2026     │ │
│ │                 │ │
│ │ ┌─┐ ┌─┐         │ │
│ │ │ │ │ │         │ │
│ │ └─┘ └─┘         │ │
│ │ 2 items         │ │
│ │                 │ │
│ │ Total: $44.98   │ │
│ │ ✓ Delivered     │ │
│ │ Jan 5, 2026     │ │
│ │                 │ │
│ │ [Buy Again]     │ │
│ │ [Write Review]  │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Order #BM-..294 │ │
│ │ Dec 28, 2024    │ │
│ │                 │ │
│ │ ┌─┐ ┌─┐ ┌─┐ +2  │ │
│ │ │ │ │ │ │ │     │ │
│ │ └─┘ └─┘ └─┘     │ │
│ │ 5 items         │ │
│ │                 │ │
│ │ Total: $112.45  │ │
│ │ ✓ Delivered     │ │
│ │ Jan 1, 2025     │ │
│ │                 │ │
│ │ [Buy Again]     │ │
│ └─────────────────┘ │
│                     │
│ [Load More]         │
│                     │
└─────────────────────┘

Cards: Tap to expand details
Filters: Tab bar at top
Pull-to-refresh: Enabled
Status: Color-coded badges
```

---

## 15. Support Chat (Mobile)

```
┌─────────────────────┐
│ [←] Support    [···]│
├─────────────────────┤
│                     │
│ ┌─────────────────┐ │
│ │ 🤖 AI Assistant │ │
│ │ Hi John! How    │ │
│ │ can I help?     │ │
│ │                 │ │
│ │ • Order track   │ │
│ │ • Returns       │ │
│ │ • Account       │ │
│ │ • Payment       │ │
│ │         10:30   │ │
│ └─────────────────┘ │
│                     │
│     ┌─────────────┐ │
│     │ I haven't   │ │
│     │ received my │ │
│     │ order yet   │ │
│     │ 10:31       │ │
│     └─────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ 🤖 AI Assistant │ │
│ │ I found your    │ │
│ │ order #BM-..142 │ │
│ │                 │ │
│ │ Status: Transit │ │
│ │ Expected:       │ │
│ │ Jan 16-18       │ │
│ │                 │ │
│ │[View Tracking]  │ │
│ │                 │ │
│ │ Is this the one?│ │
│ │         10:31   │ │
│ └─────────────────┘ │
│                     │
│     ┌─────────────┐ │
│     │ Yes, but I  │ │
│     │ need it for │ │
│     │ a gift      │ │
│     │ 10:32       │ │
│     └─────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ 🤖 AI Assistant │ │
│ │ I understand.   │ │
│ │ Let me connect  │ │
│ │ you with an     │ │
│ │ agent...        │ │
│ │         10:32   │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ 🔄 Connecting...│ │
│ │ Wait: ~2 mins   │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ 👤 Sarah        │ │
│ │ Hi John! I've   │ │
│ │ reviewed your   │ │
│ │ situation...    │ │
│ │         10:34   │ │
│ └─────────────────┘ │
│                     │
└─────────────────────┘
┌─────────────────────┐
│[+][Type message...] │
│                 [↑] │
└─────────────────────┘
      ↑ Input bar

Layout: iMessage-style
Bubbles: Different colors for AI/Agent/User
Attachment: [+] button
Actions: Tap-able buttons in bubbles
Keyboard: Dismissible, adjusts layout
Quick Replies: Suggested responses
Typing Indicator: Shows when typing
```

---

## Navigation Patterns (Mobile)

```
Bottom Navigation Bar:
┌─────────────────────┐
│                     │
│    Main Content     │
│                     │
└─────────────────────┘
┌─────────────────────┐
│[🏠][🔍][🛒][👤]    │
│Home Search Cart Me  │
└─────────────────────┘

Active state: Gold underline
Badge: Red dot on Cart icon
Height: 64px (safe area + 16px)

Hamburger Menu (Profile):
┌───────────┐
│ [Profile] │
│ ━━━━━━━━━ │
│ Orders    │
│ Library   │
│ Wishlist  │
│ Settings  │
│ Support   │
│ Sign Out  │
└───────────┘

Slide from left
Backdrop: Semi-transparent
Gestures: Swipe to open/close
```

---

## Gestures & Interactions

```
Swipe Actions:
- Swipe right: Back navigation
- Swipe left on cart item: Delete
- Swipe down: Refresh (pull-to-refresh)
- Swipe up from bottom: Filters/Sort
- Pinch: Zoom book cover

Tap Actions:
- Tap: Select/Open
- Long press: Quick actions menu
- Double tap: Add to wishlist
- 3D Touch: Peek & Pop (iOS)

Haptic Feedback:
- Success: Light impact
- Error: Notification impact
- Selection: Selection feedback
- Add to cart: Medium impact
```

---

## Animation Timings (Mobile)

```
Transitions:
- Page slide: 300ms ease-in-out
- Modal slide up: 250ms ease-out
- Bottom sheet: 200ms ease-out
- Fade: 150ms ease-in

Loading States:
- Skeleton screens: 200ms fade-in
- Shimmer effect: 1500ms loop
- Spinner: Immediate

Micro-interactions:
- Button press: 100ms scale down
- Card hover: 200ms elevation
- Ripple effect: 400ms spread
```

---

## Accessibility (Mobile)

```
Touch Targets:
- Minimum: 44x44px (iOS) / 48x48px (Android)
- Recommended: 56x56px for primary actions
- Spacing: 8px minimum between targets

Text Sizes:
- Support Dynamic Type (iOS)
- Support font scaling (Android)
- Max scale: 200%
- Maintain readability at all scales

Screen Reader:
- All images: Alt text
- All buttons: Labels
- Headings: Proper hierarchy
- Forms: Associated labels

Dark Mode:
- Auto-detect system preference
- Manual toggle in settings
- Adjusted color palette:
  * Background: #1A1A1A
  * Cards: #2A2A2A
  * Text: #E8E5DC
  * Accent: #D4A574 (lighter gold)
```

---

## Performance Targets

```
Initial Load:
- First paint: < 1s
- Interactive: < 2s
- Full load: < 3s

Navigation:
- Page transition: < 300ms
- Route change: < 500ms

Images:
- Progressive loading
- WebP format preferred
- Lazy loading below fold
- Placeholder: Low-res blur

Bundle Size:
- Initial: < 200KB
- Code splitting: Route-based
- Tree shaking: Enabled
- Compression: Gzip/Brotli
```

---
