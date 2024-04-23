import SortDirection from '@/helpers/sort-directions';
import React from 'react';
import { Button } from './ui/button';
import Link from 'next/link';
import strippedUrlSearchParams from '@/helpers/search-params-helper';
import { ReadonlyURLSearchParams } from 'next/navigation';
import {
  ArrowDownAZ,
  ArrowDownWideNarrow,
  ArrowUpAZ,
  ArrowUpWideNarrow,
} from 'lucide-react';

interface SearchParamsWithSort {
  sort: string | undefined;
}

export default function SortButton({
  children,
  searchParams,
  baseUrl,
}: {
  children: React.ReactNode;
  searchParams: Partial<SearchParamsWithSort>;
  baseUrl: string;
}) {
  const sort = SortDirection.opposite(searchParams.sort),
    ArrowComp =
      sort == SortDirection.Ascending ? ArrowDownWideNarrow : ArrowUpWideNarrow;
  return (
    <>
      <input type='hidden' name='sort' value={searchParams.sort} />
      <Button asChild variant={'outline'} className='gap-2'>
        <Link
          href={`${baseUrl}?${new ReadonlyURLSearchParams({
            ...searchParams,
            sort: sort,
          }).toString()}`}
        >
          {children}
          <ArrowComp size={20} />
        </Link>
      </Button>
    </>
  );
}
