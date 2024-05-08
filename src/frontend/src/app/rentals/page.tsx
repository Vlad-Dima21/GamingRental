import GameCard from '@/components/GameCard';
import PageableContainer from '@/components/PageableContainer';
import ReloginButton from '@/components/ReloginButton';
import SortButton from '@/components/SortButton';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { ScrollArea } from '@/components/ui/scroll-area';
import { get, patch } from '@/helpers/api-helpers';
import { getSession, logout } from '@/helpers/auth';
import strippedUrlSearchParams from '@/helpers/search-params-helper';
import PageableResponse from '@/models/PageableResponse';
import Rental from '@/models/Rental';
import { Metadata } from 'next';
import { revalidatePath } from 'next/cache';
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
  let shouldLogin = false;
  let data: PageableResponse<Rental>;
  if (!response.ok) {
    data = { items: [], totalPages: 0 };
    shouldLogin = response.status === 401;
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
            {data.items
              .map((rental) => ({
                ...rental,
                isPastDue:
                  new Date(rental.rentalDueDate) <
                  (rental.rentalReturnDate
                    ? new Date(rental.rentalReturnDate)
                    : new Date()),
                isReturned: !!rental.rentalReturnDate,
              }))
              .map((rental) => (
                <Card
                  key={new Date(rental.rentalDueDate).getTime()}
                  className='flex flex-col md:flex-row gap-2 p-5 justify-between bg-white/70 backdrop-blur-sm hover:shadow-md'
                >
                  <div className='w-full'>
                    <CardHeader>
                      <CardTitle className='flex flex-col gap-2'>
                        <div className='flex items-end gap-2'>
                          <span>{rental.rentalDevice.deviceName}</span>
                          <span className='text-sm text-gray-600'>
                            due{' '}
                            {new Date(
                              rental.rentalDueDate
                            ).toLocaleDateString()}
                          </span>
                        </div>
                        <Badge
                          variant={rental.isReturned ? 'default' : 'outline'}
                          className={`w-fit space-x-2 ${
                            rental.isPastDue
                              ? 'bg-red-500 text-white'
                              : rental.isReturned
                              ? 'bg-green-400 text-white'
                              : ''
                          }`}
                        >
                          <span>
                            {rental.isReturned ? 'Returned' : 'On rent'}
                          </span>
                          {rental.rentalReturnDate && (
                            <span>
                              {new Date(
                                rental.rentalReturnDate
                              ).toLocaleDateString()}
                            </span>
                          )}
                        </Badge>
                      </CardTitle>
                    </CardHeader>
                    <CardContent className='w-full flex flex-col md:flex-row justify-between gap-5'>
                      <div className='flex-grow flex justify-between overflow-auto'>
                        <div className='grow flex flex-col md:flex-row gap-2 md:py-2'>
                          {rental.rentalGames.map((g) => (
                            <GameCard
                              key={g.gameId}
                              gameCopy={g}
                              readOnly={true}
                              className='grow'
                            />
                          ))}
                        </div>
                      </div>
                      {!rental.isReturned && (
                        <form
                          action={async (formData: FormData) => {
                            'use server';
                            const rentalId: number = parseInt(
                              formData.get('rentalId') as string
                            );
                            await patch(`/rentals/return/${rentalId}`);
                            revalidatePath('/rentals');
                          }}
                          className='flex-shrink md:self-end md:m-2'
                        >
                          <input
                            hidden
                            name='rentalId'
                            value={rental.rentalId}
                          />
                          <Button
                            variant={rental.isReturned ? 'outline' : 'default'}
                            className='w-full'
                          >
                            Return
                          </Button>
                        </form>
                      )}
                    </CardContent>
                  </div>
                </Card>
              ))}
          </div>
        </PageableContainer>
        {shouldLogin && <ReloginButton />}
      </div>
    </div>
  );
}
