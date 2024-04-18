import {
  ReadonlyURLSearchParams,
  notFound,
  redirect,
  usePathname,
} from 'next/navigation';
import { Button } from './ui/button';
import Link from 'next/link';

export default function PageableContainer<T>({
  searchParams,
  totalPages,
  baseUrl,
  children,
}: {
  searchParams: { [key: string]: string };
  totalPages: number;
  baseUrl: string;
  children: React.ReactNode;
}) {
  const page = searchParams.page ? parseInt(searchParams.page) : 1;

  if (page > totalPages) {
    redirect(
      `${baseUrl}?${new ReadonlyURLSearchParams({
        ...searchParams,
        page: totalPages.toString(),
      }).toString()}`
    );
  }
  return (
    <div className='w-full h-full flex flex-col gap-3 justify-between'>
      {children}
      <div className='flex w-full justify-between'>
        <Button
          asChild
          variant='outline'
          className={page == 1 ? 'invisible' : ''}
        >
          <Link
            href={`${baseUrl}?${new ReadonlyURLSearchParams({
              ...searchParams,
              page: (page - 1).toString(),
            })}`}
          >
            Previous page
          </Link>
        </Button>
        <Button asChild className={page == totalPages ? 'invisible' : ''}>
          <Link
            href={`${baseUrl}?${new ReadonlyURLSearchParams({
              ...searchParams,
              page: (page + 1).toString(),
            })}`}
          >
            Next page
          </Link>
        </Button>
      </div>
    </div>
  );
}
