'use client';

import { Button } from '@/components/ui/button';

export default function ErrorPage({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  return (
    <div className='max-w-5xl m-auto h-full w-full flex flex-col items-center justify-center'>
      <h1 className='text-4xl font-bold'>Error</h1>
      <p className='text-2xl italic'>{error.message}</p>
      <Button variant='link' onClick={reset}>
        Try again
      </Button>
    </div>
  );
}
