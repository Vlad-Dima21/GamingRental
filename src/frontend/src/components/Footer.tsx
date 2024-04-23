import Link from 'next/link';
import { Button } from './ui/button';
import { getSession } from '@/helpers/auth';

export default async function Footer() {
  const session = await getSession();
  return (
    <div className='w-full bg-gradient-to-r from-gray-800 to-purple-300'>
      <div
        className={`max-w-5xl m-auto w-1/2 grid ${
          !!session ? 'grid-cols-3' : 'grid-cols-2'
        } justify-items-center gap-3 md:gap-10`}
      >
        <Button
          asChild
          variant='link'
          className='text-white hover:text-orange-400'
        >
          <Link href={'/'}>Home</Link>
        </Button>
        {session && (
          <Button
            asChild
            variant='link'
            className='text-white hover:text-orange-400'
          >
            <Link href={'/rentals'}>Rentals</Link>
          </Button>
        )}
        <Button
          asChild
          variant='link'
          className='text-white hover:text-orange-400'
        >
          <a href='https://github.com/Vlad-Dima21/GamingRental' target='_blank'>
            GitHub
          </a>
        </Button>
      </div>
    </div>
  );
}
