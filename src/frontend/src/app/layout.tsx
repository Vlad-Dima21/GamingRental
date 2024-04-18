import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';
import { AppBar } from '@/components/AppBar';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: {
    default: 'Gaming Rental',
    template: `%s | Gaming Rental`,
  },
  description: 'Rent games for your favorite console',
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang='en'>
      <body className={inter.className}>
        <main className='flex flex-col min-h-screen bg-gradient-to-t from-indigo-100  to-white'>
          <AppBar />
          {children}
        </main>
      </body>
    </html>
  );
}
