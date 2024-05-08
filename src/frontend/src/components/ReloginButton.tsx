'use client';
import { logout } from '@/helpers/auth';
import { Button } from './ui/button';
import { useRouter } from 'next/navigation';

export default function ReloginButton() {
  const router = useRouter();
  return (
    <div className='flex justify-center items-center'>
      Click{' '}
      <Button
        variant='link'
        onClick={() => {
          logout();
          router.push('/login');
        }}
      >
        here
      </Button>{' '}
      to log in again.
    </div>
  );
}
