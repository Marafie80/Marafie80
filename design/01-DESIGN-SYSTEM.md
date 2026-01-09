# Book Marketplace Design System

## Color Palette

```
Primary Colors:
- Background/Cards:     #CAC6B1  (Beige/Natural)
- Text Primary:         #1C3F68  (Dark Blue)
- Background Secondary: #647CA4  (Soft Blue Gray)
- Accent/CTA:          #AF924A  (Warm Gold)
- Highlight:           #EEDEAA  (Light Yellow)

Semantic Colors:
- Success:             #6B8E6B  (Muted Green)
- Error:               #C85C5C  (Muted Red)
- Info:                #647CA4  (Soft Blue Gray)
- Warning:             #D4A574  (Warm Beige)

Neutral Scale:
- White:               #FFFFFF
- Light Gray:          #E8E5DC
- Medium Gray:         #9B9B9B
- Dark Gray:           #4A4A4A
- Black:               #1A1A1A
```

## Typography

```
Font Families:
- Primary (Arabic + Latin): Cairo
- Secondary (Latin):        Montserrat
- Body:                     Lato

Font Scales:
- Hero:        48px / 3rem    (Bold)
- H1:          36px / 2.25rem (Bold)
- H2:          28px / 1.75rem (SemiBold)
- H3:          24px / 1.5rem  (SemiBold)
- H4:          20px / 1.25rem (Medium)
- Body Large:  18px / 1.125rem (Regular)
- Body:        16px / 1rem    (Regular)
- Body Small:  14px / 0.875rem (Regular)
- Caption:     12px / 0.75rem (Regular)

Line Heights:
- Headings: 1.2
- Body:     1.6
- Captions: 1.4
```

## Spacing System

```
Base Unit: 8px

Scale:
- xs:   4px   (0.5 unit)
- sm:   8px   (1 unit)
- md:   16px  (2 units)
- lg:   24px  (3 units)
- xl:   32px  (4 units)
- 2xl:  48px  (6 units)
- 3xl:  64px  (8 units)
```

## Border Radius

```
- sm:   4px   (buttons, small cards)
- md:   8px   (cards, inputs)
- lg:   12px  (modals, large cards)
- xl:   16px  (hero sections)
- full: 9999px (pills, avatars)
```

## Shadows

```
- sm:   0 1px 3px rgba(28, 63, 104, 0.08)
- md:   0 4px 8px rgba(28, 63, 104, 0.12)
- lg:   0 8px 16px rgba(28, 63, 104, 0.16)
- xl:   0 12px 24px rgba(28, 63, 104, 0.20)
```

## Component Specifications

### BookCard (Standard)
```
Dimensions:
- Desktop: 220px width x 380px height
- Mobile:  160px width x 280px height

Structure:
┌────────────────┐
│                │
│   Book Cover   │  (3:4 ratio)
│   [Image]      │
│                │
├────────────────┤
│ Title          │  (H4, 2 lines max)
│ Author         │  (Body Small, 1 line)
│ ⭐ 4.5 (120)   │  (Caption)
│ $19.99         │  (H4, Accent)
└────────────────┘
```

### BookCard (Featured)
```
Dimensions:
- Desktop: 320px width x 480px height
- Mobile:  Full width x 400px height

Structure:
┌──────────────────────┐
│                      │
│   Book Cover         │  (16:9 aspect)
│   [Hero Image]       │
│                      │
├──────────────────────┤
│ Title (Large)        │  (H2)
│ Author               │  (Body Large)
│ ⭐ 4.8 (2,345)       │  (Body Small)
│ Brief description... │  (Body, 3 lines)
│ [Add to Cart] [Buy]  │  (CTAs)
└──────────────────────┘
```

### Button Styles
```
Primary CTA:
- Background: #AF924A (Warm Gold)
- Text: #FFFFFF
- Padding: 12px 24px
- Border Radius: 4px
- Font: Montserrat SemiBold 16px

Secondary:
- Background: Transparent
- Border: 2px solid #1C3F68
- Text: #1C3F68
- Padding: 12px 24px

Ghost:
- Background: Transparent
- Text: #647CA4
- Padding: 8px 16px
```

### Input Fields
```
- Height: 48px
- Background: #FFFFFF
- Border: 1px solid #E8E5DC
- Border Radius: 4px
- Padding: 12px 16px
- Font: Lato Regular 16px
- Focus: Border #AF924A, Shadow sm

States:
- Default:  Border #E8E5DC
- Hover:    Border #9B9B9B
- Focus:    Border #AF924A
- Error:    Border #C85C5C
- Success:  Border #6B8E6B
```

## Navigation Structure

### Top Level Navigation
```
1. Home
2. Browse (Categories, Publishers, New Releases)
3. Search
4. Cart (Badge with count)
5. Profile (Orders, Wishlist, Settings)
```

### 3-Click Compliance Map

```
Critical Actions Maximum Clicks:
1. Find Book → View Details → Add to Cart:     3 clicks
2. Search → Filter → Add to Cart:              3 clicks
3. Home → Featured Book → Buy Now:             2 clicks
4. Cart → Checkout → Confirm Order:            3 clicks
5. Profile → Order History → Track Order:      3 clicks
6. Book Details → Start Reading:               2 clicks
7. Home → Search → Book Details:               3 clicks
8. Support → Open Ticket:                      2 clicks
9. Browse Category → Book Details:             2 clicks
10. Wishlist → Add to Cart:                    2 clicks
```

## Responsive Breakpoints

```
- Mobile:       < 640px
- Tablet:       640px - 1024px
- Desktop:      > 1024px
- Large Screen: > 1440px
```

## Animation Guidelines

```
Timing Functions:
- Standard: cubic-bezier(0.4, 0.0, 0.2, 1)  [200ms]
- Enter:    cubic-bezier(0.0, 0.0, 0.2, 1)  [250ms]
- Exit:     cubic-bezier(0.4, 0.0, 1, 1)    [150ms]

Use Cases:
- Button Hover:      100ms
- Card Hover:        200ms
- Modal Open/Close:  250ms
- Page Transition:   300ms
- Loading Spinner:   Continuous
```

## Accessibility

```
- WCAG 2.1 AA Compliance
- Color Contrast Ratios:
  * Text: 4.5:1 minimum
  * Large Text: 3:1 minimum
  * Interactive: 3:1 minimum
- Focus indicators on all interactive elements
- Keyboard navigation support
- Screen reader labels
- Alt text for all images
```
