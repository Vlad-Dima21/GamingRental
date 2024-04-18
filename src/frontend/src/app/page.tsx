import Image from 'next/image';
import api from '@/helpers/api-helpers';
import Device from '@/models/Device';
import PageableResponse from '@/models/PageableResponse';
import Link from 'next/link';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

export default async function Home() {
  const response = await api.get('/devices');
  const data: PageableResponse<Device> = await response.json();
  const { items: devices, totalPages } = data;
  return (
    <div className='p-16 flex flex-col items-center'>
      <div className='space-y-5 max-w-5xl w-full'>
        <h1 className='text-2xl font-bold'>Devices</h1>
        <div className='w-full flex flex-col gap-5'>
          {devices.map((device) => (
            <Link
              key={device.deviceBaseName}
              href={`/device/${device.deviceBaseName}`}
            >
              <Card className='flex gap-2 p-5 justify-between'>
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
                  src={require('../assets/device-placeholder.png')}
                  width={200}
                  height={200}
                  objectFit='cover'
                  alt={device.deviceBaseName}
                />
              </Card>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
}
