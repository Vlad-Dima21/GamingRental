import { redirect } from 'next/navigation';
import { Button } from './ui/button';
import Link from 'next/link';
import Image from 'next/image';

export const AppBar = () => {
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
            <span>Gaming Rental</span>
          </Link>
        </h1>
        <div className='flex gap-2'>
          <Button asChild>
            <Link href={'/login'}>Sign In</Link>
          </Button>
          <Button asChild>
            <Link href={'/register'}>Sign Up</Link>
          </Button>
        </div>
      </header>
    </nav>
  );
};
