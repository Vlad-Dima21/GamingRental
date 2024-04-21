'use client';

import { Button } from './ui/button';
import Link from 'next/link';
import Image from 'next/image';
import { getSession, logout } from '@/helpers/auth';
import { Oswald } from 'next/font/google';
import { useContext } from 'react';
import { CartContext } from '@/contexts/cart-context';
import { ShoppingCart } from 'lucide-react';

const oswald = Oswald({ weight: '400', subsets: ['latin'] });

export const AppBar = () => {
  const { email, cart, setUserCart } = useContext(CartContext);
  return (
    <nav className='shadow-sm p-4'>
      <header className='flex justify-between max-w-5xl m-auto'>
        <h1 className='text-2xl'>
          <Link href={'/'} className='flex gap-2'>
            <Image
              src={require('@/assets/game-controller.png')}
              alt='Gaming Rental'
              width={32}
              height={32}
            />
            <span className={oswald.className}>Gaming Rental</span>
          </Link>
        </h1>
        {!email && (
          <div className='flex gap-2'>
            <Button asChild>
              <Link href={'/login'}>Sign In</Link>
            </Button>
            <Button asChild>
              <Link href={'/register'}>Sign Up</Link>
            </Button>
          </div>
        )}
        {email && (
          <div className='flex gap-2'>
            <Button asChild variant='link'>
              <Link href={'/rentals'}>{email}</Link>
            </Button>
            <Button
              asChild
              variant='outline'
              className='inline-flex items-center'
            >
              <Link href={'/cart'}>
                <div className='relative'>
                  <ShoppingCart />
                  {cart.length > 0 && (
                    <div className='absolute inline-flex items-center justify-center w-5 h-5 text-xs font-bold text-white bg-red-500 border-2 border-white rounded-full -top-2 -end-2 dark:border-gray-900'>
                      {cart.length}
                    </div>
                  )}
                </div>
              </Link>
            </Button>
            <form action={logout}>
              <Button variant='outline'>Sign Out</Button>
            </form>
          </div>
        )}
      </header>
    </nav>
  );
};
