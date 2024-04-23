import Image from 'next/image';
import { get } from '@/helpers/api-helpers';
import DeviceBase from '@/models/DeviceBase';
import PageableResponse from '@/models/PageableResponse';
import Link from 'next/link';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import {
  ReadonlyURLSearchParams,
  redirect,
  useSearchParams,
} from 'next/navigation';
import PageableContainer from '@/components/PageableContainer';
import { Input } from '@/components/ui/input';
import strippedUrlSearchParams from '@/helpers/search-params-helper';
import SortButton from '@/components/SortButton';
import SortDirection from '@/helpers/sort-directions';

interface SearchParams {
  page: string;
  name: string;
  producer: string;
  year: string;
  sort: string;
}

export default async function Home({
  searchParams,
}: {
  searchParams: Partial<SearchParams>;
}) {
  searchParams.name == '' && delete searchParams.name;
  searchParams.producer == '' && delete searchParams.producer;
  searchParams.sort == '' && delete searchParams.sort;

  const response = await get(
    `/devices?${new ReadonlyURLSearchParams(searchParams).toString()}`
  );
  const data: PageableResponse<DeviceBase> = await response.json();
  const { items: devices, totalPages } = data;

  return (
    <div className='p-4 sm:p-8 md:p-16 flex flex-col items-center'>
      <div className='space-y-5 max-w-5xl w-full'>
        <div className='flex flex-col lg:flex-row justify-between items-center gap-5'>
          <h1 className='text-2xl font-bold'>Devices</h1>
          <form
            className='flex gap-5 flex-col lg:flex-row w-full lg:w-auto max-w-5xl'
            action={async (formData: FormData) => {
              'use server';
              redirect(`/?${strippedUrlSearchParams(formData).toString()}`);
            }}
          >
            <div className='flex gap-2 w-full lg:w-auto'>
              <SortButton searchParams={searchParams} baseUrl='/'>
                Name
              </SortButton>
              <Input
                className='flex-grow lg:flex-initial md:w-[230px]'
                placeholder='Search by name...'
                name='name'
                defaultValue={searchParams.name}
              />
            </div>
            <Input
              className='w-full'
              placeholder='Search by producer...'
              name='producer'
              defaultValue={searchParams.producer}
            />
            <input type='submit' hidden />
          </form>
        </div>
        <PageableContainer
          searchParams={searchParams}
          totalPages={totalPages}
          baseUrl='/'
        >
          <div className='w-full flex flex-col gap-5'>
            {devices.map((device) => (
              <Link
                key={device.deviceBaseName}
                href={`/device/${device.deviceBaseId}`}
              >
                <Card className='flex flex-col md:flex-row gap-2 p-5 justify-between bg-white/70 backdrop-blur-sm hover:border-purple-600 hover:shadow-md'>
                  <div>
                    <CardHeader>
                      <CardTitle>{device.deviceBaseName}</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <p>{device.deviceBaseProducer}</p>
                      <p>{device.deviceBaseYearOfRelease}</p>
                    </CardContent>
                  </div>
                  <Image
                    src={device.deviceBaseImageUrl}
                    width={150}
                    height={150}
                    objectFit='cover'
                    className='border border-gray-300 rounded-md self-center md:self-auto'
                    alt={device.deviceBaseName}
                  />
                </Card>
              </Link>
            ))}
            {devices.length === 0 && (
              <p className='self-center mt-20'>No devices found</p>
            )}
          </div>
        </PageableContainer>
      </div>
    </div>
  );
}
