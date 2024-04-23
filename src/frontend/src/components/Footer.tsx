import Link from 'next/link';
import { Button } from './ui/button';
import { getSession } from '@/helpers/auth';

function FooterLink({
  children,
}: {
  children: React.ReactElement<typeof Link>;
}) {
  return (
    <Button asChild variant='link' className='text-white hover:text-indigo-100'>
      {children}
    </Button>
  );
}

export default async function Footer() {
  const session = await getSession();
  return (
    <div className='w-full bg-gradient-to-r from-gray-800 to-purple-300'>
      <div
        className={`max-w-5xl m-auto w-1/2 grid ${
          !!session ? 'grid-cols-3' : 'grid-cols-2'
        } justify-items-center gap-3 md:gap-10`}
      >
        <FooterLink>
          <Link href={'/'}>Home</Link>
        </FooterLink>
        {session && (
          <FooterLink>
            <Link href={'/rentals'}>Rentals</Link>
          </FooterLink>
        )}
        <FooterLink>
          <a href='https://github.com/Vlad-Dima21/GamingRental' target='_blank'>
            GitHub
          </a>
        </FooterLink>
      </div>
    </div>
  );
}
