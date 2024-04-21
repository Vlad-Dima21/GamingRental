import Image from 'next/image';
import api from '@/helpers/api-helpers';
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

interface PathParams {
  page: string;
  name: string;
  producer: string;
  year: string;
  sort: string;
}

export default async function Home({
  searchParams,
}: {
  searchParams: Partial<PathParams>;
}) {
  searchParams.name == '' && delete searchParams.name;
  searchParams.producer == '' && delete searchParams.producer;

  const response = await api.get(
    `/devices?${new ReadonlyURLSearchParams(searchParams).toString()}`
  );
  const data: PageableResponse<DeviceBase> = await response.json();
  const { items: devices, totalPages } = data;

  return (
    <div className='p-16 flex flex-col items-center'>
      <div className='space-y-5 max-w-5xl w-full'>
        <div className='flex justify-between items-center'>
          <h1 className='text-2xl font-bold'>Devices</h1>
          <form
            className='flex gap-5'
            action={async (formData: FormData) => {
              'use server';
              redirect(
                `/?${new ReadonlyURLSearchParams({
                  ...searchParams,
                  ...Object.fromEntries(formData),
                }).toString()}`
              );
            }}
          >
            <Input
              className='w-[300px]'
              placeholder='Search by name...'
              name='name'
              defaultValue={searchParams.name}
            />
            <Input
              className='w-[300px]'
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
                <Card className='flex gap-2 p-5 justify-between bg-white/70 backdrop-blur-sm hover:border-purple-600 hover:shadow-md'>
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
                    className='border border-gray-300 rounded-md'
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
