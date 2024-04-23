import DeviceCard from '@/components/DeviceCard';
import { get } from '@/helpers/api-helpers';
import Device from '@/models/Device';
import DeviceBase from '@/models/DeviceBase';
import { Metadata } from 'next';

async function getDeviceAndDeviceBase(
  id: number
): Promise<[DeviceBase, Device[]]> {
  return await Promise.all([
    get(`/devices/${id}`).then((res) => res.json()),
    get(`/units/of-id/${id}`).then((res) => res.json()),
  ]);
}

export async function generateMetadata({
  params: { id },
}: {
  params: { id: number };
}) {
  const [deviceBase, devices] = await getDeviceAndDeviceBase(id);
  return {
    title: deviceBase.deviceBaseName,
  };
}

export default async function DevicePage({
  params: { id },
}: {
  params: { id: number };
}) {
  const [deviceBase, devices] = await getDeviceAndDeviceBase(id);
  return (
    <div className='flex flex-col max-w-5xl gap-10 mx-auto w-full py-16'>
      <div className='flex items-end gap-2'>
        <h1 className='text-2xl font-bold'>{deviceBase.deviceBaseName}</h1>
        <span className='text-xl opacity-70'>units</span>
      </div>
      <div className='grid grid-cols-1 md:grid-cols-3 gap-4'>
        {devices.map((device, index) => (
          <DeviceCard key={device.deviceId} device={device} index={index} />
        ))}
        {devices.length === 0 && (
          <div className='text-lg font-bold col-span-3 text-center'>
            No units available at this time. Please check back later.
          </div>
        )}
      </div>
    </div>
  );
}
