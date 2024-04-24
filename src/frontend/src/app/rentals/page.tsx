import PageableContainer from '@/components/PageableContainer';
import SortButton from '@/components/SortButton';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { get } from '@/helpers/api-helpers';
import { getSession } from '@/helpers/auth';
import strippedUrlSearchParams from '@/helpers/search-params-helper';
import PageableResponse from '@/models/PageableResponse';
import Rental from '@/models/Rental';
import { Metadata } from 'next';
import { ReadonlyURLSearchParams, redirect } from 'next/navigation';

export const metadata: Metadata = {
  title: 'Your rentals',
};

interface SearchParams {
  page: string;
  deviceName: string;
  returned: string;
  pastDue: string;
  sort: string;
}

export default async function RentalsPage({
  searchParams,
}: {
  searchParams: Partial<SearchParams>;
}) {
  const response = await get(
    `/rentals?${new ReadonlyURLSearchParams(searchParams).toString()}`
  );
  let data: PageableResponse<Rental>;
  if (!response.ok) {
    data = { items: [], totalPages: 0 };
  } else {
    data = await response.json();
  }
  return (
    <div className='p-4 sm:p-8 md:p-16 flex flex-col items-center'>
      <div className='space-y-5 max-w-5xl w-full'>
        <div className='flex flex-col lg:flex-row justify-between items-center gap-5'>
          <h1 className='text-2xl font-bold'>Your rentals</h1>
          <form
            className='flex gap-2 flex-col lg:flex-row w-full lg:w-auto max-w-5xl'
            action={async (formData: FormData) => {
              'use server';
              redirect(
                `/rentals?${strippedUrlSearchParams(formData).toString()}`
              );
            }}
          >
            <div className='flex gap-2 w-full lg:w-auto'>
              <SortButton searchParams={searchParams} baseUrl='/rentals'>
                Return date
              </SortButton>
              <Input
                className='flex-grow lg:flex-initial md:w-[230px]'
                placeholder='Search by name...'
                name='deviceName'
                defaultValue={searchParams.deviceName}
              />
            </div>
            <input type='submit' hidden />
          </form>
        </div>
        <PageableContainer
          searchParams={searchParams}
          totalPages={data.totalPages}
          baseUrl='/rentals'
        >
          <div className='w-full flex flex-col gap-5'>
            {data.items.map((rental) => (
              <Card
                key={new Date(rental.rentalDueDate).getTime()}
                className='flex flex-col md:flex-row gap-2 p-5 justify-between bg-white/70 backdrop-blur-sm hover:shadow-md'
              >
                <div>
                  <CardHeader>
                    <CardTitle className='flex items-end gap-2'>
                      <span
                        className={
                          new Date(rental.rentalDueDate) <
                          (rental.rentalReturnDate
                            ? new Date(rental.rentalReturnDate)
                            : new Date())
                            ? 'text-red-500'
                            : ''
                        }
                      >
                        {rental.rentalDevice.deviceName}
                      </span>
                      <span className='text-sm text-gray-600'>
                        {new Date(rental.rentalDueDate).toLocaleDateString()}
                      </span>
                    </CardTitle>
                  </CardHeader>
                  <CardContent>
                    <p>
                      {rental.rentalGames
                        .map((g) => g.gameBase.gameName)
                        .join()}
                    </p>
                    {rental.rentalReturnDate && (
                      <p>
                        {new Date(rental.rentalReturnDate).toLocaleDateString()}
                      </p>
                    )}
                  </CardContent>
                </div>
              </Card>
            ))}
          </div>
        </PageableContainer>
      </div>
    </div>
  );
}
