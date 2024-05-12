'use client';

import DeviceCard from '@/components/DeviceCard';
import GameCard from '@/components/GameCard';
import LoadingContainer from '@/components/LoadingContainer';
import { Card } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import { Slider } from '@/components/ui/slider';
import { CartContext, CartItem } from '@/contexts/cart-context';
import { clientGet } from '@/helpers/api-helpers';
import { EntityException, ServerResponse } from '@/helpers/server-response';
import DeviceBase from '@/models/DeviceBase';
import { useContext, useEffect, useState } from 'react';
import { Button } from '@/components/ui/button';
import { ActionResult } from './page';
import { useRouter } from 'next/navigation';
import { ScrollArea } from '@radix-ui/react-scroll-area';
import { ScrollBar } from '@/components/ui/scroll-area';

interface Props {
  rentalAction: (cartItem: CartItem) => Promise<ActionResult | undefined>;
}

export default function CartPageContent({ rentalAction }: Props) {
  const router = useRouter();
  const [devices, setDevices] = useState<DeviceBase[] | null>(null);
  const { cart, setUserCart } = useContext(CartContext);
  const [submitState, setSubmitState] = useState<{
    loading: boolean;
    error: EntityException | undefined;
  }>({ loading: false, error: void 0 });

  const handleSubmit = async () => {
    if (!cart) {
      return;
    }
    const action = rentalAction.bind(null, cart);
    setSubmitState({ loading: true, error: void 0 });
    const result = await action();
    if (result?.success) {
      setUserCart(void 0);
      router.push('/rentals');
    } else {
      setSubmitState({ loading: false, error: result?.error });
    }
  };

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
        {!!cart && (
          <Card className='p-8 w-full flex flex-col md:flex-row gap-6 bg-slate-500/10'>
            <div className='flex flex-col w-full gap-6 md:w-3/4'>
              {devices
                ?.filter((device) => cart.deviceBaseId == device.deviceBaseId)
                .map((device) => (
                  <div key={device.deviceBaseId} className='pt-2 space-y-4'>
                    <h2 className='text-xl font-bold'>
                      {device.deviceBaseName}
                    </h2>
                    {device.deviceBaseUnits
                      .filter(
                        (dbu) =>
                          cart.deviceBaseId == device.deviceBaseId &&
                          cart.deviceUnitId == dbu.deviceId
                      )
                      .map((dbu, index) => (
                        <DeviceCard
                          key={dbu.deviceId}
                          className='max-w-[50%]'
                          device={dbu}
                          index={index}
                        />
                      ))}
                    {!!device.deviceBaseGameCopies.length && (
                      <>
                        <h3 className='text-lg font-bold'>
                          Games available for this device
                        </h3>
                        <div className='overflow-auto'>
                          <div className='flex items-center gap-4 pb-2'>
                            {device.deviceBaseGameCopies
                              .filter((game) => game.available)
                              .filter(
                                (game, idx, arr) =>
                                  arr.findIndex(
                                    (g) =>
                                      g.gameBase.gameName ==
                                      game.gameBase.gameName
                                  ) === idx
                              )
                              .map((game) => (
                                <GameCard
                                  key={game.gameId}
                                  className='w-fit'
                                  gameCopy={game}
                                />
                              ))}
                          </div>
                        </div>
                      </>
                    )}
                  </div>
                ))}
              <div className='flex flex-col md:flex-row gap-3 md:gap-10'>
                <span className='whitespace-nowrap italic'>
                  Number of days
                  {!!cart.numberOfDays && (
                    <>
                      :{' '}
                      <span className='not-italic font-bold'>
                        {cart.numberOfDays}
                      </span>
                    </>
                  )}
                </span>
                <Slider
                  defaultValue={[30]}
                  min={30}
                  max={90}
                  value={[cart.numberOfDays]}
                  onValueChange={(value) =>
                    setUserCart({
                      ...cart,
                      numberOfDays: value[0],
                    })
                  }
                />
              </div>
            </div>
            <div className='flex-auto flex flex-col min-w-[100px] md:w-1/4'>
              <div className='flex justify-between'>
                <span className='text-lg'>Total</span>
                <span className='text-lg font-bold'>
                  $
                  {(cart.gameCopiesIds?.length ?? 0) * 5 * cart.numberOfDays +
                    cart.numberOfDays * 20}
                </span>
              </div>
              <Button
                disabled={submitState.loading}
                onClick={handleSubmit}
                className='w-full'
              >
                Rent
              </Button>
              <span className='text-red-500 text-sm'>
                {submitState.error?.details ?? submitState.error?.message}
              </span>
            </div>
          </Card>
        )}
        {!cart && <div className='text-lg font-bold'>No items</div>}
      </LoadingContainer>
    </div>
  );
}
