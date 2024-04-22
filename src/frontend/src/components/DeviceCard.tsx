'use client';

import { HTMLAttributes, useContext } from 'react';
import { Card } from './ui/card';
import Device from '@/models/Device';
import { Badge } from './ui/badge';
import { Gamepad } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { CartContext } from '@/contexts/cart-context';

interface DeviceCardProps extends HTMLAttributes<HTMLDivElement> {
  device: Device;
  index: number;
}

export default function DeviceCard({
  device,
  index,
  ...props
}: DeviceCardProps) {
  const { cart, setUserCart } = useContext(CartContext);
  const isInCart = cart.some(
    (item) =>
      item.deviceBaseId == device.deviceBaseId &&
      item.deviceUnits?.includes(device.deviceId)
  );

  const removeFromCart = () => {
    const cartCopy = cart.slice();
    const cartBaseIndex = cartCopy.findIndex(
        (item) => item.deviceBaseId === device.deviceBaseId
      ),
      cartDevice = cartCopy[cartBaseIndex];
    cartDevice.deviceUnits = cartDevice.deviceUnits?.filter(
      (du) => du != device.deviceId
    );
    !cartDevice.deviceUnits?.length
      ? setUserCart(cartCopy.toSpliced(cartBaseIndex, 1))
      : setUserCart(cartCopy);
  };

  const addToCart = () => {
    const cartCopy = cart.slice();
    let cartDevice = cartCopy.find(
      (c) => c.deviceBaseId === device.deviceBaseId
    );
    if (!cartDevice) {
      cartDevice = { deviceBaseId: device.deviceBaseId };
      cartCopy.push(cartDevice);
    }
    cartDevice.deviceUnits = [
      ...(cartDevice.deviceUnits ?? []),
      device.deviceId,
    ];
    setUserCart(cartCopy);
  };

  return (
    <Card
      {...props}
      className='flex flex-col gap-2 p-5 justify-around bg-white/70 backdrop-blur-sm hover:shadow-md'
    >
      <div className='flex items-center gap-2'>
        <span>Unit no. {index + 1}</span>
        <Badge variant={device.deviceIsAvailable ? 'default' : 'destructive'}>
          {device.deviceIsAvailable ? 'Available' : 'Not available'}
        </Badge>
      </div>
      <div className='flex items-center gap-2'>
        <Gamepad />
        <span>
          {device.deviceNumberOfControllers}{' '}
          {device.deviceNumberOfControllers > 1 ? 'controllers' : 'controller'}
        </span>
      </div>
      {!isInCart && (
        <Button
          onClick={addToCart}
          disabled={!device.deviceIsAvailable}
          className='mt-6'
        >
          Add to cart
        </Button>
      )}
      {isInCart && (
        <Button
          variant={'outline'}
          onClick={removeFromCart}
          disabled={!device.deviceIsAvailable}
          className='mt-6'
        >
          Remove from cart
        </Button>
      )}
    </Card>
  );
}
