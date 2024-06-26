import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';
import { AppBar } from '@/components/AppBar';
import { getSession } from '@/helpers/auth';
import CartProvider from '@/components/CartProvider';
import Footer from '@/components/Footer';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: {
    default: 'Gaming Rental',
    template: `%s | Gaming Rental`,
  },
  description: 'Rent games for your favorite console',
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const session = await getSession();

  return (
    <html lang='en'>
      <body className={inter.className}>
        <main className='flex flex-col min-h-screen bg-gradient-to-t from-indigo-100  to-white'>
          <CartProvider session={session}>
            <AppBar />
            <div className='p-4 overflow-auto'>{children}</div>
          </CartProvider>
        </main>
        <Footer />
      </body>
    </html>
  );
}
