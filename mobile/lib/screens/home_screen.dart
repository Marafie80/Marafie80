import 'package:flutter/material.dart';
import '../core/models/book.dart';
import '../core/theme/app_colors.dart';
import '../core/theme/app_typography.dart';
import '../core/theme/app_spacing.dart';
import '../widgets/molecules/book_card.dart';
import '../widgets/atoms/app_button.dart';
import '../widgets/atoms/app_badge.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _selectedIndex = 0;

  // Mock data
  final List<Book> _mockBooks = [
    Book(
      id: '1',
      isbn: '978-1234567890',
      title: 'The Midnight Garden',
      author: 'Sarah Williams',
      coverUrl: 'https://via.placeholder.com/300x400/647CA4/FFFFFF?text=Midnight+Garden',
      rating: 4.9,
      reviewCount: 3421,
      price: 24.99,
      currency: 'USD',
      format: BookFormat.digital,
      category: 'Fiction',
      subcategory: 'Mystery',
      publisher: 'HarperCollins',
      publishedDate: '2025-03-01',
      pages: 384,
      language: 'English',
      description: 'A mesmerizing tale of love, loss, and mystery set in a forgotten English garden.',
      badge: BookBadge.featured,
    ),
    Book(
      id: '2',
      isbn: '978-1234567891',
      title: 'Ocean\'s Whisper',
      author: 'James Peterson',
      coverUrl: 'https://via.placeholder.com/300x400/1C3F68/FFFFFF?text=Ocean+Whisper',
      rating: 4.7,
      reviewCount: 1289,
      price: 19.99,
      currency: 'USD',
      format: BookFormat.paperback,
      category: 'Fiction',
      subcategory: 'Adventure',
      publisher: 'Penguin Random House',
      publishedDate: '2024-11-15',
      pages: 312,
      language: 'English',
      description: 'An epic adventure across the seven seas.',
    ),
    Book(
      id: '3',
      isbn: '978-1234567892',
      title: 'Digital Dreams',
      author: 'Lisa Chen',
      coverUrl: 'https://via.placeholder.com/300x400/AF924A/FFFFFF?text=Digital+Dreams',
      rating: 4.8,
      reviewCount: 2156,
      price: 19.99,
      currency: 'USD',
      format: BookFormat.audiobook,
      category: 'Science Fiction',
      publisher: 'Tor Books',
      publishedDate: '2025-01-10',
      pages: 456,
      language: 'English',
      description: 'A journey through virtual reality and the future of humanity.',
      badge: BookBadge.bestseller,
    ),
    Book(
      id: '4',
      isbn: '978-1234567893',
      title: 'The Last Symphony',
      author: 'Michael Brown',
      coverUrl: 'https://via.placeholder.com/300x400/6B8E6B/FFFFFF?text=Last+Symphony',
      rating: 4.6,
      reviewCount: 987,
      price: 22.99,
      currency: 'USD',
      format: BookFormat.digital,
      category: 'Fiction',
      subcategory: 'Historical',
      publisher: 'Simon & Schuster',
      publishedDate: '2024-09-20',
      pages: 428,
      language: 'English',
      description: 'A historical fiction set in Vienna during the golden age of classical music.',
    ),
    Book(
      id: '5',
      isbn: '978-1234567894',
      title: 'Starlight Chronicles',
      author: 'Emma Davis',
      coverUrl: 'https://via.placeholder.com/300x400/C85C5C/FFFFFF?text=Starlight',
      rating: 4.9,
      reviewCount: 4521,
      price: 0,
      currency: 'USD',
      format: BookFormat.digital,
      category: 'Fantasy',
      publisher: 'Orbit Books',
      publishedDate: '2024-06-01',
      pages: 512,
      language: 'English',
      description: 'An epic fantasy saga spanning multiple worlds.',
      isFree: true,
      badge: BookBadge.newRelease,
    ),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: _buildAppBar(),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildHeroSection(),
            const SizedBox(height: AppSpacing.lg),
            _buildRecommendedSection(),
            const SizedBox(height: AppSpacing.lg),
            _buildFreeBooksSection(),
            const SizedBox(height: AppSpacing.lg),
            _buildBookOfYearSection(),
            const SizedBox(height: AppSpacing.lg),
            _buildCategoriesSection(),
            const SizedBox(height: AppSpacing.xl),
          ],
        ),
      ),
      bottomNavigationBar: _buildBottomNav(),
    );
  }

  PreferredSizeWidget _buildAppBar() {
    return AppBar(
      title: Row(
        children: [
          const Text('📚', style: TextStyle(fontSize: 24)),
          const SizedBox(width: AppSpacing.sm),
          Text('BookMart', style: AppTypography.h3(color: AppColors.primaryDarkBlue)),
        ],
      ),
      actions: [
        IconButton(
          icon: const BadgedWidget(
            badge: AppBadge.count(count: 3),
            child: Icon(Icons.shopping_cart_outlined),
          ),
          onPressed: () {
            // Navigate to cart
          },
        ),
        IconButton(
          icon: const Icon(Icons.person_outline),
          onPressed: () {
            // Navigate to profile
          },
        ),
        const SizedBox(width: AppSpacing.sm),
      ],
    );
  }

  Widget _buildHeroSection() {
    final featuredBook = _mockBooks[0];
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
      child: BookCard(
        book: featuredBook,
        variant: BookCardVariant.featured,
        onTap: () => _handleBookTap(featuredBook),
        onAddToCart: () => _handleAddToCart(featuredBook),
      ),
    );
  }

  Widget _buildRecommendedSection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                '📖 Recommended For You',
                style: AppTypography.h3(color: AppColors.primaryDarkBlue),
              ),
              AppButton(
                text: 'View All',
                variant: AppButtonVariant.ghost,
                size: AppButtonSize.small,
                onPressed: () {},
              ),
            ],
          ),
        ),
        const SizedBox(height: AppSpacing.md),
        SizedBox(
          height: 280,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
            itemCount: _mockBooks.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.only(right: AppSpacing.md),
                child: BookCard(
                  book: _mockBooks[index],
                  variant: BookCardVariant.standard,
                  onTap: () => _handleBookTap(_mockBooks[index]),
                  onAddToCart: () => _handleAddToCart(_mockBooks[index]),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  Widget _buildFreeBooksSection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                '🎁 Free Books',
                style: AppTypography.h3(color: AppColors.primaryDarkBlue),
              ),
              AppButton(
                text: 'View All',
                variant: AppButtonVariant.ghost,
                size: AppButtonSize.small,
                onPressed: () {},
              ),
            ],
          ),
        ),
        const SizedBox(height: AppSpacing.md),
        SizedBox(
          height: 280,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
            itemCount: _mockBooks.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.only(right: AppSpacing.md),
                child: BookCard(
                  book: _mockBooks[index],
                  variant: BookCardVariant.standard,
                  onTap: () => _handleBookTap(_mockBooks[index]),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  Widget _buildBookOfYearSection() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                '🏆 Book of the Year 2026',
                style: AppTypography.h3(color: AppColors.primaryDarkBlue),
              ),
              AppButton(
                text: 'View All',
                variant: AppButtonVariant.ghost,
                size: AppButtonSize.small,
                onPressed: () {},
              ),
            ],
          ),
        ),
        const SizedBox(height: AppSpacing.md),
        SizedBox(
          height: 280,
          child: ListView.builder(
            scrollDirection: Axis.horizontal,
            padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
            itemCount: _mockBooks.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.only(right: AppSpacing.md),
                child: BookCard(
                  book: _mockBooks[index],
                  variant: BookCardVariant.standard,
                  onTap: () => _handleBookTap(_mockBooks[index]),
                  onAddToCart: () => _handleAddToCart(_mockBooks[index]),
                ),
              );
            },
          ),
        ),
      ],
    );
  }

  Widget _buildCategoriesSection() {
    final categories = [
      'Fiction',
      'Non-Fiction',
      'Mystery',
      'Romance',
      'Sci-Fi',
      'Fantasy',
      'Biography',
      'Self-Help',
    ];

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: AppSpacing.screenPadding),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            '📚 Browse by Category',
            style: AppTypography.h3(color: AppColors.primaryDarkBlue),
          ),
          const SizedBox(height: AppSpacing.md),
          Wrap(
            spacing: AppSpacing.sm,
            runSpacing: AppSpacing.sm,
            children: categories.map((category) {
              return OutlinedButton(
                onPressed: () {
                  // Navigate to category
                },
                style: OutlinedButton.styleFrom(
                  side: const BorderSide(color: AppColors.neutralLightGray, width: 2),
                  foregroundColor: AppColors.primaryDarkBlue,
                  padding: const EdgeInsets.symmetric(
                    horizontal: AppSpacing.lg,
                    vertical: AppSpacing.md,
                  ),
                ),
                child: Text(category),
              );
            }).toList(),
          ),
        ],
      ),
    );
  }

  Widget _buildBottomNav() {
    return BottomNavigationBar(
      currentIndex: _selectedIndex,
      onTap: (index) {
        setState(() {
          _selectedIndex = index;
        });
      },
      type: BottomNavigationBarType.fixed,
      items: const [
        BottomNavigationBarItem(
          icon: Icon(Icons.home_outlined),
          activeIcon: Icon(Icons.home),
          label: 'Home',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.search_outlined),
          activeIcon: Icon(Icons.search),
          label: 'Search',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.shopping_cart_outlined),
          activeIcon: Icon(Icons.shopping_cart),
          label: 'Cart',
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.person_outline),
          activeIcon: Icon(Icons.person),
          label: 'Profile',
        ),
      ],
    );
  }

  void _handleBookTap(Book book) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Viewing: ${book.title}')),
    );
    // TODO: Navigate to book details
  }

  void _handleAddToCart(Book book) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Added "${book.title}" to cart')),
    );
    // TODO: Add to cart logic
  }
}
