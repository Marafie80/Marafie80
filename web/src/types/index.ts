// Book types
export interface Book {
  id: string;
  isbn: string;
  title: string;
  author: string;
  coverUrl: string;
  rating: number;
  reviewCount: number;
  price: number;
  currency: string;
  format: 'digital' | 'paperback' | 'hardcover' | 'audiobook';
  category: string;
  subcategory?: string;
  publisher: string;
  publishedDate: string;
  pages: number;
  language: string;
  description: string;
  isFree?: boolean;
  isOwned?: boolean;
  badge?: 'new' | 'bestseller' | 'featured' | 'winner' | 'nominee';
}

// User types
export interface User {
  id: string;
  name: string;
  email: string;
  avatar?: string;
  readerLevel: 'beginner' | 'intermediate' | 'advanced' | 'avid';
  memberSince: string;
  booksRead: number;
  reviewCount: number;
  wishlistCount: number;
}

// Cart types
export interface CartItem {
  bookId: string;
  book: Book;
  quantity: number;
  format: string;
  addedAt: string;
}

export interface Cart {
  items: CartItem[];
  subtotal: number;
  tax: number;
  discount: number;
  total: number;
}

// Order types
export type OrderStatus = 'processing' | 'shipped' | 'in_transit' | 'delivered' | 'cancelled';

export interface OrderItem {
  bookId: string;
  book: Book;
  quantity: number;
  format: string;
  price: number;
}

export interface TrackingUpdate {
  status: string;
  location: string;
  timestamp: string;
  description: string;
}

export interface Order {
  id: string;
  orderNumber: string;
  date: string;
  items: OrderItem[];
  subtotal: number;
  tax: number;
  discount: number;
  total: number;
  status: OrderStatus;
  trackingNumber?: string;
  trackingUrl?: string;
  trackingHistory?: TrackingUpdate[];
  estimatedDelivery?: string;
  deliveryAddress?: Address;
  paymentMethod?: string;
}

// Address types
export interface Address {
  id?: string;
  fullName: string;
  line1: string;
  line2?: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  phone: string;
  isDefault?: boolean;
  deliveryInstructions?: string;
}

// Review types
export interface Review {
  id: string;
  bookId: string;
  userId: string;
  userName: string;
  userAvatar?: string;
  rating: number;
  title?: string;
  content: string;
  verifiedPurchase: boolean;
  helpfulCount: number;
  notHelpfulCount: number;
  createdAt: string;
}

// Filter types
export interface FilterOption {
  id: string;
  label: string;
  value: string | number;
  count?: number;
}

export interface FilterGroup {
  id: string;
  label: string;
  type: 'checkbox' | 'radio' | 'range' | 'toggle';
  options?: FilterOption[];
  min?: number;
  max?: number;
}

export interface SearchFilters {
  categories?: string[];
  priceMin?: number;
  priceMax?: number;
  rating?: number;
  format?: string[];
  publisher?: string[];
  language?: string[];
}

// Support types
export interface SupportMessage {
  id: string;
  sender: 'user' | 'ai' | 'agent';
  senderName?: string;
  content: string;
  timestamp: string;
  attachments?: string[];
}

export interface SupportTicket {
  id: string;
  ticketNumber: string;
  subject: string;
  status: 'active' | 'resolved' | 'closed';
  messages: SupportMessage[];
  createdAt: string;
  resolvedAt?: string;
  assignedAgent?: string;
  orderId?: string;
}

// API Response types
export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
  message?: string;
}

export interface PaginatedResponse<T> {
  items: T[];
  total: number;
  page: number;
  pageSize: number;
  hasMore: boolean;
}

// Component prop types
export interface ButtonProps {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger' | 'success';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  loading?: boolean;
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  fullWidth?: boolean;
  onClick?: () => void;
  type?: 'button' | 'submit' | 'reset';
  className?: string;
  children: React.ReactNode;
}

export interface InputProps {
  type?: 'text' | 'email' | 'password' | 'number' | 'tel' | 'url' | 'search';
  label?: string;
  placeholder?: string;
  value: string;
  onChange: (value: string) => void;
  error?: string;
  helperText?: string;
  required?: boolean;
  disabled?: boolean;
  icon?: React.ReactNode;
  iconPosition?: 'left' | 'right';
  autoComplete?: string;
  className?: string;
}

export interface BookCardProps {
  book: Book;
  variant?: 'standard' | 'featured' | 'compact' | 'list';
  onAddToCart?: () => void;
  onWishlist?: () => void;
  onClick?: () => void;
  className?: string;
}

export interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  size?: 'sm' | 'md' | 'lg' | 'xl' | 'fullscreen';
  closeOnBackdrop?: boolean;
  closeOnEsc?: boolean;
  children: React.ReactNode;
  className?: string;
}

export interface ToastProps {
  message: string;
  type?: 'success' | 'error' | 'warning' | 'info';
  duration?: number;
  action?: {
    label: string;
    onClick: () => void;
  };
  onClose?: () => void;
}
