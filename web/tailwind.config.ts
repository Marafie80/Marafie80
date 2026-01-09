import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          beige: '#CAC6B1',
          'dark-blue': '#1C3F68',
          'blue-gray': '#647CA4',
          gold: '#AF924A',
          yellow: '#EEDEAA',
        },
        semantic: {
          success: '#6B8E6B',
          error: '#C85C5C',
          info: '#647CA4',
          warning: '#D4A574',
        },
        neutral: {
          white: '#FFFFFF',
          'light-gray': '#E8E5DC',
          'medium-gray': '#9B9B9B',
          'dark-gray': '#4A4A4A',
          black: '#1A1A1A',
        },
      },
      fontFamily: {
        cairo: ['Cairo', 'sans-serif'],
        montserrat: ['Montserrat', 'sans-serif'],
        lato: ['Lato', 'sans-serif'],
      },
      fontSize: {
        hero: ['3rem', { lineHeight: '1.2', fontWeight: '700' }],
        h1: ['2.25rem', { lineHeight: '1.2', fontWeight: '700' }],
        h2: ['1.75rem', { lineHeight: '1.2', fontWeight: '600' }],
        h3: ['1.5rem', { lineHeight: '1.2', fontWeight: '600' }],
        h4: ['1.25rem', { lineHeight: '1.2', fontWeight: '500' }],
        'body-lg': ['1.125rem', { lineHeight: '1.6', fontWeight: '400' }],
        body: ['1rem', { lineHeight: '1.6', fontWeight: '400' }],
        'body-sm': ['0.875rem', { lineHeight: '1.6', fontWeight: '400' }],
        caption: ['0.75rem', { lineHeight: '1.4', fontWeight: '400' }],
      },
      spacing: {
        xs: '0.25rem', // 4px
        sm: '0.5rem', // 8px
        md: '1rem', // 16px
        lg: '1.5rem', // 24px
        xl: '2rem', // 32px
        '2xl': '3rem', // 48px
        '3xl': '4rem', // 64px
      },
      borderRadius: {
        sm: '4px',
        DEFAULT: '8px',
        lg: '12px',
        xl: '16px',
        full: '9999px',
      },
      boxShadow: {
        sm: '0 1px 3px rgba(28, 63, 104, 0.08)',
        DEFAULT: '0 4px 8px rgba(28, 63, 104, 0.12)',
        lg: '0 8px 16px rgba(28, 63, 104, 0.16)',
        xl: '0 12px 24px rgba(28, 63, 104, 0.20)',
      },
      transitionTimingFunction: {
        'standard': 'cubic-bezier(0.4, 0.0, 0.2, 1)',
        'enter': 'cubic-bezier(0.0, 0.0, 0.2, 1)',
        'exit': 'cubic-bezier(0.4, 0.0, 1, 1)',
      },
      transitionDuration: {
        'micro': '100ms',
        'fast': '150ms',
        'normal': '200ms',
        'smooth': '250ms',
        'slow': '300ms',
      },
      screens: {
        'xs': '0px',
        'sm': '640px',
        'md': '768px',
        'lg': '1024px',
        'xl': '1280px',
        '2xl': '1440px',
      },
    },
  },
  plugins: [],
}

export default config
