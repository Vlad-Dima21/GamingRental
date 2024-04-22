'use client';

import { clientGet } from '@/helpers/api-helpers';
import DeviceBase from '@/models/DeviceBase';
import Device from '@/models/Device';
import { useContext, useEffect, useMemo, useState } from 'react';
import { ServerResponse } from '@/helpers/server-response';
import LoadingContainer from '@/components/LoadingContainer';
import { Card } from '@/components/ui/card';
import { CartContext } from '@/contexts/cart-context';
import DeviceCard from '@/components/DeviceCard';
import { Separator } from '@/components/ui/separator';
import GameCard from '@/components/GameCard';

// export const metadata: Metadata = {
//   title: 'Cart',
// };

export default function CartPage() {
  const [devices, setDevices] = useState<DeviceBase[] | null>(null);
  const { cart } = useContext(CartContext);

  useEffect(() => {
    const fetchDevices = async () => {
      try {
        const getDevices = clientGet.bind(null, '/devices/all');
        const { ok, status, body }: ServerResponse<DeviceBase[]> =
          await getDevices().then((res) => JSON.parse(res));
        if (!ok) {
          return console.error(
            `Failed to fetch devices: ${status}, err: ${body}`
          );
        }
        setDevices(body as DeviceBase[]);
      } catch (error) {
        console.error(error);
      }
    };
    fetchDevices();
  }, []);
  return (
    <div className='flex flex-col max-w-5xl gap-10 mx-auto w-full py-16'>
      <div>
        <h1 className='text-2xl font-bold'>Cart</h1>
        <Separator />
      </div>
      <LoadingContainer
        className='flex items-center justify-center'
        loading={devices == null}
      >
        {!!cart.length && (
          <Card className='p-8 w-full flex flex-col gap-6 bg-slate-500/10 divide-y-2 divide-gray-300'>
            {devices
              ?.filter((device) =>
                cart.some((item) => item.deviceBaseId == device.deviceBaseId)
              )
              .map((device) => (
                <div key={device.deviceBaseId} className='pt-2'>
                  <h2 className='text-xl font-bold'>{device.deviceBaseName}</h2>
                  <div className='grid grid-cols-3 gap-4'>
                    {device.deviceBaseUnits
                      .filter((dbu) =>
                        cart.some(
                          (item) =>
                            item.deviceBaseId == device.deviceBaseId &&
                            item.deviceUnits?.includes(dbu.deviceId)
                        )
                      )
                      .map((dbu, index) => (
                        <DeviceCard
                          key={dbu.deviceId}
                          device={dbu}
                          index={index}
                        />
                      ))}
                  </div>
                  <h3 className='text-lg font-bold'>
                    Maybe try out some games
                  </h3>
                  <div className='flex items-center gap-4 overflow-auto py-2'>
                    {device.deviceBaseGameCopies
                      .filter((game) => game.available)
                      .filter(
                        (game, idx, arr) =>
                          arr.findIndex(
                            (g) => g.gameBase.gameName == game.gameBase.gameName
                          ) === idx
                      )
                      .map((game) => (
                        <GameCard
                          key={game.gameId}
                          className='min-w-[200px]'
                          device={device}
                          gameCopy={game}
                        />
                      ))}
                  </div>
                </div>
              ))}
          </Card>
        )}
        {!cart.length && <div className='text-lg font-bold'>No items</div>}
      </LoadingContainer>
    </div>
  );
}
